package gui.panels;

import model.User;
import service.FinanceService;
import util.UIConstants;

import javax.swing.*;

import gui.MainFrame;

import java.awt.*;

public class DashboardPanel extends JPanel {

    // Logged in user
    private User currentUser;

    // Services
    private FinanceService financeService;

    // Panels
    private JPanel summaryPanel;
    private JPanel buttonPanel;

    // Labels
    private JLabel titleLabel;
    private JLabel welcomeLabel;

    private JLabel budgetLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel remainingLabel;
    private JLabel savingsLabel;

    private MainFrame mainFrame;

    // Recent Expenses
    private JTextArea recentExpenseArea;

    // Buttons
    private JButton addExpenseButton;
    private JButton addIncomeButton;
    private JButton refreshButton;

    public DashboardPanel(User currentUser,MainFrame mainFrame) {

        this.currentUser = currentUser;
        this.mainFrame= mainFrame;

        financeService = new FinanceService();

        initializeComponents();
        layoutComponents();
        loadDashboardData();
        registerListeners();
    }

    private void initializeComponents() {

        setLayout(new BorderLayout(20,20));
        setBackground(UIConstants.BACKGROUND_COLOR);

        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(UIConstants.TITLE_FONT);

        welcomeLabel = new JLabel(
                "Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(UIConstants.NORMAL_FONT);

        budgetLabel = new JLabel();
        incomeLabel = new JLabel();
        expenseLabel = new JLabel();
        remainingLabel = new JLabel();
        savingsLabel = new JLabel();

        Font infoFont = new Font("Segoe UI", Font.BOLD,18);

        budgetLabel.setFont(infoFont);
        incomeLabel.setFont(infoFont);
        expenseLabel.setFont(infoFont);
        remainingLabel.setFont(infoFont);
        savingsLabel.setFont(infoFont);

        recentExpenseArea = new JTextArea(10,40);
                recentExpenseArea.setLineWrap(true);
recentExpenseArea.setWrapStyleWord(true);
recentExpenseArea.setBackground(UIConstants.PANEL_COLOR);


        recentExpenseArea.setEditable(false);
        recentExpenseArea.setFont(UIConstants.NORMAL_FONT);

        addExpenseButton = new JButton("Add Expense");
        addIncomeButton = new JButton("Add Income");

        addExpenseButton.setFont(UIConstants.BUTTON_FONT);
        addIncomeButton.setFont(UIConstants.BUTTON_FONT);

        addExpenseButton.setBackground(UIConstants.BUTTON_COLOR);
        addExpenseButton.setForeground(Color.WHITE);

        addIncomeButton.setBackground(UIConstants.BUTTON_COLOR);
        addIncomeButton.setForeground(Color.WHITE);

        refreshButton = new JButton("Refresh");

refreshButton.setFont(UIConstants.BUTTON_FONT);

refreshButton.setBackground(UIConstants.BUTTON_COLOR);
refreshButton.setForeground(Color.WHITE);

summaryPanel = new JPanel(new GridLayout(5,1,5,5));
summaryPanel.setBorder(
        BorderFactory.createEmptyBorder(10,10,10,10));        summaryPanel.setBackground(UIConstants.PANEL_COLOR);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,50));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

    }

    private void layoutComponents() {

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel,BoxLayout.Y_AXIS));
        northPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        northPanel.add(titleLabel);
        northPanel.add(Box.createVerticalStrut(10));
        northPanel.add(welcomeLabel);

        summaryPanel.add(budgetLabel);
        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(remainingLabel);
        summaryPanel.add(savingsLabel);

        JPanel expensePanel = new JPanel(new BorderLayout());

        expensePanel.setBackground(UIConstants.PANEL_COLOR);

        expensePanel.setBorder(
                BorderFactory.createTitledBorder("Recent Expenses"));

        expensePanel.add(new JScrollPane(recentExpenseArea));

        buttonPanel.add(addExpenseButton);
        buttonPanel.add(addIncomeButton);
        buttonPanel.add(refreshButton);
JPanel centerPanel = new JPanel();
centerPanel.setLayout(new BorderLayout(20,20));
centerPanel.setBackground(UIConstants.BACKGROUND_COLOR);

centerPanel.add(summaryPanel, BorderLayout.NORTH);
centerPanel.add(expensePanel, BorderLayout.CENTER);

add(northPanel, BorderLayout.NORTH);
add(centerPanel, BorderLayout.CENTER);
add(buttonPanel, BorderLayout.SOUTH);

    }

    public void loadDashboardData() {

        int month = java.time.LocalDate.now().getMonthValue();
        int year = java.time.LocalDate.now().getYear();

        double budget = 0;

        if(financeService.getBudget(currentUser.getUserId(),month,year)!=null){

            budget = financeService.getBudget(
                    currentUser.getUserId(),
                    month,
                    year
            ).getMonthlyBudget();

        }

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

        budgetLabel.setText("Monthly Budget : ETB " + budget);

        incomeLabel.setText("Monthly Income : ETB " + income);

        expenseLabel.setText("Monthly Expenses : ETB " + expense);

        remainingLabel.setText("Remaining Budget : ETB " + remaining);

        savingsLabel.setText(
                String.format("Savings Rate : %.2f%%", savings));

       loadRecentExpenses();
    }

    private void loadRecentExpenses() {

    recentExpenseArea.setText("");

    var expenses = financeService.getExpenses(currentUser.getUserId());

    if(expenses.isEmpty()){

        recentExpenseArea.setText("No expenses found.");

        return;

    }

    int count = 0;

    for(var expense : expenses){

        recentExpenseArea.append(

                expense.getDateSpent()

                + "   |   "

                + expense.getCategory()

                + "   |   ETB "

                + expense.getAmount()

                + "\n"

        );

        count++;

        if(count == 5){

            break;

        }

    }

}
    private void registerListeners() {

       addExpenseButton.addActionListener(e ->
        mainFrame.showPanel("Expense"));
    
    addIncomeButton.addActionListener(e ->
        mainFrame.showPanel("Income"));

        refreshButton.addActionListener(e -> {
    loadDashboardData();
    loadRecentExpenses();

});
    }

}