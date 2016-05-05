import java.util.Date;

public interface Constants {
    String GENRES = "genres";
    String MOVIES = "movies";
    String ACTORS = "actors";
    String RATINGS = "ratings";
    String ID = "id";
    String ERR_DELETE = "You can't delete this item because it is already being used somewhere else";
    int MOVIES_NUMBER = 1000000;
    int ACTORS_NUMBER = 1000000;
    int AVERAGE_ACTORS_CAST_MOVIE_NUMBER = 10;
    String[] GENRES_NAMES = {"biographic", "drama", "lovestory", "action", "thriller", "comedy", "arthouse"};
    String[] F_NAMES = {"Sarah", "Jennifer", "Katniss", "Primrose", "Monica", "Elizabet", "Willow", "Grace", "Victoria", "Alma", "Rue", "Clove", "Effie", "Kate", "Veronika", "Germiona", "Lily"};
    String[] M_NAMES = {"John", "Peeta", "Gale", "Liam", "Josh", "Daniel", "Leo", "Gerard", "Teo", "Finnick", "Cinna", "Cato", "Marvel", "Ivan", "Evgeny", "Harry", "Sirius", "Ben", "Ron", "Caesar", "Haymitch"};
    String[] M_SURNAMES = {"Smith", "Messi", "Snow", "Terry", "Mellark", "Hotorn", "Hawthorne", "Potter", "Black", "Odair", "Crane", "Flickerman", "Ivanov", "Petrov", "Pyankov", "Dumbledore", "Griffingor", "Slytherin", "Malfoy"};
    String[] F_SURNAMES = {"Smith", "Green", "Everdeen", "Trinket", "Potter", "Mason", "Cresta", "Paylor", "Cardew", "Mellark", "Hotorn", "Kolesnikova", "Bunakova", "Granger", "Delacour", "Hufflepuff", "Lestrange"};
    Date MOVIE_DATE_FROM = new Date(40, 1, 1);
    Date CURRENT_DATE = new Date();
    String[] RATING_SITES = {"Kinopoisk", "IMDb", "Guardian", "Yahoo!Movies", "MrQE"};
}
