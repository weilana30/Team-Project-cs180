public interface LoginInterface {

    boolean authenticate(String username, String password);

    User[] getUsers();

    void setUsers(User[] users);

}
