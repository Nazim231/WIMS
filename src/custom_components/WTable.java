package custom_components;

import javax.swing.JTable;

import custom_classes.Theme;

public class WTable extends JTable {

    // TODO : Customize Table UI

    public WTable() {
        // Header Properties
        getTableHeader().setDefaultRenderer(new WTableCellRenderer());
        setRowHeight(40);
        setFont(Theme.latoFont);
        setForeground(Theme.MUTED_TEXT_COLOR);
        setSelectionBackground(Theme.PRIMARY);
    }
    
}
