package com.triadss.doctrack2.activity.healthprof.fragments.records;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.adapters.ViewMedicationAdapter;
import com.triadss.doctrack2.activity.healthprof.fragments.HealthProfHomeFragment;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.MedicalHistoryDto;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.dto.VitalSignsDto;
import com.triadss.doctrack2.repoositories.MedicalHistoryRepository;
import com.triadss.doctrack2.repoositories.MedicationRepository;
import com.triadss.doctrack2.repoositories.PatientRepository;
import com.triadss.doctrack2.repoositories.VitalSignsRepository;
import com.triadss.doctrack2.utils.CheckboxStringProcessor;
import com.triadss.doctrack2.utils.EditTextStringProcessor;
import com.triadss.doctrack2.utils.FragmentFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        FloatingActionButton homeBtn = rootView.findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(view -> {
            FragmentFunctions.ChangeFragmentNoStack(requireActivity(), new HealthProfHomeFragment());
        });

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
            TextView patientPhone = rootView.findViewById(R.id.value_ContactNo);
            TextView patientCourse = rootView.findViewById(R.id.value_Course);
            TextView patientGender = rootView.findViewById(R.id.value_Gender);
            TextView patientYear = rootView.findViewById(R.id.value_Year);
            TextView patientStatus = rootView.findViewById(R.id.value_Status);
            TextView patientdateofBirth = rootView.findViewById(R.id.value_dateOfBirth);

            patientRepository.getPatient(patientUid, new PatientRepository.PatientFetchCallback(){

                @Override
                public void onSuccess(AddPatientDto patient) {
                    patientId.setText(patient.getIdNumber());
                    patientName.setText(patient.getFullName());
                    patientEmail.setText(patient.getEmail());
                    patientAddress.setText(patient.getAddress());
                    patientPhone.setText(patient.getPhone());
                    patientCourse.setText(patient.getCourse());
                    patientYear.setText(String.valueOf(patient.getYear()));
                    patientGender.setText(patient.getGender());
                    patientStatus.setText(patient.getGender());
                    patientdateofBirth.setText(DateTimeDto.ToDateTimeDto(patient.getDateOfBirth()).ToString());
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
                    String SplitDelimiter = "\\|";

                    List<String> separatedPastIllness = CheckboxStringProcessor.StringToSeparatedValues(SplitDelimiter, medicalHistory.getPastIllness());
                    String pastIllnessString = CheckboxStringProcessor.SeparatedValuesToString(", ", separatedPastIllness);
                    patientPastIllness.setText(pastIllnessString);
                    patientPrevHospitalization.setText(medicalHistory.getPrevOperation());

                    List<String> separatedMedicalHistory = CheckboxStringProcessor.StringToSeparatedValues(SplitDelimiter, medicalHistory.getFamilyHist());
                    String medicalHistoryString = CheckboxStringProcessor.SeparatedValuesToString(", ", separatedMedicalHistory);
                    patientFamilyHistory.setText(medicalHistoryString);

                    List<String> separatedOBGyneHistory = EditTextStringProcessor.StringToSeparatedValues(SplitDelimiter, medicalHistory.getObgyneHist());
                    Map<String, String> mappedOBGyneHistory = EditTextStringProcessor.SeparatedValuesToMap(":", separatedOBGyneHistory);
                    List<String> processedOBGyneHistory = EditTextStringProcessor.MapToSeparatedValues(": ", mappedOBGyneHistory);
                    String obgyneString = EditTextStringProcessor.SeparatedValuesToString("\n", processedOBGyneHistory);
                    patientOBGyneHistory.setText(obgyneString);
                }

                @Override
                public void onError(String message) {
                    System.out.println();
                }
            });

            //Patient Vital Signs
            TextView bloodPressure = rootView.findViewById(R.id.value_bp1);
            TextView temperature = rootView.findViewById(R.id.value_temp1);
            TextView spo2 = rootView.findViewById(R.id.value_spo2_1);
            TextView pulseRate = rootView.findViewById(R.id.value_pulseRate1);
            TextView weight = rootView.findViewById(R.id.value_weight1);
            TextView height = rootView.findViewById(R.id.value_height1);
            TextView BMI = rootView.findViewById(R.id.value_BMI1);
            vitalSignsRepository.getVitalSignOfPatient(patientUid, new VitalSignsRepository.FetchCallback() {
                @Override
                public void onSuccess(VitalSignsDto vitalSigns) {
                    if(vitalSigns != null){
                        bloodPressure.setText(vitalSigns.getBloodPressure());
                        temperature.setText(String.valueOf(vitalSigns.getTemperature()));
                        spo2.setText(String.valueOf(vitalSigns.getOxygenLevel()));
                        pulseRate.setText(String.valueOf(vitalSigns.getPulseRate()));
                        weight.setText(String.valueOf(vitalSigns.getWeight()));
                        height.setText(String.valueOf(vitalSigns.getHeight()));
                        BMI.setText(String.valueOf(vitalSigns.getBMI()));
                    }
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
