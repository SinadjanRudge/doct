package com.triadss.doctrack2.activity.patient.fragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.Timestamp;

public class AppointmentRequest extends Fragment {
    private Button mPickDateButton, pickTimeBtn, confirmButton;
    private AppointmentRepository appointmentRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_patient_request, container, false);

        appointmentRepository = new AppointmentRepository();

        // now register the text view and the button with
        // their appropriate IDs
        mPickDateButton = (Button) rootView.findViewById(R.id.datebutton);
        // mShowSelectedDateText = (TextView) findViewById(R.id.showselecteddate);
        pickTimeBtn = (Button) rootView.findViewById(R.id.idBtnPickTime);
        confirmButton = rootView.findViewById(R.id.confirmbutton);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        // mShowSelectedDateText.setText("Selected Date is : " +
                        // materialDatePicker.getHeaderText());
                        mPickDateButton.setText(materialDatePicker.getHeaderText());
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });

        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                    int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                // selectedTimeTV.setText(hourOfDay + ":" + minute);
                                pickTimeBtn.setText("  " + hourOfDay + ":" + minute + "  ");
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sample values for AppointmentDto
                String nameOfRequester = "John Doe";
                String purpose = "General Checkup";

                // Sample Timestamp for date and time of appointment
                Timestamp dateTimeOfAppointment = new Timestamp(new Date(2024 - 1900, 2, 15));
// Sample Time - Assuming the Time class is represented as milliseconds
                long timeOfAppointment = System.currentTimeMillis();

                String status = "Pending";

                AppointmentDto appointment = new AppointmentDto("asdaasfasfasf",
                        nameOfRequester, purpose, dateTimeOfAppointment, timeOfAppointment, status);

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
            }
        });


        return rootView;
    }
    // Add a helper method to convert Date to String
//    private String formatDateToString(Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        return sdf.format(new Date(date.getYear(), date.getMonth(), date.getDay()));
//    }


}