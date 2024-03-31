public class Signup implements SignupInterface {

    private User[] users;

    // constructor for Signup.java which takes in one parameter (User[] users)
    // which is an array of all users on the platform
    public Signup(User[] users) {
        if (users == null) {
            users = new User[10];
        }
        this.users = users;
    }

    // this method creates a new user using Strings username and password as parameters
    public boolean createUser(String username, String password) {

        int index = 0;

        // this gets the index of the next available spot in the users array that is null
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                index += 1;
            }
        }

        // this creates the new user and appends it to the array using the index from before
        // also returns true if successful
        if (username != null & password != null) {
            if (checkUsername(username) & checkPassword(password)) {
                User user = new User(username, password);
                users[index] = user;
                return true;
            }
        }

        // otherwise return false
        return false;

    }

    // this method checks if the username inputted has not already been taken by another user
    public boolean checkUsername(String username) {

        // ensures users is not null to bypass NullPointerException
        if (this.users == null) {
            this.users = new User[10];
        }

        // checks if any preexisting user in the users array has the same username as
        // the username to check
        // if already exists, then returns false
        for (User user : this.users) {
            if (user != null) {
                if (user.getUsername().equals(username)) {
                    return false;
                }
            }
        }

        // otherwise returns true
        return true;

    }

    // this method checks if the password inputted meets all the requirements for a secure password
    public boolean checkPassword(String password) {

        // creating 3 different string representations of allowed symbols from a keyboard
        String chars = "~ ` ! @ # $ % ^ & * ( ) - _ + = { [ } ] | \\ ' ; : ? / > . < ,";
        String ints = "1 2 3 4 5 6 7 8 9 0";
        String letters = "a b c d e f g h i j k l m n o p q r s t u v w x y z";

        // splitting these different strings into string arrays, so they are iterable
        String[] requiredChars = chars.split(" ");
        String[] requiredInts = ints.split(" ");
        String[] requiredLetters = letters.split(" ");

        // initializing 4 boolean conditions that must be met by the end to be successful
        boolean hasChar = false;
        boolean hasInt = false;
        boolean hasUpper = false;
        boolean hasLower = false;

        // iterating through each symbol array and making sure the password to check has
        // at least one of the necessary symbols for each required symbol
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

        // returns true if all boolean conditions are met
        // otherwise returns false
        return hasChar & hasInt & hasUpper & hasLower;

    }

    // getter and setter for users
    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
