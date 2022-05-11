package models;

public class Address {


    private String fullName;
    private String companyName;
    private String addressPart;
    private String addressComplement;
    private String postalCode;
    private String city;
    private String country;
    private String phoneNumber;

    public Address(String fullName, String companyName, String addressPart, String addressComplement, String postalCode, String city, String country, String phoneNumber) {
        this.fullName = fullName;
        this.companyName = companyName;
        this.addressPart = addressPart;
        this.addressComplement = addressComplement;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return fullName;
    }


    public String getCompanyName() {
        return companyName;
    }

    public String getAddressPart() {
        return addressPart;
    }

    public String getAddressComplement() {
        return addressComplement;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
