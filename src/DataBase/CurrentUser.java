package DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CurrentUser extends User {
    private int tokensCount;
    private int numFreePremiumMovies;


    private List<Movie> purchasedMovies = new ArrayList<>();
    private List<Movie> watchedMovies = new ArrayList<>();
    private List<Movie> likedMovies = new ArrayList<>();
    private List<Movie> ratedMovies = new ArrayList<>();
    private List<Movie> notifications = new ArrayList<>();
    Map<String, Integer> rememberRated = new HashMap<>();
    public CurrentUser(final int tokensCount, final int numFreePremiumMovies,
                       final List<Movie>  purchasedMovies,
                       final List<Movie>  watchedMovies, final List<Movie> likedMovies,
                       final List<Movie>  ratedMovies) {
        this.tokensCount = tokensCount;
        this.numFreePremiumMovies = numFreePremiumMovies;
        this.purchasedMovies = purchasedMovies;
        this.watchedMovies = watchedMovies;
        this.likedMovies = likedMovies;
        this.ratedMovies = ratedMovies;
    }

    public CurrentUser() { };

    public CurrentUser(final User user) {
        super(user);
        this.tokensCount = 0;
        this.numFreePremiumMovies = 15;
        this.purchasedMovies = new ArrayList<>();
        this.watchedMovies = new ArrayList<>();
        this.likedMovies = new ArrayList<>();
        this.ratedMovies = new ArrayList<>();
    }



    public CurrentUser(final User user, final Integer tokensCount,
                       final Integer numFreePremiumMovies,
                       final List<Movie>  purchasedMovies, final List<Movie>  watchedMovies,
                       final List<Movie>  likedMovies, final List<Movie>  ratedMovies) {
        super(user);
        this.tokensCount = tokensCount;
        this.numFreePremiumMovies = numFreePremiumMovies;
        this.purchasedMovies = purchasedMovies;
        this.watchedMovies = watchedMovies;
        this.likedMovies = likedMovies;
        this.ratedMovies = ratedMovies;
    }

    public Map<String, Integer> getRememberRated() {
        return rememberRated;
    }

    public void setRememberRated(Map<String, Integer> rememberRated) {
        this.rememberRated = rememberRated;
    }

    public List<Movie>  getNotifications() {
        return notifications;
    }

    public int getTokensCount() {
        return tokensCount;
    }

    public void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public List<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(final List<Movie>  purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public List<Movie>  getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(final List<Movie>  watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public List<Movie>  getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(final List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    @Override
    public String toString() {
        return  super.toString() + "Database.CurrentUser{"
                + "tokensCount=" + tokensCount
                + ", numFreePremiumMovies=" + numFreePremiumMovies
                + ", purchasedMovies=" + purchasedMovies
                + ", watchedMovies=" + watchedMovies
                + ", likedMovies=" + likedMovies
                + ", ratedMovies=" + ratedMovies
                + '}';
    }

    public List<Movie>  getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(final List<Movie>  ratedMovies) {
        this.ratedMovies = ratedMovies;
    }
}
