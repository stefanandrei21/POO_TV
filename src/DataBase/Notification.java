package DataBase;

public class Notification {
    private String movieName;
    private String message;

    public Notification(String movieName, String message) {
        this.movieName = movieName;
        this.message = message;
    }
    public Notification(Notification notification) {
        this.movieName = notification.getMovieName();
        this.message = notification.getMessage();
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(final String movieName) {
        this.movieName = movieName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
