import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements Runnable {
    private Socket clientSocket;
    private static Profile profile = new Profile();
    private ProfileViewer profileViewer;
    private Friends friends = new Friends(profile);
    private User user;
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
                    String[] parts = newAccount.split(", ");
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
            System.out.println(userChoice);
            if ("friends".equalsIgnoreCase(userChoice)) {
                handleFriends(user, br, pw);
            } else if ("search".equalsIgnoreCase(userChoice)) {
                //handleProfileSearch(br, pw);
            } else if ("signout".equalsIgnoreCase(userChoice)) {
                pw.println("Signing out...");
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

    private void handleFriends(User user, BufferedReader bfr, PrintWriter pw) throws IOException {

        File friendsFile = new File(user.getUsername() + "sFriends");
        if (!friendsFile.exists()) {
            friendsFile.createNewFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(user.getUsername() + "sFriends"))) {
            String friendUsername;
            while ((friendUsername = reader.readLine()) != null) {
                pw.println(friendUsername);
            }
            pw.println(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        bfr.readLine();
        String response = bfr.readLine();
        System.out.println(response);
        boolean valid;
        do {
            if (response.equalsIgnoreCase("message")) {
                String friendToMessage = bfr.readLine();
                String userMessaging = bfr.readLine();
                String first = "";
                String second = "";

                if (friendToMessage.compareTo(userMessaging) < 0) {
                    first = friendToMessage;
                    second = userMessaging;
                }
                else if (friendToMessage.compareTo(userMessaging) > 0) {
                    first = userMessaging;
                    second = friendToMessage;
                }

                File messageFile = new File(first + second + ".txt");
                if (!messageFile.exists()) {
                    messageFile.createNewFile();
                }

                try (BufferedReader reader = new BufferedReader(new FileReader(first + second + ".txt"))) {

                    String message = reader.readLine();

                    while (message != null) {
                        pw.println(message);
                        message = reader.readLine();
                    }
                    pw.println(" ");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                String message = bfr.readLine();

                try (PrintWriter pwr = new PrintWriter(new FileWriter(first + second + ".txt", true));
                    BufferedReader reader = new BufferedReader(new FileReader(first + second + ".txt"))) {

                    if (reader.readLine() != null) {
                        pwr.println();
                        pwr.write(userMessaging + ": " + message);
                        pwr.flush();
                    }
                    else if (reader.readLine() == null) {
                        pwr.write(userMessaging + ": " + message);
                        pwr.flush();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                pw.write("yes");
                valid = true;
            } else if (response.equalsIgnoreCase("view")) {
                valid = true;
                String friendToView = bfr.readLine();
                String unfriendOption = bfr.readLine();

                System.out.println(unfriendOption);

                if (unfriendOption.equals("unfriend")) {

                    friends.removeFriend(friendToView, user.getUsername());

                    try (BufferedReader reader = new BufferedReader(new FileReader(user.getUsername() + "sFriends"))) {
                        String friendUsername;
                        while ((friendUsername = reader.readLine()) != null) {
                            pw.println(friendUsername);
                        }
                        pw.println(" ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (unfriendOption.equals("profile")) {

                }
            }
            else if (response.equalsIgnoreCase("profile")) {
                valid = true;
            }
            else {
                valid = false;
            }
        } while (!valid);
    }

    private void handleProfileSearch(BufferedReader br, PrintWriter pw) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            // Prompt the user to initiate a search
            System.out.print("Do you want to find a user? (yes/no): ");
            choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("yes")) {
                // Prompt the user to choose search criteria
                System.out.print("Choose search criteria (username/phone number/email): ");
                String searchCriteria = scanner.nextLine().trim().toLowerCase();
                // Prompt the user to enter the value to search for
                System.out.print("Enter the value to search for: ");
                String searchValue = scanner.nextLine().trim();
                // Perform search based on the chosen criteria
                switch (searchCriteria) {
                    case "username":
                        if (profile.getUserByUsername(searchValue) == null) {
                            System.out.println("Make sure you enter the correct username!");
                        } else {
                            System.out.println(profileViewer.displayUserInformationByUsername(searchValue));
                            System.out.println("Do you want to add this user as your friend?(yes/no): ");
                            String addFriendAnswer = scanner.nextLine().trim().toLowerCase();
                            if (addFriendAnswer == "yes") {
                                friends.addFriend(profile.getUserByUsername(searchValue).getUsername(), user.getUsername());
                            }
                        }
                        break;
                    case "phone number":
                        if (profile.getUserByPhoneNumber(searchValue) == null) {
                            System.out.println("Make sure you enter the correct phone number!");
                        } else {
                            System.out.println(profileViewer.displayUserInformationByPhoneNumber(searchValue));
                            System.out.println("Do you want to add this user as your friend?(yes/no): ");
                            String addFriendAnswer = scanner.nextLine().trim().toLowerCase();
                            if (addFriendAnswer == "yes") {
                                friends.addFriend(profile.getUserByPhoneNumber(searchValue).getUsername(), user.getUsername());
                            }
                        }
                        break;
                    case "email":
                        if (profile.getUserByEmail(searchValue) == null) {
                            System.out.println("Make sure you enter the correct email!");
                        } else {
                            System.out.println(profileViewer.displayUserInformationByEmail(searchValue));
                            System.out.println("Do you want to add this user as your friend?(yes/no): ");
                            String addFriendAnswer = scanner.nextLine().trim().toLowerCase();
                            if (addFriendAnswer == "yes") {
                                friends.addFriend(profile.getUserByEmail(searchValue).getUsername(), user.getUsername());
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid search criteria!");
                }
            }
        } while (choice.equals("yes")); // Repeat the search process if the user wants to continue
        System.out.println("Exiting search.");
    }
}
