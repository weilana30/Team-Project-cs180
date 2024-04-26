import java.io.*;
import java.util.ArrayList;

/**
 * directMessage
 *
 * <p>
 * Creates an object that resembled the chat between two users
 *
 * @author Andrew Weiland, lab section 15
 * @version March 31, 2024
 */
public class DirectMessage implements DirectMessageInterface{
    private String fileName;
    private User [] users;
    private int textNumber;
    private ArrayList<IndividualText> messages;

    public DirectMessage(User user1, User user2) {
        if (user1.getUsername().compareTo(user2.getUsername()) < 1) {
            this.fileName = user1.getUsername() + user2.getUsername() + ".txt";
        } else {
            this.fileName = user2.getUsername() + user1.getUsername() + ".txt";
        }
        this.users = new User[]{user1, user2};
        this.textNumber = 0;
        this.messages = new ArrayList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public User[] getUsers() {
        return users;
    }

    public ArrayList<IndividualText> getMessages() {
        return messages;
    }

    //adds a message to the arraylist when there is a new message
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
    //updates the file with the current contents of messages
    public synchronized boolean updateFile() {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file, false);
            PrintWriter pw = new PrintWriter(fos);
            pw.println(this);
            pw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    //deletes a message if the user who sent it is the one trying to delete it
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
