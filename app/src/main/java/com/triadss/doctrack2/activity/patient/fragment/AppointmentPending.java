package com.triadss.doctrack2.activity.patient.fragment;

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
        return rootView;
    }

}