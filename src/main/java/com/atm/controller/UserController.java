package com.atm.controller;

import java.io.Console;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.atm.model.Transaction;
import com.atm.model.User;
import com.atm.service.service_impl.TransactionServiceImpl;
import com.atm.service.service_impl.UserServiceImpl;
import com.atm.style.ConsoleColor;
import com.atm.style.ConsoleLogger;

public class UserController {

    private static UserServiceImpl service = new UserServiceImpl();
    private static TransactionServiceImpl transactionService = new TransactionServiceImpl();
    private static Scanner sc = new Scanner(System.in);

    private boolean isLogin = false;
    private static Console console = System.console();

    public void welcomeMessage() {
        ConsoleLogger.print("Welcome to ATM", ConsoleColor.ANSI_GREEN + ConsoleColor.ANSI_BOLD, true, false);
        ConsoleLogger.print(StringUtils.repeat("-", "welcome to atm".length() + 2), ConsoleColor.ANSI_WHITE, false,
                false);
        ConsoleLogger.print("Choose your option:", ConsoleColor.ANSI_BLUE, false, false);
        ConsoleLogger.print("1. Login", ConsoleColor.ANSI_BLUE, false, false);
        ConsoleLogger.print("2. New User", ConsoleColor.ANSI_BLUE, false, false);
        ConsoleLogger.print("3. Exit", ConsoleColor.ANSI_BLUE, false, true);
        callServices(getInputChoice());
    }

    private void callServices(Integer option) {
        if (option == 1) {
            ConsoleLogger.print("Please Enter your account number: ", ConsoleColor.ANSI_PURPLE, false, false);
            String accountNumber = sc.nextLine();
            ConsoleLogger.print("Please Enter your password: ", ConsoleColor.ANSI_PURPLE, false, false);
            char[] temp = console.readPassword();
            String password = new String(temp);
            ConsoleLogger.printPassword(password, ConsoleColor.ANSI_GREEN, false, false);
            isLogin = service.userLogin(accountNumber, password);
            if (isLogin) {
                User user = service.getUserByAccountNumber(accountNumber);
                callLoginServices(user);
            } else {
                showErrorMessage();
                welcomeMessage();
            }
        } else if (option == 2) {
            newUser();
        } else {
            ConsoleLogger.print("Exiting ATM program", ConsoleColor.ANSI_PURPLE + ConsoleColor.ANSI_BOLD, true, true);
            System.exit(0);
        }
    }

    private Integer getInputChoice() {
        String choice = sc.nextLine();
        if (Character.isDigit(choice.charAt(0))) {
            return Integer.parseInt(choice.substring(0, 1));
        } else {
            showWrongInputMessage();
        }
        return getInputChoice();
    }

    private void newUser() {
        ConsoleLogger.print("For new User We need some details:", ConsoleColor.ANSI_PURPLE + ConsoleColor.ANSI_BOLD,
                true, false);
        ConsoleLogger.print("Please enter your first name", ConsoleColor.ANSI_BLUE, false, false);
        String firstName = sc.nextLine();
        ConsoleLogger.print("Please enter your last name", ConsoleColor.ANSI_BLUE, false, false);
        String lastName = sc.nextLine();
        ConsoleLogger.print("Please enter your password", ConsoleColor.ANSI_BLUE, false, false);
        String password = sc.nextLine();
        ConsoleLogger.print("Please enter your account type:", ConsoleColor.ANSI_BLUE, false, false);
        ConsoleLogger.print("1. SAVING ACCOUNT", ConsoleColor.ANSI_BLUE, false, false);
        ConsoleLogger.print("2. CURRENT ACCOUNT", ConsoleColor.ANSI_BLUE, false, false);
        ConsoleLogger.print("3. SALARY ACCOUNT", ConsoleColor.ANSI_BLUE, false, false);
        Integer option = getInputChoice();
        String accountType;
        if (option == 1) {
            accountType = AccountType.SAVING.getType();
        } else if (option == 2) {
            accountType = AccountType.CURRENT.getType();
        } else {
            accountType = AccountType.SALARY.getType();
        }
        ConsoleLogger.print("Your opening balance will be 500.00 rupees only", ConsoleColor.ANSI_BLUE, false, false);
        User user = new User(firstName, lastName, password, accountType, 500d);
        service.createUser(user);
        welcomeMessage();
    }

    private void callLoginServices(User user) {
        while (isLogin) {
            showLoginDashboard();
            Integer option = getInputChoice();
            if (option == 1) {
                checkBalance(user);
            } else if (option == 2) {
                withdraw(user);
            } else if (option == 3) {
                deposit(user);
            } else if (option == 4) {
                transfer(user);
            }else if(option==5){
                printTransaction(user);
            }else if (option == 6) {
                changePassword(user);
            } else if (option == 7) {
                isLogin = false;
                welcomeMessage();
            }
        }
    }

    private void showLoginDashboard() {
        ConsoleLogger.print("1. Balance Enquiry", ConsoleColor.ANSI_GREEN, true, false);
        ConsoleLogger.print("2. Withdraw Fund", ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("3. Deposit Fund", ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("4. Transfer Fund", ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("5. Print Transactions", ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("6. Change Password", ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("7. Log out", ConsoleColor.ANSI_GREEN, false, false);
    }

    private void checkBalance(User user) {
        service.checkUserBalance(user.getAccountNumber());
    }

    private void withdraw(User user) {
        ConsoleLogger.print("Please Enter amount to withdraw:", ConsoleColor.ANSI_PURPLE, false, false);
        Double amount = 0.0;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (InputMismatchException e) {
            showErrorMessage();
        }
        Transaction transaction = new Transaction(user.getAccountNumber(), "Withdrawal",amount, user.getAccountBalance()-amount);
        transactionService.save(transaction);
        service.withdrawFund(user.getAccountNumber(), amount);
    }

    private void deposit(User user) {
        ConsoleLogger.print("Please Enter amount to deposit:", ConsoleColor.ANSI_PURPLE, false, false);
        Double amount = 0.0;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (InputMismatchException e) {
            showErrorMessage();
        }
        Transaction transaction = new Transaction(user.getAccountNumber(), "Deposited",amount, user.getAccountBalance()+amount);
        transactionService.save(transaction);
        service.depositFund(user.getAccountNumber(), amount);
    }

    private void transfer(User user) {
        ConsoleLogger.print("Please Enter destination user account number:", ConsoleColor.ANSI_PURPLE, false, false);
        String destinationAccountNumber = sc.nextLine();
        ConsoleLogger.print("Please Enter amount to transfer", ConsoleColor.ANSI_PURPLE, false, false);

        Double amount = 0.0;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (InputMismatchException e) {
            showErrorMessage();
        }
        User destinationUser = service.getUserByAccountNumber(destinationAccountNumber);
        Transaction transaction1 = new Transaction(user.getAccountNumber(), "Transfer",amount, destinationAccountNumber, user.getAccountBalance()-amount);
        Transaction transaction2 = new Transaction(destinationAccountNumber, "Received",amount,user.getAccountNumber(), destinationUser.getAccountBalance()+amount);

        transactionService.save(transaction1);
        transactionService.save(transaction2);
        service.transferFund(user.getAccountNumber(), destinationAccountNumber, amount);
    }

    private void changePassword(User user) {
        ConsoleLogger.print("Please enter your current password: ", ConsoleColor.ANSI_BLUE, false, false);
        String currentPassword = sc.nextLine();
        ConsoleLogger.print("Please enter your new password: ", ConsoleColor.ANSI_BLUE, false, false);
        String newPassword = sc.nextLine();
        service.changePassword(user.getAccountNumber(), currentPassword, newPassword);
    }

    private void printTransaction(User user){
        List<Transaction> transactions = transactionService.findAll(user.getAccountNumber());
        service.printCurrentUserDetails(user.getAccountNumber());
        for(Transaction transaction : transactions){
            ConsoleLogger.print("Transaction id: "+transaction.getTransactionId(), ConsoleColor.ANSI_PURPLE, true, false);
            ConsoleLogger.print("Transaction type: "+transaction.getTransactionType(), ConsoleColor.ANSI_PURPLE, false, false);
            ConsoleLogger.print("Amount: "+transaction.getAmount(), ConsoleColor.ANSI_PURPLE, false, false);
            ConsoleLogger.print("Source/Destination account number: "+(transaction.getSourceOrDestinationAccountNumber()==null||transaction.getSourceOrDestinationAccountNumber().isEmpty()? null: transaction.getSourceOrDestinationAccountNumber()), ConsoleColor.ANSI_PURPLE, false, false);
            ConsoleLogger.print("Transaction date: "+transaction.getTransactionDate(), ConsoleColor.ANSI_PURPLE, false, false);   
            ConsoleLogger.print("Closing balance: "+transaction.getClosingBalance(), ConsoleColor.ANSI_PURPLE, false, false);
        }
    }

    private void showWrongInputMessage() {
        ConsoleLogger.print("Wrong input. Please try again!", ConsoleColor.ANSI_RED + ConsoleColor.ANSI_BOLD, true,
                true);
    }

    private void showErrorMessage() {
        ConsoleLogger.print("Something went wrong. Please Try Again!", ConsoleColor.ANSI_RED + ConsoleColor.ANSI_BOLD,
                true, true);
    }

}
