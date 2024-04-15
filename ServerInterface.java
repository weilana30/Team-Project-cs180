import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * ServerInterface
 * <p>
 * Interface for Server
 *
 * @author Andrew Weiland, lab section 15
 * @version April 15, 2024
 */
public interface ServerInterface {

    void run();

    void returningUser(String username, BufferedReader br, PrintWriter pw) throws IOException;

    void handleFriends(User user, BufferedReader bfr, PrintWriter pw) throws IOException;

    void handleProfileSearch(BufferedReader br, PrintWriter pw) throws IOException;
}
