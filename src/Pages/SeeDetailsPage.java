package Pages;

import DataBase.Movie;

public final class SeeDetailsPage extends Page {
    private Movie seeDetailsToMovie;
    public SeeDetailsPage(final String seeDetailsPage) {
        super(seeDetailsPage);
    }

    public Movie getSeeDetailsToMovie() {
        return seeDetailsToMovie;
    }

    public void setSeeDetailsToMovie(final Movie seeDetailsToMovie) {
        this.seeDetailsToMovie = seeDetailsToMovie;
    }
}
