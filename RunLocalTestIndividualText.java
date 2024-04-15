import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

/**
 * RunLocalTestIndividualText
 * <p>
 * Runs a test on the IndividualText class
 *
 * @author Andrew Weiland, lab section 15
 * @version March 31, 2024
 */
@RunWith(Enclosed.class)
public class RunLocalTestIndividualText {
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
        public void individualTextEqualsTest() {
            Object obj = new Object();
            User user1 = new User("CR7", "I-am-the-Goat7");
            User user2 = new User("Leo-Messii10", "Wheres-your-world-cup?");
            IndividualText individualText1 = new IndividualText(user1, "SUIIIIIIIIII");
            IndividualText individualText2 = new IndividualText(user2, "Encara Messi. Goal goal goal goal");
            Assert.assertFalse("Ensure that an object that is not of the IndividualText class is not" +
                    "classified as equal to one that is.", individualText1.equals(obj));
            Assert.assertFalse("Ensure two different messages are not classified as equal.",
                    individualText1.equals(individualText2));
            Assert.assertTrue("Ensure the same message is classified as equal.",
                    individualText1.equals(individualText1));
        }
    }
}
