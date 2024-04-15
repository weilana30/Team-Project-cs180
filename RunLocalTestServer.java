import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
/**
 * RunLocalTestServer
 * <p>
 * Test for Server. Server must be running for this test to work
 *
 * @author Andrew Weiland, lab section 15
 * @version April 15, 2024
 */
@RunWith(JUnit4.class)
public class RunLocalTestServer {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(RunLocalTestServer.class);
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
    public void testConstructor() {
        try {
            Socket socket = new Socket("localhost", 1234);
            Server server = new Server(socket);
            assertNotNull(server);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}
