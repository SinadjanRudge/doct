package com.triadss.doctrack2.activity.patient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triadss.doctrack2.R;


public class AppointmentPending extends Fragment {

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
                transaction.replace(R.id.frame_layout, new AppointmentRequest());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            } else if (itemId == R.id.pending) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new AppointmentPending());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            } else if (itemId == R.id.status) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new AppointmentStatus());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            }
            return true;
        });
        return rootView;
    }
}