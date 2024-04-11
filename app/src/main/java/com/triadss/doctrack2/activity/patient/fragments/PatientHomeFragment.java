package com.triadss.doctrack2.activity.patient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.activity.patient.adapters.PatientHomeAppointmentAdapter;
import com.triadss.doctrack2.activity.patient.adapters.PatientHomeMedicationAdapter;
import com.triadss.doctrack2.activity.patient.fragments.records.RecordFragment;
import com.triadss.doctrack2.config.constants.MedicationTypeConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.MedicationRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientHomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MedicationRepository medicationRepository = new MedicationRepository();
    private AppointmentRepository appointmentRepository = new AppointmentRepository();
    private RecyclerView medicationRecyclerView;
    private RecyclerView appointmentRecyclerView;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public PatientHomeFragment(){
        //Required empty constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientHomeFragment newInstance(String param1, String param2) {
        PatientHomeFragment fragment = new PatientHomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_patient_home_page, container, false);

        medicationRecyclerView = rootview.findViewById(R.id.recyclerView_medication);
        appointmentRecyclerView = rootview.findViewById(R.id.recyclerView_pendingAppointment);

        ImageView menuImageView = rootview.findViewById(R.id.menu);
        menuImageView.setOnClickListener(v -> showPopupMenu(v));

        loadMedications();
        loadAppointments();

        return rootview;
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_item1) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.inflate(R.menu.patient_popup_menu);
        popupMenu.show();
    }



    public void loadMedications()
    {
        medicationRepository.getAllMedications(MedicationTypeConstants.ONGOING, new MedicationRepository.MedicationFetchCallback() {
            @Override
            public void onSuccess(List<MedicationDto> medications) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                medicationRecyclerView.setLayoutManager(linearLayoutManager);

                PatientHomeMedicationAdapter adapter = new PatientHomeMedicationAdapter(getContext(), (ArrayList)medications);
                medicationRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void loadAppointments()
    {
        appointmentRepository.getAllPatientPendingAppointments(currentUser.getUid(), new AppointmentRepository.AppointmentPatientPendingFetchCallback() {
            @Override
            public void onSuccess(List<AppointmentDto> appointments) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                appointmentRecyclerView.setLayoutManager(linearLayoutManager);

                PatientHomeAppointmentAdapter adapter = new PatientHomeAppointmentAdapter(getContext(), (ArrayList)appointments);
                appointmentRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
