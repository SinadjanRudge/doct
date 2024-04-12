package com.triadss.doctrack2.activity.healthprof.fragments.medications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.adapters.AddMedicationAdapter;
import com.triadss.doctrack2.activity.healthprof.fragments.vitalsigns.UpdateVitalSigns;
import com.triadss.doctrack2.config.constants.MedicationTypeConstants;
import com.triadss.doctrack2.config.constants.SessionConstants;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.repoositories.MedicationRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

import java.util.ArrayList;
import java.util.List;

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
    String loggedInUserId;
    ReportsRepository _reportsRepository = new ReportsRepository();

    public UpdateMedications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param patientUid Parameter 1.
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
        SharedPreferences sharedPref = getContext().getSharedPreferences(SessionConstants.SessionPreferenceKey, Context.MODE_PRIVATE);
        loggedInUserId = sharedPref.getString(SessionConstants.LoggedInUid, "");

        repository = new MedicationRepository();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_medication_update_list, container, false);
        Button nxtButton = rootView.findViewById(R.id.nextBtn);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        TextView errorMedicationNm = rootView.findViewById(R.id.errorMedicationNm);
        TextView errorNote = rootView.findViewById(R.id.errorNote);
        errorMedicationNm.setVisibility(rootView.INVISIBLE);
        errorNote.setVisibility(rootView.INVISIBLE);

        EditText inputMedicine = rootView.findViewById(R.id.input_medicine);
        EditText inputNote = rootView.findViewById(R.id.input_note);

        Button addMedication = rootView.findViewById(R.id.btn_addMedicine);
        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputMedicine.getText().toString().isEmpty() && !inputNote.getText().toString().isEmpty()){

                    errorMedicationNm.setVisibility(rootView.INVISIBLE);
                    errorNote.setVisibility(rootView.INVISIBLE);

                    MedicationDto dto = new MedicationDto("", patientUid,
                            inputMedicine.getText().toString(),
                            inputNote.getText().toString(),
                            Timestamp.now(),
                            MedicationTypeConstants.ONGOING);

                    ButtonManager.disableButton(addMedication);

                    repository.addMedication(dto, new MedicationRepository.MedicationsAddCallback() {
                        @Override
                        public void onSuccess(String medicationId) {
                            _reportsRepository.addHealthProfPatientAddMedicationReport(loggedInUserId, patientUid, dto, new ReportsRepository.ReportCallback(){

                                @Override
                                public void onReportAddedSuccessfully() {
                                    updateMedicationList();
                                    ButtonManager.enableButton(addMedication);
                                }

                                @Override
                                public void onReportFailed(String errorMessage) {
                                    ButtonManager.enableButton(addMedication);
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            ButtonManager.enableButton(addMedication);
                        }
                    });
                }
                else{
                    if(inputMedicine.getText().toString().isEmpty()) errorMedicationNm.setVisibility(rootView.VISIBLE);
                    else if (!inputMedicine.getText().toString().isEmpty()) errorMedicationNm.setVisibility(rootView.INVISIBLE);
                    if(inputNote.getText().toString().isEmpty()) errorNote.setVisibility(rootView.VISIBLE);
                    else if (!inputNote.getText().toString().isEmpty()) errorNote.setVisibility(rootView.INVISIBLE);
                }
            }
        });

        nxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVitalSigns();
            }
        });

        updateMedicationList();
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
                        _reportsRepository.addHealthProfPatientRemovedMedicationReport(loggedInUserId, userId, medicationId, new ReportsRepository.ReportCallback() {
                            @Override
                            public void onReportAddedSuccessfully() {
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

                            @Override
                            public void onReportFailed(String errorMessage) {

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
        transaction.replace(R.id.frame_layout, UpdateVitalSigns.newInstance(patientUid));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}