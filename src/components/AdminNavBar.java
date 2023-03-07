package components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.Main;

import javax.swing.JFrame;
import javax.swing.JLabel;

import custom_classes.Theme;
import custom_components.MenuList;
import custom_components.WButton;
import models.MenuItemModel;
import net.miginfocom.swing.MigLayout;

public class AdminNavBar extends JPanel {

    JPanel header;
    JLabel appFirstName;
    JLabel appSecondName;
    MenuList menu;

    private int x;
    private int y;

    public AdminNavBar() {
        initializeNav();
    }

    private void initializeNav() {
        setLayout(new MigLayout("al center, gap 0"));
        setOpaque(false);

        // Top Header (App Name)
        header = new JPanel(new MigLayout("al center, gap 0"));
        header.setBackground(Theme.TRANSPARENT_COLOR);
        add(header, "width 100%, wrap");

        appFirstName = new JLabel("The Wi");
        appFirstName.setForeground(Color.WHITE);
        appFirstName.setFont(Theme.latoHeadlineLightFont);
        header.add(appFirstName, "gaptop 20, gapbottom 20");

        appSecondName = new JLabel("MS");
        appSecondName.setForeground(Color.WHITE);
        appSecondName.setFont(Theme.latoHeadlineBoldFont);
        header.add(appSecondName, "gaptop 20, gapbottom 20");

        // Menu
        menu = new MenuList();
        menu.addItem(new MenuItemModel("Dashboard", "/icons/ic_dashboard.png"));
        menu.addItem(new MenuItemModel("Shops", "/icons/ic_shop.png"));
        menu.addItem(new MenuItemModel("Employees", "/icons/ic_employees.png"));
        menu.addItem(new MenuItemModel("Categories", "/icons/ic_categories.png"));
        menu.addItem(new MenuItemModel("Stocks", "/icons/ic_stock.png"));

        // TODO : Make "Dashboard" menu item selected as default

        menu.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Main.changeItem(menu.getSelectedIndex());
                }
            }
        });

        add(menu, "width 100%, wrap");

        // Close Button
        WButton btnClose = new WButton("EXIT");
        btnClose.setFont(Theme.poppinsFont);
        btnClose.setBgColor(Theme.PRIMARY);
        btnClose.setForeground(Color.WHITE);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        add(btnClose, "width 240, gaptop 10, pos 0.5al 1al");

    }

    // Function to change the location of App by draggin it
    public void initMoving(JFrame frame) {
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        header.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
            }
        });
    }


    // Graident Background
    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, Theme.PRIMARY, 0, getHeight(), Theme.SECONDARY);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2d.fillRect(getWidth() - 16, 0, getWidth(), getHeight());
        super.paintChildren(g);
    }

}
