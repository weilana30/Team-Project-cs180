import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Client
 * <p>
 * Client which interacts with the server so the user can use the app
 *
 * @author Andrew Weiland, lab section 15
 * @version April 15, 2024
 */
public class Client {
    static JFrame loginFrame = new JFrame("Login");
    static JFrame usernameFrame = new JFrame("Enter Username");
    static JFrame passwordFrame = new JFrame("Enter Password");

    public static void main(String[] args) throws IOException, NullPointerException {
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             InputStream is = socket.getInputStream();
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
             Scanner scan = new Scanner(System.in)) {
            boolean newOrReturning = showLogInMessage(pw);
            boolean continueGoing = false;
            boolean newUser = false;
            String[] userInformation = new String[0];
            do {
                if (newOrReturning) {
                    //sends yes to the server if they are a returning user
                    pw.println("yes");
                    continueGoing = true;
                } else {
                    //send server a no if they are a new user
                    pw.write("no");
                    pw.println();
                    boolean invalidInformation = false;
                    do {
                        String newUserInfo = createNewUsername();
                        //sends a string of the new users username, email, number, and birthday
                        pw.write(newUserInfo);
                        pw.println();
                        String validNewUser = bfr.readLine();
                        //if the username, email, and number are not taken
                        if (validNewUser.equals("yes")) {
                            //creates a new password
                            String password = createNewPassword();
                            String[] information = newUserInfo.split(", ");
                            String userString = information[0] + ", " + information[1] + ", " + password + ", " +
                                    information[2] + ", " + information[3] + ", " + information[4];
                            pw.println(userString);
                            userInformation = new String[]{information[0], information[1], password,
                                    information[2], information[3], information[4]};
                            continueGoing = true;
                            newUser = true;
                            invalidInformation = true;
                        } else {
                            if (validNewUser.equals("username")) {
                                System.out.println("The username is already taken.");
                                if (is.available() > 0) {
                                    validNewUser = bfr.readLine();
                                }
                            }
                            if (validNewUser.equals("email")) {
                                System.out.println("There is already an account with that email.");
                                if (is.available() > 0) {
                                    validNewUser = bfr.readLine();
                                }
                            }
                            if (validNewUser.equals("phoneNumber")) {
                                System.out.println("There is already an account with that phoneNumber.");
                            }
                        }
                        //continues looping until the information is new and valid
                    } while (!invalidInformation);
                }
            } while (!continueGoing);
            boolean isUser;
            if (!newUser) {
                do {
                    String validUser = bfr.readLine();
                    if (validUser.equals("no")) {
                        JOptionPane.showMessageDialog(null, "The Username, email, or phone-Number you entered does not have an account", "Search Database",
                                JOptionPane.ERROR_MESSAGE);
                        isUser = false;
                    } else {
                        usernameFrame.dispose();
                        isUser = true;
                    }
                } while (!isUser);
                String userInfoString = bfr.readLine();
                //receives the user information from the server
                // if they are a valid user and splits it into each component
                String[] userInfo = userInfoString.split(", ");
                //this should check if the password is correct after
                int attempts = 0;
                boolean validPassword;
                do {
                    enterPassword(pw);

                    String password = bfr.readLine();

                    if (!password.equals(userInfo[2])) {
                        JOptionPane.showMessageDialog(null, "That is the wrong password. Please try again! You have " + (2 - attempts) + " attempts remaining.", "Incorrect Password",
                                JOptionPane.ERROR_MESSAGE);
                        pw.print("no");
                        pw.println();
                        pw.flush();
                        attempts += 1;
                        validPassword = false;
                    } else {
                        pw.print("yes");
                        pw.println();
                        pw.flush();
                        passwordFrame.dispose();
                        JOptionPane.showMessageDialog(null, "Login Successful!");
                        attempts = 3;
                        validPassword = true;
                    }
                    if (attempts == 3 && !validPassword) {
                        passwordFrame.dispose();
                        JOptionPane.showMessageDialog(null, "You have used your maximum attempts. " +
                                "You will now be logged out to prevent suspicious activity");
                    }
                } while (attempts < 3);

                if (validPassword) {
                    userInformation = userInfo;
                }
            }

            //create a user object when logged in

            boolean askQuestion;
            String response;
            boolean logout = false;
            do {
                do {
                    showProfilePage(userInformation, pw, bfr);
                    System.out.println("Welcome to textogram. What would you like to do? " +
                            "(Type 'friends', 'search', or 'signout')");
                    response = scan.nextLine();
                    if (!response.equalsIgnoreCase("friends") &&
                            !response.equalsIgnoreCase("search") &&
                            !response.equalsIgnoreCase("signout")) {
                        System.out.println("Not a valid response");
                        askQuestion = true;
                    } else {
                        askQuestion = false;
                    }
                } while (askQuestion);
                pw.write(response);
                pw.println();
                pw.flush();
                boolean validResponse = false;
                do {
                    if (response.equalsIgnoreCase("friends")) {
                        friendsOption(pw, bfr, userInformation);
                        validResponse = true;
                    } else if (response.equalsIgnoreCase("search")) {
                        try {
                            searchUsers(pw, bfr, userInformation);
                            validResponse = true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (response.equalsIgnoreCase("signout")) {
                        logout = true;
                        validResponse = true;
                    } else {
                        validResponse = false;
                    }
                } while (!validResponse);
            } while (!logout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void friendsOption(PrintWriter pw, BufferedReader bfr,
                                     String[] userInfo) throws IOException {
        pw.write("friends");
        pw.println();
        pw.flush();

        String friends = bfr.readLine();
        ArrayList<String> allFriendsUsers = new ArrayList<>();
        ArrayList<String> allFriends = new ArrayList<>();

        if (friends.equals(" ")) {
            System.out.println("No friends found!\n\n");
        } else {
            while (!friends.equals(" ")) {
                allFriendsUsers.add(friends.split(", ")[0]);
                allFriends.add(friends);
                friends = bfr.readLine();
            }

            JFrame frame = new JFrame("TextOGram - Friends");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setResizable(true);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            JPanel friendsPanel = new JPanel(new BorderLayout());
            JLabel friendsLabel = new JLabel("Here are your friends:");
            friendsPanel.add(friendsLabel, BorderLayout.NORTH);

            DefaultListModel<String> friendsListModel = new DefaultListModel<>();

            for (String friend : allFriendsUsers) {
                friendsListModel.addElement(friend);
            }

            JList<String> friendsList = new JList<>(friendsListModel);

            friendsPanel.add(new JScrollPane(friendsList), BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton messageButton = new JButton("Message");
            messageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedFriend = friendsList.getSelectedValue();
                    if (selectedFriend != null) {
                        pw.write("message");
                        pw.println();
                        pw.flush();

                        pw.write(selectedFriend);
                        pw.println();
                        pw.flush();

                        pw.write(userInfo[0]);
                        pw.println();
                        pw.flush();

                        try {
                            openMessageWindow(selectedFriend, pw, bfr, frame);
                            frame.setVisible(false);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please select a friend to message.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton viewButton = new JButton("View");
            viewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedFriend = friendsList.getSelectedValue();
                    if (selectedFriend != null) {
                        pw.write("view");
                        pw.println();
                        pw.flush();

                        pw.write(selectedFriend);
                        pw.println();
                        pw.flush();

                        openViewWindow(selectedFriend, pw, bfr, frame, friendsPanel);
                        frame.setVisible(false);

                    } else {
                        JOptionPane.showMessageDialog(frame, "Please select a friend to view.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });


            JButton profileButton = new JButton("Profile");
            profileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("profile");
                    pw.println();
                    pw.flush();

                    frame.setVisible(false);
                    showProfilePage(userInfo, pw, bfr);
                }
            });

            buttonPanel.add(messageButton);
            buttonPanel.add(viewButton);
            buttonPanel.add(profileButton);

            frame.add(friendsPanel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        }
    }

    private static void openMessageWindow(String friendUsername, PrintWriter pw, BufferedReader bfr, JFrame frame) throws IOException {
        JFrame messageFrame = new JFrame("Message to " + friendUsername);
        messageFrame.setSize(400, 300);
        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageFrame.setLocationRelativeTo(null);
        messageFrame.setLayout(new BorderLayout());

        JTextArea messageHistoryArea = new JTextArea();

        String message = bfr.readLine();
        String fullMessageHistory = messageHistoryArea.getText();

        while (!message.equals(" ")) {
            if (!fullMessageHistory.isEmpty()) {
                fullMessageHistory += "\n";
            }
            fullMessageHistory += message;
            message = bfr.readLine();
        }

        messageHistoryArea.setText(fullMessageHistory);
        messageHistoryArea.setEditable(false);
        JScrollPane historyScrollPane = new JScrollPane(messageHistoryArea);
        messageFrame.add(historyScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextArea messageInputArea = new JTextArea();
        inputPanel.add(messageInputArea, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!messageInputArea.getText().isEmpty()) {
                    sendMessage(pw, messageInputArea);
                    messageFrame.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Message sent!",
                            "TextOGram", JOptionPane.PLAIN_MESSAGE);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a message.",
                            "TextOGram", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        inputPanel.add(sendButton, BorderLayout.SOUTH);
        messageFrame.add(inputPanel, BorderLayout.SOUTH);

        messageFrame.setVisible(true);
    }


    private static void sendMessage(PrintWriter pw, JTextArea messageArea) {
        String message = messageArea.getText();

        pw.println(message);
        messageArea.setText("");
    }

    private static void openViewWindow(String friendUsername, PrintWriter pw, BufferedReader bfr, JFrame frame, JPanel friendsPanel) {
        JFrame viewFrame = new JFrame(friendUsername + "'s Profile");
        viewFrame.setSize(400, 300);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setLayout(new GridLayout(4, 1));

        JPanel usernamePanel = new JPanel(new BorderLayout());
        JPanel namePanel = new JPanel(new BorderLayout());
        JPanel emailPanel = new JPanel(new BorderLayout());
        JPanel birthdayPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        String name = "";
        String email = "";
        String birthday = "";

        try {
            name = bfr.readLine();
            email = bfr.readLine();
            birthday = bfr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel usernameLabel = new JLabel("Username: " + friendUsername);
        JLabel nameLabel = new JLabel("Name: " + name);
        JLabel emailLabel = new JLabel("Email: " + email);
        JLabel birthdayLabel = new JLabel("Birthday: " + birthday);

        usernamePanel.add(usernameLabel, BorderLayout.CENTER);
        namePanel.add(nameLabel, BorderLayout.CENTER);
        emailPanel.add(emailLabel, BorderLayout.CENTER);
        birthdayPanel.add(birthdayLabel, BorderLayout.CENTER);

        viewFrame.add(usernamePanel);
        viewFrame.add(namePanel);
        viewFrame.add(emailPanel);
        viewFrame.add(birthdayPanel);

        JButton unfriendButton = new JButton("Unfriend");
        JButton leaveButton = new JButton("Leave");

        unfriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.write("unfriend");
                pw.println();
                pw.flush();

                ArrayList<String> users = new ArrayList<>();

                String result = "";

                try {
                    result = bfr.readLine();
                    String friend = bfr.readLine();
                    while (!friend.equals(" ")) {
                        users.add(friend);
                        friend = bfr.readLine();
                    }
                    System.out.println(result);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println(users);

                viewFrame.setVisible(false);

                if (result.equals("yes")) {
                    JOptionPane.showMessageDialog(null, "Friend successfully removed!",
                            "TextOGram", JOptionPane.PLAIN_MESSAGE);
                    updateFrame(frame, users, friendsPanel);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Sorry, there was an error!",
                            "TextOGram", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.write("leave");
                pw.println();
                pw.flush();

                viewFrame.setVisible(false);
                frame.setVisible(true);
            }
        });

        buttonPanel.add(unfriendButton);
        buttonPanel.add(leaveButton);

        viewFrame.add(buttonPanel);

        viewFrame.setVisible(true);
    }

    private static void updateFrame(JFrame frame, ArrayList<String> users, JPanel friendsPanel) {
        frame.getContentPane().remove(friendsPanel);
        frame.setLocationRelativeTo(null);

        JPanel newFriendsPanel = new JPanel(new BorderLayout());
        JLabel friendsLabel = new JLabel("Here are your friends:");
        newFriendsPanel.add(friendsLabel, BorderLayout.NORTH);

        DefaultListModel<String> friendsListModel = new DefaultListModel<>();

        for (String user : users) {
            friendsListModel.addElement(user);
        }

        JList<String> friendsList = new JList<>(friendsListModel);

        newFriendsPanel.add(new JScrollPane(friendsList), BorderLayout.CENTER);

        frame.getContentPane().add(newFriendsPanel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    public static void searchUsers(PrintWriter pw, BufferedReader bfr, String[] userInfo)
            throws IOException, InterruptedException {
        boolean validResponse = false;
        Scanner scan = new Scanner(System.in);
        String response;
        JFrame frame = new JFrame("User Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());

        JButton backToProfile = new JButton("Profile");
        JButton search = new JButton("Search");
        JTextField searchText = new JTextField("", 5);
        backToProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == backToProfile) {
                    pw.println("no");
                    frame.setVisible(false);
                    showProfilePage(userInfo, pw, bfr);
                }
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == search) {
                    pw.println("yes");
                    String search = searchText.getText();
                    pw.println(search);

                    ArrayList<String> users = new ArrayList<>();
                    String fUser = "";
                    try {
                        String firstUser = bfr.readLine();
                        users.add(firstUser);
                        System.out.println(firstUser);

                        while (!fUser.equalsIgnoreCase("done")) {
                            fUser = bfr.readLine();
                            if (fUser.equalsIgnoreCase("done")) {
                                break;
                            }
                            if (!fUser.equalsIgnoreCase(userInfo[0])) {
                                users.add(fUser);
                            }
                            for (String user : users) {
                                System.out.println(user);
                            }

                        }
                        System.out.println("hello");
                        for (String user : users) {
                            System.out.println(user);
                        }
                        if (!users.get(0).equalsIgnoreCase("no")) {
                            System.out.println("here");
                            frame.setVisible(false);
                            openFoundUsers(users, pw, bfr, userInfo);
                        } else {
                            JOptionPane.showMessageDialog(frame, "No users found.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception er) {
                        System.out.println("An error occurred");
                    }
                }
            }
        });


        JPanel panel = new JPanel();

        panel.add(backToProfile);
        panel.add(searchText);
        panel.add(search);
        frame.add(panel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea description = new JTextArea(String.valueOf("Type a username, email, or name of a User\n" +
                "                 then press search!"));
        centerPanel.add(description);
        description.setEditable(false);
        frame.setSize(400, 200);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
//        do {
//            System.out.println("Would you like to search for a user? yes or no");
//            response = scan.nextLine();
//            if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
//                System.out.println("Not a valid response");
//            } else {
//                validResponse = true;
//            }
//        } while (!validResponse);
//        if (response.equalsIgnoreCase("yes")) {
//            pw.println(response);
//            boolean searchUser = true;
//            do {
//                System.out.println("Please enter the user you are searching for?");
//                String userToSearch = scan.nextLine();
//                //sends the server the name they are searching for
//                pw.println(userToSearch);
//                ArrayList<String> users = new ArrayList<>();
//                String fUser = "";
//                String firstUser = bfr.readLine();
//                if (!firstUser.equalsIgnoreCase(userName) && !firstUser.equalsIgnoreCase(userName)) {
//                    System.out.println(firstUser);
//                }
//                users.add(firstUser);
//                while (!fUser.equalsIgnoreCase("done")) {
//                    fUser = bfr.readLine();
//                    if (fUser.equalsIgnoreCase("done")) {
//                        break;
//                    }
//                    if (!fUser.equalsIgnoreCase(userName)) {
//                        users.add(fUser);
//                    }
//                }
//                if (users.get(0).equalsIgnoreCase("no") || (users.size() == 1 &&
//                        users.get(0).equalsIgnoreCase(userName))) {
//                    boolean again = false;
//                    do {
//                        System.out.println("There were no results.\n" +
//                                "If you want to search again, type search. " +
//                                "If you want to go back to your profile, type profile.");
//                        String noResultsResponse = scan.nextLine();
//                        if (noResultsResponse.equalsIgnoreCase("profile")) {
//                            searchUser = false;
//                            pw.println("end");
//                        } else if (!noResultsResponse.equalsIgnoreCase("search")) {
//                            System.out.println("Not a valid response");
//                            again = true;
//                        } else if (noResultsResponse.equals("search")) {
//                            again = false;
//                            pw.println("search");
//                        }
//                    } while (again);
//                } else {
//                    System.out.println("Users Found:");
//                    for (String username : users) {
//                        if (!username.equalsIgnoreCase(userName)) {
//                            System.out.println(username);
//                        }
//                    }
//                    boolean validUsername = false;
//                    do {
//                        System.out.println("If you want to view one of these users profiles enter their username.\n" +
//                                "If you want to search again, type search. " +
//                                "If you want to go back to your profile, type profile.");
//                        String nextResponse = scan.nextLine();
//                        if (nextResponse.equalsIgnoreCase("profile")) {
//                            searchUser = false;
//                            validUsername = true;
//                            pw.println("end");
//                        } else if (!nextResponse.equalsIgnoreCase("search")) {
//                            //sends the username to open the profile
//                            pw.println(nextResponse);
//                            //should send back the users string
//                            String profile = bfr.readLine();
//                            //sends back no if it not a valid username
//                            if (profile.equalsIgnoreCase("no")) {
//                                System.out.println("That username is not valid");
//                            } else {
//                                validUsername = true;
//                                String[] info = profile.split(", ");
//                                //prints the profile of the user
//                                showProfilePage(info);
//                                boolean repeatFriend = false;
//                                do {
//                                    //asks the user if they want to add them as a friend
//                                    System.out.println("Would you like to add the user as a friend or " +
//                                            "block them? Enter add " +
//                                            "or block, or entire profile to return to the profile");
//                                    String friend = scan.nextLine();
//                                    //if block, add the user to the blocked list
//                                    if (friend.equalsIgnoreCase("block")) {
//                                        //blocks the user
//                                        pw.println("block");
//                                        pw.println(userName);
//                                        System.out.println("User blocked Successfully. Returning to profile.");
//                                        searchUser = false;
//                                        //if add, adds the user to friends list
//                                    } else if (friend.equalsIgnoreCase("add")) {
//                                        //adds the user as a friend
//                                        pw.println("add");
//                                        pw.println(userName);
//                                        System.out.println("Friend added Succesfully. Returning to profile.");
//                                        searchUser = false;
//                                        repeatFriend = false;
//                                        //if profile, returns to user profile
//                                    } else if (friend.equalsIgnoreCase("profile")) {
//                                        searchUser = false;
//                                        pw.println("done");
//                                        repeatFriend = false;
//                                    } else {
//                                        System.out.println("Not a valid response");
//                                        repeatFriend = true;
//                                    }
//                                } while (repeatFriend);
//                            }
//                        } else {
//                            pw.println("search");
//                            validUsername = true;
//                            searchUser = true;
//                        }
//                    } while (!validUsername);
//                }
//            } while (searchUser);
//        } else {
//            pw.println("no");
//        }
    }

    public static void openFoundUsers(ArrayList<String> users, PrintWriter pw, BufferedReader bfr, String[] userInfo) {
        JFrame foundUsers = new JFrame("Here are the found users");
        foundUsers.setSize(400, 200);
        foundUsers.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        foundUsers.setLayout(new BorderLayout());
        DefaultListModel<String> foundListModel = new DefaultListModel<>();
        for (String user : users) {
            foundListModel.addElement(user);
        }
        JList<String> foundList = new JList<>(foundListModel);
        foundUsers.add(new JScrollPane(foundList), BorderLayout.CENTER);
        foundUsers.setLocationRelativeTo(null);
        JButton viewUser = new JButton("View User");
        viewUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = foundList.getSelectedValue();
                pw.println(selectedUser);
                try {
                    String userString = bfr.readLine();
                    if (userString.equalsIgnoreCase("no")) {
                        JOptionPane.showMessageDialog(foundUsers, "Please select a user.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        foundUsers.setVisible(false);
                        searchedUserProfile(pw, bfr, userString, userInfo);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        foundUsers.add(viewUser, BorderLayout.SOUTH);
        foundUsers.setVisible(true);
    }


    public static void searchedUserProfile(PrintWriter pw, BufferedReader bfr, String user, String[] userInfoSplit) {
        String[] profilePageThings = user.split(", ");
        String username = profilePageThings[0];
        String name = profilePageThings[1];
        String email = profilePageThings[3];
        String birthday = profilePageThings[5];
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("Username: ").append(username).append("\n");
        userInfo.append("Name: ").append(name).append("\n");
        userInfo.append("Email: ").append(email).append("\n");
        userInfo.append("Birthday: ").append(birthday).append("\n");
        System.out.println("fuck");
        JFrame frame = new JFrame(String.valueOf(profilePageThings[0]));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea description = new JTextArea(String.valueOf(userInfo));
        description.setEditable(false);
        frame.setSize(400, 300);
        JButton addFriend = new JButton("Add Friend");
        addFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addFriend) {
                    pw.println("add");
                    pw.println("end");
                    JOptionPane.showMessageDialog(frame, "User Successfully added as friend.",
                            "User added", JOptionPane.PLAIN_MESSAGE);
                    frame.setVisible(false);
                    showProfilePage(userInfoSplit, pw, bfr);
                }
            }
        });
        JButton block = new JButton("Block");
        block.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == block) {
                    pw.println("block");
                    pw.println("end");
                    JOptionPane.showMessageDialog(frame, "User Successfully blocked.",
                            "User blocked", JOptionPane.PLAIN_MESSAGE);
                    frame.setVisible(false);
                    showProfilePage(userInfoSplit, pw, bfr);
                }
            }
        });
        JButton profile = new JButton("Return to Profile");
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.println("profile");
                pw.println("end");
                frame.setVisible(false);
                showProfilePage(userInfoSplit, pw, bfr);
            }
        });
        JPanel panel = new JPanel();
        panel.add(block);
        panel.add(addFriend);
        panel.add(profile);
        frame.setLayout(new BorderLayout());
        frame.add(description, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    public static boolean showLogInMessage(PrintWriter pw) {
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 100);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(true);
        loginFrame.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton newUserButton = new JButton("New User");
        JButton returningUserButton = new JButton("Returning User");
        buttonPanel.add(newUserButton);
        buttonPanel.add(returningUserButton);
        loginFrame.add(buttonPanel, BorderLayout.NORTH);

        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                loginFrame.dispose();
                createNewUserGUI(pw);
            }
        });

        returningUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    enterUsername(pw);
                    loginFrame.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        loginFrame.setVisible(true);
        return true;
    }


    public static String createNewUsername() {
        System.out.println("Please enter the username for your new account");
        Scanner scan = new Scanner(System.in);
        String username = scan.nextLine();
        System.out.println("Please enter the email for your new account");
        String email = scan.nextLine();
        System.out.println("Please enter the phone number for your new account");
        String phone = scan.nextLine();
        System.out.println("Please enter your birthday in the format MM-DD-YYYY");
        String birthday = scan.nextLine();
        System.out.println("Please enter your first and last name");
        String name = scan.nextLine();
        return String.format("%s, %s, %s, %s, %s", username, name, email, phone, birthday);
    }
    public static String createNewPassword() {
        boolean same = false;
        String passwordOne;
        String passwordTwo;
        do {
            do {
                System.out.println("Please enter your password for your new account");
                System.out.println("The password must contain at least one uppercase letter, lowercase");
                System.out.println("letter, number, and special character");
                Scanner scan = new Scanner(System.in);
                passwordOne = scan.nextLine();
                System.out.println("Please re-enter your new password");
                passwordTwo = scan.nextLine();
                if (passwordOne.equals(passwordTwo)) {
                    same = true;
                }
            } while (!same);
            if (!checkPassword(passwordOne)) {
                System.out.println("The password does not include all of the required characters");
            }
        } while (!checkPassword(passwordOne));
        return passwordOne;
    }
    // this method checks if the password inputted meets all the requirements for a secure password
    public static boolean checkPassword(String password) {
        // creating 3 different string representations of allowed symbols from a keyboard
        String chars = "~ ` ! @ # $ % ^ & * ( ) - _ + = { [ } ] | \\ ' ; : ? / > . < ,";
        String ints = "1 2 3 4 5 6 7 8 9 0";
        String letters = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
        // splitting these different strings into string arrays, so they are iterable
        String[] requiredChars = chars.split(" ");
        String[] requiredInts = ints.split(" ");
        String[] requiredLetters = letters.split(" ");
        // initializing 4 boolean conditions that must be met by the end to be successful
        boolean hasChar = false;
        boolean hasInt = false;
        boolean hasUpper = false;
        boolean hasLower = false;
        // iterating through each symbol array and making sure the password to check has
        // at least one of the necessary symbols for each required symbol
        for (String character : requiredChars) {
            if (password.contains(character)) {
                hasChar = true;
                break;
            }
        }
        for (String number : requiredInts) {
            if (password.contains(number)) {
                hasInt = true;
                break;
            }
        }
        for (String letter : requiredLetters) {
            if (password.contains(letter.toUpperCase())) {
                hasUpper = true;
                break;
            }
        }
        for (String letter : requiredLetters) {
            if (password.contains(letter)) {
                hasLower = true;
                break;
            }
        }
        // returns true if all boolean conditions are met
        // otherwise returns false
        return hasChar & hasInt & hasUpper & hasLower;
    }
    public static void enterUsername(PrintWriter pw) throws IOException {
        usernameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        usernameFrame.setSize(300, 150);
        usernameFrame.setLocationRelativeTo(null);
        usernameFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JTextField usernameField = new JTextField();
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                pw.println(username);
            }
        });

        panel.add(new JLabel("Enter your username, email, or phone number:"));
        panel.add(usernameField);
        panel.add(submitButton);

        usernameFrame.add(panel, BorderLayout.CENTER);
        usernameFrame.setVisible(true);
    }
    public static void enterPassword(PrintWriter pw) throws IOException {

        passwordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        passwordFrame.setSize(300, 150);
        passwordFrame.setLocationRelativeTo(null);
        passwordFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JPasswordField passwordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordField.getText();

                // Perform action with the entered username
                // For example, send it to the server
                pw.print(password);
                pw.println();
                pw.flush();
                passwordField.setText("");
            }
        });

        panel.add(new JLabel("Enter your password:"));
        panel.add(passwordField);
        panel.add(submitButton);

        passwordFrame.add(panel, BorderLayout.CENTER);
        passwordFrame.setVisible(true);
    }
    public static void createNewUserGUI(PrintWriter pw) {
        JFrame newUserFrame = new JFrame("New User Registration");
        newUserFrame.setSize(400, 300);
        newUserFrame.setLayout(new BorderLayout());

        JPanel userInfoPanel = new JPanel(new GridLayout(5, 2));

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField birthdayField = new JTextField();
        JTextField nameField = new JTextField();

        userInfoPanel.add(new JLabel("Username:"));
        userInfoPanel.add(usernameField);
        userInfoPanel.add(new JLabel("Email:"));
        userInfoPanel.add(emailField);
        userInfoPanel.add(new JLabel("Phone Number:"));
        userInfoPanel.add(phoneField);
        userInfoPanel.add(new JLabel("Birthday (MM-DD-YYYY):"));
        userInfoPanel.add(birthdayField);
        userInfoPanel.add(new JLabel("Name:"));
        userInfoPanel.add(nameField);

        JButton registerButton = new JButton("Register");
        newUserFrame.add(userInfoPanel, BorderLayout.CENTER);
        newUserFrame.add(registerButton, BorderLayout.SOUTH);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUserInfo = String.format("%s, %s, %s, %s, %s",
                        usernameField.getText(), nameField.getText(),
                        emailField.getText(), phoneField.getText(), birthdayField.getText());

                pw.write(newUserInfo);
                pw.println();
                newUserFrame.dispose();
            }
        });

        newUserFrame.setVisible(true);
    }
    public static void showProfilePage(String[] profilePageThings, PrintWriter pw, BufferedReader bfr) {
        //splits the user information;
        String username = profilePageThings[0];
        String name = profilePageThings[1];
        String email = profilePageThings[3];
        String birthday = profilePageThings[5];
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("Username: ").append(username).append("\n");
        userInfo.append("Name: ").append(name).append("\n");
        userInfo.append("Email: ").append(email).append("\n");
        userInfo.append("Birthday: ").append(birthday).append("\n");
        JFrame frame = new JFrame(String.valueOf(profilePageThings[0]));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea description = new JTextArea(String.valueOf(userInfo));
        frame.setSize(400, 300);
        JButton search = new JButton("Search");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == search) {
                    pw.println("search");
                    frame.setVisible(false);
                    try {
                        searchUsers(pw, bfr, profilePageThings);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        JButton friends = new JButton("Friends");
        friends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == friends) {
                    pw.println("friends");
                    frame.setVisible(false);
                    try {
                        friendsOption(pw, bfr, profilePageThings);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        JButton signout = new JButton("Sign out");
        signout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //signout
            }
        });
        JPanel panel = new JPanel();
        description.setEditable(false);
        panel.add(search);
        panel.add(friends);
        panel.add(signout);
        frame.setLayout(new BorderLayout());
        frame.add(description, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
