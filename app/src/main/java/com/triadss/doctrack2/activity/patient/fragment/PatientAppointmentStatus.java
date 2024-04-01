package com.triadss.doctrack2.activity.patient.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.contracts.IListView;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;

public class PatientAppointmentStatus extends Fragment implements IListView {
    RecyclerView recyclerView;
    private AppointmentRepository appointmentRepository;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentRepository = new AppointmentRepository();
        View rootView = inflater.inflate(R.layout.fragment_patient_appointment_status, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        ReloadList();
        CallSomething();
        return rootView;
    }

    public void ReloadList() {
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
            boolean stop = false;
            public void run() {
                //When you are not in fragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout);
                boolean isCurrentlyAtPatientAppointmentPending = currentFragment instanceof PatientAppointmentPending;
                if(!isCurrentlyAtPatientAppointmentPending) {
                    stop = true;
                }

                SharedPreferences sh = getActivity().getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                int a = sh.getInt("PatientStatus",9);
                carl = a;
                if(carl == 10){
                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    myEdit.putInt("PatientStatus", Integer.parseInt("0"));

                    myEdit.apply();
                    ReloadList();
                }
                //Toast.makeText(getContext(), Integer.toString(carl), Toast.LENGTH_SHORT).show();
                if (!stop) handler.postDelayed(this,1000);
            }
        }, 1000);
    }
}