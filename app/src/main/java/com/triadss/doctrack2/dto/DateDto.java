package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
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

    public Timestamp ToTimestamp()
    {
        try {
            // Parse the string to a Date object
            String dateString = getYear() + "-" + getMonth() + "-" + getDay() + " 12:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateString);

            // Convert the Date object to a Firebase Timestamp
            Timestamp timestamp = new Timestamp(date);
            return timestamp;
        }
        catch(Exception e) {
            e.printStackTrace();
            return new Timestamp(new Date());
        }
    }
}
