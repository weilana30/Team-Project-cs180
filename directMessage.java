import java.util.*;

public class directMessage implements directMessageInterface{
    private String name;
    private User [] users;
    private int textNumber;
    private ArrayList <IndividualText> messages;

    public directMessage(User user1, User user2) {
        this.name = user1.getUsername() + user2.getUsername();
        this.users = new User[]{user1, user2};
        this.textNumber = 0;
        this.messages = new ArrayList<>();
    }

    public User[] getUsers() {
        return users;
    }

    public ArrayList<IndividualText> getMessages() {
        return messages;
    }


    public boolean addMessage(User user, String message) {
        boolean found = false;
        for (User currentUser : this.users) {
            if (user.getUsername().equals(currentUser.getUsername())) {
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        textNumber++;
        IndividualText newText = new IndividualText(user, message, textNumber);
        messages.add(newText);
        return true;
    }
    public boolean deleteMessage(IndividualText text, User user) {
        //Checks if the message is actually in the messages list
        if (!messages.contains(text)) {
            return false;
        }
        //Does not allow one user to delete another users text
        if (!user.getUsername().equals(text.getUser().getUsername())) {
            return false;
        }
        return messages.remove(text);
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (IndividualText text: messages) {
            stringBuilder.append(text.toString() + "\n");
        }
        return stringBuilder.toString();
    }
}


