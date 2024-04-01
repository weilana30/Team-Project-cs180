import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

@RunWith(JUnit4.class)
public class RunLocalTestFriends {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        System.out.printf("Test Count: %d.\n", result.getRunCount());
        if (result.wasSuccessful()) {
            System.out.printf("Excellent - all local tests ran successfully.\n");
        } else {
            System.out.printf("Tests failed: %d.\n", result.getFailureCount());
            for (Failure failure: result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {

        @Test
        public void testAddFriend() {
            Profile profiles = new Profile();
            User user1 = new User("user1", "12345");
            User user2 = new User("user2", "54321");
            profiles.addUser(user1);
            profiles.addUser(user2);

            Friends friends = new Friends(profiles);

            //adding friend (true)
            boolean result1 = friends.addFriend("user2", "user1");
            Assert.assertTrue(result1);

            //adding same friend (false)
            boolean result2 = friends.addFriend("user2", "user1");
            Assert.assertFalse(result2);

            //adding non existent friend (false)
            boolean result3 = friends.addFriend("user3", "user1");
            Assert.assertFalse(result3);
        }

        @Test
        public void testRemoveFriend() {
            Profile profiles = new Profile();
            User user1 = new User("user1", "12345");
            User user2 = new User("user2", "54321");
            profiles.addUser(user1);
            profiles.addUser(user2);

            Friends friends = new Friends(profiles);
            friends.addFriend("user2", "user1");

            //remove friend (true)
            boolean result1 = friends.removeFriend("user2");
            Assert.assertTrue(result1);

            //remove non existent friend (False)
            boolean result2 = friends.removeFriend("user2");
            Assert.assertFalse(result2);
        }

        @Test
        public void testBlockUser() {
            Profile profiles = new Profile();
            User user1 = new User("user1", "12345");
            User user2 = new User("user2", "54321");
            profiles.addUser(user1);
            profiles.addUser(user2);

            Friends friends = new Friends(profiles);
            friends.addFriend("user2", "user1");

            // blocking friend test (true)
            boolean result1 = friends.blockUser("user2", "user1");
            Assert.assertTrue(result1);

            // blocking same user (False)
            boolean result2 = friends.blockUser("user2", "user1");
            Assert.assertFalse(result2); // Should return false

            // block user that doesn't exist (False)
            boolean result3 = friends.blockUser("user3", "user1");
            Assert.assertFalse(result3);
        }
    }
}
