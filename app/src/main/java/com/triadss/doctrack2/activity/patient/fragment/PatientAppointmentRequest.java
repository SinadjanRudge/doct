package com.triadss.doctrack2.activity.patient.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.NotificationConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.NotificationDTO;
import com.triadss.doctrack2.notification.NotificationService;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.NotificationRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PatientAppointmentRequest extends Fragment {
    private Button pickDateButton, pickTimeBtn, confirmButton;
    private EditText textInputPurpose;
    private AppointmentRepository appointmentRepository;
    private NotificationRepository notificationRepository;
    private ReportsRepository _reportsRepository = new ReportsRepository();
    private NotificationDTO notifyDto;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_patient_request, container, false);

        appointmentRepository = new AppointmentRepository();

        pickDateButton = rootView.findViewById(R.id.datebutton);
        pickTimeBtn = rootView.findViewById(R.id.idBtnPickTime);
        confirmButton = rootView.findViewById(R.id.confirmbutton);
        textInputPurpose = rootView.findViewById(R.id.textInputPurpose);

        setupDatePicker();
        setupTimePicker();
        setupConfirmationButton();

        return rootView;
    }

    private void setupDatePicker() {
        // Set up Date Picker Dialog
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create and show the Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth) {
                                // Store the selected date
                                selectedYear = year;
                                selectedMonth = monthOfYear;
                                selectedDay = dayOfMonth;

                                // Update the text on the button
                                pickDateButton.setText(
                                        String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear,
                                                selectedMonth + 1, selectedDay));
                            }
                        }, year, month, day);

                // Show the Date Picker Dialog
                datePickerDialog.show();
            }
        });
    }

    private void setupTimePicker() {
        // Set up Time Picker Dialog
        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create and show the Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Store the selected time
                                selectedHour = hourOfDay;
                                selectedMinute = minute;

                                // Update the text on the button
                                pickTimeBtn.setText(
                                        String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                            }
                        }, hour, minute, false);

                // Show the Time Picker Dialog
                timePickerDialog.show();
            }
        });
    }

    private void setupConfirmationButton() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirmation button click
                handleConfirmationButtonClick();
            }
        });
    }

    private void handleConfirmationButtonClick() {
        // Sample values for AppointmentDto
        String purpose = textInputPurpose.getText().toString();

        Timestamp dateTimeOfAppointment = new Timestamp(
                new Date(selectedYear - 1900, selectedMonth, selectedDay, selectedHour, selectedMinute));

        final String status = AppointmentTypeConstants.ONGOING;

        AppointmentDto appointment = new AppointmentDto("",
                "", purpose, dateTimeOfAppointment, status);

        notifyDto = new NotificationDTO();
        notifyDto.setTitle("Patient New Appointment Request on " + pickDateButton.getText().toString() +" "+ pickTimeBtn.getText().toString());
        notifyDto.setContent(textInputPurpose.getText().toString());
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
        String dateNow = currentDate.format(formatter);

        notifyDto.setDataSent(java.sql.Timestamp.valueOf(dateNow));
        notificationRepository = new NotificationRepository();
        notificationRepository.pushUserNotification(notifyDto, new NotificationRepository.NotificationAddCallback() {
            @Override
            public void onSuccess(String appointmentId) {
                //scheduleNotification(getNotification( "1 second delay" ) , 1000 );
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        appointmentRepository.addAppointment(appointment, new AppointmentRepository.AppointmentAddCallback() {
            @Override
            public void onSuccess(String appointmentId) {
                _reportsRepository.addPatientRequestScheduleReport(appointment, new ReportsRepository.ReportCallback() {
                    @Override
                    public void onReportAddedSuccessfully() {
                        textInputPurpose.setText("");
                        pickTimeBtn.setText("Select Date");
                        pickDateButton.setText("Select Time");
                        Toast.makeText(getContext(), appointmentId + " added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReportFailed(String errorMessage) {

                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error, if needed
            }
        });
    }
    public void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( getContext(), NotificationService.class);
        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID , 1 );
        notificationIntent.putExtra(NotificationService.NOTIFICATION , notification);
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( getContext(),
                0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT );
        long futureInMillis = SystemClock.elapsedRealtime () + delay;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent);
    }

    public Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( getContext(), NotificationConstants.DEFAULT_NOTIFICATION_CHANNEL_ID ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NotificationConstants.NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }
}
