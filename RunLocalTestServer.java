import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.JUnit4;

import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void testConstructorWithClientSocket() {
        Socket testSocket = new Socket();
      
        Server server = new Server(testSocket);
        assertNotNull(server);
      
        assertEquals(testSocket, server.clientSocket);
    }
}
