import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements ClientInterface {

    public static void main(String[] args) throws IOException, NullPointerException {

        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             InputStream is = socket.getInputStream();
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
             Scanner scan = new Scanner(System.in)) {

            boolean newOrReturning = showLogInMessage();
            boolean continueGoing = false;
            boolean newUser = false;
            String[] userInformation = new String[0];
            do {
                if (newOrReturning) {
                    //sends yes to the server if they are a returning user
                    pw.println("yes");
                    continueGoing = true;
                } else {
                    //send server a no if they are a new user
                    pw.write("no");
                    pw.println();
                    boolean invalidInformation = false;
                    do {
                        String newUserInfo = createNewUsername();
                        //sends a string of the new users username, email, number, and birthday
                        pw.write(newUserInfo);
                        pw.println();
                        String validNewUser = bfr.readLine();
                        //if the username, email, and number are not taken
                        if (validNewUser.equals("yes")) {
                            //creates a new password
                            String password = createNewPassword();
                            String[] information = newUserInfo.split(", ");
                            String userString = information[0] + ", " + information[1] + ", " + password + ", " +
                                    information[2] + ", " + information[3] + ", " + information[4];
                            pw.println(userString);
                            ;
                            userInformation = new String[]{information[0], information[1], password,
                                    information[2], information[3], information[4]};

                            continueGoing = true;
                            newUser = true;
                            invalidInformation = true;
                            //userInformation = new String[]{accountInfo[0], accountInfo[1], accountInfo[2], accountInfo[3],}
                        } else {
                            if (validNewUser.equals("username")) {
                                System.out.println("The username is already taken.");
                                validNewUser = bfr.readLine();
                            }
                            if (validNewUser.equals("email")) {
                                System.out.println("There is already an account with that email.");
                                validNewUser = bfr.readLine();
                            }
                            if (validNewUser.equals("phoneNumber")) {
                                System.out.println("There is already an account with that phoneNumber.");
                            }
                        }
                        //continues looping until the information is new and valid
                    } while (!invalidInformation);
                }
            } while (!continueGoing);
            boolean isUser;
            if (!newUser) {
                do {
                    String username = enterUsername();
                    pw.write(username);
                    pw.println();
                    //server returns whether or not they are a valid user
                    String validUser = bfr.readLine();
                    if (validUser.equals("no")) {
                        System.out.println("The Username, email, or phone-Number you entered does not have an account");
                        isUser = false;
                    } else {
                        isUser = true;
                    }
                } while (!isUser);
                String userInfoString = bfr.readLine();

                //receives the user information from the server if they are a valid user and splits it into each component
                String[] userInfo = userInfoString.split(", ");

                //this should check if the password is correct after
                int attempts = 0;
                boolean validPassword;
                do {
                    String password = enterPassword();

                    if (!password.equals(userInfo[2])) {
                        System.out.println("That is the wrong password. Please try again! You have " + (2 - attempts) + " attempts remaining.");
                        attempts += 1;
                        validPassword = false;
                    } else {
                        System.out.println("Login successful!");
                        attempts = 3;
                        validPassword = true;
                    }
                    if (attempts == 3 && !validPassword) {
                        System.out.println("You have used your maximum attempts. You will now be logged out to prevent suspicious activity.");
                    }
                } while (attempts < 3);

                if (validPassword) {
                    userInformation = userInfo;
                }
            }

            //create a user object when logged in

            boolean askQuestion;
            String response;
            boolean logout = false;
            do {
                do {
                    showProfilePage(userInformation);
                    System.out.println("Welcome to textogram. What would you like to do? (Type 'friends', 'search', or 'signout')");
                    response = scan.nextLine();
                    if (!response.equalsIgnoreCase("friends") && !response.equalsIgnoreCase("search") && !response.equalsIgnoreCase("signout")) {
                        System.out.println("Not a valid response");
                        askQuestion = true;
                    } else {
                        askQuestion = false;
                    }
                } while (askQuestion);
                pw.write(response);
                pw.println();
                pw.flush();
                boolean validResponse = false;
                do {
                    if (response.equalsIgnoreCase("friends")) {
                        friendsOption(pw, bfr, userInformation, scan);
                        validResponse = true;
                    } else if (response.equalsIgnoreCase("search")) {
                        try {
                            searchUsers(pw, bfr, is, userInformation[0]);
                            validResponse = true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (response.equalsIgnoreCase("signout")) {
                        logout = true;
                        validResponse = true;
                    } else {
                        validResponse = false;
                    }
                } while (!validResponse);
            } while (!logout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void friendsOption(PrintWriter pw, BufferedReader bfr, String[] userInfo, Scanner scan) throws IOException {
        System.out.println("Here are your friends:");
        pw.write("friends");
        pw.println();
        pw.flush();
        String friends = bfr.readLine();
        ArrayList<String> allFriendsUsers = new ArrayList<>();
        ArrayList<String> allFriends = new ArrayList<>();

        if (friends.equals(" ")) {
            System.out.println("No friends found!\n\n");
        } else {
            while (!friends.equals(" ")) {
                allFriendsUsers.add(friends.split(", ")[0]);
                allFriends.add(friends);
                System.out.println(friends);
                friends = bfr.readLine();
            }
            System.out.println(" ");
            boolean valid;
            do {
                System.out.println("What would you like to do now? (Type 'message', 'view', or 'profile')");
                String response = scan.nextLine();

                if (response.equalsIgnoreCase("message")) {
                    pw.write(response);
                    pw.println();
                    pw.flush();

                    boolean validFriend;
                    String friendToMessage;
                    do {
                        System.out.println("Which friend would you like to message?");
                        friendToMessage = scan.nextLine();

                        if (allFriendsUsers.contains(friendToMessage)) {
                            validFriend = true;
                        } else {
                            System.out.println("That person is not one of your friends!");
                            validFriend = false;
                        }
                    } while (!validFriend);

                    pw.write(friendToMessage);
                    pw.println();
                    pw.flush();

                    pw.write(userInfo[0]);
                    pw.println();
                    pw.flush();

                    System.out.println("Opening message file...");
                    String message = bfr.readLine();
                    if (message.equals(" ")) {
                        System.out.println("No previous message history!");
                    }
                    while (!message.equals(" ")) {
                        System.out.println(message);
                        message = bfr.readLine();
                    }
                    System.out.println(" ");
                    System.out.println("What would you like to send to " + friendToMessage + "?");
                    String messageToSend = scan.nextLine();

                    pw.write(messageToSend);
                    pw.println();
                    pw.flush();

                    String outcome = bfr.readLine();

                    if (outcome.equals("yes")) {
                        System.out.println("Message sent!");
                        System.out.println("Returning to Profile...\n");
                    }

                    valid = true;
                } else if (response.equalsIgnoreCase("view")) {
                    pw.write("view");
                    pw.println();
                    pw.flush();

                    boolean validFriend;
                    String friendToView;
                    do {
                        System.out.println("Which friend would you like to view the profile of?");
                        friendToView = scan.nextLine();

                        if (allFriendsUsers.contains(friendToView)) {
                            validFriend = true;
                        } else {
                            System.out.println("That person is not one of your friends!");
                            validFriend = false;
                        }
                    } while (!validFriend);

                    pw.write(friendToView);
                    pw.println();
                    pw.flush();

                    for (String friend : allFriends) {
                        if (friend.split(", ")[0].equals(friendToView)) {
                            showProfilePage(friend.split(", "));
                            break;
                        }
                    }

                    boolean validOption;
                    String unfriendOption;
                    do {
                        System.out.println("What would you like to do now? (Type 'unfriend' or 'profile')");
                        unfriendOption = scan.nextLine();

                        if (unfriendOption.equalsIgnoreCase("unfriend") || unfriendOption.equalsIgnoreCase("profile")) {
                            validOption = true;
                        } else {
                            System.out.println("Not a valid response");
                            validOption = false;
                        }
                    } while (!validOption);

                    pw.write(unfriendOption);
                    pw.println();
                    pw.flush();

                    if (unfriendOption.equalsIgnoreCase("unfriend")) {

                        System.out.println("Here is your new friend list:");
                        String friend = bfr.readLine();

                        if (friend.equals(" ")) {
                            System.out.println("No friends found!");
                        } else {
                            while (friends != null) {
                                System.out.println(friends);
                                friends = bfr.readLine();
                            }
                        }
                    }
                    valid = true;
                } else if (!response.equalsIgnoreCase("profile")) {
                    System.out.println("Not a valid response");
                    valid = false;
                } else {
                    pw.write("profile");
                    pw.println();
                    pw.flush();
                    valid = true;
                }
            } while (!valid);
        }
    }


    public static void searchUsers(PrintWriter pw, BufferedReader bfr, InputStream is, String userName) throws IOException, InterruptedException {
        boolean validResponse = false;
        Scanner scan = new Scanner(System.in);
        String response;
        do {
            System.out.println("Would you like to search for a user? yes or no");
            response = scan.nextLine();
            if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
                System.out.println("Not a valid response");
            } else {
                validResponse = true;
            }
        } while (!validResponse);
        if (response.equalsIgnoreCase("yes")) {
            pw.println(response);
            boolean search = true;
            do {
                System.out.println("Please enter the user you are searching for?");
                String userToSearch = scan.nextLine();

                //sends the server the name they are searching for
                pw.println(userToSearch);


                ArrayList<String> users = new ArrayList<>();
                String fUser = "";

                String firstUser = bfr.readLine();
                System.out.println(firstUser);
                users.add(firstUser);

                while (!fUser.equalsIgnoreCase("done")) {
                    fUser = bfr.readLine();
                    if (fUser.equalsIgnoreCase("done")) {
                        break;
                    }
                    users.add(fUser);
                }
                if (users.get(0).equalsIgnoreCase("no")) {
                    boolean again = false;
                    do {
                        System.out.println("There were no results.\n" +
                                "If you want to search again, type search. If you want to go back to your profile, type profile.");
                        String noResultsResponse = scan.nextLine();
                        if (noResultsResponse.equalsIgnoreCase("profile")) {
                            search = false;
                        } else if (!noResultsResponse.equalsIgnoreCase("search")) {
                            System.out.println("Not a valid response");
                            again = true;
                        } else if (noResultsResponse.equals(search)) {
                            again = false;
                        }
                    } while (again);
                } else {
                    System.out.println("Users Found:");
                    for (String username : users) {
                        System.out.println(username);
                    }
                    boolean validUsername = false;
                    do {
                        System.out.println("If you want to view one of these users profiles enter their username.\n" +
                                "If you want to search again, type search. If you want to go back to your profile, type profile.");
                        String nextResponse = scan.nextLine();
                        if (nextResponse.equalsIgnoreCase("profile")) {
                            search = false;
                            validUsername = true;
                            pw.println("end");
                        } else if (!nextResponse.equalsIgnoreCase("search")) {
                            //sends the username to open the profile
                            pw.println(nextResponse);
                            //should send back the users string
                            String profile = bfr.readLine();
                            //sends back no if it not a valid username
                            if (profile.equalsIgnoreCase("no")) {
                                System.out.println("That username is not valid");
                            } else {
                                validUsername = true;
                                String[] info = profile.split(", ");
                                //prints the profile of the user
                                showProfilePage(info);

                                boolean repeatFriend = false;
                                do {
                                    //asks the user if they want to add them as a friend
                                    System.out.println("Would you like to add the user as a friend or block them? Enter add " +
                                            "or block, or entire profile to return to the profile");
                                    String friend = scan.nextLine();


                                    //if block, add the user to the blocked list
                                    if (friend.equalsIgnoreCase("block")) {
                                        //blocks the user
                                        pw.println("block");
                                        pw.println(userName);
                                        System.out.println("User blocked Successfully. Returning to profile.");
                                        search = false;
                                        //if add, adds the user to friends list
                                    } else if (friend.equalsIgnoreCase("add")) {
                                        //adds the user as a friend
                                        pw.println("add");
                                        pw.println(userName);
                                        System.out.println("Friend added Succesfully. Returning to profile.");
                                        search = false;
                                        repeatFriend = false;
                                        //if profile, returns to user profile
                                    } else if (friend.equalsIgnoreCase("profile")) {
                                        search = false;
                                        pw.println("done");
                                        repeatFriend = false;
                                    } else {
                                        System.out.println("Not a valid response");
                                        repeatFriend = true;
                                    }
                                } while (repeatFriend);
                            }
                        } else {
                            pw.println("search");
                            validUsername = true;
                            search = true;
                        }
                    } while (!validUsername);
                }
            } while (search);
        } else {
            pw.println("no");
        }
    }

    public static boolean showLogInMessage() {
        boolean validResponse = false;
        do {
            System.out.println("Welcome to TextOGram");
            System.out.println("If you already have an account enter yes. If you want to create an account enter no.");
            Scanner scan = new Scanner(System.in);
            String response = scan.nextLine();

            //returns old if it is an old user
            if (response.equals("yes")) {
                validResponse = true;
                return true;
                //returns new if it is a new user
            } else if (response.equals("no")) {
                validResponse = true;
                return false;
            }
        } while (!validResponse);
        return false;
    }


    public static String createNewUsername() {
        System.out.println("Please enter the username for your new account");
        Scanner scan = new Scanner(System.in);
        String username = scan.nextLine();
        System.out.println("Please enter the email for your new account");
        String email = scan.nextLine();
        System.out.println("Please enter the phone number for your new account");
        String phone = scan.nextLine();
        System.out.println("Please enter your birthday in the format MM-DD-YYYY");
        String birthday = scan.nextLine();
        System.out.println("Please enter your first and last name");
        String name = scan.nextLine();
        return String.format("%s, %s, %s, %s, %s", username, name, email, phone, birthday);
    }

    public static String createNewPassword() {
        boolean same = false;
        String passwordOne;
        String passwordTwo;
        do {
            do {
                System.out.println("Please enter your password for your new account");
                System.out.println("The password must contain at least one uppercase letter, lowercase");
                System.out.println("letter, number, and special character");
                Scanner scan = new Scanner(System.in);
                passwordOne = scan.nextLine();
                System.out.println("Please re-enter your new password");
                passwordTwo = scan.nextLine();
                if (passwordOne.equals(passwordTwo)) {
                    same = true;
                }
            } while (!same);
            if (!checkPassword(passwordOne)) {
                System.out.println("The password does not include all of the required characters");
            }
        } while (!checkPassword(passwordOne));
        return passwordOne;
    }

    // this method checks if the password inputted meets all the requirements for a secure password
    public static boolean checkPassword(String password) {

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

    public static String enterUsername() throws IOException {
        System.out.println("Please enter your username, email, or phoneNumber");
        Scanner scan = new Scanner(System.in);
        String response = scan.nextLine();
        return response;
    }

    public static String enterPassword() throws IOException {
        System.out.println("Please enter your password");
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    public static void showMessage() {
        System.out.println("Enter Message");

    }

    public static void sendMessage(PrintWriter pw, BufferedReader bfr) throws IOException {
        System.out.println("Please enter your friend that you would like to send a message to.");
        Scanner scan = new Scanner(System.in);
        String username = scan.nextLine();
        pw.write(username);
        pw.println();
        pw.flush();
        String friend = bfr.readLine();
        if (friend.equals("yes")) {
            System.out.println("Please enter the message you would like to send");
            String message = scan.nextLine();
            pw.write(message);
            pw.println();
            pw.flush();
        } else {
            System.out.println("Your are not friends with that user");
        }
    }

    public static void showProfilePage(String[] profilePageThings) {
        //splits the user information;
        String username = profilePageThings[0];
        String name = profilePageThings[1];
        String email = profilePageThings[3];
        String birthday = profilePageThings[5];
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("Username: ").append(username).append("\n");
        userInfo.append("Name: ").append(name).append("\n");
        userInfo.append("Email: ").append(email).append("\n");
        ;
        userInfo.append("Birthday: ").append(birthday).append("\n");
        System.out.println(userInfo);
    }
}
