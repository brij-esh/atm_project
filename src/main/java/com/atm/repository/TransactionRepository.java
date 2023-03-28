package com.atm.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.atm.jdbc_configuration.JdbcConnection;
import com.atm.model.Transaction;
import com.atm.style.ConsoleColor;
import com.atm.style.ConsoleLogger;

public class TransactionRepository {

    private static final String INSERT = "INSERT INTO transactions(transactionId, accountNumber, transactionType, amount, localDateTime, sourceOrDestinationAccountNumber, closingBalance) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_ALL_BY_ID = "SELECT * FROM transactions WHERE accountNumber = ?";


    private static Connection connection = JdbcConnection.getConnection();

    public void save(Transaction transaction){
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = createPreparedStatement(INSERT);
            preparedStatement.setString(1, transaction.getTransactionId());
            preparedStatement.setString(2, transaction.getAccountNumber());
            preparedStatement.setString(3, transaction.getTransactionType());
            preparedStatement.setDouble(4, transaction.getAmount());
            preparedStatement.setTimestamp(5, transaction.getTransactionDate());
            preparedStatement.setString(6, transaction.getSourceOrDestinationAccountNumber());
            preparedStatement.setDouble(7, transaction.getClosingBalance());
            int rowEffected = preparedStatement.executeUpdate();
            if(rowEffected>0){
                ConsoleLogger.print("Transaction saved", ConsoleColor.ANSI_GREEN, true, true);
            }
        } catch (SQLException e) {
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }finally{
            closeStatement(preparedStatement);
        }
    }

    public List<Transaction> findAll(String id){
        PreparedStatement preparedStatement = null;
        List<Transaction> transactions = new ArrayList<>();
        try {
            preparedStatement = createPreparedStatement(FIND_ALL_BY_ID);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Transaction transaction = new Transaction(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getTimestamp(5),
                    resultSet.getString(6),
                    resultSet.getDouble(7)
                    );
                    transactions.add(transaction);
            }
        } catch (SQLException e) {
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }
        return transactions;
    }

    private PreparedStatement createPreparedStatement(String query) throws SQLException{
        return connection.prepareStatement(query);
    }
    private void closeStatement(PreparedStatement preparedStatement){
        try {
            if(preparedStatement!=null){
                preparedStatement.close();
            }
        } catch (SQLException e) {
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }
    }
    
}
