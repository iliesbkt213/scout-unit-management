package businessPackage;

import exceptionPackage.LoadReferenceDataException;
import modelPackage.Address;
import modelPackage.City;
import modelPackage.InvitationToPay;
import modelPackage.Person;
import modelPackage.PersonRole;
import modelPackage.Role;
import modelPackage.UnitScout;

import java.util.List;

public interface ReferenceDataBusiness {

    List<Person> getAllPersons() throws LoadReferenceDataException;

    List<PersonRole> getAllPersonRoles() throws LoadReferenceDataException;

    List<UnitScout> getAllUnitScouts() throws LoadReferenceDataException;

    List<InvitationToPay> getAllInvitationsToPay() throws LoadReferenceDataException;

    List<Role> getAllRoles() throws LoadReferenceDataException;

    int addInvitationToPay(InvitationToPay invitation) throws LoadReferenceDataException;

    int addPerson(Person person) throws LoadReferenceDataException;

    int addPersonRole(PersonRole personRole) throws LoadReferenceDataException;

    List<Address> getAllAddresses() throws LoadReferenceDataException;

    int addAddress(Address address) throws LoadReferenceDataException;

    List<City> getAllCities() throws LoadReferenceDataException;
}
