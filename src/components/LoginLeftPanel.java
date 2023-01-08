package components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;

import javax.swing.JPanel;

import custom_classes.Theme;

public class LoginLeftPanel extends JPanel {

    public LoginLeftPanel() {
        setOpaque(false);
    }

    // Making Gradient Background
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, Theme.PRIMARY, 0, getHeight(), Theme.SECONDARY);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
        g2d.fillRect(32, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
