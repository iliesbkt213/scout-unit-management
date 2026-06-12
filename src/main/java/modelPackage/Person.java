package modelPackage;

import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Person {

    private Integer personId;
    private Integer addressId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthDate;
    private String email;
    private String phone;
    private Address address;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+0-9 .()-]{6,30}$");

    public Person() {
    }

    public Person(Integer personId, Integer addressId, String firstName, String lastName,
                  String gender, Date birthDate, String email, String phone) {
        setPersonId(personId);
        setAddressId(addressId);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setBirthDate(birthDate);
        setEmail(email);
        setPhone(phone);
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        if (personId != null && personId <= 0) {
            throw new IllegalArgumentException("L'identifiant de la personne doit être positif.");
        }
        this.personId = personId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        if (addressId == null) {
            throw new IllegalArgumentException("L'adresse est obligatoire.");
        }
        if (addressId <= 0) {
            throw new IllegalArgumentException("L'adresse doit être positive.");
        }
        this.addressId = addressId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        }
        if (firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        if (firstName.length() > 100) {
            throw new IllegalArgumentException("Le prénom ne peut pas dépasser 100 caractères.");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }
        if (lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        if (lastName.length() > 100) {
            throw new IllegalArgumentException("Le nom ne peut pas dépasser 100 caractères.");
        }
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Le genre est obligatoire.");
        }
        if (!gender.equals("M") && !gender.equals("F") && !gender.equals("X")) {
            throw new IllegalArgumentException("Le genre doit être 'M', 'F' ou 'X'.");
        }
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La date de naissance est obligatoire.");
        }
        Calendar today = Calendar.getInstance();
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);
        if (birth.after(today)) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur.");
        }
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide.");
        }
        if (email.length() > 150) {
            throw new IllegalArgumentException("L'email ne peut pas dépasser 150 caractères.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Le format de l'email n'est pas valide.");
        }
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Le téléphone est obligatoire.");
        }
        if (phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Le téléphone ne peut pas être vide.");
        }
        if (phone.length() > 30) {
            throw new IllegalArgumentException("Le téléphone ne peut pas dépasser 30 caractères.");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Le format du téléphone n'est pas valide.");
        }
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
