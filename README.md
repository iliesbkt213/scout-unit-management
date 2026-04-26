# Scout Unit Management

Project for course **Programmation orientée objet avancée** - Bloc 2 - Hénallux.

Authors: Renilde de Smedt and Ilies Boukhatem.

## Domain

The application supports the management of a scout unit (informatisation
de la gestion d'une unité scoute). The CRUD table is `Inscription`,
which links a person playing a role to a unit, an invitation to pay
and (optionally) a legal guardian.

## Architecture

The project follows the MVC + 3-tier architecture seen in the course:

```
viewPackage         → Swing UI (JFrame, JPanel, JTable, JComboBox, JSpinner)
controllerPackage   → Application controller (delegates to business layer)
businessPackage     → Business managers (validations, business task)
dataAccessPackage   → Singleton connection + DAO interfaces and implementations
modelPackage        → POJO model classes with validation in setters
exceptionPackage    → Custom exceptions, one per operation
```

## Database

* Database name: `scout_unit`
* MySQL with password (configure `USER` and `PASSWORD` in
  `dataAccessPackage/SingletonConnection.java`).
* SQL scripts are in `sql/`:
  * `01_create_database.sql` — creates the schema with `CHECK`,
    `UNIQUE` and foreign-key constraints.
  * `02_fill_database.sql` — inserts sample data so the program can be
    demonstrated immediately.

## Running

### With Maven

```
mvn clean compile exec:java -Dexec.mainClass="viewPackage.Main"
mvn test
```

### With IntelliJ

1. Open the `ScoutUnitManagement` folder.
2. Mark `src/main/java` as Sources Root and `src/test/java` as Test
   Sources Root.
3. Add MySQL Connector/J and JUnit 5 to the project libraries.
4. Run `viewPackage.Main`.

## Features

* **Welcome window** with menu bar (Home / Inscriptions / Searches /
  Tools / Help) and an animated scout-themed panel running on a
  separate thread (`ScoutAnimationPanel`).
* **CRUD on `Inscription`** (text, numeric/FK, date, boolean,
  optional legal guardian) with combo-boxes for foreign keys and
  double validation (View + Business + Model setters).
* **Three searches with multi-table joins** (each crosses at least
  three tables, one uses a `JSpinner` date):
  1. By unit name and registration-date period (date filter).
  2. By role and city (string filter, joins on Role/Person/Address/City).
  3. By status and registration date (date filter).
* **JTable** results showing columns coming from at least three
  different tables (person + role + unit + city + amount).
* **Business task** computed in `InscriptionManager` and displayed
  via `BusinessTaskPanel` (total revenue, confirmation rate, count
  of incomplete inscriptions, count of confirmed inscriptions in a
  period).
* **Additional thread** : `ScoutAnimationPanel` paints a small scout
  scene (waving flag, flickering campfire, tent) on a daemon thread.
  It does not access the database.
* **JUnit tests** on the model setters and on the business manager
  (using a fake DAO so tests do not hit the database).
* **Custom exceptions** per operation, no `SQLException` propagated
  to upper layers, no name `BDException`.
* **Singleton connection** + **PreparedStatement** everywhere (no
  string concatenation in queries → no SQL injection).
* **English names** for classes, variables, methods, tables and
  columns.

## Constraints checklist

| Constraint | Where |
|---|---|
| Welcome window with menus | `viewPackage/MainFrame.java` |
| CRUD with text/numeric/date/boolean/optional/FK | `Inscription` (model + add/update panels) |
| 3 searches with 3+ tables, one with date | `InscriptionDBAccess` search methods + view panels |
| JTable with cross-table columns | `InscriptionListPanel`, search panels |
| Business task in business layer | `InscriptionManager.computeTotalRevenueFromConfirmedInscriptions`, `computeConfirmationRate`, `countIncompleteInscriptions` |
| Additional thread (not clock, not DB) | `ScoutAnimationPanel` |
| JUnit tests on filters | `InscriptionTest`, `PersonTest`, `InvitationToPayTest`, `InscriptionManagerTest` |
| MySQL with password | `SingletonConnection` constants |
| 6+ tables, FK + constraints | `01_create_database.sql` (8 tables) |
| Singleton + PreparedStatement | `SingletonConnection`, `InscriptionDBAccess`, `ReferenceDBAccess` |
| DAO pattern with interfaces | `InscriptionDataAccess`, `ReferenceDataAccess` and DB implementations |
| Custom exceptions, no `SQLException`, no `BDException` | `exceptionPackage` |
| English everywhere | code + DB |
| No console / dialog output outside View | only `JOptionPane` calls in `viewPackage` |
