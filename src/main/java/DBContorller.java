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
        System.out.println("\nRatings:");
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

    public ArrayList<Map<String, String>> search(String Category, String SearchKey){
        // result data array
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        // translated query
        String translatedSearchKey = langpadSwitch(SearchKey);

        System.out.println("\nSelect all records from \""+Category+"\"");
        System.out.println("  where attributes contains \""+SearchKey+"\" (or maybe \""+translatedSearchKey+"\")\n");

        if (!(Category.equalsIgnoreCase(GENRES) || Category.equalsIgnoreCase(MOVIES) || Category.equalsIgnoreCase(ACTORS))){

            // invalid category name
            // you may throw an exception here...

        } else {

            // list of available ids of current category
            Set<String> idlist = jedis.smembers(Category);

            boolean contains; // if record contains searchkey
            //String currentInfo; // record info
            Map<String,String> record; // record map
            // for each available ids
            for(String id : idlist)
            {
                contains = false;
                //currentInfo = "";

                if (Category.equalsIgnoreCase(ACTORS) || Category.equalsIgnoreCase(MOVIES)){
                    // hashmap handling
                    record = jedis.hgetAll(name(Category,id));
                    //currentInfo = "Key: \"" + name(Category,id) + "\"\n";
                    for(Map.Entry<String, String> e : record.entrySet()) {
                    //    currentInfo += e.getKey() + ": " + e.getValue() + "\n";
                        if (e.getValue().contains(SearchKey) || e.getValue().contains(translatedSearchKey)){
                            contains = true;
                        }
                    }
                } else  {
                    // string handling
                    String key = name(Category,id);
                    String value = jedis.get(key);
                    record = new HashMap<String, String>();
                    record.put(key, value);
                    if (value.contains(translatedSearchKey) || value.contains(SearchKey)){
                    //    currentInfo = "\"" + key + "\": " + value + "\n";
                        contains = true;
                    }
                }

                // if current record contains searchkey then add ones to result
                // and print info
                if (contains) {
                    data.add(record);
                    //System.out.println(currentInfo);
                }
            }
        }

        System.out.println(data.size() + " rows selected.\n");
        return data;
    }

    public ArrayList<Map<String, String>> search(String Category, String SearchKey,  String Field){
        // result data array
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        // translated query
        String translatedSearchKey = langpadSwitch(SearchKey);

        System.out.println("\nSelect all records from \""+Category+"\"");
        System.out.println("  where field \"" + Field + "\" contains \""+SearchKey+"\" (or maybe \""+translatedSearchKey+"\")\n");

        if (!(Category.equalsIgnoreCase(MOVIES) || Category.equalsIgnoreCase(ACTORS))){

            // invalid category name
            // you may throw an exception here...

        } else {

            // list of available ids of current category
            Set<String> idlist = jedis.smembers(Category);

            boolean contains; // if record contains searchkey
            String value; //  value of record's field
            // for each available ids
            for(String id : idlist)
            {
                value = jedis.hget(name(Category,id), Field);

                // hashmap handling
                contains =  value.contains(SearchKey) || value.contains(translatedSearchKey);

                // if selected record's field contains the searchkey then add ones to result
                if (contains) {
                    data.add(jedis.hgetAll(name(Category,id)));
                }
            }
        }

        System.out.println(data.size() + " rows selected.\n");
        return data;
    }

    public String langpadSwitch(String message){
        String translatedMessage = "";
        String en_ru_langsPattern = "qwertyuiop[]asdfghjkl;'zxcvbnm,./QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?йцукенгшщзхъфывапролджэячсмитьбю.ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ,";
        int langpadSymbolsCount = en_ru_langsPattern.length() / 2;
        for (int i = 0; i < message.length(); i++){
            if (en_ru_langsPattern.indexOf(message.charAt(i)) > -1){
                int translatedIndex = (en_ru_langsPattern.indexOf(message.charAt(i)) + langpadSymbolsCount) % (2 * langpadSymbolsCount);
                translatedMessage += en_ru_langsPattern.charAt(translatedIndex);
            } else{
                translatedMessage += message.charAt(i);
            }
        }
        return translatedMessage;
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
