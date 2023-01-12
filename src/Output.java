import DataBase.CurrentUser;
import DataBase.Movie;

import java.util.ArrayList;
import java.util.List;

public final class Output {
    private String error;
    private List<Movie> currentMovieList;
    private CurrentUser currentUser = new CurrentUser();



    public Output() {
        this.error = null;
        this.currentMovieList = new ArrayList<>();
        this.currentUser = null;
    }
    public Output(String rand) {
        this.error = null;
        this.currentMovieList = null;
        this.currentUser = null;
    }


    public Output(final String error, final List<Movie> currentMovieList,
                  final CurrentUser currentUser) {
        this.error = error;
        this.currentMovieList = new ArrayList<>();
        this.currentUser = new CurrentUser(currentUser);
    }

    /**
     * getter si setter pt eroare
     * @return
     */
    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    /**
     * getter si setter pt CurrentMovieList
     * @return
     */
    public List<Movie> getCurrentMovieList() {
        return currentMovieList;
    }

    public void setCurrentMovieList(final List<Movie> currentMovieList) {
        this.currentMovieList = new ArrayList<>(currentMovieList);
    }

    /**
     * getter si setter pt current user
     * @return
     */
    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public String toString() {
        return "Output{"
                + "error='" + error + '\''
                + ", currentMovieList=" + currentMovieList
                + ", currentUser=" + currentUser
                + '}';
    }
}
