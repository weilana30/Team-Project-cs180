import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * RunLocalTestSignup.java
 * <p>
 * A test case file that will run different test cases
 * on Signup.java and its methods.
 *
 * @author Rishi Velma, lab section 15
 * @version April 1st, 2024
 */

@RunWith(Enclosed.class)
public class RunLocalTestSignup {
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
     * test cases for Signup.java.
     *
     * @author Rishi Velma, lab section 15
     * @version April 1st, 2024
     */

    public static class TestCase {
        @Test(timeout = 1000)
        public void createUserTest() {
            User[] users = new User[10];
            Signup signup = new Signup(users);

            assertTrue(signup.createUser("testUser", "Password123!"));
            assertEquals("testUser", users[0].getUsername());
            assertEquals("Password123!", users[0].getPassword());
        }

        @Test(timeout = 1000)
        public void createUserTest2() {
            User[] users = new User[10];
            users[0] = new User("rvelma", "pAssword1!");
            users[1] = new User("theVelma", "Password2?");
            users[2] = new User("Velmanator", "passworD3/");
            Signup signup = new Signup(users);

            assertTrue(signup.createUser("testUser", "Password123!"));
            assertEquals("testUser", users[3].getUsername());
            assertEquals("Password123!", users[3].getPassword());
        }

        @Test(timeout = 1000)
        public void checkUsernameTest() {
            User[] users = new User[10];
            users[0] = new User("Rvelma", "Password1234!");
            Signup signup = new Signup(users);

            assertTrue(signup.checkUsername("test"));
            assertFalse(signup.checkUsername("Rvelma"));
        }

        @Test(timeout = 1000)
        public void checkPasswordTest() {
            Signup signup = new Signup(new User[10]);

            assertTrue(signup.checkPassword("Password123!"));
            assertFalse(signup.checkPassword("password123?"));
            assertFalse(signup.checkPassword("PASSWORD123&"));
            assertFalse(signup.checkPassword("Password/"));
            assertFalse(signup.checkPassword("Password123"));
        }
    }
}