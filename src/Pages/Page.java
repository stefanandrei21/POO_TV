package Pages;

public class Page {
    private String title;

    public Page() { }

    public Page(final String title) {
        this.title = title;
    }

    /**
     * getter pt titlu
     * @return titlu
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter pt titlu
     * @param title titlu
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * to string override method
     */
    @Override
    public String toString() {
        return "Pages.Page{"
                + "title='" + title + '\''
                + '}';
    }
}
