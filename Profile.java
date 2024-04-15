import java.io.*;
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
public class Profile implements ProfileInterface {
    private ArrayList<User> allUsers;

    public Profile() {
        this.allUsers = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"));
            String userString = bfr.readLine();
            while (userString != null) {
                User user = new User(userString);
                this.allUsers.add(user);
                userString = bfr.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(User user) {
        allUsers.add(user);
    }

    public User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return this.allUsers;
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

    public User getUserByPhoneNumber(String phoneNumber) {
        for (User user : allUsers) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByEmail(String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
