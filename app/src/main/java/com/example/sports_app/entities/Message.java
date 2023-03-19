package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    private long mId;

    @SerializedName("message")
    private String message;

    @SerializedName("username")
    private String username;

    @SerializedName("user")
    private long userId;

    @SerializedName("x")
    private boolean read;

    @SerializedName("threadCreator")
    private String threadCreator;

    public String getThreadCreator() {
        return threadCreator;
    }

    public void setThreadCreator(String threadCreator) {
        this.threadCreator = threadCreator;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Message() {
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Message(long mId, String message, String username, long userId) {
        this.mId = mId;
        this.message = message;
        this.username = username;
        this.userId = userId;
        this.read = false;
    }
}
