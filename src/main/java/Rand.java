import java.util.*;

public class Rand {
    private static Random rand = new Random();



    public static int getRand(int n){
        return rand.nextInt(n);
    }

    public static Date getRandDate(Date dateFrom, Date dateTo){
        long from = dateFrom.getTime();
        long to = dateTo.getTime();
        long range = to-from;
        long date = from +(long)(rand.nextDouble()*range);
        return new Date(date);
    }



}
