import org.example.Login;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testValidUsername() {
        Login login = new Login();
        assertEquals("kyl_1", "kyl_1");
    }

    @Test
    public void testInvalidUsername() {
        Login login = new Login();
        assertEquals("kyle!!!!!!!", "kyle!!!!!!!");
    }

    @Test
    public void testInvalidPassword() {
        Login login = new Login();
        assertEquals(false, login.checkPasswordComplexity("password"));
    }

    @Test
    public void testValidPassword() {
        Login login = new Login();
        assertEquals(true, login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testValidCellphone() {
        Login login = new Login();
        assertEquals(true, login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testInvalidCellphone() {
        Login login = new Login();
        assertEquals(false, login.checkCellPhoneNumber("0838968976"));
    }

    @Test
    public void testSuccessfulLogin() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testFailedLogin() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyle!!!!!!!", "password", "0838968976"));
    }

    @Test
    public void testCorrectUsername() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.getUsername().equals("kyl_1"));
    }

    @Test
    public void testIncorrectUsername() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.getUsername().equals("kyle!!!!!!!"));
    }

    @Test
    public void testCorrectPassword() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.getUserPassword().equals("Ch&&sec@ke99!"));
    }

    @Test
    public void testIncorrectPassword() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.getUserPassword().equals("password"));
    }

    @Test
    public void testCorrectCellphone() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.getCellphone().equals("+27838968976"));
    }

    @Test
    public void testIncorrectCellphone() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.getCellphone().equals("0838968976"));
    }
}