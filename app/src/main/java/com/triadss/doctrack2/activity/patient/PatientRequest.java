package com.triadss.doctrack2.activity.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.triadss.doctrack2.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.patient.fragment.RecordFragment;
import com.triadss.doctrack2.databinding.ActivityPatientHomeBinding;

import java.util.Calendar;

public class PatientRequest extends AppCompatActivity {

    ActivityPatientHomeBinding binding;
    private Button mPickDateButton, pickTimeBtn;
    private BottomNavigationView bottomNavigationView, PatientbottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_request);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        PatientbottomNavigationView = findViewById(R.id.PatientbottomNavigationView);

        // now register the text view and the button with
        // their appropriate IDs
        mPickDateButton = (Button) findViewById(R.id.datebutton);
      //  mShowSelectedDateText = (TextView) findViewById(R.id.showselecteddate);
        pickTimeBtn = (Button) findViewById(R.id.idBtnPickTime);
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
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(PatientRequest.this,
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

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.record_menu) {
               // replaceFragment(new RecordFragment());

                Intent intent = new Intent(getApplicationContext(), PatientHome.class);
                startActivity(intent);
            }
            else if (item.getItemId() == R.id.temp_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else if (item.getItemId() == R.id.appointment_menu) {

                Intent intent = new Intent(getApplicationContext(), PatientRequest.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        PatientbottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.request) {//replaceFragment(new HomeFragment());

            } else if (itemId == R.id.pending) {// replaceFragment(new SubscriptionFormat());
                Intent intent = new Intent(PatientRequest.this, PatientAppointment.class);
                startActivity(intent);
            } else if (itemId == R.id.status) {//  showLogOutConfirmationDialog();
                Intent intent = new Intent(PatientRequest.this, PatientStatus.class);
                startActivity(intent);
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}