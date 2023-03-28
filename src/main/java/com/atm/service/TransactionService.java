package com.atm.service;

import java.util.List;

import com.atm.model.Transaction;

public interface TransactionService {
    
    public void save(Transaction transaction);

    public List<Transaction> findAll(String accountNumber);
}
