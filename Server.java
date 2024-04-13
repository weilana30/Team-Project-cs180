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
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);
            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new Server(clientSocket)).start();
                } catch (IOException e) {
                    if (!isRunning) {
                        System.out.println("Server is shutting down...");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String newUser = br.readLine();
            User user;

           if ("yes".equalsIgnoreCase(newUser)) {
                do {
                    String username = br.readLine();
                    user = profile.getUserByUsername(username);
                    if (user != null) {
                        pw.println("yes");
                        boolean passwordCorrect = false;
                        pw.println(user);
                        while (!passwordCorrect) {
                            String password = br.readLine();
                            if (user.getPassword().equals(password)) {
                                passwordCorrect = true;
                            } else {
                                pw.println("incorrect_password");
                            }
                        }
                        pw.println("Welcome, " + username + "! What would you like to do? (Type 'friends', 'search', or 'signout')");

                    } else {
                        pw.println("no");
                    }
                } while (user == null);
            }
                    pw.println("Welcome, " + username + "! What would you like to do? (Type 'friends', 'search', or 'signout')");
                    String userChoice = br.readLine();
                    if ("friends".equalsIgnoreCase(userChoice)) {
                        handleFriends(user, pw);
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
            }
        } catch (IOException e) {
            System.err.println("An error occurred with the client connection: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("An error occurred while closing the client socket: " + e.getMessage());
            }
        }
    }

    private void handleFriends(User user, PrintWriter pw) {
        List<User> friendsList = user.getFriends();
        if (friendsList.isEmpty()) {
            pw.println("You have no friends added.");
        } else {
            for (User friend : friendsList) {
                pw.println(friend.getUsername());
            }
        }
    }

    private void handleProfileSearch(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("Do you want to find a user? (yes/no)");
        String choice = br.readLine().trim().toLowerCase();
        if ("yes".equals(choice)) {
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
        } else if (!"no".equals(choice)) {
            pw.println("Invalid choice!");
        }
    }

    public static void stopServer() {
        isRunning = false;
    }
}
