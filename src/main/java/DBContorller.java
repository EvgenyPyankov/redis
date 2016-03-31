import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.*;

public class DBContorller implements Constants{
    Jedis jedis;

    public DBContorller(){
        jedis = new Jedis("localhost", 6379, 60000);
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
        if (jedis.smembers(name(GENRES,genreID,MOVIES))==null) {
            jedis.srem(GENRES, genreID);
            jedis.del(GENRES+":"+genreID);
        }
        else System.out.println(ERR_DELETE);
    }

    public void deleteActor(String actorID){
        if (jedis.smembers(name(ACTORS,actorID,MOVIES))==null) {
            jedis.srem(ACTORS, actorID);
            jedis.del(ACTORS+":"+actorID);
        }
        else System.out.println(ERR_DELETE);
    }

    public void deleteMovie(String movie){
        Set<String> genres = jedis.smembers(name(MOVIES,movie,GENRES));
        for (String genre:genres){
            jedis.srem(name(GENRES,genre,MOVIES),movie);
        }
        jedis.srem(MOVIES,movie);
        jedis.del(name(MOVIES,movie,GENRES));
        jedis.del(name(MOVIES,movie,ACTORS));
        jedis.del(name(MOVIES,movie,RATINGS));
        jedis.del(name(MOVIES,movie));
    }

    //update

    public void updateGenre(long id, String name){
        String genreId = String.valueOf(id);
        jedis.set(name(GENRES,genreId), name);
    }

    public void updateActor(long id, HashMap actor){
        String actorId = String.valueOf(id);
        jedis.hmset(name(ACTORS,actorId), actor);
    }

    public void updateRatings(long id, HashMap ratings){
        String movieId = String.valueOf(id);
        jedis.hmset(name(MOVIES,movieId,RATINGS),ratings);
    }

    public void updateMovie(long movieId,HashMap movie){
        String id = String.valueOf(movieId);
        jedis.hmset(name(MOVIES,id), movie);
    }

    //supporting

    public Set getGenres(){
        return jedis.smembers(GENRES);
    }

    public Set getActors(){
        return jedis.smembers(ACTORS);
    }

    public void getInfoAboutMovie(long id){
        String movieId = String.valueOf(id);

        Map<String,String> movie = jedis.hgetAll(name(MOVIES,movieId));
        System.out.println("\nID: "+movieId);
        for(Map.Entry<String, String> e : movie.entrySet()) {
            System.out.print(e.getKey()+": "+e.getValue()+"\n");
        }

        Map<String, String> ratings = jedis.hgetAll(name(MOVIES,movieId,RATINGS));
        System.out.println("\nRaings:");
        for(Map.Entry<String, String> e : ratings.entrySet()) {
            System.out.print(e.getKey()+": "+e.getValue()+"\n");
        }

        Set<String>genres = jedis.smembers(name(MOVIES,movieId,GENRES));
        Iterator<String> iterator2 = genres.iterator();
        System.out.println("\nGenres:");
        while(iterator2.hasNext()) {
            String genreId = iterator2.next();
            System.out.println(jedis.get(name(GENRES,genreId)));
        }

        Set<String>actors = jedis.smembers(name(MOVIES,movieId,ACTORS));
        Iterator<String> iterator = actors.iterator();
        System.out.println("\nActors:");
        while(iterator.hasNext()) {
            String actorId = iterator.next();
            System.out.println(jedis.hget(name(ACTORS, actorId), "name") + " " + jedis.hget(name(ACTORS, actorId), "surname"));
        }
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
