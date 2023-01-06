package custom_components;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import models.MenuItemModel;

public class MenuList extends JList<MenuItemModel> {

    private final DefaultListModel<MenuItemModel> model;
    private int selectedIndex = -1;

    public MenuList() {
        model = new DefaultListModel<MenuItemModel>();
        setModel(model);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                selectedIndex = index;
                repaint();
            }
        });
    }

    @Override
    public ListCellRenderer<? super MenuItemModel> getCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                MenuItemModel data = (MenuItemModel) value;
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex == index);
                return item;
            }
        };
    }

    public void addItem(MenuItemModel item) {
        model.addElement(item);
    }
}
