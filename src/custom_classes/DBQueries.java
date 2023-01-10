package custom_classes;

import java.sql.Connection;
import java.sql.DriverManager;
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
    private static ResultSet resultSet;

    // Login User
    public static UserDetails loginUser(String username, String password) {

        if (makeConnection() == Results.ERROR)
            return new UserDetails(Results.ERROR);

        try {
            String query = "select * from USERS where USERNAME='" + username + "' and PASSWORD='" + password + "'";
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

            return tableModel;
        } catch (SQLException ex) {
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

}
