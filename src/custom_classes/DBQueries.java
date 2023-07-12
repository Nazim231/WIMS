package custom_classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

import models.EmployeeDetails;
import models.ShopDetails;
import models.ShopStockDetails;
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

    // Logged In User Profile Details
    public static UserDetails currentUser;
    public static ShopDetails shopDetails = new ShopDetails(2, "Storesy", "Bulandshahr");

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
                String name = resultSet.getString("name");

                // getting the registered shop at the employee
                if (role == "emp") {
                    query = "select * from SHOPS where emp_id=" + resultSet.getInt("emp_id");
                    resultSet = statement.executeQuery(query);
                    if (resultSet.next()) {
                        int shopId = resultSet.getInt("id");
                        String shopName = resultSet.getString("shop_name");
                        String shopAddress = resultSet.getString("shop_address");
                        shopDetails = new ShopDetails(shopId, shopName, shopAddress);
                    }
                }

                closeConnection(true);
                currentUser = new UserDetails(Results.SUCCESS, name, role);
                return currentUser;
            } else {
                // username or password is incorrect
                closeConnection(true);
                currentUser = new UserDetails(Results.FAILED);
                return currentUser;
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

    public static ShopDetails getShopDetails(int shopId) {

        if (makeConnection() == Results.ERROR)
            return null;

        String query = "select s.*, users.name as user_name from shops as s "
                + "inner join users on s.emp_id = users.id "
                + "WHERE s.id = " + shopId;
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int empId = resultSet.getInt("emp_id");
                String shopName = resultSet.getString("shop_name");
                String shopAddress = resultSet.getString("shop_address");
                String empName = resultSet.getString("user_name");
                ShopDetails shopDetails = new ShopDetails(id, shopName, shopAddress, empId, empName);
                return shopDetails;
            } else {
                closeConnection(true);
                return null;
            }
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

    public interface WarehouseStock {
        void warehouseStock(int status, StockDetails stock);
    }

    public static void getWarehouseStock(WarehouseStock stockDetails) {

        if (makeConnection() == Results.ERROR) {
            stockDetails.warehouseStock(Results.ERROR, null);
        }

        String query = "SELECT * FROM wh_stock";
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String prodName = resultSet.getString("prod_name");
                int categoryId = resultSet.getInt("category_id");
                int sellingPrice = resultSet.getInt("selling_price");
                int mrp = resultSet.getInt("mrp");
                int costPrice = resultSet.getInt("cost_price");
                String brandName = resultSet.getString("brand");
                int qty = resultSet.getInt("quantity");

                StockDetails data = new StockDetails(prodName, brandName, categoryId, mrp, sellingPrice, costPrice,
                        qty);
                data.setProdId(id);
                stockDetails.warehouseStock(Results.SUCCESS, data);
            }
            closeConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            stockDetails.warehouseStock(Results.ERROR, null);
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
            String query = "insert into SHOPS(emp_id, shop_name, shop_address) VALUES(?, ?, ?)";
            pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pStatement.setInt(1, empID);
            pStatement.setString(2, shopName);
            pStatement.setString(3, address);
            pStatement.addBatch();
            pStatement.executeBatch(); // executing Query
            resultSet = pStatement.getGeneratedKeys(); // fetching PRIMARY KEY value (ID) of shop
            int status = resultSet.getInt("id") > 0 ? Results.SUCCESS : Results.FAILED;
            closeConnection(true);
            return status;
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

        boolean prodAdded = false;
        /**
         * checking if the product with same data already exists or not
         * if exists updating the quantity of the product without creating a new stock
         **/
        String query = "select * from wh_stock where prod_name = '" + stockDetails.getProdName().trim()
                + "' AND selling_price = '" + stockDetails.getSellingPrice() + "' AND mrp = '" + stockDetails.getMrp()
                + "' AND cost_price = '" + (int) (stockDetails.getCostPrice() / stockDetails.getQty())
                + "' AND brand = '"
                + stockDetails.getBrandName() + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                stockDetails.setProdId(resultSet.getInt("id"));
                int oldQty = resultSet.getInt("quantity");
                int updQty = oldQty + stockDetails.getQty();
                query = "UPDATE wh_stock SET quantity = '" + updQty + "' WHERE id = '" + stockDetails.getProdId() + "'";
                int result = statement.executeUpdate(query);
                prodAdded = result > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Add WH Stock Update Existing Product Error: " + ex);
            closeConnection(true);
            return Results.ERROR;
        }

        /**
         * insert a new product to the Database
         */
        if (!prodAdded) {
            query = "insert into wh_stock(category_id, prod_name, selling_price, mrp, cost_price, brand, quantity) values(?, ?, ?, ?, ?, ?, ?)";
            try {
                pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pStatement.setInt(1, stockDetails.getCategoryId());
                pStatement.setString(2, stockDetails.getProdName());
                pStatement.setInt(3, stockDetails.getSellingPrice());
                pStatement.setInt(4, stockDetails.getMrp());
                pStatement.setInt(5, (int) stockDetails.getCostPrice() / stockDetails.getQty());
                pStatement.setString(6, stockDetails.getBrandName());
                pStatement.setInt(7, stockDetails.getQty());
                pStatement.addBatch();
                pStatement.executeBatch(); // executing query
                resultSet = pStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    stockDetails.setProdId((int) resultSet.getLong(1));
                } else {
                    closeConnection(true);
                    return Results.FAILED;
                }
            } catch (SQLException ex) {
                System.out.println("Add WH Stock Insert New Product Error:\n" + ex);
                closeConnection(true);
                return Results.ERROR;
            }
        }

        /**
         * adding a Warehouse History
         */
        query = "INSERT INTO wh_purchasing_history(prod_id, quantity, total_price) VALUES(?, ?, ?)";
        try {
            pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pStatement.setInt(1, stockDetails.getProdId());
            pStatement.setInt(2, stockDetails.getQty());
            pStatement.setInt(3, stockDetails.getCostPrice());
            pStatement.addBatch();
            pStatement.executeBatch();
            resultSet = pStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int key = (int) resultSet.getLong(1);
                closeConnection(true);
                return key > 0 ? Results.SUCCESS : Results.FAILED;
            } else {
                closeConnection(true);
                return Results.FAILED;
            }
        } catch (SQLException ex) {
            System.out.println("Add WH Stock Purchase History Error:\n" + ex);
            closeConnection(true);
            return Results.ERROR;
        }

    }

    /**
     * Add Stock to the Shop
     */
    public static int addShopStock(int shopId, DefaultListModel<StockDetails> stocks,
            HashMap<Integer, Integer> stockQtys) {

        if (makeConnection() == Results.ERROR) {
            return Results.ERROR;
        }

        // Remove Stock From Warehouse
        String updateWHStockQuery = "UPDATE wh_stock SET quantity = quantity - ? WHERE id = ?";
        Set<Integer> ids = stockQtys.keySet();
        try {
            pStatement = connection.prepareStatement(updateWHStockQuery);
            int i = 1;
            for (int id : ids) {
                pStatement.setInt(1, stockQtys.get(id));
                pStatement.setInt(2, id);
                pStatement.addBatch();
                if (i == ids.size()) {
                    pStatement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(false);
            return Results.ERROR;
        }
        // END : Removing Stocks From Warehouse

        // Adding Shop Purchase History
        String historyQuery = "INSERT INTO shops_history(prod_id, shop_id, quantity, stock_from_admin) VALUES(?, "
                + shopId + ", ?, 1)";
        try {
            pStatement = connection.prepareStatement(historyQuery);
            int i = 1;
            for (Entry<Integer, Integer> item : stockQtys.entrySet()) {
                pStatement.setInt(1, item.getKey());
                pStatement.setInt(2, item.getValue());
                pStatement.addBatch();
                if (i == stocks.size()) {
                    pStatement.executeBatch();
                }
                i++;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(false);
            return Results.ERROR;
        }
        // END : Adding Shop Purchase History

        // Update Shop Stock Quantity Queries
        String query = "SELECT stock_id, stock_quantity FROM shops_stock WHERE stock_id IN (";
        Set<Integer> prodIds = stockQtys.keySet();
        int index = 1;
        for (int prodId : prodIds) {
            query += prodId + (index != stockQtys.size() ? ", " : "");
        }
        query = query + ") AND shop_id = " + shopId;
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Update Product Quantities
                int prodId = resultSet.getInt("stock_id");
                int updatedQty = resultSet.getInt("stock_quantity") + stockQtys.get(prodId);
                String query2 = "UPDATE shops_stock SET stock_quantity=? WHERE shop_id=? AND stock_id=?";
                pStatement = connection.prepareStatement(query2);
                pStatement.setInt(1, updatedQty);
                pStatement.setInt(2, shopId);
                pStatement.setInt(3, prodId);
                pStatement.addBatch();
                pStatement.executeBatch();
                stockQtys.remove(prodId);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return Results.ERROR;
        }
        // END : Update Shop Stock Quantities

        // Inserting new products to the shop stocks
        try {
            query = "INSERT INTO shops_stock(stock_id, shop_id, stock_quantity) VALUES(?, ?, ?)";
            pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < stocks.size(); i++) {
                StockDetails item = stocks.getElementAt(i);
                if (!stockQtys.containsKey(item.getProdId()))
                    continue;
                pStatement.setInt(1, item.getProdId());
                pStatement.setInt(2, shopId);
                pStatement.setInt(3, stockQtys.get(item.getProdId()));
                pStatement.addBatch();
                if (i == stocks.size() - 1) {
                    pStatement.executeBatch();
                    closeConnection(false);
                    return Results.SUCCESS;
                }
            }
            return stockQtys.size() == 0 ? Results.SUCCESS : Results.FAILED;

        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return Results.ERROR;
        }
        // END : Insert new products to the shop stocks
    }

    public interface ProductDetail {
        void getProductDetail(int status, StockDetails stockDetails);
    }

    public static void getProductDetails(int stockId, ProductDetail productDetail) {

        if (makeConnection() == Results.ERROR) {
            productDetail.getProductDetail(Results.ERROR, null);
            return;
        }

        String query = "select ws.*, c.name as category, MAX(wph.purchasing_date) as last_refill_date "
                + "from wh_stock as ws "
                + "left join categories as c on ws.category_id = c.id "
                + "left join wh_purchasing_history as wph on ws.id = wph.prod_id "
                + "where ws.id=" + stockId;
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                // Data found
                int prodId = resultSet.getInt("id");
                int categoryId = resultSet.getInt("category_id");
                String prodName = resultSet.getString("prod_name");
                int sellingPrice = resultSet.getInt("selling_price");
                int mrp = resultSet.getInt("mrp");
                int costPrice = resultSet.getInt("cost_price");
                int qty = resultSet.getInt("quantity");
                String brand = resultSet.getString("brand");
                String categoryName = resultSet.getString("category");
                String lastRefillDate = resultSet.getDate("last_refill_date").toString();

                StockDetails stockDetail = new StockDetails(prodName, categoryName, mrp, sellingPrice, costPrice, qty,
                        lastRefillDate);
                stockDetail.setProdId(prodId);
                stockDetail.setCategoryId(categoryId);
                stockDetail.setBrandName(brand);

                productDetail.getProductDetail(Results.SUCCESS, stockDetail);
            } else {
                closeConnection(true);
                productDetail.getProductDetail(Results.NO_RECORD_FOUND, null);
                ;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            productDetail.getProductDetail(Results.ERROR, null);
        }
    }

    public interface ShopStock {
        void stockDetails(int status, ShopStockDetails stockData);
    }

    /**
     * function to get the available stock details
     * in a current user shop
     */
    public static void getShopAvailableStock(int shopId, ShopStock shopStock) {
        if (makeConnection() == Results.ERROR) {
            shopStock.stockDetails(Results.ERROR, null);
            return;
        }

        // getting stock product basic details
        String query = "select SS.id as id, SS.stock_id as `Stock ID`, WS.prod_name as product, SS.stock_quantity as quantity "
                + "from SHOPS_STOCK as SS "
                + "inner join WH_STOCK as WS ON SS.stock_id = WS.id "
                + "where SS.shop_id=" + shopId;
        getShopStock(query, shopStock);

    }

    public static void getShopAvailableStock(ShopStock shopStock) {

        if (makeConnection() == Results.ERROR) {
            shopStock.stockDetails(Results.ERROR, null);
            return;
        }

        // getting stock product basic details
        String query = "select SS.id as id, SS.stock_id as `Stock ID`, WS.prod_name as product, SS.stock_quantity as quantity "
                + "from SHOPS_STOCK as SS "
                + "inner join WH_STOCK as WS ON SS.stock_id = WS.id "
                + "where SS.shop_id=" + shopDetails.getShopId();
        getShopStock(query, shopStock);

    }

    private static void getShopStock(String query, ShopStock shopStock) {
        try {
            resultSet = statement.executeQuery(query);
            boolean stockFound = false;
            while (resultSet.next()) {
                stockFound = true;
                int id = resultSet.getInt("id");
                int stockId = resultSet.getInt("Stock ID");
                String productName = resultSet.getString("product");
                int quantity = resultSet.getInt("quantity");
                ShopStockDetails stockData = new ShopStockDetails(id, stockId, productName, quantity);
                shopStock.stockDetails(Results.SUCCESS, stockData);
            }

            // if shop doesn't have any stock records
            if (!stockFound) {
                shopStock.stockDetails(Results.NO_RECORD_FOUND, null);
            }
            closeConnection(true);

        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            shopStock.stockDetails(Results.ERROR, null);
        }
    }

    public static void getStockRequestStatus(ShopStock shopStock) {

        if (makeConnection() == Results.ERROR) {
            shopStock.stockDetails(Results.ERROR, null);
            return;
        }

        String query = "SELECT sh.id, ws.prod_name, sh.prod_id, sh.quantity, sh.date, sh.request_status "
                + "FROM shops_history AS sh "
                + "INNER JOIN wh_stock AS ws on sh.prod_id = ws.id "
                + "WHERE sh.shop_id = ? AND is_requested = 1";
        try {
            pStatement = connection.prepareStatement(query);
            pStatement.setInt(1, shopDetails.getShopId());
            resultSet = pStatement.executeQuery();
            boolean recordFound = false;
            while (resultSet.next()) {
                recordFound = true;
                int id = resultSet.getInt("id");
                int stockId = resultSet.getInt("prod_id");
                String prodName = resultSet.getString("prod_name");
                int qty = resultSet.getInt("quantity");
                String date = resultSet.getDate("date").toString();
                String requestStatus = resultSet.getString("request_status");
                ShopStockDetails stockDetails = new ShopStockDetails(id, stockId, prodName, qty);
                stockDetails.setHistroyDate(date);
                stockDetails.setRequestStatus(requestStatus);
                shopStock.stockDetails(Results.SUCCESS, stockDetails);
            }
            if (!recordFound) {
                shopStock.stockDetails(Results.NO_RECORD_FOUND, null);
            }
            closeConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            shopStock.stockDetails(Results.ERROR, null);
        }
    }

    public interface CustomerDetails {
        void setCustomerName(int status, String customerName);
    }

    public static void searchCustomer(long phoneNum, CustomerDetails customer) {
        if (makeConnection() == Results.ERROR) {
            customer.setCustomerName(Results.ERROR, null);
            return;
        }

        try {
            String query = "SELECT * FROM customer_billing WHERE phone_num = ? LIMIT 1";
            pStatement = connection.prepareStatement(query);
            pStatement.setLong(1, phoneNum);
            resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                String customerName = resultSet.getString("name");
                customer.setCustomerName(Results.SUCCESS, customerName);
            } else {
                customer.setCustomerName(Results.NO_RECORD_FOUND, null);
            }
            closeConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            customer.setCustomerName(Results.ERROR, null);
        }
    }

    public static int sellProduct(long mobNum, String cusName, boolean customerExists, DefaultListModel<ShopStockDetails> stocksData, HashMap<Integer, Integer> stockQtys) {
        
        if (makeConnection() == Results.ERROR) {
            return Results.ERROR;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

        // adding customer billing details
        String billingId = "BILL_" + generateRandomInt();
        String query = "INSERT INTO customer_billing(c_billing_id, name, phone_num, billing_date, customer_insertion_date) VALUES (?, ?, ?, ?, ?)";
        try {
            pStatement = connection.prepareStatement(query);
            pStatement.setString(1, billingId);
            pStatement.setString(2, cusName);
            pStatement.setLong(3, mobNum);
            
            pStatement.setString(4, formatter.format(date));
            pStatement.setString(5, customerExists ? null : formatter.format(date));
            pStatement.addBatch();
            pStatement.executeBatch();
            
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return Results.ERROR;
        }

        // updating shop stock
        query = "UPDATE shops_stock SET stock_quantity = stock_quantity - ? WHERE stock_id = ? AND shop_id = " + shopDetails.getShopId();
        try {
            pStatement = connection.prepareStatement(query);
            for (int i = 0; i < stocksData.size(); i++) {
                ShopStockDetails item = stocksData.getElementAt(i);
                int prodId = item.getId();
                int qty = stockQtys.get(prodId);
                pStatement.setInt(1, qty);
                pStatement.setInt(2, prodId);
                pStatement.addBatch();
                if (i == stocksData.size() - 1) {
                    pStatement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return Results.ERROR;
        }
        
        // insert data into shops history
        query = "INSERT INTO shops_history (prod_id, shop_id, quantity, date, cus_id) VALUES (?, '"+shopDetails.getShopId()+"', ?, '"+formatter.format(date)+"', '"+billingId+"')";
        try {
            pStatement = connection.prepareStatement(query);
            for (int i = 0; i < stocksData.size(); i++) {
                ShopStockDetails item = stocksData.getElementAt(i);
                int prodId = item.getId();
                int quantity = stockQtys.get(prodId);
                pStatement.setInt(1, prodId);
                pStatement.setInt(2, quantity);
                pStatement.addBatch();
                if (i == stocksData.size() - 1) {
                    pStatement.executeBatch();
                    return Results.SUCCESS;
                }
            }
            return Results.FAILED;
        } catch (SQLException ex) {
            System.out.println(ex);
            closeConnection(true);
            return Results.ERROR;
        }
    }

    /**
     * connect with DB to execute the DB queries
     * 
     * @return Status Code (int)
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
     * @param closeResultSet boolean
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

    private static int generateRandomInt() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < 11; i++)
            list.add(i);
        Collections.shuffle(list);
        String num = "";
        for (int i = 0; i < 5; i++)
            num += list.get(i);
        return Integer.parseInt(num);
    }

}
