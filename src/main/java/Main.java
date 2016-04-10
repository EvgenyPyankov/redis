import java.util.Date;
import java.util.HashMap;

public class Main implements Constants{
    public static void main(String[] args) {
        DBContorller db = new DBContorller();

        // examples using search info
        db.search(GENRES, "");
        db.search(MOVIES, "Dec");
        db.search(ACTORS, "vgen");
    }
}
