package model;

import java.time.LocalDate;

public class Income {

    // Income information
    private int incomeId;
    private double amount;
    private String incomeType;
    private LocalDate dateReceived;
    private String description;

    // Default Constructor
    public Income() {

    }

    // Constructor used when creating a new income
    public Income(double amount, String incomeType,
                  LocalDate dateReceived, String description) {

        this.amount = amount;
        this.incomeType = incomeType;
        this.dateReceived = dateReceived;
        this.description = description;
    }

    // Constructor used when retrieving income from the database
    public Income(int incomeId, double amount, String incomeType,
                  LocalDate dateReceived, String description) {

        this.incomeId = incomeId;
        this.amount = amount;
        this.incomeType = incomeType;
        this.dateReceived = dateReceived;
        this.description = description;
    }

    //  Getters 

    public int getIncomeId() {
        return incomeId;
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