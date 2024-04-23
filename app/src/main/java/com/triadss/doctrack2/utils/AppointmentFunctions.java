package com.triadss.doctrack2.utils;

import com.triadss.doctrack2.dto.TimeDto;

public class AppointmentFunctions {
    public static boolean IsValidHour(int hour) {
        return hour < 8 || hour > 17;
    }

    public static boolean IsValidHour(TimeDto time) {
        return IsValidHour(time.getHour());
    }
}
