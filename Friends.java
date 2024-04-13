import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Friends implements FriendsInterface {
    // fields
    private ArrayList<User> friends;
    private ArrayList<User> blocked;
    private Profile profiles;

    // constructor
    public Friends(Profile profiles) {
        this.profiles = profiles;
        this.friends = new ArrayList<>();
        this.blocked = new ArrayList<>();
    }

    // methods
    public boolean addFriend(String friendUsername, String userUsername) {
        User friend = profiles.getUserByUsername(friendUsername);
        User user = profiles.getUserByUsername(userUsername);

        // if friend or user doesn't exist, returns false
        if (friend == null || user == null) {
            return false;
        }

        // if friend username is the same as user's username OR
        // if blocklist contains friend's username, returns false
        if (friend.equals(user) || blocked.contains(friend)) {
            return false;
        }

        // if friends list already contains the friend's username (they've already been added)
        // returns false
        if (friends.contains(friend)) {
            return false;
        }

        friends.add(friend);
        updateFriendsFile(user.getUsername()); // updates the [username]Friends.txt file
        return true;
    }

    public boolean removeFriend(String friendUsername) {
        User friend = profiles.getUserByUsername(friendUsername);

        // if friend username doesn't exist
        if (friend == null) {
            return false;
        }

        // if friend username is able to be removed from friend, return true
        // else it means that friend is not on friends list and can't be removed
        if (friends.remove(friend)) {
            updateFriendsFile(friend.getUsername()); // updates the [username]Friends.txt file
            return true;
        } else {
            System.out.println(friendUsername + " is not on your friends list.");
        }

        return false;
    }

    public boolean blockUser(String userToBlockUsername, String currentUserUsername) {
        User userToBlock = profiles.getUserByUsername(userToBlockUsername);
        User currentUser = profiles.getUserByUsername(currentUserUsername);

        // if userToBlock or currentUser doesn't exist, returns false
        if (userToBlock == null || currentUser == null) {
            return false;
        }

        // if user you want to block is already blocked OR
        // is the same user as the current user, returns false
        if (userToBlock.equals(currentUser) || blocked.contains(userToBlock)) {
            return false;
        }

        // automatically removes userToBlock from friend list if they're in it
        // adds to block list
        removeFriend(userToBlock.getUsername());
        blocked.add(userToBlock);
        updateBlockedFile(currentUser.getUsername()); // updates the [username]Blocked.txt file
        return true;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    private void updateFriendsFile(String username) {
        try {
            FileWriter writer = new FileWriter(username + "Friends.txt");
            for (User friend : friends) {
                writer.write(friend.getUsername() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateBlockedFile(String username) {
        try {
            FileWriter writer = new FileWriter(username + "Blocked.txt");
            for (User blockedUser : blocked) {
                writer.write(blockedUser.getUsername() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
