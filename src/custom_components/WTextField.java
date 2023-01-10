package custom_components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;

public class WTextField extends JTextField{

    int radius = 0;
    Color bgColor = Color.LIGHT_GRAY;

    public WTextField() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Theme.TRANSPARENT_COLOR);
        setForeground(Theme.TEXT_COLOR);
        setFont(Theme.headerFont);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Focused Effect
        if (isFocusOwner()) {
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }

        super.paintComponent(g);
    }
    
}
