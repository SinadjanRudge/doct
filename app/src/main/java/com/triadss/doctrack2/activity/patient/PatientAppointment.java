package com.triadss.doctrack2.activity.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triadss.doctrack2.R;


public class PatientAppointment extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView, PatientbottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        PatientbottomNavigationView = findViewById(R.id.PatientbottomNavigationView);

        PatientbottomNavigationView.setSelectedItemId(R.id.pending);
        PatientbottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.request) {//replaceFragment(new HomeFragment());
                Intent intent = new Intent(PatientAppointment.this, PatientRequest.class);
                startActivity(intent);
            } else if (itemId == R.id.pending) {// replaceFragment(new SubscriptionFormat());

            } else if (itemId == R.id.status) {//  showLogOutConfirmationDialog();
                Intent intent = new Intent(PatientAppointment.this, PatientStatus.class);
                startActivity(intent);
            }
            return true;
        });

    }


}