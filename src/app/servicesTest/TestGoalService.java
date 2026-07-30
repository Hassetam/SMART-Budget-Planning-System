package app.servicesTest;


import java.time.LocalDate;
import java.util.List;
import model.GeneralGoal;
import model.Goal;
import service.GoalService;

public class TestGoalService {

    private static final GoalService goalService = new GoalService();
    private static int lastDeletedGoalId;

    public static void main(String[] args) {
        //call the methods here one-by-one
        testAddGeneralGoal();
        testGetGoalsByUser();
        testGetGoalById();
        testUpdateGoal();
        testUpdateSavedAmount();
        testSetGoalCompleted();
        testDeleteGoal();
        testRestoreGoal();
    }

    //=========================================================
    // Prints the title of the current test
    //=========================================================
    private static void printTestHeader(int number, String title) {

        System.out.println();
        System.out.println("==================================================");
        System.out.println("TEST " + number + " : " + title);
        System.out.println("==================================================");
    }

    //=========================================================
    // TEST 1
    // Add General Goal
    //=========================================================
    public static void testAddGeneralGoal() {

        printTestHeader(1, "ADD GENERAL GOAL");

        GeneralGoal goal = new GeneralGoal(
                1,
                "Emergency Fund",
                "General",
                50000,
                10000,
                LocalDate.of(2027, 1, 1),
                false
        );

        boolean success = goalService.addGoal(goal);

        if (success) {

            System.out.println("PASS : Goal added successfully.");

        } else {

            System.out.println("FAIL : Goal was not added.");
        }
    }

    //=========================================================
    // TEST 2
    // Get all goals of a user
    //=========================================================
    public static void testGetGoalsByUser() {

        printTestHeader(2, "GET GOALS BY USER");

        List<Goal> goals = goalService.getGoalsByUser(1);

        if (goals.isEmpty()) {

            System.out.println("No goals found.");

        } else {

            System.out.println("Number of goals found: " + goals.size());

            System.out.println();

            for (Goal goal : goals) {

                System.out.println(goal);
                System.out.println("--------------------------------------------");
            }
        }
    }

    //=========================================================
    // TEST 3
    // Get a goal by its ID
    //=========================================================
    public static void testGetGoalById() {

        printTestHeader(3, "GET GOAL BY ID");

        int goalId = goalService.getGoalsByUser(1).get(0).getGoalId();

        Goal goal = goalService.getGoalById(goalId);

        if (goal == null) {

            System.out.println("FAIL : No goal found with that ID.");

        } else {

            System.out.println("PASS : Goal found.");
            System.out.println(goal);
        }
    }


    //=========================================================
    // TEST 4
    // Update an existing goal
    //=========================================================
    public static void testUpdateGoal() {

        printTestHeader(4, "UPDATE GOAL");

        int goalId = goalService.getGoalsByUser(1).get(0).getGoalId();

        Goal goal = goalService.getGoalById(goalId);

        if (goal == null) {

            System.out.println("FAIL : No goal found to update.");
            return;
        }

        goal.setGoalName("Emergency Fund (Updated)");
        goal.setTargetAmount(60000);

        boolean success = goalService.updateGoal(goal);

        if (success) {

            System.out.println("PASS : Goal updated successfully.");
            System.out.println(goalService.getGoalById(goalId));

        } else {

            System.out.println("FAIL : Goal was not updated.");
        }
    }

    //=========================================================
    // TEST 5
    // Update the saved amount of a goal
    //=========================================================
    public static void testUpdateSavedAmount() {

        printTestHeader(5, "UPDATE SAVED AMOUNT");

        int goalId = goalService.getGoalsByUser(1).get(0).getGoalId();

        boolean success = goalService.updateSavedAmount(goalId, 25000);

        if (success) {

            System.out.println("PASS : Saved amount updated successfully.");
            System.out.println(goalService.getGoalById(goalId));

        } else {

            System.out.println("FAIL : Saved amount was not updated.");
        }
    }

    //=========================================================
    // TEST 6
    // Set a goal's completed status
    //=========================================================
    public static void testSetGoalCompleted() {

        printTestHeader(6, "SET GOAL COMPLETED");

        int goalId = goalService.getGoalsByUser(1).get(0).getGoalId();

        boolean success = goalService.setGoalCompleted(goalId, true);

        if (success) {

            System.out.println("PASS : Goal completion status updated.");
            System.out.println(goalService.getGoalById(goalId));

        } else {

            System.out.println("FAIL : Goal completion status was not updated.");
        }
    }


     //=========================================================
    // TEST 7
    // Soft-delete a goal (move it to DeletedGoals)
    //=========================================================
    public static void testDeleteGoal() {

        printTestHeader(7, "DELETE GOAL (SOFT DELETE)");

        int goalId = goalService.getGoalsByUser(1).get(0).getGoalId();

        lastDeletedGoalId = goalId;

        boolean success = goalService.deleteGoal(goalId);

        if (success) {

            System.out.println("PASS : Goal moved to DeletedGoals.");
            System.out.println("Remaining goals: " + goalService.getGoalsByUser(1).size());

        } else {

            System.out.println("FAIL : Goal was not deleted.");
        }
    }

    //=========================================================
    // TEST 8
    // Restore a soft-deleted goal
    //=========================================================
    public static void testRestoreGoal() {

        printTestHeader(8, "RESTORE GOAL");

        boolean success = goalService.restoreGoal(lastDeletedGoalId);

        if (success) {

            System.out.println("PASS : Goal restored.");
            System.out.println(goalService.getGoalById(lastDeletedGoalId));

        } else {

            System.out.println("FAIL : Goal was not restored.");
        }
    }

}
