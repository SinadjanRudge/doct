package com.triadss.doctrack2.activity.healthprof;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.fragment.HealthProfessionalAppointmentPendingAdapter;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthProfessionalPending#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthProfessionalPending extends Fragment implements IListView  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthProfessionalPending() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthProfessionalPending.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthProfessionalPending newInstance(String param1, String param2) {
        HealthProfessionalPending fragment = new HealthProfessionalPending();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appointmentRepository = new AppointmentRepository();
        View rootView = inflater.inflate(R.layout.fragment_health_professional_pending, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        ReloadList();
        return rootView;
    }

    public void ReloadList() {
        appointmentRepository.getAppointmentsByStatus(AppointmentTypeConstants.PENDING, new AppointmentRepository.AppointmentFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                HealthProfessionalAppointmentPendingAdapter adapter = new HealthProfessionalAppointmentPendingAdapter(getContext(), (ArrayList)appointments, 
                    new HealthProfessionalAppointmentPendingAdapter.AppointmentCallback() {
                        @Override
                        public void onRescheduleConfirmed(DateTimeDto dateTime, String appointmentUid) {
                            appointmentRepository.updateAppointmentSchedule(appointmentUid, dateTime, new AppointmentRepository.AppointmentAddCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {
                                    Toast.makeText(context, appointmentId + " updated", Toast.LENGTH_SHORT).show();
                                    CallPending();
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    Log.e(TAG, "Error updating medication: " + errorMessage);
                                }
                            });
                        }

                        @Override
                        public void onCancel(String appointmentUid) {
                            appointmentRepository.deleteAppointment(appointmentUid, new AppointmentRepository.AppointmentAddCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {
                                    Toast.makeText(context, appointmentId + " deleted", Toast.LENGTH_SHORT).show();
                                    CallPending();
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    Log.e(TAG, "Error deleting appointment: " + errorMessage);
                                }
                            });
                        }
                });

                recyclerView.setAdapter(adapter);
            }
        });
    }
}