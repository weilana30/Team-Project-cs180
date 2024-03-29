import java.util.ArrayList;

public class Friends {
    private ArrayList<Profile> friends; // arraylist for friends
    private ArrayList<Profile> blocked; // arraylist for blocked profiles 

    //not sure where to implement this, but i want to get the list of all users from Profile.java
    //or whatever the class name is for whichever one is storing the database

    public Friends() {
        this.friends = new ArrayList<>();
        this.blocked = new ArrayList<>();
    }

    public ArrayList<Profile> getAllProfiles() {
        //get the method from the other class for managing profiles
        //iterate through the file to make it into an arraylist ig idk
        //return [method from other class].getAllProfiles();
    }

    public boolean addFriend(Profile friend, User user) { //again Profile is just temporary
        //read the database of users
        //if friend username matches a profile then add to arraylist, return true
        //check if the profile has been blocked by the user
        //if it has, then return false
        //check if friend has already been added, if they have then don't add twice!!
        //if theres time, add a message like "[friend] has already been added"
        //if user does not exist, print out a message and return false
        //user can't add themselves as a friend either so just return false if that happens (this is probably when you use the User class)
        //return false as default, and return true only if friend is able to be added
        try {
            if (friends == null) {
                friends = new ArrayList<>();
            }

            ArrayList<Profile> allProfiles = getAllProfiles();
            if (allProfiles != null) {
                //profiles can't be null
                if (getAllProfiles().contains(friend)) {
                    //above: checks if friend is in the database of users
                    if (!blocked.contains(friend)) {
                        //if friend is NOT blocked
                        if (!friends.contains(friend)) {
                            //if friend is not already added as a friend
                            if (!friend.getName().equals(user.getName())) {
                                //user cannot add themselves
                                friends.add(friend);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }
    
    public boolean removeFriend(User friend) {
        //check through the arraylist of friends to see if "friend" matches
        //if it does, remove "friend" from the arraylist
        //this applies more to blockUser but if user is a friend and then is blocked, remove them as a friend (call upon this method)
        //if friends list is empty, then catch a Null Pointer Exception and return false
        //return false as default, and return true only if friend is able to be removed
        //maybe if there's time do a "are you sure you want to remove [friend] as a friend?"
        try {
            //checks if friends is empty. :(
            if (friends == null || friends.isEmpty()) {
                return false;
            }
            //removes friend from friends list
            if (friends.contains(friend)) {
                friends.remove(friend);
                return true;
            } else {
                System.out.println(friend.getName() + "is not on your friends list.");
                //modify accordingly depending on where getName for other users is
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    public boolean blockUser(User userToBlock, User currentUser) {
        //if userToBlock is currently in the friend's list, call removeFriend method and remove them from friend's list before blocking
        //read the database of users
        //if userToBlock username matches a profile then add to arraylist of blocked users
        //if userToBlock is already blocked then return false
        //user can't block themsleves so just return false if that happens
        //catch a null pointer exception and create a new arraylist for blocked (blocked = new ArrayList<>();)
        //then add userToBlock, and check the requirements above (they need to exist in the database, not the user, if they're currently in friendlist)
            //"if userToBlock is already blocked then return false" doesnt apply here since there wasn't a blocklist at all
        //maybe if theres time, do a  "are you sure you want to block [userToBlock]?" check
        try {
            //cant block urself
            if (userToBlock.equals(currentUser)) {
                return false;
            }

            //already blocked, dont add twice
            if (blocked.contains(userToBlock)) {
                return false;
            }

            //is usertoblock a friend?
            if (friends.contains(userToBlock)) {
                removeFriend(userToBlock);
            }

            blocked.add(userToBlock);
            return true;

        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

}
