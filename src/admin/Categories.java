package admin;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import custom_classes.Theme;
import custom_components.WButton;
import custom_components.WTable;
import net.miginfocom.swing.MigLayout;

public class Categories extends JPanel {

    MigLayout layout;
    WButton btnAddNewCtg;
    JLabel title;
    WTable cateTable;

    public Categories() {
        init();
        String[] cols = { "ID", "Name" };
        String[] rows = {
            "1",
            "Electronics"
        };
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        tableModel.addRow(rows);
        cateTable.setModel(tableModel);

        System.out.println(tableModel.getDataVector());
    }

    private void init() {
        layout = new MigLayout("fillx");
        setLayout(layout);
        setOpaque(false);

        // Add Categories Button
        btnAddNewCtg = new WButton("Add Category");
        btnAddNewCtg.setFont(Theme.latoFont);
        btnAddNewCtg.setForeground(Color.WHITE);
        btnAddNewCtg.setBgColor(Theme.PRIMARY);
        btnAddNewCtg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Open Add Category Page");
            }
        });

        add(btnAddNewCtg, "height 40, gaptop 10, right, wrap");

        // Title
        title = new JLabel("Categories");
        title.setFont(Theme.poppinsSemiboldTitleFont);
        title.setForeground(Theme.MUTED_TEXT_COLOR);
        add(title, "gaptop 5, wrap");

        // Categories Table
        cateTable = new WTable();
        JScrollPane tableContainer = new JScrollPane(cateTable);
        add(tableContainer, "width 100%, wrap");

    }

}
