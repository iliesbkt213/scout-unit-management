package modelPackage;

import java.sql.Date;

public class PersonRole {

    private Integer identifier;
    private Integer personId;
    private String roleLabel;
    private Date startDate;
    private Date endDate;
    private Person person;
    private Role role;

    public PersonRole() {
    }

    public PersonRole(Integer personId, String roleLabel, Date startDate, Date endDate) {
        setPersonId(personId);
        setRoleLabel(roleLabel);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public PersonRole(Integer identifier, Integer personId, String roleLabel, Date startDate, Date endDate) {
        setIdentifier(identifier);
        setPersonId(personId);
        setRoleLabel(roleLabel);
        setStartDate(startDate);
        setEndDate(endDate);
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

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        if (personId == null) {
            throw new IllegalArgumentException("L'identifiant de la personne est obligatoire.");
        }
        if (personId <= 0) {
            throw new IllegalArgumentException("L'identifiant de la personne doit être positif.");
        }
        this.personId = personId;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public void setRoleLabel(String roleLabel) {
        if (roleLabel == null) {
            throw new IllegalArgumentException("Le rôle est obligatoire.");
        }
        if (roleLabel.trim().isEmpty()) {
            throw new IllegalArgumentException("Le rôle ne peut pas être vide.");
        }
        if (roleLabel.length() > 50) {
            throw new IllegalArgumentException("Le rôle ne peut pas dépasser 50 caractères.");
        }
        this.roleLabel = roleLabel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("La date de début est obligatoire.");
        }
        if (endDate != null && startDate.after(endDate)) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin.");
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate != null && startDate != null && endDate.before(startDate)) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
        }
        this.endDate = endDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        String base = roleLabel;
        if (person != null) {
            base = person.toString() + " - " + base;
        }
        return base;
    }
}
