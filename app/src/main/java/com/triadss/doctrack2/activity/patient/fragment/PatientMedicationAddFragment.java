package com.triadss.doctrack2.activity.patient.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.TimeDto;

import java.util.Calendar;
import java.util.Locale;

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
    private DateDto selectedDate;
    private TimeDto selectedTime;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientMedicationAddFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_patient_medication_add, container, false);
    }

    private void setupDatePicker(Button pickDateButton) {
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
                                selectedDate = new DateDto(year, monthOfYear, dayOfMonth);

                                // Update the text on the button
                                pickDateButton.setText(selectedDate.ToString());
                            }
                        }, year, month, day);

                // Show the Date Picker Dialog
                datePickerDialog.show();
            }
        });
    }

    private void setupTimePicker(Button pickTimeBtn) {
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
                                selectedTime = new TimeDto(hourOfDay, minute);

                                // Update the text on the button
                                pickTimeBtn.setText(selectedTime.ToString());
                            }
                        }, hour, minute, false);

                // Show the Time Picker Dialog
                timePickerDialog.show();
            }
        });
    }
}