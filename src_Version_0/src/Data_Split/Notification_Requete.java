package Data_Split;

import java.util.Date;

public class Notification_Requete {
    String id_event;

    long id_user;
    long geo;
    long device_type;
    Date time;
    public Notification_Requete(String id_event , long id_user, Date time, long geo, long device_type){
        this.id_event=id_event;
        this.id_user=id_user;
        this.time=time;
        this.geo=geo;
        this.device_type=device_type;


    }
    public String toString(){
        return id_event+","+id_user+","+time+","+geo+","+device_type;
    }
}
