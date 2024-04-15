import java.util.*;

/**
 * directMessageInterface
 * <p>
 * An interface for the directMessage class
 *
 * @author Andrew Weiland, lab section 15
 * @version March 31, 2024
 */
public interface DirectMessageInterface {
    User[] getUsers();

    ArrayList<IndividualText> getMessages();

    boolean addMessage(User user, String message);

    boolean updateFile();

    boolean deleteMessage(IndividualText individualText, User user);

    String toString();
}
