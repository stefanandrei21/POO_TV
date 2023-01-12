package DataBase;


public final class Notification {
    private final String movieName;
    private final String message;

    public Notification(final String movieName, final String message) {
        this.movieName = movieName;
        this.message = message;
    }
    public Notification(final Notification notification) {
        this.movieName = notification.getMovieName();
        this.message = notification.getMessage();
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Notification{" + "movieName='" + movieName + '\''
                + ", message='" + message + '\''
                + '}';
    }
}
