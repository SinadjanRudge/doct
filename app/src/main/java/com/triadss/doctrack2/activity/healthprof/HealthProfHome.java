package com.triadss.doctrack2.activity.healthprof;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.activity.healthprof.fragment.HealthProfessionalAppointmentFragment;
import com.triadss.doctrack2.activity.healthprof.fragment.HealthProfessionalReportFragment;
import com.triadss.doctrack2.databinding.ActivityHealthProfHomeBinding;
import com.triadss.doctrack2.activity.healthprof.fragment.AppointmentFragment;
import com.triadss.doctrack2.activity.core.DeviceFragment;
import com.triadss.doctrack2.activity.healthprof.fragment.PatientFragment;
import com.triadss.doctrack2.activity.patient.fragment.RecordFragment;

public class HealthProfHome extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseUser user;

    ActivityHealthProfHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthProfHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Replace the initial fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new AppointmentFragment())
                .commit();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.appointment_menu) {
               // replaceFragment(new AppointmentFragment());
                replaceFragment(new HealthProfessionalAppointmentFragment());
            } else if (item.getItemId() == R.id.device_menu) {
                replaceFragment(new DeviceFragment());
            } else if (item.getItemId() == R.id.patient_menu) {
                replaceFragment(new PatientFragment());
            } else if (item.getItemId() == R.id.record_menu) {
                replaceFragment(new RecordFragment());
            } else if (item.getItemId() == R.id.report_menu) {
                replaceFragment(new HealthProfessionalReportFragment());
            } else if (item.getItemId() == R.id.temp_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}