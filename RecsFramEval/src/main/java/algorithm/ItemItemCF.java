package algorithm;

import util.Util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.googlecode.javaewah.EWAHCompressedBitmap;
import data.Event;
import data.Item;
import data.Request;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

/**
 * Implements a traditional Item to Item collaborative filtering scheme. I.e.,
 * the similarity between to items is higher the more overlap the two items have
 * in terms of the users that clicked on them. The exact overlap similarity
 * metric used is the Jaccard measure.
 * 
 * @author MJ
 *
 */
public class ItemItemCF extends Algorithm {
	// a map between an item id and the bitmap representation of users that
	// clicked on this item
	private Map<Long, EWAHCompressedBitmap> itemClickMap = new Long2ObjectOpenHashMap<>();
	// a map for user ids (EWAHCompressedBitmap can only hold INT, but user ids
	// are LONG)
	private Map<Long, Integer> userMap = new Long2IntOpenHashMap();
	// indicates which user id to map to next
	private int currentUserCounter = 0;
	// if desired, we buffer the n most recent clicks here (storage format
	// <user, item>)
	private ObjectLinkedOpenHashSet<Entry<Long, Long>> ringBuffer = new ObjectLinkedOpenHashSet<>();
	// a map between users and items. For easy lookup so that not all items have
	// to be compared with each other.
	// in case they have no overlap at all.
	private Map<Integer, Set<Long>> userItemMap = new Int2ObjectOpenHashMap<Set<Long>>();

	// should we take only the N most recent clicks into account?
	private boolean buffer = false;
	// if so, how many?
	private int bufferSize = 1000;
	// for the final recommendation list scores, should we take only the
	// similarity with the currently
	// clicked item or the whole current user session into account.
	private boolean wholeSession = false;

	@Override
	public void handleEventNotification(Event eventData) {

		// update the click maps of each item
		updateMap(eventData.getId_user(), eventData.getId_item());
		if (buffer) {
			// update the buffer so that these clicks are removed later from the
			// click maps
			SimpleEntry<Long, Long> tuple = new SimpleEntry<Long, Long>(eventData.getId_user(),
					eventData.getId_item());
			// if the click is already contained, remove it
			if (ringBuffer.contains(tuple)) {
				ringBuffer.remove(tuple);
			}
			// in any case, add the click at the end in the buffer
			ringBuffer.add(tuple);
		}

		// if we are using only the most recent N clicks, remove "old" click
		// from the map, here.
		if (buffer) {
			ObjectListIterator<Entry<Long, Long>> iterator = ringBuffer.iterator();
			while (buffer && ringBuffer.size() > bufferSize) {
				Entry<Long, Long> next = iterator.next();
				itemClickMap.get(next.getValue()).clear(userMap.get(next.getKey()));
				userItemMap.get(userMap.get(next.getKey())).remove(next.getValue());
				iterator.remove();
			}
		}

	}

	@Override
	public void handleItemUpdate(Item item) {

	}


	/**
	 * add a click to the click maps
	 * 
	 * @param user the user that clicked
	 * @param item the item that was clicked
	 */
	private void updateMap(long user, long item) {
		// if the user is unknown, generate an integer ID for the user
		// because EWAHCompressedBitmap cannot store long.
		if (!userMap.containsKey(user)) {
			userMap.put(user, ++currentUserCounter);
			userItemMap.put(currentUserCounter, new LongOpenHashSet());
		}
		// extract the integer id the user
		int userInt = userMap.get(user);
		// extract the bitmap of clicks for this item
		EWAHCompressedBitmap itemClicks = itemClickMap.get(item);
		// if the bitmap does not exist, create it
		if (itemClicks == null) {
			itemClicks = new EWAHCompressedBitmap();
			itemClickMap.put(item, itemClicks);
		}
		// put the current click in the click map
		itemClicks.set(userInt);
		// also update the reverse lookup
		userItemMap.get(userInt).add(item);
	}

	@Override
	public LongArrayList getRecommendations(Request currentRequest) {

		// generate a list of item ids, from the current session, with which we
		// want to compare
		LongOpenHashSet itemIDsToCompare = new LongOpenHashSet();
		if (wholeSession) {

			/*List<List<Notification>> sessionList = SessionExtractor.sessions.get(currentRequest.getUserId());
			List<EventNotification> lastSession = sessionList.get(sessionList.size() - 1);

			// either all items from the current session
			for (EventNotification sessionTransaction : lastSession) {
				itemIDsToCompare.add(sessionTransaction.getItemID());
			}*/
		} else {
			// or only the last item
			//get current item (context)
			itemIDsToCompare.add(currentRequest.getCurrentItemId());
		}

		// make a list of all candidate items
		LongOpenHashSet items = new LongOpenHashSet();
		// from the reverse lookup, extract all items that have at least
		// one user in common with the item(s) from the current sessions
		for (long itemToCompare : itemIDsToCompare) {
			EWAHCompressedBitmap currentItemClicks = itemClickMap.get(itemToCompare);
			if (currentItemClicks != null) {
				for (Integer user : currentItemClicks) {
					items.addAll(userItemMap.get(user));
				}
			}
		}

		// remove the current item from the candidates
		items.remove(currentRequest.getCurrentItemId());

		// create a map of scores for each recommendation candidate item
		Long2DoubleOpenHashMap scores = new Long2DoubleOpenHashMap();
		// iterate over the candidate items
		for (LongIterator iterator = items.iterator(); iterator.hasNext();) {
			long item = iterator.nextLong();

			// -------------------------------------------
			/*if (!recommendableItems.contains(item)) {
				continue;
			}*/
			// -------------------------------------------

			EWAHCompressedBitmap entry = itemClickMap.get(item);
			double score = 0;
			// compare the actual click maps and sum up the scores
			for (long itemId : itemIDsToCompare) {

				EWAHCompressedBitmap bitmap = itemClickMap.get(itemId);
				if (bitmap != null) {
					score += similarity(itemClickMap.get(itemId), entry);
				}
			}
			// if the score is not 0, add it to the score map
			if (score > 0) {
				scores.put(item, score);
			}
		}
		// return candidates ordered by score
		return (LongArrayList) Util.sortByValueAndGetKeys(scores, false, new LongArrayList(), currentRequest.getLimit());

	}

	/**
	 * Calculates the Jaccard similarity of two items in terms of the sets of users
	 * that clicked on them respectively
	 * 
	 * @param itemClicks         Users that clicked on the first item
	 * @param anotherItemsClicks Users that clicked on the other item
	 * @return the Jaccard similarity
	 */
	protected double similarity(EWAHCompressedBitmap itemClicks, EWAHCompressedBitmap anotherItemsClicks) {
		int intersection = itemClicks.andCardinality(anotherItemsClicks);
		if (intersection == 0) {
			// if the intersection is 0 -> return 0
			return 0;
		}
		// otherwise calculate the jaccard
		int union = itemClicks.orCardinality(anotherItemsClicks);
		return intersection * 1d / union * 1d;
	}

	/**
	 * Should we consider the similarity with all items from the current session or
	 * just the item that was clicked last?
	 * 
	 * @param wholeSession -
	 */
	public void setWholeSession(boolean wholeSession) {
		this.wholeSession = wholeSession;
	}

	/**
	 * Should we only consider the N most recent clicks?
	 * 
	 * @param buffer -
	 */
	void setBuffer(boolean buffer) {
		this.buffer = buffer;
	}

	/**
	 * Should we only consider the N most recent clicks? If so, how many?
	 * 
	 * @param bufferSize -
	 */
	void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}


}
