import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RunLocalTestFriends {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(RunLocalTestFriends.class);
        System.out.printf("Test Count: %d.\n", result.getRunCount());
        if (result.wasSuccessful()) {
            System.out.printf("Excellent - all local tests ran successfully.\n");
        } else {
            System.out.printf("Tests failed: %d.\n", result.getFailureCount());
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }


    @Test
    public void testAddFriend() {
        Profile profiles = new Profile();
        User user1 = new User("user1", "12345");
        User user2 = new User("user2", "54321");
        profiles.addUser(user1);
        profiles.addUser(user2);

        Friends friends = new Friends(profiles);

        // Adding a friend
        boolean result1 = friends.addFriend("user2", "user1");
        Assert.assertTrue(result1);

        // Adding the same friend again (should fail)
        boolean result2 = friends.addFriend("user2", "user1");
        Assert.assertFalse(result2);

        // Adding a non-existent friend (should fail)
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

        // Removing a friend
        boolean result1 = friends.removeFriend("user2", "user1");
        Assert.assertTrue(result1);

        // Removing a non-existent friend (should fail)
        boolean result2 = friends.removeFriend("user2", "user1");
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

        // Blocking a friend
        boolean result1 = friends.blockUser("user2", "user1");
        Assert.assertTrue(result1);

        // Blocking the same user again (should fail)
        boolean result2 = friends.blockUser("user2", "user1");
        Assert.assertFalse(result2);

        // Blocking a non-existent user (should fail)
        boolean result3 = friends.blockUser("user3", "user1");
        Assert.assertFalse(result3);
    }
}
