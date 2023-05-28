package custom_classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import models.EmployeeDetails;
import models.StockDetails;
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
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String rowData[] = { Integer.toString(id), name, email };
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

    public static DefaultTableModel getStocksList() {

        if (makeConnection() == Results.ERROR)
            return null;

        String[] cols = { "ID", "Name", "Category", "Brand", "MRP", "Price", "Quantity" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);

        try {
            String query = "SELECT s.id, s.prod_name, c.name as category_name, s.brand, s.mrp, s.selling_price, s.quantity FROM wh_stock as s INNER JOIN categories as c ON s.category_id = c.id";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String prodName = resultSet.getString("prod_name");
                String category = resultSet.getString("category_name");
                String brand = resultSet.getString("brand");
                float mrp = resultSet.getFloat("mrp");
                float price = resultSet.getFloat("selling_price");
                int quantity = resultSet.getInt("quantity");
                String rowData[] = { Integer.toString(id), prodName, category, brand, Float.toString(mrp),
                        Float.toString(price), Integer.toString(quantity) };
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

    // function to get all categories
    public static DefaultTableModel getCategoriesList() {

        if (makeConnection() == Results.ERROR)
            return null;

        String cols[] = { "ID", "Name" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);

        try {
            String query = "SELECT * FROM categories";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String rowData[] = { Integer.toString(id), name };
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

    public interface CategoriesDetail {
        void getCategory(int queryStatus, String categoryName);
    }

    // function to get all categories for Add Product Page
    public static void getCategories(CategoriesDetail categories) {

        if (makeConnection() == Results.ERROR) {
            categories.getCategory(Results.ERROR, null);
            return;
        }

        try {
            String query = "SELECT name FROM categories";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                categories.getCategory(Results.SUCCESS, resultSet.getString("name"));
            }
            closeConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            categories.getCategory(Results.ERROR, null);
        }

    }

    public interface UnAssignedEmployees {
        void getUnAssignedEmp(int queryStatus, EmployeeDetails details);
    }

    // function to get employees which aren't assigned to any shop
    public static void getUnAssignedEmps(String input, String filter, UnAssignedEmployees empDetails) {

        if (makeConnection() == Results.ERROR) {
            empDetails.getUnAssignedEmp(Results.ERROR, null);
            return;
        }

        try {
            String filteredQuery;
            if (filter.equals("Name"))
                filteredQuery = "name LIKE '%" + input + "%'";
            else
                filteredQuery = "email = '" + input + "'";

            String query = "SELECT id, name, email FROM users " +
                    "WHERE id NOT IN (SELECT emp_id FROM shops) " +
                    "AND role = 'emp' " +
                    "AND " + filteredQuery +
                    "ORDER BY " + filter;
            resultSet = statement.executeQuery(query);
            boolean anyRowFetched = false;
            while (resultSet.next()) {
                anyRowFetched = true;
                empDetails.getUnAssignedEmp(
                        Results.SUCCESS,
                        new EmployeeDetails(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email")));
            }

            if (anyRowFetched == false)
                empDetails.getUnAssignedEmp(Results.NO_RECORD_FOUND, null);
            closeConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            empDetails.getUnAssignedEmp(Results.FAILED, null);
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

    /**
     * function to add new category to DB
     * 
     * @param categoryName
     * @return status code (int)
     */
    public static int addCategory(String categoryName) {

        if (makeConnection() == Results.ERROR) {
            return Results.ERROR;
        }

        try {
            String query = "INSERT INTO categories(name) VALUES(?)";
            pStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, categoryName);
            pStatement.addBatch();
            pStatement.executeBatch(); // executing query
            resultSet = pStatement.getGeneratedKeys(); // fetching PRIMARY KEY value (ID) of Category
            if (resultSet.next()) { // Category Added Successfully
                closeConnection(true);
                return Results.SUCCESS;
            } else {
                closeConnection(true);
                return Results.FAILED;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return Results.ERROR;
        }

    }

    /**
     * function to add stock
     * 
     * @param stockDetails object containing data of the stock
     * @return status code (int)
     */
    public static int addStock(StockDetails stockDetails) {

        if (makeConnection() == Results.ERROR)
            return Results.ERROR;

        /**
         * checking if the product with same data already exists or not
         * if exists updating the quantity of the product without creating a new stock
         **/
        String query = "select * from wh_stock where prod_name = '" + stockDetails.getProdName().trim()
                + "' AND selling_price = '" + stockDetails.getSellingPrice() + "' AND mrp = '" + stockDetails.getMrp()
                + "' AND cost_price = '" + stockDetails.getCostPrice() + "' AND brand = '"
                + stockDetails.getBrandName() + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int prodId = resultSet.getInt("id");
                int oldQty = resultSet.getInt("quantity");
                int updQty = oldQty + stockDetails.getQty();
                query = "UPDATE wh_stock SET quantity = '" + updQty + "' WHERE id = '" + prodId + "'";
                int result = statement.executeUpdate(query);
                closeConnection(true);
                return result > 0 ? Results.SUCCESS : Results.FAILED;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Results.ERROR;
        }

        /**
         * insert a new product to the Database
         */
        query = "insert into wh_stock(category_id, prod_name, selling_price, mrp, cost_price, brand, quantity) values(?, ?, ?, ?, ?, ?, ?)";
        try {
            pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pStatement.setInt(1, stockDetails.getCategoryId());
            pStatement.setString(2, stockDetails.getProdName());
            pStatement.setInt(3, stockDetails.getSellingPrice());
            pStatement.setInt(4, stockDetails.getMrp());
            pStatement.setInt(5, stockDetails.getCostPrice());
            pStatement.setString(6, stockDetails.getBrandName());
            pStatement.setInt(7, stockDetails.getQty());
            pStatement.addBatch();
            pStatement.executeBatch(); // executing query
            resultSet = pStatement.getGeneratedKeys();
            if (resultSet.next()) {
                closeConnection(true);
                return Results.SUCCESS;
            } else {
                closeConnection(true);
                return Results.FAILED;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return Results.ERROR;
        }
    }

    /**
     * connect with DB to execute the DB queries
     * 
     * @return int
     **/
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

    /**
     * close DB Connection after executing DB Query
     * 
     * @param closeResultSet
     */
    private static void closeConnection(Boolean closeResultSet) {

        try {
            connection.close();
            statement.close();
            if (pStatement != null && !pStatement.isClosed())
                pStatement.close();
            if (closeResultSet)
                resultSet.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    /**
     * generate a random alphanumeric string of 8 characters
     * 
     * @return String (Random Password)
     */
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
