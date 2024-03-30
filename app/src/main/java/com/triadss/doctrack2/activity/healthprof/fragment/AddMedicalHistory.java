package com.triadss.doctrack2.activity.healthprof.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.MedicalHistoryDto;
import com.triadss.doctrack2.repoositories.MedicalHistoryRepository;
import com.triadss.doctrack2.utils.CheckboxStringProcessor;
import com.triadss.doctrack2.utils.EditTextName;
import com.triadss.doctrack2.utils.EditTextStringProcessor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMedicalHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMedicalHistory extends Fragment {
    EditText editPrevHospitalization;

    CheckboxStringProcessor pastIllnessProcessor, familyHistoryProcessor;
    EditTextStringProcessor obgyneHistoryProcessor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PATIENT_UID = "patientUid";

    // TODO: Rename and change types of parameters
    String patientUid;

    public AddMedicalHistory() {
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
    public static AddMedicalHistory newInstance(String patientUid) {
        AddMedicalHistory fragment = new AddMedicalHistory();
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
        View rootView = inflater.inflate(R.layout.fragment_patient_record_add_medical_history, container, false);
        Button nextButton = rootView.findViewById(R.id.nxtButton);

        pastIllnessProcessor = new CheckboxStringProcessor(
                rootView.findViewById(R.id.otherPastIllness),
                rootView.findViewById(R.id.otherPastIllnessText),
                rootView.findViewById(R.id.tuborculosis),
                rootView.findViewById(R.id.hypertension),
                rootView.findViewById(R.id.heartDiseases),
                rootView.findViewById(R.id.nervousBreakdown),
                rootView.findViewById(R.id.pelpticUlcer),
                rootView.findViewById(R.id.kidneyDiseases),
                rootView.findViewById(R.id.bronchialAsthma),
                rootView.findViewById(R.id.hernia),
                rootView.findViewById(R.id.seizuresEpilepsy),
                rootView.findViewById(R.id.venerealDiseases),
                rootView.findViewById(R.id.allergicReaction),
                rootView.findViewById(R.id.insomnia)
        );

        familyHistoryProcessor = new CheckboxStringProcessor(
                rootView.findViewById(R.id.cb_famHist_others),
                rootView.findViewById(R.id.otherFamilyHistoryText),
                rootView.findViewById(R.id.cb_famHist_diabetes),
                rootView.findViewById(R.id.cb_famHist_hyper),
                rootView.findViewById(R.id.cb_famHist_mentalHealthDisorder),
                rootView.findViewById(R.id.cb_famHist_asthma),
                rootView.findViewById(R.id.cb_famHist_bleedingDisorder)
        );

        editPrevHospitalization = rootView.findViewById(R.id.input_previous_hospitalization);

        obgyneHistoryProcessor = new EditTextStringProcessor(
            new EditTextName("Menarche",rootView.findViewById(R.id.input_menarche)),
            new EditTextName("LMP",rootView.findViewById(R.id.input_lmp)),
            new EditTextName("Gravida",rootView.findViewById(R.id.input_gravida)),
            new EditTextName("Para",rootView.findViewById(R.id.input_para)),
            new EditTextName("Abortion",rootView.findViewById(R.id.input_abortion)),
            new EditTextName("Menopause",rootView.findViewById(R.id.input_menopause)),
            new EditTextName("PAP Smear",rootView.findViewById(R.id.input_papSmear))
        );

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMedicalHistory(patientUid);
            }
        });
        return rootView;
    }

    private void createMedicalHistory(String userId){
        MedicalHistoryDto medicalHistoryDto= new MedicalHistoryDto();
        StringBuilder pastIllnessBuilder = new StringBuilder();
        StringBuilder familyHistoryBuilder = new StringBuilder();
        StringBuilder obgyneHistoryBuilder = new StringBuilder();

        String pastIllness = pastIllnessProcessor.getString();
        medicalHistoryDto.setPastIllness(pastIllness);

        //Previous Hospitalization
        medicalHistoryDto.setPrevOperation(String.valueOf(editPrevHospitalization.getText()).trim());

        String familyHistory = familyHistoryProcessor.getString();
        medicalHistoryDto.setFamilyHist(familyHistory);

        //ObGyne History
        String obgyneHistory = obgyneHistoryProcessor.getString();
        medicalHistoryDto.setObgyneHist(obgyneHistory);

        MedicalHistoryRepository medicalHistoryRepo = new MedicalHistoryRepository();
        medicalHistoryRepo.AddMedicalHistory(userId, medicalHistoryDto, new MedicalHistoryRepository.AddUpdateCallback() {

            @Override
            public void onSuccess(String medicalHistoryId) {
                showMedication();
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println();
            }
        });

    }

    private void showMedication() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
        transaction.replace(R.id.frame_layout, AddMedication.newInstance(patientUid));
        transaction.commit();
    }
}