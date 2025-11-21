import org.example.Message;
import org.example.Message.MessageEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageLength() {
        String shortMessage = "This is a short message.";
        String longMessage = new String(new char[260]).replace("\0", "a"); // 260 characters

        assertEquals("Message ready to be sent.", Message.checkMessageLength(shortMessage), "Expected short message to be considered");
        assertEquals("Message exceeds 250 characters by 10, please reduce size", Message.checkMessageLength(longMessage));
    }

    @Test
    public void testCheckRecipientCell() {
        String validCell = "+27838968976";
        String invalidCell = "0838968976";

        final String VALID_RESPONSE = "Cellphone number successfully added.";
        assertEquals(VALID_RESPONSE, Message.checkCellPhoneNumber(validCell));
        assertEquals("Cellphone number incorrectly formatted or does not contain international code.", Message.checkCellPhoneNumber(invalidCell));
    }

    @Test
    public void testCreateMessageHash() {
        String messageID = "0012345678";
        String messageContent = "Hi Mike, can you join us for dinner tonight?";
        String expectedHash = "00:1:HITONIGHT";
        assertEquals(expectedHash, Message.createMessagehash(messageID, messageContent));
    }

    @Test
    public void testGenerateRandomMessageID() {
        String id = Message.generateRandomMessageID();

        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}")); // Digits only
    }

    @Test
    public void testMessageSentAction() {
        assertEquals("Message successfully sent.", Message.messageSentAction("Send Message"));
        assertEquals("Press 0 to delete message", Message.messageSentAction("Disregarded Message"));
        assertEquals("Message successfully stored.", Message.messageSentAction("Store Message"));
    }

    @BeforeEach
    public void setupTestData() {
        // Reset counts first
        Message.sentCount = 0;
        Message.storedCount = 0;
        Message.disregardedCount = 0;

        //Populate the arrays with test data
        Message.populateTestData();
    }

    @Test
    public void testSentMessagesArray() {
        assertEquals(2, Message.sentCount, "Sent messages array should contain 2 messages.");
        assertEquals("Did you get the cake?", Message.sentMessages[0].getMessage());
        assertEquals("It is dinner time!", Message.sentMessages[1].getMessage());
    }

    @Test
    public void testStoredMessageArray() {
        assertEquals(2, Message.storedCount, "Stored messages array should contain 2 messages.");
        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.storedMessages[0].getMessage());
        assertEquals("Ok, I am leaving without you.", Message.storedMessages[1].getMessage());
    }

    @Test
    public void testDisregardedMessagesArray() {
        assertEquals(1, Message.disregardedCount, "Disregarded messages array should contain 1 message.");
        assertEquals("Yohoooo, I am at your gate.", Message.disregardedMessages[0].getMessage());
    }

    @Test
    public void testDisplayLongestMessage() {
        //The longest message should be the stored message 2
        MessageEntry longestMessage = Message.storedMessages[0];

        for (int i = 1; i < Message.storedCount; i++) {
            if (Message.storedMessages[i].getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = Message.storedMessages[i];
            }
        }
        assertEquals("Where are you? You are late! I have asked you to be on time.", longestMessage.getMessage());

    }

    @Test
    public void testSearchByMessageID() {
        //Test search for a sent message
        String searchID = "0838884567";
        boolean found = false;

        for (int i = 0; i < Message.sentCount; i++) {
            if (Message.sentMessages[i].getMessageID().equals(searchID)) {
                found = true;
                assertEquals("It is dinner time!", Message.sentMessages[i].getMessage());
                break;
            }
        }
        assertTrue(found, "MessageID should be found in sent messages.");
    }

    @Test
    public void testDeleteMessageByHash() {
        //Delete stored message 2
        String hashToDelete = Message.storedMessages[0].getHash();
        Message.deleteByHash(hashToDelete);

        //Check that the message was removed
        for (int i = 0; i < Message.storedCount; i++) {
            assertNotEquals(hashToDelete, Message.storedMessages[i].getHash());
        }
    }

    @Test
    public void testDisplayReport() {
        //Display report should include all sent messages
        //Here we check that the sent messages count and messages themselves are correct
        assertEquals(2, Message.sentCount);

        //Check details of the first message
        assertNotNull(Message.sentMessages[0].getHash());
        assertEquals("+27834557896", Message.sentMessages[0].getRecipient());
        assertEquals("Did you get the cake?", Message.sentMessages[0].getMessage());

        //Check details of the second message
        assertNotNull(Message.sentMessages[1].getHash());
        assertEquals("+27834434567", Message.sentMessages[1].getRecipient());
        assertEquals("It is dinner time!", Message.sentMessages[1].getMessage());

    }

    @Test
    public void testSearchByRecipient() {
        //Example: Search all messages for +27838884567
        String recipient = "+27838884567";
        StringBuilder messagesFound = new StringBuilder();

        for (int i = 0; i < Message.storedCount; i++) {
            if (Message.storedMessages[i].getRecipient().equals(recipient)) {
                messagesFound.append(Message.storedMessages[i].getMessage()).append(";");
            }
        }

        assertTrue(messagesFound.toString().contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(messagesFound.toString().contains("Ok, I am leaving without you."));
    }
}



