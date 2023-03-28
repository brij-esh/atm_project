package com.atm.service.service_impl;

import java.util.List;

import com.atm.model.Transaction;
import com.atm.repository.TransactionRepository;
import com.atm.service.TransactionService;

public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository = new TransactionRepository();

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll(String accountNumber) {
        return transactionRepository.findAll(accountNumber);
    }
    
}
