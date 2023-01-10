package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;

import javax.swing.JLabel;
import javax.swing.JPanel;

import custom_classes.Theme;
import net.miginfocom.swing.MigLayout;

public class LoginLeftPanel extends JPanel {

    JPanel header;
    JLabel appFirstName, appSecondName, slogan;

    public LoginLeftPanel() {
        setOpaque(false);
        setLayout(new MigLayout("al center center, gap 0"));

        header = new JPanel(new MigLayout("gap 0"));
        header.setBackground(Theme.TRANSPARENT_COLOR);
        // add(header, "pos 0.5al 0.49al, wrap");
        add(header, "center, wrap");
        appFirstName = new JLabel("The Wi");
        appFirstName.setForeground(Color.WHITE);
        appFirstName.setFont(Theme.sideNavHeaderFirstFont);
        header.add(appFirstName);

        appSecondName = new JLabel("MS");
        appSecondName.setForeground(Color.WHITE);
        appSecondName.setFont(Theme.sideNavHeaderSecondFont);
        header.add(appSecondName);

        slogan = new JLabel("Manage your warehouse inventory easily");
        slogan.setFont(Theme.headerFont);
        slogan.setForeground(Color.WHITE);
        add(slogan);
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
