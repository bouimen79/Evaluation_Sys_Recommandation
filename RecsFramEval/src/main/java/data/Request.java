package data;


import java.text.ParseException;
import java.util.Date;

public class Request {

    private long id;
    private long id_user;
    private long geoUser;
    private long device_type;
    private Date time;
    private long currentItemId;

    private int limit;


    public Request(){

    }

    public Request(Event event) {
        this.id = event.getId();
        String time = "";
        this.time= event.getTime();
        this.id_user = event.getId_user();
        this.geoUser= event.getGeoUser();
        this.device_type= event.getDevice_type();
        this.currentItemId = event.getId_item();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public long getGeoUser() {
        return geoUser;
    }

    public void setGeoUser(long geoUser) {
        this.geoUser = geoUser;
    }

    public long getDevice_type() {
        return device_type;
    }

    public void setDevice_type(long device_type) {
        this.device_type = device_type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getCurrentItemId() {
        return currentItemId;
    }

    public void setCurrentItemId(long currentItemId) {
        this.currentItemId = currentItemId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
