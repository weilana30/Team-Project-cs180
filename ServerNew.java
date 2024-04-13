import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
            } else {

                boolean accountCorrect = false;

                while (!accountCorrect) {
                    String newAccount = br.readLine();
                    String [] parts = newAccount.split(", ");
                    String username = parts[0].trim();
                    String email = parts[1].trim();
                    String phoneNumber = parts[2].trim();
                    if (profile.getUserByUsername(username) != null) {
                        pw.write("username");
                        pw.println();
                    } else if (profile.getEmailByUsername(email) != null) {
                        pw.write("email");
                        pw.println();
                    } else if (profile.getPhoneNumberByUsername(phoneNumber) != null) {
                        pw.write("phoneNumber");
                        pw.println();
                    } else {
                        System.out.println("Yes");
                        accountCorrect = true;
                        pw.write("yes");
                        pw.println();
                    }
                }
                String userInfo = br.readLine();
                user = new User(userInfo);
                System.out.println("Hello");
                user.addUserToFile();
            }
            String userChoice = br.readLine();
            if ("friends".equalsIgnoreCase(userChoice)) {
                handleFriends(user, br, pw);
            } else if ("search".equalsIgnoreCase(userChoice)) {
                //handleProfileSearch(br, pw);
            } else if ("signout".equalsIgnoreCase(userChoice)) {
                pw.println("Signing out...");
                return;
            } else {
                pw.println("Invalid choice. Please type 'friends', 'search', or 'signout'.");
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
//        pw.println("Do you want to find a user? (yes/no)");
//        String choice = br.readLine().trim().toLowerCase();
//        if (choice.equals("yes")) {
//            pw.println("Choose search criteria (username/phone number/email): ");
//            String searchCriteria = br.readLine().trim().toLowerCase();
//            pw.println("Enter the value to search for: ");
//            String searchValue = br.readLine().trim();
//            switch (searchCriteria) {
//                case "username":
//                    pw.println(profileViewer.displayUserInformationByUsername(searchValue));
//                    break;
//                case "phone number":
//                    pw.println(profileViewer.displayUserInformationByPhoneNumber(searchValue));
//                    break;
//                case "email":
//                    pw.println(profileViewer.displayUserInformationByEmail(searchValue));
//                    break;
//                default:
//                    pw.println("Invalid search criteria!");
//            }
//        } else if (!choice.equals("no")) {
//            pw.println("Invalid choice!");
//        }
    }
}
