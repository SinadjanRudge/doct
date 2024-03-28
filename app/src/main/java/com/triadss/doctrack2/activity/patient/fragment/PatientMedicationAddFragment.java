package com.triadss.doctrack2.activity.patient.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.config.constants.MedicationTypeConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.dto.TimeDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.MedicationRepository;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.util.Log;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientMedicationAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientMedicationAddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PatientMedicationAddFragment";

    private DateTimeDto selectedDateTime;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button button_date, button_time, add_button, clear_button;
    private TextInputEditText medicineInput, noteInput;
    private MedicationRepository medicationRepository;

    public PatientMedicationAddFragment() {
        // Required empty public constructor
        selectedDateTime = new DateTimeDto();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientMedicationAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientMedicationAddFragment newInstance(String param1, String param2) {
        PatientMedicationAddFragment fragment = new PatientMedicationAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient_medication_add, container, false);

        medicationRepository = new MedicationRepository();

        button_date = rootView.findViewById(R.id.button_date);
        button_time = rootView.findViewById(R.id.button_time);
        medicineInput = rootView.findViewById(R.id.medicineInput);
        noteInput = rootView.findViewById(R.id.noteInput);
        add_button = rootView.findViewById(R.id.add_button);
        clear_button = rootView.findViewById(R.id.clear_button);

        setupDatePicker();
        setupTimePicker();
        setupClearButton();
        setupConfirmationButton();

        return rootView;
    }

    private void setupDatePicker() {
        // Set up Date Picker Dialog
        button_date.setOnClickListener(new View.OnClickListener() {
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
                        selectedDateTime.setDate(new DateDto(year, monthOfYear, dayOfMonth));

                        // Update the text on the button
                        button_date.setText(selectedDateTime.getDate().ToString());
                    }
                }, year, month, day);

                Calendar calendar = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // Show the Date Picker Dialog
                datePickerDialog.show();
            }
        });
    }

    private void setupTimePicker() {
        // Set up Time Picker Dialog
        button_time.setOnClickListener(new View.OnClickListener() {
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
                            if (!((hourOfDay >= 8 && hourOfDay < 17) || (hourOfDay == 17 && minute == 0)))
                            {
                                Toast.makeText(getActivity(), "Please select a time between 8 AM and 5 PM", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            selectedDateTime.setTime(new TimeDto(hourOfDay, minute));

                            // Update the text on the button
                            button_time.setText(selectedDateTime.getTime().ToString());
                        }
                    }, hour, minute, false);

                // Show the Time Picker Dialog
                timePickerDialog.show();
            }
        });
    }

    private void setupClearButton() {
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClearButtonClick();
            }
        });
    }

    private void setupConfirmationButton() {
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirmation button click
                handleConfirmationButtonClick();
            }
        });
    }

    private void handleClearButtonClick() {
        medicineInput.setText("");
        noteInput.setText("");
        selectedDateTime.setDate(null);
        selectedDateTime.setTime(null);
        button_time.setText("Select Time");
        button_date.setText("Select Date");
    }

    private void handleConfirmationButtonClick() {
        try {
            // Sample values for MedicationDto
            String medicine = medicineInput.getText().toString();
            String note = noteInput.getText().toString();

            Timestamp dateTimeOfAppointment = selectedDateTime.ToTimestamp();

            final String status = MedicationTypeConstants.ONGOING;

            String patientId = FirebaseAuth.getInstance().getUid();

            MedicationDto medication = new MedicationDto("",
                    patientId, medicine, note, dateTimeOfAppointment, status);
            medicationRepository.addMedication(medication, new MedicationRepository.MedicationsAddCallback() {
                @Override
                public void onSuccess(String medicationId) {
                    Log.e(TAG, "Successfully added medication with the id of " + medicationId);

                    ViewPager2 vp = getActivity().findViewById(R.id.viewPager); // Fetch ViewPager instance
                    vp.setCurrentItem(1);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e(TAG, "Failure in adding medication in the document");
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}