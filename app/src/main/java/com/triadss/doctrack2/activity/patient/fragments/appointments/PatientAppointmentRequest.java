package com.triadss.doctrack2.activity.patient.fragments.appointments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.config.constants.NotificationConstants;
import com.triadss.doctrack2.config.constants.ReportConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.TimeDto;
import com.triadss.doctrack2.helper.ButtonManager;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.NotificationDTO;
import com.triadss.doctrack2.notification.NotificationService;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.NotificationRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;
import com.triadss.doctrack2.utils.AppointmentFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PatientAppointmentRequest extends Fragment {
    private Button pickDateButton, pickTimeBtn, confirmButton;
    private EditText textInputPurpose;
    private AppointmentRepository appointmentRepository;
    private NotificationRepository notificationRepository = new NotificationRepository();
    private ReportsRepository _reportsRepository = new ReportsRepository();
    private NotificationDTO notifyDto;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private TextView dateErrorText, timeErrorText;

    private String TimePick;
    public String getTimePick(){

        return TimePick;
    }

    public void setTimePick(String TimePick){

        if(TimePick.equals("8:00 am - 9:00 am")){
            TimePick = "8";
        }
        if(TimePick.equals("9:00 am - 10:00 am")){
            TimePick = "9";
        }
        if(TimePick.equals("10:00 am - 11:00 am")){
            TimePick ="10";
        }
        if(TimePick.equals("11:00 am - 12:00 pm")){
            TimePick = "11";
        }
        if(TimePick.equals("12:00 pm - 1:00 pm")){
            TimePick = "12";
        }
        if(TimePick.equals("1:00 pm - 2:00 pm")){
            TimePick = "13";
        }
        if(TimePick.equals("2:00 pm - 3:00 pm")){
            TimePick = "14";
        }
        if(TimePick.equals("3:00 pm - 4:00 pm")){
            TimePick = "15";
        }
        if(TimePick.equals("4:00 pm - 5:00 pm")){
            TimePick = "16";
        }
        this.TimePick = TimePick;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_patient_request, container, false);

        appointmentRepository = new AppointmentRepository();

        pickDateButton = rootView.findViewById(R.id.datebutton);
        pickTimeBtn = rootView.findViewById(R.id.idBtnPickTime);
        confirmButton = rootView.findViewById(R.id.confirmbutton);
        textInputPurpose = rootView.findViewById(R.id.textInputPurpose);
        dateErrorText = rootView.findViewById(R.id.dateErrorText);
        timeErrorText = rootView.findViewById(R.id.timeErrorText);

        setupDatePicker();
        setupTimePicker();
        setupConfirmationButton();

        return rootView;
    }

    private void setupDatePicker() {
        pickDateButton.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create and show the Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, monthOfYear, dayOfMonth);

                        Calendar currentDate = Calendar.getInstance();

                        if (selectedDate.before(currentDate)) {
                            // Selected date is in the past
                            Toast.makeText(getContext(), "Cannot select past date", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            // Update the text on the button with the selected date
                            pickDateButton.setText(
                                    String.format(Locale.getDefault(), "%04d-%02d-%02d", year1,
                                            monthOfYear + 1, dayOfMonth));
                        }
                    }, year, month, day);

            // Show the Date Picker Dialog
            datePickerDialog.show();
            pickTimeBtn.setText("Pick Time");
        });
    }


    private void setupTimePicker() {
//        // Set up Time Picker Dialog
        pickTimeBtn.setOnClickListener(v -> {
//            // Get the current time
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create and show the Time Picker Dialog
//            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
//                    (view, hourOfDay, minute1) -> {
//                        // Store the selected time
//                        selectedHour = hourOfDay;
//                        selectedMinute = minute1;
//
//                        // Update the text on the button
//                        pickTimeBtn.setText(
//                                String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
//                    }, hour, minute, false);
//
//            // Show the Time Picker Dialog
//            timePickerDialog.show();
//        });
            DateTimeDto selectedDateTime = new DateTimeDto();

            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            // TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
            TimeZone tz = TimeZone.getDefault();
            inFormat.setTimeZone(tz);

            if (pickDateButton.getText().toString().equals("Date") || pickDateButton.getText().toString().equals(" ")) {

                Toast.makeText(getContext(), "Error: must select date first", Toast.LENGTH_SHORT)
                        .show();
            } else {
                selectedDateTime.setDate(new DateDto(selectedYear, selectedMonth + 1, selectedDay));
                selectedDateTime.setTime(new TimeDto(0, 00));
//                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    // TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
//                    TimeZone tz = TimeZone.getDefault();
//                    inFormat.setTimeZone(tz);
                Date date = null;
                try {
                    date = inFormat.parse(pickDateButton.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat outYear = new SimpleDateFormat("yyyy");
                SimpleDateFormat outMonth = new SimpleDateFormat("MM");
                SimpleDateFormat outDay = new SimpleDateFormat("dd");

                String goal = outFormat.format(date);
                String timepickYear = outYear.format(date);
                String timepickMonth = outMonth.format(date);
                String timepickDay = outDay.format(date);

                appointmentRepository.checkAppointmentExists(goal, selectedDateTime.ToTimestampForTimePicker(), Integer.valueOf(timepickYear), Integer.valueOf(timepickMonth), Integer.valueOf(timepickDay), new AppointmentRepository.CheckAppointmentExistFetchCallback() {
                    public void onSuccess(ArrayList<String> lngList) {

                        //AnotherPickTimeSlot(lngList);
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.time_slots_picker);
                        Button cancelBtn = dialog.findViewById(R.id.timePickCancel);

                        String Carl = "";

                        ListView hello = dialog.findViewById(R.id.breakdown);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, lngList) {

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                TextView textView = (TextView) super.getView(position, convertView, parent);
                                textView.setTextSize(15);
                                return textView;
                            }
                        };

                        hello.setOnItemClickListener((adapterView, view, i, l) -> {
                            // String s = hello.getItemAtPosition(i).toString();
                            String itemValue = (String) hello.getItemAtPosition(i);
                            Toast.makeText(getContext(), itemValue, Toast.LENGTH_LONG).show();
                            if (!itemValue.equals("Not available")) {
                                // updateTime.setText(itemValue);
                                setTimePick(itemValue);
                                pickTimeBtn.setText(getTimePick() + ":00");
                                selectedDateTime.setTime(new TimeDto(Integer.parseInt(getTimePick()), 00));

                                selectedHour = Integer.parseInt(getTimePick());
                                selectedMinute = 00;
                                dialog.dismiss();
                            }
                            // adapter.dismiss(); // If you want to close the adapter
                        });
                        cancelBtn.setOnClickListener(v -> {
                            dialog.dismiss();
                        });

                        hello.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        // dialog.getWindow().setGravity(Gravity.LEFT);
                        //updateTime.setText(getTimePick());
                        dialog.show();
                        // updateTime.setText(getTimePick());
                    }

                    @Override
                    public void onError(String errorMessage) {


                    }

                });
                //updateTime.setText(getTimePick());
                //FromDateandTime(updateDate.getText().toString(),timepickyear,timepickmonth,timepickday);
            }
        });
    }

    private void setupConfirmationButton() {

        DateTimeDto selectedDateTime = new DateTimeDto();
        confirmButton.setOnClickListener(v -> {
            // Handle confirmation button click
            handleConfirmationButtonClick();

        });
    }

    private boolean isInputsNotValid() {
        dateErrorText.setVisibility(View.GONE);
        timeErrorText.setVisibility(View.GONE);
        boolean invalidTime = AppointmentFunctions.IsValidHour(selectedHour);
        boolean invalid = false;
        if (TextUtils.isEmpty(textInputPurpose.getText().toString()) &&
                (selectedYear == 0 || selectedMonth == 0 || selectedDay == 0) && invalidTime) {
            Toast.makeText(getContext(), "Please enter a purpose and select a valid date and time", Toast.LENGTH_SHORT)
                    .show();

            textInputPurpose.setError("Purpose cannot be empty");
            dateErrorText.setVisibility(View.VISIBLE);
            timeErrorText.setVisibility(View.VISIBLE);
            invalid = true;
        }

        if (TextUtils.isEmpty(textInputPurpose.getText().toString())) {
            textInputPurpose.setError("Purpose cannot be empty");
            Toast.makeText(getContext(), "Please enter a purpose", Toast.LENGTH_SHORT).show();
            invalid = true;
        }

        if ((selectedYear == 0 || selectedMonth == 0 || selectedDay == 0)
                && invalidTime) {
            Toast.makeText(getContext(), "Please select a valid date and time", Toast.LENGTH_SHORT).show();
            dateErrorText.setVisibility(View.VISIBLE);
            timeErrorText.setVisibility(View.VISIBLE);
            invalid = true;
        }

        if (selectedYear == 0 || selectedMonth == 0 || selectedDay == 0) {
            Toast.makeText(getContext(), "Please select a valid date", Toast.LENGTH_SHORT).show();
            dateErrorText.setVisibility(View.VISIBLE);
            invalid = true;
        }

        if (invalidTime) {
            Toast.makeText(getContext(), "Must be between 8:00 - 17:00", Toast.LENGTH_SHORT).show();
            timeErrorText.setVisibility(View.VISIBLE);
            invalid = true;
        }
        return invalid;
    }

    private void handleConfirmationButtonClick() {
        if (isInputsNotValid())
            return;

        if(DateDto.isDayWeekend(selectedYear, selectedMonth, selectedDay)){
            Toast.makeText(getContext(), "Appointments cannot be scheduled on Weekends", Toast.LENGTH_SHORT).show();
            return;
        }

        String purpose = textInputPurpose.getText().toString();

        Timestamp dateTimeOfAppointment = new Timestamp(
                new Date(selectedYear - 1900, selectedMonth, selectedDay, selectedHour, selectedMinute));

        final String status = AppointmentTypeConstants.ONGOING;

        AppointmentDto appointment = new AppointmentDto("",
                "", purpose, dateTimeOfAppointment, status);

        ButtonManager.disableButton(confirmButton);

        appointmentRepository.addAppointment(appointment, new AppointmentRepository.AppointmentAddCallback() {
            @Override
            public void onSuccess(String appointmentId) {
                _reportsRepository.addPatientRequestScheduleReport(appointment, new ReportsRepository.ReportCallback() {
                    @Override
                    public void onReportAddedSuccessfully() {
                        textInputPurpose.setText("");
                        pickTimeBtn.setText("Pick Time");
                        pickDateButton.setText("Select Date");
                        Toast.makeText(getContext(), appointmentId + " added", Toast.LENGTH_SHORT).show();

                        ButtonManager.enableButton(confirmButton);
                    }

                    @Override
                    public void onReportFailed(String errorMessage) {
                        ButtonManager.enableButton(confirmButton);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                ButtonManager.enableButton(confirmButton);
            }
        });

    }

    public void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(getContext(), NotificationService.class);
        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationService.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),
                NotificationConstants.DEFAULT_NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NotificationConstants.NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }
}
