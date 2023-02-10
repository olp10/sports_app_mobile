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
}
