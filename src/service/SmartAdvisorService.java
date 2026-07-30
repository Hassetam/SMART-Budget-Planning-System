package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Budget;
import model.Expense;
import model.Goal;
import model.Income;
import model.OccasionGoal;


public class SmartAdvisorService {

    private final FinanceService financeService = new FinanceService();
    private final GoalService goalService = new GoalService();

    //=========================================================
    // FEATURE 1
    // Daily Budget Adjustment
    //=========================================================
    public DailyBudgetResult analyzeDailyBudget(int userId) {

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        Budget budget = financeService.getCurrentBudget(userId);

        if (budget == null) {

            return new DailyBudgetResult(
                    today, 0, 0, 0, 0, false,
                    "No budget has been set for this month yet.");
        }

        double monthlyBudget = budget.getMonthlyBudget();

        List<Expense> monthExpenses = financeService.getMonthlyExpenses(userId, month, year);

        double spentBeforeToday = 0;
        double spentToday = 0;

        for (Expense expense : monthExpenses) {

            if (expense.getDateSpent().isEqual(today)) {

                spentToday += expense.getAmount();

            } else if (expense.getDateSpent().isBefore(today)) {

                spentBeforeToday += expense.getAmount();
            }
        }

        int totalDaysInMonth = today.lengthOfMonth();
        int dayOfMonth = today.getDayOfMonth();
        int daysRemainingIncludingToday = totalDaysInMonth - dayOfMonth + 1;

        double expectedDailyBudget = (monthlyBudget - spentBeforeToday) / daysRemainingIncludingToday;

        double difference = expectedDailyBudget - spentToday;

        int daysRemainingAfterToday = totalDaysInMonth - dayOfMonth;
        double remainingBudgetAfterToday = monthlyBudget - spentBeforeToday - spentToday;

        double recalculatedDailyBudget = daysRemainingAfterToday > 0
                ? remainingBudgetAfterToday / daysRemainingAfterToday
                : remainingBudgetAfterToday;

        if (difference > 0.01) {

            return new DailyBudgetResult(
                    today, expectedDailyBudget, spentToday, difference,
                    recalculatedDailyBudget, true,
                    String.format(
                            "You spent ETB %.2f less than expected today. Where should the extra ETB %.2f go?",
                            difference, difference));

        } else if (difference < -0.01) {

            return new DailyBudgetResult(
                    today, expectedDailyBudget, spentToday, difference,
                    recalculatedDailyBudget, false,
                    String.format(
                            "You overspent by ETB %.2f today. Your daily budget for the rest of the month is now ETB %.2f.",
                            -difference, recalculatedDailyBudget));

        } else {

            return new DailyBudgetResult(
                    today, expectedDailyBudget, spentToday, difference,
                    recalculatedDailyBudget, false,
                    "Right on budget today!");
        }
    }

    //=========================================================
    // Applies the user's chosen destination for leftover money
    // — reused by both Daily Budget Adjustment and (later)
    // Unexpected Income Allocation.
    //=========================================================
    public boolean allocateFunds(String destination, double amount, Integer goalId) {

        switch (destination) {

            case "Monthly Expenses" -> {

                // No action needed — the money simply stays in the budget
                // pool and is already folded into recalculatedDailyBudget.
                return true;
            }

            case "Savings", "Occasion Fund" -> {

                if (goalId == null) {
                    return false;
                }

                Goal goal = goalService.getGoalById(goalId);

                if (goal == null) {
                    return false;
                }

                goal.addSavings(amount);

                return goalService.updateSavedAmount(goalId, goal.getSavedAmount());
            }

            default -> {
                return false;
            }
        }
    }

    //=========================================================
    // FEATURE 2
    // Unexpected Income Allocation
    //=========================================================
    public IncomeAllocationResult analyzeUnexpectedIncome(Income income) {

        if (income.isExpected()) {
            return null;
        }

        List<String> options = List.of("Monthly Expenses", "Savings", "Occasion Fund");

        String message = String.format(
                "You received an unexpected income of ETB %.2f. Where should it go?",
                income.getAmount());

        return new IncomeAllocationResult(income.getAmount(), options, message);
    }

    //=========================================================
    // Returns the user's active (not-yet-completed) goals, so
    // the GUI can offer them as targets when the destination is
    // "Savings" or "Occasion Fund".
    //=========================================================
    public List<Goal> getActiveGoals(int userId) {

        return goalService.getGoalsByUser(userId).stream()
                .filter(goal -> !goal.isCompleted())
                .toList();
    }

    //=========================================================
    // FEATURE 3
    // Occasion goal shortfall check
    //=========================================================
    public List<OccasionShortfallResult> checkOccasionGoals(int userId) {

        List<Goal> goals = goalService.getGoalsByUser(userId);

        List<OccasionShortfallResult> results = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Goal goal : goals) {

            if (goal instanceof OccasionGoal occasionGoal
                    && !occasionGoal.isCompleted()
                    && !occasionGoal.getDeadline().isAfter(today)) {

                double shortfall = occasionGoal.getTargetAmount() - occasionGoal.getSavedAmount();

                if (shortfall > 0.01) {

                    results.add(new OccasionShortfallResult(
                            occasionGoal.getGoalId(),
                            occasionGoal.getGoalName(),
                            occasionGoal.getTargetAmount(),
                            occasionGoal.getSavedAmount(),
                            shortfall,
                            true,
                            String.format(
                                    "The occasion date for \"%s\" has arrived, but you're short by ETB %.2f. Where should the remaining amount come from?",
                                    occasionGoal.getGoalName(), shortfall)));

                } else {

                    results.add(new OccasionShortfallResult(
                            occasionGoal.getGoalId(),
                            occasionGoal.getGoalName(),
                            occasionGoal.getTargetAmount(),
                            occasionGoal.getSavedAmount(),
                            0,
                            false,
                            String.format(
                                    "Great news! \"%s\" is fully funded.",
                                    occasionGoal.getGoalName())));
                }
            }
        }

        return results;
    }

    //=========================================================
    // Resolves a shortfall — either the monthly budget covers it
    // directly, or money is transferred from a regular savings goal.
    //=========================================================
    public boolean resolveOccasionShortfall(
            String source, int occasionGoalId, double shortfall, Integer savingsGoalId) {

        switch (source) {

            case "Monthly Budget" -> {

                Goal occasionGoal = goalService.getGoalById(occasionGoalId);

                if (occasionGoal == null) {
                    return false;
                }

                occasionGoal.addSavings(shortfall);

                return goalService.updateSavedAmount(occasionGoalId, occasionGoal.getSavedAmount());
            }

            case "Regular Savings" -> {

                if (savingsGoalId == null) {
                    return false;
                }

                Goal savingsGoal = goalService.getGoalById(savingsGoalId);
                Goal occasionGoal = goalService.getGoalById(occasionGoalId);

                if (savingsGoal == null || occasionGoal == null) {
                    return false;
                }

                if (savingsGoal.getSavedAmount() < shortfall) {
                    return false;
                }

                savingsGoal.setSavedAmount(savingsGoal.getSavedAmount() - shortfall);
                occasionGoal.addSavings(shortfall);

                boolean savingsUpdated = goalService.updateSavedAmount(savingsGoalId, savingsGoal.getSavedAmount());
                boolean occasionUpdated = goalService.updateSavedAmount(occasionGoalId, occasionGoal.getSavedAmount());

                return savingsUpdated && occasionUpdated;
            }

            default -> {
                return false;
            }
        }
    }

    //=========================================================
    // FEATURE 4
    // Budget Warnings
    //=========================================================
    public List<BudgetWarning> checkBudgetWarnings(int userId) {

        List<BudgetWarning> warnings = new ArrayList<>();

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        Budget budget = financeService.getCurrentBudget(userId);

        if (budget == null) {
            return warnings;
        }

        double monthlyBudget = budget.getMonthlyBudget();
        double monthlySpent = financeService.getMonthlyExpenseTotal(userId, month, year);

        double percentUsed = monthlyBudget > 0 ? (monthlySpent / monthlyBudget) * 100 : 0;

        if (percentUsed >= 100) {

            warnings.add(new BudgetWarning(
                    "CRITICAL",
                    String.format(
                            "You've exceeded your monthly budget! Spent ETB %.2f of ETB %.2f (%.0f%%).",
                            monthlySpent, monthlyBudget, percentUsed)));

        } else if (percentUsed >= 80) {

            warnings.add(new BudgetWarning(
                    "WARNING",
                    String.format(
                            "You're approaching your monthly budget limit. Spent ETB %.2f of ETB %.2f (%.0f%%).",
                            monthlySpent, monthlyBudget, percentUsed)));
        }

        DailyBudgetResult dailyResult = analyzeDailyBudget(userId);

        if (dailyResult.actualSpentToday() > dailyResult.expectedDailyBudget()) {

            warnings.add(new BudgetWarning(
                    "WARNING",
                    String.format(
                            "Today's spending (ETB %.2f) is higher than your planned daily budget (ETB %.2f).",
                            dailyResult.actualSpentToday(), dailyResult.expectedDailyBudget())));
        }

        return warnings;
    }

    //=========================================================
    // FEATURE 5
    // Savings Suggestions (end of month)
    //=========================================================
    public SavingsSuggestion suggestEndOfMonthSavings(int userId) {

        LocalDate today = LocalDate.now();

        if (today.getDayOfMonth() != today.lengthOfMonth()) {
            return null;
        }

        int month = today.getMonthValue();
        int year = today.getYear();

        Budget budget = financeService.getCurrentBudget(userId);

        if (budget == null) {
            return null;
        }

        double monthlyBudget = budget.getMonthlyBudget();
        double monthlySpent = financeService.getMonthlyExpenseTotal(userId, month, year);

        double leftover = monthlyBudget - monthlySpent;

        if (leftover <= 0.01) {
            return null;
        }

        List<String> options = List.of("Savings", "Occasion Fund");

        String message = String.format(
                "The month is ending with ETB %.2f left in your budget. Want to move it into savings?",
                leftover);

        return new SavingsSuggestion(leftover, options, message);
    }

    //=========================================================
    // FEATURE 6
    // Financial Insights
    //=========================================================
    public List<String> generateFinancialInsights(int userId) {

        List<String> insights = new ArrayList<>();

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        Budget budget = financeService.getCurrentBudget(userId);

        if (budget == null) {
            insights.add("Set a monthly budget to start receiving personalized insights.");
            return insights;
        }

        double monthlyBudget = budget.getMonthlyBudget();

        // Category breakdown
        List<Expense> monthExpenses = financeService.getMonthlyExpenses(userId, month, year);

        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : monthExpenses) {

            categoryTotals.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

            if (monthlyBudget > 0) {

                double percent = (entry.getValue() / monthlyBudget) * 100;

                insights.add(String.format(
                        "%s expenses are %.0f%% of your monthly budget.",
                        entry.getKey(), percent));
            }
        }

        // Budget pace
        double monthlySpent = financeService.getMonthlyExpenseTotal(userId, month, year);

        double percentSpent = monthlyBudget > 0 ? (monthlySpent / monthlyBudget) * 100 : 0;

        int daysRemaining = today.lengthOfMonth() - today.getDayOfMonth();

        insights.add(String.format(
                "You have spent %.0f%% of your budget with %d day(s) remaining.",
                percentSpent, daysRemaining));

        // Month-over-month comparison
        LocalDate previousMonth = today.minusMonths(1);

        double currentSavingsRate = financeService.getSavingsRate(userId, month, year);

        double previousSavingsRate = financeService.getSavingsRate(
                userId, previousMonth.getMonthValue(), previousMonth.getYear());

        if (currentSavingsRate > previousSavingsRate) {

            insights.add("Great job! You're saving more than last month.");

        } else if (currentSavingsRate < previousSavingsRate) {

            insights.add("Heads up — your savings rate is lower than last month.");
        }

        return insights;
    }

}