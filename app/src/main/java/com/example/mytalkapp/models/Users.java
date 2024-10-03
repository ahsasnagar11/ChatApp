package com.example.mytalkapp.models;

public class Users {
    String profilepic, username, mail, userId, lastmessage, password;

    public Users(String profilepic, String username, String mail, String userId, String lastmessage, String password) {
        this.profilepic = profilepic;
        this.username = username;
        this.mail = mail;
        this.userId = userId;
        this.lastmessage = lastmessage;
        this.password = password;
    }
    public Users(){}
    public Users(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
