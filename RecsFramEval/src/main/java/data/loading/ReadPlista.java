package data.loading;

import data.Event;
import data.Item;
import data.Notification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReadPlista {


    public static List<Notification> loadPlistaData(String fileItem, String fileEvent) {
        Event event;
        Item item;
        List<Notification> listNotifications = new ArrayList<Notification>();
        long event_id =0;
        long anonymousUserIDs=0;
        try {
            BufferedReader br_item = new BufferedReader(new FileReader(fileItem));

            String line = "";
            String line_event = "";


            Boolean first_item = true;
            Boolean first_event = true;

            while ((line = br_item.readLine()) != null) {
                if (first_item) {
                    first_item = false;
                } else {
                    item = new Item(line);
                    listNotifications.add(item);
                }
            }

            br_item.close();

            //-------------------------------------------------------
            BufferedReader br_event = new BufferedReader(new FileReader(fileEvent));
            while ((line_event = br_event.readLine()) != null) {
                if (first_event) {
                    first_event = false;
                } else {
                    event = new Event(line_event);
                    event.setId(event_id);
                    /*if (event.getId_user() == 0) {
                        anonymousUserIDs = anonymousUserIDs - 1;
                        event.setId_user(anonymousUserIDs);
                    }*/
                    listNotifications.add(event);
                    event_id++;

                }
            }
            br_event.close();

            return listNotifications;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}