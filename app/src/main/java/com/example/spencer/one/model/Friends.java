package com.example.spencer.one.model;

import com.backendless.BackendlessUser;

import java.util.ArrayList;

/**
 * Created by spencerevans on 7/3/16.
 */
public class Friends {

    private String currentUserId;
    private String friendId;
    private String userName;
    private String actualName;

    public Friends() {
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActualName() {
        return actualName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }
}
