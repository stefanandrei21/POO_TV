package DataBase;

import java.util.ArrayList;
import java.util.List;

public final class Movie {
    private String name;
    private Integer year;
    private Integer duration;
    private List<String> genres;
    private List<String> actors;
    private List<String> countriesBanned;


    private Integer numLikes;
    private Double rating;
    private Integer numRatings;

    public Movie() {
        this.name = null;
        this.year = null;
        this.duration = null;
        this.genres = null;
        this.actors = null;
        this.countriesBanned = null;
        this.numRatings = 0;
        this.numLikes = 0;
        this.rating = 0.00;
    }

    public Movie(final String name, final Integer year, final Integer duration,
                 final List<String> genres, final List<String> countriesBanned,
                 final List<String> actors) {
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.genres = genres;
        this.actors = actors;
        this.countriesBanned = countriesBanned;

    }


    public Movie(final Movie movie) {
        this.name = movie.getName();
        this.year = movie.getYear();
        this.duration = movie.getDuration();
        this.genres = movie.getGenres();
        this.actors = movie.getActors();
        this.countriesBanned = movie.getCountriesBanned();
        this.numLikes = movie.getNumLikes();
        this.rating = movie.getRating();
        this.numRatings = movie.getNumRatings();
    }
    public Movie(final String name, final Integer year, final Integer duration,
                 final List<String> genres, final List<String> countriesBanned,
                 final List<String> actors, final Integer numLikes, final Double rating,
                 final Integer numRatings) {
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.genres = genres;
        this.actors = actors;
        this.countriesBanned = countriesBanned;
        this.numLikes = numLikes;
        this.rating = rating;
        this.numRatings = numRatings;
    }

    /**
     * verifica daca contine actorul in lista de actori
     * @param cntActors
     * @return
     */
    public boolean containsActors(final List<String> cntActors) {
        return this.actors.containsAll(cntActors);
    }

    /**
     * verifica daca contine genul
     * @param genre
     * @return
     */
    public boolean containsGenre(final List<String> genre) {
        return this.genres.containsAll(genre);
    }
    public Integer getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(final Integer numLikes) {
        this.numLikes = numLikes;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(final Double rating) {
        this.rating = rating;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(final Integer numRatings) {
        this.numRatings = numRatings;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public Integer getDuration() {
        return duration;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(final List<String> genres) {
        this.genres = genres;
    }

    public List<String> getCountriesBanned() {
        return countriesBanned;
    }

    public void setCountriesBanned(final List<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(final List<String> actors) {
        this.actors = actors;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Database.Movie{"
                + "name='" + name + '\''
                + ", year=" + year
                + ", duration=" + duration
                + ", genres=" + genres
                + ", actors=" + actors
                + ", countriesBanned=" + countriesBanned
                + ", numLikes=" + numLikes
                + ", rating=" + rating
                + ", numRatings=" + numRatings
                + '}';
    }
}
