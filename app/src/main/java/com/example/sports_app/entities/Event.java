package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Event {
    @SerializedName("id")
    private long mId;
    @SerializedName("event_name")
    private String mEventName;
    @SerializedName("event_description")
    private String mEventDescription;
    @SerializedName("sport")
    private String mSport;
    @SerializedName("event_date")
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

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getEventName() {
        return mEventName;
    }

    public void setEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public String getEventDescription() {
        return mEventDescription;
    }

    public void setEventDescription(String mEventDescription) {
        this.mEventDescription = mEventDescription;
    }

    public String getSport() {
        return mSport;
    }

    public void setSport(String mSport) {
        this.mSport = mSport;
    }

    public LocalDate getEventDate() {
        return mEventDate;
    }

    public void setEventDate(LocalDate mEventDate) {
        this.mEventDate = mEventDate;
    }
}
