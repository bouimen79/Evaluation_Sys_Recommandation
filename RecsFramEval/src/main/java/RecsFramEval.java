import algorithm.*;
import evaluation.Evaluator;
import evaluation.metrics.*;
import org.apache.commons.math3.util.Precision;
import sender.Sender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecsFramEval {

    public static void main (String [] args) throws IOException {

        String file_item = "C:\\\\Users\\\\User\\\\Desktop\\\\data\\\\Items_plista418_1m_fixed.csv";
        String file_event = "C:\\\\Users\\\\User\\\\Desktop\\\\data\\\\Events_plista418_1m_dedup.csv";
        String fileExtractFullText="C:\\Users\\User\\Desktop\\data\\items.json";


        // to check recommendation requests
        long evaluationWindowSize = 10*60*1000L;
        String evalWindowSize="10";
        int recsSizeLimit =5;


        //Algorithm algo = new RecentlyClicked();
        //Algorithm algo = new Random();
        Algorithm algo = new MostPopular();
         //Algorithm algo = new RecentlyPopular();
        //Algorithm algo = new ItemItemCF();
        //Algorithm algo = new FastSessionCoOccurrence();

        Metric clickThroughRate = new ClickThroughRate();
        clickThroughRate.setK(recsSizeLimit);
        //Metric f1 = new F1();
        //f1.setK(recsSizeLimit);
        //Metric map = new MAP();
        //map.setK(recsSizeLimit);
        //Metric ndcg = new NDCG();
        //ndcg.setK(recsSizeLimit);

        List<Metric> metricsList = new ArrayList<>();
        //metricsList.add(f1);
        //metricsList.add(map);
        //metricsList.add(ndcg);
        metricsList.add(clickThroughRate);


        float ratio =  0.8f;
        Sender.onlineStrategy(file_item,file_event,algo,recsSizeLimit,evaluationWindowSize,metricsList);
        Evaluator.printResult("src/main/java/resultEvaluation/resultonlineEval418",algo.getClass().toString(),evalWindowSize,Integer.toString(recsSizeLimit),metricsList);

        //Sender.onlineStrategyWithIa(file_item,file_event,fileExtractFullText,algo,recsSizeLimit,metricsList);
        //Evaluator.printResult("src/main/java/resultEvaluation/resultonlineEvalWithIa418",algo.getClass().toString(),evalWindowSize,Integer.toString(recsSizeLimit),metricsList);

        //Sender.offlineStrategy(file_item,file_event,algo,recsSizeLimit,ratio,metricsList);
        // Evaluator.printResult("src/main/java/resultEvaluation/resultofflineEval1677",algo.getClass().toString(),null,Integer.toString(recsSizeLimit),metricsList);


    }


}
