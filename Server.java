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
                returningUser(username, pw);
            } else {
                //sign up
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returningUser(String username, PrintWriter pw) {
        User user = profile.getUserByUsername(username);
        
        if (user != null) {
            String userInfo = String.format("Username: %s, Name: %s, Password: %s, Email: %s, Phone: %s, Birthday: %s",
                    user.getUsername(), user.getName(), user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getBirthday());
            pw.write(userInfo);
            pw.println();
            pw.flush();
        } else {
            pw.write("no");
            pw.println();
            pw.flush();
        }
    }
}
