package employee;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_classes.DBQueries.CustomerDetails;
import custom_classes.DBQueries.ShopStock;
import custom_components.RoundedCornerPanel;
import custom_components.ShopStockListCellRenderer;
import custom_components.WButton;
import custom_components.WTextField;
import custom_components.GlassPanePopup.GlassPane;
import models.ShopStockDetails;
import net.miginfocom.swing.MigLayout;

public class SellProduct extends JPanel {

    MigLayout layout;
    JLabel lblHeading, lblListContainerHeading, lblCMobNum, lblCName, lblCustomerHeading;
    JPanel productListsContainer, btnContainer, customerDetailsContainer;
    JList<ShopStockDetails> stockList, sellList;
    WButton btnSendtoSellList, btnRevertToShop, btnSearch, btnFinalizeChange;
    WTextField txtCMobNum, txtCName;
    DefaultListModel<ShopStockDetails> stockModel, sellModel;
    HashMap<Integer, Integer> sellProdsQtys = new HashMap<>();
    boolean customerExists = false;
    long mobNum = 0;
    String cusName = "";

    public SellProduct() {
        initSellProduct();
        setShopStockData();
    }

    private void initSellProduct() {
        layout = new MigLayout("fillx, gapy 16");
        setLayout(layout);
        setOpaque(false);

        lblHeading = new JLabel("Sell Product");
        lblHeading.setFont(Theme.poppinsSemiboldTitleFont);
        lblHeading.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblHeading, "gaptop 10, width 100%, wrap");

        productListsContainer = new JPanel(new MigLayout("insets 16"));
        productListsContainer.setOpaque(false);
        productListsContainer.setBorder(new LineBorder(Theme.MUTED_TEXT_COLOR, 1));
        add(productListsContainer, "width 100%, height 40%, wrap");

        lblListContainerHeading = new JLabel("Select Products to Sell");
        lblListContainerHeading.setFont(Theme.poppinsMediumFont);
        lblListContainerHeading.setForeground(Theme.MUTED_TEXT_COLOR);
        productListsContainer.add(lblListContainerHeading, "width 100%, grow, span, wrap");

        // Shop Stocks List
        stockList = new JList<>();
        stockList.setFont(Theme.poppinsFont);
        stockList.setCellRenderer(new ShopStockListCellRenderer());
        stockList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        stockModel = new DefaultListModel<>();
        stockList.setModel(stockModel);
        productListsContainer.add(stockList, "width 50%, height 100%, left");

        // Buttons Container
        btnContainer = new JPanel(new MigLayout());
        btnContainer.setOpaque(false);
        productListsContainer.add(btnContainer, "gapleft 16, gapright 16, center");

        // Button Add to Shop List
        btnSendtoSellList = new WButton(">");
        btnSendtoSellList.setPadding(24, 16);
        btnSendtoSellList.setFont(Theme.poppinsMediumFont);
        btnSendtoSellList.setCornerRadius(64);
        btnSendtoSellList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProdToSellList();
            }
        });
        btnContainer.add(btnSendtoSellList, "wrap");

        // Button Remove from Shop List
        btnRevertToShop = new WButton("<");
        btnRevertToShop.setPadding(24, 16);
        btnRevertToShop.setFont(Theme.poppinsMediumFont);
        btnRevertToShop.setCornerRadius(64);
        btnRevertToShop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                revertSellList();
            }
        });
        btnContainer.add(btnRevertToShop, "gaptop 16, wrap");

        // Sell Product List
        sellList = new JList<>();
        sellList.setFont(Theme.poppinsFont);
        sellList.setCellRenderer(new ShopStockListCellRenderer());
        sellList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sellModel = new DefaultListModel<>();
        sellList.setModel(sellModel);
        productListsContainer.add(sellList, "width 50%, height 100%, left");

        // END : Product Lists Container

        // Customer Details Container
        customerDetailsContainer = new JPanel(new MigLayout("insets 16, gapy 16"));
        customerDetailsContainer.setOpaque(false);
        customerDetailsContainer.setBorder(new LineBorder(Theme.MUTED_TEXT_COLOR, 1));
        add(customerDetailsContainer, "width 100%, wrap");

        lblCustomerHeading = new JLabel("Customer Details");
        lblCustomerHeading.setFont(Theme.poppinsMediumFont);
        lblCustomerHeading.setForeground(Theme.MUTED_TEXT_COLOR);
        customerDetailsContainer.add(lblCustomerHeading, "width 100%, grow, span, wrap");

        // Customer Mobile Number
        lblCMobNum = new JLabel("Mobile Number: ");
        lblCMobNum.setFont(Theme.poppinsFont);
        lblCMobNum.setForeground(Theme.MUTED_TEXT_COLOR);
        customerDetailsContainer.add(lblCMobNum, "grow");

        // Mob. Num. Text Field
        txtCMobNum = new WTextField();
        txtCMobNum.setFont(Theme.poppinsFont);
        txtCMobNum.setRadius(16);
        customerDetailsContainer.add(txtCMobNum, "width 100%, gapleft 16, grow");

        // Search Cutomer Button
        btnSearch = new WButton("Search");
        btnSearch.setBgColor(Theme.PRIMARY);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(Theme.poppinsFont);
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                searchCustomer();
            }
        });
        customerDetailsContainer.add(btnSearch, "gapleft 16, right, wrap");

        // Customer Name
        lblCName = new JLabel("Customer Name: ");
        lblCName.setFont(Theme.poppinsFont);
        lblCName.setForeground(Theme.MUTED_TEXT_COLOR);
        customerDetailsContainer.add(lblCName, "grow");

        txtCName = new WTextField();
        txtCName.setFont(Theme.poppinsFont);
        txtCName.setRadius(16);
        customerDetailsContainer.add(txtCName, "width 100%, gapleft 16, grow");
        // END : Customer Details Container

        btnFinalizeChange = new WButton("MAKE A SELL");
        btnFinalizeChange.setFont(Theme.poppinsFont);
        btnFinalizeChange.setBgColor(Theme.PRIMARY);
        btnFinalizeChange.setForeground(Color.WHITE);
        btnFinalizeChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showConfirmPanel();
            }
        });
        add(btnFinalizeChange, "grow, wrap");
    }

    private void setShopStockData() {
        stockModel.removeAllElements();
        DBQueries.getShopAvailableStock(new ShopStock() {
            public void stockDetails(int status, ShopStockDetails stockData) {
                if (status == Results.SUCCESS) {
                    stockModel.addElement(stockData);
                }
            }
        });
    }

    private void addProdToSellList() {
        List<ShopStockDetails> selectedItems = stockList.getSelectedValuesList();
        for (ShopStockDetails selectedItem : selectedItems) {
            String prodName = selectedItem.getProductName();
            int prodQty = selectedItem.getQuantity();
            inputQty(prodName, prodQty, new InputQuantity() {
                public void setInsertedQuantity(int qty) {
                    sellProdsQtys.put(selectedItem.getId(), qty);
                    sellModel.addElement(selectedItem);
                    stockModel.removeElement(selectedItem);
                    sellList.revalidate();
                    stockList.revalidate();
                }
            });
        }
    }

    private void revertSellList() {
        List<ShopStockDetails> selectedItems = sellList.getSelectedValuesList();
        for (ShopStockDetails selectedItem : selectedItems) {
            sellProdsQtys.remove(selectedItem.getId());
            stockModel.addElement(selectedItem);
            sellModel.removeElement(selectedItem);
        }
        stockList.revalidate();
        sellList.revalidate();
    }

    private interface InputQuantity {
        void setInsertedQuantity(int qty);
    }

    /**
     * function to ask for the quantity to be added
     * in shops for the selected products
     * 
     * @param prodName      name of the product
     * @param stockQty      quantity present in warehouse
     * @param inputQuantity InputQuantity interface
     */
    private void inputQty(String prodName, int stockQty, InputQuantity inputQuantity) {

        RoundedCornerPanel inputPanel = new RoundedCornerPanel();
        inputPanel.setLayout(new MigLayout("insets 20, gapy 16"));

        JLabel title = new JLabel("Enter Quantity");
        title.setFont(Theme.poppinsMediumFont);
        title.setForeground(Theme.MUTED_TEXT_COLOR);
        inputPanel.add(title, "width 100%, span, grow, wrap");

        JLabel lblProdName = new JLabel("Product: ");
        lblProdName.setFont(Theme.poppinsFont);
        lblProdName.setForeground(Theme.MUTED_TEXT_COLOR);
        inputPanel.add(lblProdName, "grow");

        JLabel txtProdName = new JLabel(prodName);
        txtProdName.setFont(Theme.poppinsFont);
        inputPanel.add(txtProdName, "width 100%, gapleft 16, grow, wrap");

        JLabel lblQty = new JLabel("Available Quantity: ");
        lblQty.setFont(Theme.poppinsFont);
        lblQty.setForeground(Theme.MUTED_TEXT_COLOR);
        inputPanel.add(lblQty, "grow");

        JLabel txtQty = new JLabel(Integer.toString(stockQty));
        txtQty.setFont(Theme.poppinsFont);
        inputPanel.add(txtQty, "width 100%, gapleft 16, grow, wrap");

        JLabel lblEnterQty = new JLabel("Enter Product Quantity for Shop");
        lblEnterQty.setFont(Theme.poppinsFont);
        lblEnterQty.setForeground(Theme.MUTED_TEXT_COLOR);
        inputPanel.add(lblEnterQty, "grow, span, wrap");

        WTextField txtEnterQty = new WTextField();
        txtEnterQty.setRadius(16);
        inputPanel.add(txtEnterQty, "width 100%, grow, span, wrap");

        WButton btnSetQty = new WButton("Add Product");
        btnSetQty.setBgColor(Theme.PRIMARY);
        btnSetQty.setForeground(Color.WHITE);
        btnSetQty.setFont(Theme.poppinsFont);
        btnSetQty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    int quantity = Integer.parseInt(txtEnterQty.getText());
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(getParent(), "Quantity should be greater than 0");
                    } else if (quantity > stockQty) {
                        JOptionPane.showMessageDialog(getParent(),
                                "Warehouse doesn\'t have this much quantity, you shold not exceed more than: "
                                        + stockQty);
                    } else {
                        inputQuantity.setInsertedQuantity(quantity);
                        GlassPane.closePopupLast();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(getParent(), "Please enter a valid value");
                }
            }
        });
        inputPanel.add(btnSetQty, "span, right, left");

        GlassPane.showPopup(inputPanel);
    }

    /**
     * function to search the customer
     * by the entered phone number
     */
    private void searchCustomer() {
        String input = txtCMobNum.getText();
        if (input.length() < 10) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
            return;
        }

        try {
            mobNum = Long.parseLong(input);
        } catch (NumberFormatException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Invalid Value Entered");
            return;
        }

        // Getting Customer Details
        DBQueries.searchCustomer(mobNum, new CustomerDetails() {
            public void setCustomerName(int status, String customerName) {
                if (status == Results.SUCCESS) {
                    // Customer Found
                    customerExists = true;
                    cusName = customerName;
                    txtCName.setText(customerName);
                    txtCName.setEditable(false);
                } else if (status == Results.NO_RECORD_FOUND) {
                    JOptionPane.showMessageDialog(null,
                            "No Customer Found, New Customer will be added automatically at the time of finalizing Order");
                    txtCName.setText("");
                    txtCName.setEditable(true);
                    cusName = "";
                    customerExists = false;
                } else if (status == Results.ERROR) {
                    JOptionPane.showMessageDialog(null, 
                    "An Error Occured, please try again later");
                    mobNum = 0;
                    customerExists = false;
                }
            }
        });

    }

    // Show Final Changes
    private void showConfirmPanel() {

        int numItems = sellModel.size();
        if (numItems == 0) {
            JOptionPane.showMessageDialog(getParent(),
                    "Please select products from Warehouse Stock to be added in Shop Stocks");
            return;
        }

        cusName = txtCName.getText();
        if (cusName == "" || cusName == null || mobNum == 0) {
            JOptionPane.showMessageDialog(getParent(), "Please Insert Customer Details");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(getParent(),
                "Are you sure you want to add these products to the selected shop?", "Add Stock",
                JOptionPane.YES_NO_OPTION);

        if (confirmation != 0)
            return;


        RoundedCornerPanel panel = new RoundedCornerPanel();
        panel.setLayout(new MigLayout("fillx, insets 16"));
        
        // Heading
        JLabel lblHeading = new JLabel("Sell Products");
        lblHeading.setFont(Theme.poppinsSemiboldTitleFont);
        panel.add(lblHeading, "width 100%, grow, span, wrap");

        JPanel prodsList = new JPanel(new MigLayout("gapy 8"));
        prodsList.setOpaque(false);
        panel.add(prodsList, "width 100%, grow, wrap");

        for (int i = 0; i < numItems; i++) {
            ShopStockDetails item = sellModel.getElementAt(i);
            String prodName = item.getProductName();
            int qty = sellProdsQtys.get(item.getId());

            JLabel lblProdName = new JLabel(prodName);
            lblProdName.setFont(Theme.poppinsFont);
            prodsList.add(lblProdName, "width 90%, grow");

            JLabel lblQty = new JLabel(Integer.toString(qty));
            lblQty.setFont(Theme.poppinsFont);
            prodsList.add(lblQty, "width 18%, grow, wrap");
        }

        JPanel btnContainer = new JPanel(new MigLayout("insets 0"));
        panel.add(btnContainer, "right");

        WButton btnAddStock = new WButton("Confirm Stock Changes");
        btnAddStock.setFont(Theme.poppinsFont);
        btnAddStock.setBgColor(Theme.PRIMARY);
        btnAddStock.setForeground(Color.WHITE);
        btnAddStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                sellProducts();
            }
        });
        btnContainer.add(btnAddStock);

        WButton btnCancel = new WButton("Cancel");
        btnCancel.setFont(Theme.poppinsFont);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                GlassPane.closePopupLast();
            }
        });
        btnContainer.add(btnCancel, "gapleft 16");

        GlassPane.showPopup(panel);
        
    }

    private void sellProducts() {
        int status = DBQueries.sellProduct(mobNum, cusName, customerExists, sellModel, sellProdsQtys);
        System.out.println(status);
        String msg = "";
        if (status == Results.SUCCESS) {
            msg = "Product(s) sold successfully";
        } else if (status == Results.FAILED) {
            msg = "Failed to sell product";
        } else {
            msg = "Some error occured, please try again";
        }
        JOptionPane.showMessageDialog(getParent(), msg);
    }
}
