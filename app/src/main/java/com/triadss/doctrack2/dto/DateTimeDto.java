package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Locale;

public class DateTimeDto {
    public DateTimeDto()
    {
    }

    private DateDto date;
    private TimeDto time;

    public DateDto getDate() {
        return date;
    }

    public void setDate(DateDto date) {
        this.date = date;
    }

    public TimeDto getTime() {
        return time;
    }

    public void setTime(TimeDto time) {
        this.time = time;
    }

    public Timestamp ToTimestamp()
    {
        return new Timestamp(
                new Date(date.getYear() - 1900, date.getMonth(), date.getDay(), time.getHour(), time.getMinute()));
    }

}
