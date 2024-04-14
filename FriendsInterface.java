import java.util.ArrayList;

public interface FriendsInterface {

    boolean addFriend(String friendUsername, String userUsername);

    boolean removeFriend(String friendUsername);

    boolean blockUser(String userToBlockUsername, String currentUserUsername);

    boolean unblockUser(String userToUnblockUsername, String currentUserUsername);

    ArrayList<User> getFriends(String username);

    ArrayList<User> getBlocked(String username);

}
