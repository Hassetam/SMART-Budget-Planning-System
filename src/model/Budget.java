package model;

public class Budget {

    // Budget information
    private int budgetId;
    private double monthlyBudget;
    private int month;
    private int year;

    // Default Constructor
    public Budget() {

    }

    // Constructor used when creating a new budget
    public Budget(double monthlyBudget, int month, int year) {
        this.monthlyBudget = monthlyBudget;
        this.month = month;
        this.year = year;
    }

    // Constructor used when retrieving a budget from the database
    public Budget(int budgetId, double monthlyBudget, int month, int year) {
        this.budgetId = budgetId;
        this.monthlyBudget = monthlyBudget;
        this.month = month;
        this.year = year;
    }

    // Getters 

    public int getBudgetId() {
        return budgetId;
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

    //  Setters 

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    //  Display 

    @Override
    public String toString() {
        return "Budget: " + monthlyBudget +
               " | Month: " + month +
               " | Year: " + year;
    }
}