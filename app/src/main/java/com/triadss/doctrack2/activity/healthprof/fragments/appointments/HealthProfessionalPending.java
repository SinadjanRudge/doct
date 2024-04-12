package com.triadss.doctrack2.activity.healthprof.fragments.appointments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;

import com.triadss.doctrack2.activity.healthprof.adapters.HealthProfessionalAppointmentPendingAdapter;
import com.triadss.doctrack2.config.constants.SessionConstants;
import com.triadss.doctrack2.contracts.IListView;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthProfessionalPending#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthProfessionalPending extends Fragment implements IListView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ReportsRepository reportsRepository = new ReportsRepository();
    private NotificationRepository notifcationRepository = new NotificationRepository();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String loggedInUserId;

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
        SharedPreferences sharedPref = getContext().getSharedPreferences(SessionConstants.SessionPreferenceKey, Context.MODE_PRIVATE);
        loggedInUserId = sharedPref.getString(SessionConstants.LoggedInUid, "");

        // Inflate the layout for this fragment
        appointmentRepository = new AppointmentRepository();
        View rootView = inflater.inflate(R.layout.fragment_health_professional_pending, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        ReloadList();
        return rootView;
    }

    public void ReloadList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        appointmentRepository.getPendingAppointmentsForHealthProf(currentUser.getUid(), new AppointmentRepository.AppointmentFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                HealthProfessionalAppointmentPendingAdapter adapter = new HealthProfessionalAppointmentPendingAdapter(getContext(), (ArrayList)appointments, 
                    new HealthProfessionalAppointmentPendingAdapter.AppointmentCallback() {
                        @Override
                        public void onRescheduleConfirmed(DateTimeDto dateTime, String appointmentUid) {
                            reportsRepository.addHealthProfRescheduledAppointmentReport(loggedInUserId, appointmentUid, dateTime, new ReportsRepository.ReportCallback() {
                                @Override
                                public void onReportAddedSuccessfully() {
                                    notificationRepository.NotifyRescheduledAppointmentToPatient(appointmentUid, dateTime, new NotificationRepository.NotificationPushedCallback() {
                                        @Override
                                        public void onNotificationDone() {
                                            appointmentRepository.updateAppointmentSchedule(appointmentUid, dateTime, new AppointmentRepository.AppointmentAddCallback() {
                                                @Override
                                                public void onSuccess(String appointmentId) {
                                                    Toast.makeText(getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();
                                                    ReloadList();
                                                }

                                                @Override
                                                public void onError(String errorMessage) {
                                                    Log.e(TAG, "Error updating appointment: " + errorMessage);
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onReportFailed(String errorMessage) {
                                    System.out.println();
                                }
                            });
                        }

                        @Override
                        public void onCancel(String appointmentUid) {
                            appointmentRepository.cancelAppointment(appointmentUid, new AppointmentRepository.AppointmentCancelCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {
                                    notifcationRepository.NotifyCancelledAppointmentToPatient(appointmentId);

                                    Toast.makeText(getContext(), appointmentId + " cancelled", Toast.LENGTH_SHORT).show();
                                    reportsRepository.addHealthProfCancelledAppointmentReport(loggedInUserId, appointmentId, new ReportsRepository.ReportCallback() {
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
                                    Log.e(TAG, "Error deleting appointment: " + errorMessage);
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