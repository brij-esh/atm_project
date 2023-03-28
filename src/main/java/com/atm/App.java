package com.atm;

import com.atm.controller.UserController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        UserController userController = new UserController();
        userController.welcomeMessage();


    }
}

// import java.io.*;

// public class App {
//     public static void main(final String[] args) {
//         String password = PasswordField.readPassword("Enter password:");
//         System.out.println("Password entered was:" + password);
//     }
// }

//