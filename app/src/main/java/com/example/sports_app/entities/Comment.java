package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Comment {
    @SerializedName("id")
    private Long mId;
    @SerializedName("userName")
    private String mUser;
//    @SerializedName("dateCommented")
//    private LocalDate mDateCommented;
    @SerializedName("timeCommented")
    private LocalDateTime mTimeCommented;
    @SerializedName("comment")
    private String mComment;
    private Thread mThread;

    public Comment() {

    }

    public Comment(Long Id, String user, LocalDateTime timeCommented, String comment, Thread thread) {
        this.mId = Id;
        this.mUser = user;
//        this.mDateCommented = dateCommented;
        this.mTimeCommented = timeCommented;
        this.mComment = comment;
        this.mThread = thread;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

//    public LocalDate getDateCommented() {
//        return mDateCommented;
//    }
//
//    public void setDateCommented(LocalDate dateCommented) {
//        mDateCommented = dateCommented;
//    }

    public LocalDateTime getTimeCommented() {
        return mTimeCommented;
    }

    public void setTimeCommented(LocalDateTime timeCommented) {
        mTimeCommented = timeCommented;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public Thread getThread() {
        return mThread;
    }

    public void setThread(Thread thread) {
        mThread = thread;
    }
}
