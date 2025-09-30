# JavaMicroCredit - Comprehensive Test Plan

## Setup Instructions

1. **Reset Database**:
   ```bash
   psql -U your_username -d your_database -f tables.sql
   psql -U your_username -d your_database -f seed.sql
   ```

2. **Compile and Run**:
   ```bash
   javac -d bin -cp "lib/*" src/Main.java src/Models/*.java src/Views/*.java src/Enums/*.java src/Database/*.java src/Services/*.java
   java -cp "bin;lib/*" Main
   ```

---

## Score Calculation Breakdown

This section details how each client's score is calculated based on the rules in `project.txt`.

**Ahmed Bennani (Employee)**: `77`
- **Professional Stability (30/30)**: `25` (CDI Public) + `5` (72 months seniority)
- **Financial Capacity (30/30)**: `30` (12000 DH salary)
- **History (-3/15)**: `-3` (1 late payment)
- **Client Relationship (10/10)**: `10` (> 3 years)
- **Complementary Criteria (10/10)**: `10` (investment and placement)

**Fatima Alami (Employee)**: `51`
- **Professional Stability (18/30)**: `15` (CDI Private Large) + `3` (36 months seniority)
- **Financial Capacity (25/30)**: `25` (8500 DH salary)
- **History (0/15)**: `0` (no credit history)
- **Client Relationship (8/10)**: `8` (1-3 years)
- **Complementary Criteria (0/10)**: `0`

**Youssef Tazi (Employee)**: `27`
- **Professional Stability (10/30)**: `10` (Temporary)
- **Financial Capacity (10/30)**: `10` (2500 DH salary)
- **History (0/15)**: `0`
- **Client Relationship (7/10)**: `4` (age 25) + `3` (married) + `0` (>2 children)
- **Complementary Criteria (0/10)**: `0`

**Houda Mansouri (Employee)**: `45`
- **Professional Stability (13/30)**: `12` (CDI SME) + `1` (20 months seniority)
- **Financial Capacity (20/30)**: `20` (5000 DH salary)
- **History (0/15)**: `0`
- **Client Relationship (12/10)**: `8` (age 32) + `2` (single) + `2` (0 children)
- **Complementary Criteria (0/10)**: `0`

**Omar Chraibi (Employee)**: `52`
- **Professional Stability (18/30)**: `15` (CDI Private Large) + `3` (30 months seniority)
- **Financial Capacity (20/30)**: `20` (7000 DH salary)
- **History (0/15)**: `0`
- **Client Relationship (14/10)**: `10` (age 36) + `3` (married) + `1` (1 child)
- **Complementary Criteria (0/10)**: `0`

**Meriem Kettani (Employee)**: `75`
- **Professional Stability (30/30)**: `25` (CDI Public) + `5` (60 months seniority)
- **Financial Capacity (25/30)**: `25` (9000 DH salary)
- **History (0/15)**: `0`
- **Client Relationship (10/10)**: `10` (> 3 years)
- **Complementary Criteria (10/10)**: `10`

**Amine Lahlou (Employee)**: `53`
- **Professional Stability (18/30)**: `15` (CDI Private Large) + `3` (48 months seniority)
- **Financial Capacity (25/30)**: `25` (8000 DH salary)
- **History (0/15)**: `0`
- **Client Relationship (10/10)**: `10` (> 3 years)
- **Complementary Criteria (0/10)**: `0`

**Hassan Ziani (Employee)**: `30`
- **Professional Stability (15/30)**: `12` (CDI SME) + `3` (40 months seniority)
- **Financial Capacity (15/30)**: `15` (4500 DH salary)
- **History (-8/15)**: `-3` (3 late payments) + `5` (1 unpaid settled) - `10` (1 unpaid unsettled)
- **Client Relationship (8/10)**: `8` (1-3 years)
- **Complementary Criteria (0/10)**: `0`

**Karim Benjelloun (Professional)**: `78`
- **Professional Stability (18/30)**: `18` (Liberal Profession)
- **Financial Capacity (30/30)**: `30` (15000 DH income)
- **History (10/15)**: `10` (perfect payment history)
- **Client Relationship (10/10)**: `10` (> 3 years)
- **Complementary Criteria (10/10)**: `10`

**Samira Idrissi (Professional)**: `50`
- **Professional Stability (18/30)**: `18` (Liberal Profession)
- **Financial Capacity (20/30)**: `20` (6000 DH income)
- **History (0/15)**: `0`
- **Client Relationship (12/10)**: `8` (age 30) + `2` (single) + `2` (0 children)
- **Complementary Criteria (0/10)**: `0`

### TEST 1: View Clients and Verify Scores
**Objective**: Verify all clients have calculated scores (not 0)

**Steps**:
1. Main Menu → Client Management (1) → Manage Employees (1) → List All Employees (5)
2. Main Menu → Client Management (1) → Manage Professionals (2) → List All Professionals (5)

**Expected Results**:
- Ahmed Bennani: Score ≈ 77
- Fatima Alami: Score ≈ 51
- Youssef Tazi: Score ≈ 27
- Houda Mansouri: Score ≈ 45
- Omar Chraibi: Score ≈ 52
- Meriem Kettani: Score ≈ 75
- Amine Lahlou: Score ≈ 53
- Hassan Ziani: Score ≈ 30

**Professionals**:
- Karim Benjelloun: Score ≈ 78
- Samira Idrissi: Score ≈ 50

---

### TEST 2: Search Employee by ID
**Objective**: Verify score is displayed in details

**Steps**:
1. Main Menu → Client Management (1) → Manage Employees (1) → Search Employee by ID (2)
2. Enter ID: 1 (Ahmed Bennani)

**Expected Results**:
```
Employee Details:
ID: 1
Name: Ahmed Bennani
Score: 77.0
Created At: [3 years ago timestamp]
```

---

### TEST 3: Credit Request - High Score (Manual Review)
**Objective**: Test manual review for a high score client that is not high enough for automatic approval

**Steps**:
1. Main Menu → Credit Scoring & Decision (2) → Create Credit Request (1)
2. Select Employee (1)
3. Enter Employee ID: 1 (Ahmed - score 77)
4. Credit Type: CONSUMER (any)
5. Requested Amount: 30000 DH
6. Interest Rate: 5%
7. Duration: 12 months
8. Confirm: Yes

**Expected Results**:
```
SUCCESS: Credit request created successfully!
Credit ID: [new ID]
Decision: MANUAL_REVIEW
Approved Amount: 30000.0 DH
```

---

### TEST 4: Credit Request - Low Score (Automatic Rejection)
**Objective**: Test automatic rejection for low score client

**Steps**:
1. Main Menu → Credit Scoring & Decision (2) → Create Credit Request (1)
2. Select Employee (1)
3. Enter Employee ID: 3 (Youssef - score 27)
4. Credit Type: CONSUMER
5. Requested Amount: 10000 DH
6. Interest Rate: 7%
7. Duration: 12 months
8. Confirm: Yes

**Expected Results**:
```
Credit decision: AUTOMATIC_REJECTION
Decision: AUTOMATIC_REJECTION
Approved Amount: 0.0 DH
```

---

### TEST 5: Credit Request - Medium Score (Manual Review)
**Objective**: Test manual review for a medium score client

**Steps**:
1. Main Menu → Credit Scoring & Decision (2) → Create Credit Request (1)
2. Select Employee (1)
3. Enter Employee ID: 2 (Fatima - score 51)
4. Credit Type: AUTO
5. Requested Amount: 18000 DH
6. Interest Rate: 6.5%
7. Duration: 10 months
8. Confirm: Yes

**Expected Results**:
```
Credit decision: MANUAL_REVIEW
Approved Amount: 18000.0 DH
```

---

### TEST 6: Review Pending Credits (Manual Approval)
**Objective**: Test manual credit review and approval

**Steps**:
1. Main Menu → Credit Scoring & Decision (2) → Review Pending Credits (2)
2. Should see Credit ID 6 (Fatima's credit) in MANUAL_REVIEW status
3. Do you want to review? Yes (y)
4. Enter credit number: 6
5. Select: Approve with requested amount (1)

**Expected Results**:
```
SUCCESS: Credit approved! 10 installments generated.
```
(Now installments are created for this credit)

---

### TEST 7: View Client Credits
**Objective**: Verify all credits for a client are displayed

**Steps**:
1. Main Menu → Client Management (1) → Manage Employees (1) → View Employee Credits (6)
2. Enter Employee ID: 1 (Ahmed - has 1 existing credit from seed)

**Expected Results**:
```
Employee: Ahmed Bennani
Total Credits: 2 (1 from seed + 1 from TEST 3)

--- Credit #1 ---
Credit ID: 1
Credit Type: CONSUMER
...
Decision: IMMEDIATE_APPROVAL

--- Credit #2 ---
Credit ID: [new from TEST 3]
...
```

---

### TEST 8: Pay Installement - View Credits First
**Objective**: Test payment flow with credit selection

**Steps**:
1. Main Menu → Client Management (1) → Manage Employees (1) → Pay Installement (7)
2. Enter Employee ID: 1 (Ahmed)
3. Should see list of Ahmed's credits with statuses
4. Enter credit number: 1 (existing credit from seed)

**Expected Results**:
```
Employee: Ahmed Bennani

Credits:

1. Credit ID: 1 | Type: CONSUMER | Amount: 40000.0 DH | Status: IMMEDIATE_APPROVAL

Installements for Credit ID 1:

1. Installement ID: 1 | Due Date: [5 months ago] | Amount: 3516.67 DH | Status: ON_TIME
2. Installement ID: 2 | Due Date: [4 months ago] | Amount: 3516.67 DH | Status: ON_TIME
3. Installement ID: 3 | Due Date: [3 months ago] | Amount: 3516.67 DH | Status: PAID_LATE
4. Installement ID: 4 | Due Date: [2 months ago] | Amount: 3516.67 DH | Status: ON_TIME
5. Installement ID: 5 | Due Date: [1 month ago] | Amount: 3516.67 DH | Status: NOT PAID
6. Installement ID: 6 | Due Date: [today] | Amount: 3516.67 DH | Status: NOT PAID
```
(Future installments 7-12 should NOT be visible)

---

### TEST 9: Pay Installement - ON_TIME Payment
**Objective**: Test paying current month installment on time

**Steps**:
1. Continuing from TEST 8...
2. Enter installement number: 6 (due today)
3. Confirm payment: Yes

**Expected Results**:
```
SUCCESS: Payment recorded successfully!
Payment Status: ON_TIME
Payment Date: [current timestamp]
```

**Verify Score Update**:
- Go to Search Employee by ID (2) → Enter ID: 1
- Score should remain high (good payment history maintained)

---

### TEST 10: Pay Installement - LATE Payment
**Objective**: Test paying overdue installment (5 from TEST 8)

**Steps**:
1. Main Menu → Client Management (1) → Manage Employees (1) → Pay Installement (7)
2. Enter Employee ID: 1
3. Select credit 1
4. Enter installement number: 5 (1 month overdue)
5. Confirm: Yes

**Expected Results**:
```
SUCCESS: Payment recorded successfully!
Payment Status: PAID_LATE (or UNPAID_SETTLED depending on days overdue)
Payment Date: [current timestamp]
```

**Verify Score Decreased**:
- Search Employee ID: 1
- Score should be slightly lower due to late payment penalty

---

### TEST 11: Cannot Pay Already Paid Installement
**Objective**: Test payment validation

**Steps**:
1. Pay Installement → Employee ID: 1 → Credit 1
2. Try to pay installement 1 (already paid ON_TIME)

**Expected Results**:
```
ERROR: This installement has already been paid.
```

---

### TEST 12: View Professional Credits and Payment
**Objective**: Test professional credit and payment flow

**Steps**:
1. Main Menu → Client Management (1) → Manage Professionals (2) → View Professional Credits (6)
2. Enter Professional ID: 1 (Karim)

**Expected Results**:
```
Professional: Karim Benjelloun
Total Credits: 1

--- Credit #1 ---
Credit ID: 4
Credit Type: BUSINESS
Decision: IMMEDIATE_APPROVAL
```

**Pay Installement**:
1. Pay Installement (7) → Professional ID: 1 → Credit 1
2. Should see installments 13, 14, 15 (paid), 16 (due now)
3. Pay installement 16
4. Score should increase (perfect payment history bonus)

---

### TEST 13: Analytics - Sort Clients by Score
**Objective**: Test sorting functionality using Java Streams

**Steps**:
1. Main Menu → Analytics & Reports (4) → Sort Clients (1)
2. Sort by Score (Descending) (1)

**Expected Results** (descending order):
```
1. Karim Benjelloun | Score: 78.0
2. Ahmed Bennani | Score: 77.0
3. Meriem Kettani | Score: 75.0
4. Amine Lahlou | Score: 53.0
5. Omar Chraibi | Score: 52.0
6. Fatima Alami | Score: 51.0
7. Samira Idrissi | Score: 50.0
8. Houda Mansouri | Score: 45.0
9. Test Auto-Entrepreneur | Score: 39.0
10. Test CDD | Score: 32.0
11. Hassan Ziani | Score: 30.0
12. Youssef Tazi | Score: 27.0
```

---

### TEST 14: Analytics - Sort by Income
**Objective**: Test income sorting (combining Employee salary + Professional income)

**Steps**:
1. Analytics → Sort Clients (1) → Sort by Income (Descending) (3)

**Expected Results** (descending order):
```
1. Karim Benjelloun | Income: 15000.0 DH (Professional)
2. Ahmed Bennani | Income: 12000.0 DH (Employee)
3. Meriem Kettani | Income: 9000.0 DH
...
10. Youssef Tazi | Income: 2500.0 DH (lowest)
```

---

### TEST 15: Analytics - Sort by Relationship Seniority
**Objective**: Test sorting by createdAt date

**Steps**:
1. Analytics → Sort Clients (1) → Sort by Relationship Seniority (Oldest First) (5)

**Expected Results** (oldest first):
```
1. Meriem Kettani | Member Since: [5 years ago]
2. Karim Benjelloun | Member Since: [4 years ago]
3. Ahmed Bennani | Member Since: [3 years ago]
...
10. Omar Chraibi | Member Since: [today] (newest)
```

---

### TEST 16: Recalculate Scores for Existing Clients
**Objective**: Test score recalculation utility

**Steps**:
1. Main Menu → Analytics & Reports (4) → Recalculate All Scores (6)
2. Confirm: Yes

**Expected Results**:
```
Recalculated scores for 8 employees.
Recalculated scores for 2 professionals.
SUCCESS: All client scores have been recalculated successfully!
```

**Verify**:
- List all employees/professionals
- All scores should be updated based on current data
- Hassan Ziani (Employee 8) should have LOW score due to bad payment history

---

### TEST 17: Edge Case - Create New Employee
**Objective**: Verify new employee gets initial score immediately

**Steps**:
1. Main Menu → Client Management (1) → Manage Employees (1) → Create Employee (1)
2. Fill in details:
   - Name: Test User
   - DOB: 1995-05-10
   - Salary: 6000
   - Months in work: 24
   - Contract: PERMANENT
   - Sector: PRIVATE_LARGE
   - Family: MARRIED
   - Children: 1
   - No investments

3. After creation, search for the new employee by ID

**Expected Results**:
```
Score: ~55-60 (calculated immediately, NOT 0!)
```

---

### TEST 18: Edge Case - Request Amount Exceeds Limit
**Objective**: Test borrowing limit validation

**Steps**:
1. Create Credit Request for Employee ID: 3 (Youssef - 2500 DH salary)
2. Request Amount: 50000 DH (way more than 4x salary = 10000)
3. Interest: 5%, Duration: 12

**Expected Results**:
```
Decision: AUTOMATIC_REJECTION
Approved Amount: 0.0 DH
```
(Exceeds borrowing capacity)

---

### TEST 19: Payment History Impact on Score
**Objective**: Verify payment history affects score dynamically

**Steps**:
1. Check Hassan Ziani (Employee 8) score - should be LOW (30)
2. View his credits and payment history
3. Verify he has:
   - Multiple PAID_LATE records
   - 1 UNPAID_SETTLED record
   - 1 UNPAID (no payment yet)

**Expected**: Score is significantly reduced due to:
- -10 pts for unpaid
- -5 pts for 4+ late payments
- +5 pts for unpaid settled

---

### TEST 20: Future Installments Not Visible
**Objective**: Verify future installments are hidden

**Steps**:
1. Pay Installement → Employee 1 → Credit 1
2. Should only see installments 1-6 (up to current month)
3. Installments 7-12 (future months) should NOT appear

**Expected Results**:
## Additional Test Cases

### TEST 21: New `auto-entrepreneur` Professional
**Objective**: Verify scoring for a new professional with `auto-entrepreneur` status.

**Steps**:
1. Main Menu → Client Management (1) → Manage Professionals (2) → Create Professional (1)
2. Fill in details:
   - Name: Test Auto-Entrepreneur
   - DOB: 1993-01-01
   - Income: 4000
   - Sector: `SERVICE`
   - Activity: `Consultant`
   - Family: `SINGLE`
   - Children: 0
   - No investments
3. After creation, search for the new professional by ID.

**Expected Results**:
- Score: `12` (Prof. Stability) + `15` (Financial) + `0` (History) + `12` (Relationship) + `0` (Complementary) = `39`

### TEST 22: New `CDD/Intérim` Employee
**Objective**: Verify scoring for a new employee with a `CDD` contract.

**Steps**:
1. Main Menu → Client Management (1) → Manage Employees (1) → Create Employee (1)
2. Fill in details:
   - Name: Test CDD
   - DOB: 1998-01-01
   - Salary: 3500
   - Months in work: 18
   - Contract: `TEMPORARY`
   - Sector: `SME`
   - Family: `SINGLE`
   - Children: 0
   - No investments
3. After creation, search for the new employee by ID.

**Expected Results**:
- Score: `10` (Prof. Stability) + `1` (Prof. Stability Bonus) + `15` (Financial) + `0` (History) + `6` (Relationship) + `0` (Complementary) = `32`

### TEST 23: Complex Payment History
**Objective**: Verify score calculation for a client with a complex payment history.

**Steps**:
1. Create a new employee.
2. Create a credit for the employee.
3. Add a mix of `ON_TIME`, `PAID_LATE`, `UNPAID_SETTLED`, and `UNPAID_UNSETTLED` payments.
4. Recalculate the employee's score.

**Expected Results**:
### TEST 24: Credit Request - Immediate Approval
**Objective**: Test automatic approval for a client with a score of 80 or higher.

**Steps**:
1. Main Menu → Credit Scoring & Decision (2) → Create Credit Request (1)
2. Select Employee (1)
3. Enter Employee ID: 11 (Nadia El Fassi - score 80)
4. Credit Type: CONSUMER
5. Requested Amount: 50000 DH
6. Interest Rate: 5%
7. Duration: 12 months
8. Confirm: Yes

**Expected Results**:
```
SUCCESS: Credit request created successfully!
Credit ID: [new ID]
Decision: IMMEDIATE_APPROVAL
Approved Amount: 50000.0 DH
Credit approved! 12 installments generated.
```

---

## Summary Checklist

- [ ] All clients have non-zero scores
- [ ] Score displayed in client details
- [ ] High score → IMMEDIATE_APPROVAL + installments generated
- [ ] Low score → AUTOMATIC_REJECTION + no installments
- [ ] Medium score → MANUAL_REVIEW + no installments initially
- [ ] Manual review can approve/reject credits
- [ ] Approved credits generate installments
- [ ] Payment records created with correct status (ON_TIME, PAID_LATE, UNPAID_SETTLED)
- [ ] Cannot pay already paid installments
- [ ] Cannot see/pay future installments
- [ ] Score updates dynamically after each payment
- [ ] Sort by score/income/seniority works correctly (Java Streams)
- [ ] New clients get immediate score calculation
- [ ] Bad payment history reduces score
- [ ] Borrowing limit validation works

---

## Expected Edge Cases Covered

1. ✅ New client (no createdAt history)
2. ✅ Client with perfect payment history
3. ✅ Client with bad payment history (lates + unpaid)
4. ✅ Request exceeding borrowing limit
5. ✅ Different employment types (CDI, CDD, Professional)
6. ✅ Different salary ranges (2500 to 15000)
7. ✅ Different family situations (single, married, children)
8. ✅ Manual review workflow
9. ✅ Future installments hidden
10. ✅ Payment validation (already paid)
11. ✅ Score recalculation for existing data
12. ✅ Combined Employee + Professional sorting

---

**Total Test Cases**: 20
**Estimated Testing Time**: 45-60 minutes for complete coverage