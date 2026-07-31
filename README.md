# SMART-Budget-Planning-System

A Java Object-Oriented Programming (OOP) application designed to help university and college students effectively manage their personal finances. The system enables users to plan budgets, track income and expenses, receive smart spending insights, and make informed financial decisions.

# README – How to Run the Smart Budget Planning System

## Prerequisites

Before running the project, ensure the following software is installed:

* Java Development Kit (JDK 17 or later)
* Visual Studio Code or any Java IDE (IntelliJ IDEA, Eclipse, NetBeans, etc.)
* Microsoft SQL Server
* SQL Server JDBC Driver

---

## Step 1: Set Up the Database

1. Open Microsoft SQL Server.
2. Create a database named:

```
SmartBudgetPlannerDB
```

3. Execute the provided SQL script to create all required tables and relationships.
4. Ensure SQL Server is running.

---

## Step 2: Configure the Database Connection

Open the database configuration file (or `DatabaseManager.java`) and verify the connection details.

Example:

```java
db.url=jdbc:sqlserver://localhost;databaseName=SmartBudgetPlannerDB;encrypt=true;trustServerCertificate=true
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD
```

If Windows Authentication is used, configure the connection accordingly.

---

## Step 3: Add the JDBC Driver

Add the Microsoft SQL Server JDBC Driver (`mssql-jdbc.jar`) to the project's build path or library folder.

---

## Step 4: Open the Project

Open the project folder in your preferred Java IDE.

Ensure all source files and libraries are loaded successfully.

---

## Step 5: Build the Project

Compile the project to verify there are no compilation errors.

---

## Step 6: Run the Application

Run the `Main.java` file.

The application will launch in the following sequence:

```
Splash Screen
        ↓
Login Screen
        ↓
Register (for new users)
        ↓
Main Dashboard
```

---

## Using the Application

After logging in successfully, users can:

* Create and manage monthly budgets.
* Record income.
* Record expenses.
* View the financial dashboard.
* Manage savings goals.
* Generate monthly reports.
* Receive smart budgeting advice.
* Change password or delete their account from the Settings page.

---

## Troubleshooting

### Database connection error

* Ensure SQL Server is running.
* Verify the database name is `SmartBudgetPlannerDB`.
* Confirm the username and password are correct.
* Ensure the JDBC Driver has been added to the project.

### Application does not start

* Verify that JDK 17 (or later) is installed.
* Ensure all project files are included.
* Rebuild the project and run `Main.java` again.

---

## Entry Point

The application's entry point is:

```
Main.java
```

