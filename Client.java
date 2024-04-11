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
            }
        } while(!continueGoing);
        boolean isUser = true;
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
        } while(!isUser);
        BufferedReader readUserInfo = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String userInfoString = readUserInfo.readLine();
        readUserInfo.close();

        //recieves the user information from the server if they are a valid user and splits it into each component
        String [] userInfo = userInfoString.split(" ");

        //this should check if the password is correct after
        boolean validPassword;
        do {
            String password = enterPassword();
            
            if (!password.equals(userInfo[4])) {
                System.out.println("That is the wrong password. Please try again!");
                validPassword = false;
            }
            else {
                System.out.println("Login successful!");
                validPassword = true;
            }
        } while (!validPassword);

        //
        showProfilePage();


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

    public static void showProfilePage(String profileInformation) {
        //splits the user information
        String [] profilePageThings = profileInformation.split("-");
        for (String thing : profilePageThings) {
            System.out.println(thing);
        }
    }
}


