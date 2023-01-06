package components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLayeredPane;

import custom_classes.Theme;
import net.miginfocom.swing.MigLayout;

public class MainPanel extends JLayeredPane {
    
    public MainPanel() {
        init();
    }

    private void init() {
        setLayout(new MigLayout());
        setOpaque(false);
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Theme.BG_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g2d.fillRect(0, 0, getWidth() - 16, getHeight());
        super.paintChildren(g);
    }
}
