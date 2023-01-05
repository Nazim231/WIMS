package components;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import custom_classes.Theme;
import net.miginfocom.swing.MigLayout;

/*
 * TODO : Change EXIT button from Button to ImageButton
 */

public class LoginForm extends JPanel {

    JLabel lblTitle, lblUsername, lblPassword;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin, btnExit;
    JPanel formPanel;

    // Default Contructor
    public LoginForm() {
        initializeForm();
    }

    // Form Initialization function
    public void initializeForm() {
        
        // Panel Properties
        setLayout(new MigLayout("fillx"));
        setBackground(Theme.BG_COLOR);

        // Exit Button
        btnExit = new JButton("Exit");
        btnExit.setFont(Theme.normalFont);
        btnExit.addActionListener(new ActionListener() {
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
        lblTitle.setForeground(Theme.TITLE_COLOR);
        formPanel.add(lblTitle, "center, wrap");

        // Username
        lblUsername = new JLabel("Username");
        lblUsername.setFont(Theme.normalFont);
        formPanel.add(lblUsername, "wrap, gapy 10");

        txtUsername = new JTextField();
        txtUsername.setFont(Theme.normalFont);
        txtUsername.setBackground(Theme.LOGIN_TF_COLOR);
        formPanel.add(txtUsername, "width 50%, height 40, wrap");

        // Password
        lblPassword = new JLabel("Password");
        lblPassword.setFont(Theme.normalFont);
        formPanel.add(lblPassword, "wrap, gapy 5");

        txtPassword = new JPasswordField();
        txtPassword.setFont(Theme.normalFont);
        txtPassword.setBackground(Theme.LOGIN_TF_COLOR);
        formPanel.add(txtPassword, "width 50%, height 40, wrap");

        // Login Button
        btnLogin = new JButton("Login");
        btnLogin.setFont(Theme.btnLoginFont);
        btnLogin.setBackground(Theme.PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        formPanel.add(btnLogin, "center, width 150, height 50, gapy 10");
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


}
