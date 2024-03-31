import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

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
    public static class TestCase {
        @Test(timeout = 1000)
        public void authenticateTest() {
            User[] users = new User[10];
            users[0] = new User("Rvelma", "Password1!");
            Login login = new Login(users);

            assertTrue(login.authenticate("Rvelma", "Password1!"));
            assertTrue(login.authenticate("rvelma", "Password1!"));
            assertFalse(login.authenticate("rvelma", "Password1"));
            assertFalse(login.authenticate("rvelma", "password1"));
            assertFalse(login.authenticate("Velma", "Password1!"));
        }

    }
}