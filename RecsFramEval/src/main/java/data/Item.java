package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Item extends Notification{

    private long Domain;
    private String Url;
    private long category;
    private String text;
    private String title;
    private String type;

    public Item(String line) throws ParseException {
        String split_file[] = line.split(",");
        this.id = Long.valueOf(split_file[3]);
        this.time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(split_file[2]);
        this.Domain = Long.parseLong(split_file[1]);
        this.Url = split_file[5];
        this.category = Long.parseLong(split_file[7]);
        this.text = split_file[8];
        this.title = split_file[6];
    }


    public long getDomain() {
        return Domain;
    }

    public void setDomain(long domain) {
        Domain = domain;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
