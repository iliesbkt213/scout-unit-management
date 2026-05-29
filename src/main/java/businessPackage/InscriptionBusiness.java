package businessPackage;

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

public interface InscriptionBusiness {

    int addInscription(Inscription inscription) throws AddInscriptionException;

    void updateInscription(Inscription inscription)
            throws UpdateInscriptionException, InscriptionNotFoundException;

    void deleteInscription(int identifier)
            throws DeleteInscriptionException, InscriptionNotFoundException;

    List<InscriptionDetail> getAllInscriptions() throws AllInscriptionsException;

    Inscription getInscriptionById(int identifier)
            throws AllInscriptionsException, InscriptionNotFoundException;

    List<InscriptionDetail> searchInscriptionsByUnitAndDate(InscriptionSearchCriteria criteria)
            throws SearchInscriptionException;

    List<InscriptionDetail> searchInscriptionsByRoleAndCity(InscriptionSearchCriteria criteria)
            throws SearchInscriptionException;

    List<InscriptionDetail> searchPendingInscriptionsByStatus(InscriptionSearchCriteria criteria)
            throws SearchInscriptionException;

    BigDecimal computeTotalRevenueFromConfirmedInscriptions(List<InscriptionDetail> details)
            throws BusinessTaskException;

    double computeConfirmationRate(List<InscriptionDetail> details)
            throws BusinessTaskException;

    int countIncompleteInscriptions(List<InscriptionDetail> details)
            throws BusinessTaskException;

    int countConfirmedInscriptionsInPeriod(Date from, Date to)
            throws SearchInscriptionException, BusinessTaskException;
}
