import java.io.BufferedReader;
import java.io.FileReader;
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
        updateFriendsFile(user.getUsername(), friend.getUsername()); // updates the [username]Friends.txt file
        return true;
    }

    public boolean removeFriend(String friendUsername, String currentUserUsername) {
        User friend = profiles.getUserByUsername(friendUsername);
        User currentUser = profiles.getUserByUsername(currentUserUsername);

        // if either friend or current user doesn't exist, return false
        if (friend == null || currentUser == null) {
            return false;
        }

        // if friend username is able to be removed from friends list, return true
        // otherwise, it means that friend is not on the friends list and can't be removed
        if (friends.remove(friend)) {
            updateFriendsFile(currentUser.getUsername(), friend.getUsername()); // updates the [username]Friends.txt file for the current user
            return true;
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
        removeFriend(userToBlock.getUsername(), currentUser.getUsername());
        blocked.add(userToBlock);
        updateBlockedFile(currentUser.getUsername()); // updates the [username]Blocked.txt file
        return true;
    }

    public ArrayList<User> getFriends(String username) {
        if (friends.isEmpty()) {
            readFriendsFromFile(username); //populates friends ArrayList from file if it's empty
        }
        return friends;
    }

    private void readFriendsFromFile(String username) {
        // reads friends data from file and populates the friends ArrayList
        try {
            FileReader reader = new FileReader(username + "Friends.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                User friend = profiles.getUserByUsername(line.trim());
                if (friend != null) {
                    friends.add(friend);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean unblockUser(String userToUnblockUsername, String currentUserUsername) {
        User userToUnblock = profiles.getUserByUsername(userToUnblockUsername);
        User currentUser = profiles.getUserByUsername(currentUserUsername);

        // if both users don't exist, returns false
        if (userToUnblock == null || currentUser == null) {
            return false;
        }

        // if user you want to unblock is not blocked, returns false
        if (!blocked.contains(userToUnblock)) {
            return false;
        }

        // removes userToUnblock from the blocklist
        blocked.remove(userToUnblock);
        updateBlockedFile(currentUser.getUsername()); // updates the [username]Blocked.txt file
        return true;
    }


    private void updateFriendsFile(String username, String friendToRemove) {
        try {
            FileReader reader = new FileReader(username + "Friends.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            ArrayList<String> lines = new ArrayList<>();
            String line;
            // Read all lines from the file
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().equals(friendToRemove)) {
                    lines.add(line); // Exclude the line corresponding to the friend being removed
                }
            }
            bufferedReader.close();

            FileWriter writer = new FileWriter(username + "Friends.txt");
            for (String friendLine : lines) {
                writer.write(friendLine + "\n");
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

    public ArrayList<User> getBlocked(String username) {
        if (blocked.isEmpty()) {
            readBlockedFromFile(username); // populates blocked ArrayList from file if it's empty
        }
        return blocked;
    }

    private void readBlockedFromFile(String username) {
        // reads blocked users data from file specific to the given username and populate the blocked ArrayList
        try {
            FileReader reader = new FileReader(username + "Blocked.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                User blockedUser = profiles.getUserByUsername(line.trim());
                if (blockedUser != null) {
                    blocked.add(blockedUser);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
