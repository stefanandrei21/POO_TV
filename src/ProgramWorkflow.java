import DataBase.DataBase;
import DataBase.CurrentUser;
import DataBase.Movie;
import DataBase.User;
import DataBase.Actions;
import DataBase.Notification;
import Pages.Page;
import Pages.UpgradesPage;
import Pages.SeeDetailsPage;
import Pages.MoviesPage;
import Pages.HomePage;
import Pages.Login;
import Pages.RegisterPage;


import java.util.ArrayList;
import java.util.List;

public final class ProgramWorkflow {
    private DataBase dataBase;

    public DataBase getDataBase() {
        return dataBase;
    }

    public void setDataBase(final DataBase dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * metoda pentru functionarea temei
     *
     * @return
     */
    public List<Output>  work() {
        int register = 1;
        int isLogged = 0;
        //instantiez paginile
        Login login = new Login("loginpage");
        RegisterPage registerPage = new RegisterPage("registerPage");
        HomePage homePage = new HomePage("homePage");
        MoviesPage moviesPage = new MoviesPage("moviesPage", dataBase.getMovieList());
        UpgradesPage upgradesPage = new UpgradesPage("UpgradesPage");
        SeeDetailsPage seeDetailsPage = new SeeDetailsPage("seeDetailsPage");
        Page currentPage = homePage;
        int i = 0;
        //incep sa trec prin lista mea de actiuni
        List<Output> listToPrint = new ArrayList<Output>();
        for (Actions action : dataBase.getActions()) {
         //   System.out.println(action);
            i++;
            Output output = new Output();
            List<Movie> purchasedMovieList;
            register = 1;
            if (action.getType().equals("change page")) {
                //realizez actiunea de change page si verific erorile
                if (action.getPage().equals("login") && isLogged == 1) {
                    output.setError("Error");
                } else if (action.getPage().equals("logout") && isLogged == 0) {
                    output.setError("Error");
                } else if (action.getPage().equals("login") && isLogged == 0) {
                    currentPage = login;
                } else if (action.getPage().equals("register")) {
                    currentPage = registerPage;
                } else if (action.getPage().equals("logout")) {
                    login.setUserLoggedIn(null);
                    currentPage = homePage;
                    isLogged = 0;
                } else if (action.getPage().equals("movies") && isLogged == 1) {
                    currentPage = moviesPage;

                    chPgMovies(currentPage, moviesPage, login, output, listToPrint);

                } else if (action.getPage().equals("see details")) {
                    boolean found_movie = false;
                    Movie seeDetailsMovie = new Movie();
                    //verific daca filmul este in lista de filme
                    for (Movie movie : login.getLoggedInMovieList()) {
                        if (action.getMovie().equals(movie.getName())) {
                            found_movie = true;
                            seeDetailsMovie = new Movie(movie);
                        }
                    }
                    // daca nu e eroare
                    if (!found_movie) {
                        output.setError("Error");

                    } else {
                        //daca e il pun in current list si afisez
                        currentPage = seeDetailsPage;
                        seeDetailsPage.setSeeDetailsToMovie(seeDetailsMovie);
                        List<Movie> helpMovie = new ArrayList<Movie>();
                        helpMovie.add(seeDetailsMovie);

                        output.setCurrentMovieList(deepCopy(helpMovie));

                        purchasedMovieList =
                                new ArrayList<>(deepCopy(login.getUserLoggedIn()
                                        .getPurchasedMovies()));
                        output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                            login.getUserLoggedIn().getTokensCount(),
                            login.getUserLoggedIn().getNumFreePremiumMovies(),
                            purchasedMovieList,
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                        listToPrint.add(output);
                    }
                } else if (action.getPage().equals("upgrades") && isLogged == 1) {
                    currentPage = upgradesPage;
                } else {
                    output.setError("Error");
                }
            } else if (action.getType().equals("on page")) {
                //incep verificarile pentru on page
                if (action.getFeature().equals("login")
                        && !currentPage.getTitle().equals("loginpage")) {
                    //daca incerc sa ma loghez si nu sunt pe
                    //pagina login eroare

                    output.setError("Error");
                } else
                if (action.getFeature().equals("login")
                        && currentPage.getTitle().equals("loginpage")) {
                    //setez userul pe care vreau sa il loghez
                    //si verific credentialele
                    CurrentUser loggedUser = new CurrentUser(action.getUser(), 0, 15,
                            null, null, null, null);
                    if (isLogged == 0) {
                        login.setUserLoggedIn(loggedUser);
                    }
                    //daca userul este deja inregistrat si nu este deja logat
                    //il adaug pe pagina mea de logat
                    if (login.verifyCredentials(dataBase.getUserList()) && isLogged == 0) {
                        CurrentUser userToWork = dataBase.findUserByUsername(loggedUser.getName());


                        CurrentUser loginUser = new CurrentUser(userToWork,
                                userToWork.getTokensCount(),
                                userToWork.getNumFreePremiumMovies(),
                                userToWork.getPurchasedMovies(),
                                userToWork.getWatchedMovies(),
                                userToWork.getLikedMovies(),
                                userToWork.getRatedMovies());

                        login.setUserLoggedIn(loginUser);

                        login.getUserLoggedIn().setPurchasedMovies(login.updatePurchasedMl(dataBase.getMovieList()));
                        login.getUserLoggedIn().setWatchedMovies(login.updateWatchedMl(dataBase.getMovieList()));
                        login.getUserLoggedIn().setRatedMovies(login.updateRatedMl(dataBase.getMovieList()));
                        login.getUserLoggedIn().setLikedMovies(login.updateLikedMl(dataBase.getMovieList()));
                        login.getUserLoggedIn().setRememberRated(dataBase.findUserByUsername(login.getUserLoggedIn().getName()).getRememberRated());


                        //afisez userul
                        purchasedMovieList =
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies()));
                        output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                            login.getUserLoggedIn().getTokensCount(),
                            login.getUserLoggedIn().getNumFreePremiumMovies(),
                            purchasedMovieList,
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));
                        listToPrint.add(output);
                        isLogged = 1;

                    } else {

                        output.setError("Error");

                    }
                    //daca featureul este register verific daca sunt pe register
                    //page
                } else if (action.getFeature().equals("register")
                        && currentPage.getTitle().equals("Pages.RegisterPage")) {
                    //setez userul pe care vreau sa il inregistrez
                    CurrentUser userToRegister = new CurrentUser(action.getUser());
                    //ma asigur ca username-ul nu este folosit
                    for (User user : dataBase.getUserList()) {
                        if (user.getName().equals(userToRegister.getName())) {
                            register = 0;

                            output.setError("Error");
                        }
                    }
                    // adaug userul in basa mea de date
                    if (register == 1) {
                        currentPage = login;
                        login.setUserLoggedIn(userToRegister);

                        dataBase.addUser(userToRegister);
                    // afisez
                    purchasedMovieList = new ArrayList<>(deepCopy(
                            login.getUserLoggedIn().getPurchasedMovies()));
                    output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                            login.getUserLoggedIn().getTokensCount(),
                            login.getUserLoggedIn().getNumFreePremiumMovies(),
                            purchasedMovieList,
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                        listToPrint.add(output);
                       currentPage = login;
                        isLogged = 1;

                    }
                    //daca feature-ul este search verific daca sunt pe
                    //moviesPage
                } else if (action.getFeature().equals("search")
                        && currentPage.getTitle().equals("moviesPage")) {
                    purchasedMovieList = new ArrayList<>();
                    //setez userul de output
                    output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                            login.getUserLoggedIn().getTokensCount(),
                            login.getUserLoggedIn().getNumFreePremiumMovies(),
                            new ArrayList<>(deepCopy(
                                    login.getUserLoggedIn().getPurchasedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                    //verific daca exista filmul
                    List<Movie> searchedMovieList = new ArrayList<Movie>();
                    for (Movie movie : login.getLoggedInMovieList()) {
                        if (movie.getName().startsWith(action.getStartsWith())) {
                            searchedMovieList.add(movie);

                        }

                    }
                    //setez output

                    output.setCurrentMovieList(deepCopy(searchedMovieList));

                    listToPrint.add(output);

                    //feature filter verific daca sunt pe movies page
                } else if (action.getFeature().equals("filter")
                        && currentPage.getTitle().equals("moviesPage")) {
                    List<Movie> movieListContains = new ArrayList<Movie>();
                    //verific daca exista filter pt actori
                    //si daca exista retin filmul cu actorul
                    if (action.getActors() != null) {
                        for (Movie movie : moviesPage.getMovieListNoCountry(
                                login.getUserLoggedIn().getCountry())) {
                            if (movie.containsActors(action.getActors())) {
                                movieListContains.add(movie);
                            }
                        }
                        login.setLoggedInMovieList(movieListContains);
                    }

                    List<Movie> movieListContains2 = new ArrayList<Movie>();
                    //verific si pentru genul filmului
                    if (action.getGenre() != null) {

                        for (Movie movie : login.getLoggedInMovieList()) {
                            if (movie.containsGenre(action.getGenre())) {
                                movieListContains2.add(movie);
                            }
                        }
                        login.setLoggedInMovieList(movieListContains2);
                    }
                    //filtru pentru sortarea dupa durata
                    login.sortByDuration(action.getDuration(), action.getRating());

                    //setez output
                    output.setCurrentMovieList(deepCopy(login.getLoggedInMovieList()));
                    output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                        login.getUserLoggedIn().getTokensCount(),
                        login.getUserLoggedIn().getNumFreePremiumMovies(),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                    listToPrint.add(output);
                } else if (action.getFeature().equals("filter")
                        && !currentPage.getTitle().equals("moviesPage")) {

                    output.setError("Error");
                    //actiunea de buy tokens se face decat de pe
                    //pagina upgrades
                } else if (action.getFeature().equals("buy tokens")
                        && currentPage.getTitle().equals("UpgradesPage")) {
                    if (action.getCount() <= login.getUserLoggedIn().getBalance()) {
                        //verific daca am balanta pentru a cumpara
                        //setez in clasa de login
                        Integer tokensCnt = action.getCount();
                        Integer balance = login.getUserLoggedIn().getBalance();
                        login.getUserLoggedIn().setBalance(
                                login.getUserLoggedIn().getBalance() - action.getCount());
                        //setez si in database
                        login.getUserLoggedIn().setTokensCount(action.getCount());
                        dataBase.findUserByUsername(
                                login.getUserLoggedIn().getName()).setTokensCount(tokensCnt);
                        dataBase.findUserByUsername(
                                login.getUserLoggedIn().getName()).setBalance(balance - tokensCnt);

                    }
                    //cumpar premium account
                } else if (action.getFeature().equals("buy premium account")
                        && currentPage.getTitle().equals("UpgradesPage")) {
                    if (login.getUserLoggedIn().getTokensCount() >= 10) {
                        //setez in login
                        login.getUserLoggedIn().setTokensCount(
                                login.getUserLoggedIn().getTokensCount() - 10);
                        login.getUserLoggedIn().setAccountType("premium");
                        //setez in database
                        dataBase.findUserByUsername(
                                login.getUserLoggedIn().getName()).setAccountType("premium");
                    }
                    //cumpar film
                } else if (action.getFeature().equals("purchase")
                        && currentPage.getTitle().equals("seeDetailsPage")) {
                    if (login.IsInPurchased(seeDetailsPage.getSeeDetailsToMovie().getName()) == false) {


                        boolean buyMovie = false;

                        //verific ce tip de cont am
                        if (login.getUserLoggedIn().getAccountType().equals("standard")) {
                            if (login.getUserLoggedIn().getTokensCount() >= 2) {
                                login.getUserLoggedIn().setTokensCount(
                                        login.getUserLoggedIn().getTokensCount() - 2);
                                buyMovie = true;
                            }
                        } else if (login.getUserLoggedIn().getAccountType().equals("premium")) {
                            if (login.getUserLoggedIn().
                                    getNumFreePremiumMovies() > 0) {
                                login.getUserLoggedIn().setNumFreePremiumMovies(
                                        login.getUserLoggedIn().getNumFreePremiumMovies() - 1);
                                buyMovie = true;
                            } else {
                                if (login.getUserLoggedIn().getTokensCount() >= 2) {
                                    login.getUserLoggedIn().setTokensCount(
                                            login.getUserLoggedIn().getTokensCount() - 2);
                                    buyMovie = true;
                                }
                            }
                        }

                        if (buyMovie) {
                            login.getUserLoggedIn().getPurchasedMovies().
                                    add(seeDetailsPage.getSeeDetailsToMovie());
                            dataBase.findUserByUsername(login.getUserLoggedIn().getName()).setPurchasedMovies(login.getUserLoggedIn().getPurchasedMovies());

                            //setez in database
                            dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                                    setNumFreePremiumMovies(login.getUserLoggedIn().getNumFreePremiumMovies());
                            dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                                    setTokensCount(login.getUserLoggedIn().getTokensCount());

                            List<Movie> setMovToL = new ArrayList<Movie>();
                            setMovToL.add(seeDetailsPage.getSeeDetailsToMovie());

                            //setez output
                            output.setCurrentMovieList(deepCopy(setMovToL));
                            output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                                    login.getUserLoggedIn().getTokensCount(),
                                    login.getUserLoggedIn().getNumFreePremiumMovies(),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));
                            listToPrint.add(output);
                        }
                    } else {
                        output.setError("Error");
                    }


                } else if (action.getFeature().equals("watch")
                        && currentPage.getTitle().equals("seeDetailsPage")) {
                    //dverific daca filmul a fost cumparat
                    //pentru a l vizualiza
                    if(login.getUserLoggedIn().getName().equals("Mihail")) {
                        int x = 0;
                    }
                    if(!login.ifWatched(seeDetailsPage.getSeeDetailsToMovie().getName())) {
                        if (login.checkIfMovieIsInPurchased(
                                seeDetailsPage.getSeeDetailsToMovie().getName()) != null) {
                            //setez in login si setez si in database

                            login.getUserLoggedIn().getWatchedMovies().
                                    add(login.checkIfMovieIsInPurchased(
                                            seeDetailsPage.getSeeDetailsToMovie().getName()));
                            dataBase.findUserByUsername(login.getUserLoggedIn().
                                            getName()).
                                    setWatchedMovies(login.getUserLoggedIn().getWatchedMovies());

                            //afisez
                            List<Movie> ml = new ArrayList<>();
                            ml.add(seeDetailsPage.getSeeDetailsToMovie());
                            output.setCurrentMovieList(deepCopy(ml));
                            output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                                    login.getUserLoggedIn().getTokensCount(),
                                    login.getUserLoggedIn().getNumFreePremiumMovies(),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));
                            listToPrint.add(output);
                        } else {
                            output.setError("Error");
                        }
                    }

                } else if (action.getFeature().equals("like")
                        && currentPage.getTitle().equals("seeDetailsPage")) {
                    //pentru a da like verific daca filmul a fost
                    //cumparat
                    Movie likeMovie = login.checkIfMovieIsInPurchased(
                            seeDetailsPage.getSeeDetailsToMovie().getName());
                    if (likeMovie != null) {
                        //setez in database si in login
                        likeMovie.setNumLikes(likeMovie.getNumLikes() + 1);
                        login.getUserLoggedIn().getLikedMovies().add(likeMovie);
                        dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                                setLikedMovies(login.getUserLoggedIn().getLikedMovies());
                        dataBase.findMovieByUsername(
                                likeMovie.getName()).setNumLikes(likeMovie.getNumLikes());
                        List<Movie> setMovToL = new ArrayList<Movie>();
                        setMovToL.add(likeMovie);

                    //afisez la output
                    output.setCurrentMovieList(deepCopy(setMovToL));
                    output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                            login.getUserLoggedIn().getTokensCount(),
                            login.getUserLoggedIn().getNumFreePremiumMovies(),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                            new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                        listToPrint.add(output);
                    } else {
                        output.setError("Error");
                    }

                } else if (action.getFeature().equals("rate")
                        && currentPage.getTitle().equals("seeDetailsPage")) {
                    //pentru a da rate verific daca filmul a fost cumparat
                    Movie rateMovie = login.checkIfMovieIsInPurchased(
                            seeDetailsPage.getSeeDetailsToMovie().getName());
                    if(login.ifRated(rateMovie.getName())) {
                        Output out2 = new Output();
                        List<Movie> ratedM = new ArrayList<Movie>();
                        ratedM.add(seeDetailsPage.getSeeDetailsToMovie());
                        //setez si la output
                        out2.setCurrentMovieList(deepCopy(ratedM));
                        out2.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                                login.getUserLoggedIn().getTokensCount(),
                                login.getUserLoggedIn().getNumFreePremiumMovies(),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                        listToPrint.add(out2);
                        Integer numRatings = rateMovie.getNumRatings();
                        Double ratioRating = rateMovie.getRating();

                        Double aux = numRatings * ratioRating;

                        Double newSum = aux + action.getRate();
                        Double update = newSum - login.getUserLoggedIn().getRememberRated().get(rateMovie.getName());

                        Double newRating = update / numRatings;

                        rateMovie.setRating(newRating);
                        dataBase.findMovieByUsername(
                                rateMovie.getName()).setRating(rateMovie.getRating());
                        List<Movie> nratedM = new ArrayList<Movie>();

                        nratedM.add(dataBase.findMovieByUsername(
                                rateMovie.getName()));
                        System.out.println(nratedM);
                        //setez si la output
                        output.setCurrentMovieList(deepCopy(nratedM));
                        output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                                login.getUserLoggedIn().getTokensCount(),
                                login.getUserLoggedIn().getNumFreePremiumMovies(),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                                new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                        listToPrint.add(output);

                    } else if (rateMovie != null && action.getRate() >= 0 && action.getRate() <= 5 && login.ifRated(rateMovie.getName()) == false) {
                        //daca da setez in login si in database
                        login.getUserLoggedIn().getRememberRated().put(rateMovie.getName(), action.getRate());
                        login.getUserLoggedIn().getRatedMovies().add(rateMovie);
                        rateMovie.setNumRatings(rateMovie.getNumRatings() + 1);
                        rateMovie.setRating((rateMovie.getRating() + action.getRate())
                                / rateMovie.getNumRatings());

                        dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                                setRememberRated(login.getUserLoggedIn().getRememberRated());
                        dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                                setRatedMovies(login.getUserLoggedIn().getRatedMovies());
                        dataBase.findMovieByUsername(
                                rateMovie.getName()).setNumRatings(rateMovie.getNumRatings());
                        dataBase.findMovieByUsername(
                                rateMovie.getName()).setRating(rateMovie.getRating());

                        List<Movie> ratedM = new ArrayList<Movie>();
                        ratedM.add(seeDetailsPage.getSeeDetailsToMovie());
                    //setez si la output
                    output.setCurrentMovieList(deepCopy(ratedM));
                    output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                        login.getUserLoggedIn().getTokensCount(),
                        login.getUserLoggedIn().getNumFreePremiumMovies(),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                        new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));

                        listToPrint.add(output);
                    } else {
                        output.setError("Error");
                    }

                } else if (action.getFeature().equals("subscribe")
                        && currentPage.getTitle().equals("seeDetailsPage")) {
                    if (seeDetailsPage.getSeeDetailsToMovie() == null) {
                        output.setError("Error");
                    }

                }
                else {
                    output.setError("Error");
                }
            } else if (action.getType().equals("back")) {
                if(currentPage.getTitle().equals("homePage")) {
                    output.setError("Error");
                } else if (currentPage.getTitle().equals("loginpage")) {
                    output.setError("Error");
                } else if (currentPage.getTitle().equals("seeDetailsPage")) {
                    currentPage = moviesPage;

                    chPgMovies(currentPage, moviesPage, login, output, listToPrint);

                }
            }
          //  System.out.println(dataBase.getActions().size() + " " + i);




            if (output.getError() != null) {
                listToPrint.add(output);
            }

        }
        if (login.getUserLoggedIn().getAccountType().equals("premium")) {
            // System.out.println("aiciestei" + i);
            System.out.println("da");
            Output output = new Output("da");
            Notification myNot = new Notification("No recommendation", "Recommendation");
            List<Notification> myNotList = new ArrayList<Notification>();
            myNotList.add(myNot);
//            List<Movie> ml = null;
//            output.setCurrentMovieList(ml);
            output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                    login.getUserLoggedIn().getTokensCount(),
                    login.getUserLoggedIn().getNumFreePremiumMovies(),
                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                    new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies())),
                    new ArrayList<>(deepCopyNotification(myNotList))));

            listToPrint.add(output);
        }
        return listToPrint;
    }

    /**
     * deep Copy function for creating
     * the output
     * @param movies
     * @return
     */
    public static List<Movie> deepCopy(final List<Movie> movies) {
        if (movies != null) {
            List<Movie> copiedMovies = new ArrayList<>();
            for (Movie movie : movies) {
                copiedMovies.add(new Movie(movie));
            }
            return copiedMovies;
        }
        return new ArrayList<>();
    }

    public static List<Notification> deepCopyNotification(final List<Notification> notifications) {
        if (notifications != null) {
            List<Notification> copiedNotifications = new ArrayList<>();
            for (Notification notification : notifications) {
                copiedNotifications.add(new Notification(notification));
            }
            return copiedNotifications;
        }
        return new ArrayList<>();
    }


    public static Output setOutput() {
        Output output = new Output();


        return output;
    }

    public void chPgMovies(Page currentPage, MoviesPage moviesPage, Login login, Output output, List<Output> listToPrint) {

        login.setLoggedInMovieList(moviesPage.getMovieListNoCountry(
                login.getUserLoggedIn().getCountry()));


        //setez output
        output.setCurrentMovieList(deepCopy(login.getLoggedInMovieList()));

        output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                login.getUserLoggedIn().getTokensCount(),
                login.getUserLoggedIn().getNumFreePremiumMovies(),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies()))));
        //adaug la lista mea de output
        listToPrint.add(output);
    }
}
