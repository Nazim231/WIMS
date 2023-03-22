package admin;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import custom_classes.Theme;
import custom_components.WButton;
import custom_components.WTable;
import net.miginfocom.swing.MigLayout;

public class Stocks extends JPanel {

    JLabel lblTitle;
    WButton btnAddStock;
    WTable tableStocks;

    public Stocks() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, debug"));
        setOpaque(false);

        // Button Add Stock
        btnAddStock = new WButton("Add Stock");
        btnAddStock.setBgColor(Theme.PRIMARY);
        btnAddStock.setForeground(Color.WHITE);
        btnAddStock.setFont(Theme.latoFont);
        btnAddStock.setCornerRadius(16);
        add(btnAddStock, "height 40, gaptop 10, right, wrap");

        // Title
        lblTitle = new JLabel("Stocks");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "gaptop 5, wrap");

        // Stocks Table
        tableStocks = new WTable();
        JScrollPane tableContainer = new JScrollPane(tableStocks);
        add(tableContainer, "width 100%, wrap");
    }

}
