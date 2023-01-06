package app;

import javax.swing.JLayeredPane;

import admin.Dashboard;
import components.AdminNavBar;
import components.MainPanel;
import custom_classes.Theme;
import net.miginfocom.swing.MigLayout;

public class Main extends javax.swing.JFrame {

    String role;

    public Main(String role) {
        this.role = role;
        initializeDashboard();
    }

    private void initializeDashboard() {
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
        if (role.equals("admin")) { // Admin Panel
            AdminNavBar navBar = new AdminNavBar();
            navBar.initMoving(Main.this);
            bg.add(navBar, "width 260, height 100%");
        } else { // Employee Panel
            System.out.println("Other");
        }

        // Right Panel
        MainPanel panel = new MainPanel();
        bg.add(panel, "width 100% - 260, height 100%");
        panel.add(new Dashboard(), "width 100%, height 100%");
    }

    public static void main(String args[]) {
        new Main("admin").setVisible(true);
    }
}
