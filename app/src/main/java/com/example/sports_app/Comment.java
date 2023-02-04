package com.example.sports_app;

import java.time.LocalDate;

public class Comment {
    private Long ID;
    private User user;
    private LocalDate dateCommented;
    private LocalDate timeCommented;
    private String comment;
    private Thread thread;

    public Comment() {

    }

    public Comment(Long ID, User user, LocalDate dateCommented, LocalDate timeCommented, String comment, Thread thread) {
        this.ID = ID;
        this.user = user;
        this.dateCommented = dateCommented;
        this.timeCommented = timeCommented;
        this.comment = comment;
        this.thread = thread;
    }
}
