package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.ArrayList;

public class Thread implements Comparable<Thread> {
    @SerializedName("id")
    private long mId;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("pinned")
    private boolean mIsPinned;
    @SerializedName("header")
    private String mHeader;
    @SerializedName("body")
    private String mBody;
    @SerializedName("sport")
    private String mSport;

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    @SerializedName("date")
    private String mDate;

    private ArrayList<Comment> comments = new ArrayList<Comment>();

    public ArrayList<Comment> getComments() {

        if (comments != null){
            return comments;
        } else {
            return new ArrayList<Comment>();
        }
    }

    public int getNumberOfComments() {
        return getComments().size();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public boolean isIsPinned() {
        return mIsPinned;
    }

    public void setIsPinned(boolean mIsPinned) {
        this.mIsPinned = mIsPinned;
    }

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String mHeader) {
        this.mHeader = mHeader;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }

    public String getSport() {
        return mSport;
    }

    public void setSport(String mSport) {
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

    @Override
    public int compareTo(Thread o) {
//         Pinned ekki að virka?
        System.out.println("Pinned? " + this.isIsPinned());
        if (this.isIsPinned() && !o.isIsPinned()){
            return 1;
        } else if (!this.isIsPinned() && o.isIsPinned()) {
            return -1;
        }
        if (this.mDate.equals(o.getmDate())) {
            return 0;
        } else if (this.mDate.compareTo(o.getmDate()) > 0) {
            return 1;
        }
        return -1;
    }
}
