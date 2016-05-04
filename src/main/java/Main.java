import java.util.Date;
import java.util.HashMap;

public class Main implements Constants{
    public static void main(String[] args) {
        DBContorller db = new DBContorller();

        // examples using search info
        db.searchGenresUsingJaroWinkler("bgaph", 0.5);
        db.searchGenresUsingLevenstein("bgaph", 5);
        //db.searchGenresUsingDamerauLevenstein("bgaph", 5);
    }
}
