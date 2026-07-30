package gui.panels;

import model.User;
import service.AuthenticationService;
import util.UIConstants;

import javax.swing.*;

import gui.LoginFrame;

import java.awt.*;

public class SettingsPanel extends JPanel {

    private User currentUser;

    private JLabel titleLabel;
    private JLabel nameLabel;
    private JLabel usernameLabel;

    private JButton changePasswordButton;
    private JButton deleteAccountButton;
    private JButton logoutButton;

    private AuthenticationService authenticationService;

    public SettingsPanel(User user) {

        currentUser = user;

        authenticationService = new AuthenticationService();
        initializeComponents();
        layoutComponents();
        registerListeners();

    }


    private void initializeComponents() {

        setBackground(UIConstants.BACKGROUND_COLOR);


        titleLabel = new JLabel("Settings");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);


        nameLabel = new JLabel(
                "Name: " + currentUser.getFullName()
        );

        usernameLabel = new JLabel(
                "Username: " + currentUser.getUsername()
        );


        nameLabel.setFont(UIConstants.NORMAL_FONT);
        usernameLabel.setFont(UIConstants.NORMAL_FONT);


        changePasswordButton = new JButton(
                "Change Password"
        );


        deleteAccountButton = new JButton(
                "Delete Account"
        );


        logoutButton = new JButton(
                "Logout"
        );


        changePasswordButton.setFont(UIConstants.BUTTON_FONT);
        deleteAccountButton.setFont(UIConstants.BUTTON_FONT);
        logoutButton.setFont(UIConstants.BUTTON_FONT);


        deleteAccountButton.setBackground(
                UIConstants.DELETE_BUTTON_COLOR
        );

        logoutButton.setBackground(
                UIConstants.DELETE_BUTTON_COLOR
        );


        deleteAccountButton.setForeground(Color.WHITE);
        logoutButton.setForeground(Color.WHITE);

    }



    private void layoutComponents() {

        setLayout(new BorderLayout());


        JPanel contentPanel = new JPanel();

        contentPanel.setBackground(
                UIConstants.PANEL_COLOR
        );


        contentPanel.setLayout(
                new BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        );


        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        contentPanel.add(
                Box.createVerticalStrut(30)
        );


        contentPanel.add(titleLabel);


        contentPanel.add(
                Box.createVerticalStrut(40)
        );


        contentPanel.add(nameLabel);


        contentPanel.add(
                Box.createVerticalStrut(10)
        );


        contentPanel.add(usernameLabel);


        contentPanel.add(
                Box.createVerticalStrut(40)
        );


        contentPanel.add(changePasswordButton);


        contentPanel.add(
                Box.createVerticalStrut(15)
        );


        contentPanel.add(deleteAccountButton);


        contentPanel.add(
                Box.createVerticalStrut(15)
        );


        contentPanel.add(logoutButton);



        add(contentPanel, BorderLayout.CENTER);

    }



    private void registerListeners() {


      changePasswordButton.addActionListener(e -> {


    JPasswordField passwordField = new JPasswordField();

    int option = JOptionPane.showConfirmDialog(
            this,
            passwordField,
            "Enter new password",
            JOptionPane.OK_CANCEL_OPTION
    );


    if(option == JOptionPane.OK_OPTION) {


        String newPassword =
                new String(passwordField.getPassword());


        boolean changed =
                authenticationService.changePassword(
                        currentUser.getUserId(),
                        newPassword
                );


        if(changed) {


            JOptionPane.showMessageDialog(
                    this,
                    "Password changed successfully."
            );


        } else {


            JOptionPane.showMessageDialog(
                    this,
                    "Password must be at least 6 characters.",
                    "Change Failed",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

});



       deleteAccountButton.addActionListener(e -> {

    int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete your account?\nThis action cannot be undone.",
            "Delete Account",
            JOptionPane.YES_NO_OPTION
    );


    if(choice == JOptionPane.YES_OPTION) {


        boolean deleted = authenticationService.deleteAccount(
                currentUser.getUserId()
        );


        if(deleted) {


            JOptionPane.showMessageDialog(
                    this,
                    "Account deleted successfully."
            );


            JFrame frame = 
                    (JFrame) SwingUtilities.getWindowAncestor(this);

            frame.dispose();


            new LoginFrame();


        } else {


            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete account.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

});



        logoutButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Logged out successfully."
            );

        });

    }


}