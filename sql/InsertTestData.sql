--USERS TEST VALUES
INSERT INTO Users
VALUES 
( 'Abigiya Endale' , 'abbyend', 'OpenWorld1', '2026-07-20'),
('Hasset Adugna', 'hassetam', 'Helloworld', '2026-07-20'),
('Feven Endale', 'feven_end', '1214hi', '2026-07-21');


--BUDGETS TEST VALUES
INSERT INTO Budgets
VALUES
(1,3000.00, 07, 2026),
(2,4000.00, 07, 2026),
(3,5000.00, 07, 2026);


--EXPENSES TEST VALUES
INSERT INTO Expenses 
VALUES
(1, 800,'Food', '2026-07-14', NULL),
(2, 300, 'Transportation', '2026-07-10', 'Had to take uber'),
(3, 1000, 'Others', '2026-07-17', 'Lent for a friend');

--INCOME TEST VALUES
INSERT INTO Income 
VALUES
(1, 1500.00, 'FALSE', '2026-07-19', NULL),
(2, 2000.00, 'TRUE', '2026-07-05', NULL),
(3, 800, 'FALSE','2026-07-04', NULL );


--GOALS TEST VALUES
INSERT INTO Goals
VALUES
(1, 'Buy acrylic paint', 'Long term', 3000.00, 1000.00, '2027-01-01','FALSE'),
(2, 'Buy Boots', 'Short term', 8000.00, 2000.00, '2026-08-30', 'FALSE'),
(3, 'Buy skin care products', 'short term', 4000.00, 2000.00, '2026-08-14', 'FALSE');
