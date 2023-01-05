package models;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;

public class MenuItemModel {

    String title;
    FontAwesome FIcon;
    GoogleMaterialDesignIcons GIcon;

    public MenuItemModel(String title) {
        this.title = title;
    }

    public MenuItemModel(String title, FontAwesome icon) {
        this.title = title;
        this.FIcon = icon;
    }

    public MenuItemModel(String title, GoogleMaterialDesignIcons icon) {
        this.title = title;
        this.GIcon = icon;
    }

    public String getTitle() {
        return title;
    }

    public FontAwesome getFIcon() {
        return FIcon;
    }

    public GoogleMaterialDesignIcons getGIcon() {
        return GIcon;
    }
}
