package app.servicesTest;


import java.time.LocalDate;
import java.util.List;
import model.Goal;
import model.Income;
import service.BudgetWarning;
import service.DailyBudgetResult;
import service.FinanceService;
import service.IncomeAllocationResult;
import service.OccasionShortfallResult;
import service.SavingsSuggestion;
import service.SmartAdvisorService;

public class TestSmartAdvisorService {
    private static final SmartAdvisorService smartAdvisorService = new SmartAdvisorService();
    private static final FinanceService financeService = new FinanceService();

    public static void main(String[] args) {
        testAnalyzeDailyBudget();
        testAnalyzeUnexpectedIncome();
        testCheckOccasionGoals();
        testCheckBudgetWarnings();
        testSuggestEndOfMonthSavings();
        testGenerateFinancialInsights();
    }

    //=========================================================
    // Prints the title of the current test
    //=========================================================
    private static void printTestHeader(int number, String title) {

        System.out.println();
        System.out.println("==================================================");
        System.out.println("TEST " + number + " : " + title);
        System.out.println("==================================================");
    }

    //=========================================================
    // TEST 1
    // Analyze today's daily budget
    //=========================================================
    public static void testAnalyzeDailyBudget() {

        printTestHeader(1, "ANALYZE DAILY BUDGET");

        DailyBudgetResult result = smartAdvisorService.analyzeDailyBudget(1);

        System.out.println(result.message());

        if (result.needsAllocationDecision()) {

            System.out.println("Decision needed — surplus of ETB " + result.difference());

            // Simulating "Monthly Expenses" for now — the real GUI will
            // collect this choice from a dialog once SmartAdvisorPanel exists.
            boolean applied = smartAdvisorService.allocateFunds(
                    "Monthly Expenses", result.difference(), null);

            System.out.println(applied
                    ? "PASS : Allocation applied."
                    : "FAIL : Allocation not applied.");

        } else {

            System.out.println("PASS : No decision needed today.");
        }
    }

    //=========================================================
    // TEST 2
    // Analyze unexpected income allocation
    //=========================================================
    public static void testAnalyzeUnexpectedIncome() {

        printTestHeader(2, "ANALYZE UNEXPECTED INCOME");

        Income income = new Income(1, 500, false, LocalDate.now(), "Birthday gift");

        boolean added = financeService.addIncome(income);

        if (!added) {
            System.out.println("FAIL : Income was not added.");
            return;
        }

        IncomeAllocationResult result = smartAdvisorService.analyzeUnexpectedIncome(income);

        if (result == null) {

            System.out.println("FAIL : Income was not flagged as unexpected.");
            return;
        }

        System.out.println(result.message());
        System.out.println("Options: " + result.options());

        List<Goal> activeGoals = smartAdvisorService.getActiveGoals(1);

        if (activeGoals.isEmpty()) {

            System.out.println("No active goals to allocate toward — skipping allocation.");
            return;
        }

        int goalId = activeGoals.get(0).getGoalId();

        boolean applied = smartAdvisorService.allocateFunds("Savings", result.amount(), goalId);

        System.out.println(applied ? "PASS : Allocation applied." : "FAIL : Allocation not applied.");
    }

    //=========================================================
    // TEST 3
    // Check occasion goals for shortfall on their deadline
    //=========================================================
    public static void testCheckOccasionGoals() {

        printTestHeader(3, "CHECK OCCASION GOALS");

        List<OccasionShortfallResult> results = smartAdvisorService.checkOccasionGoals(1);

        if (results.isEmpty()) {

            System.out.println("No occasion goals reaching their deadline today.");
            return;
        }

        for (OccasionShortfallResult result : results) {

            System.out.println(result.message());

            if (result.needsFundingDecision()) {

                boolean resolved = smartAdvisorService.resolveOccasionShortfall(
                        "Monthly Budget", result.goalId(), result.shortfall(), null);

                System.out.println(resolved
                        ? "PASS : Shortfall resolved."
                        : "FAIL : Shortfall not resolved.");
            }
        }
    }

    //=========================================================
    // TEST 4
    // Check budget warnings
    //=========================================================
    public static void testCheckBudgetWarnings() {

        printTestHeader(4, "CHECK BUDGET WARNINGS");

        List<BudgetWarning> warnings = smartAdvisorService.checkBudgetWarnings(1);

        if (warnings.isEmpty()) {

            System.out.println("No warnings — spending is within a healthy range.");

        } else {

            for (BudgetWarning warning : warnings) {

                System.out.println("[" + warning.level() + "] " + warning.message());
            }
        }
    }

    //=========================================================
    // TEST 5
    // Suggest end-of-month savings
    //=========================================================
    public static void testSuggestEndOfMonthSavings() {

        printTestHeader(5, "SUGGEST END OF MONTH SAVINGS");

        SavingsSuggestion suggestion = smartAdvisorService.suggestEndOfMonthSavings(1);

        if (suggestion == null) {

            System.out.println("Not the end of the month yet — no suggestion to make.");
            return;
        }

        System.out.println(suggestion.message());
        System.out.println("Options: " + suggestion.options());

        List<Goal> activeGoals = smartAdvisorService.getActiveGoals(1);

        if (activeGoals.isEmpty()) {

            System.out.println("No active goals to move leftover funds into — skipping.");
            return;
        }

        int goalId = activeGoals.get(0).getGoalId();

        boolean applied = smartAdvisorService.allocateFunds("Savings", suggestion.leftoverAmount(), goalId);

        System.out.println(applied ? "PASS : Leftover moved to savings." : "FAIL : Leftover was not moved.");
    }

    //=========================================================
    // TEST 6
    // Generate financial insights
    //=========================================================
    public static void testGenerateFinancialInsights() {

        printTestHeader(6, "GENERATE FINANCIAL INSIGHTS");

        List<String> insights = smartAdvisorService.generateFinancialInsights(1);

        if (insights.isEmpty()) {

            System.out.println("No insights available yet.");

        } else {

            for (String insight : insights) {

                System.out.println("- " + insight);
            }
        }
    }
}
