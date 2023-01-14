package DataBase;

import java.util.List;

public final class DataBase {
    private List<CurrentUser> userList;
    private List<Movie> movieList;

    private List<Actions> actions;

    private static DataBase instance = null;

    private DataBase() { }

    /**
     * making DataBase class SINGLETON
     * @return
     */
    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    /**
     * initializare
     * @param myUserList lista de utilizatori primita la citire
     * @param myMovieList lista de filme primita la citire
     * @param myActions lista de actiuni primita la citire
     */
    public void init(final List<CurrentUser> myUserList, final List<Movie> myMovieList,
                     final List<Actions> myActions) {
        this.userList = myUserList;
        this.movieList = myMovieList;
        this.actions = myActions;
    }

    /**
     *returneaza un utilizator pe care il gaseste intr o lista
     *      * de utilizatori
     * @param name numele utilizatorului care este cautat
     * @return utilizatorul gasit
     */
    public CurrentUser findUserByUsername(final String name) {
        for (CurrentUser user : userList) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * returneaza un film gasit in movieList cautat dupa numele
     * acestuia
     * @param name numele filmului care este cautat
     * @return filmul
     */
    public Movie findMovieByUsername(final String name) {
        for (Movie mov : this.movieList) {
            if (mov.getName().equals(name)) {
                return mov;
            }
        }
        return null;
    }

    /**
     *  adauga un user intr o lista de Current useri
     * @param user Userul de adaugat
     */
    public void addUser(final CurrentUser user) {
        userList.add(user);
    }

    public List<CurrentUser> getUserList() {
        return userList;
    }

    public void setUserList(final List<CurrentUser> userList) {
        this.userList = userList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(final List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Actions> getActions() {
        return actions;
    }

    public void setActions(final List<Actions> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "Database.Input{"
                + "userList=" + userList
                + ", currentMovieList=" + movieList
                + ", actions=" + actions
                + '}';
    }
}
