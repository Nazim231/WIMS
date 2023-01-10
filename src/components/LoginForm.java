package components;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import custom_classes.Theme;
import custom_components.WButton;
import custom_components.WPasswordField;
import custom_components.WTextField;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;

public class LoginForm extends JPanel {

    JLabel lblTitle, lblUsername, lblPassword;
    WTextField txtUsername;
    WPasswordField txtPassword;
    WButton btnLogin, btnExit;
    JPanel formPanel;

    // Default Contructor
    public LoginForm() {
        initializeForm();
    }

    // Form Initialization function
    public void initializeForm() {

        // Panel Properties
        setOpaque(false);
        setLayout(new MigLayout("fillx"));
        setBackground(Theme.BG_COLOR);

        // Exit Button Icon
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Icon icon = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 28, Theme.PRIMARY);
        // Exit Button
        btnExit = new WButton();
        btnExit.setIcon(icon);
        btnExit.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnExit.setFont(Theme.normalFont);
        btnExit.setBgColor(Theme.TRANSPARENT_COLOR);
        btnExit.setCornerRadius(128);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(new ActionListener() {    // Action to perform on click of exit
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(btnExit, "al right top, wrap");

        // Form Panel
        formPanel = new JPanel(new MigLayout("al center center"));
        formPanel.setBackground(Theme.BG_COLOR);
        add(formPanel, "width 100%, height 100%");

        // Title
        lblTitle = new JLabel("Login");
        lblTitle.setFont(Theme.titleFont);
        lblTitle.setForeground(Theme.MUTED_TEXT_COLOR);
        formPanel.add(lblTitle, "center, wrap");

        // Username
        lblUsername = new JLabel("Username");
        lblUsername.setFont(Theme.normalFont);
        lblUsername.setForeground(Theme.MUTED_TEXT_COLOR);
        formPanel.add(lblUsername, "wrap, gapy 10");

        txtUsername = new WTextField();
        txtUsername.setBgColor(Theme.LOGIN_TF_COLOR);
        txtUsername.setRadius(16);
        formPanel.add(txtUsername, "width 50%, height 40, wrap");

        // Password
        lblPassword = new JLabel("Password");
        lblPassword.setFont(Theme.normalFont);
        lblPassword.setForeground(Theme.MUTED_TEXT_COLOR);
        formPanel.add(lblPassword, "wrap, gapy 5");

        txtPassword = new WPasswordField();
        txtPassword.setEchoChar('•');
        txtPassword.setBgColor(Theme.LOGIN_TF_COLOR);
        txtPassword.setRadius(16);
        formPanel.add(txtPassword, "width 50%, height 40, wrap");

        // Login Button
        btnLogin = new WButton("Login");
        btnLogin.setFont(Theme.normalFont);
        btnLogin.setBgColor(Theme.PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnLogin, "center, width 100, gapy 10");
    }

    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return String.valueOf(txtPassword.getPassword());
    }

    public void btnLoginActionListener(ActionListener actionListener) {
        btnLogin.addActionListener(actionListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
        g2.fillRect(0, 0, getWidth() - 32, getHeight());
        super.paintComponent(g);
    }
}
