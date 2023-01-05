package app;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import components.LoginForm;
import components.LoginLeftPanel;
import custom_classes.DBQueries;
import custom_classes.Theme;
import models.UserDetails;
import custom_classes.EncryptPassword;
import custom_classes.Results;
import net.miginfocom.swing.MigLayout;

public class Login extends javax.swing.JFrame {

    LoginLeftPanel leftPanel;
    LoginForm loginForm;
    MigLayout layout;
    JLayeredPane bg;

    // Default Constructor
    public Login() {
        initializeApp();
    }

    public static void main(String args[]) {

        // Setting Nimbus Look & Feel (Theme)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Fook & Feel Exception: " + ex);
        }

        // Displaying Frame
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    private void initializeApp() {

        // Frame Properties
        setLayout(new MigLayout("insets 0"));
        setPreferredSize(Theme.FRAME_SIZE);
        setSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(Theme.TRANSPARENT_COLOR);
        setLocationRelativeTo(null);

        JFrame context = this;

        // Background
        bg = new JLayeredPane();
        layout = new MigLayout("insets 0");
        bg.setLayout(layout);
        bg.setOpaque(true);
        bg.setBackground(Theme.TRANSPARENT_COLOR);

        add(bg, "width 100%, height 100%");

        // Left Panel
        leftPanel = new LoginLeftPanel();
        bg.add(leftPanel, "pos 0.5al 0 n 100%, width 50%");

        // Login Form
        loginForm = new LoginForm();
        bg.add(loginForm, "pos 0.5al 0 n 100%, width 50%");

        // Application Starting Animation
        startingAnimation();

        // Login Action
        loginForm.btnLoginActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                // Getting Username & Password from Login Form
                String username = loginForm.getUsername();
                String password = loginForm.getPassword();

                // Validating username & password
                if (username.length() == 0) {
                    JOptionPane.showMessageDialog(getContentPane(), "Please enter your username");
                    return;
                } else if (password.length() < 8) {
                    JOptionPane.showMessageDialog(getContentPane(),
                            password.length() == 0 ? "Please enter your password"
                                    : "Password must be at least 8 characters long");
                    return;
                }

                // Encrypting Password
                password = new EncryptPassword().encrypt(password);

                // Attempting Login
                UserDetails user = DBQueries.loginUser(username, password);

                switch (user.getLoginStatus()) {
                    case Results.SUCCESS:
                        context.setVisible(false);
                        new Main(user.getRole()).setVisible(true);
                        break;
                    case Results.FAILED:
                        JOptionPane.showMessageDialog(context, "Login Failed,\nKindly check your credentials");
                        break;
                    default:
                        JOptionPane.showMessageDialog(context,
                                "Login Failed,\nSome error occured, please try again later");
                }
            }
        });

    }

    private void startingAnimation() {
        // Application Starting Animation
        TimingTarget target;
        target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                float fractionPanel = 0.5f - fraction;
                float fractionForm = 0.5f + fraction;
                // Panel Animation
                if (fractionPanel >= -0.05f)
                    layout.setComponentConstraints(leftPanel, "width 50%, pos " + fractionPanel + "al 0 n 100%");

                // Form Animation
                if (fractionForm <= 1f) {
                    layout.setComponentConstraints(loginForm, "width 50%, pos " + fractionForm + "al 0 n 100%");
                }
                bg.revalidate();
            };
        };

        Animator animator = new Animator(1000, target);
        animator.setResolution(0);

        // Delay animation for some time
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!animator.isRunning())
                    animator.start();
            }
        }, 200);
    }

}