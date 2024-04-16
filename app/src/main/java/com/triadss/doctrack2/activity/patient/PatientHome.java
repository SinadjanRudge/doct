package com.triadss.doctrack2.activity.patient;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.core.DeviceFragment;
import com.triadss.doctrack2.activity.patient.fragments.appointments.PatientAppointmentFragment;
import com.triadss.doctrack2.activity.patient.fragments.PatientHomeFragment;
import com.triadss.doctrack2.activity.patient.fragments.medications.PatientMedicationFragment;
import com.triadss.doctrack2.activity.patient.fragments.records.PatientReportFragment;
import com.triadss.doctrack2.activity.patient.fragments.records.RecordFragment;
import com.triadss.doctrack2.databinding.ActivityPatientHomeBinding;

public class PatientHome extends AppCompatActivity {

    FirebaseAuth auth;

    Button button;

    FirebaseUser user;

    ActivityPatientHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        replaceFragment(new PatientHomeFragment());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        button = findViewById(R.id.logout_btn);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding = ActivityPatientHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.record_menu) {
                replaceFragment(new RecordFragment());
            }
            else if (item.getItemId() == R.id.temp_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else if(item.getItemId() == R.id.device_menu)
            {
                replaceFragment(new DeviceFragment());
            }
            else if (item.getItemId() == R.id.appointment_menu) {
                replaceFragment(new PatientAppointmentFragment());
            } else if (item.getItemId() == R.id.medication_menu) {
                replaceFragment(new PatientMedicationFragment());
            } else if (item.getItemId() == R.id.report_menu) {
                replaceFragment(new PatientReportFragment());
            }
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Back is pressed... Finishing the activity
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);
                boolean isCurrentlyAtHomepage = currentFragment instanceof PatientHomeFragment;
                if(!isCurrentlyAtHomepage) {
                    fragmentManager.popBackStack();
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);
        boolean isCurrentlyAtHomepage = currentFragment instanceof PatientHomeFragment;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        if(isCurrentlyAtHomepage) {
            fragmentTransaction.addToBackStack("toHome");
        }
        fragmentTransaction.commit();
    }
}