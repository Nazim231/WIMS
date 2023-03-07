package custom_classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import models.UserDetails;

/*
 * This class is used to make all the Database Queries
 * All Database Queries are executed from this class functions
 */

public class DBQueries {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement pStatement;
    private static ResultSet resultSet;

    // Login User
    public static UserDetails loginUser(String username, String password) {

        if (makeConnection() == Results.ERROR)
            return new UserDetails(Results.ERROR);

        try {
            String query = "select * from USERS where email='" + username + "' and PASSWORD='" + password + "'";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                // logged in successfully
                String role = resultSet.getString("role");
                closeConnection(true);
                return new UserDetails(Results.SUCCESS, role);
            } else {
                // username or password is incorrect
                closeConnection(true);
                return new UserDetails(Results.FAILED);
            }
        } catch (SQLException ex) {
            // error occured while executing the query
            // the error will be printed in Console Panel
            System.out.println(ex);
            closeConnection(true);
            return new UserDetails(Results.ERROR);
        }
    }

    // function to get all shops
    public static DefaultTableModel getShopsList() {

        if (makeConnection() == Results.ERROR)
            return null;

        String[] cols = { "ID", "Employee ID", "Shop Name", "Shop Address" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);

        try {
            String query = "select * from shops";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int empID = resultSet.getInt("emp_id");
                String shopName = resultSet.getString("shop_name");
                String shopAddress = resultSet.getString("shop_address");
                String rowData[] = { Integer.toString(id), Integer.toString(empID), shopName, shopAddress };
                tableModel.addRow(rowData);
            }
            closeConnection(true);
            return tableModel;
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return null;
        }
    }

    // function to get all employees
    public static DefaultTableModel getEmployeesList() {

        if (makeConnection() == Results.ERROR)
            return null;

        String[] cols = { "ID", "Name", "Email" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);

        try {
            String query = "select id, name, email from USERS where role='emp'";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String rowData[] = { Integer.toString(id), name, email };
                tableModel.addRow(rowData);
            }
            closeConnection(true);
            return tableModel;
        } catch(SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return null;
        }

    }

    // function to add new shop data to DB
    public static int addNewShop(String shopName, int empID, String address) {
        if (makeConnection() == Results.ERROR)
            return Results.ERROR;

        try {
            String query = "insert into SHOPS(emp_id, shop_name, shop_address) VALUES('" + empID + "','" + shopName
                    + "','" + address + "')";
            int result = statement.executeUpdate(query);
            closeConnection(false);
            return result > 0 ? Results.SUCCESS : Results.FAILED;
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(false);
            return Results.ERROR;
        }
    }

    // interface to return the Employee Query Execution Status
    public interface AddingEmployeeStatus {
        void employeeStatus(int result, String password);
    }

    // function to add new employee to DB
    public static void addEmployee(String name, String email, AddingEmployeeStatus queryStatus) {
        if (makeConnection() == Results.ERROR) {
            queryStatus.employeeStatus(Results.ERROR, null);
            return;
        }
        try {
            String query = "insert into USERS(name, email, password, sec_ques, sec_ans, role) VALUES(?, ?, ?, ?, ?, ?)";
            pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, name);
            pStatement.setString(2, email);
            String plainText = generatePassword(); // Generating Random String(Password)
            pStatement.setString(3, EncryptPassword.encrypt(plainText));
            pStatement.setString(4, "What is your Birth Place?");
            pStatement.setString(5, "Imaginary");
            pStatement.setString(6, "emp");
            pStatement.addBatch();
            pStatement.executeBatch(); // executing Query
            resultSet = pStatement.getGeneratedKeys(); // fetching PRIMARY KEY value (ID) of employee
            if (resultSet.next()) { // Employee Created Successfully
                int id = Integer.parseInt(resultSet.getString(1));
                closeConnection(true);
                queryStatus.employeeStatus(id, plainText);
            } else {
                closeConnection(true);
                queryStatus.employeeStatus(Results.FAILED, "");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(false);
            queryStatus.employeeStatus(Results.ERROR, "");
        }
    }

    // function to connect with DB to execute the DB queries
    private static int makeConnection() {
        try {
            Class.forName("java.sql.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wims", "root", "");
            statement = connection.createStatement();
            return Results.SUCCESS;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
            return Results.ERROR;
        }
    }

    // function to close DB connection after executing the DB queries
    private static void closeConnection(Boolean closeResultSet) {
        try {
            connection.close();
            statement.close();
            if (closeResultSet)
                resultSet.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    // Generate a Random String(Password) of length 8
    private static String generatePassword() {
        int length = 8;
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

}
