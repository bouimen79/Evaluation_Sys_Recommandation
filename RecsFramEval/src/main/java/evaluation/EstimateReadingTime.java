package evaluation;

import java.util.StringTokenizer;

public class EstimateReadingTime {
    static int WPM = 265;
    public static int countWordsUsingStringTokenizer(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(sentence);
        return tokens.countTokens();
    }
    public static long estimateTestWindowSize(String input){
        int r=(countWordsUsingStringTokenizer(input)/WPM);
        if(r==0){
            r=1;
        }
        //System.out.println("Time for read is: "+r+" min");
        long result=r*60*1000L;
        return result;
    }
    public static int recListSize(int min){
        int recsLimit;
        if(min<=5){
            recsLimit=5;
            return recsLimit;
        }else{
            if(min<=10){
                recsLimit=10;
                return recsLimit;
            }else{
                if(min<=15){
                    recsLimit=15;
                    return recsLimit;
                }else{
                    recsLimit=20;
                    return recsLimit;
                }
            }
        }
    }
}

