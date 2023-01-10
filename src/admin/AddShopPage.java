package admin;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_components.RoundedCornerPanel;
import custom_components.WButton;
import custom_components.WTextField;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

public class AddShopPage extends JFrame {

    JLabel lblTitle, lblSName, lblSAddress, lblEmpID;
    WTextField shopName, empID;
    JTextArea shopAddress;
    WButton btnClose, btnAddShop;

    public AddShopPage() {
        init();
    }

    private void init() {
        setPreferredSize(new Dimension((int) Theme.FRAME_SIZE.getWidth() - 200, 500));
        setMinimumSize(getPreferredSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(Theme.TRANSPARENT_COLOR);
        setLocationRelativeTo(null);

        RoundedCornerPanel panel = new RoundedCornerPanel();
        panel.setCornerRadius(16);
        panel.setBgColor(Theme.BG_COLOR);
        panel.setLayout(new MigLayout("insets 20, gapx 20, gapy 10"));
        add(panel);

        /*
         * => TODO : Shop CAN'T be added if no employee is selected to manage the shop
         * => TODO : Search Functionality for Employee
         */

        // Label Title
        lblTitle = new JLabel("Add New Shop");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        panel.add(lblTitle, "left, gapbottom 30");

        // Close Button
        btnClose = new WButton();
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Icon icon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 28, Theme.PRIMARY);
        btnClose.setIcon(icon);
        btnClose.setCornerRadius(128);
        btnClose.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnClose.setBgColor(Theme.TRANSPARENT_COLOR);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddShopPage.this.setVisible(false);

            }
        });
        panel.add(btnClose, "right, gapbottom 30, wrap");

        // Label Shop Name
        lblSName = new JLabel("Shop Name");
        lblSName.setFont(Theme.latoFont);
        panel.add(lblSName, "width 50%");

        // Label Employee ID
        lblEmpID = new JLabel("Employee ID");
        lblEmpID.setFont(Theme.latoFont);
        panel.add(lblEmpID, "width 50%, wrap");

        // Field Shop Name
        shopName = new WTextField();
        shopName.setRadius(16);
        shopName.setBgColor(Theme.TF_COLOR);
        panel.add(shopName, "width 50%, height 50");

        // Field Employee ID
        empID = new WTextField();
        empID.setRadius(16);
        empID.setBgColor(Theme.TF_COLOR);
        panel.add(empID, "width 50%, height 50, wrap");

        // Label Shop Address
        lblSAddress = new JLabel("Shop Address");
        lblSAddress.setFont(Theme.latoFont);
        panel.add(lblSAddress, "gaptop 10, wrap");

        // Field Shop Address
        shopAddress = new JTextArea();
        shopAddress.setFont(Theme.latoFont);
        shopAddress.setForeground(Theme.TEXT_COLOR);
        shopAddress.setColumns(10);
        shopAddress.setRows(10);
        panel.add(shopAddress, "width 100%, height 200, span, wrap");

        // Add Shop Button
        btnAddShop = new WButton("ADD SHOP");
        btnAddShop.setBgColor(Theme.SECONDARY);
        btnAddShop.setForeground(Color.WHITE);
        btnAddShop.setFont(Theme.latoFont);
        btnAddShop.setCornerRadius(16);
        btnAddShop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get Field Values
                String name = shopName.getText();
                String eID = empID.getText();
                String address = shopAddress.getText();

                // Validating Fields
                if (name.equals("") || eID.equals("") || address.equals("")) {
                    JOptionPane.showMessageDialog(panel, "Please fill all details");
                    return;
                }

                // Adding Shop Data to DB
                int result = DBQueries.addNewShop(name, Integer.parseInt(eID), address);
                if (result == Results.SUCCESS) {    // Shop Added
                    JOptionPane.showMessageDialog(panel, "Shop added successfully");
                    // resetting fields
                    shopName.setText("");
                    empID.setText("");
                    shopAddress.setText("");
                } else {    // Failed to Add Shop
                    JOptionPane.showMessageDialog(panel, "Sorry, Shop can't be added\nplease try again later");
                }
            }
        });
        panel.add(btnAddShop, "height 40, span, right, gaptop 10");
    }

}
