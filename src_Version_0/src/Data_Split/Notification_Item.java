package Data_Split;

import java.util.Date;

public class Notification_Item {
    long id_item;
    Date created_at;
    String Url;
    long category;
    String text;
    String title;
    Item item;
    public Notification_Item(Item item){
        this.item=item;
    }
    public Notification_Item(long id_item, Date created_at, String Url, long category, String text, String title){
        this.id_item=id_item;
        this.created_at=created_at;
        this.Url=Url;
        this.category=category;
        this.text=text;
        this.title=title;

    }
}

