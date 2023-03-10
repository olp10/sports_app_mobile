package com.example.sports_app.entities;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
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

    public String getmEventStartDate() {
        return mEventStartDate;
    }

    public void setmEventStartDate(String startDate) {
        this.mEventStartDate = startDate;
    }

    public List<Long> getSubscribers() { return mSubscribers; }

}
