package admin;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.Icon;
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
import custom_components.GlassPanePopup.GlassPane;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

public class AddShopPage extends RoundedCornerPanel {

    JLabel lblTitle, lblSName, lblSAddress, lblSelectEmp;
    WTextField shopName, employee;
    JTextArea shopAddress;
    WButton btnClose, btnAddShop;

    int empID;
    String empName;
    boolean focusGained;

    public AddShopPage() {
        init();
    }

    private void init() {
        setBgColor(Theme.BG_COLOR);
        setLayout(new MigLayout("insets 20, gapx 20, gapy 10"));

        /*
         * => TODO : Shop CAN'T be added if no employee is selected to manage the shop
         * => TODO : Search Functionality for Employee
         */

        // Label Title
        lblTitle = new JLabel("Add New Shop");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "left, gapbottom 30");

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
                GlassPane.closePopupLast();
            }
        });
        add(btnClose, "right, gapbottom 30, wrap");

        // Label Shop Name
        lblSName = new JLabel("Shop Name");
        lblSName.setFont(Theme.latoFont);
        add(lblSName, "width 50%");

        // Label Employee
        lblSelectEmp = new JLabel("Select Employee");
        lblSelectEmp.setFont(Theme.latoFont);
        add(lblSelectEmp, "width 50%, wrap");

        // Field Shop Name
        shopName = new WTextField();
        shopName.setRadius(16);
        shopName.grabFocus();
        add(shopName, "width 50%, height 50");

        // Choose Employee
        employee = new WTextField();
        employee.setRadius(16);
        employee.setEditable(false);
        employee.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                if (!focusGained) {
                    focusGained = true; // this flag is used to ignore bug (running code twice) in focusGained()
                    GlassPane.showPopup(new SearchEmployeePage((id, name) -> {
                        empID = Integer.parseInt(id);
                        empName = name;
                        if (!name.equals(""))
                            employee.setText(name);
                    }));
                    shopAddress.grabFocus();
                }
            }
        });
        add(employee, "width 50%, height 50, wrap");

        // Label Shop Address
        lblSAddress = new JLabel("Shop Address");
        lblSAddress.setFont(Theme.latoFont);
        add(lblSAddress, "gaptop 10, wrap");

        // Field Shop Address
        shopAddress = new JTextArea();
        shopAddress.setFont(Theme.latoFont);
        shopAddress.setForeground(Theme.TEXT_COLOR);
        shopAddress.setColumns(10);
        shopAddress.setRows(10);
        shopAddress.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focusGained = false;
            }
        });
        add(shopAddress, "width 100%, height 200, span, wrap");

        // Add Shop Button
        btnAddShop = new WButton("ADD SHOP");
        btnAddShop.setBgColor(Theme.SECONDARY);
        btnAddShop.setForeground(Color.WHITE);
        btnAddShop.setFont(Theme.latoFont);
        btnAddShop.setCornerRadius(16);
        btnAddShop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addShopData();
            }
        });
        add(btnAddShop, "height 40, span, right, gaptop 10");
    }

    // function to add shop to Database
    private void addShopData() {
        // Get Field Values
        String name = shopName.getText();
        String address = shopAddress.getText();

        // Validating Fields
        if (name.equals("") || empName.equals("") || address.equals("") || empID == -1) {
            JOptionPane.showMessageDialog(getParent(), "Please fill all details");
            return;
        }

        // Adding Shop Data to DB
        int result = DBQueries.addNewShop(name, empID, address);
        if (result == Results.SUCCESS) {
            // Shop Added
            JOptionPane.showMessageDialog(getParent(), "Shop added successfully");
            // resetting fields
            shopName.setText("");
            employee.setText("");
            shopAddress.setText("");
            empID = -1;
            empName = "";
            GlassPane.closePopupLast();
        } else {
            // Failed to Add Shop
            JOptionPane.showMessageDialog(getParent(), "Sorry, Shop can't be added\nplease try again later");
        }
    }

}
