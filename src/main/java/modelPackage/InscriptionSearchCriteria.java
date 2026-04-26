package modelPackage;

import java.sql.Date;

public class InscriptionSearchCriteria {

    private String unitName;
    private String roleLabel;
    private Date registrationDateFrom;
    private Date registrationDateTo;
    private String status;
    private String cityName;

    public InscriptionSearchCriteria() {
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public void setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
    }

    public Date getRegistrationDateFrom() {
        return registrationDateFrom;
    }

    public void setRegistrationDateFrom(Date registrationDateFrom) {
        if (registrationDateFrom != null && registrationDateTo != null
                && registrationDateFrom.after(registrationDateTo)) {
            throw new IllegalArgumentException(
                    "La date de début doit être antérieure à la date de fin.");
        }
        this.registrationDateFrom = registrationDateFrom;
    }

    public Date getRegistrationDateTo() {
        return registrationDateTo;
    }

    public void setRegistrationDateTo(Date registrationDateTo) {
        if (registrationDateTo != null && registrationDateFrom != null
                && registrationDateTo.before(registrationDateFrom)) {
            throw new IllegalArgumentException(
                    "La date de fin doit être postérieure à la date de début.");
        }
        this.registrationDateTo = registrationDateTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
