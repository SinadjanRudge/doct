package com.triadss.doctrack2.activity.healthprof.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthProfHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthProfHomeFragment extends Fragment {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    AppointmentRepository appointmentRepository = new AppointmentRepository();
    RecyclerView recyclerView;
    TextView pendingAppointmentCountVal;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthProfHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthProfHomeFragment newInstance(String param1, String param2) {
        HealthProfHomeFragment fragment = new HealthProfHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health_professional_home_page, container, false);
        recyclerView= rootView.findViewById(R.id.recycler_view_pending_appointments);
        pendingAppointmentCountVal = rootView.findViewById(R.id.pendingAppointmentCountVal);

        ReloadList();

        return rootView;
    }

    public void ReloadList() {

        appointmentRepository.getPendingAppointmentsForHealthProf(currentUser.getUid(), new AppointmentRepository.AppointmentFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                pendingAppointmentCountVal.setText(String.valueOf(appointments.size()));

                HealthProfessionalAppointmentPendingAdapter adapter = new HealthProfessionalAppointmentPendingAdapter(getContext(), (ArrayList)appointments,
                        new HealthProfessionalAppointmentPendingAdapter.AppointmentCallback() {
                            @Override
                            public void onRescheduleConfirmed(DateTimeDto dateTime, String appointmentUid) {
                                appointmentRepository.updateAppointmentSchedule(appointmentUid, dateTime, new AppointmentRepository.AppointmentAddCallback() {
                                    @Override
                                    public void onSuccess(String appointmentId) {
                                        Toast.makeText(getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();
                                        ReloadList();
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
                                        Toast.makeText(getContext(), appointmentId + " deleted", Toast.LENGTH_SHORT).show();
                                        ReloadList();
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