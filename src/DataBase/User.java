package DataBase;


public class User {
    private String name;
    private String password;
    private String accountType;
    private String country;
    private Integer balance;
    private boolean loggedIn = false;

    public User() {
        this.name = null;
        this.password = null;
        this.accountType = null;
        this.country = null;
        this.balance = null;
    }
    public User(final String name, final String password, final String accountType,
                final String country, final Integer balance) {
        this.name = name;
        this.password = password;
        this.accountType = accountType;
        this.country = country;
        this.balance = balance;
    }

    public User(final User user) {
        this.name = user.name;
        this.password = user.password;
        this.balance = user.balance;
        this.country = user.country;
        this.accountType = user.accountType;
    }


    /**
     * verific daca user este logat
     * @return userul logat
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * setez user logat
     */
    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * getter pt nume
     * @return nume
     */
    public String getName() {
        return name;
    }

    /**
     * seter pt nume
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * getter pt parola
     * @return parola
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter pt parola
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * getter pt account type
     * @return tipul contului
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * setter pt account type
     */
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    /**
     * getter pt country
     * @return tara din care este utilizatorul
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter pt country
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * getter pt balanta
     * @return balanta contului
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * setter pt balanta
     */
    public void setBalance(final Integer balance) {
        this.balance = balance;
    }

    /**
     * Override tostring
     */
    @Override
    public String toString() {
        return "Database.User{"
                + "name='" + name + '\''
                + ", password='" + password + '\''
                + ", accountType='" + accountType + '\''
                + ", country='" + country + '\''
                + ", balance=" + balance
                + '}';
    }

}
