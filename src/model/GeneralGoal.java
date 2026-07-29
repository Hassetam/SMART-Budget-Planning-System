package model;

import java.time.LocalDate;

public class GeneralGoal extends Goal {

    // Default Constructor
    public GeneralGoal() {

    }

    // Constructor for creating a new goal
    public GeneralGoal(int userId,
                       String goalName,
                       String goalType,
                       double targetAmount,
                       double savedAmount,
                       LocalDate deadline,
                       boolean completed) {

        super(userId, goalName, goalType, targetAmount, savedAmount, deadline, completed);
    }

    // Constructor for retrieving from the database
    public GeneralGoal(int goalId,
                       int userId,
                       String goalName,
                       String goalType,
                       double targetAmount,
                       double savedAmount,
                       LocalDate deadline,
                       boolean completed) {

        super(goalId, userId, goalName, goalType, targetAmount,
              savedAmount, deadline, completed);
    }

    @Override
    public String analyze() {

        double percentage = (getSavedAmount() / getTargetAmount()) * 100;

        return "General Goal Progress: "
                + String.format("%.2f", percentage)
                + "% completed.";
    }

}