import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface ServerInterface {

    void run();

    void returningUser(String username, BufferedReader br, PrintWriter pw) throws IOException;

    void handleFriends(User user, BufferedReader bfr, PrintWriter pw) throws IOException;

    void handleProfileSearch(BufferedReader br, PrintWriter pw) throws IOException;
}
