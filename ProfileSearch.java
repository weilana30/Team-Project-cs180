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
    public ProfileSearch(Profile profile, ProfileViewerInterface profileViewer) {
        this.profile = profile;
        this.profileViewer = profileViewer;
    }
    public void search() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.print("Do you want to find a user? (yes/no): ");
            choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("yes")) {
                System.out.print("Choose search criteria (username/phone number/email): ");
                String searchCriteria = scanner.nextLine().trim().toLowerCase();
                System.out.print("Enter the value to search for: ");
                String searchValue = scanner.nextLine().trim();
                switch (searchCriteria) {
                    case "username":
                        System.out.println(profileViewer.displayUserInformationByUsername(searchValue));
                        break;
                    case "phone number":
                        System.out.println(profileViewer.displayUserInformationByPhoneNumber(searchValue));
                        break;
                    case "email":
                        System.out.println(profileViewer.displayUserInformationByEmail(searchValue));
                        break;
                    default:
                        System.out.println("Invalid search criteria!");
                }
            }
        } while (choice.equals("yes"));
        System.out.println("Exiting search.");
    }
    public static void main(String[] args) {
        Profile profile = new Profile();
        ProfileSearch profileSearch = new ProfileSearch(profile, new ProfileViewer(profile));
        profileSearch.search();
    }
}
