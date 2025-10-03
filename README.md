# JavaMicroCredit

JavaMicroCredit is a console-based application for managing micro-credit scoring and decisions. It provides a comprehensive solution for handling different types of clients (Employees and Professionals), evaluating their credit requests, and tracking payments. The system features automated scoring, credit decisioning, and payment management, making it an effective tool for micro-finance institutions.

## Features

- **Client Management**: Manage detailed profiles for both employed and professional clients.
- **Automated Credit Scoring**: A sophisticated scoring service that continuously updates client scores based on their financial data.
- **Credit Decisioning**: Automated decisions for credit applications, categorized as Immediate Approval, Manual Review, or Automatic Rejection.
- **Payment Tracking**: Manages credit installments and tracks payment records.
- **Overdue Management**: Automatically identifies and flags overdue installments.
- **Analytics & Reports**: Provides insights into credit and client data.
- **Scheduled Tasks**: Periodically updates client scores and checks for overdue installments to ensure data accuracy.

## Technologies Used

- **Backend**: Java
- **Database**: PostgreSQL
- **Containerization**: Docker

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Docker and Docker Compose
- PostgreSQL JDBC Driver (included in the `lib` directory)
- An IDE like IntelliJ IDEA or VS Code

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd JavaMicroCredit
    ```

2.  **Start the database:**
    Use Docker Compose to build and run the PostgreSQL container. The database will be initialized with the required schema from `tables.sql`.
    ```bash
    docker-compose up -d
    ```
    The database will be accessible on `localhost:5433`.

3.  **Configure Database Connection:**
    Ensure your database connection settings in `src/Database/DB.java` match the credentials in the `docker-compose.yml` file:
    -   **DB_URL**: `jdbc:postgresql://localhost:5433/mydb`
    -   **USER**: `myuser`
    -   **PASS**: `mypassword`

4.  **Compile and Run:**
    Open the project in your favorite IDE, ensure the PostgreSQL JDBC driver from the `lib` folder is added to the project's dependencies, and then run the `src/Main.java` file.

## Database Schema

The database schema is defined in the `tables.sql` file and includes tables for managing clients, credits, and payments.

<details>
<summary>Click to view the full database schema</summary>

```sql
DROP TABLE IF EXISTS PaymentRecord CASCADE;
DROP TABLE IF EXISTS Installement CASCADE;
DROP TABLE IF EXISTS Credit CASCADE;
DROP TABLE IF EXISTS Professional CASCADE;
DROP TABLE IF EXISTS Employee CASCADE;

DROP TYPE IF EXISTS FamilyStatus CASCADE;
DROP TYPE IF EXISTS ContractType CASCADE;
DROP TYPE IF EXISTS SectorType CASCADE;
DROP TYPE IF EXISTS CreditType CASCADE;
DROP TYPE IF EXISTS DecisionEnum CASCADE;
DROP TYPE IF EXISTS PaymentStatusEnum CASCADE;

-- ======================================================
-- ENUM DEFINITIONS
-- ======================================================

CREATE TYPE FamilyStatus AS ENUM ('SINGLE', 'MARRIED', 'DIVORCED', 'WIDOWED');

CREATE TYPE ContractType AS ENUM ('PERMANENT', 'TEMPORARY', 'INTERIM');

CREATE TYPE SectorType AS ENUM (
    'PUBLIC',
    'PRIVATE_LARGE',
    'SME',
    'AGRICULTURE',
    'SERVICE',
    'TRADE',
    'CONSTRUCTION',
    'OTHER'
);

CREATE TYPE CreditType AS ENUM ('MORTGAGE', 'CONSUMER', 'AUTO', 'BUSINESS');

CREATE TYPE DecisionEnum AS ENUM ('IMMEDIATE_APPROVAL', 'MANUAL_REVIEW', 'AUTOMATIC_REJECTION');

CREATE TYPE PaymentStatusEnum AS ENUM ('ON_TIME', 'LATE', 'PAID_LATE', 'UNPAID_UNSETTLED', 'UNPAID_SETTLED');

-- ======================================================
-- TABLES
-- ======================================================

-- Employee Table
CREATE TABLE Employee (
    id              SERIAL PRIMARY KEY,
    firstName       VARCHAR(100) NOT NULL,
    lastName        VARCHAR(100) NOT NULL,
    dateOfBirth     DATE NOT NULL,
    city            VARCHAR(100),
    investment      BOOLEAN DEFAULT FALSE,
    placement       BOOLEAN DEFAULT FALSE,
    childrenCount   INT DEFAULT 0,
    familyStatus    FamilyStatus NOT NULL,
    score           DOUBLE PRECISION,
    salary          DOUBLE PRECISION NOT NULL,
    monthsInWork    INT NOT NULL,
    position        VARCHAR(100),
    contractType    ContractType NOT NULL,
    employmentSector SectorType NOT NULL,
    createdAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Professional Table
CREATE TABLE Professional (
    id                      SERIAL PRIMARY KEY,
    firstName               VARCHAR(100) NOT NULL,
    lastName                VARCHAR(100) NOT NULL,
    dateOfBirth             DATE NOT NULL,
    city                    VARCHAR(100),
    investment              BOOLEAN DEFAULT FALSE,
    placement               BOOLEAN DEFAULT FALSE,
    childrenCount           INT DEFAULT 0,
    familyStatus            FamilyStatus NOT NULL,
    score                   DOUBLE PRECISION,
    income                  DOUBLE PRECISION NOT NULL,
    taxRegistrationNumber   VARCHAR(50) UNIQUE NOT NULL,
    businessSector          SectorType NOT NULL,
    activity                VARCHAR(255),
    createdAt               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt               TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Credit Table
CREATE TABLE Credit (
    id                  SERIAL PRIMARY KEY,
    employeeId          INT,
    professionalId      INT,
    creditDate          DATE NOT NULL,
    requestedAmount     DOUBLE PRECISION NOT NULL,
    approvedAmount      DOUBLE PRECISION,
    interestRate        DOUBLE PRECISION,
    durationInMonths    INT,
    creditType          CreditType NOT NULL,
    decision            DecisionEnum NOT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employee(id) ON DELETE CASCADE,
    FOREIGN KEY (professionalId) REFERENCES Professional(id) ON DELETE CASCADE,
    CHECK ((employeeId IS NOT NULL AND professionalId IS NULL) OR (employeeId IS NULL AND professionalId IS NOT NULL))
);

-- Installement Table
CREATE TABLE Installement (
    id              SERIAL PRIMARY KEY,
    creditId        INT NOT NULL,
    dueDate         DATE NOT NULL,
    amount          DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (creditId) REFERENCES Credit(id) ON DELETE CASCADE
);

-- PaymentRecord Table
CREATE TABLE PaymentRecord (
    id              SERIAL PRIMARY KEY,
    installementId  INT NOT NULL,
    status          PaymentStatusEnum NOT NULL,
    createdAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (installementId) REFERENCES Installement(id) ON DELETE CASCADE
);
```

</details>

## Project Structure

The project is organized into the following main packages:

-   `src/Database`: Contains the `DB.java` class for handling the database connection.
-   `src/Enums`: Defines various enumerations used throughout the application (e.g., `CreditType`, `FamilyStatus`).
-   `src/Models`: Contains the entity classes that map to the database tables (e.g., `Credit`, `Employee`).
-   `src/Repositories`: Includes repository classes for data access operations on each entity.
-   `src/Services`: Holds the business logic, including `ScoringService` and `PaymentService`.
-   `src/Views`: Contains classes for rendering the console-based user interface menus and prompts.
-   `src/Main.java`: The entry point of the application.
-   `docs/`: Contains project documentation, including the class diagram.
-   `lib/`: Contains the PostgreSQL JDBC driver.
