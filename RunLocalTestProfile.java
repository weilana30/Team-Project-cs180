import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RunLocalTestProfile
 *
 * <p>
 * Creates an object that examine the profile
 *
 * @author Ricardo Liu, lab section 15
 * @version March 31, 2024
 */
public class RunLocalTestProfile {
    private Profile profile;

    @BeforeEach
    public void setUp() {
        try (BufferedWriter clearWriter = new BufferedWriter(new FileWriter("Users.txt"))) {
            clearWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Users.txt", true))) {
            writer.write("johnDoe, John Doe, password123, john.doe@example.com, 1234567890, 01/01/1990\n");
            writer.write("janeDoe, Jane Doe, pass456, jane.doe@example.com, 0987654321, 02/02/1992\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile = new Profile();
    }

    @AfterEach
    public void tearDown() {
        try (BufferedWriter clearWriter = new BufferedWriter(new FileWriter("Users.txt"))) {
            clearWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserByUsername() {
        User user = profile.getUserByUsername("johnDoe");
        assertNotNull(user);
        assertEquals("johnDoe", user.getUsername());
        assertEquals("John Doe", user.getName());
        assertEquals("password123", user.getPassword());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals("01/01/1990", user.getBirthday());
    }

    @Test
    public void testGetEmailByUsername() {
        String email = profile.getEmailByUsername("johnDoe");
        assertNotNull(email);
        assertEquals("john.doe@example.com", email);
    }

    @Test
    public void testGetPhoneNumberByUsername() {
        String phoneNumber = profile.getPhoneNumberByUsername("johnDoe");
        assertNotNull(phoneNumber);
        assertEquals("1234567890", phoneNumber);
    }

    @Test
    public void testGetBirthdayByUsername() {
        String birthday = profile.getBirthdayByUsername("johnDoe");
        assertNotNull(birthday);
        assertEquals("01/01/1990", birthday);
    }

    @Test
    public void testGetPasswordByUsername() {
        String password = profile.getPasswordByUsername("johnDoe");
        assertNotNull(password);
        assertEquals("password123", password);
    }

    @Test
    public void testGetNameByUsername() {
        String name = profile.getNameByUsername("johnDoe");
        assertNotNull(name);
        assertEquals("John Doe", name);
    }

    @Test
    public void testGetUsers() {
        ArrayList<User> users = profile.getUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUserByPhoneNumber() {
        User user = profile.getUserByPhoneNumber("1234567890");
        assertNotNull(user);
        assertEquals("johnDoe", user.getUsername());
    }

    @Test
    public void testGetUserByEmail() {
        User user = profile.getUserByEmail("john.doe@example.com");
        assertNotNull(user);
        assertEquals("johnDoe", user.getUsername());
    }

    @Test
    public void testAddUser() {
        User newUser = new User("johnSmith, John Smith, pass789, john.smith@example.com, 9876543210, 03/03/1995");
        profile.addUser(newUser);

        User addedUser = profile.getUserByUsername("johnSmith");
        assertNotNull(addedUser);
        assertEquals("johnSmith", addedUser.getUsername());
        assertEquals("John Smith", addedUser.getName());
        assertEquals("pass789", addedUser.getPassword());
        assertEquals("john.smith@example.com", addedUser.getEmail());
        assertEquals("9876543210", addedUser.getPhoneNumber());
        assertEquals("03/03/1995", addedUser.getBirthday());
    }
}
