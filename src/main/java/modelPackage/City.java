package modelPackage;

public class City {

    private Integer cityId;
    private String cityName;
    private String postalCode;

    public City() {
    }

    public City(Integer cityId, String cityName, String postalCode) {
        setCityId(cityId);
        setCityName(cityName);
        setPostalCode(postalCode);
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        if (cityId == null) {
            throw new IllegalArgumentException("La ville est obligatoire.");
        }
        if (cityId <= 0) {
            throw new IllegalArgumentException("La ville doit être positive.");
        }
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        if (cityName == null) {
            throw new IllegalArgumentException("Le nom de la ville est obligatoire.");
        }
        if (cityName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la ville ne peut pas être vide.");
        }
        if (cityName.length() > 100) {
            throw new IllegalArgumentException("Le nom de la ville ne peut pas dépasser 100 caractères.");
        }
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if (postalCode == null) {
            throw new IllegalArgumentException("Le code postal est obligatoire.");
        }
        if (postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Le code postal ne peut pas être vide.");
        }
        if (postalCode.length() > 20) {
            throw new IllegalArgumentException("Le code postal ne peut pas dépasser 20 caractères.");
        }
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return postalCode + " " + cityName;
    }
}
