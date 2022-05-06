package data;

import java.util.Date;

public class Notification implements Comparable<Notification> {

    protected long id;

    protected Date time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int compareTo(Notification o) {
        return this.getTime().compareTo(o.getTime());
    }
}
