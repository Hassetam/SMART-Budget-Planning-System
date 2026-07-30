package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {

    // User information
    private int userId; // user id is initialized the database
    private String fullName;
    private String username;
    private String password;
    private LocalDate registrationDate;

    // User financial information
    private Budget budget;
    private ArrayList<Income> incomes;
    private ArrayList<Expense> expenses;
    private ArrayList<Goal> goals;

    // Default Constructor
    public User() {
        initializeCollections();
    }

    // Constructor used when registering a new user
    public User(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.registrationDate = LocalDate.now();

        initializeCollections();
    }

    // Constructor used when retrieving a user from the database
    public User(int userId, String fullName, String username,
            String password, LocalDate registrationDate) {

        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;

        initializeCollections();
    }

    // Initializes object references and collections
    // private because we want this method only in this class, we choose it instead
    // of repeating this code.
    private void initializeCollections() {
        budget = new Budget();
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
        goals = new ArrayList<>();
    }

    // Getters - because our attributes are private.

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public Budget getBudget() {
        return budget;
    }

    public ArrayList<Income> getIncomes() {
        return incomes;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }

    // Setters - also because we have private attributes

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    // User Methods - or methods that would actually be used by the user.

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
    }

    public void addIncome(Income income) {
        incomes.add(income);
    }

    public void removeIncome(Income income) {
        incomes.remove(income);
    }

    public void addGoal(Goal goal) {
        goals.add(goal);

    }

    public void removeGoal(Goal goal) {
        goals.remove(goal);
    }

    // Display

    @Override
    public String toString() {
        return fullName + " (" + username + ")";
    }
}