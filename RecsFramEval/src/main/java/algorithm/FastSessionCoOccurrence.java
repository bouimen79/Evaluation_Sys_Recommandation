package algorithm;

import java.util.*;
import java.util.Map.Entry;

import data.Event;
import data.Item;
import data.Request;
import data.session.SessionExtractor;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import util.Util;

/**
 * Counts the co-occurrences for two item clicks in one session.
 * 
 * @author Mozhgan
 *
 */
public class FastSessionCoOccurrence extends Algorithm {
	// a map of co-occurrences between item ids. the key type is string to be
	// more general
	protected Map<String, Object2IntOpenHashMap<String>> coOcurrenceMap = new Object2ObjectOpenHashMap<>();
	// should the whole current sessions be considered or just the current item
	protected boolean wholeSession = false;
	// should we only count co-occurrences in the last N clicks?
	protected boolean buffer = false;
	// how large should the click buffer be?
	protected int bufferSize = 10000;
	// the buffer of co-occurrences from the last N clicks
	protected LinkedList<Entry<String, String>> ringBuffer = new LinkedList<>();

	SessionExtractor sessionExtractor = new SessionExtractor();

	List<Event> currenctUserSession;

	@Override
	public void handleEventNotification(Event eventData) {
		currenctUserSession = Collections
				.unmodifiableList(new ArrayList<>(sessionExtractor.addClick(eventData)));

		updateMap(currenctUserSession);
	}


	@Override
	public LongArrayList getRecommendations(Request currentRequest) {
		// create a list of scores for each item, which is the sum of all
		// co-occurrence counts
		Map<String, Double> combineWeights = new Object2DoubleOpenHashMap<String>();
		// depending on if we are supposed to use the whole session or not,
		// this for loop only does one iteration on the last element or it
		// iterates over
		// all click in the current user sesssion
		List<List<Event>> sessionList = SessionExtractor.sessions.get(currentRequest.getId_user());

		List<Event> lastSession;

		Event e;

		if (sessionList == null) {
			lastSession = new ArrayList<Event>();
			e = new Event();
			e.setId_user(currentRequest.getId_user());
			e.setId_item(currentRequest.getCurrentItemId());
			e.setGeoUser(currentRequest.getGeoUser());
			e.setDevice_type(currentRequest.getDevice_type());
			e.setTime(currentRequest.getTime());
			lastSession.add(e);
		} else {
			lastSession = sessionList.get(sessionList.size() - 1);
		}

		for (int i = wholeSession ? 0 : (lastSession.size() - 1); i < lastSession.size(); i++) {
			Event click = lastSession.get(i);
			// if the inner map is empty, we cannot recommend anything
			if (!coOcurrenceMap.containsKey(getCoOccurrenceKey(click))) {
				continue;
			}
			// get the inner map of items that this item has co-occurred with
			Map<String, Integer> m = coOcurrenceMap.get(getCoOccurrenceKey(click));
			for (Entry<String, Integer> entry : m.entrySet()) {
				// sum up the co-occurrence weights for each item
				Double currVal = combineWeights.get(entry.getKey());
				if (currVal == null) {
					currVal = 0d;
				}
				// -------------------------------------------
				
				/*if (!recommendableItems.contains(Long.parseLong(entry.getKey()))) {
					continue;
				}*/
				// -------------------------------------------
				combineWeights.put(entry.getKey(), currVal + entry.getValue());
				
			}
		}

		// sort the weighted sums
		Map<String, Double> sortedKeys = Util.sortByValue(combineWeights, false);
		return generateResultList(sortedKeys,currentRequest.getLimit());
	}

	@Override
	public void handleItemUpdate(Item item) {
		// TODO Auto-generated method stub

	}

	/**
	 * Generate a result list from a map of summed up co-occurence counts. In this
	 * case, we are just parsing the string back to a long (item id). In future
	 * implementations, this method can be overridden and do more intersting stuff.
	 * 
	 * @param sortedKeys -
	 * @param maxRecListSize  -
	 * @return a sorted recommendation list
	 */
	protected LongArrayList generateResultList(Map<String, Double> sortedKeys,int maxRecListSize) {
		// remap all item ids back to the actual Item objects
		LongArrayList sortedItems = new LongArrayList();
		int sizeList = 0;
		for (String itemId : sortedKeys.keySet()) {
			sortedItems.add(Long.parseLong(itemId));
			sizeList++;
			if (sizeList >= maxRecListSize) {
				break;
			}
			
		}
		return sortedItems;
	}

	/**
	 * Updates the co-occurrence map based on the current click of the user
	 * 
	 * @param session -
	 */
	protected void updateMap(List<Event> session) {
		
		for (int i = 0; i < session.size() - 1; i++) {
			if (getCoOccurrenceKey(session.get(i)) == getCoOccurrenceKey(session.get(session.size() - 1))) {
				// ignore co-occurrences of items with themselves
				continue;
			}
			// we add one entry to the map with the "correct" order (time-based)
			addTuple(session.get(i), session.get(session.size() - 1));
			// map with opposite order
			addTuple(session.get(session.size() - 1), session.get(i));
		}
		// if we are supposed to only look at the last N clicks,
		// this part of the method checks the buffer and cleans out old clicks.
		if (buffer) {
			while (ringBuffer.size() > bufferSize) {
				// adjust map
				Entry<String, String> first = ringBuffer.poll();
				Object2IntOpenHashMap<String> map = coOcurrenceMap.get(first.getKey());
				map.addTo(first.getValue(), -1);
				map.remove(first.getValue(), 0);// remove if 0
			}
		}
	}

	/**
	 * Adds one co-occurrence tuple to the map
	 * 
	 * @param a A transaction
	 * @param b Another transaction that the first one occurred with in one session
	 */
	private void addTuple(Event a, Event b) {
		String keyA = getCoOccurrenceKey(a);
		String keyB = getCoOccurrenceKey(b);
		if (buffer) {
			ringBuffer.add(new AbstractMap.SimpleEntry<String, String>(keyA, keyB));
		}
		// check if the inner map exists
		if (!coOcurrenceMap.containsKey(keyA)) {
			coOcurrenceMap.put(keyA, new Object2IntOpenHashMap<String>());
		}
		Object2IntOpenHashMap<String> map = coOcurrenceMap.get(keyA);
		// check if the item in the inner map exists
		map.addTo(keyB, 1);
	}

	protected String getCoOccurrenceKey(Event t) {
		return "" + t.getId_item();
	}

	/**
	 * Consider the whole current session or just the current click
	 * 
	 * @param wholeSession -
	 */
	public void setWholeSession(boolean wholeSession) {
		this.wholeSession = wholeSession;
	}

	/**
	 * Should we only look at the N most recent co-occurrences?
	 * 
	 * @param buffer -
	 */
	void setBuffer(boolean buffer) {
		this.buffer = buffer;
	}

	/**
	 * Should we only look at the N most recent co-occurrences? If so, this method
	 * sets how many.
	 * 
	 * @param bufferSize -
	 */
	void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}
