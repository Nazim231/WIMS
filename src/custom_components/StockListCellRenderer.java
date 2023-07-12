package custom_components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;
import models.StockDetails;

public class StockListCellRenderer extends JLabel implements ListCellRenderer<StockDetails> {

    public StockListCellRenderer() {
        setOpaque(true);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends StockDetails> list, StockDetails value, int index,
            boolean isSelected,
            boolean cellHasFocus) {
        if (value instanceof StockDetails) {
            StockDetails prod = (StockDetails) value;
            setText(prod.getProdName());
        }
        setBorder(new EmptyBorder(8, 16, 8, 16));
        setFont(Theme.poppinsFont);
        setBackground(isSelected ? Theme.PRIMARY : Color.WHITE);
        setForeground(isSelected ? Color.WHITE : Color.BLACK);
        return this;
    }

}