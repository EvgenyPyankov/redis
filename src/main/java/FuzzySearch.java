public interface FuzzySearch {

    double getDistance(String s1, String s2);

    boolean fits(String s1, String s2, double limit);
}
