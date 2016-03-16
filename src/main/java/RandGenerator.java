import java.util.HashMap;

public class RandGenerator {
    static DBContorller db = new DBContorller();

    public static void generateGenres(){

    }

    public static void generateMovies(int n){

    }

    public static void generateActors(int n){
        for (int i=0; i<n; i++){
            HashMap actor = new HashMap();
            db.addActor(actor);
        }
    }

    public static void allocateActors(int n){

    }
}
