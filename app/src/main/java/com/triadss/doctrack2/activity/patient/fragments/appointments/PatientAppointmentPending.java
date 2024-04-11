package com.triadss.doctrack2.activity.patient.fragments.appointments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.triadss.doctrack2.activity.patient.adapters.PatientAppointmentPendingAdapter;
import com.triadss.doctrack2.contracts.IListView;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;

public class PatientAppointmentPending extends Fragment implements IListView {
    RecyclerView recyclerView;
    private AppointmentRepository appointmentRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //storage of patient appointment on fire store
        appointmentRepository = new AppointmentRepository();

        View rootView = inflater.inflate(R.layout.fragment_patient_appointment_pending, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        ReloadList();
        CallSomething();
        return rootView;
    }

   public void ReloadList() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

       appointmentRepository.getAllPatientPendingAppointments(currentUser.getUid(), new AppointmentRepository.AppointmentPatientPendingFetchCallback() {
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

                int a = sh.getInt("PatientPending",9);
                carl = a;
                if(carl == 10){
                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    myEdit.putInt("PatientPending", Integer.parseInt("0"));

                    myEdit.apply();
                    ReloadList();

                }
                if (!stop) handler.postDelayed(this,1000);
            }
        }, 1000);
    }
}