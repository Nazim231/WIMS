package employee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_classes.DBQueries.ShopStock;
import custom_components.WButton;
import custom_components.WTable;
import models.ShopStockDetails;
import net.miginfocom.swing.MigLayout;

public class Stocks extends JPanel {

    MigLayout layout;
    JPanel topPanel, buttonsContainer;
    JLabel lblHeading, lblNoData;
    WButton btnCurrentStock, btnStockStatus, btnHistory;
    WTable dataTable;
    JScrollPane tableContainer;

    public Stocks() {
        initStocks();
        setShopAvailableStock();
    }

    private void initStocks() {
        layout = new MigLayout("fillx");
        setLayout(layout);
        setOpaque(false);

        /**
         * LAYOUT IDEA =>
         * i. TOP ->
         * One Heading for the Current Page Title and 3 pill buttons
         * which will change the structure of the Main Table with data,
         * buttons are listed below >>>
         * a. Current Stock (Default Selected)
         * b. Stock Status -- Show the Request Stock Status (Approved / Rejected)
         * c. History -- Show current shop stock request and sell history
         * 
         * ii. MAIN (Table) ->
         * This Table will show the data related to current shop stock,
         * desired columns are listed below >>>
         * a. Current Stock Table >>
         * | ID | Product | Quantity | SKU |
         * b. Stock Status >>
         * | Request ID | Product | Status | Remark |
         * c. History >>
         * | Product | Date | Type | Quantity |
         **/

        // Top Panel
        topPanel = new JPanel(new MigLayout("insets 0"));
        topPanel.setOpaque(false);

        // Heading
        lblHeading = new JLabel("Shop Stock");
        lblHeading.setFont(Theme.poppinsSemiboldTitleFont);
        lblHeading.setForeground(Theme.MUTED_TEXT_COLOR);
        topPanel.add(lblHeading, "gapbottom 10, width 100%, wrap");
        // --- END : Heading ---

        // Buttons Container
        buttonsContainer = new JPanel(new MigLayout("gapx 16"));
        buttonsContainer.setOpaque(false);

        // Button Current Stock
        btnCurrentStock = new WButton("Available");
        btnCurrentStock.setCornerRadius(40);
        btnCurrentStock.setBorderWidth(1);
        btnCurrentStock.setBorderColor(Theme.PRIMARY);
        btnCurrentStock.setFont(Theme.poppinsFont);
        btnCurrentStock.setPadding(16, 8);
        btnCurrentStock.setForeground(Theme.BG_COLOR);
        btnCurrentStock.setBgColor(Theme.PRIMARY);
        btnCurrentStock.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCurrentStock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setActiveButton(evt);
                setShopAvailableStock();
            }
        });
        buttonsContainer.add(btnCurrentStock);
        // --- END : Button Current Stock ---

        // Button Stock Status
        btnStockStatus = new WButton("Stock Status");
        btnStockStatus.setCornerRadius(40);
        btnStockStatus.setBorderWidth(1);
        btnStockStatus.setBorderColor(Theme.PRIMARY);
        btnStockStatus.setFont(Theme.poppinsFont);
        btnStockStatus.setPadding(16, 8);
        btnStockStatus.setForeground(Theme.PRIMARY);
        btnStockStatus.setBgColor(Theme.BG_COLOR);
        btnStockStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStockStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setActiveButton(evt);
                setShopRequestStatuses();
            }
        });
        buttonsContainer.add(btnStockStatus);
        // --- END : Button Stock Status ---

        // Button History
        btnHistory = new WButton("History");
        btnHistory.setCornerRadius(40);
        btnHistory.setBorderWidth(1);
        btnHistory.setBorderColor(Theme.PRIMARY);
        btnHistory.setFont(Theme.poppinsFont);
        btnHistory.setPadding(16, 8);
        btnHistory.setForeground(Theme.PRIMARY);
        btnHistory.setBgColor(Theme.BG_COLOR);
        btnHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setActiveButton(evt);
            }
        });
        buttonsContainer.add(btnHistory);
        // --- END : Button History ---

        topPanel.add(buttonsContainer);
        // --- END : Buttons Container ---

        add(topPanel, "width 100%, gaptop 16, wrap");
        // --- END : Top Panel ---

        // No Data Label
        lblNoData = new JLabel();
        lblNoData.setFont(Theme.poppinsFont);
        lblNoData.setForeground(Color.RED);
        lblNoData.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblNoData, "width 100%, wrap");

        // Table
        dataTable = new WTable();
        tableContainer = new JScrollPane(dataTable);
        tableContainer.getViewport().setBackground(Theme.BG_COLOR);
        tableContainer.setBorder(null);
        tableContainer.setVisible(false);
        add(tableContainer, "width 100%, height 100%, wrap");

    }

    private void showTable(boolean flag) {
        if (flag) {
            tableContainer.setVisible(true);
            lblNoData.setVisible(false);
            tableContainer.revalidate();
        } else {
            tableContainer.setVisible(false);
            lblNoData.setVisible(true);
        }
        revalidate();
    }

    private void setShopAvailableStock() {
        showTable(true);
        String[] cols = { "ID", "Product Name", "Quantity" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        DBQueries.getShopAvailableStock(new ShopStock() {
            public void stockDetails(int status, ShopStockDetails stockData) {
                if (status == Results.SUCCESS) {
                    // insert the data into the Table
                    String[] rowData = {
                            Integer.toString(stockData.getId()),
                            stockData.getProductName(),
                            Integer.toString(stockData.getQuantity())
                    };
                    tableModel.addRow(rowData);
                    dataTable.setModel(tableModel);
                    dataTable.revalidate();
                } else if (status == Results.NO_RECORD_FOUND) {
                    // shop doesn't have any stock history
                    lblNoData.setText("No stock found for this shop, please request stock from warehouse");
                    showTable(false);
                } else {
                    // if any error occured
                    JOptionPane.showMessageDialog(getParent(), "Error in fetching data");
                    lblNoData.setText("Error in fetching Data");
                    showTable(false);
                }
            }

        });
    }

    private void setShopRequestStatuses() {
        showTable(true);
        String[] cols = { "ID", "Product", "Quantity", "Date of Request", "Status" };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        DBQueries.getStockRequestStatus(new ShopStock() {
            public void stockDetails(int status, ShopStockDetails stockData) {
                if (status == Results.SUCCESS) {
                    String[] rowData = {
                            Integer.toString(stockData.getId()),
                            stockData.getProductName(),
                            Integer.toString(stockData.getQuantity()),
                            stockData.getHistoryDate(),
                            stockData.getRequestStatus()
                    };
                    tableModel.addRow(rowData);
                    dataTable.setModel(tableModel);
                    dataTable.revalidate();
                } else if (status == Results.NO_RECORD_FOUND) {
                    // shop doesn't have any stock history
                    lblNoData.setText("No stock found for this shop, please request stock from warehouse");
                    showTable(false);
                } else {
                    // if any error occured
                    JOptionPane.showMessageDialog(getParent(), "Error in fetching data");
                    lblNoData.setText("Error in fetcing data");
                    showTable(false);
                }
            }
        });
    }

    private void setActiveButton(ActionEvent evt) {
        WButton clickedBtn = (WButton) evt.getSource();
        int x = clickedBtn.getX(), y = clickedBtn.getY();
        List<Component> components = Arrays.asList(buttonsContainer.getComponents());
        int clickedIndex = Arrays.asList(buttonsContainer.getComponents()).indexOf(buttonsContainer.getComponentAt(x,
                y));
        int i = 0;
        for (Component comp : components) {
            if (comp instanceof WButton) {
                WButton btn = (WButton) comp;
                if (clickedIndex == i) {
                    btn.setBgColor(Theme.PRIMARY);
                    btn.setForeground(Color.WHITE);
                    btn.revalidate();
                } else {
                    btn.setBgColor(Theme.BG_COLOR);
                    btn.setForeground(Theme.PRIMARY);
                    btn.revalidate();
                }
            }
            i++;
        }
    }
}
