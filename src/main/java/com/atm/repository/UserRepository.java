package com.atm.repository;

import com.atm.jdbc_configuration.JdbcConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.atm.model.User;
import com.atm.style.ConsoleColor;
import com.atm.style.ConsoleLogger;



public class UserRepository {

    private static final String FIND_BY_ID = "SELECT * FROM user WHERE accountNumber = ?";
    private static final String FIND_ALL = "SELECT * FROM user";
    private static final String DELETE_BY_ID = "DELETE FROM user WHERE accountNumber = ?";
    private static final String UPDATE_BY_ID = "UPDATE user SET firstName = ?, lastName = ?, password = ?, accountType = ?, accountBalance = ? WHERE accountNumber = ?";
    private static final String CREATE = "INSERT INTO user(firstName, lastName, accountNumber, password, accountType, accountBalance) VALUES (?, ?, ?, ?, ?, ?)";

    

    private static Connection connection = JdbcConnection.getConnection(); 
    
    public void save(User user){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = createPreparedStatement(CREATE);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getAccountNumber());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getAccountType());
            preparedStatement.setDouble(6, user.getAccountBalance());
            int rowEffected = preparedStatement.executeUpdate();
            if(rowEffected>0){
                ConsoleLogger.print("A new User Created.", ConsoleColor.ANSI_BLACK, true, false);
                ConsoleLogger.print("Account Details are here:" , ConsoleColor.ANSI_GREEN, false, false);
                ConsoleLogger.print("Account holder name: " +user.getFirstName()+" "+user.getLastName() , ConsoleColor.ANSI_GREEN, false, false);
                ConsoleLogger.print("Account number: "+ user.getAccountNumber() , ConsoleColor.ANSI_GREEN, false, false);
                ConsoleLogger.print("Account type: "+user.getAccountType() , ConsoleColor.ANSI_GREEN, false, false);
                ConsoleLogger.print("Current account balance: "+user.getAccountBalance() , ConsoleColor.ANSI_GREEN, false, true);
            }
        }catch(SQLException e){
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }
         finally{
            closeStatement(preparedStatement);
        }
    }

    public void updateById(User user, String id){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = createPreparedStatement(UPDATE_BY_ID);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getAccountType());
            preparedStatement.setDouble(5, user.getAccountBalance());
            preparedStatement.setString(6, id);
            int rowUpdated = preparedStatement.executeUpdate();
            if(rowUpdated>0){
                // ConsoleLogger.print("Updated!", ConsoleColor.ANSI_BLUE,true, true);
            }
        } catch (SQLException e) {
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }
        
    }

    public void deleteById(String id){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = createPreparedStatement(DELETE_BY_ID);
            preparedStatement.setString(1, id);
            int rowEffected = preparedStatement.executeUpdate();
            if(rowEffected>0){
                ConsoleLogger.print("Deleted!", ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
            }
        } catch (SQLException e) {
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }finally{
            closeStatement(preparedStatement);
        }

    }

    public User findById(String id){
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            preparedStatement = createPreparedStatement(FIND_BY_ID);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                user = new User(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getDouble(7)
                    );
            }
        } catch (SQLException e) {
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }
        return user;
    }

    public List<User> findAll(){
        PreparedStatement preparedStatement = null;
        List<User> users = new ArrayList<>();
        try {
            preparedStatement = createPreparedStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                User user = new User(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getDouble(7)
                    );
                    users.add(user);
            }
        } catch (SQLException e) {
            ConsoleLogger.print(e.getMessage(), ConsoleColor.ANSI_RED+ConsoleColor.ANSI_BOLD, true, true);
        }
        return users;
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
