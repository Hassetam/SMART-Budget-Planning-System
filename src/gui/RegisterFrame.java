package gui;

import model.User;
import service.AuthenticationService;
import util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {

    //Components
    
    private JPanel mainPanel;
    private JPanel formPanel;

    private JLabel titleLabel;
    private JLabel subtitleLabel;

    private JLabel fullNameLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;

    private JTextField fullNameField;
    private JTextField usernameField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton registerButton;
    private JButton backButton;

    // Service

    private AuthenticationService authenticationService;

    // Constructor

    public RegisterFrame() {

        authenticationService = new AuthenticationService();

        initializeComponents();
        layoutComponents();
        registerListeners();

        setVisible(true);
    }

    
    // Methods

    private void initializeComponents() {

        // Frame
        setTitle("Register");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panels
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.PANEL_COLOR);

        // Labels
        titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        subtitleLabel = new JLabel("Register to Start Managing Your Budget");
        subtitleLabel.setFont(UIConstants.NORMAL_FONT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        fullNameLabel = new JLabel("Full Name");
        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        confirmPasswordLabel = new JLabel("Confirm Password");

        fullNameLabel.setFont(UIConstants.NORMAL_FONT);
        usernameLabel.setFont(UIConstants.NORMAL_FONT);
        passwordLabel.setFont(UIConstants.NORMAL_FONT);
        confirmPasswordLabel.setFont(UIConstants.NORMAL_FONT);

        // Fields
        fullNameField = new JTextField(20);
        usernameField = new JTextField(20);

        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        // Buttons
        registerButton = new JButton("Register");
        backButton = new JButton("Back");

        registerButton.setBackground(UIConstants.BUTTON_COLOR);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(UIConstants.BUTTON_FONT);

        backButton.setBackground(UIConstants.DELETE_BUTTON_COLOR);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(UIConstants.BUTTON_FONT);
    }

    private void layoutComponents() {

        // Top Panel

        JPanel topPanel = new JPanel();
        topPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(30));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(subtitleLabel);

        // Form Panel

        GridBagConstraints gbc = new GridBagConstraints();

        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(fullNameLabel, gbc);

        gbc.gridx = 1;
        fullNameField.setPreferredSize(UIConstants.TEXTFIELD_SIZE);
        formPanel.add(fullNameField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField.setPreferredSize(UIConstants.TEXTFIELD_SIZE);
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField.setPreferredSize(UIConstants.TEXTFIELD_SIZE);
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        confirmPasswordField.setPreferredSize(UIConstants.TEXTFIELD_SIZE);
        formPanel.add(confirmPasswordField, gbc);

        // Button Panel

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UIConstants.PANEL_COLOR);

        registerButton.setPreferredSize(UIConstants.BUTTON_SIZE);
        backButton.setPreferredSize(UIConstants.BUTTON_SIZE);

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // Add Everything

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void registerListeners() {

        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                register();

            }

        });

        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();

                new LoginFrame();

            }

        });

    }

    private void register() {

        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();

        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Check for empty fields
        if (fullName.isEmpty() ||
                username.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.");

            return;
        }

        // Check password confirmation
        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.");

            return;
        }

        // Check password length
        if (password.length() < 6) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must be at least 6 characters long.",
                    "Invalid Password",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        // Check if username already exists
        if (authenticationService.usernameExists(username)) {

            JOptionPane.showMessageDialog(
                    this,
                    "This username already exists. Please choose another one.",
                    "Username Exists",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        // Create User object
        User user = new User(fullName, username, password);

        // Register user
        boolean success = authenticationService.register(user);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful!");

            dispose();

            new LoginFrame();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);

        }
    }
}