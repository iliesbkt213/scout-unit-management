package dataAccessPackage;

import exceptionPackage.AddInscriptionException;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.DeleteInscriptionException;
import exceptionPackage.InscriptionNotFoundException;
import exceptionPackage.SearchInscriptionException;
import exceptionPackage.UpdateInscriptionException;
import modelPackage.Inscription;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;

import java.util.List;

public interface InscriptionDataAccess {

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

    int countConfirmedInscriptionsInPeriod(java.sql.Date from, java.sql.Date to)
            throws SearchInscriptionException;
}
