# Gestion d'unité scoute

Projet pour le cours de **Programmation orientée objet avancée** — Bloc 2 — Hénallux.

Auteurs : Renilde de Smedt et Ilies Boukhatem.

## Domaine d'application

L'application permet d'informatiser la gestion d'une unité scoute de la
Fédération des Scouts Baden-Powell. La table CRUD est `Inscription`,
qui lie une personne jouant un rôle à une unité, une invitation à payer
et (facultativement) un tuteur légal.

## Architecture

Le projet suit l'architecture MVC + 3-tiers vue dans le cours :

```
viewPackage         → Interface Swing (JFrame, JPanel, JTable, JComboBox, JSpinner)
controllerPackage   → Contrôleur applicatif (délègue à la couche métier)
businessPackage     → Gestionnaires métier (validations, tâche métier)
dataAccessPackage   → Connexion Singleton + interfaces DAO et implémentations
modelPackage        → Classes POJO avec validation dans les setters
exceptionPackage    → Exceptions personnalisées, une par opération
```

## Base de données

* Nom de la base : `scout_unit`
* MySQL avec mot de passe (configurer `USER` et `PASSWORD` dans
  `dataAccessPackage/SingletonConnection.java`).
* Les scripts SQL sont dans `sql/` :
  * `01_create_database.sql` — crée le schéma avec contraintes `CHECK`,
    `UNIQUE` et clés étrangères.
  * `02_fill_database.sql` — insère des données d'exemple pour pouvoir
    démontrer le programme immédiatement.

## Lancement

### Avec Maven

```
mvn clean compile exec:java -Dexec.mainClass="viewPackage.Main"
mvn test
```

### Avec IntelliJ

1. Ouvrir le dossier `ScoutUnitManagement`.
2. Marquer `src/main/java` comme Sources Root et `src/test/java`
   comme Test Sources Root.
3. Ajouter MySQL Connector/J et JUnit 5 aux bibliothèques du projet.
4. Lancer `viewPackage.Main`.

## Fonctionnalités

* **Fenêtre d'accueil** avec barre de menus (Accueil / Inscriptions /
  Recherches / Outils / Aide) et un panneau d'animation scout sur un
  thread séparé (`ScoutAnimationPanel`).
* **CRUD sur `Inscription`** (texte, numérique/FK, date, booléen,
  tuteur légal facultatif) avec combo-boxes pour les clés étrangères
  et double validation (Vue + Métier + setters Modèle).
* **Trois recherches avec jointures multi-tables** (chacune utilise au
  moins trois tables, une utilise une date via `JSpinner`) :
  1. Par nom d'unité et période d'inscription (filtre date).
  2. Par rôle et ville (filtre texte, joint Role/Person/Address/City).
  3. Par statut et date d'inscription (filtre date).
* **JTable** affichant des colonnes provenant d'au moins trois tables
  différentes (personne + rôle + unité + ville + montant).
* **Tâche métier** calculée dans `InscriptionManager` et affichée
  via `BusinessTaskPanel` (recettes totales, taux de confirmation,
  nombre d'inscriptions incomplètes, nombre d'inscriptions confirmées
  sur une période).
* **Thread supplémentaire** : `ScoutAnimationPanel` peint une petite
  scène scoute (drapeau qui flotte, feu de camp qui vacille, tente)
  sur un thread daemon. Il n'accède pas à la base de données.
* **Tests JUnit** sur les setters du modèle et sur le gestionnaire
  métier (avec un faux DAO pour que les tests ne touchent pas la BD).
* **Exceptions personnalisées** par opération, aucune `SQLException`
  ne remonte aux couches supérieures, aucun nom de type `BDException`.
* **Connexion Singleton** + **PreparedStatement** partout (pas de
  concaténation de chaînes dans les requêtes → pas d'injection SQL).
* **Noms anglais** pour les classes, variables, méthodes, tables et
  colonnes ; **interface utilisateur en français**.

## Liste des contraintes

| Contrainte | Emplacement |
|---|---|
| Fenêtre d'accueil avec menus | `viewPackage/MainFrame.java` |
| CRUD avec texte/numérique/date/booléen/facultatif/FK | `Inscription` (modèle + panneaux ajout/modification) |
| 3 recherches avec 3+ tables, une avec date | méthodes de recherche dans `InscriptionDBAccess` + panneaux vue |
| JTable avec colonnes inter-tables | `InscriptionListPanel`, panneaux de recherche |
| Tâche métier dans la couche métier | `InscriptionManager.computeTotalRevenueFromConfirmedInscriptions`, `computeConfirmationRate`, `countIncompleteInscriptions` |
| Thread supplémentaire (ni horloge, ni BD) | `ScoutAnimationPanel` |
| Tests JUnit sur les filtres | `InscriptionTest`, `PersonTest`, `InvitationToPayTest`, `InscriptionManagerTest` |
| MySQL avec mot de passe | constantes dans `SingletonConnection` |
| 6+ tables, FK + contraintes | `01_create_database.sql` (8 tables) |
| Singleton + PreparedStatement | `SingletonConnection`, `InscriptionDBAccess`, `ReferenceDBAccess` |
| Pattern DAO avec interfaces | `InscriptionDataAccess`, `ReferenceDataAccess` et leurs implémentations BD |
| Exceptions personnalisées, pas de `SQLException`, pas de `BDException` | `exceptionPackage` |
| Anglais dans le code et la BD | code + BD |
| Pas de sortie console / dialogue hors couche Vue | uniquement appels `JOptionPane` dans `viewPackage` |
