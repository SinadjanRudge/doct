package com.triadss.doctrack2.activity.healthprof.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.adapters.ViewMedicationAdapter;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.MedicalHistoryDto;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.dto.VitalSignsDto;
import com.triadss.doctrack2.repoositories.MedicalHistoryRepository;
import com.triadss.doctrack2.repoositories.MedicationRepository;
import com.triadss.doctrack2.repoositories.PatientRepository;
import com.triadss.doctrack2.repoositories.VitalSignsRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewPatientRecordFragment extends Fragment {
    private static final String PATIENT_UID = "patientUid";
    private String patientUid;

    public ViewPatientRecordFragment(){
        //Required empty public constructor
    }
    public static ViewPatientRecordFragment newInstance(String patientUid) {
        ViewPatientRecordFragment fragment = new ViewPatientRecordFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patientrecord_viewrecord, container, false);

        // Retrieve patient data from arguments
        Bundle args = getArguments();
        if (args != null) {
            MedicalHistoryRepository medicalHistoryRepository = new MedicalHistoryRepository();
            VitalSignsRepository vitalSignsRepository = new VitalSignsRepository();
            MedicationRepository medicationRepostory = new MedicationRepository();
            PatientRepository patientRepository = new PatientRepository();

            //Patient Personal Information
            TextView patientId = rootView.findViewById(R.id.value_patientID);
            TextView patientName = rootView.findViewById(R.id.value_Name);
            TextView patientEmail = rootView.findViewById(R.id.value_Email);
            TextView patientAddress = rootView.findViewById(R.id.value_Address);
            TextView patientAge = rootView.findViewById(R.id.value_Age);
            TextView patientPhone = rootView.findViewById(R.id.value_ContactNo);
            TextView patientCourse = rootView.findViewById(R.id.value_Course);
            patientRepository.getPatient(patientUid, new PatientRepository.PatientFetchCallback(){

                @Override
                public void onSuccess(AddPatientDto patient) {
                    patientId.setText(patient.getIdNumber());
                    patientName.setText(patient.getFullName());
                    patientEmail.setText(patient.getEmail());
                    patientAddress.setText(patient.getAddress());
                    patientAge.setText(String.valueOf(patient.getAge()));
                    patientPhone.setText(patient.getPhone());
                    patientCourse.setText(patient.getCourse());
                }

                @Override
                public void onError(String errorMessage) {
                    System.out.println();
                }
            });

            //Patient Medical History
            TextView patientPastIllness = rootView.findViewById(R.id.value_pastIllness);
            TextView patientPrevHospitalization = rootView.findViewById(R.id.value_prevHospitalization);
            TextView patientFamilyHistory = rootView.findViewById(R.id.value_familyHistory);
            TextView patientOBGyneHistory = rootView.findViewById(R.id.value_OBGyneHistory);
            medicalHistoryRepository.getMedicalHistoryOfPatient(patientUid, new MedicalHistoryRepository.FetchCallback() {
                @Override
                public void onSuccess(MedicalHistoryDto medicalHistory) {
                    patientPastIllness.setText(medicalHistory.getPastIllness());
                    patientPrevHospitalization.setText(medicalHistory.getPrevOperation());
                    patientFamilyHistory.setText(medicalHistory.getFamilyHist());
                    patientOBGyneHistory.setText(medicalHistory.getObgyneHist());
                }

                @Override
                public void onError(String message) {
                    System.out.println();
                }
            });

            //Patient Vital Signs
            TextView bloodPressure = rootView.findViewById(R.id.value_bloodPressure);
            TextView temperature = rootView.findViewById(R.id.value_temperature);
            TextView spo2 = rootView.findViewById(R.id.value_SpO2);
            TextView pulseRate = rootView.findViewById(R.id.value_pulseRate);
            TextView weight = rootView.findViewById(R.id.value_weight);
            TextView height = rootView.findViewById(R.id.value_height);
            TextView BMI = rootView.findViewById(R.id.value_BMI);
            vitalSignsRepository.getVitalSignOfPatient(patientUid, new VitalSignsRepository.FetchCallback() {
                @Override
                public void onSuccess(VitalSignsDto vitalSigns) {
                    bloodPressure.setText(vitalSigns.getBloodPressure());
                    temperature.setText(String.valueOf(vitalSigns.getTemperature()));
                    spo2.setText(String.valueOf(vitalSigns.getOxygenLevel()));
                    pulseRate.setText(String.valueOf(vitalSigns.getPulseRate()));
                    weight.setText(String.valueOf(vitalSigns.getWeight()));
                    height.setText(String.valueOf(vitalSigns.getHeight()));
                    BMI.setText(String.valueOf(vitalSigns.getBMI()));
                }

                @Override
                public void onError(String message) {
                    System.out.println();
                }
            });

            //Patient Medication
            RecyclerView medicationRecyclerView = rootView.findViewById(R.id.recyclerView_medication);
            medicationRepostory.getAllMedicationsFromUser(patientUid, new MedicationRepository.MedicationFetchCallback() {
                @Override
                public void onSuccess(List<MedicationDto> medications) {
                    ViewMedicationAdapter viewMedicationAdapter = new ViewMedicationAdapter(getContext(), (ArrayList)medications);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    medicationRecyclerView.setLayoutManager(linearLayoutManager);
                    medicationRecyclerView.setAdapter(viewMedicationAdapter);
                }

                @Override
                public void onError(String message) {
                    System.out.println();
                }
            });
        }

        Button button_update = rootView.findViewById(R.id.button_update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirect
                showPatientRecordsUpdate();
            }
        });

        return rootView;
    }

    private void showPatientRecordsUpdate() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
        transaction.replace(R.id.frame_layout, UpdatePersonalInfo.newInstance(patientUid));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
