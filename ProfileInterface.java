
/**
 * profileinterface
 *
 * <p>
 * Creates an object that restores the information of a user
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
public interface ProfileInterface {
    void addUser(User user);
    User getUserByUsername(String username);
    User getUserByPhoneNumber(String phoneNumber);
    User getUserByEmail(String email);
    String getEmailByUsername(String username);
    String getPhoneNumberByUsername(String username);
    String getBirthdayByUsername(String username);
    String getPasswordByUsername(String username);
    String getNameByUsername(String username);
}
