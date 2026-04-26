package dataAccessPackage;

import exceptionPackage.ConnectionException;
import exceptionPackage.LoadReferenceDataException;
import modelPackage.InvitationToPay;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.Role;
import modelPackage.UnitScout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReferenceDBAccess implements ReferenceDataAccess {

    @Override
    public List<Person> getAllPersons() throws LoadReferenceDataException {
        List<Person> persons = new ArrayList<>();
        String sql = "SELECT PersonId, AddressId, FirstName, LastName, Gender, "
                + "BirthDate, Email, Phone FROM Person ORDER BY LastName, FirstName";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Person person = new Person(
                        resultSet.getInt("PersonId"),
                        resultSet.getInt("AddressId"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Gender"),
                        resultSet.getDate("BirthDate"),
                        resultSet.getString("Email"),
                        resultSet.getString("Phone"));
                persons.add(person);
            }
            resultSet.close();
            statement.close();
            return persons;
        } catch (SQLException sqlException) {
            throw new LoadReferenceDataException("Unable to load persons.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new LoadReferenceDataException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<PersonRole> getAllPersonRoles() throws LoadReferenceDataException {
        List<PersonRole> personRoles = new ArrayList<>();
        String sql = "SELECT pr.Identifier, pr.PersonId, pr.RoleLabel, "
                + "pr.StartDate, pr.EndDate, p.FirstName, p.LastName "
                + "FROM PersonRole pr "
                + "INNER JOIN Person p ON pr.PersonId = p.PersonId "
                + "ORDER BY p.LastName, p.FirstName";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PersonRole personRole = new PersonRole(
                        resultSet.getInt("Identifier"),
                        resultSet.getInt("PersonId"),
                        resultSet.getString("RoleLabel"),
                        resultSet.getDate("StartDate"),
                        resultSet.getDate("EndDate"));
                Person person = new Person();
                person.setPersonId(resultSet.getInt("PersonId"));
                person.setFirstName(resultSet.getString("FirstName"));
                person.setLastName(resultSet.getString("LastName"));
                personRole.setPerson(person);
                personRoles.add(personRole);
            }
            resultSet.close();
            statement.close();
            return personRoles;
        } catch (SQLException sqlException) {
            throw new LoadReferenceDataException("Unable to load person roles.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new LoadReferenceDataException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<UnitScout> getAllUnitScouts() throws LoadReferenceDataException {
        List<UnitScout> units = new ArrayList<>();
        String sql = "SELECT Identifier, ResponsibleId, UnitName "
                + "FROM UnitScout ORDER BY UnitName";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UnitScout unitScout = new UnitScout(
                        resultSet.getInt("Identifier"),
                        resultSet.getInt("ResponsibleId"),
                        resultSet.getString("UnitName"));
                units.add(unitScout);
            }
            resultSet.close();
            statement.close();
            return units;
        } catch (SQLException sqlException) {
            throw new LoadReferenceDataException("Unable to load unit scouts.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new LoadReferenceDataException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<InvitationToPay> getAllInvitationsToPay() throws LoadReferenceDataException {
        List<InvitationToPay> invitations = new ArrayList<>();
        String sql = "SELECT Identifier, Amount, SendingDate, Communication "
                + "FROM InvitationToPay ORDER BY SendingDate DESC";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                InvitationToPay invitation = new InvitationToPay(
                        resultSet.getInt("Identifier"),
                        resultSet.getBigDecimal("Amount"),
                        resultSet.getDate("SendingDate"),
                        resultSet.getString("Communication"));
                invitations.add(invitation);
            }
            resultSet.close();
            statement.close();
            return invitations;
        } catch (SQLException sqlException) {
            throw new LoadReferenceDataException(
                    "Unable to load invitations to pay.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new LoadReferenceDataException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<Role> getAllRoles() throws LoadReferenceDataException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT Label FROM Role ORDER BY Label";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roles.add(new Role(resultSet.getString("Label")));
            }
            resultSet.close();
            statement.close();
            return roles;
        } catch (SQLException sqlException) {
            throw new LoadReferenceDataException("Unable to load roles.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new LoadReferenceDataException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public int addInvitationToPay(InvitationToPay invitation) throws LoadReferenceDataException {
        String sql = "INSERT INTO InvitationToPay (Amount, SendingDate, Communication) "
                + "VALUES (?, ?, ?)";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);
            statement.setBigDecimal(1, invitation.getAmount());
            statement.setDate(2, invitation.getSendingDate());
            statement.setString(3, invitation.getCommunication());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            int generatedId = -1;
            if (keys.next()) {
                generatedId = keys.getInt(1);
            }
            keys.close();
            statement.close();
            return generatedId;
        } catch (SQLException sqlException) {
            throw new LoadReferenceDataException(
                    "Unable to add the invitation to pay.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new LoadReferenceDataException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }
}
