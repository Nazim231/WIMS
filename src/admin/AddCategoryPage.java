package admin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import custom_classes.DBQueries;
import custom_classes.Results;
import custom_classes.Theme;
import custom_components.GlassPanePopup.GlassPane;
import custom_components.RoundedCornerPanel;
import custom_components.WTextField;
import custom_components.WButton;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

public class AddCategoryPage extends RoundedCornerPanel {

    JLabel lblTitle, lblCategoryName;
    WButton btnClose, btnAddCategory;
    WTextField txtCategoryName;

    public AddCategoryPage() {
        setLayout(new MigLayout("fillx, insets 20, gapx 20, gapy 10"));

        // Title
        lblTitle = new JLabel("Add Category");
        lblTitle.setFont(Theme.poppinsSemiboldTitleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        add(lblTitle, "width 50%, gaptop 10, gapbottom 10");

        // Close Button
        btnClose = new WButton();
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Icon closeIcon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 24, Theme.PRIMARY);
        btnClose.setIcon(closeIcon);
        btnClose.setCornerRadius(128);
        btnClose.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnClose.setBgColor(Theme.TRANSPARENT_COLOR);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                GlassPane.closePopupLast();
            }
        });
        add(btnClose, "right top, wrap");

        // Label Category Name
        lblCategoryName = new JLabel("Category Name");
        lblCategoryName.setFont(Theme.latoFont);
        add(lblCategoryName, "wrap");

        txtCategoryName = new WTextField();
        txtCategoryName.setRadius(16);
        txtCategoryName.setFont(Theme.latoFont);
        txtCategoryName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });
        add(txtCategoryName, "width 80%, height 50");

        btnAddCategory = new WButton("Add");
        btnAddCategory.setCornerRadius(16);
        btnAddCategory.setBgColor(Theme.SECONDARY);
        btnAddCategory.setFont(Theme.latoFont);
        btnAddCategory.setForeground(Color.WHITE);
        btnAddCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            };
        });
        add(btnAddCategory, "width 20%, height 50");

    }

    // function to add category
    private void addCategory() {
        String cName = txtCategoryName.getText();

        if (cName.equals("")) {
            JOptionPane.showMessageDialog(getParent(), "Please Enter Category Name");
            return;
        }

        // Adding Category to DB
        int result = DBQueries.addCategory(cName);

        String msg = "";
        switch (result) {
            case Results.SUCCESS:
                msg = "Category Added Successfully";
                break;
            case Results.FAILED:
                msg = "Failed to Add Category, please try again later";
                break;
            case Results.ERROR:
                msg = "Error while communicating with Database, please try again later";
                break;
        }

        JOptionPane.showMessageDialog(getParent(), msg);
        GlassPane.closePopupLast();
    }

}