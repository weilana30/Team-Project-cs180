import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User.java
 * <p>
 * A class that defines the user and its properties as well as
 * includes methods for equals and toString.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */
public class User implements UserInterface {
    private String name;
    private String username;
    private String password;
    private String email;
    private String birthday;
    private String phoneNumber;

    // constructor for User.java that only takes 2 required parameters (username and password)
    // all other fields will be set to null for now as they will be set using setters based on user input from
    // the sign-up page
    public User(String username, String password) {
        this.name = null;
        this.username = username;
        this.password = password;
        this.email = null;
        this.birthday = null;
        this.phoneNumber = null;
    }

    public User(String userString) {
        String [] information = userString.split(", ");
        this.username = information[0];
        this.name = information[1];
        this.password = information[2];
        this.email = information[3];
        this.phoneNumber = information[4];
        this.birthday = information[5];
    }

    // all getters and setters involving each field
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public synchronized void addUserToFile() throws IOException {
        File file = new File("Users.txt");
        FileOutputStream fos = new FileOutputStream(file, true);
        PrintWriter pw = new PrintWriter(fos);
        pw.println(this);
        pw.close();
        fos.close();
    }

    // .equals method to compare user objects with one another
    @Override
    public boolean equals(Object o) {

        if (o.getClass() == User.class) {
            return ((User) o).getUsername().equals(this.getUsername());
        } else {
            return false;
        }

    }

    // .toString() method to create a string representation of a user which could be useful
    // if we were to store all users in a file
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s", username, name, password, email, phoneNumber, birthday);
    }

}
