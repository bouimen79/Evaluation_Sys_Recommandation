package algorithm;

import data.Event;
import data.Item;
import data.Request;

import util.Util;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;

public class MostPopular extends Algorithm{

    protected Long2IntOpenHashMap Event_Counter = new Long2IntOpenHashMap();
    protected Long2IntOpenHashMap item_Counter = new Long2IntOpenHashMap();
    @Override
    public  void handleEventNotification(Event event) {
            Event_Counter.addTo(event.getId_item(), 1);
    }


    @Override
    public void handleItemUpdate(Item item) {
           // item_Counter.addTo(item.getId_item(), 1);

//        System.out.println( "item counter "+item_Counter);

    }



    @Override
    public LongArrayList getRecommendations(Request request){
        //return the items sorted by their click count

        Long2IntOpenHashMap results = new Long2IntOpenHashMap(Event_Counter);

        return (LongArrayList) Util.sortByValueAndGetKeys(results, false, new LongArrayList(),
                request.getLimit());
    }


}
