package modelPackage;

import java.math.BigDecimal;
import java.sql.Date;

public class InscriptionDetail {

    private Integer inscriptionId;
    private String status;
    private Date registrationDate;
    private Date preRegistrationDate;
    private Date confirmationDate;
    private Boolean parentAuthorization;
    private Boolean medicalCertificate;
    private Boolean paymentFinish;

    private String personFirstName;
    private String personLastName;
    private String personEmail;
    private String roleLabel;
    private String unitName;
    private String cityName;
    private String postalCode;
    private BigDecimal amount;
    private String legalGuardianName;

    public InscriptionDetail() {
    }

    public Integer getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Integer inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getPreRegistrationDate() {
        return preRegistrationDate;
    }

    public void setPreRegistrationDate(Date preRegistrationDate) {
        this.preRegistrationDate = preRegistrationDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Boolean getParentAuthorization() {
        return parentAuthorization;
    }

    public void setParentAuthorization(Boolean parentAuthorization) {
        this.parentAuthorization = parentAuthorization;
    }

    public Boolean getMedicalCertificate() {
        return medicalCertificate;
    }

    public void setMedicalCertificate(Boolean medicalCertificate) {
        this.medicalCertificate = medicalCertificate;
    }

    public Boolean getPaymentFinish() {
        return paymentFinish;
    }

    public void setPaymentFinish(Boolean paymentFinish) {
        this.paymentFinish = paymentFinish;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public void setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLegalGuardianName() {
        return legalGuardianName;
    }

    public void setLegalGuardianName(String legalGuardianName) {
        this.legalGuardianName = legalGuardianName;
    }

    public String getFullName() {
        return personFirstName + " " + personLastName;
    }
}
