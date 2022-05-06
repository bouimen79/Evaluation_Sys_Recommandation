package algorithm;

import data.Event;
import data.Request;

import util.Util;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.LinkedList;
import java.util.List;
public class RecentlyPopular extends MostPopular{

    //The time up until which clicks are counted for the "popularity"
    private int filterTime = 24*60*60*1000;//default = 1 day (millisecond)
    //a buffer of recent transactions so that click counts can be decreased when they "leave" the time window
    private List<Event> recentTransactions = new LinkedList<Event>();

    @Override
    public void handleEventNotification(Event event) {
        //train the basic "most popular" implementation
        super.handleEventNotification(event);
        //add the training click to the transaction buffer
        recentTransactions.add(event);
        //filter all clicks from the transaction buffer that have left the time window (e.g. when they occurred more than 1h ago)
        while((recentTransactions.get(recentTransactions.size()-1).getTime().getTime()- recentTransactions.get(0).getTime().getTime())>filterTime){
            //if the transactions get old, remove them from the list and decrease the popularity count
            if(recentTransactions.get(0)!=null){
                Event_Counter.addTo(recentTransactions.get(0).getId_item(), -1);
            }
            recentTransactions.remove(0);
        }
        //for the actual recommending: let the super implementation do the work
    }




    /**
     * Sets the time up until which clicks are counted for the "popularity"
     * @param filterTime -
     */
    public void setFilterTime(int filterTime) {
        this.filterTime = filterTime;
    }

}
