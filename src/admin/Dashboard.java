package admin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import custom_classes.Theme;
import custom_components.CounterSection;
import net.miginfocom.swing.MigLayout;

public class Dashboard extends JPanel {

    // variable declaration
    MigLayout layout;
    JPanel headerPanel, topPanel, gap;
    JLabel lblUserGoodName;
    CounterSection panelProductSold, panelProfitEarned, panelRevenueEarned;

    public Dashboard() {
        initDashboard();
    }

    private void initDashboard() {
        // JPanel properties
        layout = new MigLayout();
        setLayout(layout);
        setOpaque(false);

        // Header (User Good Name)
        headerPanel = new JPanel(new MigLayout("gap 10"));
        headerPanel.setOpaque(false);
        // User Good Name
        String userName = "John Doe";   // TODO : Change "John Doe" to User Real Name
        lblUserGoodName = new JLabel("Hello, " + userName);
        lblUserGoodName.setFont(Theme.headerFont);
        lblUserGoodName.setForeground(Color.BLACK);
        headerPanel.add(lblUserGoodName);
        // TODO : Add Profile Icon

        add(headerPanel, "wrap, al right center"); // Added Header Panel
        // --- END : Header ---

        // Top Panel
        topPanel = new JPanel(new MigLayout("insets 0"));
        topPanel.setOpaque(false);

        // Monthly Product Sold
        panelProductSold = new CounterSection("0", "Monthly Products Sold", "/icons/ic_stock_blue.png", Theme.SECONDARY);
        topPanel.add(panelProductSold, "width 32%, height 100%");
        // --- END : Monthly Product Sold ---

        // Gap
        gap = new JPanel();
        gap.setOpaque(false);
        topPanel.add(gap, "width 2%");

        // Monthly Profit Earned
        panelProfitEarned = new CounterSection("0", "Monthly Profit Earned", "/icons/ic_profit.png", Theme.SUCCESS_COLOR);
        topPanel.add(panelProfitEarned, "width 32%, height 100%");
        // --- END : Monthly Profit Earned ---

        // Gap
        gap = new JPanel();
        gap.setOpaque(false);
        topPanel.add(gap, "width 2%");

        // Monthly Revenue Earned
        panelRevenueEarned = new CounterSection("0", "Monthly Revenue Earned", "/icons/ic_revenue.png", Theme.WARNING_COLOR);
        topPanel.add(panelRevenueEarned, "width 32%, height 100%");
        // --- END : Monthly Revenue Earned

        add(topPanel, "width 100%, height 150, gaptop 10");
        // --- END : Top Panel ---

    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Theme.TRANSPARENT_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2d.fillRect(0, 0, getWidth() - 16, getHeight());
        super.paintChildren(g);
    }
}
