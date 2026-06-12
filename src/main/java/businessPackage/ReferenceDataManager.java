package businessPackage;

import dataAccessPackage.ReferenceDBAccess;
import dataAccessPackage.ReferenceDataAccess;
import exceptionPackage.LoadReferenceDataException;
import modelPackage.Address;
import modelPackage.City;
import modelPackage.InvitationToPay;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.Role;
import modelPackage.UnitScout;

import java.util.List;

public class ReferenceDataManager implements ReferenceDataBusiness {

    private ReferenceDataAccess dao;

    public ReferenceDataManager() {
        setDao(new ReferenceDBAccess());
    }

    public ReferenceDataManager(ReferenceDataAccess dao) {
        setDao(dao);
    }

    public ReferenceDataAccess getDao() {
        return dao;
    }

    public void setDao(ReferenceDataAccess dao) {
        if (dao == null) {
            throw new IllegalArgumentException("Le DAO ne peut pas être nul.");
        }
        this.dao = dao;
    }

    public List<Person> getAllPersons() throws LoadReferenceDataException {
        return dao.getAllPersons();
    }

    public List<PersonRole> getAllPersonRoles() throws LoadReferenceDataException {
        return dao.getAllPersonRoles();
    }

    public List<UnitScout> getAllUnitScouts() throws LoadReferenceDataException {
        return dao.getAllUnitScouts();
    }

    public List<InvitationToPay> getAllInvitationsToPay() throws LoadReferenceDataException {
        return dao.getAllInvitationsToPay();
    }

    public List<Role> getAllRoles() throws LoadReferenceDataException {
        return dao.getAllRoles();
    }

    public int addInvitationToPay(InvitationToPay invitation) throws LoadReferenceDataException {
        if (invitation == null) {
            throw new LoadReferenceDataException("L'invitation ne peut pas être nulle.");
        }
        return dao.addInvitationToPay(invitation);
    }

    public int addPerson(Person person) throws LoadReferenceDataException {
        if (person == null) {
            throw new LoadReferenceDataException("La personne ne peut pas être nulle.");
        }
        return dao.addPerson(person);
    }

    public int addPersonRole(PersonRole personRole) throws LoadReferenceDataException {
        if (personRole == null) {
            throw new LoadReferenceDataException("Le rôle de la personne ne peut pas être nul.");
        }
        return dao.addPersonRole(personRole);
    }

    public List<Address> getAllAddresses() throws LoadReferenceDataException {
        return dao.getAllAddresses();
    }

    public int addAddress(Address address) throws LoadReferenceDataException {
        if (address == null) {
            throw new LoadReferenceDataException("L'adresse ne peut pas être nulle.");
        }
        return dao.addAddress(address);
    }

    public List<City> getAllCities() throws LoadReferenceDataException {
        return dao.getAllCities();
    }
}
