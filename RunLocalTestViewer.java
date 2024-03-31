import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RunLocalTestViewer {

    @Test
    public void testDisplayUserInformation() {
        Profile profile = new Profile();

        User user1 = new User("user1", "user1@example.com", "123456", "John Doe", "1980-01-01", "1234567890");
        User user2 = new User("user2", "user2@example.com", "abcdef", "Jane Smith", "1990-05-15", "9876543210");

        profile.addUser(user1);
        profile.addUser(user2);

        Viewer viewer = new Viewer(profile);

        String expectedUserInfo1 = "Name: John Doe\nUsername: user1\nEmail: user1@example.com\nPhone Number: 1234567890\nBirthday: 1980-01-01\n";
        Assert.assertEquals(expectedUserInfo1, viewer.displayUserInformation("user1"));

        String expectedUserInfo2 = "Name: Jane Smith\nUsername: user2\nEmail: user2@example.com\nPhone Number: 9876543210\nBirthday: 1990-05-15\n";
        Assert.assertEquals(expectedUserInfo2, viewer.displayUserInformation("user2"));

        String expectedUserNotFound = "User not found!";
        Assert.assertEquals(expectedUserNotFound, viewer.displayUserInformation("nonexisting"));
    }
}
