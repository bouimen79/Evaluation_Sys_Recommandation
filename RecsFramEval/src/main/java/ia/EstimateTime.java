package ia;

import java.util.StringTokenizer;

public class EstimateTime {
    static int WPM = 265;
    public static int countWordsUsingStringTokenizer(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(sentence);
        return tokens.countTokens();
    }
    public static long estimateTime(String input){
        int r=(countWordsUsingStringTokenizer(input)/WPM);
        if(r==0){
            r=1;
        }
        System.out.println("Time for read is: "+r+" min");
        long result=r*60*1000L;
        return result;
    }
}

