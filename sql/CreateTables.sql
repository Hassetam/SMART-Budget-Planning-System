USE SmartBudgetPlannerDB;
GO

--USER TABLE1
CREATE TABLE Users (
    USERID INT IDENTITY(1,1) PRIMARY KEY,
    FullName VARCHAR(100) NOT NULL,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    RegistrationDate DATE NOT NULL
);
GO

--BUDGETS TABLE2
CREATE TABLE Budgets (
    BudgetID INT IDENTITY(1,1) PRIMARY KEY,
    USERID INT NOT NULL,
    MonthlyBudget DECIMAL(10,2) NOT NULL,
    -- StartDate and EndDate instead of Month and Year?
    Month INT NOT NULL,
    YEAR INT NOT NULL,

    CONSTRAINT FK_Budgets_Users
        FOREIGN KEY (USERID)
        REFERENCES Users(USERID)
);
GO


--EXPENSES TABLE3
CREATE TABLE Expenses (
    ExpenseID INT IDENTITY(1,1) PRIMARY KEY,
    USERID INT NOT NULL,
    Amount DECIMAL(10,2) NOT NULL,
    -- Should we keep Cateory or create a separate table
    Category VARCHAR(50) NOT NULL,
    Date DATE NOT NULL,
    Description VARCHAR(100),

    CONSTRAINT FK_Expenses_users
        FOREIGN KEY (USERID)
        REFERENCES Users (USERID)
);
GO

--INCOME TABLE4
CREATE TABLE Income (
    IncomeID INT IDENTITY(1,1) PRIMARY KEY,
    USERID INT NOT NULL,
    Amount DECIMAL(10,2) NOT NULL,
    Expected BIT NOT NULL,
    Date DATE NOT NULL,
    Description VARCHAR(100),

    CONSTRAINT FK_Income_Users
        FOREIGN KEY (USERID)
        REFERENCES Users (USERID)
);
GO



--GOALS TABLE5
CREATE TABLE Goals (
    GoalID INT IDENTITY(1,1) PRIMARY KEY,
    USERID INT NOT NULL,
    GoalName VARCHAR(50) NOT NULL,
    GoalType VARCHAR(50) NOT NULL,
    OccasionType VARCHAR(50) NULL, --for the user to specifiy the type of the occasion 
    TargetAmount DECIMAL(10,2) NOT NULL,
    SavedAmount DECIMAL(10,2) NOT NULL,
    Deadline DATE NOT NULL,
    Completed BIT NOT NULL,

    CONSTRAINT FK_Goals_Users
        FOREIGN KEY (USERID)
        REFERENCES Users(USERID)

);
GO

--To implement soft deletion
CREATE TABLE DeletedGoals (
    GoalID INT PRIMARY KEY,   -- NOT an IDENTITY column
    USERID INT NOT NULL,
    GoalName VARCHAR(50) NOT NULL,
    GoalType VARCHAR(50) NOT NULL,
    OccasionType VARCHAR(50) NULL,
    TargetAmount DECIMAL(10,2) NOT NULL,
    SavedAmount DECIMAL(10,2) NOT NULL,
    Deadline DATE NOT NULL,
    Completed BIT NOT NULL
);
GO