 public static void searchUsers(PrintWriter pw, BufferedReader bfr, InputStream is) throws IOException {
        boolean validResponse = false;
        Scanner scan = new Scanner(System.in);
        String response;
        do {
            System.out.println("Would you like to search for a user? yes or no");
            response = scan.nextLine();
            if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
                System.out.println("Not a valid response");
            } else {
                validResponse = true;
            }
        } while (!validResponse);
        if (response.equalsIgnoreCase("yes")) {
            pw.println(response);
            boolean search = true;
            do {
                System.out.println("Please enter the user you are searching for?");
                String userToSearch = scan.nextLine();
                System.out.println(userToSearch);
                
                //sends the server the name they are searching for
                pw.println(userToSearch);

                ArrayList<String> users = new ArrayList<>();
                while (is.available() > 0) {
                    String username = bfr.readLine();
                    users.add(username);
                }
                if (users.isEmpty()) {
                    boolean again = false;
                    do {
                        System.out.println("There were no results.\n" +
                                "If you want to search again, type search. If you want to go back to your profile, type profile.");
                        String noResultsResponse = scan.nextLine();
                        if (noResultsResponse.equalsIgnoreCase("profile")) {
                            search = false;
                        } else if (!noResultsResponse.equalsIgnoreCase("search")) {
                            System.out.println("Not a valid response");
                            again = true;
                        }
                    } while (again);
                } else {
                    for (String username : users) {
                        System.out.println(username);
                    }
                    boolean validUsername = false;
                    do {
                        System.out.println("If you want to view one of these users profiles enter their username.\n " +
                                "If you want to search again, type search. If you want to go back to your profile, type profile.");
                        String nextResponse = scan.nextLine();
                        if (nextResponse.equalsIgnoreCase("profile")) {
                            search = false;
                            validUsername = true;
                        }
                        if (!nextResponse.equalsIgnoreCase("search")) {
                            //sends the username to open the profile
                            pw.println(response);
                            //should send back the users string
                            String profile = bfr.readLine();
                            if (profile.equalsIgnoreCase("no")) {
                                System.out.println("That username is not valid");
                            } else {
                                validUsername = true;
                                String[] info = profile.split(", ");
                                showProfilePage(info);
                                System.out.println("Would you like to add the user as a friend or block them? Enter add " +
                                        "or block, or entire profile to return to the profile");
                                String friend = scan.nextLine();
                                if (friend.equalsIgnoreCase("block")) {
                                    //blocks the user
                                    pw.println("block");
                                } else if (friend.equalsIgnoreCase("add")) {
                                    //adds the user as a friend
                                    pw.println("add");
                                } else if (friend.equalsIgnoreCase("profile")) {
                                    search = false;
                                }

                            }
                        }
                    } while(!validUsername);
                    } while(!validResponse);
            } while (search);
        }
    }
