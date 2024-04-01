/**
 * SignupGUIInterface.java
 * <p>
 * An interface for the future SignupGUI.java that contains all
 * the methods that will be associated with it as well in the future.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */
public interface SignupGUIInterface {

    // FOR PHASE 2/3
    // all info below will be prompted at sign up
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

    User[] getUsers();

    void setUsers(User[] users);

}
