package modelPackage;

import java.sql.Date;

public class Inscription {

    private Integer identifier;
    private Integer legalGuardianId;
    private Integer personRoleId;
    private Integer unitScoutId;
    private Integer invitationToPayId;
    private String status;
    private Boolean parentAuthorization;
    private Date registrationDate;
    private Date preRegistrationDate;
    private Date confirmationDate;
    private Boolean medicalCertificate;
    private Boolean paymentFinish;

    private Person legalGuardian;
    private PersonRole personRole;
    private UnitScout unitScout;
    private InvitationToPay invitationToPay;

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    public Inscription() {
    }

    public Inscription(Integer personRoleId, Integer unitScoutId, Integer invitationToPayId,
                       String status, Boolean parentAuthorization, Date registrationDate,
                       Boolean medicalCertificate, Boolean paymentFinish,
                       Integer legalGuardianId) {
        setPersonRoleId(personRoleId);
        setUnitScoutId(unitScoutId);
        setInvitationToPayId(invitationToPayId);
        setStatus(status);
        setParentAuthorization(parentAuthorization);
        setRegistrationDate(registrationDate);
        setMedicalCertificate(medicalCertificate);
        setPaymentFinish(paymentFinish);
        setLegalGuardianId(legalGuardianId);
    }

    public Inscription(Integer identifier, Integer personRoleId, Integer unitScoutId,
                       Integer invitationToPayId, String status, Boolean parentAuthorization,
                       Date registrationDate, Boolean medicalCertificate, Boolean paymentFinish,
                       Integer legalGuardianId) {
        setIdentifier(identifier);
        setPersonRoleId(personRoleId);
        setUnitScoutId(unitScoutId);
        setInvitationToPayId(invitationToPayId);
        setStatus(status);
        setParentAuthorization(parentAuthorization);
        setRegistrationDate(registrationDate);
        setMedicalCertificate(medicalCertificate);
        setPaymentFinish(paymentFinish);
        setLegalGuardianId(legalGuardianId);
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        if (identifier != null && identifier <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif.");
        }
        this.identifier = identifier;
    }

    public Integer getLegalGuardianId() {
        return legalGuardianId;
    }

    public void setLegalGuardianId(Integer legalGuardianId) {
        if (legalGuardianId != null && legalGuardianId <= 0) {
            throw new IllegalArgumentException("Le tuteur légal doit être positif.");
        }
        this.legalGuardianId = legalGuardianId;
    }

    public Integer getPersonRoleId() {
        return personRoleId;
    }

    public void setPersonRoleId(Integer personRoleId) {
        if (personRoleId == null) {
            throw new IllegalArgumentException("Le rôle de la personne est obligatoire.");
        }
        if (personRoleId <= 0) {
            throw new IllegalArgumentException("Le rôle de la personne doit être positif.");
        }
        this.personRoleId = personRoleId;
    }

    public Integer getUnitScoutId() {
        return unitScoutId;
    }

    public void setUnitScoutId(Integer unitScoutId) {
        if (unitScoutId == null) {
            throw new IllegalArgumentException("L'unité scoute est obligatoire.");
        }
        if (unitScoutId <= 0) {
            throw new IllegalArgumentException("L'unité scoute doit être positive.");
        }
        this.unitScoutId = unitScoutId;
    }

    public Integer getInvitationToPayId() {
        return invitationToPayId;
    }

    public void setInvitationToPayId(Integer invitationToPayId) {
        if (invitationToPayId == null) {
            throw new IllegalArgumentException("L'invitation à payer est obligatoire.");
        }
        if (invitationToPayId <= 0) {
            throw new IllegalArgumentException("L'invitation à payer doit être positive.");
        }
        this.invitationToPayId = invitationToPayId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Le statut est obligatoire.");
        }
        if (status.trim().isEmpty()) {
            throw new IllegalArgumentException("Le statut ne peut pas être vide.");
        }
        if (status.length() > 50) {
            throw new IllegalArgumentException("Le statut ne peut pas dépasser 50 caractères.");
        }
        if (!status.equals(STATUS_PENDING) && !status.equals(STATUS_CONFIRMED)
                && !status.equals(STATUS_REJECTED) && !status.equals(STATUS_CANCELLED)) {
            throw new IllegalArgumentException(
                    "Le statut doit être PENDING, CONFIRMED, REJECTED ou CANCELLED.");
        }
        this.status = status;
    }

    public Boolean getParentAuthorization() {
        return parentAuthorization;
    }

    public void setParentAuthorization(Boolean parentAuthorization) {
        if (parentAuthorization == null) {
            throw new IllegalArgumentException("L'autorisation parentale est obligatoire.");
        }
        this.parentAuthorization = parentAuthorization;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        if (registrationDate == null) {
            throw new IllegalArgumentException("La date d'inscription est obligatoire.");
        }
        this.registrationDate = registrationDate;
    }

    public Date getPreRegistrationDate() {
        return preRegistrationDate;
    }

    public void setPreRegistrationDate(Date preRegistrationDate) {
        if (preRegistrationDate != null && registrationDate != null
                && preRegistrationDate.after(registrationDate)) {
            throw new IllegalArgumentException(
                    "La date de pré-inscription doit être antérieure ou égale à la date d'inscription.");
        }
        this.preRegistrationDate = preRegistrationDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        if (confirmationDate != null && registrationDate != null
                && confirmationDate.before(registrationDate)) {
            throw new IllegalArgumentException(
                    "La date de confirmation doit être postérieure ou égale à la date d'inscription.");
        }
        this.confirmationDate = confirmationDate;
    }

    public Boolean getMedicalCertificate() {
        return medicalCertificate;
    }

    public void setMedicalCertificate(Boolean medicalCertificate) {
        if (medicalCertificate == null) {
            throw new IllegalArgumentException("Le certificat médical est obligatoire.");
        }
        this.medicalCertificate = medicalCertificate;
    }

    public Boolean getPaymentFinish() {
        return paymentFinish;
    }

    public void setPaymentFinish(Boolean paymentFinish) {
        if (paymentFinish == null) {
            throw new IllegalArgumentException("L'indicateur de paiement est obligatoire.");
        }
        this.paymentFinish = paymentFinish;
    }

    public Person getLegalGuardian() {
        return legalGuardian;
    }

    public void setLegalGuardian(Person legalGuardian) {
        this.legalGuardian = legalGuardian;
    }

    public PersonRole getPersonRole() {
        return personRole;
    }

    public void setPersonRole(PersonRole personRole) {
        this.personRole = personRole;
    }

    public UnitScout getUnitScout() {
        return unitScout;
    }

    public void setUnitScout(UnitScout unitScout) {
        this.unitScout = unitScout;
    }

    public InvitationToPay getInvitationToPay() {
        return invitationToPay;
    }

    public void setInvitationToPay(InvitationToPay invitationToPay) {
        this.invitationToPay = invitationToPay;
    }

    public boolean isComplete() {
        return Boolean.TRUE.equals(parentAuthorization)
                && Boolean.TRUE.equals(medicalCertificate)
                && Boolean.TRUE.equals(paymentFinish);
    }

    @Override
    public String toString() {
        return "Inscription #" + identifier + " - " + status;
    }
}
