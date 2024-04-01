public class Login implements LoginInterface {

    private User[] users;

    // constructor for Login.java which takes in one parameter (User[] users)
    // and instantiates it as long as it is not null
    public Login(User[] users) {
        if (users == null) {
            users = new User[10];
        }
        this.users = users;
    }

    // this method ensures that the username and password inputted match the
    // username and password that is in the users array
    public boolean authenticate(String username, String password) {

        // this iterates through users array and ensures that both inputs are in the users array as a user
        // returns true if successful
        for (User user : users) {
            if (user != null) {
                if (user.getUsername().equalsIgnoreCase(username) & user.getPassword().equals(password)) {
                    return true;
                }
            }
        }

        // otherwise returns false
        return false;

    }

    // getter and setter for users
    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
