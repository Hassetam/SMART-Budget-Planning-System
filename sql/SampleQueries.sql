-- ---------------------------------------------VIEW
-- View all Users
SELECT *
FROM Users;

-- View all Budgets
SELECT *
FROM Budgets;

-- View all Expenses
SELECT *
From Expenses;

-- View all Income
SELECT *
FROM Income;

-- View all Goals
SELECT *
FROM Goals;

-- Change the UserID below to test different users
-- -------------------------------------------Filtering
--Expenses greater than 500
SELECT * 
FROM Expenses
WHERE USERID = 1 
    AND Amount > 500;

-- Budget for 2026
SELECT *
FROM Budgets
WHERE USERID = 1
    AND Year = 2026;

-- Short term goals
SELECT *
FROM Goals
WHERE USERID = 1
    AND GoalType = 'Short term';

--Unexpected income
SELECT *
FROM Income
WHERE USERID = 1
    AND Expected = 0;

-- ---------------------------------------------SORTING
-- Newset expenses
SELECT *
FROM Expenses
WHERE USERID = 1 
ORDER BY Date DESC;

-- Highest budget
SELECT *
FROM Budgets
WHERE USERID = 1
ORDER BY MonthlyBudget DESC;


-- --------------------------------------Aggregate function

-- -------------------------------------Monthly
-- -------------Income
-- Monthly Total Income
SELECT SUM(Amount) AS TotalMonthlyIncome
FROM Income
WHERE USERID = 1
    AND MONTH(Date) = 7
    AND YEAR(Date) = 2026;

-- Monthly average income
SELECT AVG(Amount) AS AverageMonthlyIncome
FROM Income
WHERE UserID = 1
  AND MONTH(Date) = 7
  AND YEAR(Date) = 2026;

-- Monthly Highest income
SELECT MAX(Amount) AS HighestMonthlyIncome
FROM Income
WHERE UserID = 1
  AND MONTH(Date) = 7
  AND YEAR(Date) = 2026;

-- Monthly Lowest income
SELECT MIN(Amount) AS LowestMonthlyIncome
FROM Income
WHERE UserID = 1
  AND MONTH(Date) = 7
  AND YEAR(Date) = 2026;

-- monthly number of income 
SELECT COUNT(*) AS NumberOfMonthlyIncomeRecords
FROM Income
WHERE UserID = 1
  AND MONTH(Date) = 7
  AND YEAR(Date) = 2026;


-- -----------Expenses
-- Monthly Total Expense 
SELECT SUM(Amount) AS TotalMonthlyExpense
FROM Expenses
WHERE USERID = 1
    AND MONTH(Date) = 7
    AND YEAR(Date) = 2026;

-- Monthly Average Expense
SELECT AVG(Amount) AS AverageMonthlyExpense
FROM Expenses
WHERE USERID = 1
    AND MONTH(Date) = 7
    AND YEAR(Date) = 2026;

-- Monthly Highest Expense
SELECT MAX(Amount) AS HighestMontlyExpense
FROM Expenses
where USERID = 1
    AND MONTH(Date) = 7
    AND YEAR(Date) = 2026;

-- Monthly Lowest expense
SELECT MIN(Amount) AS LowestMonthlyExpense
FROM Expenses
WHERE USERID = 1
    AND MONTH(Date) = 7
    AND YEAR(Date) = 2026;

-- Monthly Number of expenses
SELECT COUNT(*) AS NumberOfMonthlyExpense
FROM Expenses
WHERE USERID = 1
    AND MONTH(Date) = 7
    AND YEAR(Date) = 2026;


-- ------------------------------------------Yearly
-- -----------Income
-- Yearly Total Income
SELECT SUM(Amount) AS TotalYearlyIncome
FROM Income
WHERE USERID = 1
    AND YEAR(Date) = 2026;


-- Yearly average income
SELECT ROUND(AVG(Amount),2) AS AverageYearlyIncome
FROM Income
WHERE UserID = 1
  AND YEAR(Date) = 2026;

-- Yearly Highest income
SELECT MAX(Amount) AS HighestYearlyIncome
FROM Income
WHERE UserID = 1
  AND YEAR(Date) = 2026;

-- Yearly Lowest income
SELECT MIN(Amount) AS LowestYearlyIncome
FROM Income
WHERE UserID = 1
  AND YEAR(Date) = 2026;

-- Yearly number of income 
SELECT COUNT(*) AS NumberOfYearlyIncomeRecords
FROM Income
WHERE UserID = 1
  AND YEAR(Date) = 2026;

-- -----------Expenses
-- Yearly Total Expense 
SELECT SUM(Amount) AYearlyYearlyExpense
FROM Expenses
WHERE USERID = 1
    AND YEAR(Date) = 2026;

-- Yearly Average Expense
SELECT AVG(Amount) AS AverageYearlyExpense
FROM Expenses
WHERE USERID = 1
    AND YEAR(Date) = 2026;

-- Yearly Highest Expense
SELECT MAX(Amount) AS HighestYearlyExpense
FROM Expenses
where USERID = 1
    AND YEAR(Date) = 2026;

-- Yearly Lowest expense
SELECT MIN(Amount) AS LowestYearlyExpense
FROM Expenses
WHERE USERID = 1
    AND YEAR(Date) = 2026;

-- Yearly Number of expenses
SELECT COUNT(*) AS NumberOfYearlyExpense
FROM Expenses
WHERE USERID = 1
    AND YEAR(Date) = 2026;

-- Remaining Budget(montly budget left after expenses)
SELECT
    b.MonthlyBudget,
    ISNULL(SUM(e.Amount), 0) AS TotalExpenses,
    b.MonthlyBudget - ISNULL(SUM(e.Amount), 0) AS RemainingBudget
FROM Budgets b
LEFT JOIN Expenses e
    ON b.UserID = e.UserID
    AND MONTH(e.Date) = b.Month
    AND YEAR(e.Date) = b.Year
WHERE b.UserID = 1
  AND b.Month = 7
  AND b.Year = 2026
GROUP BY b.MonthlyBudget;

-- Savings Rate(percentage of the budget still available)
SELECT
    ((b.MonthlyBudget - ISNULL(SUM(e.Amount),0))
        / b.MonthlyBudget) * 100 AS SavingsRate
FROM Budgets b
LEFT JOIN Expenses e
    ON b.UserID = e.UserID
    AND MONTH(e.Date) = b.Month
    AND YEAR(e.Date) = b.Year
WHERE b.UserID = 1
  AND b.Month = 7
  AND b.Year = 2026
GROUP BY b.MonthlyBudget;

-- Goal Progress
SELECT
    GoalName,
    TargetAmount,
    SavedAmount,
    Round((SavedAmount / TargetAmount) * 100,2) AS GoalProgress
FROM Goals
WHERE UserID = 1;
