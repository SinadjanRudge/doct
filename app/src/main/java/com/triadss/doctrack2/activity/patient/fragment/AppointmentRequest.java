package com.triadss.doctrack2.activity.patient.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.firebase.Timestamp;

public class AppointmentRequest extends Fragment {
    private Button pickDateButton, pickTimeBtn, confirmButton;
    private EditText textInputPurpose;
    private AppointmentRepository appointmentRepository;
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
        ;

        Timestamp dateTimeOfAppointment = new Timestamp(
                new Date(selectedYear - 1900, selectedMonth, selectedDay, selectedHour, selectedMinute));

        final String status = AppointmentTypeConstants.PENDING;

        AppointmentDto appointment = new AppointmentDto("",
                "", purpose, dateTimeOfAppointment, status);

        appointmentRepository.addAppointment(appointment, new AppointmentRepository.AppointmentAddCallback() {
            @Override
            public void onSuccess(String appointmentId) {
                // Handle success, if needed
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error, if needed
            }
        });

        //! FOR TESTING
        appointmentRepository.getAllAppointments(new AppointmentRepository.AppointmentFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {
                for (AppointmentDto a : appointments) {
                    Log.d("AppointRequest Fragment", "Requester's id: " + a.getPatientId());
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
