package custom_components;

import java.awt.Color;

import javax.swing.JTable;

import custom_classes.Theme;

public class WTable extends JTable{

    // TODO : Customize Table UI

    public WTable() {
        // Header Properties
        getTableHeader().setOpaque(false);
        getTableHeader().setBackground(Color.WHITE);
        getTableHeader().setForeground(Theme.TEXT_COLOR);
        getTableHeader().setFont(Theme.poppinsMediumFont);

        setRowHeight(30);
        setFont(Theme.latoFont);
        setForeground(Theme.MUTED_TEXT_COLOR);
    }
    
}
