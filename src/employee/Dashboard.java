package employee;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import custom_classes.DBQueries;
import custom_classes.Theme;
import custom_components.CounterSection;
import net.miginfocom.swing.MigLayout;

public class Dashboard extends JPanel {

    MigLayout layout;
    JPanel headerPanel, topPanel, gap;
    JLabel lblUserName;
    CounterSection soldProducts, newCustomers, salesAmt;

    public Dashboard() {
        initDashboard();
    }

    private void initDashboard() {
        // JPanel Properties
        layout = new MigLayout();
        setLayout(layout);
        setOpaque(false);

        // Header (User Name)
        headerPanel = new JPanel(new MigLayout("gap 10"));
        headerPanel.setOpaque(false);
        // User Name
        String userName = DBQueries.currentUser.getName();
        lblUserName = new JLabel("Hello, " + userName);
        lblUserName.setFont(Theme.latoFont);
        lblUserName.setForeground(Color.BLACK);
        headerPanel.add(lblUserName);

        add(headerPanel, "wrap, al right center");
        // --- END : Header ---

        // Top Panel
        topPanel = new JPanel(new MigLayout("insets 0"));
        topPanel.setOpaque(false);

        // Current Month Sold Products
        soldProducts = new CounterSection("0", "Current Month Sell", "/icons/ic_stock_blue.png", Theme.SECONDARY);
        topPanel.add(soldProducts, "width 32%, height 100%");
        // --- END : Current Month Sold Products ---

        // Gap
        gap = new JPanel();
        gap.setOpaque(false);
        topPanel.add(gap, "width 2%");

        // New Customers
        newCustomers = new CounterSection("0", "New Customers", "", Theme.WARNING_COLOR);
        topPanel.add(newCustomers, "width 32%, height 100%");
        // --- END : New Customers ---

        // Gap
        gap = new JPanel();
        gap.setOpaque(false);
        topPanel.add(gap, "width 2%");

        // Sales Amount
        salesAmt = new CounterSection("0", "Current Month Sales Amount", "", Theme.SUCCESS_COLOR);
        topPanel.add(salesAmt, "width 32%, height 100%");
        // --- END : Sales Amount ---

        add(topPanel, "width 100%, height 150, gaptop 10");
        // --- END : Top Panel ---



    }
}
