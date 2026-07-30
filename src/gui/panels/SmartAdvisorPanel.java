package gui.panels;

import model.User;
import model.Goal;
import model.GeneralGoal;
import model.OccasionGoal;
import service.SmartAdvisorService;
import service.DailyBudgetResult;
import service.OccasionShortfallResult;
import service.BudgetWarning;
import service.SavingsSuggestion;
import util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SmartAdvisorPanel extends JPanel {

    // Logged-in user
    private User currentUser;

    // Service
    private SmartAdvisorService smartAdvisorService;

    // Panels
    private JPanel centerPanel;

    // Labels / Text Areas
    private JLabel titleLabel;
    private JTextArea insightsArea;
    private JTextArea warningsArea;

    // Buttons
    private JButton refreshButton;
    private JButton checkDailyBudgetButton;
    private JButton checkOccasionGoalsButton;
    private JButton checkSavingsSuggestionButton;

    public SmartAdvisorPanel(User currentUser) {

        this.currentUser = currentUser;
        smartAdvisorService = new SmartAdvisorService();

        initializeComponents();
        layoutComponents();
        loadAdvisorData();
        registerListeners();
    }

    private void initializeComponents() {

        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);

        titleLabel = new JLabel("Smart Advisor");
        titleLabel.setFont(UIConstants.TITLE_FONT);

        insightsArea = new JTextArea(10, 30);
        insightsArea.setEditable(false);
        insightsArea.setFont(UIConstants.NORMAL_FONT);

        warningsArea = new JTextArea(6, 30);
        warningsArea.setEditable(false);
        warningsArea.setFont(UIConstants.NORMAL_FONT);
        warningsArea.setForeground(UIConstants.WARNING_COLOR);

        refreshButton = new JButton("Refresh");
        checkDailyBudgetButton = new JButton("Check Daily Budget");
        checkOccasionGoalsButton = new JButton("Check Occasion Goals");
        checkSavingsSuggestionButton = new JButton("Check Month-End Savings");

        JButton[] buttons = {
                refreshButton,
                checkDailyBudgetButton,
                checkOccasionGoalsButton,
                checkSavingsSuggestionButton
        };

        for (JButton button : buttons) {

            button.setFont(UIConstants.BUTTON_FONT);
            button.setBackground(UIConstants.BUTTON_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
        }

        centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void layoutComponents() {

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        northPanel.add(titleLabel);

        JPanel insightsPanel = new JPanel(new BorderLayout());
        insightsPanel.setBackground(UIConstants.PANEL_COLOR);
        insightsPanel.setBorder(BorderFactory.createTitledBorder("Financial Insights"));
        insightsPanel.add(new JScrollPane(insightsArea));

        JPanel warningsPanel = new JPanel(new BorderLayout());
        warningsPanel.setBackground(UIConstants.PANEL_COLOR);
        warningsPanel.setBorder(BorderFactory.createTitledBorder("Budget Warnings"));
        warningsPanel.add(new JScrollPane(warningsArea));

        centerPanel.add(insightsPanel);
        centerPanel.add(warningsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        buttonPanel.add(checkDailyBudgetButton);
        buttonPanel.add(checkOccasionGoalsButton);
        buttonPanel.add(checkSavingsSuggestionButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAdvisorData() {

        List<String> insights = smartAdvisorService.generateFinancialInsights(currentUser.getUserId());

        StringBuilder insightsText = new StringBuilder();

        for (String insight : insights) {
            insightsText.append("• ").append(insight).append("\n");
        }

        insightsArea.setText(insightsText.toString());

        List<BudgetWarning> warnings = smartAdvisorService.checkBudgetWarnings(currentUser.getUserId());

        if (warnings.isEmpty()) {

            warningsArea.setText("No warnings right now.");

        } else {

            StringBuilder warningsText = new StringBuilder();

            for (BudgetWarning warning : warnings) {

                warningsText.append("[").append(warning.level()).append("] ")
                        .append(warning.message()).append("\n");
            }

            warningsArea.setText(warningsText.toString());
        }
    }

    private void registerListeners() {

        refreshButton.addActionListener(e -> loadAdvisorData());

        checkDailyBudgetButton.addActionListener(e -> handleDailyBudgetCheck());

        checkOccasionGoalsButton.addActionListener(e -> handleOccasionGoalsCheck());

        checkSavingsSuggestionButton.addActionListener(e -> handleSavingsSuggestion());
    }

    // =========================================================
    // Daily Budget Adjustment
    // =========================================================
    private void handleDailyBudgetCheck() {

        DailyBudgetResult result = smartAdvisorService.analyzeDailyBudget(currentUser.getUserId());

        if (result.needsAllocationDecision()) {

            String[] options = { "Monthly Expenses", "Savings", "Occasion Fund" };

            int choice = JOptionPane.showOptionDialog(
                    this,
                    result.message(),
                    "Daily Budget Surplus",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == JOptionPane.CLOSED_OPTION) {
                return;
            }

            String destination = options[choice];

            Integer goalId = null;

            if (!destination.equals("Monthly Expenses")) {

                goalId = promptForGoal(destination);

                if (goalId == null) {
                    return;
                }
            }

            boolean applied = smartAdvisorService.allocateFunds(destination, result.difference(), goalId);

            showResult(applied, "Surplus allocated successfully.", "Could not apply allocation.");

        } else {

            JOptionPane.showMessageDialog(this, result.message());
        }

        loadAdvisorData();
    }

    // =========================================================
    // Occasion goal shortfall check
    // =========================================================
    private void handleOccasionGoalsCheck() {

        List<OccasionShortfallResult> results = smartAdvisorService.checkOccasionGoals(currentUser.getUserId());

        if (results.isEmpty()) {

            JOptionPane.showMessageDialog(this, "No occasion goals reaching their deadline today.");
            return;
        }

        for (OccasionShortfallResult result : results) {

            if (result.needsFundingDecision()) {

                String[] options = { "Monthly Budget", "Regular Savings" };

                int choice = JOptionPane.showOptionDialog(
                        this,
                        result.message(),
                        "Occasion Goal Shortfall",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice == JOptionPane.CLOSED_OPTION) {
                    continue;
                }

                String source = options[choice];

                Integer savingsGoalId = null;

                if (source.equals("Regular Savings")) {

                    savingsGoalId = promptForGoal("Savings");

                    if (savingsGoalId == null) {
                        continue;
                    }
                }

                boolean resolved = smartAdvisorService.resolveOccasionShortfall(
                        source, result.goalId(), result.shortfall(), savingsGoalId);

                showResult(resolved, "Shortfall resolved.", "Could not resolve shortfall.");

            } else {

                JOptionPane.showMessageDialog(this, result.message());
            }
        }

        loadAdvisorData();
    }

    // =========================================================
    // Savings Suggestions (end of month)
    // =========================================================
    private void handleSavingsSuggestion() {

        SavingsSuggestion suggestion = smartAdvisorService.suggestEndOfMonthSavings(currentUser.getUserId());

        if (suggestion == null) {

            JOptionPane.showMessageDialog(this, "No end-of-month suggestion right now.");
            return;
        }

        String[] options = { "Savings", "Occasion Fund" };

        int choice = JOptionPane.showOptionDialog(
                this,
                suggestion.message(),
                "End of Month Savings",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        String destination = options[choice];

        Integer goalId = promptForGoal(destination);

        if (goalId == null) {
            return;
        }

        boolean applied = smartAdvisorService.allocateFunds(destination, suggestion.leftoverAmount(), goalId);

        showResult(applied, "Leftover funds allocated.", "Could not allocate funds.");

        loadAdvisorData();
    }

    // =========================================================
    // Lets the user pick which goal to fund, filtered to the
    // right goal type for the chosen destination.
    // =========================================================
    private Integer promptForGoal(String destination) {

        List<Goal> activeGoals = smartAdvisorService.getActiveGoals(currentUser.getUserId());

        List<Goal> filteredGoals = new ArrayList<>();

        for (Goal goal : activeGoals) {

            if (destination.equals("Occasion Fund") && goal instanceof OccasionGoal) {

                filteredGoals.add(goal);

            } else if (destination.equals("Savings") && goal instanceof GeneralGoal) {

                filteredGoals.add(goal);
            }
        }

        if (filteredGoals.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "You don't have any active goals for " + destination + " yet.",
                    "No Goals Found",
                    JOptionPane.WARNING_MESSAGE);

            return null;
        }

        String[] goalNames = filteredGoals.stream()
                .map(Goal::getGoalName)
                .toArray(String[]::new);

        String chosenName = (String) JOptionPane.showInputDialog(
                this,
                "Choose a goal:",
                "Select Goal",
                JOptionPane.QUESTION_MESSAGE,
                null,
                goalNames,
                goalNames[0]);

        if (chosenName == null) {
            return null;
        }

        for (Goal goal : filteredGoals) {

            if (goal.getGoalName().equals(chosenName)) {

                return goal.getGoalId();
            }
        }

        return null;
    }

    private void showResult(boolean success, String successMessage, String failureMessage) {

        JOptionPane.showMessageDialog(
                this,
                success ? successMessage : failureMessage,
                success ? "Success" : "Failed",
                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

}