package com.triadss.doctrack2.activity.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.fragment.AddPatientFragment;


public class PatientAppointment extends Fragment {

    private BottomNavigationView bottomNavigationView, PatientbottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_patient_appointment, container, false);

        bottomNavigationView = rootView.findViewById(R.id.bottomNavigationView);
        PatientbottomNavigationView = rootView.findViewById(R.id.PatientbottomNavigationView);

        PatientbottomNavigationView.setSelectedItemId(R.id.pending);
        PatientbottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.request) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new PatientRequest());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            } else if (itemId == R.id.pending) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new PatientAppointment());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            } else if (itemId == R.id.status) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new PatientStatus());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            }
            return true;
        });
        return rootView;
    }
}