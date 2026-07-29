package app.servicesTest;

import model.Budget;
import model.Expense;
import model.Income;
import service.FinanceService;

import java.time.LocalDate;

public class FinanceServiceTest {

        public static void main(String[] args) {

                FinanceService financeService = new FinanceService();

                System.out.println("=================================");
                System.out.println("      Finance Service Testing");
                System.out.println("=================================");

                // =====================================================
                // BUDGET TESTS
                // =====================================================

                System.out.println("\n========== BUDGET TESTS ==========");

                Budget budget = new Budget(
                                0,
                                1,
                                6000,
                                8,
                                2026);

                if (financeService.createBudget(budget))
                        System.out.println("Create Budget: PASSED");
                else
                        System.out.println("Create Budget: FAILED");

                Budget current = financeService.getCurrentBudget(1);

                if (current != null)
                        System.out.println("Get Current Budget: PASSED");
                else
                        System.out.println("Get Current Budget: FAILED");

                // =====================================================
                // EXPENSE TESTS
                // =====================================================

                System.out.println("\n========== EXPENSE TESTS ==========");

                Expense expense = new Expense(
                                0,
                                1,
                                250,
                                "Food",
                                LocalDate.now(),
                                "Lunch");

                if (financeService.addExpense(expense))
                        System.out.println("Add Expense: PASSED");
                else
                        System.out.println("Add Expense: FAILED");

                System.out.println("Total Expenses: "
                                + financeService.getExpenses(1).size());

                System.out.println("Monthly Expenses: "
                                + financeService.getMonthlyExpenses(
                                                1,
                                                LocalDate.now().getMonthValue(),
                                                LocalDate.now().getYear()).size());

                // =====================================================
                // INCOME TESTS
                // =====================================================

                System.out.println("\n========== INCOME TESTS ==========");

                Income income = new Income(
                                1,
                                500,
                                false,
                                LocalDate.now(),
                                "Gift");

                if (financeService.addIncome(income))
                        System.out.println("Add Income: PASSED");
                else
                        System.out.println("Add Income: FAILED");

                System.out.println("Total Income Records: "
                                + financeService.getIncome(1).size());

                System.out.println("Unexpected Income: "
                                + financeService.getUnexpectedIncome(1).size());

                // =====================================================
                // REPORT TESTS
                // =====================================================

                System.out.println("\n========== REPORT TESTS ==========");

                int month = LocalDate.now().getMonthValue();
                int year = LocalDate.now().getYear();

                System.out.println("Monthly Income: "
                                + financeService.getMonthlyIncomeTotal(1, month, year));

                System.out.println("Monthly Expense: "
                                + financeService.getMonthlyExpenseTotal(1, month, year));

                System.out.println("Remaining Budget: "
                                + financeService.getRemainingBudget(1, month, year));

                System.out.println("Savings Rate: "
                                + financeService.getSavingsRate(1, month, year) + "%");

                System.out.println("Monthly Expense Count: "
                                + financeService.getMonthlyExpenseCount(1, month, year));

                System.out.println("Monthly Income Count: "
                                + financeService.getMonthlyIncomeCount(1, month, year));

                System.out.println("Goal Progress: "
                                + financeService.getGoalProgress(1) + "%");

                System.out.println("\n=================================");
                System.out.println("      Testing Complete");
                System.out.println("=================================");
        }
}
