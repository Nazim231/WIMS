package app;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import admin.*;
import components.AdminNavBar;
import components.MainPanel;
import custom_classes.Theme;
import custom_components.GlassPanePopup.GlassPane;
import net.miginfocom.swing.MigLayout;

public class Main extends JFrame {

    String role;
    static MainPanel panel;
    static ArrayList<JPanel> adminComponents = new ArrayList<>(); // To store all Admin Menu Items Panels
    
    public Main(String role) {
        this.role = role;
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
        if (role.equals("admin")) { // Admin Panel
            AdminNavBar navBar = new AdminNavBar();
            navBar.initMoving(Main.this); // Dragging Functionality
            bg.add(navBar, "width 260, height 100%");

            // Adding Admin Menu Panel(s)
            adminComponents.add(new Dashboard());
            adminComponents.add(new Shops());
            adminComponents.add(new Employees());
            adminComponents.add(new Categories());
        } else { // Employee Panel
            System.out.println("Other");
        }

        // Right Panel
        panel = new MainPanel();
        bg.add(panel, "width 100% - 260, height 100%");
        panel.add(adminComponents.get(0), "width 100%, height 100%");
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Fook & Feel Exception: " + ex);
        }

        new Main("admin").setVisible(true);
    }


    // Change frame by selecting Menu Item from admin/employee panel.
    public static void changeItem(int index) {
        if (index >= adminComponents.size())
            return;
        
        panel.remove(panel.getComponent(0)); // Removing Current Panel
        panel.add(adminComponents.get(index), "width 100%, height 100%");
        panel.revalidate();
    }
}
