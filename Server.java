import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private Socket clientSocket;
    private static Profile profile = new Profile();
    private ProfileViewer profileViewer;

    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.profileViewer = new ProfileViewer(profile);
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
                            String userProfile = profileViewer.displayUserInformationByUsername(username);
                            pw.println(userProfile);
                            passwordCorrect = true;
                        } else {
                            pw.println("incorrect_password");
                        }
                    }
                    pw.println("Welcome, " + username + "! What would you like to do? (Type 'friends', 'search', or 'signout')");
                    String userChoice = br.readLine();
                    if ("friends".equalsIgnoreCase(userChoice)) {
                        handleFriends(user, br, pw);
                    } else if ("search".equalsIgnoreCase(userChoice)) {
                        handleProfileSearch(br, pw);
                    } else if ("signout".equalsIgnoreCase(userChoice)) {
                        pw.println("Signing out...");
                        return;
                    } else {
                        pw.println("Invalid choice. Please type 'friends', 'search', or 'signout'.");
                    }
                }
            } else {
                String newAccount = br.readLine();

                String [] parts = newAccount.split(", ");
                String username = parts[0].trim();
                String email = parts[1].trim();
                String phoneNumber = parts[2].trim();

                boolean accountCorrect = false;

                while (!accountCorrect) {
                    if (profile.getUserByUsername(username) != null) {
                        pw.write("username");
                    } else if (profile.getEmailByUsername(email) != null) {
                        pw.println("email");
                    } else if (profile.getPhoneNumberByUsername(phoneNumber) != null) {
                        pw.println("phoneNumber");
                    } else {
                        accountCorrect = true;
                        pw.write("yes");
                        pw.println();
                    }
                }
                String userInfo = br.readLine();
                File users = new File("Users.txt");
                PrintWriter userPW = new PrintWriter(new FileOutputStream(users, true));
                userPW.println(userInfo);
                userPW.close();
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

    private void handleFriends(User user, BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("Here are your friends:");
        try (BufferedReader reader = new BufferedReader(new FileReader("usernamesFriends.txt"))) {
            String friendUsername;
            while ((friendUsername = reader.readLine()) != null) {
                pw.println(friendUsername);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleProfileSearch(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("Do you want to find a user? (yes/no)");
        String choice = br.readLine().trim().toLowerCase();
        if (choice.equals("yes")) {
            pw.println("Choose search criteria (username/phone number/email): ");
            String searchCriteria = br.readLine().trim().toLowerCase();
            pw.println("Enter the value to search for: ");
            String searchValue = br.readLine().trim();
            switch (searchCriteria) {
                case "username":
                    pw.println(profileViewer.displayUserInformationByUsername(searchValue));
                    break;
                case "phone number":
                    pw.println(profileViewer.displayUserInformationByPhoneNumber(searchValue));
                    break;
                case "email":
                    pw.println(profileViewer.displayUserInformationByEmail(searchValue));
                    break;
                default:
                    pw.println("Invalid search criteria!");
            }
        } else if (!choice.equals("no")) {
            pw.println("Invalid choice!");
        }
    }
}
