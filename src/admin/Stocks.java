package admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import app.Main;
import custom_classes.DBQueries;
import custom_classes.Theme;
import custom_components.WButton;
import custom_components.WTable;
import custom_components.GlassPanePopup.GlassPane;
import net.miginfocom.swing.MigLayout;

public class Stocks extends JPanel {

    JLabel lblTitle;
    WButton btnAddStock;
    private static WTable tableStocks;

    public Stocks() {
        init();
       setTableData();
    }

    private void init() {
        setLayout(new MigLayout("fillx"));
        setOpaque(false);

        // Button Add Stock
        btnAddStock = new WButton("Add Stock");
        btnAddStock.setBgColor(Theme.PRIMARY);
        btnAddStock.setForeground(Color.WHITE);
        btnAddStock.setFont(Theme.latoFont);
        btnAddStock.setCornerRadius(16);
        btnAddStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPane.showPopup(new AddStockPage());
            }
        });
        add(btnAddStock, "height 40, gaptop 10, right, wrap");

        // Title
        lblTitle = new JLabel("Stocks");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "gaptop 5, wrap");

        // Stocks Table
        tableStocks = new WTable();
        tableStocks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    int row = tableStocks.getSelectedRow();
                    int id = Integer.parseInt(tableStocks.getValueAt(row, 0).toString());
                    ProductDetailsPage prodDetailsPage = new ProductDetailsPage(id);
                    Main.setScreen(prodDetailsPage);
                }
            }
        });
        JScrollPane tableContainer = new JScrollPane(tableStocks);
        tableContainer.getViewport().setBackground(Theme.BG_COLOR);
        tableContainer.setBorder(null);
        add(tableContainer, "width 100%, gaptop 8, wrap");
    }

    public static void setTableData() {
         // Adding Stocks List to Stocks Table
        DefaultTableModel tableModel = DBQueries.getStocksList();
        if (tableModel != null)
            tableStocks.setModel(tableModel);
        else {
            String[] cols = { "ID", "Name", "Category", "Brand", "MRP", "Price", "Quantity" };
            tableModel = new DefaultTableModel(cols, 0);
            tableStocks.setModel(tableModel);
            tableStocks.revalidate();
        }
    }
}
