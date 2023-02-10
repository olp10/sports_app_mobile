package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Thread {
    @SerializedName("id")
    private long mId;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("isPinned")
    private boolean mIsPinned = false;
    @SerializedName("header")
    private String mHeader;
    @SerializedName("body")
    private String mBody;
    @SerializedName("sport")
    private String mSport;

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public boolean ismIsPinned() {
        return mIsPinned;
    }

    public void setmIsPinned(boolean mIsPinned) {
        this.mIsPinned = mIsPinned;
    }

    public String getmHeader() {
        return mHeader;
    }

    public void setmHeader(String mHeader) {
        this.mHeader = mHeader;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    public String getmSport() {
        return mSport;
    }

    public void setmSport(String mSport) {
        this.mSport = mSport;
    }



    public Thread() {

    }

    public Thread(long ID, String username, boolean isPinned, String header, String body, String sport) {
        this.mId = ID;
        this.mUsername = username;
        this.mIsPinned = isPinned;
        this.mHeader = header;
        this.mBody = body;
        this.mSport = sport;
    }
}
