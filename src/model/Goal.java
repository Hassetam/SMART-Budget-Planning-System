package model;

import java.time.LocalDate;
import interfaces.Analyzable;

public abstract class Goal implements Analyzable {

    // Goal information
    private int goalId;
    private String goalName;
    private double targetAmount;
    private double savedAmount;
    private LocalDate deadline;
    private boolean completed;

    // Default Constructor
    public Goal() {

    }

    // Constructor used when creating a new goal
    public Goal(String goalName, double targetAmount,
                double savedAmount, LocalDate deadline,
                boolean completed) {

        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
        this.completed = completed;
    }

    // Constructor used when retrieving a goal from the database
    public Goal(int goalId, String goalName,
                double targetAmount, double savedAmount,
                LocalDate deadline, boolean completed) {

        this.goalId = goalId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
        this.completed = completed;
    }

    // Getters 

    public int getGoalId() {
        return goalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getSavedAmount() {
        return savedAmount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    //  Setters 

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setSavedAmount(double savedAmount) {
        this.savedAmount = savedAmount;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    //  Goal Methods 

    public void addSavings(double amount) {
        this.savedAmount += amount;
    }

    // Implemented differently by child classes
    @Override
    public abstract String analyze();

    @Override
    public String toString() {
        return goalName + " (" + savedAmount + "/" + targetAmount + ")";
    }
}