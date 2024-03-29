public class User implements UserInterface {
    private String name;
    private String username;
    private String password;
    private String email;
    private String birthday;

    public User(String username, String password) {
        this.name = null;
        this.username = username;
        this.password = password;
        this.email = null;
        this.birthday = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {

        if (o.getClass() == User.class) {
            return ((User) o).getUsername().equals(this.getUsername());
        }
        else {
            return false;
        }

    }

    @Override
    public String toString() {
        return String.format("Username: %s, Password: %s,",
                getUsername(), getPassword());
    }
}
