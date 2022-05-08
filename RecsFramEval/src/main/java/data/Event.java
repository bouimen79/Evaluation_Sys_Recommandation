package data;

import java.text.ParseException;
import java.util.Date;

public class Event extends Notification{

    private  long geoUser;
    private long id_item;
    private  long id_user;
    private  String action;
    private	long device_type;

    public Event(){

    }
    public Event(String line) throws ParseException {
        String split_file_event[] = line.split(",");
        this.action= split_file_event[0];
        this.id_item = Long.parseLong(split_file_event[1]);
        this.id_user = Long.parseLong(split_file_event[2]);
        //SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        this.time = new Date((Long.parseLong(split_file_event[3])));
        this.geoUser= Long.parseLong(split_file_event[4]);
        this.device_type= Long.parseLong(split_file_event[5]);

    }

    public long getGeoUser() {
        return geoUser;
    }

    public void setGeoUser(long geoUser) {
        this.geoUser = geoUser;
    }

    public long getId_item() {
        return id_item;
    }

    public void setId_item(long id_item) {
        this.id_item = id_item;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getDevice_type() {
        return device_type;
    }

    public void setDevice_type(long device_type) {
        this.device_type = device_type;
    }
}
