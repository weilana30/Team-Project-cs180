/**
 * LoginGUIInterface.java
 * <p>
 * An interface for the future LoginGUI.java that contains all
 * the methods that will be associated with it as well in the future.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */
public interface LoginGUIInterface {

    // FOR PHASE 2/3

    User[] getUsers();

    void setUsers(User[] users);

    // if they would rather sign in using email or phone number
    String getEmail();

    void setEmail(String email);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);
}
