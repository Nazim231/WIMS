package custom_classes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

/*
 * This class is used to store all the Styling related values
 * to make the whole application looks similar
 * e.g., Colors, Fonts
 */

public class Theme {
    
    // Frame Size
    public static final Dimension FRAME_SIZE = new Dimension(1200, 750);

    // Colors
    public static final Color PRIMARY = new Color(51, 111, 246);
    public static final Color SECONDARY = new Color(5, 65, 203);
    public static final Color BG_COLOR = new Color(230, 230, 230);
    public static final Color LOGIN_TF_COLOR = new Color(160,189,255);
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    public static final Color MUTED_TEXT_COLOR = new Color(100, 100, 100);
    public static final Color SUCCESS_COLOR = new Color(14, 120, 47);
    public static final Color WARNING_COLOR  =new Color(240, 111, 39);

    // Fonts
    public static final Font titleFont = new Font("Poppins SemiBold", 0, 24);
    public static final Font buttonFont = new Font("Poppins Medium", 0, 14);
    public static final Font normalFont = new Font("Poppins", 0, 14);
    public static final Font headerFont = new Font("Lato", 0, 14);
    public static final Font btnLoginFont = new Font("Poppins Medium", 0, 18);
    public static final Font counterFont = new Font("Lato Bold", 0, 40);
    // App Name Typography Fonts
    public static final Font sideNavHeaderFirstFont = new Font("Lato Light", 0, 28);
    public static final Font sideNavHeaderSecondFont = new Font("Lato Bold", 0, 28);

    // Functions to set some Theme based on parameters
    // public static Dimension setParentWidth(java.awt.Container parenComponent, int height) {
    //     return new Dimension(parenComponent.getWidth(), height);
    // }
}
