import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * RunLocalTestLogin.java
 * <p>
 * A test case file that will run different test cases
 * on Login.java and its methods.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */

@RunWith(Enclosed.class)
public class RunLocalTestLogin {
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
     * test cases for Login.java.
     *
     * @author Rishi Velma, lab section 15
     * @version April 1st, 2024
     */

    public static class TestCase {
        @Test(timeout = 1000)
        public void authenticateUsernameTest() {
            User[] users = new User[10];
            users[0] = new User("Rvelma", "Password1!");
            Login login = new Login(users);

            assertTrue(login.authenticateUsername("Rvelma", "Password1!"));
            assertTrue(login.authenticateUsername("rvelma", "Password1!"));
            assertFalse(login.authenticateUsername("rvelma", "Password1"));
            assertFalse(login.authenticateUsername("rvelma", "password1"));
            assertFalse(login.authenticateUsername("Velma", "Password1!"));
        }

        @Test(timeout = 1000)
        public void authenticateEmailTest() {
            User[] users = new User[10];
            users[0] = new User("Rvelma", "Password1!");
            users[0].setEmail("rvelma@purdue.edu");
            Login login = new Login(users);

            assertTrue(login.authenticateEmail("Rvelma@purdue.edu", "Password1!"));
            assertTrue(login.authenticateEmail("rvelma@purdue.edu", "Password1!"));
            assertFalse(login.authenticateEmail("3303960658", "Password1!"));
        }

        @Test(timeout = 1000)
        public void authenticatePhoneTest() {
            User[] users = new User[10];
            users[0] = new User("Rvelma", "Password1!");
            users[0].setPhoneNumber("3303960658");
            Login login = new Login(users);

            assertTrue(login.authenticatePhone("330-396-0658", "Password1!"));
            assertTrue(login.authenticatePhone("3303960658", "Password1!"));
            assertTrue(login.authenticatePhone("330 396 0658", "Password1!"));
            assertFalse(login.authenticatePhone("rvelma@purdue.edu", "Password1!"));
        }
    }
}