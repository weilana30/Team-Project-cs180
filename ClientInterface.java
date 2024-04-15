import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public interface ClientInterface {

    boolean showLogInMessage();

    String createNewUsername();

    String createNewPassword();

    boolean checkPassword(String password);

    String enterUsername() throws IOException;

    String enterPassword() throws IOException;

    void showProfilePage(String[] profilePageThings);

    void friendsOption(PrintWriter pw, BufferedReader bfr, String[] userInfo, Scanner scan) throws IOException;

    void searchUsers(PrintWriter pw, BufferedReader bfr, InputStream is, String userName) throws IOException, InterruptedException;

    void sendMessage(PrintWriter pw, BufferedReader bfr) throws IOException;

    void showMessage();
}
