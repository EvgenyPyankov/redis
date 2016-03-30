import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.*;

public class DBContorller implements Constants{
    Jedis jedis;

    public DBContorller(){
        jedis = new Jedis("localhost", 6379);
    }

    //add

    public void addMovie(HashMap movie, HashMap ratings, int movieId){
        String id = String.valueOf(movieId);
        jedis.sadd(MOVIES, id);
        jedis.hmset(name(MOVIES,id), movie);
        addRatingsToMovie(ratings,id);
    }

    public void addActor(HashMap actor){
        String id = nextVal();
        jedis.sadd(ACTORS,id);
        jedis.hmset(name(ACTORS,id), actor);
    }

    public void addGenre(String genre, int genreId){
        String id = String.valueOf(genreId);
        jedis.sadd(GENRES, id);
        jedis.set(name(GENRES,id), genre);
    }

    public void addGenreToMovie(String genreId, String movieId){
        jedis.sadd(name(GENRES,genreId,MOVIES),movieId);
        jedis.sadd(name(MOVIES,movieId,GENRES),genreId);
    }

    public void addActorToMovie(String actorId, String movieId){
        jedis.sadd(name(ACTORS,actorId,MOVIES),movieId);
        jedis.sadd(name(MOVIES,movieId,ACTORS),actorId);
    }

    public void addActorToMovie(int aId, int mId){
        String actorId=String.valueOf(aId);
        String movieId = String.valueOf(mId);
        jedis.sadd(name(ACTORS,actorId,MOVIES),movieId);
        jedis.sadd(name(MOVIES,movieId,ACTORS),actorId);
    }

    public void addRatingsToMovie(HashMap ratings, String movieId){
        jedis.hmset(name(MOVIES,movieId,RATINGS),ratings);
    }

    //delete

    public void deleteGenre(String genreID){
        if (jedis.smembers(name(GENRES,genreID,"movies"))==null) {
            jedis.srem(GENRES, genreID);
            jedis.del(GENRES+":"+genreID);
        }
        else System.out.println(ERR_DELETE);
    }

    public void deleteActor(String actorID){
        if (jedis.smembers(name(ACTORS,actorID,"movies"))==null) {
            jedis.srem(ACTORS, actorID);
            jedis.del(ACTORS+":"+actorID);
        }
        else System.out.println(ERR_DELETE);
    }

    public void deleteMovie(String movie){
        Set<String> genres = jedis.smembers(name(MOVIES,movie,"genres"));
        for (String genre:genres){
            jedis.srem(name(GENRES,genre,"movies"),movie);
        }
        jedis.srem(MOVIES,movie);
        jedis.del(name(MOVIES,movie,"genres"));
        jedis.del(name(MOVIES,movie,"actors"));
        jedis.del(name(MOVIES,movie));
    }

    //update

    public Set getGenres(){
        return jedis.smembers(GENRES);
    }

    public Set getActors(){
        return jedis.smembers(ACTORS);
    }

    public void getInfoAboutMovie(int id){
        String movieId = String.valueOf(id);
        List<String> movie = jedis.hmget(name(MOVIES,movieId));
       // System.out.println(movieId+": "+);

    }

    private String name(String str1, String str2, String str3){
        return String.format("%s:%s:%s",str1,str2,str3);
    }

    private String name(String str1, String str2){
        return String.format("%s:%s",str1,str2);
    }

    private String nextVal(){
        return (jedis.incr(ID)).toString();
    }

}
