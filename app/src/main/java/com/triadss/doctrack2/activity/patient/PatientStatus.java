package com.triadss.doctrack2.activity.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triadss.doctrack2.R;

public class PatientStatus extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView, PatientbottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_status);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        PatientbottomNavigationView = findViewById(R.id.PatientbottomNavigationView);


        PatientbottomNavigationView.setSelectedItemId(R.id.status);
        PatientbottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.request) {//replaceFragment(new HomeFragment());
                Intent intent = new Intent(PatientStatus.this, PatientRequest.class);
                startActivity(intent);
            } else if (itemId == R.id.pending) {// replaceFragment(new SubscriptionFormat());
                Intent intent = new Intent(PatientStatus.this, PatientAppointment.class);
                startActivity(intent);
            } else if (itemId == R.id.status) {//  showLogOutConfirmationDialog();

            }
            return true;
        });
    }
}