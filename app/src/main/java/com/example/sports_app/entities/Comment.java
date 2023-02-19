package com.example.sports_app.entities;

import java.time.LocalDate;

public class Comment {
    private Long mId;
    private User mUser;
    private LocalDate mDateCommented;
    private LocalDate mTimeCommented;
    private String mComment;
    private Thread mThread;

    public Comment() {

    }

    public Comment(Long Id, User user, LocalDate dateCommented, LocalDate timeCommented, String comment, Thread thread) {
        this.mId = Id;
        this.mUser = user;
        this.mDateCommented = dateCommented;
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

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
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

    public Thread getThread() {
        return mThread;
    }

    public void setThread(Thread thread) {
        mThread = thread;
    }
}
