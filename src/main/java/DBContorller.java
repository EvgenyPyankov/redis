import redis.clients.jedis.*;

public class DBContorller implements Constants{
    Jedis jedis;

    public DBContorller(){
        jedis = new Jedis("localhost", 6379);
    }

    public void addMovie(String movie){
        jedis.sadd(MOVIES_LIST, movie);
    }

    public void addActor(String actor){
        jedis.sadd(ACTORS_LIST, actor);
    }

    public void addGenre(String genre){
        jedis.sadd(GENRES_LIST,genre);
    }

    public void addGenreToMovie(long genre, long movie){
        //jedis.sadd(GENRES_LIST+":"+"movies",movie);
    }

    public void addActorToMovie(){

    }

    public long nextVal(){
        return jedis.incr(ID);
    }
}
