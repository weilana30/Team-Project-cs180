/**
 * UserInterface.java
 * <p>
 * An interface for User.java that contains all
 * the methods associated with it as well.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */
public interface UserInterface {

    String getName();

    void setName(String name);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getBirthday();

    void setBirthday(String birthday);

    String getEmail();

    void setEmail(String email);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);
    void addUserToFile();

    boolean equals(Object o);

    String toString();
}
