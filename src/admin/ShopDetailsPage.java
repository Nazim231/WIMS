package admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.Main;
import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_classes.DBQueries.ShopStock;
import custom_components.WButton;
import custom_components.WTable;
import models.ShopDetails;
import models.ShopStockDetails;
import net.miginfocom.swing.MigLayout;

public class ShopDetailsPage extends JPanel {

    private int shopId;

    MigLayout layout;
    JLabel lblName, lblId, lblEmp, lblAddress, lblShopStock;
    JLabel txtId, txtEmp, txtAddress;
    JLabel lblNoStockFound;
    WTable stockTable;

    public ShopDetailsPage(int shopId) {
        this.shopId = shopId;
        initShopDetailsPage();
        setShopData();
        setShopStockData();
    }

    private void initShopDetailsPage() {

        layout = new MigLayout("gapy 16, fillx");
        setLayout(layout);
        setOpaque(false);

        // Shop Name
        lblName = new JLabel("Shop Name");
        lblName.setForeground(Theme.SECONDARY);
        lblName.setFont(Theme.poppinsSemiboldTitleFont);
        add(lblName, "width 100%, gaptop 16, gapbottom 16, span, wrap");

        // Shop ID
        lblId = new JLabel("ID: ");
        lblId.setFont(Theme.poppinsMediumFont);
        lblId.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblId, "grow");

        txtId = new JLabel();
        txtId.setFont(Theme.poppinsFont);
        add(txtId, "gap 16 0, grow, wrap");

        // Shop Address
        lblAddress = new JLabel("Address: ");
        lblAddress.setFont(Theme.poppinsMediumFont);
        lblAddress.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblAddress, "grow");

        txtAddress = new JLabel();
        txtAddress.setFont(Theme.poppinsFont);
        add(txtAddress, "gap 16 0, grow, wrap");

        // Assigned Employee
        lblEmp = new JLabel("Assigned Person: ");
        lblEmp.setBorder(new EmptyBorder(0, 0, 0, 2));
        lblEmp.setFont(Theme.poppinsMediumFont);
        lblEmp.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblEmp, "grow");

        txtEmp = new JLabel();
        txtEmp.setFont(Theme.poppinsFont);
        add(txtEmp, "gap 16 0, grow, wrap");

        // Shop Stock
        lblShopStock = new JLabel("Stock Details");
        lblShopStock.setFont(Theme.poppinsSemiboldTitleFont);
        lblShopStock.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblShopStock, "gaptop 32, bottom");

        // Add Stock Button
        WButton btnAddStock = new WButton("Add Stock");
        btnAddStock.setBgColor(Theme.PRIMARY);
        btnAddStock.setForeground(Color.WHITE);
        btnAddStock.setFont(Theme.poppinsFont);
        btnAddStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                AddShopStockPage addShopStock = new AddShopStockPage(shopId);
                Main.setScreen(addShopStock);
            }
        });
        add(btnAddStock, "bottom, gapbottom 8, right, wrap");

        stockTable = new WTable();
        JScrollPane tableContainer = new JScrollPane(stockTable);
        tableContainer.getViewport().setBackground(Theme.BG_COLOR);
        tableContainer.setBorder(null);
        add(tableContainer, "width 100%, span, grow, wrap");

        lblNoStockFound = new JLabel("Oops! seems like this shop doesn\'t contain any stock");
        lblNoStockFound.setFont(Theme.poppinsFont);
        lblNoStockFound.setForeground(Color.RED);
        lblNoStockFound.setHorizontalAlignment(SwingConstants.CENTER);
        lblNoStockFound.setVisible(false);
        add(lblNoStockFound, "gaptop 8, width 100%, span, grow, wrap");
    }

    private void setShopData() {

        ShopDetails details = DBQueries.getShopDetails(shopId);
        if (details != null) {
            lblName.setText(details.getShopName());
            txtId.setText(Integer.toString(details.getShopId()));
            txtAddress.setText(details.getShopAddress());
            txtEmp.setText("[" + details.getEmpId() + "] " + details.getEmpName());
        }

    }

    private void setShopStockData() {

        String[] cols = {"ID", "Product", "Quantity"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        stockTable.setModel(tableModel);

        DBQueries.getShopAvailableStock(shopId, new ShopStock() {
            public void stockDetails(int status, ShopStockDetails stockData) {
                if (status == Results.SUCCESS) {
                    lblNoStockFound.setVisible(false);
                    String[] rowData = {
                        Integer.toString(stockData.getId()),
                        stockData.getProductName(),
                        Integer.toString(stockData.getQuantity())
                    };
                    tableModel.addRow(rowData);
                    stockTable.revalidate();
                } else if (status == Results.NO_RECORD_FOUND) {
                    lblNoStockFound.setVisible(true);
                }
            }
        });
    }
}
