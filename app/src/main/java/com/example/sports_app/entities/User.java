package com.example.sports_app.entities;

import java.util.List;

public class User {
    private Long mId;

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

    public void setmComments(List<Comment> mComments) {
        this.mComments = mComments;
    }

    public User(String mUsername, String mUserPassword, boolean isAdmin) {
        this.mUsername = mUsername;
        this.mUserPassword = mUserPassword;
        this.mIsAdmin = isAdmin;
    }

    private String mUsername;
    private String mUserPassword;
    private boolean mIsAdmin;
    private List<Comment> mComments;
}
