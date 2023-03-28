package com.atm.model;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String accountNumber;
    private String password;
    private String accountType;
    private Double accountBalance;
    private List<Transaction> transactions;
    private static Random random = new Random();

    

    public User(Long id, String firstName, String lastName, String accountNumber, String password, String accountType,
            Double accountBalance) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
        this.password = password;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    public User(String firstName, String lastName,String password, String accountType,
            Double accountBalance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = generateAccountNumber();
        this.password = password;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        transaction.setAccountNumber(accountNumber);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (accountNumber == null) {
            if (other.accountNumber != null)
                return false;
        } else if (!accountNumber.equals(other.accountNumber))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (accountType == null) {
            if (other.accountType != null)
                return false;
        } else if (!accountType.equals(other.accountType))
            return false;
        if (accountBalance == null) {
            if (other.accountBalance != null)
                return false;
        } else if (!accountBalance.equals(other.accountBalance))
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
        result = prime * result + ((accountBalance == null) ? 0 : accountBalance.hashCode());
        return result+random.nextInt(10000);
    }

    private String generateAccountNumber(){
        int hashCode = hashCode();
        return ""+Calendar.getInstance().get(Calendar.YEAR)+(hashCode<0?-hashCode:hashCode);
    }

    
}
