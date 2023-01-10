package custom_components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import custom_classes.Theme;
import models.MenuItemModel;
import net.miginfocom.swing.MigLayout;

public class MenuItem extends JPanel {

    private MenuItemModel item;
    private JLabel itemTitle;
    private boolean selected;

    public MenuItem(MenuItemModel item) {
        this.item = item;
        init();
    }

    public void init() {
        setLayout(new MigLayout("insets 10, height 50, al left center"));
        setBackground(new Color(255, 255, 255, 40));
        setOpaque(false);

        // Menu Item Name
        itemTitle = new JLabel(item.getTitle());
        itemTitle.setFont(Theme.poppinsFont);
        itemTitle.setForeground(Color.WHITE);
        // Menu Item Icon
        ImageIcon icon = new ImageIcon(getClass().getResource(item.getIconLocation()));
        itemTitle.setIcon(icon);
        itemTitle.setIconTextGap(10);
        add(itemTitle);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

        repaint();
    }

    public boolean getSelected() {
        return selected;
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(selected ? getBackground() : Theme.TRANSPARENT_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        super.paintChildren(g);
    }
}
