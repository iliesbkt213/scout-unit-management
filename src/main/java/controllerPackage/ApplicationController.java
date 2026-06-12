package controllerPackage;

import businessPackage.InscriptionBusiness;
import businessPackage.InscriptionManager;
import businessPackage.ReferenceDataBusiness;
import businessPackage.ReferenceDataManager;
import exceptionPackage.AddInscriptionException;
import exceptionPackage.AllInscriptionsException;
import exceptionPackage.BusinessTaskException;
import exceptionPackage.DeleteInscriptionException;
import exceptionPackage.InscriptionNotFoundException;
import exceptionPackage.LoadReferenceDataException;
import exceptionPackage.SearchInscriptionException;
import exceptionPackage.UpdateInscriptionException;
import modelPackage.Address;
import modelPackage.City;
import modelPackage.Inscription;
import modelPackage.InscriptionDetail;
import modelPackage.InscriptionSearchCriteria;
import modelPackage.InvitationToPay;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.Role;
import modelPackage.UnitScout;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class ApplicationController {

    private InscriptionBusiness inscriptionManager;
    private ReferenceDataBusiness referenceDataManager;

    public ApplicationController() {
        setInscriptionManager(new InscriptionManager());
        setReferenceDataManager(new ReferenceDataManager());
    }

    public InscriptionBusiness getInscriptionManager() {
        return inscriptionManager;
    }

    public void setInscriptionManager(InscriptionBusiness inscriptionManager) {
        if (inscriptionManager == null) {
            throw new IllegalArgumentException("Le gestionnaire d'inscriptions ne peut pas être nul.");
        }
        this.inscriptionManager = inscriptionManager;
    }

    public ReferenceDataBusiness getReferenceDataManager() {
        return referenceDataManager;
    }

    public void setReferenceDataManager(ReferenceDataBusiness referenceDataManager) {
        if (referenceDataManager == null) {
            throw new IllegalArgumentException("Le gestionnaire de données de référence ne peut pas être nul.");
        }
        this.referenceDataManager = referenceDataManager;
    }

    public int addInscription(Inscription inscription) throws AddInscriptionException {
        return inscriptionManager.addInscription(inscription);
    }

    public void updateInscription(Inscription inscription)
            throws UpdateInscriptionException, InscriptionNotFoundException {
        inscriptionManager.updateInscription(inscription);
    }

    public void deleteInscription(int identifier)
            throws DeleteInscriptionException, InscriptionNotFoundException {
        inscriptionManager.deleteInscription(identifier);
    }

    public List<InscriptionDetail> getAllInscriptions() throws AllInscriptionsException {
        return inscriptionManager.getAllInscriptions();
    }

    public Inscription getInscriptionById(int identifier)
            throws AllInscriptionsException, InscriptionNotFoundException {
        return inscriptionManager.getInscriptionById(identifier);
    }

    public List<InscriptionDetail> searchInscriptionsByUnitAndDate(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        return inscriptionManager.searchInscriptionsByUnitAndDate(criteria);
    }

    public List<InscriptionDetail> searchInscriptionsByRoleAndCity(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        return inscriptionManager.searchInscriptionsByRoleAndCity(criteria);
    }

    public List<InscriptionDetail> searchPendingInscriptionsByStatus(
            InscriptionSearchCriteria criteria) throws SearchInscriptionException {
        return inscriptionManager.searchPendingInscriptionsByStatus(criteria);
    }

    public BigDecimal computeTotalRevenueFromConfirmedInscriptions(List<InscriptionDetail> details)
            throws BusinessTaskException {
        return inscriptionManager.computeTotalRevenueFromConfirmedInscriptions(details);
    }

    public double computeConfirmationRate(List<InscriptionDetail> details)
            throws BusinessTaskException {
        return inscriptionManager.computeConfirmationRate(details);
    }

    public int countIncompleteInscriptions(List<InscriptionDetail> details)
            throws BusinessTaskException {
        return inscriptionManager.countIncompleteInscriptions(details);
    }

    public int countConfirmedInscriptionsInPeriod(Date from, Date to)
            throws SearchInscriptionException, BusinessTaskException {
        return inscriptionManager.countConfirmedInscriptionsInPeriod(from, to);
    }

    public List<Person> getAllPersons() throws LoadReferenceDataException {
        return referenceDataManager.getAllPersons();
    }

    public List<PersonRole> getAllPersonRoles() throws LoadReferenceDataException {
        return referenceDataManager.getAllPersonRoles();
    }

    public List<UnitScout> getAllUnitScouts() throws LoadReferenceDataException {
        return referenceDataManager.getAllUnitScouts();
    }

    public List<InvitationToPay> getAllInvitationsToPay() throws LoadReferenceDataException {
        return referenceDataManager.getAllInvitationsToPay();
    }

    public List<Role> getAllRoles() throws LoadReferenceDataException {
        return referenceDataManager.getAllRoles();
    }

    public int addInvitationToPay(InvitationToPay invitation) throws LoadReferenceDataException {
        return referenceDataManager.addInvitationToPay(invitation);
    }

    public int addPerson(Person person) throws LoadReferenceDataException {
        return referenceDataManager.addPerson(person);
    }

    public int addPersonRole(PersonRole personRole) throws LoadReferenceDataException {
        return referenceDataManager.addPersonRole(personRole);
    }

    public List<Address> getAllAddresses() throws LoadReferenceDataException {
        return referenceDataManager.getAllAddresses();
    }

    public int addAddress(Address address) throws LoadReferenceDataException {
        return referenceDataManager.addAddress(address);
    }

    public List<City> getAllCities() throws LoadReferenceDataException {
        return referenceDataManager.getAllCities();
    }
}
