package data.splliting;

import data.Event;
import data.Item;
import data.Notification;

import java.util.*;

public class DataSplittingStrategy {

    public static List<Event> offlineHoldoutGetTrainingSet(List<Notification> listNotifications, float trainingRatio) {
        List<Event> spplitedList = new ArrayList<Event>();
        int total = listNotifications.size();
        int cpt=0;
        for (Notification notification:listNotifications){
                if (notification instanceof Item){
                    continue;
                }
                spplitedList.add((Event) notification);
                cpt++;
                if (cpt/total>=trainingRatio){
                    break;
                }
        }

        return spplitedList;

    }


    public static List<Event> offlineHoldoutGetTestSet(List<Notification> listNotifications, float trainingRatio) {
        List<Event> spplitedList = new ArrayList<Event>();
        int total = listNotifications.size();
        int cpt=0;
        for (Notification notification:listNotifications){
            if (notification instanceof Item){
                continue;
            }
            cpt++;
            if (cpt/total>=trainingRatio){
                spplitedList.add((Event) notification);
            }
        }

        return spplitedList;

    }

    public static List<Item> offlineHoldoutGetItemSet(List<Notification> listNotifications, float trainingRatio) {
        List<Item> spplitedList = new ArrayList<Item>();
        int total = listNotifications.size();
        int cpt=0;
        for (Notification notification:listNotifications){
            if (notification instanceof Event){
                continue;
            }
            spplitedList.add((Item) notification);
            cpt++;
            if (cpt/total>=trainingRatio){
                break;
            }
        }

        return spplitedList;

    }

    public static List<Notification> offlineRandomSplit(List<Notification> listNotifications, float trainingRatio) {

        return null;
    }

    // Sort based on the time

    public static void onLineTemporalSplit(List<Notification> listNotifications){
        Collections.sort(listNotifications);
    }

    public static Map<Long, List<Event>> getTestWindowByUser(long userID, List<Notification> listNotifications){

        Map<Long, List<Event>> testWindowByUsers = new HashMap<Long, List<Event>>();

        return testWindowByUsers;
    }
}
