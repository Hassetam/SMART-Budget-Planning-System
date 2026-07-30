package gui;

import model.User;

import javax.swing.*;
import java.awt.*;

import gui.panels.*;
import util.UIConstants;

public class MainFrame extends JFrame {

    // Logged-in user
    private User currentUser;

    // Layout
    private CardLayout cardLayout;

    // Panels
    private JPanel mainPanel;
    private JPanel navigationPanel;
    private JPanel contentPanel;

    // Navigation Buttons
    private JButton dashboardButton;
    private JButton budgetButton;
    private JButton expenseButton;
    private JButton incomeButton;
    private JButton goalButton;
    private JButton reportButton;
    private JButton advisorButton;
    private JButton settingsButton;
    private JButton logoutButton;

    // Constructor
    public MainFrame(User currentUser) {

        this.currentUser = currentUser;

        initializeComponents();
        layoutComponents();
        registerListeners();

        setVisible(true);
    }

    // Methods
    private void initializeComponents() {

        setTitle(UIConstants.APP_NAME);

        setSize(
                UIConstants.FRAME_WIDTH,
                UIConstants.FRAME_HEIGHT);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        cardLayout = new CardLayout();

        mainPanel = new JPanel(new BorderLayout());

        navigationPanel = new JPanel();

        contentPanel = new JPanel(cardLayout);

        navigationPanel.setBackground(UIConstants.PRIMARY_COLOR);

        dashboardButton = new JButton("Dashboard");
        budgetButton = new JButton("Budget");
        expenseButton = new JButton("Expense");
        incomeButton = new JButton("Income");
        goalButton = new JButton("Goals");
        reportButton = new JButton("Reports");
        advisorButton = new JButton("Smart Advisor");
        settingsButton = new JButton("Settings");
        logoutButton = new JButton("Logout");
    }

    private void layoutComponents() {

        // Navigation Panel
        navigationPanel.setLayout(new GridLayout(9, 1, 0, 10));
        navigationPanel.setPreferredSize(new Dimension(220, getHeight()));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Style buttons
        JButton[] buttons = {
                dashboardButton,
                budgetButton,
                expenseButton,
                incomeButton,
                goalButton,
                reportButton,
                advisorButton,
                settingsButton,
                logoutButton
        };

        for (JButton button : buttons) {

            button.setFont(UIConstants.BUTTON_FONT);
            button.setBackground(UIConstants.BUTTON_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);

            navigationPanel.add(button);
        }

        // Content Panel
        contentPanel.add(new DashboardPanel(), "Dashboard");
        contentPanel.add(new BudgetPanel(), "Budget");
        contentPanel.add(new ExpensePanel(), "Expense");
        contentPanel.add(new IncomePanel(), "Income");
        contentPanel.add(new GoalPanel(), "Goal");
        contentPanel.add(new ReportPanel(), "Report");
        contentPanel.add(new SmartAdvisorPanel(), "Advisor");
        contentPanel.add(new SettingsPanel(), "Settings");

        // Show dashboard first
        cardLayout.show(contentPanel, "Dashboard");

        // Main Layout
        mainPanel.add(navigationPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void registerListeners() {

        dashboardButton.addActionListener(e -> cardLayout.show(contentPanel, "Dashboard"));

        budgetButton.addActionListener(e -> cardLayout.show(contentPanel, "Budget"));

        expenseButton.addActionListener(e -> cardLayout.show(contentPanel, "Expense"));

        incomeButton.addActionListener(e -> cardLayout.show(contentPanel, "Income"));

        goalButton.addActionListener(e -> cardLayout.show(contentPanel, "Goal"));

        reportButton.addActionListener(e -> cardLayout.show(contentPanel, "Report"));

        advisorButton.addActionListener(e -> cardLayout.show(contentPanel, "Advisor"));

        settingsButton.addActionListener(e -> cardLayout.show(contentPanel, "Settings"));

        logoutButton.addActionListener(e -> {

            dispose();

            new LoginFrame();

        });
    }

}