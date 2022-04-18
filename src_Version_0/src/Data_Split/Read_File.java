package Data_Split;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Data_Split.Connection_Mongodb.ADD_Document_Event;
import static Data_Split.Connection_Mongodb.ADD_Document_Item;

public class Read_File {


    public static List<Object> read_load_file(){
        String file_item = "C:\\Users\\User\\Desktop\\data\\Items_plista418_1m_fixed.csv";
        String file_event = "C:\\Users\\User\\Desktop\\data\\Events_plista418_1m_dedup.csv";
        String file_event_test="C:\\Users\\User\\Desktop\\data\\test_Event.txt";
        Item i;
        Event ev;
        List<Object> session = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file_item));
            BufferedReader br2 = new BufferedReader(new FileReader(file_event_test));
            String split_by=",";
            String line = "";
            String line2 = "";
            long event_id = 0;
            Boolean first_Item=true;
            while (((line = br.readLine()) != null) ) {
                if(first_Item) {
                    first_Item=false;
                }else {
                    String split_file[] = line.split(split_by);
                    try {
                        long id_item = Long.parseLong(split_file[3].trim());
                        Date created_at = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(split_file[2]);
                        String created_At=split_file[2];

                        String domain = split_file[1];
                        String recom = split_file[4];
                        String Url = split_file[5];
                        String category = split_file[7];
                        String text = split_file[8];
                        String title = split_file[6];

                        i = new Item(id_item, created_at);
                        //add document to mongdb
                        ADD_Document_Item(domain,created_At,id_item,recom,Url,title,category,text);
                        session.add(i);
                    } catch (Exception e) {
                    }
                }
            }
            Boolean first_Event=true;
            while(( line2=br2.readLine())!=null) {
                if (first_Event) {
                    first_Event = false;
                } else {
                    try {

                        String split_file_Event[] = line2.split(split_by);
                        String action_type=split_file_Event[0];
                        long item_id_Event = Long.parseLong(split_file_Event[1]);
                        long user_id_Event = Long.parseLong(split_file_Event[2]);
                        Date time_ = new Date((Long.parseLong(split_file_Event[3])));
                        String time=split_file_Event[3];
                        long geoUser=Long.parseLong(split_file_Event[4]);
                        long device_type=Long.parseLong(split_file_Event[5]);
                        event_id++;
                        ev = new Event(event_id,item_id_Event,user_id_Event ,time_);
                        // add document to mongoDb
                        ADD_Document_Event(event_id,action_type,item_id_Event,user_id_Event,time,geoUser,device_type);
                        session.add(ev);

                    } catch (NumberFormatException e) {
                        System.out.println("Error" + e);
                        e.printStackTrace();
                    }
                }
            }
            br.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }
    public static List<Object> Sort_with_time(List<Object> a) {
        a.sort(((o1, o2) -> {
            if (o1 instanceof Item && o2 instanceof Item){
                Item g1 = (Item) o1;
                Item g2 = (Item) o2;
                return g1.created_at.compareTo(g2.created_at);
            }else if((o1 instanceof Event && o2 instanceof Event)) {
                Event g1 = (Event) o1;
                Event g2 = (Event) o2;
                return g1.time.compareTo(g2.time);
            }else if(o1 instanceof Event&& o2 instanceof Item){
                Event g1 = (Event) o1;
                Item g2 = (Item) o2;
                return g1.time.compareTo(g2.created_at);
            }else{
                Item g1 = (Item) o1;
                Event g2 = (Event) o2;
                return g1.created_at.compareTo(g2.time);
            }
        }));
        for(Object Object:a) {
            //System.out.println(a);
            return a;
        }
        return a;
    }

}
