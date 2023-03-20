package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;
import custom_components.WTextField;
import custom_components.GlassPanePopup.GlassPane;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import custom_components.RoundedCornerPanel;
import custom_components.WButton;

public class AddCategoryPage extends RoundedCornerPanel {

    JLabel lblTitle, lblCategoryName;
    WButton btnClose;
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
        add(txtCategoryName, "width 100%, height 50");
    }


}