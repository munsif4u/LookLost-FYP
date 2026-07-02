package com.example.looklost;

public class UserModel {
    private String userId;
    private String userName,userEmail;
    private String userProfile;
    private String userNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public UserModel(String userId, String userName, String userEmail,String userNumber, String userProfile) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userNumber = userNumber;
        this.userProfile = userProfile;
    }

    public UserModel() {

    }

}
