public class Signup implements SignupInterface {

    private User[] users;

    public Signup(User[] users) {
        if (users == null) {
            users = new User[10];
        }
        this.users = users;
    }

    public boolean createUser(String username, String password) {

        int index = 0;

        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                index += 1;
            }
        }

        if (username != null & password != null) {
            if (checkUsername(username) & checkPassword(password)) {
                User user = new User(username, password);
                users[index] = user;
                return true;
            }
        }

        return false;

    }

    public boolean checkUsername(String username) {

        if (this.users == null) {
            this.users = new User[10];
        }

        for (User user : this.users) {
            if (user != null) {
                if (user.getUsername().equals(username)) {
                    return false;
                }
            }
        }

        return true;

    }

    public boolean checkPassword(String password) {

        String chars = "~ ` ! @ # $ % ^ & * ( ) - _ + = { [ } ] | \\ ' ; : ? / > . < ,";
        String ints = "1 2 3 4 5 6 7 8 9 0";
        String letters = "a b c d e f g h i j k l m n o p q r s t u v w x y z";

        String[] requiredChars = chars.split(" ");
        String[] requiredInts = ints.split(" ");
        String[] requiredLetters = letters.split(" ");

        boolean hasChar = false;
        boolean hasInt = false;
        boolean hasUpper = false;
        boolean hasLower = false;

        for (String character : requiredChars) {
            if (password.contains(character)) {
                hasChar = true;
                break;
            }
        }

        for (String number : requiredInts) {
            if (password.contains(number)) {
                hasInt = true;
                break;
            }
        }

        for (String letter : requiredLetters) {
            if (password.contains(letter.toUpperCase())) {
                hasUpper = true;
                break;
            }
        }

        for (String letter : requiredLetters) {
            if (password.contains(letter)) {
                hasLower = true;
                break;
            }
        }

        return hasChar & hasInt & hasUpper & hasLower;

    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
