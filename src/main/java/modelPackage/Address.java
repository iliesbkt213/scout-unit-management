package modelPackage;

public class Address {

    private Integer addressId;
    private Integer cityId;
    private String street;
    private String number;
    private City city;

    public Address() {
    }

    public Address(Integer addressId, Integer cityId, String street, String number) {
        setAddressId(addressId);
        setCityId(cityId);
        setStreet(street);
        setNumber(number);
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (street == null) {
            throw new IllegalArgumentException("La rue est obligatoire.");
        }
        if (street.trim().isEmpty()) {
            throw new IllegalArgumentException("La rue ne peut pas être vide.");
        }
        if (street.length() > 150) {
            throw new IllegalArgumentException("La rue ne peut pas dépasser 150 caractères.");
        }
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (number == null) {
            throw new IllegalArgumentException("Le numéro est obligatoire.");
        }
        if (number.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro ne peut pas être vide.");
        }
        if (number.length() > 10) {
            throw new IllegalArgumentException("Le numéro ne peut pas dépasser 10 caractères.");
        }
        this.number = number;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return street + " " + number + (city != null ? ", " + city.toString() : "");
    }
}
