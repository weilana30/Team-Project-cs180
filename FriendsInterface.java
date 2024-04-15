import java.util.ArrayList;

public interface FriendsInterface {

    boolean addFriend(String friendUsername, String currentUserUsername);

    boolean removeFriend(String friendUsername, String currentUserUsername);

    void updateFriendsFile(String username);

    boolean blockUser(String userToBlockUsername, String currentUserUsername);

    boolean unblockUser(String userToUnblockUsername, String currentUserUsername);

    ArrayList<User> getFriends(String username);

    ArrayList<User> getBlocked(String username);

}
