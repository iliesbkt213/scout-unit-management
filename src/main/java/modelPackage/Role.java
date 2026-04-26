package modelPackage;

public class Role {

    private String label;

    public Role() {
    }

    public Role(String label) {
        setLabel(label);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Le libellé est obligatoire.");
        }
        if (label.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé ne peut pas être vide.");
        }
        if (label.length() > 50) {
            throw new IllegalArgumentException("Le libellé ne peut pas dépasser 50 caractères.");
        }
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
