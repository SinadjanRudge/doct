package com.triadss.doctrack2.activity.patient.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.Timestamp;

public class AppointmentRequest extends Fragment {
    private static final String TAG = "AppointmentRequest";
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
        String purpose = textInputPurpose.getText().toString();;

        // THE DATE AND TIME PICKER MUST HAVE VALUES IN THE UI TO MAKE THE ADD APPOINTMENT WORKED!!!
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

        //*********************************************************************************************//
        //! FOR TESTING ADD APPOINTMENT USING THIS SAMPLE APPOINTMENT DTO OBJECT
//        AppointmentDto sampleAddAppointment = new AppointmentDto(
//                "samplePatientId",
//                "John Doe",
//                "Regular Checkup",
//                Timestamp.now(), // Use the current timestamp for testing
//                "Scheduled"
//        );
//
//        appointmentRepository.addAppointment(sampleAddAppointment, new AppointmentRepository.AppointmentAddCallback() {
//            @Override
//            public void onSuccess(String appointmentId) {
//                // Handle success, if needed
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                // Handle error, if needed
//            }
//        });

        //*********************************************************************************************//
        //! FOR TESTING GET ALL APPOINTMENTS
//        appointmentRepository.getAllAppointments(new AppointmentRepository.AppointmentFetchCallback() {
//            @Override
//            public void onSuccess(List<AppointmentDto> appointments, List<String> appointmentIds) {
//                //! VIEW THE VALUES IN THE LOGCAT
//                for (int i = 0; i < appointments.size(); i++) {
//                    AppointmentDto appointment = appointments.get(i);
//                    String appointmentId = appointmentIds.get(i);
//
//                    Log.d("AppointRequest Fragment", "Appointment ID: " + appointmentId);
//                    Log.d("AppointRequest Fragment", "Requester's ID: " + appointment.getPatientId());
//                    Log.d("AppointRequest Fragment", "Name of Requester: " + appointment.getNameOfRequester());
//                    Log.d("AppointRequest Fragment", "Purpose: " + appointment.getPurpose());
//                    Log.d("AppointRequest Fragment", "Date of Appointment: " + appointment.getDateOfAppointment());
//                    Log.d("AppointRequest Fragment", "Status: " + appointment.getStatus());
//                    Log.d("AppointRequest Fragment", "Created At: " + appointment.getCreatedAt());
//                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });


        //****************************************************************************************//
        //! SAMPLE DATA TO TEST UPDATE APPOINTMENT
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//        String sampleAppointmentId = "1pGPWAZ0wxXaET8YuswQ";    // the doc id must exists in the firestore
//        AppointmentDto sampleAppointment = new AppointmentDto(
//                "samplePatientId",
//                "John Doe",
//                "Regular Checkup",
//                Timestamp.now(), // Use the current timestamp for testinupdatedAppointmentg
//                "Scheduled"
//        );
//
//        CollectionReference appointmentsCollection = firestore
//                .collection(FireStoreCollection.APPOINTMENTS_TABLE);
//
//        DocumentReference appointmentRef = appointmentsCollection.document(sampleAppointmentId);
//
//        appointmentRepository.updateAppointment(sampleAppointmentId, sampleAppointment, new AppointmentRepository.UpdateAppointmentCallback() {
//            @Override
//            public void onSuccess() {
//                // Handle success
//                System.out.println("Appointment updated successfully!");
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                // Handle error
//                System.out.println("Error updating appointment: " + errorMessage);
//            }
//        });
        //****************************************************************************************//
        //! SAMPLE DATA TO TEST GET APPOINTMENT
//        String sampleAppointmentId = "0RD49B906MYchHOzSAW9";  // must exists in the firestore
//        appointmentRepository.getAppointment(sampleAppointmentId, new AppointmentRepository.AppointmentFetchOneCallback() {
//            @Override
//            public void onSuccess(List<AppointmentDto> appointments) {
//                // Process the fetched appointments
            //! VIEW THE VALUES IN THE LOGCAT
//                for (AppointmentDto appointment : appointments) {
//                    Log.d(TAG, "Appointment ID: " + sampleAppointmentId);
//                    Log.d(TAG, "Patient ID: " + appointment.getPatientId());
//                    Log.d(TAG, "Name of Requester: " + appointment.getNameOfRequester());
//                    Log.d(TAG, "Purpose: " + appointment.getPurpose());
//                    Log.d(TAG, "Date of Appointment: " + appointment.getDateOfAppointment());
//                    Log.d(TAG, "Status: " + appointment.getStatus());
//                    Log.d(TAG, "Created At: " + appointment.getCreatedAt());
//                    Log.d(TAG, "------------------------");
//                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                // Handle the error
//            }
//        });

        //****************************************************************************************//
        //! SAMPLE DATA TO TEST DELETE APPOINTMENT
//        String sampleAppointmentId = "0RD49B906MYchHOzSAW9"; // must exists in the firestore
//        appointmentRepository.deleteAppointment(sampleAppointmentId, new AppointmentRepository.DeleteAppointmentCallback() {
//            @Override
//            public void onSuccess() {
//                // Handle success
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                // Handle the error
//            }
//        });


    }
}
