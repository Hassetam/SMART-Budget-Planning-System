package service;

import database.DatabaseManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.GeneralGoal;
import model.Goal;
import model.OccasionGoal;

public class GoalService {

    //=========================================================
    // STEP 1
    // Add a new goal
    //=========================================================
    public boolean addGoal(Goal goal) {

        String sql = """
            INSERT INTO Goals
            (UserID,
             GoalName,
             GoalType,
             OccasionType,
             TargetAmount,
             SavedAmount,
             Deadline,
             Completed)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            // User information
            statement.setInt(1, goal.getUserId());

            // Goal information
            statement.setString(2, goal.getGoalName());
            statement.setString(3, goal.getGoalType());

            /*
             * Only OccasionGoal has an OccasionType.
             * GeneralGoal stores NULL.
             */
            if (goal instanceof OccasionGoal occasionGoal) {

                statement.setString(
                        4,
                        occasionGoal.getOccasionType()
                );

            } else {

                statement.setNull(
                        4,
                        Types.VARCHAR
                );
            }

            statement.setDouble(
                    5,
                    goal.getTargetAmount()
            );

            statement.setDouble(
                    6,
                    goal.getSavedAmount()
            );

            statement.setDate(
                    7,
                    Date.valueOf(goal.getDeadline())
            );

            statement.setBoolean(
                    8,
                    goal.isCompleted()
            );

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("Error adding goal.");

            e.printStackTrace();

            return false;
        }
    }

    //=========================================================
    // STEP 2
    // Get all goals belonging to one user
    //=========================================================
    public List<Goal> getGoalsByUser(int userId) {

        List<Goal> goals = new ArrayList<>();

        String sql = """
            SELECT *
            FROM Goals
            WHERE USERID = ?
            ORDER BY GoalID
            """;

        try (
                Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int goalId = resultSet.getInt("GoalID");
                String goalName = resultSet.getString("GoalName");
                String goalType = resultSet.getString("GoalType");
                String occasionType = resultSet.getString("OccasionType");

                double targetAmount = resultSet.getDouble("TargetAmount");
                double savedAmount = resultSet.getDouble("SavedAmount");

                Date deadline = resultSet.getDate("Deadline");

                boolean completed = resultSet.getBoolean("Completed");

                Goal goal;

                if (goalType.equalsIgnoreCase("Occasion")) {

                    goal = new OccasionGoal(
                            goalId,
                            userId,
                            goalName,
                            goalType,
                            targetAmount,
                            savedAmount,
                            deadline.toLocalDate(),
                            completed,
                            occasionType
                    );

                } else {

                    goal = new GeneralGoal(
                            goalId,
                            userId,
                            goalName,
                            goalType,
                            targetAmount,
                            savedAmount,
                            deadline.toLocalDate(),
                            completed
                    );
                }

                goals.add(goal);
            }

        } catch (SQLException e) {

            System.out.println("Error retrieving goals.");

            e.printStackTrace();
        }

        return goals;
    }

    //=========================================================
    // STEP 3
    // Get a single goal by its ID
    //=========================================================
    public Goal getGoalById(int goalId) {

        String sql = """
            SELECT *
            FROM Goals
            WHERE GoalID = ?
            """;

        try (
                Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, goalId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int userId = resultSet.getInt("UserID");
                String goalName = resultSet.getString("GoalName");
                String goalType = resultSet.getString("GoalType");
                String occasionType = resultSet.getString("OccasionType");

                double targetAmount = resultSet.getDouble("TargetAmount");
                double savedAmount = resultSet.getDouble("SavedAmount");

                Date deadline = resultSet.getDate("Deadline");

                boolean completed = resultSet.getBoolean("Completed");

                if (goalType.equalsIgnoreCase("Occasion")) {

                    return new OccasionGoal(
                            goalId,
                            userId,
                            goalName,
                            goalType,
                            targetAmount,
                            savedAmount,
                            deadline.toLocalDate(),
                            completed,
                            occasionType
                    );

                } else {

                    return new GeneralGoal(
                            goalId,
                            userId,
                            goalName,
                            goalType,
                            targetAmount,
                            savedAmount,
                            deadline.toLocalDate(),
                            completed
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println("Error retrieving goal.");

            e.printStackTrace();
        }

        return null;
    }

    //=========================================================
    // STEP 4
    // Update an existing goal
    //=========================================================
    public boolean updateGoal(Goal goal) {

        String sql = """
            UPDATE Goals
            SET GoalName = ?,
                GoalType = ?,
                OccasionType = ?,
                TargetAmount = ?,
                SavedAmount = ?,
                Deadline = ?,
                Completed = ?
            WHERE GoalID = ?
            """;

        try (
                Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, goal.getGoalName());
            statement.setString(2, goal.getGoalType());

            if (goal instanceof OccasionGoal occasionGoal) {

                statement.setString(3, occasionGoal.getOccasionType());

            } else {

                statement.setNull(3, Types.VARCHAR);
            }

            statement.setDouble(4, goal.getTargetAmount());
            statement.setDouble(5, goal.getSavedAmount());
            statement.setDate(6, Date.valueOf(goal.getDeadline()));
            statement.setBoolean(7, goal.isCompleted());
            statement.setInt(8, goal.getGoalId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("Error updating goal.");

            e.printStackTrace();

            return false;
        }
    }

    //=========================================================
    // STEP 5
    // Update the saved amount of a goal (overwrites, does not add)
    //=========================================================
    public boolean updateSavedAmount(int goalId, double savedAmount) {

        String sql = """
            UPDATE Goals
            SET SavedAmount = ?
            WHERE GoalID = ?
            """;

        try (
                Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, savedAmount);
            statement.setInt(2, goalId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("Error updating saved amount.");

            e.printStackTrace();

            return false;
        }
    }

    //=========================================================
    // STEP 6
    // Set a goal's completed status (true or false)
    //=========================================================
    public boolean setGoalCompleted(int goalId, boolean completed) {

        String sql = """
            UPDATE Goals
            SET Completed = ?
            WHERE GoalID = ?
            """;

        try (
                Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, completed);
            statement.setInt(2, goalId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("Error updating goal completion status.");

            e.printStackTrace();

            return false;
        }
    }


    //=========================================================
    // STEP 7
    // Soft-delete: move the goal into DeletedGoals, then remove
    // it from Goals. Both statements run in one transaction so
    // a failure can't leave the goal in both tables, or neither.
    //=========================================================
    public boolean deleteGoal(int goalId) {

        String moveSql = """
            INSERT INTO DeletedGoals
                (GoalID, UserID, GoalName, GoalType, OccasionType,
                 TargetAmount, SavedAmount, Deadline, Completed)
            SELECT GoalID, UserID, GoalName, GoalType, OccasionType,
                   TargetAmount, SavedAmount, Deadline, Completed
            FROM Goals
            WHERE GoalID = ?
            """;

        String removeSql = """
            DELETE FROM Goals
            WHERE GoalID = ?
            """;

        Connection connection = DatabaseManager.getConnection();

        if (connection == null) {
            return false;
        }

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement moveStatement = connection.prepareStatement(moveSql)) {
                moveStatement.setInt(1, goalId);
                moveStatement.executeUpdate();
            }

            int rowsAffected;

            try (PreparedStatement removeStatement = connection.prepareStatement(removeSql)) {
                removeStatement.setInt(1, goalId);
                rowsAffected = removeStatement.executeUpdate();
            }

            connection.commit();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("Error deleting goal.");

            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }

            return false;

        } finally {

            DatabaseManager.closeConnection(connection);
        }
    }

    //=========================================================
    // Restore a soft-deleted goal back into Goals
    //=========================================================
    public boolean restoreGoal(int goalId) {

        String moveBackSql = """
            INSERT INTO Goals
                (GoalID, UserID, GoalName, GoalType, OccasionType,
                 TargetAmount, SavedAmount, Deadline, Completed)
            SELECT GoalID, UserID, GoalName, GoalType, OccasionType,
                   TargetAmount, SavedAmount, Deadline, Completed
            FROM DeletedGoals
            WHERE GoalID = ?
            """;

        String removeFromDeletedSql = """
            DELETE FROM DeletedGoals
            WHERE GoalID = ?
            """;

        Connection connection = DatabaseManager.getConnection();

        if (connection == null) {
            return false;
        }

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement moveBackStatement = connection.prepareStatement(moveBackSql)) {
                moveBackStatement.setInt(1, goalId);
                moveBackStatement.executeUpdate();
            }

            int rowsAffected;

            try (PreparedStatement removeStatement = connection.prepareStatement(removeFromDeletedSql)) {
                removeStatement.setInt(1, goalId);
                rowsAffected = removeStatement.executeUpdate();
            }

            connection.commit();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("Error restoring goal.");

            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }

            return false;

        } finally {

            DatabaseManager.closeConnection(connection);
        }
    }

}
