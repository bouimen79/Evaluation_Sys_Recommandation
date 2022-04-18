package Algorithme;

import Data_Split.Event;
import Data_Split.Item;
import Data_Split.Notification_Requete;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MostPopular extends Algorithm{
    protected Long2IntOpenHashMap Event_Counter = new Long2IntOpenHashMap();
    @Override
    public  void handleEventNotification(List<Event> id_event) {

        for (Event e: id_event) {
            Event_Counter.addTo(e.id_item, 1);
        }
        System.out.println( "event counter "+Event_Counter);
    }


    @Override
    public void handleItemUpdate(List<Item> item) {
        for (Item i: item) {
            Event_Counter.addTo(i.id_item, 1);
        }
        //System.out.println( "event counter "+Event_Counter);

    }
    public static <K, V extends Comparable<? super V>> List<K> sortByValueAndGetKeys(Map<K, V> map,
                                                                                     final boolean ascending, List<K> output) {
        List<Map.Entry<K, V>> list = new ObjectArrayList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (ascending) {
                    return (o1.getValue()).compareTo(o2.getValue());
                } else {
                    return (o2.getValue()).compareTo(o1.getValue());
                }

            }
        });
        for (Map.Entry<K, V> entry : list) {
            output.add(entry.getKey());
        }
        return output;

    }


    @Override
    public LongArrayList getRecommendations(List<Notification_Requete> id_event){
        //return the items sorted by their click count
        System.out.println( "event counter "+Event_Counter);
        return (LongArrayList) sortByValueAndGetKeys(Event_Counter, false, new LongArrayList());

    }


}
