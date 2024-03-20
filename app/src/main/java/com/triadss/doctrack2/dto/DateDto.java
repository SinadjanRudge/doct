package com.triadss.doctrack2.dto;

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

    public Timestamp ToTimeStamp()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        
        long millis = calendar.getTimeInMillis();

        Timestamp timestamp = new Timestamp(millis);

        return timestamp;
    }

    public static DateDto fromDatePicker(DatePicker datepicker)
    {
        return new DateDto(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth());
    } 
}
