-- ============================================
-- JavaMicroCredit - Test Data Seed File
-- ============================================
-- This file contains test data for all edge cases
-- Run this AFTER tables.sql to populate test data
-- ============================================

-- Clear existing data
TRUNCATE TABLE PaymentRecord CASCADE;
TRUNCATE TABLE Installement CASCADE;
TRUNCATE TABLE Credit CASCADE;
TRUNCATE TABLE Professional CASCADE;
TRUNCATE TABLE Employee CASCADE;

ALTER SEQUENCE employee_id_seq RESTART WITH 1;
ALTER SEQUENCE professional_id_seq RESTART WITH 1;
ALTER SEQUENCE credit_id_seq RESTART WITH 1;
ALTER SEQUENCE installement_id_seq RESTART WITH 1;
ALTER SEQUENCE paymentrecord_id_seq RESTART WITH 1;

-- ============================================
-- TEST CASE 1: HIGH SCORE EMPLOYEES
-- ============================================
-- Employee 1: Perfect profile - CDI Public, high salary, married, investments
-- Expected Score: ~75-80 (30 stability + 30 financial + 0 history + 15 relationship + 10 patrimoine)
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Ahmed', 'Bennani', '1985-05-15', 'Casablanca', true, true, 2, 'MARRIED',
        12000, 72, 'Manager', 'PERMANENT', 'PUBLIC', 77, NOW() - INTERVAL '3 years', NOW());

-- Employee 2: Good profile - CDI Private Large, good salary
-- Expected Score: ~65-70
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Fatima', 'Alami', '1990-08-22', 'Rabat', false, false, 1, 'MARRIED',
        8500, 36, 'Engineer', 'PERMANENT', 'PRIVATE_LARGE', 51, NOW() - INTERVAL '2 years', NOW());

-- ============================================
-- TEST CASE 2: LOW SCORE EMPLOYEES (At-Risk)
-- ============================================
-- Employee 3: Low score - CDD, low salary, young, many children
-- Expected Score: ~35-40
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Youssef', 'Tazi', '2000-03-10', 'Marrakech', false, false, 3, 'MARRIED',
        2500, 8, 'Assistant', 'TEMPORARY', 'SME', 27, NOW() - INTERVAL '6 months', NOW());

-- Employee 4: Medium-low score - CDI SME, medium salary, new client
-- Expected Score: ~50-55
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Houda', 'Mansouri', '1992-11-30', 'Fes', false, false, 0, 'SINGLE',
        5000, 20, 'Accountant', 'PERMANENT', 'SME', 45, NOW() - INTERVAL '8 months', NOW());

-- ============================================
-- TEST CASE 3: PROFESSIONALS
-- ============================================
-- Professional 1: High score - Stable liberal profession, high income
-- Expected Score: ~70-75
INSERT INTO Professional (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                         income, taxRegistrationNumber, businessSector, activity, score, createdAt, updatedAt)
VALUES ('Karim', 'Benjelloun', '1980-07-12', 'Casablanca', true, false, 1, 'MARRIED',
        15000, 'TAX123456', 'SERVICE', 'Lawyer', 78, NOW() - INTERVAL '4 years', NOW());

-- Professional 2: Medium score - Auto-entrepreneur, medium income, new
-- Expected Score: ~55-60
INSERT INTO Professional (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                         income, taxRegistrationNumber, businessSector, activity, score, createdAt, updatedAt)
VALUES ('Samira', 'Idrissi', '1995-02-20', 'Tanger', false, false, 0, 'SINGLE',
        6000, 'TAX789012', 'TRADE', 'Merchant', 50, NOW() - INTERVAL '10 months', NOW());

-- ============================================
-- TEST CASE 4: EDGE CASE - NEW CLIENT (No createdAt history)
-- ============================================
-- Employee 5: Brand new client - for testing new client scoring
-- Expected Score: ~60 (no relationship history bonus)
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Omar', 'Chraibi', '1988-12-05', 'Agadir', false, false, 1, 'MARRIED',
        7000, 30, 'Developer', 'PERMANENT', 'PRIVATE_LARGE', 52, NOW(), NOW());

-- ============================================
-- TEST CASE 5: MORTGAGE ELIGIBLE CLIENTS
-- ============================================
-- Employee 6: Perfect for mortgage - Age 25-50, CDI, >4000 salary, score >70, married
-- Expected: ELIGIBLE for mortgage credit
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Meriem', 'Kettani', '1990-06-18', 'Casablanca', true, true, 1, 'MARRIED',
        9000, 60, 'Director', 'PERMANENT', 'PUBLIC', 75, NOW() - INTERVAL '5 years', NOW());

-- Employee 7: NOT eligible - Single (fails family status requirement)
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Amine', 'Lahlou', '1992-09-25', 'Rabat', false, false, 0, 'SINGLE',
        8000, 48, 'Consultant', 'PERMANENT', 'PRIVATE_LARGE', 53, NOW() - INTERVAL '3 years', NOW());

-- ============================================
-- TEST CASE 6: CREDITS WITH DIFFERENT DECISIONS
-- ============================================
-- Credit 1: IMMEDIATE_APPROVAL for Employee 1 (High score, good profile)
INSERT INTO Credit (employeeId, professionalId, creditDate, requestedAmount, approvedAmount,
                   interestRate, durationInMonths, creditType, decision)
VALUES (1, NULL, NOW() - INTERVAL '6 months', 40000, 40000, 5.5, 12, 'CONSUMER', 'IMMEDIATE_APPROVAL');

-- Credit 2: MANUAL_REVIEW for Employee 4 (Medium score)
INSERT INTO Credit (employeeId, professionalId, creditDate, requestedAmount, approvedAmount,
                   interestRate, durationInMonths, creditType, decision)
VALUES (4, NULL, NOW() - INTERVAL '3 months', 20000, 20000, 6.0, 10, 'AUTO', 'MANUAL_REVIEW');

-- Credit 3: AUTOMATIC_REJECTION for Employee 3 (Low score)
INSERT INTO Credit (employeeId, professionalId, creditDate, requestedAmount, approvedAmount,
                   interestRate, durationInMonths, creditType, decision)
VALUES (3, NULL, NOW() - INTERVAL '2 months', 15000, 0, 7.0, 12, 'CONSUMER', 'AUTOMATIC_REJECTION');

-- ============================================
-- TEST CASE 7: INSTALLMENTS WITH PAYMENT HISTORY
-- ============================================
-- Installments for Credit 1 (12 months, approved 6 months ago)
-- Generate 6 past due installments and show payment patterns

-- Month 1: Paid ON_TIME
INSERT INTO Installement (creditId, dueDate, amount)
VALUES (1, NOW() - INTERVAL '5 months', 3516.67);

INSERT INTO PaymentRecord (installementId, status, createdAt)
VALUES (1, 'ON_TIME', NOW() - INTERVAL '5 months');

-- Month 2: Paid ON_TIME
INSERT INTO Installement (creditId, dueDate, amount)
VALUES (1, NOW() - INTERVAL '4 months', 3516.67);

INSERT INTO PaymentRecord (installementId, status, createdAt)
VALUES (2, 'ON_TIME', NOW() - INTERVAL '4 months');

-- Month 3: Paid LATE (10 days late)
INSERT INTO Installement (creditId, dueDate, amount)
VALUES (1, NOW() - INTERVAL '3 months', 3516.67);

INSERT INTO PaymentRecord (installementId, status, createdAt)
VALUES (3, 'PAID_LATE', NOW() - INTERVAL '3 months' + INTERVAL '10 days');

-- Month 4: Paid ON_TIME
INSERT INTO Installement (creditId, dueDate, amount)
VALUES (1, NOW() - INTERVAL '2 months', 3516.67);

INSERT INTO PaymentRecord (installementId, status, createdAt)
VALUES (4, 'ON_TIME', NOW() - INTERVAL '2 months');

-- Month 5: NOT PAID YET (due last month - should show as overdue)
INSERT INTO Installement (creditId, dueDate, amount)
VALUES (1, NOW() - INTERVAL '1 month', 3516.67);

-- Month 6: Due now (current month - available to pay)
INSERT INTO Installement (creditId, dueDate, amount)
VALUES (1, NOW(), 3516.67);

-- Months 7-12: Future installments (should NOT be visible yet)
INSERT INTO Installement (creditId, dueDate, amount)
VALUES
(1, NOW() + INTERVAL '1 month', 3516.67),
(1, NOW() + INTERVAL '2 months', 3516.67),
(1, NOW() + INTERVAL '3 months', 3516.67),
(1, NOW() + INTERVAL '4 months', 3516.67),
(1, NOW() + INTERVAL '5 months', 3516.67),
(1, NOW() + INTERVAL '6 months', 3516.67);

-- ============================================
-- TEST CASE 8: PROFESSIONAL WITH CREDIT
-- ============================================
-- Credit 4: Professional credit - approved
INSERT INTO Credit (employeeId, professionalId, creditDate, requestedAmount, approvedAmount,
                   interestRate, durationInMonths, creditType, decision)
VALUES (NULL, 1, NOW() - INTERVAL '4 months', 50000, 50000, 5.0, 12, 'BUSINESS', 'IMMEDIATE_APPROVAL');

-- Generate 4 installments (all paid on time - perfect history)
INSERT INTO Installement (creditId, dueDate, amount)
VALUES
(4, NOW() - INTERVAL '3 months', 4375.00),
(4, NOW() - INTERVAL '2 months', 4375.00),
(4, NOW() - INTERVAL '1 month', 4375.00),
(4, NOW(), 4375.00);

INSERT INTO PaymentRecord (installementId, status, createdAt)
VALUES
(13, 'ON_TIME', NOW() - INTERVAL '3 months'),
(14, 'ON_TIME', NOW() - INTERVAL '2 months'),
(15, 'ON_TIME', NOW() - INTERVAL '1 month');

-- ============================================
-- TEST CASE 9: CLIENT WITH BAD PAYMENT HISTORY
-- ============================================
-- Employee 8: Client with unpaid settlements
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Hassan', 'Ziani', '1987-04-14', 'Meknes', false, false, 2, 'MARRIED',
        4500, 40, 'Supervisor', 'PERMANENT', 'SME', 30, NOW() - INTERVAL '2 years', NOW());

-- Credit with bad payment history
INSERT INTO Credit (employeeId, professionalId, creditDate, requestedAmount, approvedAmount,
                   interestRate, durationInMonths, creditType, decision)
VALUES (8, NULL, NOW() - INTERVAL '8 months', 25000, 25000, 8.0, 6, 'CONSUMER', 'IMMEDIATE_APPROVAL');

-- Installments with various payment issues
INSERT INTO Installement (creditId, dueDate, amount)
VALUES
(5, NOW() - INTERVAL '7 months', 4500.00),
(5, NOW() - INTERVAL '6 months', 4500.00),
(5, NOW() - INTERVAL '5 months', 4500.00),
(5, NOW() - INTERVAL '4 months', 4500.00),
(5, NOW() - INTERVAL '3 months', 4500.00);

-- Bad payment pattern: late, unpaid settled, late, late, unpaid unsettled
INSERT INTO PaymentRecord (installementId, status, createdAt)
VALUES
(16, 'PAID_LATE', NOW() - INTERVAL '7 months' + INTERVAL '15 days'),
(17, 'UNPAID_SETTLED', NOW() - INTERVAL '6 months' + INTERVAL '45 days'),
(18, 'PAID_LATE', NOW() - INTERVAL '5 months' + INTERVAL '12 days'),
(19, 'PAID_LATE', NOW() - INTERVAL '4 months' + INTERVAL '20 days');
-- Installment 20 (Month 5) is UNPAID_UNSETTLED (no payment record)

-- ============================================
-- SUMMARY OF TEST DATA
-- ============================================
-- Employees: 8 total
--   - High Score (>75): 2 (Ahmed, Meriem)
--   - Medium Score (50-75): 3 (Fatima, Houda, Omar, Amine)
--   - Low Score (<50): 2 (Youssef, Hassan)
--
-- Professionals: 2 total
--   - High Score: 1 (Karim)
--   - Medium Score: 1 (Samira)
--
-- Credits: 5 total
--   - IMMEDIATE_APPROVAL: 3
--   - MANUAL_REVIEW: 1
--   - AUTOMATIC_REJECTION: 1
--
-- Payment History:
--   - Perfect history: Professional 1 (all on time)
--   - Good history: Employee 1 (1 late payment)
--   - Bad history: Employee 8 (multiple lates, unpaid settled, 1 unpaid)
--   - No history: Other clients

-- Employee 11: Immediate Approval Case
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Nadia', 'El Fassi', '1980-01-01', 'Rabat', true, true, 0, 'SINGLE',
        15000, 84, 'Director', 'PERMANENT', 'PUBLIC', 80, NOW() - INTERVAL '5 years', NOW());

-- ============================================
-- TEST CASE 10: ADDITIONAL TEST CASES
-- ============================================
-- Professional 3: New auto-entrepreneur
INSERT INTO Professional (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                         income, taxRegistrationNumber, businessSector, activity, score, createdAt, updatedAt)
VALUES ('Test', 'Auto-Entrepreneur', '1993-01-01', 'Tanger', false, false, 0, 'SINGLE',
        4000, 'TAX999999', 'SERVICE', 'Consultant', 39, NOW(), NOW());

-- Employee 9: New CDD/Intérim
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Test', 'CDD', '1998-01-01', 'Marrakech', false, false, 0, 'SINGLE',
        3500, 18, 'Assistant', 'TEMPORARY', 'SME', 32, NOW(), NOW());

-- Employee 10: Complex Payment History
INSERT INTO Employee (firstName, lastName, dateOfBirth, city, investment, placement, childrenCount, familyStatus,
                     salary, monthsInWork, position, contractType, employmentSector, score, createdAt, updatedAt)
VALUES ('Test', 'History', '1980-01-01', 'Fes', false, false, 1, 'MARRIED',
        6000, 120, 'Manager', 'PERMANENT', 'PRIVATE_LARGE', 0, NOW() - INTERVAL '10 years', NOW());

-- Credit for Employee 10
INSERT INTO Credit (employeeId, professionalId, creditDate, requestedAmount, approvedAmount,
                   interestRate, durationInMonths, creditType, decision)
VALUES (10, NULL, NOW() - INTERVAL '1 year', 10000, 10000, 5.0, 12, 'CONSUMER', 'IMMEDIATE_APPROVAL');

-- Installments for Credit 6
INSERT INTO Installement (creditId, dueDate, amount)
VALUES
(6, NOW() - INTERVAL '11 months', 856.07),
(6, NOW() - INTERVAL '10 months', 856.07),
(6, NOW() - INTERVAL '9 months', 856.07),
(6, NOW() - INTERVAL '8 months', 856.07),
(6, NOW() - INTERVAL '7 months', 856.07),
(6, NOW() - INTERVAL '6 months', 856.07),
(6, NOW() - INTERVAL '5 months', 856.07),
(6, NOW() - INTERVAL '4 months', 856.07),
(6, NOW() - INTERVAL '3 months', 856.07),
(6, NOW() - INTERVAL '2 months', 856.07),
(6, NOW() - INTERVAL '1 month', 856.07),
(6, NOW(), 856.07);

-- Payment Records for Credit 6
INSERT INTO PaymentRecord (installementId, status, createdAt)
VALUES
(21, 'ON_TIME', NOW() - INTERVAL '11 months'),
(22, 'PAID_LATE', NOW() - INTERVAL '10 months' + INTERVAL '10 days'),
(23, 'PAID_LATE', NOW() - INTERVAL '9 months' + INTERVAL '20 days'),
(24, 'UNPAID_SETTLED', NOW() - INTERVAL '8 months' + INTERVAL '40 days'),
(25, 'ON_TIME', NOW() - INTERVAL '7 months'),
(26, 'ON_TIME', NOW() - INTERVAL '6 months'),
(28, 'PAID_LATE', NOW() - INTERVAL '4 months' + INTERVAL '5 days'),
(29, 'UNPAID_UNSETTLED', NOW() - INTERVAL '3 months' + INTERVAL '35 days');

SELECT 'Seed data inserted successfully!' AS status;