package com.atm.service;

import java.util.List;

import com.atm.model.User;

public interface UserService {
    public void createUser(User user);
    public User getUserByAccountNumber(String accountNumber);
    public void printCurrentUserDetails(String accountNumber);
    public List<User> getAllUser();
    public void deleteByUserAccountNumber(String accountNumber);
    public void updateByUserAccountNumber(User user, String accountNumber);
    
    public boolean userLogin(String accountNumber, String password);

    public void checkUserBalance(String accountNumber);

    public void depositFund(String accountNumber, Double amount);

    public void withdrawFund(String accountNumber, Double amount);

    public void transferFund(String sourceAccountNumber, String destinationAccountNumber, Double amount);

    public void changePassword(String accountNumber, String oldPassword, String newPassword);
}
