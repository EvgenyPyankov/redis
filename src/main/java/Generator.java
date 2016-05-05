import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Generator implements Constants {
    static DBContorller db = new DBContorller();

    public static void generate() {
        generateGenres();
        generateActors(ACTORS_NUMBER);
        generateMovies(MOVIES_NUMBER);
        allocateGenres();
        allocateActors(AVERAGE_ACTORS_CAST_MOVIE_NUMBER);
    }

    private static void generateGenres() {
        for (int i = 0; i < GENRES_NAMES.length; i++) {
            db.addGenre(GENRES_NAMES[i], i);
        }
    }

    private static void allocateGenres() {
        for (int i = 0; i < MOVIES_NUMBER; i++) {
            int numberOfGenres = Rand.getRand(4) + 1;
            for (int j = 0; j < numberOfGenres; j++) {
                int index = Rand.getRand(GENRES_NAMES.length);
                db.addGenreToMovie(String.valueOf(index), String.valueOf(i));
            }
            System.out.println("allocate genres: " + i);
        }
    }

    private static void generateMovies(int n) {
        Date dateFrom = new Date(40, 1, 1);
        System.out.println(dateFrom);
        Date currentDate = new Date();
        System.out.println(currentDate);
        for (int i = 0; i < MOVIES_NUMBER; i++) {
            HashMap movie = new HashMap();
            movie.put("name", "Movie #" + i);
            movie.put("release", Rand.getRandDate(MOVIE_DATE_FROM, CURRENT_DATE).toString());
            HashMap ratings = new HashMap();
            for (int j = 0; j < RATING_SITES.length; j++) {
                ratings.put(RATING_SITES[j], String.valueOf(Rand.getRand(100)));
            }
            db.addMovie(movie, ratings, i);
            System.out.println("Movie: " + i);
        }

    }

    private static void generateActors(int n) {
        for (int i = 0; i < n; i++) {
            HashMap actor = new HashMap();
            int gender = Rand.getRand(2);
            if (gender > 0) {
                actor.put("gender", "m");
                actor.put("name", M_NAMES[Rand.getRand(M_NAMES.length)]);
                actor.put("surname", M_SURNAMES[Rand.getRand(M_SURNAMES.length)]);
            } else {
                actor.put("gender", "f");
                actor.put("name", F_NAMES[Rand.getRand(F_NAMES.length)]);
                actor.put("surname", F_SURNAMES[Rand.getRand(F_SURNAMES.length)]);
            }
            db.addActor(actor);
            System.out.println("Actors: " + i);
        }
    }

    private static void allocateActors(int n) {
        Object[] actors = db.getActors().toArray();
        for (int i = 0; i < MOVIES_NUMBER; i++) {
            int numberOfActors = Rand.getRand(AVERAGE_ACTORS_CAST_MOVIE_NUMBER) + 1;
            for (int j = 0; j < numberOfActors; j++) {
                int index = Rand.getRand(actors.length);
                db.addActorToMovie(actors[index].toString(), String.valueOf(i));
            }
            System.out.println("allocateActors: " + i);
        }
    }

}
