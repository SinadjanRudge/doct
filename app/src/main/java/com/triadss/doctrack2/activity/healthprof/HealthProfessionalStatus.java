package com.triadss.doctrack2.activity.healthprof;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.fragment.AddPatientFragment;
import com.triadss.doctrack2.activity.healthprof.fragment.HealthProfessionalAppointmentStatusAdapter;
import com.triadss.doctrack2.activity.healthprof.fragment.HealthProfessionalAppointmentUpcomingAdapter;
import com.triadss.doctrack2.contracts.IListView;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthProfessionalStatus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthProfessionalStatus extends Fragment implements IListView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthProfessionalStatus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthProfessionalStatus.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthProfessionalStatus newInstance(String param1, String param2) {
        HealthProfessionalStatus fragment = new HealthProfessionalStatus();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    RecyclerView recyclerView;
    private AppointmentRepository appointmentRepository;
    private BottomNavigationView bottomNavigationView, PatientbottomNavigationView;

    ArrayList<String> Purpose = new ArrayList<>();
    ArrayList<String> Date = new ArrayList<>();
    ArrayList<String> Time = new ArrayList<>();

    ArrayList<String> Identification = new ArrayList<>();
    ArrayList<String> Name = new ArrayList<>();

    ArrayList<String> Status = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appointmentRepository = new AppointmentRepository();
        View rootView = inflater.inflate(R.layout.fragment_health_professional_status, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        ReloadList();
        return rootView;
    }

    public void ReloadList() {
        appointmentRepository.getAllAppointments(new AppointmentRepository.AppointmentFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                HealthProfessionalAppointmentStatusAdapter adapter = new HealthProfessionalAppointmentStatusAdapter(getContext(), Purpose, Date, Time, Status, Identification, Name);

                for (AppointmentDto a : appointments) {
                    Log.d("AppointRequest Fragment", "Requester's id: " + a.getPatientId());
                    Purpose.add(a.getPurpose());

                    Date.add(a.getDateOfAppointment().toString());
                    Time.add(a.getDateOfAppointment().toString());
                    Identification.add(a.getPatientId().toString());
                    Name.add(a.getNameOfRequester().toString());
                    Status.add("Status:                          " + a.getStatus().toString());
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}