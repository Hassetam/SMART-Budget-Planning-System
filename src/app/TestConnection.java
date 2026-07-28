package app;

import database.DatabaseManager;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        Connection connection = DatabaseManager.getConnection();

        if (connection != null) {
            System.out.println(" Connected to SQL Server successfully!");
            DatabaseManager.closeConnection(connection);
        } else {
            System.out.println(" Connection failed.");
        }
    }
}