package Data_Split;
import Algorithme.MostPopular;
import Algorithme.Recently_Popular;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Data_Split.Connection_Mongodb.Get_Document;
import static Data_Split.Connection_Mongodb.database;
import static Data_Split.Read_File.Sort_with_time;
import static Data_Split.Read_File.read_load_file;


public class Main_ {
    static Connection_Mongodb Connection_mongo=new Connection_Mongodb();

    public static List<Event> id_list_event = new ArrayList<>();
    public static List<Item> id_list_item = new ArrayList<>();
    public static List<Notification_Requete> not_eq_list = new ArrayList();
    public static List<Notification_Event> not_ev_list = new ArrayList();
    public static Event ev;
    public static Item It;
    public static Notification_Requete n_req;
    public static Notification_Item n_it;
    public static  Notification_Event n_ev;


    public static void main(String[] args) throws ParseException {
        Connection_mongo.connection_mongo("data_sys_rec");
        List<Object> Session=new ArrayList();
        Session=read_load_file();
        //Session_Sorted -------------->Sort List Of item_Event by time
        List<Object>session_sorted=Sort_with_time(Session);


        //SysRec=Objet Sys_Recommandation(c'est un classe)
        //List Choix d'utilisateur .add(BPR,POP....algorithme de recommandation)
        //hado rah ywaliw fal main

        for (int i=0;i<session_sorted.size();i++){
            if(session_sorted.get(i) instanceof Item){
                It = new Item(((Item) session_sorted.get(i)).id_item, ((Item) session_sorted.get(i)).created_at, ((Item) session_sorted.get(i)).Url, ((Item) session_sorted.get(i)).category, ((Item) session_sorted.get(i)).text, ((Item) session_sorted.get(i)).title);
                id_list_item.add(It);
                //get documeny by id_Item from mongodb
                Document doc_it=Get_Document(database,"ItemID",((Item) session_sorted.get(i)).id_item,"Item");
                if (doc_it == null) {
                    System.out.println("No results found.");
                } else {
                    //------------------------
                    String CreatAt= ((doc_it.toString().split(",")[2]).split("=")[1]);
                    Date Creat_At_=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(CreatAt);
                    Long Item_id= Long.valueOf(((doc_it.toString().split(",")[3]).split("=")[1]));
                    String Url = ((doc_it.toString().split(",")[5]).split("=")[1]);
                    String title = ((doc_it.toString().split(",")[6]).split("=")[1]);
                    long category = Long.valueOf(((doc_it.toString().split(",")[7]).split("=")[1]));
                    String text = (((doc_it.toString().split(",")[8]).split("=")[1]));
                   ///Notification_Creation_Item
                    n_it=new Notification_Item(Item_id,Creat_At_,Url,category,text,title);

                }

            }else{
                if (session_sorted.get(i) instanceof Event) {
                    ev = new Event(((Event) session_sorted.get(i)).id_event, ((Event) session_sorted.get(i)).id_item, ((Event) session_sorted.get(i)).id_user, ((Event) session_sorted.get(i)).time);
                    System.out.println("ev" + ev);
                    id_list_event.add(ev);
                    //get documeny by id_Event from mongodb
                    Document doc=Get_Document(database,"Id_event",((Event) session_sorted.get(i)).id_event,"Event");
                    if (doc == null) {
                        System.out.println("No results found.");
                    } else {

                        String id_event = ((doc.toString().split(",")[1]).split("=")[1]);
                        Long Item_id = Long.valueOf(((doc.toString().split(",")[3]).split("=")[1]));
                        Long User_id = Long.valueOf(((doc.toString().split(",")[4]).split("=")[1]));
                        String time = ((doc.toString().split(",")[5]).split("=")[1]);
                        Date time_event = new Date((Long.parseLong(time)));
                        Long geo = Long.valueOf(((doc.toString().split(",")[6]).split("=")[1]));
                        Long device_type = Long.valueOf((((doc.toString().split(",")[7]).split("=")[1])).split("}")[0]);
                        //Notification_event
                        n_ev = new Notification_Event(id_event, Item_id, User_id, time_event, geo, device_type);
                        //demande_Requete
                        n_req = new Notification_Requete(id_event, User_id, time_event, geo, device_type);
                        not_eq_list.add(n_req);

                    }
                    //demande_req.envoyer() [List item recommandé=SysRec.get_Notification_Requete(demande_Requete)]
                    //demande_req.evaluer()
                    //event.envoyer() [SysRec.get_Notification_Event(eventNotification)]
                }
            }
        }
        MostPopular m = new MostPopular();
        Recently_Popular r = new Recently_Popular() ;
        System.out.println("iiiiiiiii-----------"+id_list_event);
        r.handleEventNotification(id_list_event);
        System.out.println(r.getRecommendations(not_eq_list));

    }
}
