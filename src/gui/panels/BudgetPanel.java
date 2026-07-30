package gui.panels;

import model.Budget;
import model.User;
import service.FinanceService;
import util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class BudgetPanel extends JPanel {

    // Logged in user
    private User currentUser;

    // Service
    private FinanceService financeService;

    // Labels
    private JLabel titleLabel;
    private JLabel budgetLabel;
    private JLabel monthLabel;
    private JLabel yearLabel;
    private JLabel currentBudgetLabel;

    // Input Components
    private JTextField budgetField;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;

    // Buttons
    private JButton saveButton;
    private JButton updateButton;
    private JButton clearButton;
    private JButton deleteButton;

    // Panels
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JPanel informationPanel;

    public BudgetPanel(User currentUser) {

        this.currentUser = currentUser;

        financeService = new FinanceService();

        initializeComponents();
        layoutComponents();
        loadCurrentBudget();
        registerListeners();

    }

    private void initializeComponents() {

        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);

        titleLabel = new JLabel("Monthly Budget");
        titleLabel.setFont(UIConstants.TITLE_FONT);

        budgetLabel = new JLabel("Monthly Budget (ETB)");
        monthLabel = new JLabel("Month");
        yearLabel = new JLabel("Year");

        currentBudgetLabel = new JLabel("No budget available.");

        budgetLabel.setFont(UIConstants.NORMAL_FONT);
        monthLabel.setFont(UIConstants.NORMAL_FONT);
        yearLabel.setFont(UIConstants.NORMAL_FONT);
        currentBudgetLabel.setFont(UIConstants.NORMAL_FONT);

        budgetField = new JTextField(20);

        String[] months = {
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };

        monthComboBox = new JComboBox<>(months);

        yearComboBox = new JComboBox<>();

        int currentYear = LocalDate.now().getYear();

        for (int i = currentYear - 2; i <= currentYear + 5; i++) {

            yearComboBox.addItem(i);

        }

        saveButton = new JButton("Save Budget");
        updateButton = new JButton("Update Budget");
        clearButton = new JButton("Clear");

        saveButton.setFont(UIConstants.BUTTON_FONT);
        updateButton.setFont(UIConstants.BUTTON_FONT);
        clearButton.setFont(UIConstants.BUTTON_FONT);

        saveButton.setBackground(UIConstants.BUTTON_COLOR);
        updateButton.setBackground(UIConstants.BUTTON_COLOR);
        clearButton.setBackground(UIConstants.BUTTON_COLOR);

        saveButton.setForeground(Color.WHITE);
        updateButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.WHITE);

        deleteButton = new JButton("Delete Budget");

        deleteButton.setFont(UIConstants.BUTTON_FONT);
        deleteButton.setBackground(UIConstants.BUTTON_COLOR);
        deleteButton.setForeground(Color.WHITE);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        informationPanel = new JPanel();
        informationPanel.setBackground(UIConstants.BACKGROUND_COLOR);

    }

    private void layoutComponents() {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Budget
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(budgetLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(budgetField, gbc);

        // Month
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(monthLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(monthComboBox, gbc);

        // Year
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(yearLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(yearComboBox, gbc);

        // Buttons
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Current Budget Panel
        informationPanel.setLayout(new BorderLayout());

        informationPanel.setBorder(
                BorderFactory.createTitledBorder("Current Budget"));

        informationPanel.add(currentBudgetLabel, BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(informationPanel, BorderLayout.SOUTH);

    }

    private void loadCurrentBudget() {

        Budget budget = financeService.getCurrentBudget(currentUser.getUserId());

        if (budget == null) {

            currentBudgetLabel.setText("No budget found.");

            return;
        }

        budgetField.setText(String.valueOf(budget.getMonthlyBudget()));

        monthComboBox.setSelectedIndex(budget.getMonth() - 1);

        yearComboBox.setSelectedItem(budget.getYear());

        currentBudgetLabel.setText(
                "<html>"
                        + "<b>Current Budget:</b> ETB "
                        + budget.getMonthlyBudget()
                        + "<br>"
                        + "<b>Month:</b> "
                        + monthComboBox.getSelectedItem()
                        + " "
                        + budget.getYear()
                        + "</html>");

    }

    private void registerListeners() {

        saveButton.addActionListener(e -> saveBudget());

        updateButton.addActionListener(e -> updateBudget());

        clearButton.addActionListener(e -> clearFields());

        deleteButton.addActionListener(e -> deleteBudget());

    }

    private void saveBudget() {

        try {

            double amount = Double.parseDouble(budgetField.getText());

            int month = monthComboBox.getSelectedIndex() + 1;

            int year = (Integer) yearComboBox.getSelectedItem();

            double monthlyIncome = financeService.getMonthlyIncomeTotal(
                    currentUser.getUserId(),
                    month,
                    year);

            if (monthlyIncome > 0 && amount > monthlyIncome) {

                JOptionPane.showMessageDialog(
                        this,
                        "Warning!\n\nYour monthly budget (ETB " + amount +
                                ") is greater than your recorded income (ETB " + monthlyIncome +
                                ").\n\nYou may spend more than you earn this month.",
                        "Budget Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

            if (financeService.budgetExists(currentUser.getUserId(), month, year)) {

                JOptionPane.showMessageDialog(
                        this,
                        "A budget already exists for this month.",
                        "Duplicate Budget",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            Budget budget = new Budget(
                    currentUser.getUserId(),
                    amount,
                    month,
                    year);

            if (financeService.createBudget(budget)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Budget saved successfully.");

                loadCurrentBudget();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to save budget.");

            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid budget amount.");

        }

    }

    private void updateBudget() {

        try {

            double amount = Double.parseDouble(budgetField.getText());

            int month = monthComboBox.getSelectedIndex() + 1;

            int year = (Integer) yearComboBox.getSelectedItem();

            double monthlyIncome = financeService.getMonthlyIncomeTotal(
                    currentUser.getUserId(),
                    month,
                    year);

            if (monthlyIncome > 0 && amount > monthlyIncome) {

                JOptionPane.showMessageDialog(
                        this,
                        "Warning!\n\nYour monthly budget (ETB " + amount +
                                ") is greater than your recorded income (ETB " + monthlyIncome +
                                ").\n\nYou may spend more than you earn this month.",
                        "Budget Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

            Budget budget = new Budget(
                    currentUser.getUserId(),
                    amount,
                    month,
                    year);

            if (financeService.updateBudget(budget)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Budget updated successfully.");

                loadCurrentBudget();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No budget found to update.");

            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid budget amount.");

        }

    }

    private void clearFields() {

        budgetField.setText("");

        monthComboBox.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        yearComboBox.setSelectedItem(LocalDate.now().getYear());

    }

    private void deleteBudget() {

        int month = monthComboBox.getSelectedIndex() + 1;

        int year = (Integer) yearComboBox.getSelectedItem();

        Budget budget = financeService.getBudget(
                currentUser.getUserId(),
                month,
                year);

        if (budget == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No budget found for the selected month.");

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete this budget?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            if (financeService.deleteBudget(budget.getBudgetId())) {

                JOptionPane.showMessageDialog(
                        this,
                        "Budget deleted successfully.");

                clearFields();

                loadCurrentBudget();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete budget.");

            }

        }

    }

}