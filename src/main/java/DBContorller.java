import redis.clients.jedis.*;

import java.util.HashMap;

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
        jedis.sadd(GENRES,genre);
    }

    public void addGenreToMovie(String genre, String movie){
        jedis.sadd(GENRES +":"+genre+"movies",movie);
        jedis.sadd(MOVIES +":"+movie+"genres",genre);
    }

    public void addActorToMovie(String actor, String movie){
        jedis.sadd(getFullName(ACTORS,actor,"movies"),movie);
        jedis.sadd(getFullName(MOVIES,movie,"actors"),actor);
    }

    public void deleteGenre(String genre){
        if (jedis.smembers(getFullName(GENRES,genre,"movies"))==null)
            jedis.srem(GENRES,genre);
        else System.out.println(ERR_DELETE);
    }

    public void deleteActor(String actor){
        if (jedis.smembers(getFullName(ACTORS,actor,"movies"))==null)
            jedis.srem(ACTORS,actor);
        else System.out.println(ERR_DELETE);
    }

    public void deleteMovie(String movie){
        jedis.srem(MOVIES,movie);
        jedis.del(getFullName(MOVIES,movie,"genres"));
        jedis.del(getFullName(MOVIES,movie,"actors"));
        jedis.del(getFullName(MOVIES,movie));
    }

    private String getFullName(String str1, String str2, String str3){
        return String.format("%s:%s:%s",str1,str2,str3);

    }

    private String getFullName(String str1, String str2){
        return String.format("%s:%s:%s",str1,str2);

    }

    private String nextVal(){
        return (jedis.incr(ID)).toString();
    }




}
