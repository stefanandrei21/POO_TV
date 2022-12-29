package DataBase;

import java.util.List;

public final class Actions {
    private final String type;
    private final String page;
    private final String feature;

    private final Integer rate;

    private final Integer count;
    private final User user;

    private final String movie;

    private String startsWith;

    private final String rating;

    private final String duration;

    private final List<String> actors;
    private final List<String> genre;
    private final List<String> countriesBanned;
    private final Integer durationOfMovie;
    private final String nameOfMovie;
    private final String yearOfMovie;

    public Actions() {
        this.countriesBanned = null;
        this.type = null;
        this.page = null;
        this.feature = null;
        this.user = null;
        this.startsWith = null;
        this.rating = null;
        this.duration = null;
        this.actors = null;
        this.genre = null;
        this.movie = null;
        this.count = 0;
        this.rate = 0;
        this.durationOfMovie = 0;
        this.nameOfMovie = null;
        this.yearOfMovie = null;
    }

    public Actions(final String type, final String page, final String feature,
                   final User user, final String startsWith,
                   final String rating, final String duration, final List<String> actors,
                   final List<String> genre, final String movie, final Integer count,
                   final Integer rate, final List<String> countriesBanned,
                   final Integer durationOfMovie, final String nameOfMovie,
                   final String yearOfMovie) {
        this.type = type;
        this.page = page;
        this.feature = feature;
        this.user = user;
        this.startsWith = startsWith;
        this.rating = rating;
        this.duration = duration;
        this.actors = actors;
        this.genre = genre;
        this.movie = movie;
        this.count = count;
        this.rate = rate;
        this.countriesBanned = countriesBanned;
        this.durationOfMovie = durationOfMovie;
        this.nameOfMovie = nameOfMovie;
        this.yearOfMovie = yearOfMovie;
    }

    public Actions(final String type, final String page,
                   final String feature, final User user,
                   final String duration, final String rating,
                   final List<String> actors, final List<String> genre,
                   final String movie, final Integer count,
                   final Integer rate, final List<String> countriesBanned,
                   final Integer durationOfMovie, final String nameOfMovie,
                   final String yearOfMovie) {
        this.type = type;
        this.page = page;
        this.feature = feature;
        this.user = user;
        this.duration = duration;
        this.rating = rating;
        this.actors = actors;
        this.genre = genre;
        this.movie = movie;
        this.count = count;
        this.rate = rate;
        this.countriesBanned = countriesBanned;
        this.durationOfMovie = durationOfMovie;
        this.nameOfMovie = nameOfMovie;
        this.yearOfMovie = yearOfMovie;
    }

    public Integer getRate() {
        return rate;
    }

    public Integer getCount() {
        return count;
    }



    public String getMovie() {
        return movie;
    }


    public String getRating() {
        return rating;
    }


    public String getDuration() {
        return duration;
    }


    public List<String> getActors() {
        return actors;
    }



    public List<String> getGenre() {
        return genre;
    }



    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(final String startsWith) {
        this.startsWith = startsWith;
    }

    public String getType() {
        return type;
    }



    public String getPage() {
        return page;
    }



    public String getFeature() {
        return feature;
    }


    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Actions{"
                + "type='" + type + '\''
                + ", page='" + page + '\''
                + ", feature='" + feature + '\''
                + ", rate=" + rate
                + ", count=" + count
                + ", user=" + user
                + ", movie='" + movie + '\''
                + ", startsWith='" + startsWith + '\''
                + ", rating='" + rating + '\''
                + ", duration='" + duration + '\''
                + ", actors=" + actors
                + ", genre=" + genre
                + ", countriesBanned =" + countriesBanned
                + ", nameOfMovie=" + nameOfMovie
                + ", yearOfMovie=" + yearOfMovie
                + '}';
    }
}
