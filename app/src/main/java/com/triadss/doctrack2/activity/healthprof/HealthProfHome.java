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
import com.triadss.doctrack2.activity.healthprof.fragment.AppointmentFragment;
import com.triadss.doctrack2.activity.healthprof.fragment.DeviceFragment;
import com.triadss.doctrack2.activity.healthprof.fragment.PatientFragment;
import com.triadss.doctrack2.activity.healthprof.fragment.RecordFragment;
import com.triadss.doctrack2.databinding.ActivityHealthProfHomeBinding;

public class HealthProfHome extends AppCompatActivity {

    FirebaseAuth auth;

//    Button button;

    FirebaseUser user;

    ActivityHealthProfHomeBinding binding;

//    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthProfHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.appointment_menu) {
                replaceFragment(new AppointmentFragment());
            } else if (item.getItemId() == R.id.device_menu) {
                replaceFragment(new DeviceFragment());
            } else if (item.getItemId() == R.id.patient_menu) {
                replaceFragment(new PatientFragment());
            } else if (item.getItemId() == R.id.record_menu) {
                replaceFragment(new RecordFragment());
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