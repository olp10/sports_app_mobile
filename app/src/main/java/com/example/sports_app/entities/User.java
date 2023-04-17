package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("id")
    private Long mId;
    @SerializedName("is_moderator")
    private boolean isModerator;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("userPassword")
    private String mUserPassword;
    @SerializedName("isAdmin")
    private boolean mIsAdmin;
    private List<Comment> mComments;
    @SerializedName("loggedIn")
    private boolean isLoggedIn;
    @SerializedName("isBanned")
    private boolean isBanned;
    @SerializedName("userFullName")
    private String mUserFullName;
    @SerializedName("userEmailAddress")
    private String mUserEmailAddress;

    public User(Long id, String mUsername, String mUserPassword, boolean isAdmin, boolean isLoggedIn, boolean isBanned) {
        this.mId = id;
        this.isLoggedIn = isLoggedIn;
        this.mUsername = mUsername;
        this.mUserPassword = mUserPassword;
        this.mIsAdmin = isAdmin;
        this.isBanned = isBanned;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmUserPassword() {
        return mUserPassword;
    }

    public void setmUserPassword(String mUserPassword) {
        this.mUserPassword = mUserPassword;
    }

    public boolean ismIsAdmin() {
        return mIsAdmin;
    }

    public void setmIsAdmin(boolean mIsAdmin) {
        this.mIsAdmin = mIsAdmin;
    }

    public List<Comment> getmComments() {
        return mComments;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }


    public boolean loggedIn() {
        return isLoggedIn;
    }

    public void setmComments(List<Comment> mComments) {
        this.mComments = mComments;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getUserFullName() {
        return mUserFullName;
    }

    public void setUserFullName(String userFullName) {
        mUserFullName = userFullName;
    }

    public String getUserEmailAddress() {
        return mUserEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        mUserEmailAddress = userEmailAddress;
    }
}
