package Pages;
import DataBase.Movie;
import java.util.ArrayList;
import java.util.List;

public final class MoviesPage extends Page {
    private List<Movie> movieListForPage = new ArrayList<Movie>();

    public MoviesPage(final List<Movie> movieList) {
        this.movieListForPage = movieList;
    }

    public MoviesPage(final String title, final List<Movie> movieList) {
        super(title);
        this.movieListForPage = movieList;
    }

    public List<Movie> getMovieList() {
        return movieListForPage;
    }

    /**
     * returnez lista fara tara care este banata
     * @param country
     * @return
     */
    public List<Movie> getMovieListNoCountry(final String country) {
        List<Movie> movieListNoBannedCountry = new ArrayList<Movie>();
        for (Movie movie : movieListForPage) {
            boolean ml = false;
            for (String banned : movie.getCountriesBanned()) {
                if (banned.equals(country)) {
                    ml = true;
                }
            }
            if (!ml) {
                movieListNoBannedCountry.add(movie);
            }
        }
        return  movieListNoBannedCountry;

    }

    public void setMovieList(final List<Movie> movieList) {
        this.movieListForPage = movieList;
    }


    @Override
    public String toString() {
        return "Pages.MoviesPage{"
                + "movieList=" + movieListForPage
                + '}';
    }
}
