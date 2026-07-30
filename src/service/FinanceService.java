package service;

import database.DatabaseManager;
import model.Budget;
import model.Expense;
import model.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FinanceService {

    // ==========================================================
    // BUDGET METHODS
    // ==========================================================

    /**
     * Creates a new monthly budget for a user.
     */
    public boolean createBudget(Budget budget) {

        String sql = """
                INSERT INTO Budgets
                (USERID, MonthlyBudget, Month, Year)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, budget.getUserId());
            statement.setDouble(2, budget.getMonthlyBudget());
            statement.setInt(3, budget.getMonth());
            statement.setInt(4, budget.getYear());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing monthly budget.
     */
    public boolean updateBudget(Budget budget) {

        String sql = """
                UPDATE Budgets
                SET MonthlyBudget = ?
                WHERE USERID = ?
                AND Month = ?
                AND Year = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, budget.getMonthlyBudget());
            statement.setInt(2, budget.getUserId());
            statement.setInt(3, budget.getMonth());
            statement.setInt(4, budget.getYear());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the latest budget of a user.
     */
    public Budget getCurrentBudget(int userId) {

        String sql = """
                SELECT *
                FROM Budgets
                WHERE USERID = ?
                ORDER BY Year DESC, Month DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return new Budget(
                        resultSet.getInt("BudgetID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("MonthlyBudget"),
                        resultSet.getInt("Month"),
                        resultSet.getInt("Year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks whether a budget already exists for a user
     * in a given month and year.
     */
    public boolean budgetExists(int userId, int month, int year) {

        String sql = """
                SELECT BudgetID
                FROM Budgets
                WHERE USERID = ?
                AND Month = ?
                AND Year = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the budget for a specific month and year.
     */
    public Budget getBudget(int userId, int month, int year) {

        String sql = """
                SELECT *
                FROM Budgets
                WHERE USERID = ?
                AND Month = ?
                AND Year = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return new Budget(
                        resultSet.getInt("BudgetID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("MonthlyBudget"),
                        resultSet.getInt("Month"),
                        resultSet.getInt("Year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deletes a budget.
     */
    public boolean deleteBudget(int budgetId) {

        String sql = """
                DELETE FROM Budgets
                WHERE BudgetID = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, budgetId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================================
    // EXPENSE METHODS
    // ==========================================================

    /**
     * Adds a new expense.
     */
    public boolean addExpense(Expense expense) {

        String sql = """
                INSERT INTO Expenses
                (USERID, Amount, Category, DateSpent, Description)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expense.getUserId());
            statement.setDouble(2, expense.getAmount());
            statement.setString(3, expense.getCategory());
            statement.setDate(4, java.sql.Date.valueOf(expense.getDateSpent()));
            statement.setString(5, expense.getDescription());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all expenses of a user.
     */
    public ArrayList<Expense> getExpenses(int userId) {

        ArrayList<Expense> expenses = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Expenses
                WHERE USERID = ?
                ORDER BY DateSpent DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Expense expense = new Expense(

                        resultSet.getInt("ExpenseID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("Category"),
                        resultSet.getDate("DateSpent").toLocalDate(),
                        resultSet.getString("Description")

                );

                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    /**
     * Returns all expenses of a specific month.
     */
    public ArrayList<Expense> getMonthlyExpenses(int userId, int month, int year) {

        ArrayList<Expense> expenses = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Expenses
                WHERE USERID = ?
                AND MONTH(DateSpent) = ?
                AND YEAR(DateSpent) = ?
                ORDER BY DateSpent DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Expense expense = new Expense(

                        resultSet.getInt("ExpenseID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("Category"),
                        resultSet.getDate("DateSpent").toLocalDate(),
                        resultSet.getString("Description")

                );

                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    /**
     * Updates an existing expense.
     */
    public boolean updateExpense(Expense expense) {

        String sql = """
                UPDATE Expenses
                SET Amount = ?,
                    Category = ?,
                    DateSpent = ?,
                    Description = ?
                WHERE ExpenseID = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, expense.getAmount());
            statement.setString(2, expense.getCategory());
            statement.setDate(3, java.sql.Date.valueOf(expense.getDateSpent()));
            statement.setString(4, expense.getDescription());
            statement.setInt(5, expense.getExpenseId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes an expense.
     */
    public boolean deleteExpense(int expenseId) {

        String sql = """
                DELETE FROM Expenses
                WHERE ExpenseID = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expenseId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all expenses in a given category.
     */
    public ArrayList<Expense> getExpensesByCategory(int userId, String category) {

        ArrayList<Expense> expenses = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Expenses
                WHERE USERID = ?
                AND Category = ?
                ORDER BY DateSpent DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, category);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Expense expense = new Expense(

                        resultSet.getInt("ExpenseID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("Category"),
                        resultSet.getDate("DateSpent").toLocalDate(),
                        resultSet.getString("Description")

                );

                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public boolean exceededDailyBudget(int userId) {

        LocalDate today = LocalDate.now();

        int month = today.getMonthValue();
        int year = today.getYear();

        Budget budget = getBudget(userId, month, year);

        if (budget == null) {
            return false;
        }

        double dailyBudget = budget.getMonthlyBudget() / today.lengthOfMonth();

        double todayExpenses = getDailyExpenseTotal(userId, today);

        return todayExpenses >= dailyBudget;
    }

    public double getDailyExpenseTotal(int userId, LocalDate date) {

        String sql = """
                SELECT ISNULL(SUM(Amount),0)
                FROM Expenses
                WHERE USERID = ?
                AND DateSpent = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(date));

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    // ==========================================================
    // INCOME METHODS
    // ==========================================================

    /**
     * Adds a new income record.
     */
    public boolean addIncome(Income income) {

        String sql = """
                INSERT INTO Income
                (USERID, Amount, Expected, DateReceived, Description)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, income.getUserId());
            statement.setDouble(2, income.getAmount());
            statement.setBoolean(3, income.isExpected());
            statement.setDate(4, java.sql.Date.valueOf(income.getDateReceived()));
            statement.setString(5, income.getDescription());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all income records of a user.
     */
    public ArrayList<Income> getIncome(int userId) {

        ArrayList<Income> incomes = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Income
                WHERE USERID = ?
                ORDER BY DateReceived DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Income income = new Income(

                        resultSet.getInt("IncomeID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("Amount"),
                        resultSet.getBoolean("Expected"),
                        resultSet.getDate("DateReceived").toLocalDate(),
                        resultSet.getString("Description")

                );

                incomes.add(income);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incomes;
    }

    /**
     * Returns all income records for a specific month.
     */
    public ArrayList<Income> getMonthlyIncome(int userId, int month, int year) {

        ArrayList<Income> incomes = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Income
                WHERE USERID = ?
                AND MONTH(DateReceived) = ?
                AND YEAR(DateReceived) = ?
                ORDER BY DateReceived DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Income income = new Income(

                        resultSet.getInt("IncomeID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("Amount"),
                        resultSet.getBoolean("Expected"),
                        resultSet.getDate("DateReceived").toLocalDate(),
                        resultSet.getString("Description")

                );

                incomes.add(income);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incomes;
    }

    /**
     * Updates an existing income record.
     */
    public boolean updateIncome(Income income) {

        String sql = """
                UPDATE Income
                SET Amount = ?,
                    Expected = ?,
                    DateReceived = ?,
                    Description = ?
                WHERE IncomeID = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, income.getAmount());
            statement.setBoolean(2, income.isExpected());
            statement.setDate(3, java.sql.Date.valueOf(income.getDateReceived()));
            statement.setString(4, income.getDescription());
            statement.setInt(5, income.getIncomeId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes an income record.
     */
    public boolean deleteIncome(int incomeId) {

        String sql = """
                DELETE FROM Income
                WHERE IncomeID = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, incomeId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns only unexpected income.
     */
    public ArrayList<Income> getUnexpectedIncome(int userId) {

        ArrayList<Income> incomes = new ArrayList<>();

        String sql = """
                SELECT *
                FROM Income
                WHERE USERID = ?
                AND Expected = 0
                ORDER BY DateReceived DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Income income = new Income(

                        resultSet.getInt("IncomeID"),
                        resultSet.getInt("USERID"),
                        resultSet.getDouble("Amount"),
                        resultSet.getBoolean("Expected"),
                        resultSet.getDate("DateReceived").toLocalDate(),
                        resultSet.getString("Description")

                );

                incomes.add(income);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incomes;
    }
    // ==========================================================
    // REPORT METHODS
    // ==========================================================

    /**
     * Returns the total monthly income.
     */
    public double getMonthlyIncomeTotal(int userId, int month, int year) {

        String sql = """
                SELECT ISNULL(SUM(Amount), 0)
                FROM Income
                WHERE USERID = ?
                AND MONTH(DateReceived) = ?
                AND YEAR(DateReceived) = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Returns the total monthly expense.
     */
    public double getMonthlyExpenseTotal(int userId, int month, int year) {

        String sql = """
                SELECT ISNULL(SUM(Amount), 0)
                FROM Expenses
                WHERE USERID = ?
                AND MONTH(DateSpent) = ?
                AND YEAR(DateSpent) = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Returns the remaining budget.
     */
    public double getRemainingBudget(int userId, int month, int year) {

        Budget budget = getBudget(userId, month, year);

        if (budget == null) {
            return 0;
        }

        return budget.getMonthlyBudget()
                - getMonthlyExpenseTotal(userId, month, year);
    }

    /**
     * Returns the savings rate as a percentage.
     */
    public double getSavingsRate(int userId, int month, int year) {

        Budget budget = getBudget(userId, month, year);

        if (budget == null || budget.getMonthlyBudget() == 0) {
            return 0;
        }

        double remaining = getRemainingBudget(userId, month, year);

        return (remaining / budget.getMonthlyBudget()) * 100;
    }

    /**
     * Returns the daily budget for the selected month.
     */
    public double getDailyBudget(int userId, int month, int year) {

        Budget budget = getBudget(userId, month, year);

        if (budget == null) {
            return 0;
        }

        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();

        return budget.getMonthlyBudget() / daysInMonth;
    }

    /**
     * Returns the number of expenses in a month.
     */
    public int getMonthlyExpenseCount(int userId, int month, int year) {

        String sql = """
                SELECT COUNT(*)
                FROM Expenses
                WHERE USERID = ?
                AND MONTH(DateSpent) = ?
                AND YEAR(DateSpent) = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Returns the number of income records in a month.
     */
    public int getMonthlyIncomeCount(int userId, int month, int year) {

        String sql = """
                SELECT COUNT(*)
                FROM Income
                WHERE USERID = ?
                AND MONTH(DateReceived) = ?
                AND YEAR(DateReceived) = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Returns the goal completion percentage.
     */
    public double getGoalProgress(int goalId) {

        String sql = """
                SELECT TargetAmount, SavedAmount
                FROM Goals
                WHERE GoalID = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, goalId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                double target = resultSet.getDouble("TargetAmount");
                double saved = resultSet.getDouble("SavedAmount");

                if (target == 0) {
                    return 0;
                }

                return (saved / target) * 100;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}