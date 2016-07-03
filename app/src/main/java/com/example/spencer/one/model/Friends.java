package com.example.spencer.one.model;

import com.backendless.BackendlessUser;

import java.util.ArrayList;

/**
 * Created by spencerevans on 7/3/16.
 */
public class Friends {

    private String email;
    private String userName;
    private String objectId;
    private String name;
    private ArrayList<BackendlessUser> friendList;

    public Friends() {
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

    public ArrayList<BackendlessUser> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<BackendlessUser> friendList) {
        this.friendList = friendList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
