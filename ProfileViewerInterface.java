/**
 * ProfileViewerInterface
 *
 * <p>
 * Create a way to find a user
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
public interface ProfileViewerInterface {
    String displayUserInformationByUsername(String username);

    String displayUserInformationByPhoneNumber(String phoneNumber);

    String displayUserInformationByEmail(String email);

    String displayUserInformation(User user);
}
