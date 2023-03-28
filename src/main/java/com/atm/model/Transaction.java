package com.atm.model;


import java.util.Date;
import java.util.Random;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction{
    private String transactionId;
    private String accountNumber;
    private String transactionType;
    private Double amount;
    private Timestamp transactionDate;
    
    private String sourceOrDestinationAccountNumber;
    private Double closingBalance;

    private static Random random = new Random();

    public Transaction(String accountNumber, String transactionType, Double amount,
            String sourceOrDestinationAccountNumber, Double closingBalance) {
        this.transactionId = generateTransactionId(transactionType, accountNumber);
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = new Timestamp(new Date().getTime());
        this.sourceOrDestinationAccountNumber = sourceOrDestinationAccountNumber;
        this.closingBalance = closingBalance;
    }
    public Transaction(String accountNumber, String transactionType, Double amount,
             Double closingBalance) {
        this.transactionId = generateTransactionId(transactionType, accountNumber);
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = new Timestamp(new Date().getTime());
        this.sourceOrDestinationAccountNumber = "";
        this.closingBalance = closingBalance;
    }

    private String generateTransactionId(String transactionType, String accountNumber){
        return transactionType.substring(0,3)+accountNumber.substring(7)+hashCode()+random.nextInt(100000);
    }
    
}
