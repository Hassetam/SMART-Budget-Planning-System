package model;

import java.time.LocalDate;

public class Expense {

    // Expense information
    private int expenseId;
    //added userId
    private int userId;
    private double amount;
    private String category;
    private LocalDate expenseDate;
    private String description;

    // Default Constructor
    public Expense() {

    }

    // Constructor used when creating a new expense
    public Expense(int userId,double amount, String category,
                   LocalDate expenseDate, String description) {
        this.userId = userId; //needed
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
        this.description = description;
    }

    // Constructor used when retrieving an expense from the database
    public Expense(int expenseId, int userId, double amount, String category,
                   LocalDate expenseDate, String description) {

        this.expenseId = expenseId; 
        this.userId = userId; //needed
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
        this.description = description;
    }

    //  Getters 

    public int getExpenseId() {
        return expenseId;
    }

    //added getter method for userId
    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public String getDescription() {
        return description;
    }

    //  Setters 
    //setter methods for expenseId and userId
    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Display 

    @Override
    public String toString() {
        return category + " | " + amount + " | " + expenseDate;
    }
}