package com.triadss.doctrack2.activity.patient.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppointmentStatus extends Fragment {

    RecyclerView recyclerView;
    private AppointmentRepository appointmentRepository;
    private BottomNavigationView bottomNavigationView, PatientbottomNavigationView;

    ArrayList<String> Purpose = new ArrayList<>(Arrays.asList("Data Structure"));

    ArrayList<String> Date = new ArrayList<>();
    ArrayList<String> Time = new ArrayList<>();
    ArrayList<String> Status = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentRepository = new AppointmentRepository();
        View rootView = inflater.inflate(R.layout.activity_patient_status, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        CallStatus();
        return rootView;
    }

    public void CallStatus() {
        appointmentRepository.getAllAppointments(new AppointmentRepository.AppointmentFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments, List<String> appointmentIds) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                PatientAppointmentStatusAdapter adapter = new PatientAppointmentStatusAdapter(getContext(), Purpose, Date, Time, Status);

                for (AppointmentDto a : appointments) {
                    Log.d("AppointRequest Fragment", "Requester's id: " + a.getPatientId());
                    Purpose.add(a.getPurpose());

                    Date.add(a.getDateOfAppointment().toString());
                    Time.add(a.getDateOfAppointment().toString());
                    Status.add("Status:                          " + a.getStatus().toString());
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSuccess(List<AppointmentDto> appointments) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                PatientAppointmentPendingAdapter adapter = new PatientAppointmentPendingAdapter(getContext(), Purpose, Date, Time);

                for (AppointmentDto a : appointments) {
                    Log.d("AppointRequest Fragment", "Requester's id: " + a.getPatientId());
                    Purpose.add(a.getPurpose());

                    Date.add(a.getDateOfAppointment().toString());
                    Time.add(a.getDateOfAppointment().toString());
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}