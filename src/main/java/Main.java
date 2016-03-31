import java.util.Date;
import java.util.HashMap;

public class Main implements Constants{
    public static void main(String[] args) {
        DBContorller db = new DBContorller();

        db.getInfoAboutMovie(33434);

    }
}
