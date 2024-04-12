package com.triadss.doctrack2.activity.healthprof.fragments.appointments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.activity.healthprof.adapters.HealthProfessionalAppointmentUpcomingAdapter;
import com.triadss.doctrack2.config.constants.SessionConstants;
import com.triadss.doctrack2.contracts.IListView;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.helper.ButtonManager;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.NotificationRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthProfessionalUpcoming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthProfessionalUpcoming extends Fragment implements IListView {
    private ReportsRepository reportsRepository = new ReportsRepository();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String loggedInUserId;

    public HealthProfessionalUpcoming() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthProfessionalUpcoming.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthProfessionalUpcoming newInstance(String param1, String param2) {
        HealthProfessionalUpcoming fragment = new HealthProfessionalUpcoming();
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
        SharedPreferences sharedPref = getContext().getSharedPreferences(SessionConstants.SessionPreferenceKey, Context.MODE_PRIVATE);
        loggedInUserId = sharedPref.getString(SessionConstants.LoggedInUid, "");
        
        appointmentRepository = new AppointmentRepository();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health_professional_upcoming, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        ReloadList();
        return rootView;
    }

    public void ReloadList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        NotificationRepository notificationRepository = new NotificationRepository();

        appointmentRepository.getOngoingAppointments(new AppointmentRepository.AppointmentFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                HealthProfessionalAppointmentUpcomingAdapter adapter = new HealthProfessionalAppointmentUpcomingAdapter(getContext(), (ArrayList)appointments, 
                    new HealthProfessionalAppointmentUpcomingAdapter.AppointmentCallback()
                    {
                        @Override
                        public void onAccept(String appointmentUid) {
                            appointmentRepository.acceptAppointment(appointmentUid, currentUser.getUid(), new AppointmentRepository.AppointmentAddCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {
                                    notificationRepository.NotifyAcceptedAppointment(appointmentId);    

                                    reportsRepository.addHealthProfAcceptedAppointmentReport(loggedInUserId, appointmentId, new ReportsRepository.ReportCallback() {
                                        @Override
                                        public void onReportAddedSuccessfully() {
                                            ReloadList();
                                        }

                                        @Override
                                        public void onReportFailed(String errorMessage) {
                                            System.out.println();
                                        }
                                    });
                                }
                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                        }

                        @Override
                        public void onReject(String appointmentUid) {
                            reportsRepository.addHealthProfRejectedAppointmentReport(loggedInUserId, appointmentUid, new ReportsRepository.ReportCallback() {
                                @Override
                                public void onReportAddedSuccessfully() {

                                    notificationRepository.NotifyRejectedAppointment(appointmentUid);

//                                    appointmentRepository.deleteAppointment(appointmentUid, new AppointmentRepository.AppointmentAddCallback() {
//                                        @Override
//                                        public void onSuccess(String appointmentId) {
//
//                                            ReloadList();
//                                        }
//
//                                        @Override
//                                        public void onError(String errorMessage) {
//                                            System.out.println();
//                                        }
//                                    });
                                }

                                @Override
                                public void onReportFailed(String errorMessage) {
                                    System.out.println();
                                }
                            });

                        }
                    });

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}