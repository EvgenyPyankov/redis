import java.util.Date;
import java.util.HashMap;

public class Main implements Constants{
    public static void main(String[] args) {
        DBContorller db = new DBContorller();
//        HashMap map = new HashMap();
//        map.put("name","movie1");
//        map.put("year","1995");
//
//        HashMap map2 = new HashMap();
//        map2.put("name","movie2");
//        map2.put("year","2015");
//
//        db.addMovie(map);
//        db.addMovie(map2);

//        Generator.generateGenres();
//        Generator.generateMovies(MOVIES_NUMBER);
//        Generator.generateActors(ACTORS_NUMBER);
//        Generator.allocateActors(AVERAGE_ACTORS_CAST_MOVIE_NUMBER);
 Generator.generate();
        //System.out.println(new Date());
        //System.out.println(CURRENT_DATE);
    }
}
