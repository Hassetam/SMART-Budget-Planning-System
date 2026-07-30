package gui;

import model.User;
import service.AuthenticationService;
import util.UIConstants;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    // Components //

    private JPanel mainPanel;
    private JPanel formPanel;

    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;

    // Services //

    private AuthenticationService authenticationService;

    // Constructor //

    public LoginFrame() {

        authenticationService = new AuthenticationService();

        initializeComponents();
        layoutComponents();
        registerListeners();

        setVisible(true);
    }

    // Methods //

    private void initializeComponents() {

        // Frame
        setTitle(UIConstants.APP_NAME);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main Panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Form Panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.PANEL_COLOR);

        // Labels
        titleLabel = new JLabel("SMART Student Financial Assistant");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        subtitleLabel = new JLabel("Login to Continue");
        subtitleLabel.setFont(UIConstants.NORMAL_FONT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIConstants.NORMAL_FONT);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIConstants.NORMAL_FONT);

        // Text Fields
        usernameField = new JTextField(20);

        passwordField = new JPasswordField(20);

        // Buttons
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        exitButton = new JButton("Exit");

        loginButton.setBackground(UIConstants.BUTTON_COLOR);
        loginButton.setForeground(Color.WHITE);

        registerButton.setBackground(UIConstants.BUTTON_COLOR);
        registerButton.setForeground(Color.WHITE);

        exitButton.setBackground(UIConstants.DELETE_BUTTON_COLOR);
        exitButton.setForeground(Color.WHITE);

        loginButton.setFont(UIConstants.BUTTON_FONT);
        registerButton.setFont(UIConstants.BUTTON_FONT);
        exitButton.setFont(UIConstants.BUTTON_FONT);
    }

    private void layoutComponents() {

        // Title section
        JPanel topPanel = new JPanel();
        topPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(30));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(subtitleLabel);

        // Form section
        GridBagConstraints gbc = new GridBagConstraints();

        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        // Username field
        gbc.gridx = 1;
        usernameField.setPreferredSize(UIConstants.TEXTFIELD_SIZE);
        formPanel.add(usernameField, gbc);

        // Password label
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        // Password field
        gbc.gridx = 1;
        passwordField.setPreferredSize(UIConstants.TEXTFIELD_SIZE);
        formPanel.add(passwordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UIConstants.PANEL_COLOR);

        loginButton.setPreferredSize(UIConstants.LOGIN_BUTTON_SIZE);
        registerButton.setPreferredSize(UIConstants.BUTTON_SIZE);
        exitButton.setPreferredSize(UIConstants.BUTTON_SIZE);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        // Add everything to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void registerListeners() {

        // Login Button
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                login();

            }
        });

        // Register Button
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();

                new RegisterFrame();

            }
        });

        // Exit Button
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);

            }
        });

    }

    private void login() {

        String username = usernameField.getText();

        String password = new String(passwordField.getPassword());

        User user = authenticationService.login(username, password);

        if (user != null) {

            dispose();

            new MainFrame(user);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);

        }

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password");

            return;
        }

    }
}
