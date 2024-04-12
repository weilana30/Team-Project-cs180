import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Client {

    public static void main(String[] args) throws IOException, NullPointerException {
        String hostName;
        int portNum;
        Socket socket = new Socket("textogram", 1234);
        Boolean newOrReturning = showLogInMessage();
        //output stream to send messages to server
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        boolean continueGoing = false;
        boolean newUser = false;
        String [] userInformation = new String[0];
        do {
            if (newOrReturning) {
                //sends yes to the server if they are a returning user
                pw.write("yes");
                pw.println();
                pw.flush();
                continueGoing = true;
            } else {
                //send server a no if they are a new user
                pw.write("no");
                pw.println();
                pw.flush();
                boolean invalidInformation = true;
                do {
                    String newUserInfo = createNewUsername();
                    //sends a string of the new users username, email, number, and birthday
                    pw.write(newUserInfo);
                    BufferedReader bfrNewUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String validNewUser = bfrNewUser.readLine();

                    //if the username, email, and number are not taken
                    if (validNewUser.equals("yes")) {
                        //creates a new password
                        String password = createNewPassword();
                        pw.write(password);
                        pw.println();
                        pw.flush();
                        continueGoing = true;
                        newUser = true;
                        String[] accountInfo = newUserInfo.split(", ");
                        userInformation = new String[]{accountInfo[0], accountInfo[1], accountInfo[2], accountInfo[3],}
                    } else {
                        invalidInformation = false;
                        if (validNewUser.equals("username")) {
                            System.out.println("The username is already taken.");
                            validNewUser = bfrNewUser.readLine();
                        }
                        if (validNewUser.equals("email")) {
                            System.out.println("There is already an account with that email.");
                            validNewUser = bfrNewUser.readLine();
                        }
                        if (validNewUser.equals("phoneNumber")) {
                            System.out.println("There is already an account with that phoneNumber.");
                        }
                    }
                    //continues looping until the information is new and valid
                } while(!invalidInformation);
            }
        } while(!continueGoing);
        boolean isUser = true;
        if (!newUser) {
            do {
                String username = enterUsername();
                pw.write(username);
                pw.println();
                pw.flush();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //server returns whether or not they are a valid user
                String validUser = bfr.readLine();
                bfr.close();
                if (validUser.equals("no")) {
                    System.out.println("The Username, email, or phone-Number you entered does not have an account");
                    isUser = false;
                }
            } while (!isUser);
            BufferedReader readUserInfo = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String userInfoString = readUserInfo.readLine();
            readUserInfo.close();

            //recieves the user information from the server if they are a valid user and splits it into each component
            String[] userInfo = userInfoString.split(" ");

            //this should check if the password is correct after
            int attempts = 0;
            boolean validPassword;
            do {
                String password = enterPassword();

                if (!password.equals(userInfo[2])) {
                    System.out.println("That is the wrong password. Please try again! You have " + (3 - attempts) + " attempts remaining.");
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
        showProfilePage(userInformation);


    }

    public static boolean showLogInMessage() {
        boolean validResponse = false;
        do {
        System.out.println("Welcome to TextOGram");
        System.out.println("If you already have an account enter yes. If you want to create an account enter no.");
        Scanner scan = new Scanner(System.in);
        String response = scan.nextLine();

        //returns old if it is an old user
        if (scan.equals("yes")) {
            validResponse = true;
            return true;
            //returns new if it is a new user
        } else if(scan.equals("no")) {
            validResponse = true;
            return false;
        }
        } while(!validResponse);
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
        return String.format("%s, %s, %s, %s", username, email, phone, birthday);
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
        } while(!checkPassword(passwordOne));
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

    public static void showProfilePage(String [] profilePageThings) {
        //splits the user information;
        String username = profilePageThings[0];
        String name = profilePageThings[1];
        String email = profilePageThings[3];
        String birthday = profilePageThings[5];
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("Username: ").append(username).append("\n");
        userInfo.append("Name: ").append(name).append("\n");
        userInfo.append("Email: ").append(email).append("\n");;
        userInfo.append("Birthday: ").append(birthday).append("\n");
        System.out.println(userInfo);
    }

}

