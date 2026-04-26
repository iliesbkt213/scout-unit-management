-- =========================================================
-- Scout Unit Management - Database creation script
-- Project Java OO Advanced - Renilde de Smedt & Ilies Boukhatem
-- =========================================================

DROP DATABASE IF EXISTS scout_unit;
CREATE DATABASE scout_unit
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE scout_unit;

-- =========================================================
-- Table : City
-- =========================================================
CREATE TABLE City (
    CityId INT PRIMARY KEY NOT NULL,
    CityName VARCHAR(100) NOT NULL,
    PostalCode VARCHAR(20) NOT NULL,
    CONSTRAINT chk_city_name_not_empty CHECK (CHAR_LENGTH(TRIM(CityName)) > 0),
    CONSTRAINT chk_postal_code_not_empty CHECK (CHAR_LENGTH(TRIM(PostalCode)) > 0)
);

-- =========================================================
-- Table : Address
-- =========================================================
CREATE TABLE Address (
    AddressId INT PRIMARY KEY NOT NULL,
    CityId INT NOT NULL,
    Street VARCHAR(150) NOT NULL,
    Number VARCHAR(10) NOT NULL,
    CONSTRAINT fk_address_city FOREIGN KEY (CityId) REFERENCES City(CityId),
    CONSTRAINT chk_street_not_empty CHECK (CHAR_LENGTH(TRIM(Street)) > 0),
    CONSTRAINT chk_number_not_empty CHECK (CHAR_LENGTH(TRIM(Number)) > 0)
);

-- =========================================================
-- Table : Person
-- =========================================================
CREATE TABLE Person (
    PersonId INT PRIMARY KEY NOT NULL,
    AddressId INT NOT NULL,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Gender VARCHAR(1) NOT NULL,
    BirthDate DATE NOT NULL,
    Email VARCHAR(150) NOT NULL,
    Phone VARCHAR(30) NOT NULL,
    CONSTRAINT fk_person_address FOREIGN KEY (AddressId) REFERENCES Address(AddressId),
    CONSTRAINT chk_person_gender CHECK (Gender IN ('M', 'F', 'X')),
    CONSTRAINT chk_person_email CHECK (Email LIKE '%@%.%'),
    CONSTRAINT uk_person_email UNIQUE (Email)
);

-- =========================================================
-- Table : Role
-- =========================================================
CREATE TABLE Role (
    Label VARCHAR(50) PRIMARY KEY NOT NULL,
    CONSTRAINT chk_role_not_empty CHECK (CHAR_LENGTH(TRIM(Label)) > 0)
);

-- =========================================================
-- Table : PersonRole
-- =========================================================
CREATE TABLE PersonRole (
    Identifier INT PRIMARY KEY AUTO_INCREMENT,
    PersonId INT NOT NULL,
    RoleLabel VARCHAR(50) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NULL,
    CONSTRAINT fk_personrole_person FOREIGN KEY (PersonId) REFERENCES Person(PersonId),
    CONSTRAINT fk_personrole_role FOREIGN KEY (RoleLabel) REFERENCES Role(Label),
    CONSTRAINT chk_personrole_dates CHECK (EndDate IS NULL OR EndDate >= StartDate)
);

-- =========================================================
-- Table : UnitScout
-- =========================================================
CREATE TABLE UnitScout (
    Identifier INT PRIMARY KEY AUTO_INCREMENT,
    ResponsibleId INT NOT NULL,
    UnitName VARCHAR(100) NOT NULL,
    CONSTRAINT fk_unitscout_responsible FOREIGN KEY (ResponsibleId) REFERENCES PersonRole(Identifier),
    CONSTRAINT chk_unit_name_not_empty CHECK (CHAR_LENGTH(TRIM(UnitName)) > 0),
    CONSTRAINT uk_unit_name UNIQUE (UnitName)
);

-- =========================================================
-- Table : InvitationToPay
-- =========================================================
CREATE TABLE InvitationToPay (
    Identifier INT PRIMARY KEY AUTO_INCREMENT,
    Amount DECIMAL(10,2) NOT NULL,
    SendingDate DATE NOT NULL,
    Communication TEXT NOT NULL,
    CONSTRAINT chk_amount_positive CHECK (Amount >= 0),
    CONSTRAINT chk_communication_not_empty CHECK (CHAR_LENGTH(TRIM(Communication)) > 0)
);

-- =========================================================
-- Table : Inscription (CRUD table)
-- =========================================================
CREATE TABLE Inscription (
    Identifier INT PRIMARY KEY AUTO_INCREMENT,
    LegalGuardianId INT NULL,
    PersonRoleId INT NOT NULL,
    UnitScoutId INT NOT NULL,
    InvitationToPayId INT NOT NULL,
    Status VARCHAR(50) NOT NULL,
    ParentAuthorization BOOLEAN NOT NULL,
    RegistrationDate DATE NOT NULL,
    PreRegistrationDate DATE NULL,
    ConfirmationDate DATE NULL,
    MedicalCertificate BOOLEAN NOT NULL,
    PaymentFinish BOOLEAN NOT NULL,
    CONSTRAINT fk_inscription_legalguardian FOREIGN KEY (LegalGuardianId) REFERENCES Person(PersonId),
    CONSTRAINT fk_inscription_personrole FOREIGN KEY (PersonRoleId) REFERENCES PersonRole(Identifier),
    CONSTRAINT fk_inscription_unitscout FOREIGN KEY (UnitScoutId) REFERENCES UnitScout(Identifier),
    CONSTRAINT fk_inscription_invitation FOREIGN KEY (InvitationToPayId) REFERENCES InvitationToPay(Identifier),
    CONSTRAINT chk_inscription_status CHECK (Status IN ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELLED')),
    CONSTRAINT chk_inscription_payment CHECK (PaymentFinish = FALSE OR Status = 'CONFIRMED'),
    CONSTRAINT chk_inscription_pre_registration CHECK (PreRegistrationDate IS NULL OR PreRegistrationDate <= RegistrationDate),
    CONSTRAINT chk_inscription_confirmation CHECK (ConfirmationDate IS NULL OR ConfirmationDate >= RegistrationDate)
);
