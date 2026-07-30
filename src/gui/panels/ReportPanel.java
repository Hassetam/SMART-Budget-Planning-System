package gui.panels;

import model.User;
import service.FinanceService;
import util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ReportPanel extends JPanel {

    // Current user
    private User currentUser;

    // Service
    private FinanceService financeService;

    // Panels
    private JPanel topPanel;
    private JPanel titlePanel;
    private JPanel controlPanel;
    private JPanel reportPanel;

    // Labels
    private JLabel titleLabel;
    private JLabel monthLabel;
    private JLabel yearLabel;

    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel remainingLabel;
    private JLabel savingsLabel;
    private JLabel expenseCountLabel;
    private JLabel incomeCountLabel;

    // Inputs
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;

    // Button
    private JButton generateButton;

    public ReportPanel(User currentUser) {

        this.currentUser = currentUser;

        financeService = new FinanceService();

        initializeComponents();
        layoutComponents();
        registerListeners();

        loadReport();
    }

    private void initializeComponents() {

        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Panels
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        titlePanel = new JPanel();
        titlePanel.setBackground(UIConstants.BACKGROUND_COLOR);

        controlPanel = new JPanel();
        controlPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        reportPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        reportPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Title
        titleLabel = new JLabel("Financial Reports");
        titleLabel.setFont(UIConstants.TITLE_FONT);

        // Controls
        monthLabel = new JLabel("Month");
        yearLabel = new JLabel("Year");

        monthComboBox = new JComboBox<>(new String[] {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        });

        monthComboBox.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        yearComboBox = new JComboBox<>();

        for (int year = 2025; year <= 2035; year++) {
            yearComboBox.addItem(year);
        }

        yearComboBox.setSelectedItem(LocalDate.now().getYear());

        generateButton = new JButton("Generate Report");
        generateButton.setFont(UIConstants.BUTTON_FONT);
        generateButton.setBackground(UIConstants.BUTTON_COLOR);
        generateButton.setForeground(Color.WHITE);

        // Report Labels
        incomeLabel = new JLabel();
        expenseLabel = new JLabel();
        remainingLabel = new JLabel();
        savingsLabel = new JLabel();
        expenseCountLabel = new JLabel();
        incomeCountLabel = new JLabel();

        Font reportFont = new Font("Segoe UI", Font.PLAIN, 16);

        incomeLabel.setFont(reportFont);
        expenseLabel.setFont(reportFont);
        remainingLabel.setFont(reportFont);
        savingsLabel.setFont(reportFont);
        expenseCountLabel.setFont(reportFont);
        incomeCountLabel.setFont(reportFont);
    }

    private void layoutComponents() {

        titlePanel.add(titleLabel);

        controlPanel.add(monthLabel);
        controlPanel.add(monthComboBox);
        controlPanel.add(yearLabel);
        controlPanel.add(yearComboBox);
        controlPanel.add(generateButton);

        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.SOUTH);

        reportPanel.add(incomeLabel);
        reportPanel.add(expenseLabel);
        reportPanel.add(remainingLabel);
        reportPanel.add(savingsLabel);
        reportPanel.add(expenseCountLabel);
        reportPanel.add(incomeCountLabel);

        add(topPanel, BorderLayout.NORTH);
        add(reportPanel, BorderLayout.CENTER);
    }

    private void loadReport() {

        if (currentUser == null) {
            return;
        }

        int month = monthComboBox.getSelectedIndex() + 1;
        int year = (Integer) yearComboBox.getSelectedItem();

        double income = financeService.getMonthlyIncomeTotal(
                currentUser.getUserId(),
                month,
                year);

        double expense = financeService.getMonthlyExpenseTotal(
                currentUser.getUserId(),
                month,
                year);

        double remaining = financeService.getRemainingBudget(
                currentUser.getUserId(),
                month,
                year);

        double savings = financeService.getSavingsRate(
                currentUser.getUserId(),
                month,
                year);

        int expenseCount = financeService.getMonthlyExpenseCount(
                currentUser.getUserId(),
                month,
                year);

        int incomeCount = financeService.getMonthlyIncomeCount(
                currentUser.getUserId(),
                month,
                year);

        incomeLabel.setText("Total Income: ETB " + String.format("%.2f", income));
        expenseLabel.setText("Total Expense: ETB " + String.format("%.2f", expense));
        remainingLabel.setText("Remaining Budget: ETB " + String.format("%.2f", remaining));
        savingsLabel.setText("Savings Rate: " + String.format("%.2f", savings) + "%");
        expenseCountLabel.setText("Expense Records: " + expenseCount);
        incomeCountLabel.setText("Income Records: " + incomeCount);
    }

    private void registerListeners() {

        generateButton.addActionListener(e -> loadReport());

    }

}