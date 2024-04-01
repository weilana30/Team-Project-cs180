import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
/**
 * runlocaltestsearch
 *
 * <p>
 * Creates an object that examine the search
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
@RunWith(JUnit4.class)
public class RunLocalTestProfileSearch {

    @Test
    public void testProfileSearch() {
        String simulatedInput = "yes\nusername\nuser1\nno\n";
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Profile profile = new Profile();
        User user1 = new User("user1", "user1@example.com", "123456", "John Doe", "1980-01-01", "1234567890");
        profile.addUser(user1);

        ProfileViewerInterface profileViewer = new Viewer(profile);

        ProfileSearch profileSearch = new ProfileSearch(profile, profileViewer);
        profileSearch.search();

        System.setIn(savedStandardInputStream);
        System.setOut(System.out);

        String expectedOutput = "Do you want to find a user? (yes/no): Choose search criteria (username/phone number/email): " +
                "Enter the value to search for: User: user1\nName: John Doe\nEmail: user1@example.com\nPhone Number: 1234567890\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }
}
