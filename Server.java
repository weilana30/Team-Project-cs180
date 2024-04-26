import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server
 * <p>
 * Server which interacts with the client and allows users to user the platform
 *
 * @author Andrew Weiland, lab section 15
 * @version April 15, 2024
 */
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
                    if (profile.getUserByUsername(username) != null) {
                        pw.write("username");
                        pw.println();
                    } else {
                        accountCorrect = true;
                        pw.write("yes");
                        pw.println();
                    }
                }
                String userInfo = br.readLine();
                user = new User(userInfo);
                user.addUserToFile();
                profile.addUser(user);
            }
            boolean signout = false;
            do {
                String userChoice = br.readLine();
                if ("friends".equalsIgnoreCase(userChoice)) {
                    handleFriends(user, br, pw);
                } else if ("search".equalsIgnoreCase(userChoice)) {
                    PrintWriter writer = new PrintWriter(this.clientSocket.getOutputStream(), false);
                    handleProfileSearch(br, writer, user);
                } else if ("signout".equalsIgnoreCase(userChoice)) {
                    pw.println("Signing out...");
                    signout = true;
                }
            } while (!signout);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void returningUser(String username, BufferedReader br, PrintWriter pw) throws IOException {
        User user = profile.getUserByUsername(username);
        while (user == null) {
            pw.println("no");
            username = br.readLine();
            user = profile.getUserByUsername(username);
        }
        // Once a valid username is found, send user information
        String userInfo = String.format("Username: %s, Name: %s, Password: %s, Email: %s, Phone: %s, Birthday: %s",
                user.getUsername(), user.getName(), user.getPassword(), user.getEmail(),
                user.getPhoneNumber(), user.getBirthday());
        pw.println(userInfo);
    }

    public void handleFriends(User user, BufferedReader bfr, PrintWriter pw) throws IOException {
        File friendsFile = new File(user.getUsername() + "Friends.txt");
        if (!friendsFile.exists()) {
            friendsFile.createNewFile();
        }
        boolean empty = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(user.getUsername()
                + "Friends.txt"))) {
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

                String removeOption = bfr.readLine();

                if (removeOption.equalsIgnoreCase("send")) {
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
                } else if (removeOption.equalsIgnoreCase("remove")) {

                    String deleteOption = bfr.readLine();

                    if (deleteOption.equalsIgnoreCase("delete")) {
                        String messageToRemove = bfr.readLine();

                        ArrayList<String> lines = new ArrayList<>();

                        try (BufferedReader reader = new BufferedReader(new FileReader(first + second + ".txt"))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                lines.add(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        boolean foundMessage = false;
                        boolean nullMessage = false;

                        for (int i = 0; i < lines.size(); i++) {
                            String line = lines.get(i);
                            if (!line.split(": ")[1].equals(messageToRemove)) {
                                lines.set(i, line);
                            } else {
                                foundMessage = true;
                                lines.remove(i);
                                i--;
                            }
                        }

                        try (PrintWriter pwr = new PrintWriter(new FileWriter(first + second + ".txt"))) {
                            for (int i = 0; i < lines.size(); i++) {
                                pwr.print(lines.get(i));
                                if (i < lines.size() - 1) {
                                    pwr.println();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (foundMessage & !nullMessage) {
                            pw.println("yes");
                        } else if (nullMessage) {
                            pw.println("null");
                        } else {
                            pw.println("no");
                        }
                    } else if (!deleteOption.equalsIgnoreCase("profile")) {
                        System.out.println("error");
                    }
                }
            } else if (response.equalsIgnoreCase("view")) {
                String friendToView = bfr.readLine();

                pw.println(profile.getNameByUsername(friendToView));
                pw.println(profile.getEmailByUsername(friendToView));
                pw.println(profile.getBirthdayByUsername(friendToView));

                String unfriendOption = bfr.readLine();

                if (unfriendOption.equals("unfriend")) {

                    boolean friendRemoved;

                    try (BufferedReader reader = new BufferedReader(new FileReader(user.getUsername() +
                            "Friends.txt"))) {

                        List<String> lines = new ArrayList<>();
                        String friendInfo = reader.readLine();

                        while (friendInfo != null) {
                            String friend = friendInfo.split(", ")[0];
                            if (!friend.equals(friendToView)) {
                                lines.add(friendInfo);
                            }
                            friendInfo = reader.readLine();
                        }

                        try (PrintWriter pwr = new PrintWriter(new FileWriter(user.getUsername() +
                                "Friends.txt", false))) {
                            for (String line : lines) {
                                pwr.println(line);
                            }
                        }

                        friendRemoved = true;
                    } catch (IOException e) {
                        friendRemoved = false;
                        e.printStackTrace();
                    }

                    if (friendRemoved) {
                        pw.println("yes");
                        try (BufferedReader reader = new BufferedReader(new FileReader(user.getUsername()
                                + "Friends.txt"))) {
                            String friendUsername;
                            while ((friendUsername = reader.readLine()) != null) {
                                pw.println(friendUsername.split(", ")[0]);
                            }
                            pw.println(" ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        pw.println("no");
                    }
                } else if (!unfriendOption.equalsIgnoreCase("profile")) {
                    System.out.println("error");
                }
            } else if (!response.equalsIgnoreCase("profile")) {
                System.out.println("error.");
            }
        }
    }

    public void handleProfileSearch(BufferedReader br, PrintWriter pw, User currentUser) throws IOException {
        String choice = br.readLine();

        if (choice.equals("yes")) {

            boolean repeatSearch = false;
            do {
                String searchValue = br.readLine();
                int found = 0;
                for (User user : profile.getUsers()) {
                    if (!user.getUsername().equals(currentUser.getUsername())) {
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
                }
                if (found == 0) {
                    pw.println("no");
                }
                pw.println("done");
                pw.flush();
                boolean doAgain = false;
                if (found != 0) {
                    do {
                        String response = br.readLine();
                        if (response.equalsIgnoreCase("search")) {
                            repeatSearch = true;
                        } else if (response.equalsIgnoreCase("end")) {
                            doAgain = false;
                            repeatSearch = false;
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
                                String userChoice = br.readLine();
                                if (userChoice.equalsIgnoreCase("block")) {
                                    String userName = br.readLine();
                                    File file = new File(userName + "Blocked.txt");
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    FileReader reader = new FileReader(file);
                                    BufferedReader bfr = new BufferedReader(reader);
                                    String line;
                                    ArrayList<String> blocked = new ArrayList<>();
                                    while ((line = bfr.readLine()) != null) {
                                        blocked.add(line);
                                    }
                                    bfr.close();
                                    if (blocked.contains(user.toString())) {
                                        PrintWriter blockWriter = new PrintWriter(new FileOutputStream(file));
                                        blockWriter.println(user);
                                        blockWriter.close();
                                    }
                                    doAgain = true;
                                } else if (userChoice.equalsIgnoreCase("add")) {
                                    String userName = br.readLine();
                                    File file = new File(userName + "Friends.txt");
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    FileReader reader = new FileReader(file);
                                    BufferedReader bfr = new BufferedReader(reader);
                                    String line;
                                    ArrayList<String> friends = new ArrayList<>();
                                    while ((line = bfr.readLine()) != null) {
                                        friends.add(line);
                                    }
                                    bfr.close();
                                    if (!friends.contains(user.toString())) {
                                        PrintWriter friendsWriter = new PrintWriter(new FileOutputStream(file, true));
                                        friendsWriter.println(user);
                                        friendsWriter.close();
                                    }
                                    doAgain = true;
                                }
                            }
                        }
                    } while (doAgain);
                }
            } while (repeatSearch);
        }
    }
}
