import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        Socket socket = new Socket("localhost", 1234);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        showLogInMessage(pw, bfr);
    }

    public static void friendsOption(PrintWriter pw, BufferedReader bfr,
                                     String[] userInfo) throws IOException {
        pw.write("friends");
        pw.println();
        pw.flush();

        String friends = bfr.readLine();
        ArrayList<String> allFriendsUsers = new ArrayList<>();

        if (friends.equals(" ")) {
            System.out.println("No friends found!\n\n");
            JOptionPane.showMessageDialog(null, "You have no friends. Go make some!",
                    "TextOGram - Friends", JOptionPane.PLAIN_MESSAGE);
            showProfilePage(userInfo, pw, bfr);
        } else {
            while (!friends.equals(" ")) {
                allFriendsUsers.add(friends.split(", ")[0]);
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
                            openMessageWindow(selectedFriend, pw, bfr, frame, userInfo);
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

                        openViewWindow(selectedFriend, pw, bfr, frame, friendsPanel, userInfo);
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

    private static void openMessageWindow(String friendUsername, PrintWriter pw, BufferedReader bfr, JFrame frame, String[] userInfo) throws IOException {
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

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!messageInputArea.getText().isEmpty()) {
                    pw.write("send");
                    pw.println();
                    pw.flush();

                    sendMessage(pw, messageInputArea);
                    messageFrame.setVisible(false);

                    try {
                        if (bfr.readLine().equalsIgnoreCase("yes")) {
                            JOptionPane.showMessageDialog(messageFrame, "Message sent!",
                                    "TextOGram", JOptionPane.PLAIN_MESSAGE);
                            showProfilePage(userInfo, pw, bfr);
                        } else {
                            JOptionPane.showMessageDialog(messageFrame, "Error",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    frame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(messageFrame, "Please enter a message.",
                            "TextOGram", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.write("remove");
                pw.println();
                pw.flush();

                try {
                    openRemoveWindow(pw, bfr, messageHistoryArea, userInfo, frame);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                messageInputArea.setText("");
                messageFrame.setVisible(false);
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
                messageFrame.setVisible(false);

                showProfilePage(userInfo, pw, bfr);
            }
        });

        buttonPanel.add(sendButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(profileButton);

        inputPanel.add(buttonPanel, BorderLayout.SOUTH);
        messageFrame.add(inputPanel, BorderLayout.SOUTH);

        messageFrame.setVisible(true);
    }


    private static void sendMessage(PrintWriter pw, JTextArea messageArea) {
        String message = messageArea.getText();

        pw.println(message);
        messageArea.setText("");
    }

    private static void openRemoveWindow(PrintWriter pw, BufferedReader bfr, JTextArea messageHistoryArea, String[] userInfo, JFrame frame) throws IOException {
        JFrame removeFrame = new JFrame("Message Removal");
        removeFrame.setSize(400, 300);
        removeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        removeFrame.setLocationRelativeTo(null);
        removeFrame.setLayout(new BorderLayout());

        JPanel removePanel = new JPanel(new BorderLayout());

        DefaultListModel<String> removeListModel = new DefaultListModel<>();

        String[] allMessages = messageHistoryArea.getText().split("\n");
        ArrayList<String> userMessages = new ArrayList<>();

        for (String message : allMessages) {
            String[] eachMessage = message.split(": ");
            if (eachMessage[0].equals(userInfo[0])) {
                userMessages.add(message);
            }
        }

        for (String message : userMessages) {
            removeListModel.addElement(message.split(": ")[1]);
        }

        JList<String> removeList = new JList<>(removeListModel);

        removePanel.add(new JScrollPane(removeList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.write("delete");
                pw.println();
                pw.flush();

                String messageToRemove = removeList.getSelectedValue();
                if (messageToRemove != null) {
                    pw.write(messageToRemove);
                    pw.println();
                    pw.flush();

                    frame.setVisible(false);
                    removeFrame.setVisible(false);

                    try {
                        if (bfr.readLine().equals("yes")) {
                            JOptionPane.showMessageDialog(removeFrame, "Message Successfully Deleted!",
                                    "TextOGram", JOptionPane.PLAIN_MESSAGE);
                        } else if (bfr.readLine().equals("no")) {
                            JOptionPane.showMessageDialog(removeFrame, "Error",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (bfr.readLine().equals("null")) {
                            JOptionPane.showMessageDialog(removeFrame, "You have no message history. Go talk to this person more!",
                                    "TextOGram", JOptionPane.PLAIN_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    showProfilePage(userInfo, pw, bfr);
                } else {
                    JOptionPane.showMessageDialog(removeFrame, "Please select a message to delete.",
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
                removeFrame.setVisible(false);

                showProfilePage(userInfo, pw, bfr);
            }
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(profileButton);

        removeFrame.add(removePanel, BorderLayout.CENTER);
        removeFrame.add(buttonPanel, BorderLayout.SOUTH);

        removeFrame.setVisible(true);
    }

    private static void openViewWindow(String friendUsername, PrintWriter pw, BufferedReader bfr, JFrame frame, JPanel friendsPanel, String[] userInfo) {
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
        JButton profileButton = new JButton("Profile");

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

                viewFrame.setVisible(false);

                if (result.equals("yes")) {
                    JOptionPane.showMessageDialog(viewFrame, "Friend successfully removed!",
                            "TextOGram", JOptionPane.PLAIN_MESSAGE);
                    updateFrame(frame, users, friendsPanel);
                    showProfilePage(userInfo, pw, bfr);
                    frame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(viewFrame, "Sorry, there was an error!",
                            "TextOGram", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.write("profile");
                pw.println();
                pw.flush();

                viewFrame.setVisible(false);
                frame.setVisible(false);

                showProfilePage(userInfo, pw, bfr);
            }
        });

        buttonPanel.add(unfriendButton);
        buttonPanel.add(profileButton);

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

                        while (!fUser.equalsIgnoreCase("done")) {
                            fUser = bfr.readLine();
                            if (fUser.equalsIgnoreCase("done")) {
                                break;
                            }
                            if (!fUser.equalsIgnoreCase(userInfo[0])) {
                                users.add(fUser);
                            }
                        }
                        if (!users.get(0).equalsIgnoreCase("no")) {
                            frame.setVisible(false);
                            openFoundUsers(users, pw, bfr, userInfo);
                        } else {
                            JOptionPane.showMessageDialog(frame, "No users found.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            pw.println("search");
                        }
                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(usernameFrame, "An Error Occurred",
                                "Error", JOptionPane.ERROR_MESSAGE);
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

    public static boolean showLogInMessage(PrintWriter pw, BufferedReader bfr) {
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
                pw.println("no");
                loginFrame.dispose();
                createNewUserGUI(pw, bfr);
            }
        });

        returningUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    pw.println("yes");
                    enterUsername(pw, bfr);
                    loginFrame.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        loginFrame.setVisible(true);
        return true;
    }


    public static void createNewPassword(PrintWriter pw, BufferedReader bfr, String userInfo) {
        JFrame newPasswordFrame = new JFrame("Create New Password");
        newPasswordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newPasswordFrame.setSize(250, 150);
        newPasswordFrame.setLocationRelativeTo(null);
        newPasswordFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 1));
        JButton submitButton = new JButton("Submit");
        JTextField passwordOne = new JTextField("");
        JTextField passwordTwo = new JTextField("");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordOne.getText();
                String passwordNumTwo = passwordTwo.getText();
                if (!passwordNumTwo.equals(password)) {
                    JOptionPane.showMessageDialog(newPasswordFrame, "Passwords Do Not Match",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean valid = checkPassword(password);
                    if (valid) {
                        String[] userInfoSplit = userInfo.split(", ");
                        String[] userInformation = new String[6];
                        userInformation[0] = userInfoSplit[0];
                        userInformation[1] = userInfoSplit[1];
                        userInformation[2] = password;
                        ;
                        userInformation[3] = userInfoSplit[2];
                        userInformation[4] = userInfoSplit[3];
                        userInformation[5] = userInfoSplit[4];
                        pw.println(userInformation[0] + ", " + userInformation[1] + ", " + userInformation[2] + ", " +
                                userInformation[3] + ", " + userInformation[3] + ", " + userInformation[5]);
                        newPasswordFrame.dispose();
                        showProfilePage(userInformation, pw, bfr);
                    } else {
                        JOptionPane.showMessageDialog(newPasswordFrame, "Password Must Contain Atleast One Special " +
                                        "Character, Uppercase Letter, Lowercase Letter, and Number",
                                "Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        });
        panel.add(new JLabel("Enter Password:"));
        panel.add(passwordOne);
        panel.add(new JLabel("Re-Enter Password:"));
        panel.add(passwordTwo);
        newPasswordFrame.add(panel, BorderLayout.CENTER);
        newPasswordFrame.add(submitButton, BorderLayout.SOUTH);
        newPasswordFrame.setVisible(true);
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

    public static void enterUsername(PrintWriter pw, BufferedReader bfr) throws IOException {
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
                boolean redoIt = false;
                String username = usernameField.getText();
                pw.println(username);
                String valid = null;
                try {
                    valid = bfr.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (valid.equalsIgnoreCase("yes")) {
                    try {
                        String userInfo = bfr.readLine();
                        usernameFrame.dispose();
                        enterPassword(pw, bfr, userInfo);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(usernameFrame, "No Account Found with that Username",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(new JLabel("Enter your username, email, or phone number:"));
        panel.add(usernameField);
        panel.add(submitButton);

        usernameFrame.add(panel, BorderLayout.CENTER);
        usernameFrame.setVisible(true);
    }

    public static void enterPassword(PrintWriter pw, BufferedReader bfr, String userInfo) throws IOException {

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
                String[] userInfoSplit = userInfo.split(", ");
                if (userInfoSplit[2].equalsIgnoreCase(password)) {
                    passwordFrame.dispose();
                    showProfilePage(userInfoSplit, pw, bfr);
                } else {
                    JOptionPane.showMessageDialog(usernameFrame, "Incorrect Password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                // Perform action with the entered username
                // For example, send it to the server
                passwordField.setText("");
            }
        });

        panel.add(new JLabel("Enter your password:"));
        panel.add(passwordField);
        panel.add(submitButton);

        passwordFrame.add(panel, BorderLayout.CENTER);
        passwordFrame.setVisible(true);
    }

    public static void createNewUserGUI(PrintWriter pw, BufferedReader bfr) {
        JFrame newUserFrame = new JFrame("New User Registration");
        newUserFrame.setSize(400, 300);
        newUserFrame.setLocationRelativeTo(null);
        newUserFrame.setLayout(new BorderLayout());
        newUserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                if (usernameField.getText().isEmpty() || nameField.getText().isEmpty() ||
                        emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                        birthdayField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(newUserFrame, "One or More Field is Blank",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean badBirthday = false;
                    try {
                        String[] birthday = birthdayField.getText().split("-");
                        int month = Integer.parseInt(birthday[0]);
                        int day = Integer.parseInt(birthday[1]);
                        int year = Integer.parseInt(birthday[2]);
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(newUserFrame, "Birthday is Not Valid and/or in the " +
                                        "Correct Format",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        badBirthday = true;
                    }
                    if (!badBirthday) {
                        String newUserInfo = String.format("%s, %s, %s, %s, %s",
                                usernameField.getText(), nameField.getText(),
                                emailField.getText(), phoneField.getText(), birthdayField.getText());

                        pw.println(newUserInfo);
                        String valid = null;
                        try {
                            valid = bfr.readLine();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (valid.equalsIgnoreCase("yes")) {
                            newUserFrame.dispose();
                            createNewPassword(pw, bfr, newUserInfo);
                        } else if (valid.equalsIgnoreCase("username")) {
                            JOptionPane.showMessageDialog(newUserFrame, "That Username is already Taken",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
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
                pw.println("signout");
                frame.dispose();

                JOptionPane.showMessageDialog(frame, "Have a nice day!",
                        "TextOGram", JOptionPane.PLAIN_MESSAGE);

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
