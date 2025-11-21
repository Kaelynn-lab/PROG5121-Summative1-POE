package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JOptionPane;

public class Message {



    //Message entry class definition
    public static class MessageEntry {
        private String messageID;
        private String recipient;
        private String message;
        private String hash;

        public MessageEntry(String messageID, String recipient, String message, String hash) {
            this.messageID = messageID;
            this.recipient = recipient;
            this.message = message;
            this.hash = hash;
        }

        //Getters
        public String getMessageID() {return messageID;}
        public String getRecipient() {return recipient;}
        public String getMessage() {return message;}
        public String getHash() {return hash;}
    }

    //Part 3 Arrays
    public static MessageEntry[] sentMessages = new MessageEntry[200];
    public static MessageEntry[] disregardedMessages = new MessageEntry[200];
    public static MessageEntry[] storedMessages = new MessageEntry[200];
    public static String[] messageHashes = new String[200];
    public static String[] messageIDs = new String[200];

    //Counters for each message type
    public static int sentCount = 0;
    public static int disregardedCount = 0;
    public static int storedCount = 0;
    public static int hashCount = 0;
    public static int idCount = 0;

    /*These HashMaps are like dictionaries. You give them a "key" like an ID or hash)
     and they quickly give you back the full MessageEntry object. */
    public static HashMap<String, MessageEntry> messageidMap = new HashMap<>(); //Maps Message ID to MessageEntry object
    public static HashMap<String, MessageEntry> messageHashToEntryMap = new HashMap<>(); //Maps Message Hash to MessageEntry object

    public static int totalMessages = 0; //This counts all messages in the system
    private static int messageLimit = 0;
    private static int messagesSentCounterForHash = 0;
    private static int hashMessageCounter = 0;

    public static HashMap<String, String> userHashMap = new HashMap<>(); //Maps username to hash
    private static int messagesSentByUser = 0;

    public static void message(String[] args) {
        Scanner scanner = new Scanner(System.in);

        JOptionPane.showMessageDialog(null, "Welcome to QuickChat");

        // Ask user for message limit

        String limitInput = JOptionPane.showInputDialog("Enter the maximum number of messages to send: ");
        messageLimit = Integer.parseInt(limitInput);

        //Main QuickChat menu loop
        while (true) {
            String choice = JOptionPane.showInputDialog(
                    "Please select an option:\n" +
                            "1. Send Message\n" +
                            "2. Show recently sent messages\n" +
                            "3. Quit");



            if (choice == null) return;
            switch (choice) {
                case "1":
                    if (totalMessages >= messageLimit) {
                        JOptionPane.showMessageDialog(null, "Message limit reached. Cannot send more messages.");
                    } else {
                        sendMessage();
                    }
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, "Recently Sent Messages - Coming Soon");
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null,"Exiting QuickChat. Goodbye!");
                    return;
                default:
                    JOptionPane.showMessageDialog(null,"Invalid option. Please try again.");
                    break;
            }
        }
    }


    public static void sendMessage() {

        //Generate random 10-digit message ID
        String messageID = generateRandomMessageID();
        System.out.println("Generated Message ID: " + messageID);

        //Validate message ID
        if (!checkMessageID(messageID)) {
            System.out.println("Message ID is not correctly formatted. It should be exactly 10 digits.");
            return;
        }

        //Get recipient cellphone number

        String recipientCell = JOptionPane.showInputDialog("Enter recipient cellphone number (with country code: +27): ");

        if (!checkRecipientCellNumber(recipientCell)) {
            JOptionPane.showMessageDialog(null, "Cellphone number incorrectly formatted or does not contain international code.");
            return;
        }

        //Get message content
        String messageContent = JOptionPane.showInputDialog("Enter your message (max 250 characters): ");

        //Check message length
        if (messageContent.length() > 250) {
            JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
        }else {
            JOptionPane.showMessageDialog(null, "Message sent.");
        }

        messagesSentCounterForHash ++;

        //Generate message hash
        String messageHash = createMessagehash(messageID, messageContent);

        //Create full message format
        String fullMessage = String.format("MessageID: %s, Message Hash: %s, Recipient: %s, Message: %s", messageID, messageHash, recipientCell, messageContent);

        //Display full message details
        System.out.println("Full Message Details: " );
        System.out.println("MessageID: " + messageID);
        System.out.println("Recipient: " + recipientCell);
        System.out.println("Message: " + messageContent);
        System.out.println("Message Hash: " + messageHash);

        //Ask user whether to send, store or disregard the message
        String action = JOptionPane.showInputDialog(
                "Choose an action for your message:\n" +
                        "1. Send Message\n" +
                        "2. Disregard Message\n" +
                        "3. Store Message to send later");


        if (action == null) return;
        switch (action) {
            case "1":

                MessageEntry entry = new MessageEntry(messageID, recipientCell, messageContent, messageHash);
                sentMessages[sentCount++] = entry; //Add to sent messages array

                //Add to HashMaps for quick lookup
                messageidMap.put(messageID, entry);
                messageHashToEntryMap.put(messageHash, entry);


                totalMessages++; //Increment total messages in system

                //Save ID and hash to arrays
                messageIDs[idCount++] = messageID;
                messageHashes[hashCount++] = messageHash;

                JOptionPane.showMessageDialog(null,
                        "Message sent successfully." + "\n" +
                                "MessageID: " + messageID + "\n" +
                                "\nMessage Hash: " + messageHash + "\n" +
                                "\nRecipient: " + recipientCell + "\n" +
                                "\nMessage: " + messageContent);
                break;

            case "2":
                disregardedMessages[disregardedCount] = new MessageEntry(messageID, recipientCell, messageContent, messageHash);

                disregardedCount++; //Increment disregarded count

                JOptionPane.showMessageDialog(null, "Message disregarded.");
                break;

            case "3":
                storeMessageAsJSON(messageID, recipientCell, messageContent, messageHash);
                storedMessages[storedCount] = new MessageEntry(messageID, recipientCell, messageContent, messageHash);
                storedCount++; //Increment stored count

                messageHashes[hashCount++] = messageHash;
                messageIDs[idCount++] = messageID;

                JOptionPane.showMessageDialog(null, "Message stored in stored_messages.json.");

                System.out.println("Message stored for later:");
                System.out.println("MessageID: " + messageID);
                System.out.println("Recipient: " + recipientCell);
                System.out.println("Hash: " + messageHash);
                System.out.println("Message: " + messageContent);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Invalid action. Message not sent.");
                break;
        }
        System.out.println("Total messages sent: " + totalMessages);
    }






    public static String generateRandomMessageID() {

        String messageID = "";

        for (int i = 0; i < 10; i++) { //loop counter
            int digit = (int)(Math.random() * 10); //random digit generator
            messageID += digit;
        }


        if (messageidMap.containsKey(messageID)) {
            return generateRandomMessageID();
        }
        return messageID;
    }

    public static boolean checkMessageID(String messageID) {
        if (messageID == null) return false;
        return messageID.matches("^\\d{10}$");
    }

    public static boolean checkRecipientCellNumber(String cellNumber) {
        if (cellNumber == null) return false;
        if (cellNumber.length() != 12) return false;
        // South African international format regex
        String SA_International_Regex = "^\\+27[\\s.-]?\\d{2}[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
        return cellNumber.matches(SA_International_Regex);
    }

    public static String createMessagehash(String messageID, String messageContent) {
        if (messageID == null || messageContent == null) {
            return "";
        }

        // Get first two digits of message ID
        String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;

        // Clean and trim message content
        String trimmed = messageContent.trim().replaceAll("[^a-zA-Z0-9\\s]", "");

        hashMessageCounter++;

        int messageNumber = hashMessageCounter;

        // Find first word
        int firstSpace = trimmed.indexOf(' ');
        String firstWord = (firstSpace == -1) ? trimmed : trimmed.substring(0, firstSpace);
        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");

        // Find last word
        int lastSpace = trimmed.lastIndexOf(' ');
        String lastWord = (lastSpace == -1) ? trimmed : trimmed.substring(lastSpace + 1);
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");

        // Build final hash
        String messageHash = firstTwo + ":" + messageNumber + ":" +  firstWord.toUpperCase() + lastWord.toUpperCase();

        return messageHash;
    }

    public static String SentMessage() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an action for your message:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                return "Send Message";
            case 2:
                return "Disregarded Message";
            case 3:
                return "Store Message";
            default:
                return "Invalid";
        }
    }

    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recently Sent Messages:\n");
        for (int i = 0; i < sentCount; i++) {
            MessageEntry entry = sentMessages[i];
            sb.append(String.format("MessageID: %s, Recipient: %s, Message: %s, Hash: %s\n",
                    entry.getMessageID(), entry.getRecipient(), entry.getMessage(), entry.getHash()));
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public static String storeMessageAsJSON(String messageID, String recipient, String message, String hash) {
        //Convert message to JSON format
        // JSON generation assisted by ChatGPT
        String json = "{\n" +
                "  \"messageID\": \"" + messageID + "\",\n" +
                "  \"recipient\": \"" + recipient + "\",\n" +
                "  \"message\": \"" + message + "\",\n" +
                "  \"hash\": \"" + hash + "\"\n" +
                "}";

        try {
            FileWriter writer = new FileWriter("stored_messages.json", true);
            writer.write(json + "\n");
            writer.close();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message");
        }
        return json;
    }

    public static String checkMessageLength(String msg) {
        if (msg.length() > 250) {
            int excess = msg.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size";
        }else {
            return "Message ready to be sent.";
        }
    }

    public static String checkCellPhoneNumber(String phone) {
        Pattern SA_International = Pattern.compile("^\\+27[\\s.-]?\\d{2}[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
        Matcher matcher = SA_International.matcher(phone);

        if (SA_International.matcher(phone).matches()) {
            return "Cellphone number successfully added.";
        }else {
            return "Cellphone number incorrectly formatted or does not contain international code.";
        }
    }

    public static String messageSentAction(String action) {
        switch (action) {
            case "Send Message":
                return "Message successfully sent.";
            case "Disregarded Message":
                return "Press 0 to delete message";
            case "Store Message":
                return "Message successfully stored.";
            default:
                return "";
        }
    }

    //Additional method for backwards compatibility
    public static String searchMessageOptions() {
        return SentMessage();
    }

    public static void resetMessagesSentCounterForHash() {
        messagesSentCounterForHash = 0;
    }

    //Display longest messsage sent by user
    public static void displayLongestMessage() {
        if (sentCount == 0) {
            System.out.println("No messages found!");
            return;
        }

        MessageEntry longestMessage = sentMessages[0];

        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = sentMessages[i];

            }
        }
        System.out.println("Longest Message Details:");
        System.out.println("MessageID: " + longestMessage.getMessageID());
        System.out.println("Recipient: " + longestMessage.getRecipient());
        System.out.println("Hash: " + longestMessage.getHash());
        System.out.println("Message: " + longestMessage.getMessage().length() + " characters");
        System.out.println("Message Content: " + longestMessage.getMessage());
        System.out.println();


    }

    public static void searchByRecipient(String recipient) {
        System.out.println("Messages for Recipient: " + recipient);
        boolean found = false;
        int messageCount = 0;

        //Search sent messages
        System.out.println("Sent Messages:");
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].getRecipient().equals(recipient)) {
                messageCount++;
                System.out.println(" " + messageCount + ". [SENT] MessageID: " + sentMessages[i].getMessageID() +
                        ", Hash: " + sentMessages[i].getHash() +
                        ", Message: " + sentMessages[i].getMessage());
                found = true;
            }
        }

        //Search stored messages
        System.out.println("\nStored Messages:");
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getRecipient().equals(recipient)) {
                messageCount++;
                System.out.println(" " + messageCount + ". [STORED] MessageID: " + storedMessages[i].getMessageID() +
                        ", Hash: " + storedMessages[i].getHash() +
                        ", Message: " + storedMessages[i].getMessage());
                found = true;
            }
        }

        //Search disregarded messages
        System.out.println("\nDisregarded Messages:");
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i].getRecipient().equals(recipient)) {
                messageCount++;
                System.out.println(" " + messageCount + ". [DISREGARDED] MessageID: " + disregardedMessages[i].getMessageID() +
                        ", Hash: " + disregardedMessages[i].getHash() +
                        ", Message: " + disregardedMessages[i].getMessage());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found for recipient: " + recipient);
        }else {
            System.out.println("\n Total messages found:"  + messageCount);
        }
        System.out.println();
    }

    //Deletes Message using message hash

    public static void deleteByHash(String hash) {
        boolean found = false;

        //Remove from sent messages
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].getHash().equals(hash)) {
                //Shift remaining messages left
                for (int j = i; j < sentCount - 1; j++) {
                    sentMessages[j] = sentMessages[j + 1];
                }
                sentCount--;
                found = true;
                System.out.println("Message with hash " + hash + " deleted from sent messages.");
                break;
            }
        }

        //Remove from message hash array
        for (int i = 0; i < hashCount; i++) {
            if (messageHashes[i].equals(hash)) {
                //Shift remaining hashes left
                for (int j = i; j < hashCount - 1; j++) {
                    messageHashes[j] = messageHashes[j + 1];
                }
                hashCount--;
                break;
            }
        }

        //Remove from stored messages
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getHash().equals(hash)) {
                System.out.println("Deleting STORED message: " + storedMessages[i].getMessage());

                //Shift remaining messages left
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                }
                storedCount--;
                found = true;
                System.out.println("Message with hash " + hash + " deleted from stored messages.");
                break;
            }
        }

        //Remove from disregarded messages
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i].getHash().equals(hash)) {
                System.out.println("Deleting DISREGARDED message: " + disregardedMessages[i].getMessage());

                //Shift remaining messages left
                for (int j = i; j < disregardedCount - 1; j++) {
                    disregardedMessages[j] = disregardedMessages[j + 1];
                }
                disregardedCount--;
                found = true;
                break;
            }
        }

        //Remove from HashMaps
        messageHashToEntryMap.remove(hash);

        if (found) {
            System.out.println("Message with hash " + hash + " deleted successfully.");
        } else {
            System.out.println("No message found with hash: " + hash);
        }
    }

    //Search for a message by ID
    public static void searchByMessageID(String messageID) {
        System.out.println("Searching for Message ID: " + messageID);
        boolean found = false;

        //Search sent messages
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].getMessageID().equals(messageID)) {
                System.out.println("Found in Sent Messages:");
                System.out.println(" Message: " + sentMessages[i].getMessage());
                System.out.println(" Recipient: " + sentMessages[i].getRecipient());
                System.out.println(" Hash: " + sentMessages[i].getHash());
                found = true;
                break;
            }
        }

        //Search stored messages
        if (!found) {
            for (int i = 0; i < storedCount; i++) {
                if (storedMessages[i].getMessageID().equals(messageID)) {
                    System.out.println("Found in Stored Messages:");
                    System.out.println(" Message: " + storedMessages[i].getMessage());
                    System.out.println(" Recipient: " + storedMessages[i].getRecipient());
                    System.out.println(" Hash: " + storedMessages[i].getHash());
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("No message found with ID: " + messageID);
        }
        System.out.println();
    }

    //Write stored messages to JSON file
    //Required for JSON file operations
    public static void writeStoredMessagesToJSON() {
        //JSON generation assisted by ChatGPT
        try {
            JSONArray jsonArray = new JSONArray();


            for (int i = 0; i < storedCount; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("messageID", storedMessages[i].getMessageID());
                jsonObject.put("recipient", storedMessages[i].getRecipient());
                jsonObject.put("message", storedMessages[i].getMessage());
                jsonObject.put("hash", storedMessages[i].getHash());
                jsonArray.put(jsonObject);
            }

            //Write JSON array to file
            Files.write(Paths.get("stored_messages.json"), jsonArray.toString(4).getBytes());
            System.out.println("Stored messages written to stored_messages.json");
        } catch (IOException e) {
            System.out.println("Error writing stored messages to JSON file: " + e.getMessage());
        }
    }
    //Reads stored messages from JSON file
    public static void readStoredMessagesFromJSON() {
        try {
            //Check if file exists
            if (!Files.exists(Paths.get("stored_messages.json"))) {
                System.out.println("No stored_messages.json file found.");
                createSampleStoredMessagesFile();
                return;
            }

            //Read file content
            String content = new String(Files.readAllBytes(Paths.get("stored_messages.json")));
            JSONArray jsonArray = new JSONArray(content);

            //Clear existing stored messages
            storedCount = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String messageID = jsonObject.optString("messageID", "UNKNOWN");
                String hash = jsonObject.optString("hash", "UNKNOWN");
                String recipient = jsonObject.optString("recipient", "UNKNOWN");
                String messageContent = jsonObject.optString("message", "UNKNOWN");

                //Create MessageEntry object and add to stored messages array
                MessageEntry entry = new MessageEntry(messageID, recipient, messageContent, hash);
                if (storedCount < storedMessages.length) {
                    storedMessages[storedCount++] = entry;
                }
            }

            System.out.println("Stored messages loaded from stored_messages.json");


        }catch (Exception e) {
            System.out.println("Error reading stored messages from JSON file: " + e.getMessage());
        }

    }

    //Create sample stored_messages.json file if not found

    private static void createSampleStoredMessagesFile() {
        try {
            JSONArray jsonArray = new JSONArray();

            //Sample message 1
            JSONObject sampleMessage1 = new JSONObject();
            sampleMessage1.put("messageID", "1234567890");
            sampleMessage1.put("hash", "12:1:HELLOWORLD");
            sampleMessage1.put("recipient", "+27123456789");
            sampleMessage1.put("message", "Hello, this is a sample message.");
            jsonArray.put(sampleMessage1);

            //Sample message 2
            JSONObject sampleMessage2 = new JSONObject();
            sampleMessage2.put("messageID", "0987654321");
            sampleMessage2.put("hash", "09:2:SAMPLEMESSAGE");
            sampleMessage2.put("recipient", "+27987654321");
            sampleMessage2.put("message", "This is another example of a message.");
            jsonArray.put(sampleMessage2);

            Files.write(Paths.get("stored_messages.json"), jsonArray.toString(4).getBytes());

            System.out.println("Sample stored_messages.json file created.");

        } catch (Exception e) {
            System.out.println("Error creating sample stored_messages.json file: " + e.getMessage());
        }
    }

    //Show full report of all messages
    //Display Message Report

    public static void displayMessageReport() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("QuickChat Message Report");
        System.out.println("=".repeat(80));

        if (sentCount == 0) {
            System.out.println("No sent messages to display.");
            System.out.println("=".repeat(80) + "\n");
            return;
        }

        System.out.printf("%-10s %-12s %-12s %-12s\n", "MessageID", "Recipient", "Hash", "Message");
        System.out.println("-".repeat(80));

        for (int i = 0; i < sentCount; i++) {
            MessageEntry entry = sentMessages[i];
            System.out.printf("%-10d %-12s %-12s %-12s %-50s\n",
                    (i + 1),
                    "You", //Sender is always "You"
                    entry.getMessageID(),
                    entry.getRecipient(),
                    entry.getHash(),
                    entry.getMessage()
            );
        }
        System.out.println("=".repeat(80));
        System.out.println("Summary");
        System.out.println("Total Messages Sent: " + sentCount);
        System.out.println("Total Messages Stored: " + storedCount);
        System.out.println("Total Messages Disregarded: " + disregardedCount);
        System.out.println("Total Messages in System: " + (sentCount + storedCount + disregardedCount));
        System.out.println("=".repeat(80) + "\n");
    }

    //Show recently sent messages

    public static void showRecentlySentMessages() {
        System.out.println("Displaying Recently Sent Messages:");

        if (sentCount == 0) {
            JOptionPane.showMessageDialog(null, "No recently sent messages to display.");
            return;
        }

        int startIndex = Math.max(0, sentCount - 5);
        System.out.println("Last " + (sentCount - startIndex) + " Sent Messages:");

        for (int i = startIndex; i < sentCount; i++) {
            System.out.println((i - startIndex +1) + ". To: " + sentMessages[i].getRecipient() +
                    ", MessageID: " + sentMessages[i].getMessageID() +
                    ", Message: " + sentMessages[i].getMessage());
        }
        System.out.println();

    }

    //Helper method to extract values from stored message strings
    private static String extractValue(String message, String key) {
        int startIndex = message.indexOf(key + ": ");
        if (startIndex == -1) return "UNKNOWN";

        startIndex += key.length();
        int endIndex = message.indexOf(",", startIndex);

        if (endIndex == -1) {
            endIndex = message.length();
        }
        return message.substring(startIndex, endIndex).trim();
    }

    public static void populateTestData() {
        //Message 1: Sent
        sentMessages[sentCount++] = new MessageEntry("1000000001", "+27834557896", "Did you get the cake?", "10:1:DIDCAKE");

        //Message 2: Stored
        storedMessages[storedCount++] = new MessageEntry("1000000002", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "10:2:WHERETIME");

        //Message 3:
        disregardedMessages[disregardedCount++] = new MessageEntry("1000000003","+27834484567", "Yohoooo, I am at your gate.", "10:3:YOH000OGATE");

        //Message 4: Sent
        sentMessages[sentCount++] = new MessageEntry("1000000004", "+27834434567", "It is dinner time!", "10:4:ITTIME");

        //Message 5: Stored
        storedMessages[storedCount++] = new MessageEntry("1000000005", "+27838884567", "Ok, I am leaving without you.", "10:5:OKYOU");
    }
}


