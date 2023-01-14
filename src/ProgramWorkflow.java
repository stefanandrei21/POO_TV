import DataBase.DataBase;
import DataBase.CurrentUser;
import DataBase.Movie;
import DataBase.User;
import DataBase.Actions;
import DataBase.Notification;
import DataBase.Constants;
import Pages.Page;
import Pages.UpgradesPage;
import Pages.SeeDetailsPage;
import Pages.MoviesPage;
import Pages.HomePage;
import Pages.Login;
import Pages.RegisterPage;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

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
     * @return o lista de outputuri
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
                    boolean foundMv = false;
                    Movie seeDetailsMovie = new Movie();
                    //verific daca filmul este in lista de filme
                    for (Movie movie : login.getLoggedInMovieList()) {
                        if (action.getMovie().equals(movie.getName())) {
                            foundMv = true;
                            seeDetailsMovie = new Movie(movie);
                        }
                    }
                    // daca nu e eroare
                    if (!foundMv) {
                        output.setError("Error");

                    } else {
                        //daca e il pun in current list si afisez
                        currentPage = seeDetailsPage;
                        seeDetailsPage.setSeeDetailsToMovie(seeDetailsMovie);
                        List<Movie> helpMovie = new ArrayList<Movie>();
                        helpMovie.add(seeDetailsMovie);

                      setOutput(login, output, listToPrint, helpMovie);
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
                    CurrentUser loggedUser = new CurrentUser(action.getUser(), 0,
                            Constants.NUMBER_OF_FREE_MOVIES,
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

                        updateLoggedInUser(login);
                        //afisez userul

                        setOutput(login, output, listToPrint, null);
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

                        setOutput(login, output, listToPrint, null);

                       currentPage = login;
                        isLogged = 1;

                    }
                    //daca feature-ul este search verific daca sunt pe
                    //moviesPage
                } else if (action.getFeature().equals("search")
                        && currentPage.getTitle().equals("moviesPage")) {
                    searchMovie(login, output, listToPrint, action);
                    //feature filter verific daca sunt pe movies page
                } else if (action.getFeature().equals("filter")
                        && currentPage.getTitle().equals("moviesPage")) {


                    filterMovieList(login, moviesPage, output, listToPrint, action);

                } else if (action.getFeature().equals("filter")
                        && !currentPage.getTitle().equals("moviesPage")) {

                    output.setError("Error");
                    //actiunea de buy tokens se face decat de pe
                    //pagina upgrades
                } else if (action.getFeature().equals("buy tokens")
                        && currentPage.getTitle().equals("UpgradesPage")) {

                    buyTokens(login, action);
                    //cumpar premium account
                } else if (action.getFeature().equals("buy premium account")
                        && currentPage.getTitle().equals("UpgradesPage")) {

                    buyPremiumAccount(login);

                    //cumpar film
                } else if (action.getFeature().equals("purchase")
                        && currentPage.getTitle().equals("seeDetailsPage")) {

                    buyMovie(login, seeDetailsPage, output, listToPrint);

                } else if (action.getFeature().equals("watch")
                        && currentPage.getTitle().equals("seeDetailsPage")) {
                    //dverific daca filmul a fost cumparat
                    //pentru a l vizualiza
                    watchAmovie(login, seeDetailsPage, output, listToPrint);

                } else if (action.getFeature().equals("like")
                        && currentPage.getTitle().equals("seeDetailsPage")) {

                    likeAmovie(login, seeDetailsPage, output, listToPrint);

                } else if (action.getFeature().equals("rate")
                        && currentPage.getTitle().equals("seeDetailsPage")) {

                    rateAmovie(login, seeDetailsPage, output, listToPrint, action);

                } else if (action.getFeature().equals("subscribe")
                        && currentPage.getTitle().equals("seeDetailsPage")) {
                    //Dau subscribe la un gen de filme
                    subscribeToGenre(login, seeDetailsPage, output, action);

                } else {
                    output.setError("Error");
                }
            } else if (action.getType().equals("back")) {
                if (currentPage.getTitle().equals("homePage")) {
                    output.setError("Error");
                } else if (currentPage.getTitle().equals("loginpage")) {
                    output.setError("Error");
                } else if (currentPage.getTitle().equals("seeDetailsPage")) {
                    currentPage = moviesPage;

                    chPgMovies(currentPage, moviesPage, login, output, listToPrint);

                } else if (currentPage.getTitle().equals("moviesPage")) {
                    currentPage = homePage;

                } else {
                    output.setError("Error");
                }
            } else if (action.getType().equals("database")) {
                //Adaug in baza de date
                addToDataBase(login, action);
            }

            if (output.getError() != null) {
                listToPrint.add(output);
            }

        }
        if (login.getUserLoggedIn().getAccountType().equals("premium")) {
            //notificarea finala
            subscribedUserNotification(login, moviesPage, listToPrint);

        }
        return listToPrint;
    }

    /**
     * deep Copy function for creating
     * the output
     * @param movies lista de filme
     * @return lista de filme deep copyed
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

    /**
     * fac deepcopy la o lista de notificari
     * @param notifications lista de clasa notificari
     * @return acelasi array deep copyed
     */
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



    /**
     * schimbare pagina pe movies,setez lista logata de filme(fara tara
     * daca este banata)
     * @param currentPage pagina curenta pe care ma afflu
     * @param moviesPage pagina cu filmele, aici tin toate filmele
     * @param login pagina de login, aici este utilizatorul logat
     * @param output output
     * @param listToPrint lista de outputuri
     */
    public void chPgMovies(final Page currentPage, final MoviesPage moviesPage,
                           final Login login, final Output output, final List<Output> listToPrint) {

        login.setLoggedInMovieList(moviesPage.getMovieListNoCountry(
                login.getUserLoggedIn().getCountry()));


       //adaug la lista mea de output

        setOutput(login, output, listToPrint, deepCopy(login.getLoggedInMovieList()));
    }

    /**
     *  cumparare cont premium, verific daca am tokeni apoi cumpar
     * @param login pagina de login, aici este utilizatorul logat
     */
    public void buyPremiumAccount(final Login login) {
        if (login.getUserLoggedIn().getTokensCount() >= Constants.BUY_PREMIUM_ACC) {
            //setez in login
            login.getUserLoggedIn().setTokensCount(
                    login.getUserLoggedIn().getTokensCount() - Constants.BUY_PREMIUM_ACC);
            login.getUserLoggedIn().setAccountType("premium");
            //setez in database
            dataBase.findUserByUsername(
                    login.getUserLoggedIn().getName()).setAccountType("premium");
        }
    }

    /**
     * cumparare film, verific daca filmul nu este deja cumparat,
     * verific ce tip de cont am, si apoi cumpar sau nu in functie
     * de asta
     * @param login pagina de login, aici este utilizatorul logat
     * @param seeDetailsPage pagina de see details, aici am filmul
     * @param output outputul
     * @param listToPrint lista de output uri
     */
    public void buyMovie(final Login login, final SeeDetailsPage seeDetailsPage,
                         final Output output, final List<Output> listToPrint) {
        if (!login.isInPurchased(seeDetailsPage.getSeeDetailsToMovie().getName())) {


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
                dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                        setPurchasedMovies(login.getUserLoggedIn().getPurchasedMovies());

                //setez in database
                dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                        setNumFreePremiumMovies(login.getUserLoggedIn().getNumFreePremiumMovies());
                dataBase.findUserByUsername(login.getUserLoggedIn().getName()).
                        setTokensCount(login.getUserLoggedIn().getTokensCount());

                List<Movie> setMovToL = new ArrayList<Movie>();
                setMovToL.add(seeDetailsPage.getSeeDetailsToMovie());

                //setez output
                setOutput(login, output, listToPrint, deepCopy(setMovToL));
            }
        } else {
            output.setError("Error");
        }

    }

    /**
     * metoda pentru a vedea un film, verific daca utilizatorul a cumparat
     * @param login pagina de login, aici este utilizatorul logat
     * @param seeDetailsPage pagina de see details, aici am filmul
     * @param output outputul
     * @param listToPrint lista de output uri
     */
    public void watchAmovie(final Login login, final SeeDetailsPage seeDetailsPage,
                            final Output output, final List<Output> listToPrint) {

        if (!login.ifWatched(seeDetailsPage.getSeeDetailsToMovie().getName())) {
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
                List<Movie> ml = new ArrayList<Movie>();
                ml.add(seeDetailsPage.getSeeDetailsToMovie());
                setOutput(login, output, listToPrint, deepCopy(ml));
            } else {
                output.setError("Error");
            }
        }
    }

    /**
     * metoda pentru a da like unui film, verific daca filmul a fost cumparat,
     * vazut
     * @param login pagina de login, aici este utilizatorul logat
     * @param seeDetailsPage pagina de see details, aici am filmul
     * @param output outputul
     * @param listToPrint lista de outputuri
     */
    public void likeAmovie(final Login login, final SeeDetailsPage seeDetailsPage,
                           final Output output, final List<Output> listToPrint) {
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

            setOutput(login, output, listToPrint, deepCopy(setMovToL));
        } else {
            output.setError("Error");
        }
    }

    /**
     * metoda pentru a da rate unui film, verific daca utilizatorul
     * a dat deja rate, daca a dat fac override la rate
     * @param login pagina de login, aici este utilizatorul logat
     * @param seeDetailsPage pagina de see details, aici am filmul
     * @param output outputul
     * @param listToPrint lista de outputuri
     * @param action lista de actiuni
     */
    public void rateAmovie(final Login login, final SeeDetailsPage seeDetailsPage,
                           final Output output, final List<Output> listToPrint,
                           final Actions action) {
        Movie rateMovie = login.checkIfMovieIsInPurchased(
                seeDetailsPage.getSeeDetailsToMovie().getName());
        if (rateMovie != null && login.ifRated(rateMovie.getName())) {
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
            Double update = newSum - login.getUserLoggedIn().
                    getRememberRated().get(rateMovie.getName());
            Double newRating = update / numRatings;
            rateMovie.setRating(newRating);

            dataBase.findMovieByUsername(
                    rateMovie.getName()).setRating(rateMovie.getRating());
            List<Movie> nratedM = new ArrayList<Movie>();

            nratedM.add(dataBase.findMovieByUsername(
                    rateMovie.getName()));

            //setez si la output

            setOutput(login, output, listToPrint, deepCopy(nratedM));
        } else if (rateMovie != null && action.getRate() >= 0
                && action.getRate() <= Constants.RATE_MAX
                && !login.ifRated(rateMovie.getName())) {

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

            setOutput(login, output, listToPrint, deepCopy(ratedM));
        } else {
            output.setError("Error");
        }
    }

    /**
     *
     * @param login pagina de login, aici este utilizatorul logat
     * @param seeDetailsPage pagina de see details, aici am filmul
     * @param output outputul pe care il creez
     * @param action actiunea la care sunt, din lista mea de actiuni
     */
    public void subscribeToGenre(final Login login, final SeeDetailsPage seeDetailsPage,
                                 final Output output, final Actions action) {
        boolean ok = false;
        for (String str : login.getUserLoggedIn().getSubscribed()) {
            if (action.getSubscribedGenre().equals(str)) {
                ok = true;
                break;
            }
        }
        if (seeDetailsPage.getSeeDetailsToMovie() == null) {
            output.setError("Error");
        } else if (ok) {
            output.setError("Error");
        } else {
            boolean verifyGenre = false;
            for (String genre : seeDetailsPage.getSeeDetailsToMovie().getGenres()) {
                if (genre.equals(action.getSubscribedGenre())) {
                    verifyGenre = true;
                    break;
                }
            }
            if (verifyGenre) {
                login.getUserLoggedIn().getSubscribed().add(action.getSubscribedGenre());
            }
        }
    }

    /**
     * adaug un film in database, verific daca filmul nu este deja
     * apoi adaug
     * @param login pagina de login, aici este utilizatorul logat
     * @param action actiunea la care sunt, din lista mea de actiuni
     */
    public void addToDataBase(final Login login, final Actions action) {
        if (action.getFeature().equals("add")) {
            Movie addNewMovie = new Movie(action.getNameOfMovie(),
                    Integer.parseInt(action.getYearOfMovie()),
                    action.getDurationOfMovie(), action.getGenre(), action.getCountriesBanned(),
                    action.getActors(), 0, 0.0, 0);

            dataBase.getMovieList().add(addNewMovie);
            boolean verifyIfNotification = false;
            for (String genre : addNewMovie.getGenres()) {
                for (String subscribed : login.getUserLoggedIn().getSubscribed()) {
                    if (genre.equals(subscribed)) {
                        verifyIfNotification = true;
                        break;
                    }
                }
            }
            if (verifyIfNotification) {

                Notification notif = new Notification(addNewMovie.getName(), "ADD");
                login.getUserLoggedIn().getNotifications().add(notif);

            }
        }
    }

    /**
     *  metoda pentru a notifica utilizator ul la finalul actiunilor
     *  verific daca filmul este deja recomandat nu il mai recomand,
     *  cu ajutorul mapei in current user vad care este cel mai likeuit
     *  film sortand mapa
     * @param login pagina de login, aici este utilizatorul logat
     * @param moviesPage pagina cu filmele, aici tin toate filmele
     * @param listToPrint lista pe care o printez
     */
    public void subscribedUserNotification(final Login login, final MoviesPage moviesPage,
                                           final List<Output> listToPrint) {

        Output output = new Output("da");

        List<Notification> myNotList = new ArrayList<Notification>(deepCopyNotification(login.
                getUserLoggedIn().getNotifications()));


        for (Movie movie : login.getUserLoggedIn().getLikedMovies()) {
            for (String genre : movie.getGenres()) {
                if (login.getUserLoggedIn().getGenreLikes().get(genre) == null) {
                    login.getUserLoggedIn().getGenreLikes().put(genre, 1);
                } else {
                    Integer update = login.getUserLoggedIn().getGenreLikes().get(genre) + 1;
                    login.getUserLoggedIn().getGenreLikes().put(genre, update);

                }
            }
        }
        // comparator pentru a sorta mapa pe care o sa o creez
        //pentru a vedea care este cel mai likeuit film
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return login.getUserLoggedIn().getGenreLikes().get(o2)
                        - login.getUserLoggedIn().getGenreLikes().get(o2);
            }
        };
        Map<String, Integer> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(login.getUserLoggedIn().getGenreLikes());

        if (sortedMap != null) {
            List<String> reccomend = new ArrayList<String>();

            List<Movie> myMovieListR = new ArrayList<>(deepCopy(moviesPage.
                    getMovieListNoCountry(login.getUserLoggedIn().getCountry())));
            List<Movie> movieListHelper = new ArrayList<>();
            for (Movie mv : myMovieListR) {
                for (Notification nt : myNotList) {
                    if (!mv.getName().equals(nt.getMovieName())) {
                        movieListHelper.add(mv);
                    }
                }
            }
            for (Movie movie : movieListHelper) {
                for (String genre : movie.getGenres()) {

                    if (genre.equals(sortedMap.entrySet().iterator().next().getKey())
                            && !login.isInPurchased(movie.getName())) {

                        reccomend.add(movie.getName());
                    }
                }
            }
            for (String str : reccomend) {
                myNotList.add(new Notification(str, "Recommendation"));
            }
        }
        if (myNotList.size() == 0) {
            Notification myNot = new Notification("No recommendation", "Recommendation");
            myNotList.add(myNot);
        }
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

    /**
     * cumpar tokeni in functie daca am balanta sau nu
     * @param login pagina de login, aici este utilizatorul logat
     * @param action actiunea la care sunt, din lista mea de actiuni
     */
    public void buyTokens(final Login login, final Actions action) {
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
    }

    /**
     *  filtrez filmele in functie de inputul primit
     * @param login pagina de login, aici este utilizatorul logat
     * @param moviesPage pagina cu filmele, aici tin toate filmele
     * @param output outputul pe care il creez
     * @param listToPrint lista mea de outputuri
     * @param action actiunea la care sunt, din lista mea de actiuni
     */
    public void filterMovieList(final Login login, final MoviesPage moviesPage,
                                final Output output, final List<Output> listToPrint,
                                final Actions action) {
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

        setOutput(login, output, listToPrint, deepCopy(login.getLoggedInMovieList()));
    }

    /**
     *  Metoda pentru cautarea unui film,
     *  se verifica daca exista filmul si daca tara nu este banata
     * @param login pagina de login, aici este utilizatorul logat
     * @param output outputul pe care il creez
     * @param listToPrint lista mea de outputuri
     * @param action actiunea la care sunt, din lista mea de actiuni
     */
    public void searchMovie(final Login login, final Output output,
                            final List<Output> listToPrint, final Actions action) {

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
    }
    /**
     *  Setez Outputul
     * @param login pagina de login, aici este utilizatorul logat
     * @param output outputul pe care il creez
     * @param listToPrint lista mea de outputuri
     * @param helpMovie filmpul pe care il setez la output
     */
    public void setOutput(final Login login, final Output output,
                          final List<Output> listToPrint, final List<Movie> helpMovie) {
        output.setCurrentMovieList(deepCopy(helpMovie));

        output.setCurrentUser(new CurrentUser(login.getUserLoggedIn(),
                login.getUserLoggedIn().getTokensCount(),
                login.getUserLoggedIn().getNumFreePremiumMovies(),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getPurchasedMovies())),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getWatchedMovies())),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getLikedMovies())),
                new ArrayList<>(deepCopy(login.getUserLoggedIn().getRatedMovies())),
                new ArrayList<>(deepCopyNotification(login.getUserLoggedIn().getNotifications()))));
        listToPrint.add(output);
    }

    /**
     * fac update la filme in caz in care un utilizator este
     * nelogat dar se fac schimbari de likeuri etc la filmele
     * respective
     * @param login pagina de login, aici este utilizatorul logat
     */
    public void updateLoggedInUser(final Login login) {
        login.getUserLoggedIn().setPurchasedMovies(login.updatePurchasedMl(dataBase.
                getMovieList()));
        login.getUserLoggedIn().setWatchedMovies(login.updateWatchedMl(dataBase.getMovieList()));
        login.getUserLoggedIn().setRatedMovies(login.updateRatedMl(dataBase.getMovieList()));
        login.getUserLoggedIn().setLikedMovies(login.updateLikedMl(dataBase.getMovieList()));
        login.getUserLoggedIn().setRememberRated(dataBase.
                findUserByUsername(login.getUserLoggedIn().getName()).getRememberRated());


    }
}
