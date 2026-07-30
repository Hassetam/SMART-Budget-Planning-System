package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OccasionGoal extends Goal {

    private String occasionType;

    // Default Constructor
    public OccasionGoal() {

    }

    // Constructor for creating a new occasion goal
    public OccasionGoal(int userId, // added
            String goalName,
            String goalType, // Added goaltype, this constructor calls the Goal constructor
            double targetAmount,
            double savedAmount,
            LocalDate deadline,
            boolean completed,
            String occasionType) {

        super(userId, goalName, goalType, targetAmount, savedAmount,
                deadline, completed);

        this.occasionType = occasionType;
    }

    // Constructor for retrieving from the database
    public OccasionGoal(int goalId,
            int userId, // added
            String goalName,
            String goalType, // added
            double targetAmount,
            double savedAmount,
            LocalDate deadline,
            boolean completed,
            String occasionType) {

        super(goalId, userId, goalName, goalType, targetAmount,
                savedAmount, deadline, completed);

        this.occasionType = occasionType;
    }

    public String getOccasionType() {
        return occasionType;
    }

    public void setOccasionType(String occasionType) {
        this.occasionType = occasionType;
    }

    @Override
    public String analyze() {

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), getDeadline());

        return "Occasion Goal: "
                + occasionType
                + " | "
                + daysLeft
                + " day(s) remaining.";
    }

    @Override
    public String toString() {

        return super.toString()
                + "Occasion Type : " + occasionType + "\n";
    }

}
