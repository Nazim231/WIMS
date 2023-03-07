package custom_components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import custom_classes.Theme;

public class WTableCellRenderer extends DefaultTableCellRenderer {

    private static final Color HEADER_BG_COLOR = new Color(240, 240, 240);
    private static final Color HEADER_FG_COLOR = new Color(51, 51, 51);
    private static final Border HEADER_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 0,
            Color.GRAY);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setPreferredSize(new Dimension(c.getWidth(), 35));
        setOpaque(true);
        setBackground(HEADER_BG_COLOR);
        setForeground(HEADER_FG_COLOR);
        setFont(Theme.poppinsMediumFont);
        setText(value.toString());
        setBorder(HEADER_BORDER);
        return this;    
    }

}
