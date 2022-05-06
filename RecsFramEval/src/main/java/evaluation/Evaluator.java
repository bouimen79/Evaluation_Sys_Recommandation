package evaluation;

import data.Event;
import data.Notification;
import evaluation.metrics.Metric;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Evaluator {


    public static LongOpenHashSet getTestWindowForUser(Event currentTransaction, List<Notification> trainingWindow,
                                                       int currentTransactionIndex, long evaluationWindowSize) {

        // test window of user
        //the ground truth of one user

        LongOpenHashSet listItems = new LongOpenHashSet();

        Event transaction;
        //long requestTimeStamp = currentTransaction.getTime().getTime();
        long testWindowTime = currentTransaction.getTime().getTime()+evaluationWindowSize;
        //System.out.print("testWindowTime : "+testWindowTime +"  --- ");
        //check items in test window (test window starts from last training index)
        for (int i = currentTransactionIndex; i < trainingWindow.size(); i++) {
            if (trainingWindow.get(i) instanceof Event) {
                transaction = (Event) trainingWindow.get(i);
                //System.out.println("transaction time : "+transaction.getTime().getTime());
                if (transaction.getTime().getTime() >= testWindowTime) {
                    break;
                }
                if (transaction.getId_user() != currentTransaction.getId_user()) {
                    continue;
                }
                listItems.add(transaction.getId_item());
            }else {
                continue;
            }
        }
        return listItems;
    }

    public static void printResult(String paths,String nameAlgo,String evalWindowSize,String recsizeLimit,List<Metric> metrics) throws IOException {
        //passing file instance in filewriter
        File fileName= new File(paths);
        BufferedWriter wr = new BufferedWriter(new FileWriter(fileName,true));
        //calling writer.write() method with the string
        wr.write("\n"+nameAlgo+"\n");
        wr.write(StringUtils.rightPad("EvaluationWindowSize:", 20, " ")+"\t"+evalWindowSize+"min"+"\n");
        wr.write(StringUtils.rightPad("recSizeLimits:", 20, " ")+"\t"+recsizeLimit+"\n");
        // print evaluation results extracted from metric classes
        DecimalFormat df = new DecimalFormat("0.0000000");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        System.out.println("//=================overall results=========================//");
        System.out.println();
        //String result ="//=================overall results=========================//";
        for (Metric m : metrics) {
            wr.write(StringUtils.rightPad(m.getName(), 20, " ") + "\t" + df.format(m.getResults())+"\n");
            //return result+StringUtils.rightPad(m.getName(), 20, " ") + "\t" + df.format(m.getResults());
            System.out.println(StringUtils.rightPad(m.getName(), 20, " ") + "\t" + df.format(m.getResults()));
            //flushing the writer
            wr.flush();
        }
        wr.write("");
        System.out.println();
        //return result;
        wr.close();
    }



    }
