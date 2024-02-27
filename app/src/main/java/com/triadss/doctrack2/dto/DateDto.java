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

    public String ToString()
    {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year,
                month + 1, day);
    }

}
