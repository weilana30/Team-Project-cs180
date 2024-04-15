import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * RunLocalTestClient
 * <p>
 * Runs a test on the methods in client
 *
 * @author Andrew Weiland, lab section 15
 * @version April 15, 2024
 */
@RunWith(Enclosed.class)
public class RunLocalTestClient {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCases.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    /**
     * TestCases
     * <p>* Has tests for the method in IndividualText
     *
     * @author Andrew Weiland, lab section 15
     * @version March 31, 2024
     */
    public static class TestCases {
        @Test(timeout = 1000)
        public void createNewPasswordTest() {
            provideInput("Me!1\nMe!1\n");
            String password = Client.createNewPassword();
            Assert.assertEquals(password, "Me!1");
        }

        @Test(timeout = 1000)
        public void createNewUsernameTest() {
            provideInput("AndrewMan!\nandyman@gmail.com\n1234567890\n02-24-2006\nJohn Joe\n");
            String userString = Client.createNewUsername();
            Assert.assertEquals(userString, "AndrewMan!, John Joe, andyman@gmail.com, 1234567890, 02-24-2006");
        }

        @Test(timeout = 1000)
        public void loginMessageTest() {
            provideInput("yes\n");
            Boolean yes = Client.showLogInMessage();
            Assert.assertEquals(yes, true);
            provideInput("no\n");
            Boolean no = Client.showLogInMessage();
            Assert.assertEquals(no, false);
        }

        @Test(timeout = 1000)
        public void checkPasswordTest() {
            Boolean yes = Client.checkPassword("Password123!");
            Assert.assertEquals(yes, true);
            Boolean no = Client.checkPassword("p");
            Assert.assertEquals(no, false);
        }

        @Test(timeout = 1000)
        public void checkEnterUsername() throws IOException {
            provideInput("andrew123\n");
            String username = Client.enterUsername();
            Assert.assertEquals(username, "andrew123");
        }

        @Test(timeout = 1000)
        public void checkEnterPassword() throws IOException {
            provideInput("Password123!\n");
            String password = Client.enterPassword();
            Assert.assertEquals(password, "Password123!");
        }
    }

    static void provideInput(String string) {
        ByteArrayInputStream in = new ByteArrayInputStream(string.getBytes());
        System.setIn(in);
    }
}
