package custom_components;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;
import net.miginfocom.swing.MigLayout;

public class CounterSection extends RoundedCornerPanel {

    private String count;
    private String desc;
    private String imageLocation;
    private Color counterColor;

    private JLabel lblCounter, lblDesc, lblIcon;

    // Constructor for Image Icon
    public CounterSection(String count, String desc, String imageLocation, Color counterColor) {
        this.count = count;
        this.desc = desc;
        this.imageLocation = imageLocation;
        this.counterColor = counterColor;
        init();
    }

    private void init() {
        setBgColor(Color.WHITE);
        setCornerRadius(32);
        setLayout(new MigLayout("insets 20, gap 0"));

        // Left Side ( For Icon )
        JPanel left = new JPanel(new MigLayout("insets 0, al center center"));
        left.setOpaque(false);
        add(left, "width 30%, height 100%, al center center");

        // Icon
        lblIcon = new JLabel();
        ImageIcon icon = new ImageIcon(this.getClass().getResource(imageLocation));
        lblIcon.setIcon(icon);
        lblIcon.setVerticalAlignment(SwingConstants.CENTER);
        left.add(lblIcon);

        // Right Side ( For Text)
        JPanel right = new JPanel(new MigLayout("insets 0, gap 0, al right center"));
        right.setOpaque(false);
        add(right, "width 70%, height 100%, al center center");

        // Counter
        lblCounter = new JLabel(count);
        lblCounter.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCounter.setForeground(counterColor);
        lblCounter.setFont(Theme.latoBoldBiggerFont);
        right.add(lblCounter, "width 100%, wrap");

        // Desc Line
        lblDesc = new JLabel(desc);
        lblDesc.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDesc.setFont(Theme.poppinsFont);
        lblDesc.setBorder(new EmptyBorder(0, 1, 0, 4));
        lblDesc.setForeground(Theme.MUTED_TEXT_COLOR);
        right.add(lblDesc, "width 100%");

    }

}
