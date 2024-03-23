package com.triadss.doctrack2.activity.patient.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;

public class PatientAppointmentStatus extends Fragment {
    RecyclerView recyclerView;
    private AppointmentRepository appointmentRepository;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentRepository = new AppointmentRepository();
        View rootView = inflater.inflate(R.layout.activity_patient_status, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        CallStatus();
        CallSomething();
        return rootView;
    }

    public void CallStatus() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        appointmentRepository.getAllPatientStatusAppointments(user.getUid(), new AppointmentRepository.AppointmentPatientStatusFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                PatientAppointmentStatusAdapter adapter = new PatientAppointmentStatusAdapter(getContext(), (ArrayList)appointments);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void CallSomething(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int carl = 2;
            public void run() {
                SharedPreferences sh = getActivity().getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                int a = sh.getInt("PatientStatus",9);
                carl = a;
                if(carl == 10){
                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    myEdit.putInt("PatientStatus", Integer.parseInt("0"));

                    myEdit.apply();
                    CallStatus();
                }
                //Toast.makeText(getContext(), Integer.toString(carl), Toast.LENGTH_SHORT).show();
                handler.postDelayed(this,1000);
            }
        }, 1000);
    }
}