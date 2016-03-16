import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.*;

public class DBContorller implements Constants{
    Jedis jedis;

    public DBContorller(){
        jedis = new Jedis("localhost", 6379);
    }

    public void addMovie(HashMap movie){
        String id = nextVal();
        jedis.sadd(MOVIES, id);
        jedis.hmset(MOVIES + ":" + id, movie);
    }

    public void addActor(HashMap actor){
        String id = nextVal();
        jedis.sadd(ACTORS,id);
        jedis.hmset(ACTORS +":"+id, actor);
    }

    public void addGenre(String genre){
        String id = nextVal();
        jedis.sadd(GENRES, id);
        jedis.set(GENRES + ":" + id, genre);
    }

    public void addGenreToMovie(String genre, String movie){
        jedis.sadd(GENRES +":"+genre+"movies",movie);
        jedis.sadd(MOVIES +":"+movie+"genres",genre);
    }

    public void addActorToMovie(String actor, String movie){
        jedis.sadd(getFullName(ACTORS,actor,"movies"),movie);
        jedis.sadd(getFullName(MOVIES,movie,"actors"),actor);
    }

    public void deleteGenre(String genreID){
        if (jedis.smembers(getFullName(GENRES,genreID,"movies"))==null) {
            jedis.srem(GENRES, genreID);
            jedis.del(GENRES+":"+genreID);
        }
        else System.out.println(ERR_DELETE);
    }

    public void deleteActor(String actorID){
        if (jedis.smembers(getFullName(ACTORS,actorID,"movies"))==null) {
            jedis.srem(ACTORS, actorID);
            jedis.del(ACTORS+":"+actorID);
        }
        else System.out.println(ERR_DELETE);
    }

    public void deleteMovie(String movie){
        Set<String> genres = jedis.smembers(getFullName(MOVIES,movie,"genres"));
        for (String genre:genres){
            jedis.srem(getFullName(GENRES,genre,"movies"),movie);
        }
        jedis.srem(MOVIES,movie);
        jedis.del(getFullName(MOVIES,movie,"genres"));
        jedis.del(getFullName(MOVIES,movie,"actors"));
        jedis.del(getFullName(MOVIES,movie));
    }

    private String getFullName(String str1, String str2, String str3){
        return String.format("%s:%s:%s",str1,str2,str3);

    }

    private String getFullName(String str1, String str2){
        return String.format("%s:%s",str1,str2);

    }

    private String nextVal(){
        return (jedis.incr(ID)).toString();
    }




}
