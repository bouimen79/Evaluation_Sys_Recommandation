package Algorithme;

import Data_Split.Event;
import Data_Split.Item;
import Data_Split.Notification_Requete;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.*;
public abstract class Algorithm {


    public abstract void handleEventNotification(List<Event> id_event);

    public abstract void handleItemUpdate(List<Item> item);

    public abstract LongArrayList getRecommendations(List<Notification_Requete> id_event);



}
