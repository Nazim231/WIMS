package admin;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

public class AddEmployeePage extends JFrame {

    // TODO : Add Security Question & Answer field

    JLabel lblTitle, lblEmpName, lblEmpEmail;
    WTextField txtEmpName, txtEmpEmail;
    WButton btnClose, btnAddEmp;

    public AddEmployeePage() {
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
        panel.setLayout(new MigLayout("fillx, insets 20, gapx 20, gapy 10"));
        add(panel);

        // Title
        lblTitle = new JLabel("Employee Details");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        panel.add(lblTitle, "gaptop 10, gapbottom 10");

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
                AddEmployeePage.this.setVisible(false);
            }
        });
        panel.add(btnClose, "right top, wrap");

        // Label Employee Name
        lblEmpName = new JLabel("Employee Name");
        lblEmpName.setFont(Theme.latoFont);
        panel.add(lblEmpName, "width 50%");

        // Label Employee Email
        lblEmpEmail = new JLabel("Employee Email");
        lblEmpEmail.setFont(Theme.latoFont);
        panel.add(lblEmpEmail, "width 50%, wrap");

        // Text Field Employee Name
        txtEmpName = new WTextField();
        txtEmpName.setRadius(16);
        txtEmpName.setBgColor(Theme.TF_COLOR);
        panel.add(txtEmpName, "width 50%");

        // Text Field Employee Email
        txtEmpEmail = new WTextField();
        txtEmpEmail.setRadius(16);
        txtEmpEmail.setBgColor(Theme.TF_COLOR);
        panel.add(txtEmpEmail, "width 50%, wrap");

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
                    JOptionPane.showMessageDialog(panel, "All Fields are required");
                    return;
                }

                DBQueries.addEmployee(name, email, (result, password) -> {
                    if (result != Results.FAILED && result != Results.ERROR) {
                        JOptionPane.showMessageDialog(panel, "Employee Added Successfully\nEmployee ID: " + result + "\nPassword: " + password);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to Add Employee, please try again later");
                    }
                });
            }
        });

        panel.add(btnAddEmp, "span, gaptop 10, right");

    }

}