import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private Socket clientSocket;
    private static Profile profile = new Profile();

    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    public static void main(String[] args) throws IOException {
        int port = 1234;
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Server(clientSocket)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    

    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
             PrintWriter pw = new PrintWriter(this.clientSocket.getOutputStream(), true)) {

            String newUser = br.readLine();

           if ("yes".equalsIgnoreCase(newUser)) {
            String username = br.readLine();
            User user = profile.getUserByUsername(username);
            if (user != null) {
                boolean passwordCorrect = false;
                while (!passwordCorrect) {
                    pw.println("Please enter your password:");
                    String password = br.readLine();
                    if (user.getPassword().equals(password)) {
                        ProfileViewer profileViewer = new ProfileViewer(profile);
                        String userProfile = profileViewer.displayUserInformation(username);
                        pw.println(userProfile);
                        passwordCorrect = true;
                    } else {
                        pw.println("incorrect_password");
                    }
                }
            } 
        } else {
                String newAccount = br.readLine();
                
                String [] parts = newAccount.split(", ");
                String username = parts[0].trim();
                String email = parts[1].trim();
                String phoneNumber = parts[2].trim();

                if (profile.getUserByUsername(username) != null) {
                    pw.write(username);
                } else if (profile.getEmailByUsername(email) != null) {
                    pw.println(email);
                } else if (profile.getPhoneNumberByUsername(phoneNumber) != null) {
                    pw.println(phoneNumber);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returningUser(String username, BufferedReader br, PrintWriter pw) throws IOException {
        User user = profile.getUserByUsername(username);
        while (user == null) {
            pw.println("no");
            username = br.readLine();
            user = profile.getUserByUsername(username);
        }
        // Once a valid username is found, send user information
        String userInfo = String.format("Username: %s, Name: %s, Password: %s, Email: %s, Phone: %s, Birthday: %s",
                user.getUsername(), user.getName(), user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getBirthday());
        pw.println(userInfo);
    }
}
