package custom_components;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class WButton extends JButton {

    boolean hover;
    Color bgColor, borderColor, hoverColor, rippleColor;
    int cornerRadius = 16, borderWidth = 0;
    int paddingX = 20, paddingY = 10;

    // default color values
    final Color BG_COLOR = Color.WHITE;
    final Color BORDER_COLOR = Color.BLACK;
    final Color RIPPLE_COLOR = new Color(255, 255, 255, 80);
    final Color HOVER_COLOR = new Color(0, 0, 0, 80);

    // Constructor
    public WButton() {
        init();
    }

    public WButton(String text) {
        setText(text);
        init();
    }

    // Initializing Button
    private void init() {
        setBorder(new EmptyBorder(paddingY, paddingX, paddingY, paddingX));
        setBgColor(BG_COLOR);
        setBorderColor(BORDER_COLOR);
        setHoverColor(HOVER_COLOR);
        setRippleColor(RIPPLE_COLOR);
        setContentAreaFilled(false);

    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isHover() {
        return hover;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        setBackground(bgColor);
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setRippleColor(Color rippleColor) {
        this.rippleColor = rippleColor;
    }

    public Color getRippleColor() {
        return rippleColor;
    }

    public void setPadding(int paddingX, int paddingY) {
        this.paddingX = paddingX;
        this.paddingY = paddingY;
        setBorder(new EmptyBorder(paddingY, paddingX, paddingY, paddingX));
    }
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean containsBorder = borderWidth > 0;
        g2.setColor(containsBorder ? borderColor : bgColor);    // button contains border then set border color else background color
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        if (containsBorder) {   // button contains border then this block will set the background color of button
            g2.setColor(getBgColor());
            g2.fillRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2),
                    cornerRadius, cornerRadius);
        }

        // Hover and Click Effect
        if (getModel().isRollover() || isFocusOwner()) {
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
        if (getModel().isPressed()) {   // added button pressing effect
            g2.setColor(new Color(255 ,255, 255, 60));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
        super.paintComponent(g);
    }

}