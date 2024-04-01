import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
/**
 * runlocaltestprofile
 *
 * <p>
 * Creates an object that examine the profile
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
public class RunLocalTestProfile {
    public void testProfile() {
        Profile profile = new Profile();
        User user1 = new User("user1", "user1@example.com", "123456", "John Doe", "1980-01-01", "1234567890");
        User user2 = new User("user2", "user2@example.com", "abcdef", "Jane Smith", "1990-05-15", "9876543210");
        profile.addUser(user1);
        profile.addUser(user2);
        Assert.assertEquals("user1@example.com", profile.getEmailByUsername("user1"));
        Assert.assertEquals("Jane Smith", profile.getNameByUsername("user2"));
        Assert.assertEquals("1234567890", profile.getPhoneNumberByUsername("user1"));
        Assert.assertEquals("1990-05-15", profile.getBirthdayByUsername("user2"));
        Assert.assertEquals("abcdef", profile.getPasswordByUsername("user2"));
        Assert.assertNull(profile.getEmailByUsername("nonexisting"));
        Assert.assertNull(profile.getNameByUsername("nonexisting"));
        Assert.assertNull(profile.getPhoneNumberByUsername("nonexisting"));
        Assert.assertNull(profile.getBirthdayByUsername("nonexisting"));
        Assert.assertNull(profile.getPasswordByUsername("nonexisting"));
    }
}
