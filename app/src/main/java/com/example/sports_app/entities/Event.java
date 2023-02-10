package com.example.sports_app.entities;

import java.time.LocalDate;

public class Event {
    private long mId;
    private String mEventName;
    private String mEventDescription;
    private String mSport;
    private LocalDate mEventDate;

    public Event(long mId, String mEventName, String mEventDescription, String mSport, LocalDate mEventDate) {
        this.mId = mId;
        this.mEventName = mEventName;
        this.mEventDescription = mEventDescription;
        this.mSport = mSport;
        this.mEventDate = mEventDate;
    }

    public Event() {

    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public String getmEventDescription() {
        return mEventDescription;
    }

    public void setmEventDescription(String mEventDescription) {
        this.mEventDescription = mEventDescription;
    }

    public String getmSport() {
        return mSport;
    }

    public void setmSport(String mSport) {
        this.mSport = mSport;
    }

    public LocalDate getmEventDate() {
        return mEventDate;
    }

    public void setmEventDate(LocalDate mEventDate) {
        this.mEventDate = mEventDate;
    }
}
