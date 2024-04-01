/**
 * LoginInterface.java
 * <p>
 * An interface for Login.java that contains all
 * the methods associated with it as well.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */
public interface LoginInterface {

    boolean authenticateUsername(String username, String password);

    boolean authenticateEmail(String email, String password);

    boolean authenticatePhone(String phoneNumber, String password);

    User[] getUsers();

    void setUsers(User[] users);

}