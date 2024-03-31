public interface SignupInterface {

    boolean createUser(String username, String password);
    boolean checkUsername(String username);
    boolean checkPassword(String password);

}
