package com.triadss.doctrack2.activity.healthprof.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.triadss.doctrack2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateMedications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateMedications extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PATIENT_UID = "patientUid";

    // TODO: Rename and change types of parameters
    private String patientUid;

    RecyclerView recyclerView;
    MedicationRepository repository;

    public UpdateMedications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addMedicalRecord.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateMedications newInstance(String patientUid) {
        UpdateMedications fragment = new UpdateMedications();
        Bundle args = new Bundle();
        args.putString(PATIENT_UID, patientUid);
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
        View rootView = inflater.inflate(R.layout.fragment_medication_update_list, container, false);
        Button submit = rootView.findViewById(R.id.updateBtn);
        recyclerView = rootView.findViewById(R.id.recyclerView);


        EditText inputMedicine = rootView.findViewById(R.id.input_medicine);
        EditText inputNote = rootView.findViewById(R.id.input_note);

        Button addMedication = rootView.findViewById(R.id.btn_addMedicine);
        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicationDto dto = new MedicationDto("", patientUid,
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

        submit.setOnClickListener(new View.OnClickListener() {
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
        transaction.replace(R.id.frame_layout, UpdateVitalSigns.newInstance("", ""));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}