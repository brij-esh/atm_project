package com.atm.service.service_impl;

import java.util.List;

import com.atm.model.Transaction;
import com.atm.model.User;
import com.atm.repository.UserRepository;
import com.atm.service.UserService;
import com.atm.style.ConsoleColor;
import com.atm.style.ConsoleLogger;
import java.util.Base64;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = new UserRepository();
    private final TransactionServiceImpl transactionService = new TransactionServiceImpl();

    private String passwordEncoder(String rawPassword){
        return Base64.getEncoder().encodeToString(rawPassword.getBytes());
    }

    private String passwordDecoder(String encodedPassword){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
        return new String(decodedBytes);
    }

    @Override
    public void createUser(User user) {
        user.setPassword(passwordEncoder(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserByAccountNumber(String accountNumber) {
        return userRepository.findById(accountNumber);
    }

    @Override
    public void printCurrentUserDetails(String accountNumber) {
        User user = getUserByAccountNumber(accountNumber);
        ConsoleLogger.print("Current account details are here:", ConsoleColor.ANSI_PURPLE, true, false);
        ConsoleLogger.print("Account holder name: " + user.getFirstName() + " " + user.getLastName(),
                ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("Account number: " + user.getAccountNumber(), ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("Account type: " + user.getAccountType(), ConsoleColor.ANSI_GREEN, false, false);
        ConsoleLogger.print("Current account balance: " + user.getAccountBalance(), ConsoleColor.ANSI_GREEN, false,
                true);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteByUserAccountNumber(String accountNumber) {
        userRepository.deleteById(accountNumber);
    }

    @Override
    public void updateByUserAccountNumber(User user, String accountNumber) {
        userRepository.updateById(user, accountNumber);
    }

    @Override
    public boolean userLogin(String accountNumber, String password) {
        User user = getUserByAccountNumber(accountNumber);
        if(user==null){
            ConsoleLogger.print("Account does not exist. Please try again!", ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
            return false;
        }
        return passwordDecoder(user.getPassword()).equals(password);
    }

    @Override
    public void checkUserBalance(String accountNumber) {
        User user = getUserByAccountNumber(accountNumber);
        ConsoleLogger.print("Your current total balance is: " + user.getAccountBalance().toString(),
                ConsoleColor.ANSI_PURPLE + ConsoleColor.ANSI_BOLD, true, true);
    }

    @Override
    public void depositFund(String accountNumber, Double amount) {
        if (amount <= 0) {
            ConsoleLogger.print("Amount should not be negative or zero", ConsoleColor.ANSI_RED, true, true);
            return;
        }
        User user = getUserByAccountNumber(accountNumber);
        user.setAccountBalance(user.getAccountBalance() + amount);
        userRepository.updateById(user, accountNumber);
        Transaction transaction = new Transaction(user.getAccountNumber(), "Deposited",amount, user.getAccountBalance());
        transactionService.save(transaction);
        ConsoleLogger.print(
                "Your account with account no. ending with " + accountNumber.substring(8) + " credited " + amount
                        + " Rs. Your current balance is: " + user.getAccountBalance(),
                ConsoleColor.ANSI_BLUE + ConsoleColor.ANSI_BOLD, true, true);
    }

    @Override
    public void withdrawFund(String accountNumber, Double amount) {
        User user = getUserByAccountNumber(accountNumber);
        if(amount<=0){
            ConsoleLogger.print("Amount should not be negative or zero.", ConsoleColor.ANSI_RED, false, false);
        }else if (user.getAccountBalance() < amount) {
            ConsoleLogger.print("Insufficient Amount! ", ConsoleColor.ANSI_RED + ConsoleColor.ANSI_BOLD, true, true);
        } else {
            user.setAccountBalance(user.getAccountBalance() - amount);
            userRepository.updateById(user, accountNumber);
            Transaction transaction = new Transaction(user.getAccountNumber(), "Withdrawal",amount, user.getAccountBalance());
            transactionService.save(transaction);
            ConsoleLogger.print(
                    "Your account with account no. ending with " + accountNumber.substring(8) + " debited " + amount
                            + " Rs. Your current balance is: " + user.getAccountBalance(),
                    ConsoleColor.ANSI_BLUE + ConsoleColor.ANSI_BOLD, true, true);
        }
    }

    @Override
    public void transferFund(String sourceAccountNumber, String destinationAccountNumber, Double amount) {
        User sourceUser = getUserByAccountNumber(sourceAccountNumber);
        User destinationUser = getUserByAccountNumber(destinationAccountNumber);
        if (destinationUser == null) {
            ConsoleLogger.print("May be this account number does not exist. Please try again!",
                    ConsoleColor.ANSI_BLACK + ConsoleColor.ANSI_BOLD, true, true);
        } else if (amount <= 0) {
            ConsoleLogger.print("Amount can not be negative or zero, it should be greater than zero!",
                    ConsoleColor.ANSI_RED + ConsoleColor.ANSI_BOLD, true, true);
        } else if (sourceUser.getAccountBalance() < amount) {
            ConsoleLogger.print("Insufficient Amount! ", ConsoleColor.ANSI_RED + ConsoleColor.ANSI_BOLD, true, true);
        } else {
            destinationUser.setAccountBalance(destinationUser.getAccountBalance() + amount);
            sourceUser.setAccountBalance(sourceUser.getAccountBalance() - amount);
            updateByUserAccountNumber(sourceUser, sourceAccountNumber);
            updateByUserAccountNumber(destinationUser, destinationAccountNumber);
            Transaction transaction1 = new Transaction(sourceUser.getAccountNumber(), "Transfer",amount, destinationAccountNumber, sourceUser.getAccountBalance());
        Transaction transaction2 = new Transaction(destinationAccountNumber, "Received",amount,sourceUser.getAccountNumber(), destinationUser.getAccountBalance());

        transactionService.save(transaction1);
        transactionService.save(transaction2);
            ConsoleLogger.print(amount + " Rs. Successfully transferred! ",
                    ConsoleColor.ANSI_RED + ConsoleColor.ANSI_BOLD, true, false);
            ConsoleLogger.print("Your current total balance is: " + sourceUser.getAccountBalance().toString(),
                    ConsoleColor.ANSI_PURPLE + ConsoleColor.ANSI_BOLD, false, true);
        }
    }

    @Override
    public void changePassword(String accountNumber, String oldPassword, String newPassword) {
        User user = getUserByAccountNumber(accountNumber);
        if (user == null) {
            ConsoleLogger.print("User does not exist. Please try again!", ConsoleColor.ANSI_RED, true, true);
        } else if (passwordDecoder(user.getPassword()).equals(oldPassword)) {
            user.setPassword(passwordEncoder(newPassword));
            updateByUserAccountNumber(user, accountNumber);
            ConsoleLogger.print("Password changed successfully!",
                    ConsoleColor.ANSI_PURPLE, true, true);
        } else {
            ConsoleLogger.print("Your password / account number does not match with your existing password. Try again please!",
                    ConsoleColor.ANSI_RED, true, true);
        }

    }

}
