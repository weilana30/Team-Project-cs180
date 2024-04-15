import java.util.ArrayList;
/**
 * FrindsInterface
 * <p>
 *
 * Interface for friends class
 *
 * @author Andrew Weiland, lab section 15
 * @version April 15, 2024
 */
public interface FriendsInterface {

    boolean addFriend(String friendUsername, String currentUserUsername);

    boolean removeFriend(String friendUsername, String currentUserUsername);

    void updateFriendsFile(String username);

    boolean blockUser(String userToBlockUsername, String currentUserUsername);

    boolean unblockUser(String userToUnblockUsername, String currentUserUsername);

    ArrayList<User> getFriends(String username);

    ArrayList<User> getBlocked(String username);

}
