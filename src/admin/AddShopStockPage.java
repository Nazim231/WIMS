package admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_components.RoundedCornerPanel;
import custom_components.StockListCellRenderer;
import custom_components.WButton;
import custom_components.WTextField;
import custom_components.GlassPanePopup.GlassPane;
import models.ShopDetails;
import models.StockDetails;
import net.miginfocom.swing.MigLayout;

public class AddShopStockPage extends JPanel {

    MigLayout layout;
    JPanel transferStocksContainer, btnContainer;
    JLabel lblTitle, lblShop, lblAddress, txtShop, txtAddress;
    WButton btnSendtoShopStock, btnRevertToWH;
    JList<StockDetails> whStock, shopStock;
    DefaultListModel<StockDetails> whModel, shopModel;
    int shopId;
    HashMap<Integer, Integer> shopStockHashMap = new HashMap<>();

    public AddShopStockPage(int shopId) {
        this.shopId = shopId;
        initAddShopStockPage();
        setShopDetails();
        setWarehouseStockData();
    }

    private void initAddShopStockPage() {

        layout = new MigLayout("fillx, gapy 16");
        setLayout(layout);
        setOpaque(false);

        // Title
        lblTitle = new JLabel("Add Stock");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        add(lblTitle, "width 100%, gaptop 16, grow, span, wrap");

        // Shop Details
        lblShop = new JLabel("Shop Name: ");
        lblShop.setFont(Theme.poppinsMediumFont);
        lblShop.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblShop, "grow");

        txtShop = new JLabel("Name");
        txtShop.setFont(Theme.poppinsFont);
        txtShop.setForeground(Theme.TEXT_COLOR);
        add(txtShop, "gapleft 16, grow, wrap");

        // Shop Address
        lblAddress = new JLabel("Shop Address: ");
        lblAddress.setFont(Theme.poppinsMediumFont);
        lblAddress.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblAddress, "grow");

        txtAddress = new JLabel();
        txtAddress.setFont(Theme.poppinsFont);
        txtAddress.setForeground(Theme.TEXT_COLOR);
        add(txtAddress, "gapleft 16, grow, wrap");

        // Stocks List Containers
        transferStocksContainer = new JPanel(new MigLayout("insets 0"));
        transferStocksContainer.setOpaque(false);
        add(transferStocksContainer, "span, grow, width 100%, height 100%, wrap");

        JLabel lblWHStock = new JLabel("Warehouse Stock");
        lblWHStock.setFont(Theme.poppinsMediumFont);
        lblWHStock.setForeground(Theme.MUTED_TEXT_COLOR);
        transferStocksContainer.add(lblWHStock, "width 50%, span 2, gaptop 16, grow");

        JLabel lblShopStock = new JLabel("Shop Stock");
        lblShopStock.setFont(Theme.poppinsMediumFont);
        lblShopStock.setForeground(Theme.MUTED_TEXT_COLOR);
        transferStocksContainer.add(lblShopStock, "width 50%, gaptop 16, grow, wrap");

        // Warehouse Stocks List
        whStock = new JList<>();
        whStock.setFont(Theme.poppinsFont);
        whStock.setCellRenderer(new StockListCellRenderer());
        whStock.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        whModel = new DefaultListModel<>();
        whStock.setModel(whModel);
        transferStocksContainer.add(whStock, "width 50%, height 100%, left");

        // Buttons Container
        btnContainer = new JPanel(new MigLayout());
        btnContainer.setOpaque(false);
        transferStocksContainer.add(btnContainer, "gapleft 16, gapright 16, center");

        // Button Add to Shop List
        btnSendtoShopStock = new WButton(">");
        btnSendtoShopStock.setPadding(24, 16);
        btnSendtoShopStock.setFont(Theme.poppinsMediumFont);
        btnSendtoShopStock.setCornerRadius(64);
        btnSendtoShopStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStockToShopList();
            }
        });
        btnContainer.add(btnSendtoShopStock, "wrap");

        // Button Remove from Shop List
        btnRevertToWH = new WButton("<");
        btnRevertToWH.setPadding(24, 16);
        btnRevertToWH.setFont(Theme.poppinsMediumFont);
        btnRevertToWH.setCornerRadius(64);
        btnRevertToWH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                revertShopStock();
            }
        });
        btnContainer.add(btnRevertToWH, "gaptop 16, wrap");

        // Shop Stock List
        shopStock = new JList<>();
        shopStock.setCellRenderer(new StockListCellRenderer());
        shopStock.setFont(Theme.poppinsFont);
        shopModel = new DefaultListModel<>();
        shopStock.setModel(shopModel);
        shopStock.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Product ID: " + shopStock.getSelectedValue().getProdId());
                }
            }
        });
        transferStocksContainer.add(shopStock, "width 50%, height 100%, right");

        // Button Finalize Change
        WButton btnFinalizeChanges = new WButton("Add Stock to Shop");
        btnFinalizeChanges.setFont(Theme.poppinsFont);
        btnFinalizeChanges.setBgColor(Theme.PRIMARY);
        btnFinalizeChanges.setForeground(Color.WHITE);
        btnFinalizeChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                finalizeChanges();
            }
        });
        add(btnFinalizeChanges, "right, span, wrap");
    }

    // Function to set the shop details when the page in initialized
    private void setShopDetails() {
        ShopDetails shopDetails = DBQueries.getShopDetails(shopId);
        if (shopDetails != null) {
            txtShop.setText(shopDetails.getShopName());
            txtAddress.setText(shopDetails.getShopAddress());
        }
    }

    // function to set the warehouse stocks in warehouse stock list
    private void setWarehouseStockData() {
        whModel.removeAllElements();
        DBQueries.getWarehouseStock(new DBQueries.WarehouseStock() {
            public void warehouseStock(int status, StockDetails stockData) {
                if (status == Results.SUCCESS) {
                    whModel.addElement(stockData);
                    whStock.revalidate();
                }
            }
        });
    }

    /**
     * function to add the selected products
     * from warehouse list to shops list
     */
    private void addStockToShopList() {
        List<StockDetails> selectedItems = whStock.getSelectedValuesList();
        for (StockDetails selectedItem : selectedItems) {
            String prodName = selectedItem.getProdName();
            int prodQty = selectedItem.getQty();
            inputQty(prodName, prodQty, new InputQuantity() {
                public void setInsertedQuantity(int qty) {
                    shopStockHashMap.put(selectedItem.getProdId(), qty);
                    shopModel.addElement(selectedItem);
                    whModel.removeElement(selectedItem);
                    shopStock.revalidate();
                    whStock.revalidate();
                }
            });
        }
    }

    /**
     * function to remove the selected products
     * from shops list
     */
    private void revertShopStock() {
        List<StockDetails> selectedItems = shopStock.getSelectedValuesList();
        for (StockDetails selectedItem : selectedItems) {
            shopStockHashMap.remove(selectedItem.getProdId());
            whModel.addElement(selectedItem);
            shopModel.removeElement(selectedItem);
        }
        whStock.revalidate();
        shopStock.revalidate();
    }

    /**
     * function to show the last confirmation
     * before adding the products into the shops
     */
    private void finalizeChanges() {
        int numItems = shopModel.size();
        if (numItems == 0) {
            JOptionPane.showMessageDialog(getParent(),
                    "Please select products from Warehouse Stock to be added in Shop Stocks");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(getParent(),
                "Are you sure you want to add these products to the selected shop?", "Add Stock",
                JOptionPane.YES_NO_OPTION);

        if (confirmation != 0)
            return;

        // Creating Layout for previewing final changes
        RoundedCornerPanel panel = new RoundedCornerPanel();
        panel.setLayout(new MigLayout("insets 20, gapy 8"));

        JLabel lblChanges = new JLabel("Add Stock to Shop");
        lblChanges.setFont(Theme.poppinsSemiboldTitleFont);
        lblChanges.setForeground(Theme.MUTED_TEXT_COLOR);
        panel.add(lblChanges, "width 100%, grow, wrap");

        JPanel prodsList = new JPanel(new MigLayout("gapy 8"));
        prodsList.setOpaque(false);
        panel.add(prodsList, "width 100%, grow, wrap");

        for (int i = 0; i < numItems; i++) {
            StockDetails item = shopModel.getElementAt(i);
            String prodName = item.getProdName();
            int qty = shopStockHashMap.get(item.getProdId());

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
                addStock();
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

    private interface InputQuantity {
        void setInsertedQuantity(int qty);
    }

    /**
     * function to ask for the quantity to be added
     * in shops for the selected products
     * 
     * @param prodName      name of the product
     * @param whQty         quantity present in warehouse
     * @param inputQuantity InputQuantity interface
     */
    private void inputQty(String prodName, int whQty, InputQuantity inputQuantity) {

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

        JLabel txtQty = new JLabel(Integer.toString(whQty));
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
                    } else if (quantity > whQty) {
                        JOptionPane.showMessageDialog(getParent(),
                                "Warehouse doesn\'t have this much quantity, you shold not exceed more than: " + whQty);
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
     * function to add the stock into the shop
     */
    private void addStock() {
        int status = DBQueries.addShopStock(shopId, shopModel, shopStockHashMap);
        String msg = "";
        switch (status) {
            case Results.SUCCESS:
                msg = "Products Inserted Successfully";
                // whModel.removeAllElements();
                setWarehouseStockData();
                shopModel.removeAllElements();
                shopStock.revalidate();
                shopStockHashMap = new HashMap<>();
                break;
            case Results.FAILED:
                msg = "Failed to Insert Stock, please try again";
                break;
            default:
                msg = "Some error occured while adding stock, please try again later";
        }
        JOptionPane.showMessageDialog(this.getParent(), msg);
        GlassPane.closePopupLast();
    }
}
