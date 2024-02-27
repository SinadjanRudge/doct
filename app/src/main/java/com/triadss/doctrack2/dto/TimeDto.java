package com.triadss.doctrack2.dto;

import java.util.Locale;

public class TimeDto {
    public TimeDto(int hour, int minute)
    {
        this.hour = hour;
        this.minute = minute;
    }

    private int hour;
    private int minute;


    public String ToString()
    {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

}
