package com.triadss.doctrack2.activity.healthprof.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Timestamp;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.adapters.AddMedicationAdapter;
import com.triadss.doctrack2.config.constants.MedicationTypeConstants;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.repoositories.MedicationRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMedication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMedication extends Fragment {
    EditText inputMedicine, inputNote;
    Button addMedicine;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PATIENT_UID = "patientUid";

    // TODO: Rename and change types of parameters
    String patientUid;

    RecyclerView recyclerView;
    MedicationRepository repository;

    public AddMedication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment addMedicalRecord.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMedication newInstance(String userId) {
        AddMedication fragment = new AddMedication();
        Bundle args = new Bundle();
        args.putString(PATIENT_UID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientUid = getArguments().getString(PATIENT_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = new MedicationRepository();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient_record_add_medication, container, false);
        Button nextButton = rootView.findViewById(R.id.nxtButton);
        String userId = getArguments().getString(PATIENT_UID);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        EditText inputMedicine = rootView.findViewById(R.id.input_medicine);
        EditText inputNote = rootView.findViewById(R.id.input_note);

        Button addMedication = rootView.findViewById(R.id.btn_addMedicine);
        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicationDto dto = new MedicationDto("", userId,
                        inputMedicine.getText().toString(),
                        inputNote.getText().toString(),
                        Timestamp.now(),
                        MedicationTypeConstants.ONGOING);

                repository.addMedication(dto, new MedicationRepository.MedicationsAddCallback() {
                    @Override
                    public void onSuccess(String medicationId) {
                        updateMedicationList();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        System.out.println();
                    }
                });
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVitalSigns();
            }
        });
        return rootView;
    }

    private void updateMedicationList()
    {
        String userId = getArguments().getString(PATIENT_UID);

        repository.getAllMedicationsFromUser(userId, new MedicationRepository.MedicationFetchCallback() {

            @Override
            public void onSuccess(List<MedicationDto> medications) {
                AddMedicationAdapter pageAdapter = new AddMedicationAdapter(getContext(),
                        (ArrayList)medications, new AddMedicationAdapter.Callback() {

                    @Override
                    public void onDelete(String medicationId) {
                        repository.deleteMedication(medicationId, new MedicationRepository.MedicationUpdateCallback() {
                            @Override
                            public void onSuccess() {
                                updateMedicationList();
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                    }
                });
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(pageAdapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void showVitalSigns() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
        transaction.replace(R.id.frame_layout, AddVitalSigns.newInstance(patientUid));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}