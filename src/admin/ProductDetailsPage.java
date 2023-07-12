package admin;

import javax.swing.JLabel;
import javax.swing.JPanel;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_classes.DBQueries.ProductDetail;
import models.StockDetails;
import net.miginfocom.swing.MigLayout;

public class ProductDetailsPage extends JPanel {

    private int stockId;

    MigLayout layout;
    JLabel lblName, lblId, lblCategory, lblQty, lblMRP, lblSelling, lblCost, lblLastRefill, lblSKU;
    JLabel txtId, txtCategory, txtQty, txtMRP, txtSelling, txtCost, txtLastRefill, txtSKU;

    public ProductDetailsPage(int stockId) {
        this.stockId = stockId;
        initProductDetails();
        setProductDetails();
    }

    private void initProductDetails() {
        layout = new MigLayout("gapy 16, insets 16 32");
        setLayout(layout);
        setOpaque(false);

        // Product Name
        lblName = new JLabel("Product Name");
        lblName.setFont(Theme.poppinsSemiboldTitleFont);
        lblName.setForeground(Theme.SECONDARY);
        add(lblName, "width 100%, span, gaptop 16, gapbottom 16, wrap");

        // Product ID
        lblId = new JLabel("ID: ");
        lblId.setFont(Theme.poppinsMediumFont);
        lblId.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblId);

        txtId = new JLabel();
        txtId.setFont(Theme.poppinsFont);
        add(txtId, "gap 16 0, wrap");

        // Product Category
        lblCategory = new JLabel("Category: ");
        lblCategory.setFont(Theme.poppinsMediumFont);
        lblCategory.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblCategory);

        txtCategory = new JLabel();
        txtCategory.setFont(Theme.poppinsFont);
        add(txtCategory, "gap 16 0, wrap");

        // Product Quantity
        lblQty = new JLabel("Quantity: ");
        lblQty.setFont(Theme.poppinsMediumFont);
        lblQty.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblQty);

        txtQty = new JLabel();
        txtQty.setFont(Theme.poppinsFont);
        add(txtQty, "gap 16 0, wrap");

        // Product MRP
        lblMRP = new JLabel("MRP: ");
        lblMRP.setFont(Theme.poppinsMediumFont);
        lblMRP.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblMRP);

        txtMRP = new JLabel();
        txtMRP.setFont(Theme.poppinsFont);
        add(txtMRP, "gap 16 0, wrap");

        // Product Selling Price
        lblSelling = new JLabel("Selling Price: ");
        lblSelling.setFont(Theme.poppinsMediumFont);
        lblSelling.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblSelling);

        txtSelling = new JLabel();
        txtSelling.setFont(Theme.poppinsFont);
        add(txtSelling, "gap 16 0, wrap");

        // Product Cost Price
        lblCost = new JLabel("Cost Price: ");
        lblCost.setFont(Theme.poppinsMediumFont);
        lblCost.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblCost);

        txtCost = new JLabel();
        txtCost.setFont(Theme.poppinsFont);
        add(txtCost, "gap 16 0, wrap");

        // Product Last Refill Date
        lblLastRefill = new JLabel("Last Refill Date: ");
        lblLastRefill.setFont(Theme.poppinsMediumFont);
        lblLastRefill.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblLastRefill);

        txtLastRefill = new JLabel();
        txtLastRefill.setFont(Theme.poppinsFont);
        add(txtLastRefill, "gap 16 0, wrap");

        // Product SKU
        lblSKU = new JLabel("SKU: ");
        lblSKU.setFont(Theme.poppinsMediumFont);
        lblSKU.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblSKU);

        txtSKU = new JLabel();
        txtSKU.setFont(Theme.poppinsFont);
        add(txtSKU, "gap 16 0, wrap");
    }

    private void setProductDetails() {
        DBQueries.getProductDetails(stockId, new ProductDetail() {
            public void getProductDetail(int status, StockDetails details) {
                if (status == Results.SUCCESS) {
                    lblName.setText(details.getProdName());
                    txtId.setText(Integer.toString(details.getProdId()));
                    txtCategory.setText(details.getCategoryName());
                    txtQty.setText(Integer.toString(details.getQty()));
                    txtMRP.setText(Integer.toString(details.getMrp()));
                    txtSelling.setText(Integer.toString(details.getSellingPrice()));
                    txtCost.setText(Integer.toString(details.getCostPrice()));
                    txtLastRefill.setText(details.getLastRefillDate());
                }
            }
        });
    }

}
