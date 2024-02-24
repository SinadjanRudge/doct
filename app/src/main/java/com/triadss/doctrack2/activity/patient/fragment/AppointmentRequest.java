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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class AppointmentRequest extends Fragment {

    private Button mPickDateButton, pickTimeBtn;
//    private BottomNavigationView bottomNavigationView, PatientbottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_patient_request, container, false);

        System.out.println("APPOINTMENT REQUEST");
        // now register the text view and the button with
        // their appropriate IDs
        mPickDateButton = (Button) rootView.findViewById(R.id.datebutton);
      //  mShowSelectedDateText = (TextView) findViewById(R.id.showselecteddate);
        pickTimeBtn = (Button) rootView.findViewById(R.id.idBtnPickTime);
      //  selectedTimeTV = (TextView) findViewById(R.id.idTVSelectedTime);
        // now create instance of the material date picker
        // builder make sure to add the "datePicker" which
        // is normal material date picker which is the first
        // type of the date picker in material design date
        // picker
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
                   // mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.getHeaderText());
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
                              //  selectedTimeTV.setText(hourOfDay + ":" + minute);
                                pickTimeBtn.setText("  " + hourOfDay + ":" + minute + "  ");
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });

//        PatientbottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.request) {
//                @SuppressLint("CommitTransaction")
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
//                        .beginTransaction();
//                transaction.replace(R.id.frame_layout, new AppointmentRequest());
//                // Add HomeFragment to the back stack with a tag
//                transaction.addToBackStack("tag_for_home_fragment");
//
//                transaction.commit();
//            } else if (itemId == R.id.pending) {
//                @SuppressLint("CommitTransaction")
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
//                        .beginTransaction();
//                transaction.replace(R.id.frame_layout, new AppointmentPending());
//                // Add HomeFragment to the back stack with a tag
//                transaction.addToBackStack("tag_for_home_fragment");
//
//                transaction.commit();
//            } else if (itemId == R.id.status) {
//                @SuppressLint("CommitTransaction")
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
//                        .beginTransaction();
//                transaction.replace(R.id.frame_layout, new AppointmentStatus());
//                // Add HomeFragment to the back stack with a tag
//                transaction.addToBackStack("tag_for_home_fragment");
//
//                transaction.commit();
//            }
//            return true;
//        });


        return rootView;
    }
}