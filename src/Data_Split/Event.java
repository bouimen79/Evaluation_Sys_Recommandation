package Data_Split;

import java.util.Date;

public class Event {
	long geoUser;
	long id_event;
	    public long id_item;
	    long id_user;
	    public Date time;
	    String action;
		long device_type;
	   /* public Event(long id_event,Date time){
	        this.id_event=id_event;
	        this.time=time;
	    }*/

	public Event(long id_event,long id_item ,long id_user,Date time){
		this.id_event=id_event;
		this.id_item=id_item;
		this.id_user=id_user;
		this.time=time;
	}
/*
	//actionType,ItemID,UserId,TimeStamp,geoUser,deviceType
		public Event(String action,long id_item,long id_user,Date time,long geoUser,long device_type){
			this.action=action;
			this.id_item=id_item;
			this.id_user=id_user;
			this.time=time;
			this.geoUser=geoUser;
			this.device_type=device_type;
		}
		//sauf id_item;
	public Event(String action,long id_user,Date time,long geoUser,long device_type){
		this.action=action;
		this.id_user=id_user;
		this.time=time;
		this.geoUser=geoUser;
		this.device_type=device_type;
	}
*/
	    public String toString(){
	        return id_event+","+id_item+","+id_user+","+time;
	    }
}
