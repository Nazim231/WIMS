package app;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import admin.*;
import components.AdminNavBar;
import components.EmployeeNavBar;
import components.MainPanel;
import custom_classes.DBQueries;
import custom_classes.Theme;
import custom_components.GlassPanePopup.GlassPane;
import employee.*;
import employee.Dashboard;
import net.miginfocom.swing.MigLayout;

public class Main extends JFrame {

    static MainPanel panel;
    static ArrayList<JPanel> navBarComponents = new ArrayList<>(); // To store all Menu Items Panels

    public Main() {
        initMain();
        GlassPane.install(this);
    }

    private void initMain() {
        // Frame Properties
        setPreferredSize(Theme.FRAME_SIZE);
        setSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setBackground(Theme.TRANSPARENT_COLOR);

        JLayeredPane bg = new JLayeredPane();
        bg.setLayout(new MigLayout("insets 0, gap 0"));
        bg.setOpaque(true);
        bg.setBackground(Theme.TRANSPARENT_COLOR);
        add(bg);

        // Setting the Layout based on the User Role
        String userRole = DBQueries.currentUser.getRole();
        if (userRole.equals("admin")) { // Admin Panel
            AdminNavBar navBar = new AdminNavBar();
            navBar.initMoving(Main.this); // Dragging Functionality
            bg.add(navBar, "width 260, height 100%");

            // Adding Admin Menu Panel(s)
            navBarComponents.add(new Dashboard());
            navBarComponents.add(new Shops());
            navBarComponents.add(new Employees());
            navBarComponents.add(new Categories());
            navBarComponents.add(new Stocks());
        } else if (userRole.equals("emp")) { // Employee Panel
            EmployeeNavBar navBar = new EmployeeNavBar();
            navBar.initMoving(Main.this);   // Dragging Functionality
            bg.add(navBar, "width 260, height 100%");

            // Adding Employee Menu Panel(s)
            navBarComponents.add(new employee.Dashboard());
            // navBarComponents.add(new Stocks());
            // navBarComponents.add(new Stocks());

        } else {
            JOptionPane.showMessageDialog(this, "Error in fetching User Profile, kindly log in again");
            setVisible(false);
            new Login().setVisible(true);
        }

        // Right Panel
        panel = new MainPanel();
        bg.add(panel, "width 100% - 260, height 100%");
        panel.add(navBarComponents.get(0), "width 100%, height 100%");
    }

    // Change frame by selecting Menu Item from admin/employee panel.
    public static void changeItem(int index) {
        if (index >= navBarComponents.size())
            return;
        
        panel.remove(panel.getComponent(0)); // Removing Current Panel
        panel.add(navBarComponents.get(index), "width 100%, height 100%");
        panel.revalidate();
    }
}
