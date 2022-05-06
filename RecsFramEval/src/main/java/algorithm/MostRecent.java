package algorithm;


import data.Event;
import data.Item;
import data.Request;
import it.unimi.dsi.fastutil.longs.LongArrayList;

/**
 * An algorithm that recommends the articles in the order of their
 * publication/updating (newest to oldest)
 * 
 * @author Mozhgan
 *
 */
public class MostRecent extends Algorithm {
	// an ordered list of the most recently published/updated news articles
	private LongArrayList mostRecentItems = new LongArrayList();

	@Override
	public void handleEventNotification(Event eventData) {

	}

	@Override
	public LongArrayList getRecommendations(Request currentRequest) {

		LongArrayList results = new LongArrayList(mostRecentItems);


		if (results.size()>currentRequest.getLimit()) {
			return (LongArrayList) results.subList(0, currentRequest.getLimit() - 1);
		}else {
			return mostRecentItems;
		}
		
		// return mostRecentItems.subList(0, currentRequest.getLimit()-1);

	}

	@Override
	public void handleItemUpdate(Item item) {
		if (mostRecentItems.contains(item.getId())) {
			mostRecentItems.rem(item.getId());
		}
		mostRecentItems.add(0, item.getId());

	}


}
