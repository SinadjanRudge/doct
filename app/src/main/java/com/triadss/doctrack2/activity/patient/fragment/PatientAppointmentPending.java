package com.triadss.doctrack2.activity.patient.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;

public class PatientAppointmentPending extends Fragment {
    RecyclerView recyclerView;
    private AppointmentRepository appointmentRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appointmentRepository = new AppointmentRepository();

       View rootView = inflater.inflate(R.layout.activity_patient_appointment, container, false);
       recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        CallPending();
        return rootView;
    }

   public void CallPending() {
       appointmentRepository.getAllAppointments(new AppointmentRepository.AppointmentFetchCallback() {

           @Override
           public void onSuccess(List<AppointmentDto> appointments) {
               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
               recyclerView.setLayoutManager(linearLayoutManager);

               PatientAppointmentPendingAdapter adapter = new PatientAppointmentPendingAdapter(getContext(), (ArrayList) appointments);

               recyclerView.setAdapter(adapter);
           }

           @Override
           public void onError(String errorMessage) {

           }
       });
    }
}