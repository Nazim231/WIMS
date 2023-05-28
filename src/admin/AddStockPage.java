package admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_components.RoundedCornerPanel;
import custom_components.WButton;
import custom_components.WTextField;
import custom_components.GlassPanePopup.GlassPane;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import models.StockDetails;
import net.miginfocom.swing.MigLayout;

public class AddStockPage extends RoundedCornerPanel {

    JComboBox<String> comboCategory;
    JLabel lblTitle, lblProdName, lblCategory, lblBrand, lblMRP, lblCPrice, lblSPrice, lblQty, lblTQty, TQty;
    WButton btnAddStock, btnClose;
    WTextField txtProdName, txtBrand, txtMRP, txtCostPrice, txtSellingPrice, txtQty;

    public AddStockPage() {
        init();
        DBQueries.getCategories((queryStatus, categoryName) -> {
            if (queryStatus == Results.SUCCESS)
                comboCategory.addItem(categoryName);
            else if (queryStatus == Results.ERROR)
                JOptionPane.showMessageDialog(getParent(), "Error in fetching categories, please try again");
        });
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 20, gapx 20"));

        // Title
        lblTitle = new JLabel("Add Product");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "width 50%, left, gapbottom 30");

        // Close Button
        btnClose = new WButton();
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Icon closeIcon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 24, Theme.PRIMARY);
        btnClose.setIcon(closeIcon);
        btnClose.setBgColor(Theme.TRANSPARENT_COLOR);
        btnClose.setCornerRadius(128);
        btnClose.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPane.closePopupLast();
            }
        });
        add(btnClose, "right, gapbottom 30,  width 40, height 40, wrap");

        lblProdName = new JLabel("Product Name");
        lblProdName.setFont(Theme.latoFont);
        add(lblProdName, "span");

        txtProdName = new WTextField();
        txtProdName.setRadius(16);
        add(txtProdName, "span, width 100%, height 40, gapbottom 15, wrap");

        lblCategory = new JLabel("Category");
        lblCategory.setFont(Theme.latoFont);
        add(lblCategory, "width 50%");

        lblBrand = new JLabel("Brand Name");
        lblBrand.setFont(Theme.latoFont);
        add(lblBrand, "width 50%, wrap");

        comboCategory = new JComboBox<>();
        comboCategory.addItem("Select Category");
        comboCategory.setFont(Theme.latoFont);
        comboCategory.setLightWeightPopupEnabled(false);
        add(comboCategory, "width 50%, height 40");

        txtBrand = new WTextField();
        txtBrand.setRadius(16);
        add(txtBrand, "width 50%, height 40, wrap");

        lblMRP = new JLabel("MRP (Per Unit)");
        lblMRP.setFont(Theme.latoFont);
        add(lblMRP, "gaptop 15");

        lblSPrice = new JLabel("Selling Price (Per Unit)");
        lblSPrice.setFont(Theme.latoFont);
        add(lblSPrice, "wrap, gaptop 15");

        txtMRP = new WTextField();
        txtMRP.setRadius(16);
        add(txtMRP, "width 50%, height 40");

        txtSellingPrice = new WTextField();
        txtSellingPrice.setRadius(16);
        add(txtSellingPrice, "width 50%, height 40, wrap");

        lblCPrice = new JLabel("Cost Price (Whole Stock)");
        lblCPrice.setFont(Theme.latoFont);
        add(lblCPrice, "gaptop 15");

        lblQty = new JLabel("Quantity");
        lblQty.setFont(Theme.latoFont);
        add(lblQty, "wrap, gaptop 15");

        txtCostPrice = new WTextField();
        txtCostPrice.setRadius(16);
        add(txtCostPrice, "width 50%, height 40");

        txtQty = new WTextField();
        txtQty.setRadius(16);
        add(txtQty, "width 50%, height 40, wrap");

        btnAddStock = new WButton("Add Stock");
        btnAddStock.setCornerRadius(16);
        btnAddStock.setBgColor(Theme.SECONDARY);
        btnAddStock.setFont(Theme.latoFont);
        btnAddStock.setForeground(Color.WHITE);
        btnAddStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStock();
            }
        });
        add(btnAddStock, "gaptop 10, width 150, height 40, right, span");

    }

    private void addStock() {

        // validating fields
        String prodName = txtProdName.getText();
        int category = comboCategory.getSelectedIndex();
        String brandName = txtBrand.getText();
        int mrp = 0, cost = 0, sellingPrice = 0, qty = 0;
        try {
            mrp = Integer.parseInt(txtMRP.getText());
            cost = Integer.parseInt(txtCostPrice.getText());
            sellingPrice = Integer.parseInt(txtSellingPrice.getText());
            qty = Integer.parseInt(txtQty.getText());
        } catch (NumberFormatException ex) {
        }

        if (prodName.equals("")) {
            showMsg("Please enter product name");
            return;
        } else if (category == 0) {
            showMsg("Please select product category");
            return;
        } else if (brandName.equals("")) {
            showMsg("Please enter product brand name");
            return;
        } else if (mrp <= 0) {
            showMsg("Product MRP is invalid");
            return;
        } else if (cost < 0) {
            showMsg("Product Cost Price is invalid");
            return;
        } else if (sellingPrice <= 0) {
            showMsg("Product selling price is invalid");
            return;
        } else if (qty <= 0) {
            showMsg("Product Quantity is invalid");
            return;
        }

        StockDetails stockDetails = new StockDetails(prodName, brandName, category, mrp, sellingPrice, cost, qty);

        // Adding Stock to DB
        int result = DBQueries.addStock(stockDetails);

        if (result == Results.SUCCESS) {
            showMsg("Stock Added Successfully");
            GlassPane.closePopupLast();
        } else if (result == Results.ERROR) {
            showMsg("An Error Occured,\nUnable to add stock, please try again");
        } else if (result == Results.FAILED) {
            showMsg("Failed to add stock, please try again");
        }
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

}
