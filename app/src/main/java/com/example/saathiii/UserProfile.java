package com.example.app.saathiii;

public class UserProfile

{
    public String userAge;
    public String userName;
    public String userEmail;
    public  String imageURL;

    public UserProfile(){


    }

    public UserProfile(String userAge, String userName, String userEmail, String imageURL) {
        this.userAge = userAge;
        this.userName = userName;
        this.userEmail = userEmail;
        this.imageURL = imageURL;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
