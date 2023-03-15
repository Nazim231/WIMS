package admin;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
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
import net.miginfocom.swing.MigLayout;

public class AddEmployeePage extends RoundedCornerPanel {

    // TODO : Add Security Question & Answer field

    JLabel lblTitle, lblEmpName, lblEmpEmail;
    WTextField txtEmpName, txtEmpEmail;
    WButton btnClose, btnAddEmp;

    public AddEmployeePage() {
        init();
    }

    private void init() {
        setBgColor(Theme.BG_COLOR);
        setLayout(new MigLayout("debug, fillx, insets 20, gapx 20, gapy 10"));

        // Title
        lblTitle = new JLabel("Employee Details");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "gaptop 10, gapbottom 10");

        // Close Button
        btnClose = new WButton();
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Icon closeIcon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 24, Theme.PRIMARY);
        btnClose.setIcon(closeIcon);
        btnClose.setCornerRadius(128);
        btnClose.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnClose.setBgColor(Theme.TRANSPARENT_COLOR);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPane.closePopupLast();
            }
        });
        add(btnClose, "right top, wrap");

        // Label Employee Name
        lblEmpName = new JLabel("Employee Name");
        lblEmpName.setFont(Theme.latoFont);
        add(lblEmpName, "width 50%");

        // Label Employee Email
        lblEmpEmail = new JLabel("Employee Email");
        lblEmpEmail.setFont(Theme.latoFont);
        add(lblEmpEmail, "width 50%, wrap");

        // Text Field Employee Name
        txtEmpName = new WTextField();
        txtEmpName.setRadius(16);
        txtEmpName.setBgColor(Theme.TF_COLOR);
        add(txtEmpName, "width 50%");

        // Text Field Employee Email
        txtEmpEmail = new WTextField();
        txtEmpEmail.setRadius(16);
        txtEmpEmail.setBgColor(Theme.TF_COLOR);
        add(txtEmpEmail, "width 50%, wrap");

        // Add Employee Button
        btnAddEmp = new WButton("Add Employee");
        btnAddEmp.setBgColor(Theme.PRIMARY);
        btnAddEmp.setForeground(Color.WHITE);
        btnAddEmp.setFont(Theme.latoFont);

        btnAddEmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String name = txtEmpName.getText();
                String email = txtEmpEmail.getText();

                if (name.equals("") || email.equals("")) {
                    JOptionPane.showMessageDialog(getParent(), "All Fields are required");
                    return;
                }

                DBQueries.addEmployee(name, email, (result, password) -> {
                    if (result != Results.FAILED && result != Results.ERROR) {
                        JOptionPane.showMessageDialog(getParent(),
                                "Employee Added Successfully\nEmployee ID: " + result + "\nPassword: " + password);
                    } else {
                        JOptionPane.showMessageDialog(getParent(), "Failed to Add Employee, please try again later");
                    }
                });
            }
        });

        add(btnAddEmp, "span, gaptop 10, right");

    }

}