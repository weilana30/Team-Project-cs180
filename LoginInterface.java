public interface LoginInterface {

    boolean authenticateUsername(String username, String password);

    boolean authenticateEmail(String email, String password);

    boolean authenticatePhone(String phoneNumber, String password);

    User[] getUsers();

    void setUsers(User[] users);

}
