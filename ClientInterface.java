import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * ClientInterface
 * <p>
 * Interface for client
 *
 * @author Andrew Weiland, lab section 15
 * @version April 15, 2024
 */
public interface ClientInterface {

    static boolean showLogInMessage() {
        return false;
    }

    ;

    static String createNewUsername() {
        return null;
    }

    ;

    static String createNewPassword() {
        return null;
    }

    ;

    static boolean checkPassword(String password) {
        return false;
    }


    static String enterUsername() throws IOException {
        return null;
    }

    ;

    static String enterPassword() throws IOException {
        return null;
    }

    ;

    static void showProfilePage(String[] profilePageThings) {

    }

    ;

    static void friendsOption(PrintWriter pw, BufferedReader bfr, String[] userInfo, Scanner scan)
            throws IOException {

    }

    ;

    static void searchUsers(PrintWriter pw, BufferedReader bfr, InputStream is, String userName)
            throws IOException, InterruptedException {

    }

    ;

    static void sendMessage(PrintWriter pw, BufferedReader bfr) throws IOException {

    }

    ;

    static void showMessage() {

    }

    ;
}
