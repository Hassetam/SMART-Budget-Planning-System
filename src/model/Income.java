package model;

import java.time.LocalDate;

public class Income {

    // Income information
    private int incomeId;
    private int userId;
    private double amount;
    private String incomeType;
    private LocalDate dateReceived;
    private String description;

    // Default Constructor
    public Income() {

    }

    // Constructor used when creating a new income
    public Income(int userId, double amount, String incomeType,
                  LocalDate dateReceived, String description) {
        this.userId =userId; //needed
        this.amount = amount;
        this.incomeType = incomeType;
        this.dateReceived = dateReceived;
        this.description = description;
    }

    // Constructor used when retrieving income from the database
    public Income(int incomeId, int userId, double amount, String incomeType,
                  LocalDate dateReceived, String description) {

        this.incomeId = incomeId;
        this.userId = userId; //needed
        this.amount = amount;
        this.incomeType = incomeType;
        this.dateReceived = dateReceived;
        this.description = description;
    }

    //  Getters 

    public int getIncomeId() {
        return incomeId;
    }

    //added getter method for userId
    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public String getDescription() {
        return description;
    }

    //  Setters 
    //added setter methods for incomeId and userId
    public void getIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public void getUserId(int userId) {
        this.userId = userId;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Display

    @Override
    public String toString() {
        return incomeType + " | " + amount + " | " + dateReceived;
    }
}