/**
 * viewer
 *
 * <p>
 * Create a way to look at the information of a viewer
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
public class ProfileViewer implements ProfileViewerInterface {
    private Profile profile;

    public ProfileViewer(Profile profile) {
        this.profile = profile;
    }

    public String displayUserInformationByUsername(String username) {
        User user = profile.getUserByUsername(username);
        if (user != null) {
            return displayUserInformation(user);
        } else {
            return "User not found!";
        }
    }

    public String displayUserInformationByPhoneNumber(String phoneNumber) {
        User user = profile.getUserByPhoneNumber(phoneNumber);
        if (user != null) {
            return displayUserInformation(user);
        } else {
            return "User not found!";
        }
    }

    public String displayUserInformationByEmail(String email) {
        User user = profile.getUserByEmail(email);
        if (user != null) {
            return displayUserInformation(user);
        } else {
            return "User not found!";
        }
    }

    public String displayUserInformation(User user) {
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("Username: ").append(user.getUsername()).append("\n");
        userInfo.append("Email: ").append(user.getEmail()).append("\n");
        userInfo.append("Phone Number: ").append(user.getPhoneNumber()).append("\n");
        userInfo.append("Birthday: ").append(user.getBirthday()).append("\n");
        return userInfo.toString();
    }
}
