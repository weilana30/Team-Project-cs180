import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
public class Client {

    public static void main(String[] args) throws IOException, NullPointerException {
        String hostName;
        int portNum;
        Boolean newOrReturning = showLogInMessage();
        if (newOrReturning) {
            //send server a yes
        } else {
            //send server a no
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
    public static boolean enterUsername() throws IOException {
        System.out.println("Please enter your username, email, or phoneNumber");
        Scanner scan = new Scanner(System.in);
        File file = new File("Users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);
        String line = bfr.readLine();
        boolean found = false;
        while(line != null) {
            String[] userInformation = line.split(" ");
            if (userInformation[0].equals(line) || userInformation[2].equals(line) || userInformation[3].equals(line)) {
                found = true;
                break;
            }
            line = bfr.readLine();
        }
        bfr.close();
        fr.close();
        return found;
    }
}

