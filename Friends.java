import java.util.ArrayList;

public class Friends implements FriendsInterface {
    private ArrayList<User> friends;
    private ArrayList<User> blocked;
    private Profile profiles;

    public Friends(Profile profiles) {
        this.profiles = profiles;
        this.friends = new ArrayList<>();
        this.blocked = new ArrayList<>();
    }

    public boolean addFriend(String friendUsername, String userUsername) {
            User friend = profiles.getUserByUsername(friendUsername);
            User user = profiles.getUserByUsername(userUsername);
            
            if (friend == null || user == null) {
                return false;
            }
            
            if (friend.equals(user) || blocked.contains(friend)) {
                return false;
            }
            
            if (friends.contains(friend)) {
                return false;
            }
            
            friends.add(friend);
            return true;
    }
    
    public boolean removeFriend(String friendUsername) {
        User friend = profiles.getUserByUsername(friendUsername);
        
        if (friend == null) {
            return false;
        }
        
        if (friends.remove(friend)) {
            return true;
        } else {
            System.out.println(friendUsername + " is not on your friends list.");
        }
            
        return false;
    }

    public boolean blockUser(String userToBlockUsername, String currentUserUsername) {
        User userToBlock = profiles.getUserByUsername(userToBlockUsername);
        User currentUser = profiles.getUserByUsername(currentUserUsername);

            if (userToBlock == null || currentUser == null) {
                return false;
            }
    
            if (userToBlock.equals(currentUser) || blocked.contains(userToBlock)) {
                return false;
            }

            removeFriend(userToBlock.getUsername());
            blocked.add(userToBlock);
            return true;
    }

}
