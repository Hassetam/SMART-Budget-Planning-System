package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class DatabaseManager {
    
    //To read the configuration
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream("config/database.properties");
            properties.load(file);
        } catch (IOException e) {
            System.out.println("Could not load database.properties");
            e.printStackTrace();
        }
    }


    //The connection method
    public static Connection getConnection() {

        try {

            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            return DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {

            System.out.println("Database connection failed.");
            e.printStackTrace();
            return null;
        }
    }


    //To close the connection
    public static void closeConnection(Connection connection) {

        try {

            if (connection != null) {
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}