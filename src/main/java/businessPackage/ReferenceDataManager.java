package businessPackage;

import dataAccessPackage.ReferenceDBAccess;
import dataAccessPackage.ReferenceDataAccess;
import exceptionPackage.LoadReferenceDataException;
import modelPackage.InvitationToPay;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.Role;
import modelPackage.UnitScout;

import java.util.List;

public class ReferenceDataManager {

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
}
