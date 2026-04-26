package businessPackage;

import dataAccessPackage.InscriptionDBAccess;
import dataAccessPackage.InscriptionDataAccess;
import exceptionPackage.AddInscriptionException;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.BusinessTaskException;
import exceptionPackage.DeleteInscriptionException;
import exceptionPackage.InscriptionNotFoundException;
import exceptionPackage.SearchInscriptionException;
import exceptionPackage.UpdateInscriptionException;
import modelPackage.Inscription;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class InscriptionManager {

    private InscriptionDataAccess dao;

    public InscriptionManager() {
        setDao(new InscriptionDBAccess());
    }

    public InscriptionManager(InscriptionDataAccess dao) {
        setDao(dao);
    }

    public InscriptionDataAccess getDao() {
        return dao;
    }

    public void setDao(InscriptionDataAccess dao) {
        if (dao == null) {
            throw new IllegalArgumentException("Le DAO ne peut pas être nul.");
        }
        this.dao = dao;
    }

    public int addInscription(Inscription inscription) throws AddInscriptionException {
        if (inscription == null) {
            throw new AddInscriptionException("L'inscription ne peut pas être nulle.");
        }
        if (Boolean.TRUE.equals(inscription.getPaymentFinish())
                && !Inscription.STATUS_CONFIRMED.equals(inscription.getStatus())) {
            throw new AddInscriptionException(
                    "Une inscription dont le paiement est effectué doit avoir le statut CONFIRMED.");
        }
        if (Inscription.STATUS_CONFIRMED.equals(inscription.getStatus())
                && !Boolean.TRUE.equals(inscription.getMedicalCertificate())) {
            throw new AddInscriptionException(
                    "Cannot confirm an inscription without medical certificate.");
        }
        return dao.addInscription(inscription);
    }

    public void updateInscription(Inscription inscription)
            throws UpdateInscriptionException, InscriptionNotFoundException {
        if (inscription == null) {
            throw new UpdateInscriptionException("L'inscription ne peut pas être nulle.");
        }
        if (inscription.getIdentifier() == null) {
            throw new UpdateInscriptionException("L'identifiant de l'inscription est obligatoire.");
        }
        if (Inscription.STATUS_CONFIRMED.equals(inscription.getStatus())
                && !Boolean.TRUE.equals(inscription.getMedicalCertificate())) {
            throw new UpdateInscriptionException(
                    "Cannot confirm an inscription without medical certificate.");
        }
        dao.updateInscription(inscription);
    }

    public void deleteInscription(int identifier)
            throws DeleteInscriptionException, InscriptionNotFoundException {
        dao.deleteInscription(identifier);
    }

    public List<InscriptionDetail> getAllInscriptions() throws AllInscriptionsException {
        return dao.getAllInscriptions();
    }

    public Inscription getInscriptionById(int identifier)
            throws AllInscriptionsException, InscriptionNotFoundException {
        return dao.getInscriptionById(identifier);
    }

    public List<InscriptionDetail> searchInscriptionsByUnitAndDate(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        if (criteria == null) {
            throw new SearchInscriptionException("Les critères de recherche ne peuvent pas être nuls.");
        }
        return dao.searchInscriptionsByUnitAndDate(criteria);
    }

    public List<InscriptionDetail> searchInscriptionsByRoleAndCity(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        if (criteria == null) {
            throw new SearchInscriptionException("Les critères de recherche ne peuvent pas être nuls.");
        }
        return dao.searchInscriptionsByRoleAndCity(criteria);
    }

    public List<InscriptionDetail> searchPendingInscriptionsByStatus(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        if (criteria == null) {
            throw new SearchInscriptionException("Les critères de recherche ne peuvent pas être nuls.");
        }
        return dao.searchPendingInscriptionsByStatus(criteria);
    }

    public BigDecimal computeTotalRevenueFromConfirmedInscriptions(List<InscriptionDetail> details)
            throws BusinessTaskException {
        if (details == null) {
            throw new BusinessTaskException("La liste des inscriptions ne peut pas être nulle.");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (InscriptionDetail detail : details) {
            if (detail == null) {
                continue;
            }
            if (Inscription.STATUS_CONFIRMED.equals(detail.getStatus())
                    && Boolean.TRUE.equals(detail.getPaymentFinish())
                    && detail.getAmount() != null) {
                total = total.add(detail.getAmount());
            }
        }
        return total;
    }

    public double computeConfirmationRate(List<InscriptionDetail> details)
            throws BusinessTaskException {
        if (details == null) {
            throw new BusinessTaskException("La liste des inscriptions ne peut pas être nulle.");
        }
        if (details.isEmpty()) {
            return 0.0;
        }
        int confirmed = 0;
        int relevant = 0;
        for (InscriptionDetail detail : details) {
            if (detail == null || detail.getStatus() == null) {
                continue;
            }
            if (Inscription.STATUS_CANCELLED.equals(detail.getStatus())) {
                continue;
            }
            relevant++;
            if (Inscription.STATUS_CONFIRMED.equals(detail.getStatus())) {
                confirmed++;
            }
        }
        if (relevant == 0) {
            return 0.0;
        }
        return ((double) confirmed / relevant) * 100.0;
    }

    public int countIncompleteInscriptions(List<InscriptionDetail> details)
            throws BusinessTaskException {
        if (details == null) {
            throw new BusinessTaskException("La liste des inscriptions ne peut pas être nulle.");
        }
        int count = 0;
        for (InscriptionDetail detail : details) {
            if (detail == null) {
                continue;
            }
            if (!Boolean.TRUE.equals(detail.getParentAuthorization())
                    || !Boolean.TRUE.equals(detail.getMedicalCertificate())
                    || !Boolean.TRUE.equals(detail.getPaymentFinish())) {
                count++;
            }
        }
        return count;
    }

    public int countConfirmedInscriptionsInPeriod(Date from, Date to)
            throws SearchInscriptionException, BusinessTaskException {
        if (from == null || to == null) {
            throw new BusinessTaskException("Les deux bornes de la période doivent être renseignées.");
        }
        if (from.after(to)) {
            throw new BusinessTaskException("La date de début doit être antérieure à la date de fin.");
        }
        return dao.countConfirmedInscriptionsInPeriod(from, to);
    }
}
