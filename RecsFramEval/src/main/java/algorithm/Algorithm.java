package algorithm;

import data.Event;
import data.Item;
import data.Request;
import it.unimi.dsi.fastutil.longs.LongArrayList;

public abstract class Algorithm {


    public abstract void handleEventNotification(Event event);

    public abstract void handleItemUpdate(Item item);

    public abstract LongArrayList getRecommendations(Request request);




}
