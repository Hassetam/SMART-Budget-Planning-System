package model;

public class Budget {

    // Budget information
    private int budgetId;
    // added userId
    private int userId;
    private double monthlyBudget;
    private int month;
    private int year;

    // Default Constructor
    public Budget() {

    }

    // Constructor used when creating a new budget
    public Budget(int userId, double monthlyBudget, int month, int year) {
        this.userId = userId; // need a userId when creating a new Budget
        this.monthlyBudget = monthlyBudget;
        this.month = month;
        this.year = year;
    }

    // Constructor used when retrieving a budget from the database
    public Budget(int budgetId, int userId, double monthlyBudget, int month, int year) {
        this.budgetId = budgetId;
        this.userId = userId; // Added userId
        this.monthlyBudget = monthlyBudget;
        this.month = month;
        this.year = year;
    }

    // Getters

    public int getBudgetId() {
        return budgetId;
    }

    // added getter method for userId
    public int getUserId() {
        return userId;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    // Setters
    // added setter methods for budgetId and userId
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Display

    @Override
    public String toString() {
        return "Budget: " + monthlyBudget +
                " | Month: " + month +
                " | Year: " + year;
    }
}