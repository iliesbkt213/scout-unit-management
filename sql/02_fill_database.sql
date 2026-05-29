-- =========================================================
-- Scout Unit Management - Sample data
-- =========================================================

USE scout_unit;

-- City
INSERT INTO City (CityId, CityName, PostalCode) VALUES
    (1, 'Namur', '5000'),
    (2, 'Liège', '4000'),
    (3, 'Bruxelles', '1000'),
    (4, 'Charleroi', '6000'),
    (5, 'Mons', '7000');

-- Address
INSERT INTO Address (AddressId, CityId, Street, Number) VALUES
    (1, 1, 'Rue de Bruxelles', '12'),
    (2, 2, 'Rue Saint-Gilles', '5'),
    (3, 3, 'Avenue Louise', '120'),
    (4, 4, 'Rue de la Montagne', '3B'),
    (5, 5, 'Grand Place', '8'),
    (6, 1, 'Rue de l''Ange', '22'),
    (7, 2, 'Boulevard de la Sauvenière', '14'),
    (8, 3, 'Rue Royale', '76'),
    (9, 1, 'Chaussée de Louvain', '210'),
    (10, 2, 'Rue Saint-Lambert', '9');

-- Person
INSERT INTO Person (PersonId, AddressId, FirstName, LastName, Gender, BirthDate, Email, Phone) VALUES
    (1, 1, 'Renilde', 'de Smedt', 'F', '2005-04-12', 'renilde.desmedt@example.be', '+32 81 23 45 67'),
    (2, 2, 'Ilies', 'Boukhatem', 'M', '2005-09-23', 'ilies.boukhatem@example.be', '+32 4 22 33 44'),
    (3, 3, 'Camille', 'Lefèvre', 'F', '2014-03-05', 'camille.lefevre@example.be', '+32 2 555 12 34'),
    (4, 4, 'Lucas', 'Martin', 'M', '2013-11-18', 'lucas.martin@example.be', '+32 71 12 34 56'),
    (5, 5, 'Emma', 'Dubois', 'F', '2012-07-30', 'emma.dubois@example.be', '+32 65 33 44 55'),
    (6, 6, 'Hugo', 'Bernard', 'M', '2010-01-14', 'hugo.bernard@example.be', '+32 81 88 77 66'),
    (7, 7, 'Sarah', 'Petit', 'F', '2011-05-22', 'sarah.petit@example.be', '+32 4 11 22 33'),
    (8, 8, 'Nathan', 'Renard', 'M', '2009-08-09', 'nathan.renard@example.be', '+32 2 999 88 77'),
    (9, 9, 'Sophie', 'Lambert', 'F', '1980-02-15', 'sophie.lambert@example.be', '+32 81 11 22 33'),
    (10, 10, 'Marc', 'Janssen', 'M', '1978-12-01', 'marc.janssen@example.be', '+32 4 99 88 77');

-- Role
INSERT INTO Role (Label) VALUES
    ('SCOUT'),
    ('CHEF'),
    ('RESPONSABLE'),
    ('SECRÉTAIRE'),
    ('TRÉSORIER');

-- PersonRole
INSERT INTO PersonRole (Identifier, PersonId, RoleLabel, StartDate, EndDate) VALUES
    (1, 1, 'RESPONSABLE', '2024-09-01', NULL),
    (2, 2, 'CHEF', '2024-09-01', NULL),
    (3, 3, 'SCOUT', '2024-09-01', NULL),
    (4, 4, 'SCOUT', '2024-09-15', NULL),
    (5, 5, 'SCOUT', '2024-10-01', NULL),
    (6, 6, 'SCOUT', '2024-10-15', NULL),
    (7, 7, 'SCOUT', '2024-09-01', NULL),
    (8, 8, 'SCOUT', '2025-01-15', NULL),
    (9, 9, 'TRÉSORIER', '2024-08-01', NULL),
    (10, 10, 'SECRÉTAIRE', '2024-08-01', NULL);

-- UnitScout
INSERT INTO UnitScout (Identifier, ResponsibleId, UnitName) VALUES
    (1, 1, 'Meute des Loups'),
    (2, 1, 'Patrouille des Aigles'),
    (3, 2, 'Tanière des Renards'),
    (4, 2, 'Groupe des Castors');

-- InvitationToPay
INSERT INTO InvitationToPay (Identifier, Amount, SendingDate, Communication) VALUES
    (1, 120.00, '2025-09-01', 'Cotisation 2025-2026 - Meute des Loups'),
    (2, 120.00, '2025-09-01', 'Cotisation 2025-2026 - Patrouille des Aigles'),
    (3, 110.00, '2025-09-15', 'Cotisation 2025-2026 - Tanière des Renards'),
    (4, 95.00, '2025-09-20', 'Cotisation 2025-2026 - Groupe des Castors'),
    (5, 110.00, '2025-10-01', 'Cotisation 2025-2026 - tarif réduit'),
    (6, 120.00, '2026-01-10', 'Cotisation de mi-année'),
    (7, 130.00, '2026-02-15', 'Cotisation tardive'),
    (8, 120.00, '2026-03-01', 'Cotisation de printemps');

-- Inscription
INSERT INTO Inscription (LegalGuardianId, PersonRoleId, UnitScoutId, InvitationToPayId,
    Status, ParentAuthorization, RegistrationDate, PreRegistrationDate, ConfirmationDate,
    MedicalCertificate, PaymentFinish) VALUES
    (9, 3, 1, 1, 'CONFIRMED', TRUE, '2025-09-05', '2025-08-20', '2025-09-10', TRUE, TRUE),
    (9, 4, 1, 2, 'CONFIRMED', TRUE, '2025-09-08', '2025-08-25', '2025-09-15', TRUE, TRUE),
    (10, 5, 2, 3, 'PENDING', TRUE, '2025-09-12', '2025-08-30', NULL, FALSE, FALSE),
    (10, 6, 2, 4, 'CONFIRMED', TRUE, '2025-09-20', NULL, '2025-09-25', TRUE, TRUE),
    (NULL, 7, 3, 5, 'PENDING', FALSE, '2025-10-02', '2025-09-15', NULL, FALSE, FALSE),
    (NULL, 8, 4, 6, 'CANCELLED', TRUE, '2026-01-15', '2026-01-05', NULL, TRUE, FALSE),
    (9, 3, 3, 7, 'CONFIRMED', TRUE, '2026-02-20', NULL, '2026-02-25', TRUE, TRUE),
    (10, 5, 4, 8, 'PENDING', TRUE, '2026-03-05', '2026-02-20', NULL, TRUE, FALSE);
