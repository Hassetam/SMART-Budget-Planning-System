package service;

import database.DatabaseManager;
import model.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AuthenticationService {

    /**
     * Registers a new user.
     * Returns true if registration is successful.
     */
    public boolean register(User user) {

        if (user == null) {
            return false;
        }

        if (!validatePassword(user.getPassword())) {
            return false;
        }

        if (usernameExists(user.getUsername())) {
            return false;
        }

        String sql = """
                INSERT INTO Users
                (FullName, Username, Password, RegistrationDate)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setDate(4, Date.valueOf(user.getRegistrationDate()));

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Logs a user into the system.
     * Returns the User object if login is successful.
     * Returns null if username or password is incorrect.
     */
    public User login(String username, String password) {

        String sql = """
                SELECT *
                FROM Users
                WHERE Username = ?
                AND Password = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int userId = resultSet.getInt("USERID");
                String fullName = resultSet.getString("FullName");
                String userName = resultSet.getString("Username");
                String userPassword = resultSet.getString("Password");
                LocalDate registrationDate =
                        resultSet.getDate("RegistrationDate").toLocalDate();

                return new User(
                        userId,
                        fullName,
                        userName,
                        userPassword,
                        registrationDate
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks whether the username already exists.
     */
    public boolean usernameExists(String username) {

        String sql = """
                SELECT USERID
                FROM Users
                WHERE Username = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks whether a password satisfies the minimum requirements.
     */
    private boolean validatePassword(String password) {

        return password != null && password.length() >= 6;
    }

   public boolean deleteAccount(int userId) {

    String deleteBudgets = """
            DELETE FROM Budgets
            WHERE USERID = ?
            """;

    String deleteExpenses = """
            DELETE FROM Expenses
            WHERE USERID = ?
            """;

    String deleteIncome = """
            DELETE FROM Income
            WHERE USERID = ?
            """;

    String deleteGoals = """
            DELETE FROM Goals
            WHERE USERID = ?
            """;

    String deleteUser = """
            DELETE FROM Users
            WHERE USERID = ?
            """;

    try (Connection connection = DatabaseManager.getConnection()) {

        connection.setAutoCommit(false);

        PreparedStatement statement;

        statement = connection.prepareStatement(deleteBudgets);
        statement.setInt(1, userId);
        statement.executeUpdate();

        statement = connection.prepareStatement(deleteExpenses);
        statement.setInt(1, userId);
        statement.executeUpdate();

        statement = connection.prepareStatement(deleteIncome);
        statement.setInt(1, userId);
        statement.executeUpdate();

        statement = connection.prepareStatement(deleteGoals);
        statement.setInt(1, userId);
        statement.executeUpdate();

        statement = connection.prepareStatement(deleteUser);
        statement.setInt(1, userId);

        int rowsAffected = statement.executeUpdate();

        connection.commit();

        return rowsAffected > 0;

    } catch (SQLException e) {

        e.printStackTrace();

        return false;
    }
}

public boolean changePassword(int userId, String newPassword) {

    if (newPassword == null || newPassword.length() < 6) {

        return false;
    }


    String sql = """
            UPDATE Users
            SET Password = ?
            WHERE UserID = ?
            """;


    try (Connection connection = DatabaseManager.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {


        statement.setString(1, newPassword);
        statement.setInt(2, userId);


        int rowsAffected = statement.executeUpdate();


        return rowsAffected > 0;


    } catch (SQLException e) {

        e.printStackTrace();
        return false;

    }

}
}