public interface FriendsInterface {
    boolean addFriend(String friendUsername, String userUsername);
    boolean removeFriend(String friendUsername);
    boolean blockUser(String userToBlockUsername, String currentUserUsername);
}
