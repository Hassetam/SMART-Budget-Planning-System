/*=========================================================
    SMART STUDENT FINANCIAL ASSISTANT
    RESET DATABASE
=========================================================*/

-----------------------------------------------------------
-- DELETE ALL DATA
-----------------------------------------------------------

DELETE FROM DeletedGoals;
DELETE FROM Goals;
DELETE FROM Income;
DELETE FROM Expenses;
DELETE FROM Budgets;
DELETE FROM Users;

GO

-----------------------------------------------------------
-- RESET IDENTITY VALUES
-----------------------------------------------------------

DBCC CHECKIDENT ('Users', RESEED, 0);
DBCC CHECKIDENT ('Budgets', RESEED, 0);
DBCC CHECKIDENT ('Expenses', RESEED, 0);
DBCC CHECKIDENT ('Income', RESEED, 0);
DBCC CHECKIDENT ('Goals', RESEED, 0);

GO

/*=========================================================
                    USERS
=========================================================*/

INSERT INTO Users
    (
    FullName,
    Username,
    Password,
    RegistrationDate
    )
VALUES
    (
        'Abigiya Endale',
        'abigiya',
        '1234',
        GETDATE()
),
    (
        'Hasset',
        'hasset',
        '1234',
        GETDATE()
);

GO

/*=========================================================
                    BUDGETS
=========================================================*/

INSERT INTO Budgets
    (
    USERID,
    MonthlyBudget,
    Month,
    Year
    )
VALUES
    (
        1,
        12000,
        7,
        2026
),
    (
        2,
        15000,
        7,
        2026
);

GO

/*=========================================================
                    INCOME
=========================================================*/

INSERT INTO Income
    (
    USERID,
    Amount,
    Expected,
    DateReceived,
    Description
    )
VALUES
    (
        1,
        15000,
        1,
        '2026-07-01',
        'Monthly Allowance'
),
    (
        1,
        3000,
        0,
        '2026-07-10',
        'Freelance Work'
),
    (
        2,
        18000,
        1,
        '2026-07-01',
        'Salary'
);

GO

/*=========================================================
                    EXPENSES
=========================================================*/

INSERT INTO Expenses
    (
    USERID,
    Amount,
    Category,
    DateSpent,
    Description
    )
VALUES
    (
        1,
        250,
        'Food',
        '2026-07-02',
        'Lunch'
),
    (
        1,
        120,
        'Transport',
        '2026-07-03',
        'Taxi'
),
    (
        1,
        800,
        'Education',
        '2026-07-04',
        'Books'
),
    (
        2,
        500,
        'Shopping',
        '2026-07-05',
        'Clothes'
);

GO

/*=========================================================
                    GOALS
=========================================================*/

INSERT INTO Goals
    (
    USERID,
    GoalName,
    GoalType,
    OccasionType,
    TargetAmount,
    SavedAmount,
    Deadline,
    Completed
    )
VALUES
    (
        1,
        'Emergency Fund',
        'General',
        NULL,
        50000,
        12000,
        '2027-01-01',
        0
),
    (
        1,
        'Birthday Celebration',
        'Occasion',
        'Birthday',
        15000,
        4000,
        '2026-12-20',
        0
),
    (
        2,
        'Vacation',
        'Occasion',
        'Holiday',
        30000,
        10000,
        '2026-11-15',
        0
);

GO

/*=========================================================
                VERIFY DATA
=========================================================*/

SELECT *
FROM Users;
SELECT *
FROM Budgets;
SELECT *
FROM Income;
SELECT *
FROM Expenses;
SELECT *
FROM Goals;

GO

PRINT 'Database reset completed successfully!';