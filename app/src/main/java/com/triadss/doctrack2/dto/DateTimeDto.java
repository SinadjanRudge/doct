package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.util.Calendar;
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

    public String ToString() {
        return date.ToString() + " " + time.ToString();
    }

    public static DateTimeDto ToDateTimeDto(Timestamp timestamp)
    {
        Date date = timestamp.toDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        DateTimeDto converted = new DateTimeDto();
        converted.setDate(new DateDto(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        converted.setTime(new TimeDto(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        return converted;
    }

    public static DateTimeDto ToDateTimeDto(LocalDateTime localDateTime)
    {
        DateTimeDto converted = new DateTimeDto();
        converted.setDate(new DateDto(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth()));
        converted.setTime(new TimeDto(localDateTime.getHour(), localDateTime.getMinute()));
        return converted;
    }
}
