public class Login implements LoginInterface {

    private User[] users;

    public Login(User[] users) {
        this.users = users;
    }

    public boolean authenticate(String username, String password) {

        for (User user : users) {
            if (user != null) {
                if (user.getUsername().equalsIgnoreCase(username) & user.getPassword().equals(password)) {
                    return true;
                }
            }
        }

        return false;

    }

}
