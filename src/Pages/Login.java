package Pages;
import DataBase.CurrentUser;
import DataBase.DataBase;
import DataBase.Movie;
import DataBase.User;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Login extends Page {
    private CurrentUser userLoggedIn;
    private List<Movie> loggedInMovieList;

    public Login(final CurrentUser userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }
    public Login(final String title) {
        super(title);
        this.userLoggedIn = null;
    }

    public Login(final String title, final CurrentUser userLoggedIn) {
        super(title);
        this.userLoggedIn = userLoggedIn;
    }

    /**
     * Sorteaza filmele in caz ca actiunea este filter
     * override compare
     * @param duration durata descendet sau ascendent in funcite de input
     * @param rating ratia descendent sau ascendent in functie de input
     */
    public void sortByDuration(final String duration, final String rating) {
        Collections.sort(this.loggedInMovieList, new Comparator<Movie>() {
            @Override
            public int compare(final Movie o1, final Movie o2) {
                if (duration != null) {
                    if (duration.equals("increasing")) {
                        if (o1.getDuration() != o2.getDuration()) {
                            return Integer.compare(o1.getDuration(), o2.getDuration());
                        } else {
                            if (rating.equals("increasing")) {
                                return Double.compare(o1.getRating(), o2.getRating());
                            } else if (rating.equals("decreasing")) {
                                return Double.compare(o2.getRating(), o1.getRating());
                            }
                        }
                    } else if (duration.equals("decreasing")) {
                        if (o1.getDuration() != o2.getDuration()) {
                            return Integer.compare(o2.getDuration(), o1.getDuration());
                        } else {
                            if (rating.equals("increasing")) {
                                return Double.compare(o1.getRating(), o2.getRating());
                            } else if (rating.equals("decreasing")) {
                                return Double.compare(o2.getRating(), o1.getRating());
                            }
                        }
                    }
                } else if (duration == null && rating != null) {
                    if (rating.equals("increasing")) {
                        return Double.compare(o1.getRating(), o2.getRating());
                    } else if (rating.equals("decreasing")) {
                        return Double.compare(o2.getRating(), o1.getRating());
                    }
                }
                return 0;
            }
        });
    }

    /**
     * verifica daca un film este cumparat si il returneaza
     * @param movie numele filmului pe care il verific
     * @return filmul
     */
    public Movie checkIfMovieIsInPurchased(final String movie) {
        for (Movie goMovie : this.userLoggedIn.getPurchasedMovies()) {
            if (goMovie.getName().equals(movie)) {
               return goMovie;
            }
        }

        return null;
    }

    /**
     *  verific daca un film este cumparat
     *  dar nu il returnez, ci returnez false sau true
     * @param movie numele filmului
     * @return false/ture
     */
    public boolean isInPurchased(final String movie) {
        for (Movie goMovie : this.userLoggedIn.getPurchasedMovies()) {
            if (goMovie.getName().equals(movie)) {
                return true;
            }
        }

        return false;
    }
    /**
     * verifica daca un film este vazut
     * @param movie numele filmului pe care il verific
     * @return true/false
     */
    public boolean ifWatched(final String movie) {
        for (Movie goMovie : this.userLoggedIn.getWatchedMovies()) {
            if (goMovie.getName().equals(movie)) {
                return true;
            }
        }

        return false;
    }

    /**
     * verific daca un film are rate sau nu
     * @param movie numele filmului pe care il verific
     * @return true/false
     */
    public boolean ifRated(final String movie) {
        for (Movie goMovie : this.userLoggedIn.getRatedMovies()) {
            if (goMovie.getName().equals(movie)) {
                return true;
            }
        }

        return false;
    }

    /**
     *  Metoda pentru a updata lista de filme vazute, in caz in care
     *  un utilizator se relocheaza
     * @param moviesInDatabase lista de filme din baza de date
     * @return lista de filme noua
     */
    public List<Movie> updateWatchedMl(final List<Movie> moviesInDatabase) {
        List<Movie> movieList = new ArrayList<Movie>();
        for (Movie movie : userLoggedIn.getWatchedMovies()) {
            for (Movie movieInD : moviesInDatabase) {
                if (movie.getName().equals(movieInD.getName())) {
                    movieList.add(movieInD);
                }
            }
        }
        return movieList;
    }

    /**
     *  Metoda pentru a updata lista de filme cumparate, in caz in care
     *  un utilizator se relocheaza
     * @param moviesInDatabase lista de filme din baza de date
     * @return lista de filme updatata
     */
    public List<Movie> updatePurchasedMl(final List<Movie> moviesInDatabase) {
        List<Movie> movieList = new ArrayList<Movie>();
        for (Movie movie : userLoggedIn.getPurchasedMovies()) {
            for (Movie movieInD : moviesInDatabase) {
                if (movie.getName().equals(movieInD.getName())) {
                    movieList.add(movieInD);
                }
            }
        }
        return movieList;
    }

    /**
     *  Metoda pentru a updata lista de filme rated, in caz in care
     *  un utilizator se relocheaza
     * @param moviesInDatabase lista de filme din baza de date
     * @return lista de filme updatata
     */
    public List<Movie> updateRatedMl(final List<Movie> moviesInDatabase) {
        List<Movie> movieList = new ArrayList<Movie>();
        for (Movie movie : userLoggedIn.getRatedMovies()) {
            for (Movie movieInD : moviesInDatabase) {
                if (movie.getName().equals(movieInD.getName())) {
                    movieList.add(movieInD);
                }
            }
        }
        return movieList;
    }

    /**
     *  Metoda pentru a updata lista de filme liked, in caz in care
     *  un utilizator se relocheaza
     * @param moviesInDatabase lista de filme din baza de date
     * @return lista de filme updatata
     */
    public List<Movie> updateLikedMl(final List<Movie> moviesInDatabase) {
        List<Movie> movieList = new ArrayList<Movie>();
        for (Movie movie : userLoggedIn.getLikedMovies()) {
            for (Movie movieInD : moviesInDatabase) {
                if (movie.getName().equals(movieInD.getName())) {
                    movieList.add(movieInD);
                }
            }
        }
        return movieList;
    }

    /**
     * getter si setter pt MovieList
     * @return lista de filme fara tara utilizatorului
     * in caz in care este banata
     */
    public List<Movie> getLoggedInMovieList() {
        return loggedInMovieList;
    }

    public void setLoggedInMovieList(final List<Movie> loggedInMovieList) {
        this.loggedInMovieList = loggedInMovieList;
    }

    /**
     * getter si setter pt userul logat
     * @CurrentUser userul curent
     */
    public CurrentUser getUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(final CurrentUser userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    /**
     * verific credentialele si daca este
     * in lista mea de utilizatori return true
     * @param userList Lista de utilizatoru din baza de date
     * @return true/false in functie daca este sau nu in baza mea de date
     */
    public boolean verifyCredentials(final List<CurrentUser> userList) {
       for (User user : userList) {
           if (this.userLoggedIn.getName().equals(user.getName())
                   && this.getUserLoggedIn().getPassword().equals(
                           user.getPassword())) {
               return true;
           }
       }
       return false;
    }
}
