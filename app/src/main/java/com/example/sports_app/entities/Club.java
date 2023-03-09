package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

public class Club {

    @SerializedName("id")
    private long mId;
    @SerializedName("clubName")
    private String mClubName;
    @SerializedName("clubUrl")
    private String mClubUrl;
    @SerializedName("clubEmail")
    private String mClubEmail;
    @SerializedName("clubLocation")
    private String mClubLocation;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("sport")
    private String mSport;

    public Club(long mId, String mClubName, String mClubUrl, String mClubEmail, String mClubLocation, String mDescription, String mSport) {
        this.mId = mId;
        this.mClubName = mClubName;
        this.mClubUrl = mClubUrl;
        this.mClubEmail = mClubEmail;
        this.mClubLocation = mClubLocation;
        this.mDescription = mDescription;
        this.mSport = mSport;
    }

    public Club() {

    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmClubName() {
        return mClubName;
    }

    public void setmClubName(String mClubName) {
        this.mClubName = mClubName;
    }

    public String getmClubUrl() {
        return mClubUrl;
    }

    public void setmClubUrl(String mClubUrl) {
        this.mClubUrl = mClubUrl;
    }

    public String getmClubEmail() {
        return mClubEmail;
    }

    public void setmClubEmail(String mClubEmail) {
        this.mClubEmail = mClubEmail;
    }

    public String getmClubLocation() {
        return mClubLocation;
    }

    public void setmClubLocation(String mClubLocation) {
        this.mClubLocation = mClubLocation;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmSport() {
        return mSport;
    }

    public void setmSport(String mSport) {
        this.mSport = mSport;
    }
}
