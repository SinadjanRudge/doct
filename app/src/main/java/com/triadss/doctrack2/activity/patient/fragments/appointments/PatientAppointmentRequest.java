package com.triadss.doctrack2.activity.patient.fragments.appointments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.helper.ButtonManager;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PatientAppointmentRequest extends Fragment {
    private Button pickDateButton, pickTimeBtn, confirmButton;
    private EditText textInputPurpose;
    private AppointmentRepository appointmentRepository;
    private final ReportsRepository _reportsRepository = new ReportsRepository();
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

    private boolean isInputsNotValid(){
        dateErrorText.setVisibility(View.GONE);
        timeErrorText.setVisibility(View.GONE);

        if (TextUtils.isEmpty(textInputPurpose.getText().toString()) &&
                (selectedYear == 0 || selectedMonth == 0 || selectedDay == 0 ||
                        selectedHour == 0 || selectedMinute == 0)) {
            Toast.makeText(getContext(), "Please enter a purpose and select a valid date and time", Toast.LENGTH_SHORT).show();

            textInputPurpose.setError("Purpose cannot be empty");
            dateErrorText.setVisibility(View.VISIBLE);
            timeErrorText.setVisibility(View.VISIBLE);
            return true;
        }

        if (TextUtils.isEmpty(textInputPurpose.getText().toString())) {
            textInputPurpose.setError("Purpose cannot be empty");
            Toast.makeText(getContext(), "Please enter a purpose", Toast.LENGTH_SHORT).show();
            return true;
        }

        if ((selectedYear == 0 || selectedMonth == 0 || selectedDay == 0) && (selectedHour == 0 || selectedMinute == 0)) {
            Toast.makeText(getContext(), "Please select a valid date and time", Toast.LENGTH_SHORT).show();
            dateErrorText.setVisibility(View.VISIBLE);
            timeErrorText.setVisibility(View.VISIBLE);
            return true;
        }

        if (selectedYear == 0 || selectedMonth == 0 || selectedDay == 0) {
            Toast.makeText(getContext(), "Please select a valid date", Toast.LENGTH_SHORT).show();
            dateErrorText.setVisibility(View.VISIBLE);
            return true;
        }

        if(selectedHour == 0 || selectedMinute == 0){
            Toast.makeText(getContext(), "Please select a valid time", Toast.LENGTH_SHORT).show();
            timeErrorText.setVisibility(View.VISIBLE);
            return true;
        } return false;
    }

    private void handleConfirmationButtonClick() {
        if(isInputsNotValid()) return;

        // Sample values for AppointmentDto
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
}
