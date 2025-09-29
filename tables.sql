DROP TABLE IF EXISTS PaymentRecord CASCADE;
DROP TABLE IF EXISTS Installement CASCADE;
DROP TABLE IF EXISTS Credit CASCADE;
DROP TABLE IF EXISTS Professional CASCADE;
DROP TABLE IF EXISTS Employee CASCADE;
DROP TABLE IF EXISTS Person CASCADE;

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

-- Person Table
CREATE TABLE Person (
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
    createdAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Credit Table
CREATE TABLE Credit (
    id                  SERIAL PRIMARY KEY,
    personId            INT NOT NULL,
    creditDate          DATE NOT NULL,
    requestedAmount     DOUBLE PRECISION NOT NULL,
    approvedAmount      DOUBLE PRECISION,
    interestRate        DOUBLE PRECISION,
    durationInMonths    INT,
    creditType          CreditType NOT NULL,
    decision            DecisionEnum NOT NULL,
    FOREIGN KEY (personId) REFERENCES Person(id) ON DELETE CASCADE
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

-- Employee Table (Subtype of Person)
CREATE TABLE Employee (
      personId        INT PRIMARY KEY,
      salary          DOUBLE PRECISION NOT NULL,
      monthsInWork    INT NOT NULL,
      position        VARCHAR(100),
      contractType    ContractType NOT NULL,
      employmentSector SectorType NOT NULL,
      FOREIGN KEY (personId) REFERENCES Person(id) ON DELETE CASCADE
);

-- Professional Table (Subtype of Person)
CREATE TABLE Professional (
    personId                INT PRIMARY KEY,
    income                  DOUBLE PRECISION NOT NULL,
    taxRegistrationNumber   VARCHAR(50) UNIQUE NOT NULL,
    businessSector          SectorType NOT NULL,
    activity                VARCHAR(255),
    FOREIGN KEY (personId) REFERENCES Person(id) ON DELETE CASCADE
);
