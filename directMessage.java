import java.io.*;

public class directMessage {
    private User [] users;
    private String messageFile;

    //constructs a new Message Object
    public directMessage(User user1, User user2) {
        this.users = new User[]{user1, user2};
        this.messageFile = user1.getUsername() +"/"+ user2.getUsername() + "Message.txt";

        //This is supposed to check if the file exists already, so it can't duplicate the chat
        //It does not work though
        File file = new File(this.messageFile);
        File file2 = new File(user2.getUsername() +"/"+ user1.getUsername() + "Message.txt");
        if (file.exists()) {
            throw new IllegalArgumentException("These two users already have a chat." + messageFile);
        }
        if (file2.exists()) {
            throw new IllegalArgumentException("These two users already have a chat." + messageFile);
        }
    }
    //This adds a message to the end of the file message in the format user-who-sent-message:::message
    public boolean addMessage(String message, User user) {
        try {
            File
            FileOutputStream fos = new FileOutputStream(messageFile);
            PrintWriter pw = new BufferedWriter(fos);
            pw.write(user.getUsername() + ":::" + message);
        } catch(IOException io) {
            io.printStackTrace();
            System.out.println("There was an error adding a message");
            return false;
        }
        return true;
    }
    public boolean deleteMessage(String message) {

    }
}
