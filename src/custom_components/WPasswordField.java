package custom_components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;

public class WPasswordField extends JPasswordField {
    
    int radius = 0;

    public WPasswordField() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Theme.TRANSPARENT_COLOR);
        setForeground(Theme.TEXT_COLOR);
        setFont(Theme.latoFont);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
    }

}
