/**
 * SignupInterface.java
 * <p>
 * An interface for Signup.java that contains all
 * the methods associated with it as well.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */
public interface SignupInterface {

    boolean createUser(String username, String password);

    boolean checkUsername(String username);

    boolean checkPassword(String password);

    User[] getUsers();

    void setUsers(User[] users);

}
