package com.example.spencer.one.model;

import com.backendless.BackendlessUser;

/**
 * Created by spencerevans on 7/2/16.
 */
public class Users extends BackendlessUser {

    private String email;
    private String userName;
    private String objectId;
    private String name;
    private String Snapchat;
    private String phoneNumber;
    private String fbid;

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getSnapchat() {
        return  Snapchat;
    }

    public void setSnapchat(String snapchat) {
        this. Snapchat = snapchat;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Users() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}