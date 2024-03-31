import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

@RunWith(Enclosed.class)
public class RunLocalTestUser {
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
    public static class TestCase {
        @Test(timeout = 1000)
        public void userTest() {
            User user1 = new User("tester", "Password1@");
            User user3 = new User("tester", "Password1@");
            User user4 = new User("test", "Password1@");

            assertTrue(user1.equals(user3));
            assertFalse(user1.equals(user4));
            assertEquals("Username: " + user4.getUsername() +
                    ", Password: " + user4.getPassword(), user4.toString());

        }
    }
}