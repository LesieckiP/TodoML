package com.soldiersofmobile.todoexpert.api;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("class")
    private String objectId;
    private String sessionToken;
    private String username;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(final String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
