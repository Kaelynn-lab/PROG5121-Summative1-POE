package org.example;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.Message.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Register your account\n");
        System.out.print("Enter Username:");
        String Uname = scanner.next();
        System.out.print("Enter Password:");
        String pass = scanner.next();
        System.out.print("Enter Cellphone Nummber:");
        CharSequence phone = scanner.next();

        Login RegisterUser = new Login(Uname, pass, phone);

        System.out.println();
        RegisterUser.checkUserName(Uname);
        System.out.println();
        RegisterUser.checkPasswordComplexity(pass);
        System.out.println();
        RegisterUser.checkCellPhoneNumber(phone);
        System.out.println();
        System.out.println(RegisterUser.registerUser());
        System.out.println();

        System.out.print("Login to your account\n");
        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            System.out.print("Enter Username:");
            String Uname1= scanner.next();
            System.out.print("Enter Password:");
            String pass1 = scanner.next();
            System.out.print("Enter Cellphone Nummber:");
            CharSequence phone1 = scanner.next();

            loginSuccessful = RegisterUser.loginUser(Uname1, pass1, phone1);

            System.out.println();
            System.out.println(RegisterUser.returnLoginStatus(loginSuccessful, loginSuccessful, loginSuccessful));

            if (!loginSuccessful) {
                System.out.println("Login failed. Please try again.\n");
            } else {
                System.out.println("Login successful. Welcome back " + Uname1 + "!");
            }
        }
        // Proceed to messaging system after successful login
        System.out.println();
        Message.message(args); // Call the message method to start the messaging system

        // Call populateTestData() Method
        populateTestData(); //Fill arrays with test messages

        //Display longest sent message
        Message.displayLongestMessage();

        //Display last 5 recently sent messages
        Message.showRecentlySentMessages();

        //Search messages for a recipient
        Message.searchByRecipient("+2783888456700");

        //Display full report
        Message.displayMessageReport();

        //Display all sent messages
        for (int i = 0; i < sentCount; i++) {
            System.out.println(sentMessages[i].getMessage());
        }

        //Display all stored messages
        for (int i = 0; i < storedCount; i++) {
            System.out.println(storedMessages[i].getMessage());
        }

        //Display all disregarded messsages
        for (int i = 0; i < disregardedCount; i++) {
            System.out.println(disregardedMessages[i].getMessage());
        }
    }

}


