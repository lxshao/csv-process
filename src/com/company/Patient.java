package com.company;

public class Patient {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer version;
    private String insuranceName;

    public Patient(Integer id, String fName, String lName, Integer version, String insuranceName) {
        this.id = id;
        this.firstName = fName;
        this.lastName = lName;
        this.version = version;
        this.insuranceName = insuranceName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getInsuranceName() {
        return insuranceName;
    }
}
