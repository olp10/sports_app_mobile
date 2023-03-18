package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Comment {
    @SerializedName("userName")
    private String mUsername;

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getmTimeCommented() {
        return mTimeCommented;
    }

    public void setmTimeCommented(String mTimeCommented) {
        this.mTimeCommented = mTimeCommented;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public Thread getmThread() {
        return mThread;
    }

    public void setmThread(Thread mThread) {
        this.mThread = mThread;
    }

    public LocalDateTime getmCommentDate() {
        return mCommentDate;
    }

    public void setmCommentDate(LocalDateTime mCommentDate) {
        this.mCommentDate = mCommentDate;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    //@SerializedName("date_commented")
    //private LocalDate mDateCommented;
    //@SerializedName("time_commented")
    //private LocalDate mTimeCommented;
    @SerializedName("id")
    private Long mId;
    @SerializedName("timeCommented")
    private String mTimeCommented;
    @SerializedName("comment")
    private String mComment;
    private Thread mThread;
    private LocalDateTime mCommentDate;

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
        this.mUsername = user;
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
        this.mCommentDate = createLocalDateTime(mTimeCommented);
        String date = mCommentDate.getYear() + " "
                + mCommentDate.getDayOfMonth() + "."
                + mCommentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
                + mCommentDate.getHour() + ":"
                + mCommentDate.getMinute();

        return date;
    }

    public LocalDateTime getCommentDate() {
        return mCommentDate;
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
