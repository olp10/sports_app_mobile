package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Comment {
    @SerializedName("username")
    private String mUsername;
    //@SerializedName("date_commented")
    //private LocalDate mDateCommented;
    //@SerializedName("time_commented")
    //private LocalDate mTimeCommented;
    @SerializedName("id")
    private Long mId;
    @SerializedName("userName")
    private String mUser;
    @SerializedName("timeCommented")
    private String mTimeCommented;
    @SerializedName("comment")
    private String mComment;
    private Thread mThread;
    private LocalDateTime mLocalDateTime;

    public Comment() {

    }

    /*
    public Comment(long Id, String userName, LocalDate dateCommented, LocalDate timeCommented, String comment, long threadId) {
        this.mId = Id;
        this.mUsername = userName;
        this.mDateCommented = dateCommented;
    }*/

    public Comment(Long Id, String user, String timeCommented, String comment, Thread thread) {
        this.mId = Id;
        this.mUser = user;
        this.mTimeCommented = timeCommented;
        this.mComment = comment;
        this.mThread = thread;
    }

    private LocalDateTime createLocalDateTime(String timeCommented) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(timeCommented, formatter);
        return localDateTime;
    }

    public String getFormattedDate() {
        this.mLocalDateTime = createLocalDateTime(mTimeCommented);
        String date = mLocalDateTime.getYear() + " "
                + mLocalDateTime.getDayOfMonth() + "."
                + mLocalDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
                + mLocalDateTime.getHour() + ":"
                + mLocalDateTime.getMinute();

        return date;
    }

    public LocalDateTime getLocalDateTime() {
        return mLocalDateTime;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getUser() {
        return mUsername;
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

    public String getTimeCommented() {
        return mTimeCommented;
    }

    public void setTimeCommented(String timeCommented) {
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
