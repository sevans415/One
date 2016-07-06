package com.example.spencer.one.model;

/**
 * Created by spencerevans on 7/6/16.
 */
public class Messages {
    private String convo;
    private String usersConcatIds;
    private String user1Name;
    private String user2Name;
    private String objectId;

    public Messages(String usersConcatIds, String user1Name, String user2Name) {
        this.usersConcatIds = usersConcatIds;
        this.user1Name = user1Name;
        this.user2Name = user2Name;
    }
    public Messages(){

    }

    public String getConvo() {
        return convo;
    }

    public void setConvo(String convo) {
        this.convo = convo;
    }

    public String getUsersConcatIds() {
        return usersConcatIds;
    }

    public void setUsersConcatIds(String usersConcatIds) {
        this.usersConcatIds = usersConcatIds;
    }

    public String getUser1Name() {
        return user1Name;
    }

    public void setUser1Name(String user1Name) {
        this.user1Name = user1Name;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public void setUser2Name(String user2Name) {
        this.user2Name = user2Name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
