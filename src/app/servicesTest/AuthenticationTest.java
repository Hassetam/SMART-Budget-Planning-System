package app.servicesTest;


import model.User;
import service.AuthenticationService;

public class AuthenticationTest {

    public static void main(String[] args) {

        AuthenticationService auth = new AuthenticationService();

        System.out.println("=================================");
        System.out.println(" Authentication Service Testing");
        System.out.println("=================================");

        // Test 1
        System.out.println("\nTest 1: Username Exists");

        boolean exists = auth.usernameExists("hassetam");

        System.out.println("Username 'hassetam' exists: " + exists);


        // Test 2
        System.out.println("\nTest 2: Wrong Username");

        boolean exists2 = auth.usernameExists("abcdefg");

        System.out.println("Username 'abcdefg' exists: " + exists2);


        // Test 3
        System.out.println("\nTest 3: Login");

        User user = auth.login("hassetam", "Helloworld");

        if (user != null) {

            System.out.println("Login Successful!");

            System.out.println(user);

        } else {

            System.out.println("Login Failed.");

        }


        // Test 4
        System.out.println("\nTest 4: Wrong Password");

        User user2 = auth.login("hassetam", "WrongPassword");

        if (user2 != null) {

            System.out.println("Login Successful!");

        } else {

            System.out.println("Login Failed.");

        }


        // Test 5
        System.out.println("\nTest 5: Register New User");

        User newUser = new User(
                "Test User",
                "testuser123",
                "password123"
        );

        boolean registered = auth.register(newUser);

        if (registered) {

            System.out.println("Registration Successful!");

        } else {

            System.out.println("Registration Failed.");

        }


        // Test 6
        System.out.println("\nTest 6: Duplicate Username");

        User duplicate = new User(
                "Abebe",
                "hassetam",
                "password123"
        );

        boolean duplicateResult = auth.register(duplicate);

        if (duplicateResult) {

            System.out.println("Registration Successful!");

        } else {

            System.out.println("Registration Failed (Duplicate Username).");

        }

    }

}


