package dataAccessPackage;

import exceptionPackage.LoadReferenceDataException;
import modelPackage.InvitationToPay;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.Role;
import modelPackage.UnitScout;

import java.util.List;

public interface ReferenceDataAccess {

    List<Person> getAllPersons() throws LoadReferenceDataException;

    List<PersonRole> getAllPersonRoles() throws LoadReferenceDataException;

    List<UnitScout> getAllUnitScouts() throws LoadReferenceDataException;

    List<InvitationToPay> getAllInvitationsToPay() throws LoadReferenceDataException;

    List<Role> getAllRoles() throws LoadReferenceDataException;

    int addInvitationToPay(InvitationToPay invitation) throws LoadReferenceDataException;
}
