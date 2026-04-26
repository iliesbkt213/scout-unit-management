package modelPackage;

public class UnitScout {

    private Integer identifier;
    private Integer responsibleId;
    private String unitName;
    private PersonRole responsible;

    public UnitScout() {
    }

    public UnitScout(Integer responsibleId, String unitName) {
        setResponsibleId(responsibleId);
        setUnitName(unitName);
    }

    public UnitScout(Integer identifier, Integer responsibleId, String unitName) {
        setIdentifier(identifier);
        setResponsibleId(responsibleId);
        setUnitName(unitName);
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

    public Integer getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Integer responsibleId) {
        if (responsibleId == null) {
            throw new IllegalArgumentException("Le responsable est obligatoire.");
        }
        if (responsibleId <= 0) {
            throw new IllegalArgumentException("Le responsable doit être positif.");
        }
        this.responsibleId = responsibleId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        if (unitName == null) {
            throw new IllegalArgumentException("Le nom de l'unité est obligatoire.");
        }
        if (unitName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'unité ne peut pas être vide.");
        }
        if (unitName.length() > 100) {
            throw new IllegalArgumentException("Le nom de l'unité ne peut pas dépasser 100 caractères.");
        }
        this.unitName = unitName;
    }

    public PersonRole getResponsible() {
        return responsible;
    }

    public void setResponsible(PersonRole responsible) {
        this.responsible = responsible;
    }

    @Override
    public String toString() {
        return unitName;
    }
}
