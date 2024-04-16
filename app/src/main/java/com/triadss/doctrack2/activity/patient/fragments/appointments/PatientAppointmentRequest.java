package com.triadss.doctrack2.activity.patient.fragments.appointments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.helper.ButtonManager;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.NotificationDTO;
import com.triadss.doctrack2.notification.NotificationService;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.NotificationRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PatientAppointmentRequest extends Fragment {
    private Button pickDateButton, pickTimeBtn, confirmButton;
    private EditText textInputPurpose;
    private AppointmentRepository appointmentRepository;
    private NotificationRepository notificationRepository = new NotificationRepository();
    private ReportsRepository _reportsRepository = new ReportsRepository();
    private NotificationDTO notifyDto;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private TextView dateErrorText, timeErrorText;

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
        // Set up Date Picker Dialog
        pickDateButton.setOnClickListener(v -> {
            // Get the current date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create and show the Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Store the selected date
                        selectedYear = year1;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;

                        // Update the text on the button
                        pickDateButton.setText(
                                String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear,
                                        selectedMonth + 1, selectedDay));
                    }, year, month, day);

            // Show the Date Picker Dialog
            datePickerDialog.show();
        });
    }

    private void setupTimePicker() {
        // Set up Time Picker Dialog
        pickTimeBtn.setOnClickListener(v -> {
            // Get the current time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create and show the Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view, hourOfDay, minute1) -> {
                        // Store the selected time
                        selectedHour = hourOfDay;
                        selectedMinute = minute1;

                        // Update the text on the button
                        pickTimeBtn.setText(
                                String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                    }, hour, minute, false);

            // Show the Time Picker Dialog
            timePickerDialog.show();
        });
    }

    private void setupConfirmationButton() {
        confirmButton.setOnClickListener(v -> {
            // Handle confirmation button click
            handleConfirmationButtonClick();
        });
    }

    private boolean isInputsNotValid() {
        dateErrorText.setVisibility(View.GONE);
        timeErrorText.setVisibility(View.GONE);
        boolean invalidTime = selectedHour < 8 || selectedHour > 17;
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
            Toast.makeText(getContext(), "Please select a valid time", Toast.LENGTH_SHORT).show();
            timeErrorText.setVisibility(View.VISIBLE);
            invalid = true;
        }
        return invalid;
    }

    private void handleConfirmationButtonClick() {
        if (isInputsNotValid())
            return;

        // Sample values for AppointmentDto
        String purpose = textInputPurpose.getText().toString();

        Timestamp dateTimeOfAppointment = new Timestamp(
                new Date(selectedYear - 1900, selectedMonth, selectedDay, selectedHour, selectedMinute));

        final String status = AppointmentTypeConstants.ONGOING;

        AppointmentDto appointment = new AppointmentDto("",
                "", purpose, dateTimeOfAppointment, status);

        notifyDto = new NotificationDTO();
        notifyDto.setTitle("Patient New Appointment Request on " + pickDateButton.getText().toString() + " "
                + pickTimeBtn.getText().toString());
        notifyDto.setContent(textInputPurpose.getText().toString());
        DateTimeDto datedto = new DateTimeDto();
        notifyDto.setDateSent(datedto.GetCurrentTimeStamp());
        notificationRepository.pushUserNotification(notifyDto, new NotificationRepository.NotificationAddCallback() {
            @Override
            public void onSuccess(String appointmentId) {
                // scheduleNotification(getNotification( "1 second delay" ) , 1000 );
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
