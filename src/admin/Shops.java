package admin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import custom_classes.Theme;
import custom_components.WButton;
import net.miginfocom.swing.MigLayout;

public class Shops extends JPanel {

    MigLayout layout;
    WButton btnAddShop;
    JLabel lblTitle;

    public Shops() {
        init();
    }

    private void init() {
        // JPanel Properties
        layout = new MigLayout("fillx");
        setLayout(layout);
        setOpaque(false);

        // Add Button to Add Shops
        btnAddShop = new WButton("Add Shop");
        btnAddShop.setFont(Theme.headerFont);
        btnAddShop.setBgColor(Theme.PRIMARY);
        btnAddShop.setForeground(Color.WHITE);
        add(btnAddShop, "height 40, gaptop 10, al right center, wrap");

        // Add Shop Button Functionality
        btnAddShop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Add Shop Page
            }
        });

        // Title
        lblTitle = new JLabel("Shops");
        lblTitle.setFont(Theme.titleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "gaptop 5, wrap");

        // Shops List Table
        String columns[] = { "ID", "EMPLOYEE ID", "SHOP NAME", "SHOP ADDRESS" };
        String data[][] = {
                { "1", "143", "Storesy", "Mumbai" },
        };
        JTable shopsTable = new JTable(data, columns);
        shopsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableContainer = new JScrollPane(shopsTable);
        add(tableContainer, "width 100%, height 250, wrap");
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Theme.TRANSPARENT_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2d.fillRect(0, 0, getWidth() - 16, getHeight());
        super.paintChildren(g);
    }
}
