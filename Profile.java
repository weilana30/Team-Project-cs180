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
public class Profile {
    private ArrayList<User> users;

    public Profile() {
        boolean usersAdded = readFile(new File("Users.txt"));
        if (!usersAdded) {
            this.users = new ArrayList<>();
        }
          try {
            File users = new File("Users.txt");
            BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"));
            String userString = bfr.readLine();
            while(userString != null) {
                User user = new User(userString);
                this.users.add(user);
                userString = bfr.readLine();
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean readFile(File usersFile) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(usersFile))) {

            String line = bfr.readLine();

            while (line != null) {
                String[] userInfo = line.split(" ");
                User user = new User(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
                users.add(user);
                line = bfr.readLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
    public User getUserByPhoneNumber(String phoneNumber) {
        for (User user : users) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
