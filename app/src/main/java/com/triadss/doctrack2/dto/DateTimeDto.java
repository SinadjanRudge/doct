package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
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
         Date extractedDate = new Date(date.getYear() - 1900,
                date.getMonth() - 1, date.getDay(), time.getHour(), time.getMinute());
        return new Timestamp(extractedDate);
    }

    public Timestamp ToTimestampForTimePicker()
    {
        Date extractedDate = new Date(date.getYear() - 1900,
                date.getMonth() - 1, date.getDay(), time.getHour(), time.getMinute());
        return new Timestamp(extractedDate);
    }
    public String ToString() {
        return date.ToString() + " " + ToAMPMString(time.ToString());
    }
    public String ToAMPMString(String time){
        try{
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");

            Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch(Exception e){
            return "";
        }
    }

    public DateTimeDto Clone() {
        DateTimeDto newDateTimeDto = new DateTimeDto();
        newDateTimeDto.setTime(time.Clone());
        newDateTimeDto.setDate(date.Clone());
        return newDateTimeDto;
    }

    public static DateTimeDto ToDateTimeDto(Timestamp timestamp)
    {
        Date date = timestamp.toDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        DateTimeDto converted = new DateTimeDto();
        converted.setDate(new DateDto(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
        converted.setTime(new TimeDto(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        return converted;
    }

    public Timestamp ReschedToTimestamp()
    {
        return new Timestamp(
                new Date(date.getYear() - 1900, date.getMonth() - 1, date.getDay(), time.getHour(), time.getMinute()));
    }

    public static DateTimeDto ToDateTimeDto(LocalDateTime localDateTime)
    {
        DateTimeDto converted = new DateTimeDto();
        converted.setDate(new DateDto(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth()));
        converted.setTime(new TimeDto(localDateTime.getHour(), localDateTime.getMinute()));
        return converted;
    }

    public static Timestamp GetCurrentTimeStamp() {
        LocalDateTime currentDate = LocalDateTime.now();

        Timestamp currentTimeStamp  = DateTimeDto.ToDateTimeDto(currentDate).ToTimestamp();

        return currentTimeStamp;
    }

    public static int ComputeAge(Timestamp birthday) {
        LocalDate birthdate = birthday.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthdate, now);
        return period.getYears();
    }
}
