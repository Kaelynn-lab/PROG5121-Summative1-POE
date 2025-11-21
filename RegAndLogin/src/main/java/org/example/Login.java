package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {

    // Private fiels to store user details
    private String Username;
    private String userPassword;
    private CharSequence cellphone;

    // Constructor to initalize user details
    public Login(String username, String userPassword, CharSequence cell) {
        setUsername(username);
        this.setUserPassword(userPassword);
        setCellphone(cell);
    }

    //Default constructor
    public Login() {

    }


    //Method to validate username that contains an underscore and is 5 characters or less
    public boolean checkUserName(String uname) {
        if (getUsername().contains("_") && getUsername().length() <= 5) {
            System.out.println("Username successfully captured");
            return true;
        } else System.out.println("Username is not correctly formatted, please ensure that your username \ncontains an underscore and is no more than five characters in length");
        return false;
    }

    //Method to validate password complexity using regex
    public boolean checkPasswordComplexity(String pass) {
        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
        Matcher matcher = pattern.matcher(pass);

        if (matcher.matches()) {
            System.out.println("Password successfully captured.");
            return true;
        }else System.out.println("Password is not correctly formatted; please ensure that the password \ncontains at least eight characters, a capital letter, a number, and a \nspecial charater.");
        return false;
    }

    /*
    The checkCellPhoneNumber() method was developed with the assistance of ChatGPT to generate an expression that validates South African cellphone numbers with its international code.
    OpenAI. (2023). ChatGPT (Mar 14 version) [Large language model]
     https://chatgpt.com/*/
    public boolean checkCellPhoneNumber(CharSequence phone) {
        Pattern SA_International = Pattern.compile("^\\+27[\\s.-]?\\d{2}[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
        Matcher matcher = SA_International.matcher(phone);

        if (SA_International.matcher(phone).matches()) {
            System.out.println("Cellphone number successfully added.");
            return true;
        }else System.out.println("Cellphone number incorrectly formatted or does not contain internatonal code.");
        return false;
    }

    // Registers a user if both username and password validations pass
    public String registerUser() {
        if (checkUserName(getUsername()) == true && checkPasswordComplexity(getUserPassword()) == true) {
            return "Welcome " + getUsername() + ", it is great to see you again.";
        }else return "Username or password incorrect, please try again.";
    }

    //Validates a user by comparing input credentials with stored ones
    public boolean loginUser(String username, String password, CharSequence phone) {
        if (username.equals(getUsername()) && password.equals(getUserPassword()) && phone.equals(getCellphone())) {
            return true;
        }else return false;
    }

    //Returns a login status message based on three validation flags.
    public String returnLoginStatus(boolean usernameValid, boolean passwordValid, boolean phoneValid) {
        if (usernameValid == true && passwordValid == true && phoneValid == true) {
            return "A successful login";
        }else return "A failed login";
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public CharSequence getCellphone() {
        return cellphone;
    }

    public void setCellphone(CharSequence cell) {
        this.cellphone = cell;

    }
}
