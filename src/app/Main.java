// Version one of main.java
package app;

import gui.SplashFrame;
import model.User;

public class Main {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println(" Smart Budget Planning System");
        System.out.println("==================================");
        System.out.println("Application started successfully.");

        // temporary to compile model classes
        User user = new User();
        System.out.println(user.getUsername());

        // to check for compilation
        new SplashFrame();

    }

}