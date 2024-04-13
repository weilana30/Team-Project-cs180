import java.util.Scanner;

/**
 * search
 *
 * <p>
 * Create a way to find a user
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
public class ProfileSearch {
    private Profile profile;
    private ProfileViewerInterface profileViewer;
    private Friends friends;
    private User user;


    // Constructor to initialize Profile and ProfileViewerInterface objects
    public ProfileSearch(Profile profile, ProfileViewerInterface profileViewer) {
        this.profile = profile;
        this.profileViewer = profileViewer;
    }

    // Method to perform user search
    public void search() {
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
                                friends.addFriend(searchValue, user.getUsername());
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

    // Main method to create a ProfileSearch instance and initiate a search
    public static void main(String[] args) {
        Profile profile = new Profile();
        ProfileSearch profileSearch = new ProfileSearch(profile, new ProfileViewer(profile));
        profileSearch.search();
    }
}
