import java.util.ArrayList;

public class User {
    private String name;
    private String username;
    private String password;
    public ArrayList <User> friends;
    private ArrayList <User> blocked;
    private directMessage [] messages;

    public static void main(String[] args) {

    }
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean addFriend(User friend) {
        try {
            for (User user : blocked) {
                if (user.getUsername().equals(friend.getUsername())) {
                    return false;
                }
            }
        } catch(NullPointerException e) {
            try {
                for (User user : friends) {
                    if (friend.getUsername().equals(user.getUsername())) {
                        return false;
                    }
                }
                friends.add(friend);
            } catch (NullPointerException er) {
                friends = new ArrayList<>();
                friends.add(friend);
                return true;
            }
        }
        return true;
    }
    public boolean removeFriend(User friend) {
        try {
            for (int i = 0; i < friends.size(); i++) {
                if (friend.getUsername().equals(friends.get(i).getUsername())) {
                    friends.remove(i);
                    return true;
                }
            }
        } catch(NullPointerException e) {
            return false;
        }
        return false;
    }
    public boolean blockUser(User userToBlock) {
        try{
            for (int i = 0; i < blocked.size(); i++) {
                if (userToBlock.getUsername().equals(blocked.get(i).getUsername())) {
                    return false;
                }
            }
            blocked.add(userToBlock);
        } catch(NullPointerException e) {
            blocked = new ArrayList<>();
            blocked.add(userToBlock);
            return true;
        }
        return true;
    }

}
