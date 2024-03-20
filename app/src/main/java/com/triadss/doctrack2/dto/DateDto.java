package com.triadss.doctrack2.dto;

import android.widget.DatePicker;

import com.google.firebase.Timestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateDto {
    public DateDto(int year, int month, int day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String ToString()
    {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year,
                month + 1, day);
    }

    public Date ToDate()
    {
        LocalDate localDate = LocalDate.of(year, month, day);

        // Convert LocalDate to LocalDateTime at the start of the day
        LocalDateTime localDateTime = localDate.atStartOfDay();

        // Convert LocalDateTime to Instant
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public Timestamp ToTimeStamp()
    {
        return new Timestamp(ToDate());
    }

    public static DateDto fromDatePicker(DatePicker datepicker)
    {
        return new DateDto(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth());
    } 
}
