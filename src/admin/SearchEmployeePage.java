package admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;

import custom_components.RoundedCornerPanel;
import custom_components.WButton;
import custom_components.GlassPanePopup.GlassPane;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

public class SearchEmployeePage extends RoundedCornerPanel {

    WButton btnClose, btnSelectEmp;

    public SearchEmployeePage() {
        init();
    }

    private void init() {
        setPreferredSize(new Dimension((int) Theme.FRAME_SIZE.getWidth() - 200, 500));
        setMinimumSize(getPreferredSize());
        setBackground(Theme.TRANSPARENT_COLOR);
        setCornerRadius(16);
        setBgColor(Theme.BG_COLOR);
        setLayout(new MigLayout("fillx, insets 20, gapx 20, gapy 10"));

        // Close Button
        btnClose = new WButton();
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Icon icon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 28, Theme.PRIMARY);
        btnClose.setIcon(icon);
        btnClose.setCornerRadius(128);
        btnClose.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnClose.setBgColor(Theme.TRANSPARENT_COLOR);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPane.closePopupLast();
            }
        });
        add(btnClose, "right, gapbottom 10, wrap");

        /*
         * TODO : ADD ComboBox to filter searching type (id, email, name)
         * TODO : ADD TextField to enter the value to be searched
         * TODO : ADD Table to show the searched results
         */

        // Select Employee Button
        btnSelectEmp = new WButton("Select Employee");
        btnSelectEmp.setBgColor(Theme.PRIMARY);
        btnSelectEmp.setFont(Theme.latoFont);
        btnSelectEmp.setForeground(Color.WHITE);
        add(btnSelectEmp, "height 40, right");
    }

}
