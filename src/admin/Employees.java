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
import javax.swing.table.DefaultTableModel;

import custom_classes.DBQueries;
import custom_classes.Theme;
import custom_components.WButton;
import custom_components.WTable;
import custom_components.GlassPanePopup.GlassPane;
import net.miginfocom.swing.MigLayout;

public class Employees extends JPanel {

    MigLayout layout;
    JLabel lblTitle;
    WButton btnAddEmp;
    WTable empTable;

    public Employees() {
        init();
        // Adding Employees List to Employee Table
        DefaultTableModel tableModel = DBQueries.getEmployeesList();
        if (tableModel != null)
            empTable.setModel(tableModel);
        else {
            String[] cols = { "ID", "Name", "Email"};
            tableModel = new DefaultTableModel(cols, 0);
            empTable.setModel(tableModel);
            empTable.revalidate();
        }
    }

    private void init() {
        layout = new MigLayout("fillx");
        setLayout(layout);
        setOpaque(false);

        // Add Employee Button
        btnAddEmp = new WButton("Add Employee");
        btnAddEmp.setFont(Theme.latoFont);
        btnAddEmp.setForeground(Color.WHITE);
        btnAddEmp.setBgColor(Theme.PRIMARY);
        btnAddEmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPane.showPopup(new AddEmployeePage());
            }
        });
        add(btnAddEmp, "height 40, gaptop 10, right, wrap");

        // Title
        lblTitle = new JLabel("Employees");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "gaptop 5, wrap");

        // Employee Table
        empTable = new WTable();
        empTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableContainer = new JScrollPane(empTable);
        // tableContainer.setBorder(null);
        add(tableContainer, "width 100%, height 250, wrap");

    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Theme.TRANSPARENT_COLOR);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2.fillRect(0, 0, getWidth() - 16, getHeight());
        super.paintChildren(g);
    }
}
