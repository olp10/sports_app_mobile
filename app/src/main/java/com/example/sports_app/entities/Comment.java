package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Comment {
    @SerializedName("comment_id")
    private long mId;
    @SerializedName("userid")
    private long mUserId;
    @SerializedName("date_commented")
    private LocalDate mDateCommented;
    @SerializedName("time_commented")
    private LocalDate mTimeCommented;
    @SerializedName("comment")
    private String mComment;
    @SerializedName("threadid")
    private long mThreadId;

    public Comment() {

    }

    public Comment(long Id, long userId, LocalDate dateCommented, LocalDate timeCommented, String comment, long threadId) {
        this.mId = Id;
        this.mUserId = userId;
        this.mDateCommented = dateCommented;
        this.mTimeCommented = timeCommented;
        this.mComment = comment;
        this.mThreadId = threadId;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public long getUser() {
        return mUserId;

    }

    public void setUser(long userId) {
        mUserId = userId;
    }

    public LocalDate getDateCommented() {
        return mDateCommented;
    }

    public void setDateCommented(LocalDate dateCommented) {
        mDateCommented = dateCommented;
    }

    public LocalDate getTimeCommented() {
        return mTimeCommented;
    }

    public void setTimeCommented(LocalDate timeCommented) {
        mTimeCommented = timeCommented;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public long getThreadId() {
        return mThreadId;
    }

    public void setThread(long threadId) {
        mThreadId = threadId;
    }
}
