import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
/**
 * RunLocalTestDirectMessage
 * <p>
 * Runs a test on the directMessage class and its methods
 *
 * @author Andrew Weiland, lab section 15
 * @version March 31, 2024
 */
@RunWith(Enclosed.class)
public class RunLocalTestDirectMessage {
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
     * Has several tests for the methods in DirectMessage
     *
     * @author Andrew Weiland, lab section 15
     * @version March 31, 2024
     */
    public static class TestCase {
        @Test(timeout = 1000)
        public void createNewMessageTest() {
            User user1 = new User("AndyMan_18", "MonkeyBananas6");
            User user2 = new User("The-Salty-Salzmann46", "LetsGoBoilers!@");
            DirectMessage message = new DirectMessage(user1, user2);
            User[] users = message.getUsers();
            Assert.assertArrayEquals("Make sure the user array is correct.", users, new User[]{user1, user2});
        }

        @Test(timeout = 1000)
        public void addMessageTest() {
            User user1 = new User("AndyMan_18", "MonkeyBananas6");
            User user2 = new User("The-Salty-Salzmann46", "LetsGoBoilers!@");
            DirectMessage message = new DirectMessage(user1, user2);
            String textMessage = "Hello Andrew, how was you day today?";
            boolean adds = message.addMessage(user2, textMessage);
            Assert.assertTrue("Ensure that it the addMessage method does not fail.", adds);
            Assert.assertEquals("Ensure that when you add a message it is being added correctly",
                    user2.getUsername() + ":   " + textMessage + "\n", message.toString());
            String textMessage2 = "It was excellent. I went to the mall with my mom!!";
            message.addMessage(user1, textMessage2);
            Assert.assertEquals("Ensure that it adds a new message correctly even if you already have a message in it.",
                    user2.getUsername() + ":   " + textMessage + "\n" + user1.getUsername() + ":   "
                            + textMessage2 + "\n", message.toString());
        }

        @Test(timeout = 1000)
        public void deleteMessageTest() {
            User user1 = new User( "AndyMan_18", "MonkeyBananas6");
            User user2 = new User("The-Salty-Salzmann46", "LetsGoBoilers!@");
            DirectMessage message = new DirectMessage(user1, user2);
            String textMessage = "Hello Andrew, how was you day today?";
            message.addMessage(user2, textMessage);
            IndividualText messageToDelete = message.getMessages().get(0);
            boolean removeFalse = message.deleteMessage(messageToDelete, user1);
            boolean removeTrue = message.deleteMessage(messageToDelete, user2);
            Assert.assertFalse("Ensure that the message is not deleted if the wrong user tries to delete it.",
                    removeFalse);
            Assert.assertTrue("Ensure that the message is properly deleted", removeTrue);
            Assert.assertEquals("Ensure the message is properly deleted.", "", message.toString());
        }
        @Test(timeout = 1000)
        public void updateFileTest() {
            User user1 = new User("AndyMan_18", "MonkeyBananas6");
            User user2 = new User("The-Salty-Salzmann46", "LetsGoBoilers!@");
            DirectMessage message = new DirectMessage(user1, user2);
            String textMessage = "Hello Andrew, how was you day today?";
            boolean adds = message.addMessage(user2, textMessage);
            String textMessage2 = "It was excellent. I went to the mall with my mom!!";
            message.addMessage(user1, textMessage2);
            boolean update = message.updateFile();
            System.out.println(update);
            Assert.assertTrue("Ensure that it updates the file correctly", update);
        }
    }
}


