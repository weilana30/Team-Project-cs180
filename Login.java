/**
 * Login.java
 * <p>
 * A class that defines the backend processes for login
 * and takes in one parameter which is the user array users of
 * all users currently on the platform.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */
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

    // this method ensures that if user chooses username log in method,
    // the username and password inputted are both in the users array as a user
    public boolean authenticateUsername(String username, String password) {

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

    // this method ensures that if user chooses email log in method,
    // the email and password inputted are both in the users array as a user
    public boolean authenticateEmail(String email, String password) {

        // this iterates through users array and ensures that both inputs are in the users array as a user
        // returns true if successful
        for (User user : users) {
            if (user != null) {
                if (user.getEmail().equalsIgnoreCase(email) & user.getPassword().equals(password)) {
                    return true;
                }
            }
        }

        // otherwise returns false
        return false;

    }

    // this method ensures that if user chooses phone number log in method,
    // the phoneNumber and password inputted are both in the users array as a user
    public boolean authenticatePhone(String phoneNumber, String password) {

        // these check if the phone number entered has hyphens or spaces between its numbers
        // if it does then it will remove them and create a new phone number without these symbols
        if (phoneNumber.contains("-")) {
            String[] splitPhone = phoneNumber.split("-");
            phoneNumber = "";
            for (String part : splitPhone) {
                phoneNumber += part;
            }
        }
        if (phoneNumber.contains(" ")) {
            String[] splitPhone = phoneNumber.split(" ");
            phoneNumber = "";
            for (String part : splitPhone) {
                phoneNumber += part;
            }
        }

        // this iterates through users array and ensures that both inputs are in the users array as a user
        // returns true if successful
        for (User user : users) {
            if (user != null) {
                if (user.getPhoneNumber().equalsIgnoreCase(phoneNumber) &
                        user.getPassword().equals(password)) {
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
