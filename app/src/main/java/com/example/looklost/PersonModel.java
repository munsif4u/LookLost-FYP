package com.example.looklost;

public class PersonModel {
    private String personId, userId;
    private String personName;
    private String personCategory;
    private String personLocation;
    private String personDescription;
    private String personImage;

    public PersonModel() {
    }

    public PersonModel(String personId, String userId, String personName, String personCategory, String personLocation, String personDescription, String personImage) {
        this.personId = personId;
        this.userId = userId;
        this.personName = personName;
        this.personCategory = personCategory;
        this.personLocation = personLocation;
        this.personDescription = personDescription;
        this.personImage = personImage;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonCategory() {
        return personCategory;
    }

    public void setPersonCategory(String personCategory) {
        this.personCategory = personCategory;
    }

    public String getPersonLocation() {
        return personLocation;
    }

    public void setPersonLocation(String personLocation) {
        this.personLocation = personLocation;
    }

    public String getPersonDescription() {
        return personDescription;
    }

    public void setPersonDescription(String personDescription) {
        this.personDescription = personDescription;
    }

    public String getPersonImage() {
        return personImage;
    }

    public void setPersonImage(String personImage) {
        this.personImage = personImage;
    }
}

