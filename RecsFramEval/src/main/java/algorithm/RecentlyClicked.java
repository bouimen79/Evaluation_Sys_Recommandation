package algorithm;

import data.Event;
import data.Item;
import data.Request;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import util.Util;

public class RecentlyClicked extends Algorithm{
    public LongLinkedOpenHashSet clickedItems = new LongLinkedOpenHashSet();

    @Override
    public void handleEventNotification(Event event) {
            clickedItems.addAndMoveToFirst(event.getId_item());
    }

    @Override
    public void handleItemUpdate(Item item) {

    }

    @Override
    public LongArrayList getRecommendations(Request request) {

        LongArrayList recs = new LongArrayList();
        recs.addAll(clickedItems);

        if (recs.size()>request.getLimit()) {
            return new LongArrayList(recs.subList(0, request.getLimit() - 1));

        }else {

            return recs;
        }
    }
}
