package models;

public class MenuItemModel {

    String title;
    String iconLocation;

    public MenuItemModel(String title) {
        this.title = title;
    }

    public MenuItemModel(String title, String iconLocation) {
        this.title = title;
        this.iconLocation = iconLocation;
    }

    public String getTitle() {
        return title;
    }

    public String getIconLocation() {
        return iconLocation;
    }
}
