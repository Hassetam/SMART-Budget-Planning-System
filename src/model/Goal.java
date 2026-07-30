package model;

import interfaces.Analyzable;
import java.time.LocalDate;

public abstract class Goal implements Analyzable {

    // Goal information
    private int goalId;
    private int userId; // added userId
    private String goalName;
    private String goalType; // added goaltype
    private double targetAmount;
    private double savedAmount;
    private LocalDate deadline;
    private boolean completed;

    // Default Constructor
    public Goal() {

    }

    // Constructor used when creating a new goal
    public Goal(int userId, String goalName, String goalType, double targetAmount,
            double savedAmount, LocalDate deadline,
            boolean completed) {
        this.userId = userId; // needed
        this.goalName = goalName;
        this.goalType = goalType; // added goalType
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
        this.completed = completed;
    }

    // Constructor used when retrieving a goal from the database
    public Goal(int goalId, int userId, String goalName,
            String goalType, double targetAmount, double savedAmount,
            LocalDate deadline, boolean completed) {

        this.goalId = goalId;
        this.userId = userId;
        this.goalName = goalName;
        this.goalType = goalType;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
        this.completed = completed;
    }

    // Getters
    public int getGoalId() {
        return goalId;
    }

    // Added getter method for the userId
    public int getUserId() {
        return userId;
    }

    public String getGoalName() {
        return goalName;
    }

    // added getter method for the goalType
    public String getGoalType() {
        return goalType;
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

    // Setters
    // added setter methods for goalId and userId
    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    // added setter method for goalType
    public void setGoalType(String goalType) {
        this.goalType = goalType;
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

    // Goal Methods
    public void addSavings(double amount) {
        this.savedAmount += amount;
    }

    // Implemented differently by child classes
    @Override
    public String toString() {

        return """
                Goal ID        : %d
                User ID        : %d
                Goal Name      : %s
                Goal Type      : %s
                Target Amount  : %.2f
                Saved Amount   : %.2f
                Deadline       : %s
                Completed      : %s
                """
                .formatted(
                        goalId,
                        userId,
                        goalName,
                        goalType,
                        targetAmount,
                        savedAmount,
                        deadline,
                        completed ? "Yes" : "No");
    }
}
