import java.util.ArrayList;

/**
 * profile
 *
 * <p>
 * Creates an object that restores the information of a user
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
public class Profile {
    private ArrayList<User> users;

    public Profile() {
        this.users = new ArrayList<>();
    }
    public void addUser(User user) {
        users.add(user);
    }
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public String getEmailByUsername(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            return user.getEmail();
        } else {
            return null;
        }
    }
    public String getPhoneNumberByUsername(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            return user.getPhoneNumber();
        } else {
            return null;
        }
    }
    public String getBirthdayByUsername(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            return user.getBirthday();
        } else {
            return null;
        }
    }
    public String getPasswordByUsername(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            return user.getPassword();
        } else {
            return null;
        }
    }
    public String getNameByUsername(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            return user.getName();
        } else {
            return null;
        }
    }
}

