package com.example.sports_app.entities;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Event {
    @SerializedName("id")
    private long mId;
    @SerializedName("eventName")
    private String mEventName;
    @SerializedName("eventDescription")
    private String mEventDescription;
    @SerializedName("sport")
    private String mSport;
    @SerializedName("event_date")
    private LocalDateTime mEventDate;
    @SerializedName("eventStartTime")
    private String mEventStartDate;
    @SerializedName("image")
    private String mEventImage;

    public String getmEventImage() {
        return mEventImage;
    }

    public void setmEventImage(String mEventImage) {
        this.mEventImage = mEventImage;
    }

    @SerializedName("inLessThan24Hours")
    private boolean inLessThan24Hours;

    private boolean beenNotified = false;

    public boolean isBeenNotified() {
        return beenNotified;
    }

    public void setBeenNotified(boolean beenNotified) {
        this.beenNotified = beenNotified;
    }

    public boolean isInLessThan24Hours() {
        return inLessThan24Hours;
    }

    public void setInLessThan24Hours(boolean inLessThan24Hours) {
        this.inLessThan24Hours = inLessThan24Hours;
    }

    private List<Long> mSubscribers;

    public Event(long mId, String mEventName, String mEventDescription, String mSport, String startDate) {
        this.mId = mId;
        this.mEventName = mEventName;
        this.mEventDescription = mEventDescription;
        this.mSport = mSport;
        this.mEventStartDate = startDate;
    }

    public Event() {

    }

    private LocalDateTime createLocalDateTime(String timeCommented) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");;
        LocalDateTime localDateTime = LocalDateTime.parse(timeCommented, formatter);
        return localDateTime;
    }

    public String getFormattedDate() {
        this.mEventDate = createLocalDateTime(mEventStartDate);
        String date = mEventDate.getYear() + " "
                + mEventDate.getDayOfMonth() + "."
                + mEventDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
                + mEventDate.getHour() + ":"
                + mEventDate.getMinute();

        return date;
    }

    public void setEventDate(String startingDate) {
        this.mEventDate = createLocalDateTime(startingDate);
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

    public String getEventStartDate() {
        return mEventStartDate;
    }

    public void setEventStartDate(String startDate) {
        this.mEventStartDate = startDate;
    }

    public List<Long> getSubscribers() { return mSubscribers; }

}
