package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OccasionGoal extends Goal {

    private String occasionType;

    // Default Constructor
    public OccasionGoal() {

    }

    // Constructor for creating a new occasion goal
    public OccasionGoal(String goalName,
                        double targetAmount,
                        double savedAmount,
                        LocalDate deadline,
                        boolean completed,
                        String occasionType) {

        super(goalName, targetAmount, savedAmount,
                deadline, completed);

        this.occasionType = occasionType;
    }

    // Constructor for retrieving from the database
    public OccasionGoal(int goalId,
                        String goalName,
                        double targetAmount,
                        double savedAmount,
                        LocalDate deadline,
                        boolean completed,
                        String occasionType) {

        super(goalId, goalName, targetAmount,
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

        long daysLeft =
                ChronoUnit.DAYS.between(LocalDate.now(), getDeadline());

        return "Occasion Goal: "
                + occasionType
                + " | "
                + daysLeft
                + " day(s) remaining.";
    }

}