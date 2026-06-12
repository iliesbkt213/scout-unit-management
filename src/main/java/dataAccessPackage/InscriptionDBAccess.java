package dataAccessPackage;

import exceptionPackage.AddInscriptionException;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.ConnectionException;
import exceptionPackage.DeleteInscriptionException;
import exceptionPackage.InscriptionNotFoundException;
import exceptionPackage.SearchInscriptionException;
import exceptionPackage.UpdateInscriptionException;
import modelPackage.Inscription;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDBAccess implements InscriptionDataAccess {

    @Override
    public int addInscription(Inscription inscription) throws AddInscriptionException {
        String sql = "INSERT INTO Inscription "
                + "(LegalGuardianId, PersonRoleId, UnitScoutId, InvitationToPayId, "
                + "Status, ParentAuthorization, RegistrationDate, PreRegistrationDate, "
                + "ConfirmationDate, MedicalCertificate, PaymentFinish) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            if (inscription.getLegalGuardianId() == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, inscription.getLegalGuardianId());
            }
            statement.setInt(2, inscription.getPersonRoleId());
            statement.setInt(3, inscription.getUnitScoutId());
            statement.setInt(4, inscription.getInvitationToPayId());
            statement.setString(5, inscription.getStatus());
            statement.setBoolean(6, inscription.getParentAuthorization());
            statement.setDate(7, inscription.getRegistrationDate());
            if (inscription.getPreRegistrationDate() == null) {
                statement.setNull(8, Types.DATE);
            } else {
                statement.setDate(8, inscription.getPreRegistrationDate());
            }
            if (inscription.getConfirmationDate() == null) {
                statement.setNull(9, Types.DATE);
            } else {
                statement.setDate(9, inscription.getConfirmationDate());
            }
            statement.setBoolean(10, inscription.getMedicalCertificate());
            statement.setBoolean(11, inscription.getPaymentFinish());

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
            throw new AddInscriptionException(
                    "Impossible d'ajouter l'inscription.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new AddInscriptionException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public void updateInscription(Inscription inscription)
            throws UpdateInscriptionException, InscriptionNotFoundException {
        String sql = "UPDATE Inscription SET "
                + "LegalGuardianId = ?, PersonRoleId = ?, UnitScoutId = ?, "
                + "InvitationToPayId = ?, Status = ?, ParentAuthorization = ?, "
                + "RegistrationDate = ?, PreRegistrationDate = ?, ConfirmationDate = ?, "
                + "MedicalCertificate = ?, PaymentFinish = ? "
                + "WHERE Identifier = ?";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);

            if (inscription.getLegalGuardianId() == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, inscription.getLegalGuardianId());
            }
            statement.setInt(2, inscription.getPersonRoleId());
            statement.setInt(3, inscription.getUnitScoutId());
            statement.setInt(4, inscription.getInvitationToPayId());
            statement.setString(5, inscription.getStatus());
            statement.setBoolean(6, inscription.getParentAuthorization());
            statement.setDate(7, inscription.getRegistrationDate());
            if (inscription.getPreRegistrationDate() == null) {
                statement.setNull(8, Types.DATE);
            } else {
                statement.setDate(8, inscription.getPreRegistrationDate());
            }
            if (inscription.getConfirmationDate() == null) {
                statement.setNull(9, Types.DATE);
            } else {
                statement.setDate(9, inscription.getConfirmationDate());
            }
            statement.setBoolean(10, inscription.getMedicalCertificate());
            statement.setBoolean(11, inscription.getPaymentFinish());
            statement.setInt(12, inscription.getIdentifier());

            int rows = statement.executeUpdate();
            statement.close();
            if (rows == 0) {
                throw new InscriptionNotFoundException(
                        "Inscription introuvable avec l'identifiant " + inscription.getIdentifier() + ".");
            }
        } catch (SQLException sqlException) {
            throw new UpdateInscriptionException(
                    "Impossible de modifier l'inscription.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new UpdateInscriptionException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public void deleteInscription(int identifier)
            throws DeleteInscriptionException, InscriptionNotFoundException {
        String sql = "DELETE FROM Inscription WHERE Identifier = ?";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, identifier);
            int rows = statement.executeUpdate();
            statement.close();
            if (rows == 0) {
                throw new InscriptionNotFoundException(
                        "Inscription introuvable avec l'identifiant " + identifier + ".");
            }
        } catch (SQLException sqlException) {
            throw new DeleteInscriptionException(
                    "Impossible de supprimer l'inscription.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new DeleteInscriptionException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<InscriptionDetail> getAllInscriptions() throws AllInscriptionsException {
        List<InscriptionDetail> details = new ArrayList<>();
        String sql = "SELECT i.Identifier, i.Status, i.RegistrationDate, "
                + "i.PreRegistrationDate, i.ConfirmationDate, "
                + "i.ParentAuthorization, i.MedicalCertificate, i.PaymentFinish, "
                + "p.FirstName, p.LastName, p.Email, "
                + "pr.RoleLabel, u.UnitName, c.CityName, c.PostalCode, "
                + "itp.Amount, "
                + "lg.FirstName AS GuardianFirstName, lg.LastName AS GuardianLastName "
                + "FROM Inscription i "
                + "INNER JOIN PersonRole pr ON i.PersonRoleId = pr.Identifier "
                + "INNER JOIN Person p ON pr.PersonId = p.PersonId "
                + "INNER JOIN UnitScout u ON i.UnitScoutId = u.Identifier "
                + "INNER JOIN InvitationToPay itp ON i.InvitationToPayId = itp.Identifier "
                + "INNER JOIN Address a ON p.AddressId = a.AddressId "
                + "INNER JOIN City c ON a.CityId = c.CityId "
                + "LEFT JOIN Person lg ON i.LegalGuardianId = lg.PersonId "
                + "ORDER BY i.RegistrationDate DESC";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                details.add(buildInscriptionDetail(resultSet));
            }
            resultSet.close();
            statement.close();
            return details;
        } catch (SQLException sqlException) {
            throw new AllInscriptionsException(
                    "Impossible de charger toutes les inscriptions.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new AllInscriptionsException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public Inscription getInscriptionById(int identifier)
            throws AllInscriptionsException, InscriptionNotFoundException {
        String sql = "SELECT Identifier, LegalGuardianId, PersonRoleId, UnitScoutId, "
                + "InvitationToPayId, Status, ParentAuthorization, RegistrationDate, "
                + "PreRegistrationDate, ConfirmationDate, "
                + "MedicalCertificate, PaymentFinish "
                + "FROM Inscription WHERE Identifier = ?";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, identifier);
            ResultSet resultSet = statement.executeQuery();
            Inscription inscription = null;
            if (resultSet.next()) {
                int guardianId = resultSet.getInt("LegalGuardianId");
                Integer guardian = resultSet.wasNull() ? null : guardianId;
                inscription = new Inscription(
                        resultSet.getInt("Identifier"),
                        resultSet.getInt("PersonRoleId"),
                        resultSet.getInt("UnitScoutId"),
                        resultSet.getInt("InvitationToPayId"),
                        resultSet.getString("Status"),
                        resultSet.getBoolean("ParentAuthorization"),
                        resultSet.getDate("RegistrationDate"),
                        resultSet.getBoolean("MedicalCertificate"),
                        resultSet.getBoolean("PaymentFinish"),
                        guardian);
                inscription.setPreRegistrationDate(resultSet.getDate("PreRegistrationDate"));
                inscription.setConfirmationDate(resultSet.getDate("ConfirmationDate"));
            }
            resultSet.close();
            statement.close();
            if (inscription == null) {
                throw new InscriptionNotFoundException(
                        "Inscription introuvable avec l'identifiant " + identifier + ".");
            }
            return inscription;
        } catch (SQLException sqlException) {
            throw new AllInscriptionsException(
                    "Impossible de charger l'inscription.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new AllInscriptionsException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<InscriptionDetail> searchInscriptionsByUnitAndDate(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        List<InscriptionDetail> details = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.Identifier, i.Status, i.RegistrationDate, "
                + "i.PreRegistrationDate, i.ConfirmationDate, "
                + "i.ParentAuthorization, i.MedicalCertificate, i.PaymentFinish, "
                + "p.FirstName, p.LastName, p.Email, "
                + "pr.RoleLabel, u.UnitName, c.CityName, c.PostalCode, "
                + "itp.Amount, "
                + "lg.FirstName AS GuardianFirstName, lg.LastName AS GuardianLastName "
                + "FROM Inscription i "
                + "INNER JOIN UnitScout u ON i.UnitScoutId = u.Identifier "
                + "INNER JOIN PersonRole pr ON i.PersonRoleId = pr.Identifier "
                + "INNER JOIN Person p ON pr.PersonId = p.PersonId "
                + "INNER JOIN Address a ON p.AddressId = a.AddressId "
                + "INNER JOIN City c ON a.CityId = c.CityId "
                + "INNER JOIN InvitationToPay itp ON i.InvitationToPayId = itp.Identifier "
                + "LEFT JOIN Person lg ON i.LegalGuardianId = lg.PersonId "
                + "WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (criteria.getUnitName() != null && !criteria.getUnitName().trim().isEmpty()) {
            sql.append("AND u.UnitName LIKE ? ");
            params.add("%" + criteria.getUnitName() + "%");
        }
        if (criteria.getRegistrationDateFrom() != null) {
            sql.append("AND i.RegistrationDate >= ? ");
            params.add(criteria.getRegistrationDateFrom());
        }
        if (criteria.getRegistrationDateTo() != null) {
            sql.append("AND i.RegistrationDate <= ? ");
            params.add(criteria.getRegistrationDateTo());
        }
        sql.append("ORDER BY i.RegistrationDate DESC");
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql.toString());
            bindParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                details.add(buildInscriptionDetail(resultSet));
            }
            resultSet.close();
            statement.close();
            return details;
        } catch (SQLException sqlException) {
            throw new SearchInscriptionException(
                    "Impossible de rechercher les inscriptions par unité et date.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new SearchInscriptionException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<InscriptionDetail> searchInscriptionsByRoleAndCity(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        List<InscriptionDetail> details = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.Identifier, i.Status, i.RegistrationDate, "
                + "i.PreRegistrationDate, i.ConfirmationDate, "
                + "i.ParentAuthorization, i.MedicalCertificate, i.PaymentFinish, "
                + "p.FirstName, p.LastName, p.Email, "
                + "pr.RoleLabel, u.UnitName, c.CityName, c.PostalCode, "
                + "itp.Amount, "
                + "lg.FirstName AS GuardianFirstName, lg.LastName AS GuardianLastName "
                + "FROM Inscription i "
                + "INNER JOIN PersonRole pr ON i.PersonRoleId = pr.Identifier "
                + "INNER JOIN Role r ON pr.RoleLabel = r.Label "
                + "INNER JOIN Person p ON pr.PersonId = p.PersonId "
                + "INNER JOIN Address a ON p.AddressId = a.AddressId "
                + "INNER JOIN City c ON a.CityId = c.CityId "
                + "INNER JOIN UnitScout u ON i.UnitScoutId = u.Identifier "
                + "INNER JOIN InvitationToPay itp ON i.InvitationToPayId = itp.Identifier "
                + "LEFT JOIN Person lg ON i.LegalGuardianId = lg.PersonId "
                + "WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (criteria.getRoleLabel() != null && !criteria.getRoleLabel().trim().isEmpty()) {
            sql.append("AND r.Label = ? ");
            params.add(criteria.getRoleLabel());
        }
        if (criteria.getCityName() != null && !criteria.getCityName().trim().isEmpty()) {
            sql.append("AND c.CityName LIKE ? ");
            params.add("%" + criteria.getCityName() + "%");
        }
        sql.append("ORDER BY p.LastName, p.FirstName");
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql.toString());
            bindParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                details.add(buildInscriptionDetail(resultSet));
            }
            resultSet.close();
            statement.close();
            return details;
        } catch (SQLException sqlException) {
            throw new SearchInscriptionException(
                    "Impossible de rechercher les inscriptions par rôle et ville.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new SearchInscriptionException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public List<InscriptionDetail> searchPendingInscriptionsByStatus(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        List<InscriptionDetail> details = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.Identifier, i.Status, i.RegistrationDate, "
                + "i.PreRegistrationDate, i.ConfirmationDate, "
                + "i.ParentAuthorization, i.MedicalCertificate, i.PaymentFinish, "
                + "p.FirstName, p.LastName, p.Email, "
                + "pr.RoleLabel, u.UnitName, c.CityName, c.PostalCode, "
                + "itp.Amount, "
                + "lg.FirstName AS GuardianFirstName, lg.LastName AS GuardianLastName "
                + "FROM Inscription i "
                + "INNER JOIN PersonRole pr ON i.PersonRoleId = pr.Identifier "
                + "INNER JOIN Person p ON pr.PersonId = p.PersonId "
                + "INNER JOIN UnitScout u ON i.UnitScoutId = u.Identifier "
                + "INNER JOIN InvitationToPay itp ON i.InvitationToPayId = itp.Identifier "
                + "INNER JOIN Address a ON p.AddressId = a.AddressId "
                + "INNER JOIN City c ON a.CityId = c.CityId "
                + "LEFT JOIN Person lg ON i.LegalGuardianId = lg.PersonId "
                + "WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (criteria.getStatus() != null && !criteria.getStatus().trim().isEmpty()) {
            sql.append("AND i.Status = ? ");
            params.add(criteria.getStatus());
        }
        if (criteria.getRegistrationDateFrom() != null) {
            sql.append("AND i.RegistrationDate >= ? ");
            params.add(criteria.getRegistrationDateFrom());
        }
        sql.append("ORDER BY i.RegistrationDate ASC");
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql.toString());
            bindParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                details.add(buildInscriptionDetail(resultSet));
            }
            resultSet.close();
            statement.close();
            return details;
        } catch (SQLException sqlException) {
            throw new SearchInscriptionException(
                    "Impossible de rechercher les inscriptions par statut.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new SearchInscriptionException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    @Override
    public int countConfirmedInscriptionsInPeriod(Date from, Date to)
            throws SearchInscriptionException {
        String sql = "SELECT COUNT(*) AS Total FROM Inscription "
                + "WHERE Status = ? AND RegistrationDate BETWEEN ? AND ?";
        try {
            Connection connection = SingletonConnection.getInstance();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, Inscription.STATUS_CONFIRMED);
            statement.setDate(2, from);
            statement.setDate(3, to);
            ResultSet resultSet = statement.executeQuery();
            int total = 0;
            if (resultSet.next()) {
                total = resultSet.getInt("Total");
            }
            resultSet.close();
            statement.close();
            return total;
        } catch (SQLException sqlException) {
            throw new SearchInscriptionException(
                    "Impossible de compter les inscriptions confirmées.", sqlException);
        } catch (ConnectionException connectionException) {
            throw new SearchInscriptionException(
                    "Impossible d'obtenir une connexion à la base de données.", connectionException);
        }
    }

    private void bindParameters(PreparedStatement statement, List<Object> params)
            throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i);
            if (value instanceof String) {
                statement.setString(i + 1, (String) value);
            } else if (value instanceof Date) {
                statement.setDate(i + 1, (Date) value);
            } else if (value instanceof Integer) {
                statement.setInt(i + 1, (Integer) value);
            } else {
                statement.setObject(i + 1, value);
            }
        }
    }

    private InscriptionDetail buildInscriptionDetail(ResultSet resultSet) throws SQLException {
        InscriptionDetail detail = new InscriptionDetail();
        detail.setInscriptionId(resultSet.getInt("Identifier"));
        detail.setStatus(resultSet.getString("Status"));
        detail.setRegistrationDate(resultSet.getDate("RegistrationDate"));
        detail.setPreRegistrationDate(resultSet.getDate("PreRegistrationDate"));
        detail.setConfirmationDate(resultSet.getDate("ConfirmationDate"));
        detail.setParentAuthorization(resultSet.getBoolean("ParentAuthorization"));
        detail.setMedicalCertificate(resultSet.getBoolean("MedicalCertificate"));
        detail.setPaymentFinish(resultSet.getBoolean("PaymentFinish"));
        detail.setPersonFirstName(resultSet.getString("FirstName"));
        detail.setPersonLastName(resultSet.getString("LastName"));
        detail.setPersonEmail(resultSet.getString("Email"));
        detail.setRoleLabel(resultSet.getString("RoleLabel"));
        detail.setUnitName(resultSet.getString("UnitName"));
        detail.setCityName(resultSet.getString("CityName"));
        detail.setPostalCode(resultSet.getString("PostalCode"));
        detail.setAmount(resultSet.getBigDecimal("Amount"));
        String guardianFirst = resultSet.getString("GuardianFirstName");
        String guardianLast = resultSet.getString("GuardianLastName");
        if (guardianFirst != null && guardianLast != null) {
            detail.setLegalGuardianName(guardianFirst + " " + guardianLast);
        }
        return detail;
    }
}
