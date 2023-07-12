package custom_components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;
import models.ShopStockDetails;

public class ShopStockListCellRenderer extends JLabel implements ListCellRenderer<ShopStockDetails>{
    
    public ShopStockListCellRenderer() {
        setOpaque(true);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends ShopStockDetails> list, ShopStockDetails value, int index,
            boolean isSelected,
            boolean cellHasFocus) {
        if (value instanceof ShopStockDetails) {
            ShopStockDetails prod = (ShopStockDetails) value;
            setText(prod.getProductName());
        }
        setBorder(new EmptyBorder(8, 16, 8, 16));
        setFont(Theme.poppinsFont);
        setBackground(isSelected ? Theme.PRIMARY : Color.WHITE);
        setForeground(isSelected ? Color.WHITE : Color.BLACK);
        return this;
    }
}
