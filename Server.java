import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
                        pw.println(user);
                        boolean passwordCorrect = false;
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
            boolean signout = false;
            do {
                String userChoice = br.readLine();
                System.out.println(userChoice);
                if ("friends".equalsIgnoreCase(userChoice)) {
                    handleFriends(user, br, pw);
                } else if ("search".equalsIgnoreCase(userChoice)) {
                    PrintWriter writer = new PrintWriter(this.clientSocket.getOutputStream(), false);
                    handleProfileSearch(br, writer);
                } else if ("signout".equalsIgnoreCase(userChoice)) {
                    pw.println("Signing out...");
                    signout = true;
                }
            } while (!signout);

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
        File friendsFile = new File(user.getUsername() + "Friends.txt");
        if (!friendsFile.exists()) {
            friendsFile.createNewFile();
        }
        boolean empty = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(user.getUsername() + "Friends.txt"))) {
            String friendUsername;
            while ((friendUsername = reader.readLine()) != null) {
                empty = false;
                pw.println(friendUsername);
            }
            pw.println(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!empty) {
            bfr.readLine();
            String response = bfr.readLine();
            System.out.println(response);
            if (response.equalsIgnoreCase("message")) {
                String friendToMessage = bfr.readLine();
                String userMessaging = bfr.readLine();
                String first = "";
                String second = "";

                if (friendToMessage.compareTo(userMessaging) < 0) {
                    first = friendToMessage;
                    second = userMessaging;
                } else if (friendToMessage.compareTo(userMessaging) > 0) {
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
                    } else if (reader.readLine() == null) {
                        pwr.write(userMessaging + ": " + message);
                        pwr.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pw.println("yes");
            } else if (response.equalsIgnoreCase("view")) {
                String friendToView = bfr.readLine();
                String unfriendOption = bfr.readLine();

                System.out.println(unfriendOption);

                if (unfriendOption.equals("unfriend")) {

                    friends.removeFriend(friendToView, user.getUsername());

                    try (BufferedReader reader = new BufferedReader(new FileReader(user.getUsername() + "Friends.txt"))) {
                        String friendUsername;
                        while ((friendUsername = reader.readLine()) != null) {
                            pw.println(friendUsername);
                        }
                        pw.println(" ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleProfileSearch(BufferedReader br, PrintWriter pw) throws IOException {
        String choice = br.readLine();
        System.out.println(choice);
        if (choice.equals("yes")) {

            boolean repeatSearch = false;
            do {
                System.out.println("beginning");
                String searchValue = br.readLine();
                System.out.println(searchValue);
                System.out.println("here");
                int found = 0;
                for (User user : profile.getUsers()) {
                    if (user.getUsername().contains(searchValue)) {
                        pw.println(user.getUsername());
                        found++;
                    } else if (user.getName().contains(searchValue)) {
                        pw.println(user.getUsername());
                        found++;
                    } else if (user.getEmail().contains(searchValue)) {
                        found++;
                        pw.println(user.getUsername());
                    }
                }
                if (found == 0) {
                    pw.println("no");
                }
                pw.println("done");
                pw.flush();
                boolean doAgain = false;
                do {
                    String response = br.readLine();
                    if (response.equalsIgnoreCase("search")) {
                        repeatSearch = true;
                    } else if (response.equalsIgnoreCase("end")) {
                        doAgain = false;
                    } else {
                        repeatSearch = false;
                        User user = profile.getUserByUsername(response);

                        if (user == null) {
                            pw.println("no");
                            pw.flush();
                            doAgain = true;
                        } else {
                            pw.println(user);
                            pw.flush();
                            System.out.println(user);
                            String userChoice = br.readLine();
                            if (userChoice.equalsIgnoreCase("block")) {
                                String userName = br.readLine();
                                File file = new File(userName + "Blocked.txt");
                                PrintWriter blockWriter = new PrintWriter(new FileOutputStream(file));
                                blockWriter.println(user);
                                blockWriter.close();
                            } else if (userChoice.equalsIgnoreCase("add")) {
                                System.out.println("here");
                                String userName = br.readLine();
                                System.out.println(userName);
                                File file = new File(userName + "Friends.txt");
                                PrintWriter friendsWriter = new PrintWriter(new FileOutputStream(file));
                                friendsWriter.println(user);
                                friendsWriter.close();
                            }
                        }
                    }
                } while (doAgain);
            } while (repeatSearch);
        }
    }
}
