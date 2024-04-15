import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * RunLocalTestProfileViewer.java
 * <p>
 * A test case file that will run different test cases
 * on ProfileViewer.java and its methods.
 *
 * @author Chenjia Liu, lab section 15
 * @version April 1st, 2024
 */

@RunWith(Enclosed.class)
public class RunLocalTestProfileViewer {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    /**
     * TestCase
     * <p>
     * The test case class that contains all the different
     * test cases for ProfileViewer.java.
     *
     * @author Chenjia Liu, lab section 15
     * @version April 1st, 2024
     */

    public static class TestCase {
        // Create a Profile object
        private Profile profile = new Profile();

        // Create a ProfileViewer object with the Profile
        private ProfileViewer profileViewer = new ProfileViewer(profile);

        @Test(timeout = 1000)
        public void displayUserInformationTest() {
            User user1 = new User("alice, Alice, password1, alice@example.com, 1234567890, 1995-05-10");
            User user2 = new User("bob, Bob, password2, bob@example.com, 0987654321, 1990-12-25");

            profile.addUser(user1);
            profile.addUser(user2);

            String expectedUser1Info = "Username: alice\n" +
                    "Email: alice@example.com\n" +
                    "Phone Number: 1234567890\n" +
                    "Birthday: 1995-05-10\n";
            assertEquals(expectedUser1Info, profileViewer.displayUserInformationByUsername("alice"));
            assertEquals(expectedUser1Info, profileViewer.displayUserInformationByPhoneNumber("1234567890"));
            assertEquals(expectedUser1Info, profileViewer.displayUserInformationByEmail("alice@example.com"));

            String expectedUser2Info = "Username: bob\n" +
                    "Email: bob@example.com\n" +
                    "Phone Number: 0987654321\n" +
                    "Birthday: 1990-12-25\n";
            assertEquals(expectedUser2Info, profileViewer.displayUserInformationByUsername("bob"));
            assertEquals(expectedUser2Info, profileViewer.displayUserInformationByPhoneNumber("0987654321"));
            assertEquals(expectedUser2Info, profileViewer.displayUserInformationByEmail("bob@example.com"));

            assertEquals("User not found!", profileViewer.displayUserInformationByUsername("charlie"));
            assertEquals("User not found!", profileViewer.displayUserInformationByPhoneNumber("1111111111"));
            assertEquals("User not found!", profileViewer.displayUserInformationByEmail("dave@example.com"));
        }
    }
}
