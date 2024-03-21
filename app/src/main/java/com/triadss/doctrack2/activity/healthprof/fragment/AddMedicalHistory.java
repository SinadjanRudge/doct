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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMedicalHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMedicalHistory extends Fragment {
    CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6, checkbox7, checkbox8, checkbox9, checkbox10, checkbox11, checkbox12, checkbox13, checkbox14;
    EditText editSpecifyText1, editSpecifyText2, editPrevHospitalization, editTextMenstruation, editTextGravida, editTextAbortion, editTextMenopause;


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

        checkbox1 = rootView.findViewById(R.id.checkbox1);
        checkbox2 = rootView.findViewById(R.id.checkbox2);
        checkbox3 = rootView.findViewById(R.id.checkbox3);
        checkbox4 = rootView.findViewById(R.id.checkbox4);
        checkbox5 = rootView.findViewById(R.id.checkbox5);
        checkbox6 = rootView.findViewById(R.id.checkbox6);
        checkbox7 = rootView.findViewById(R.id.checkbox7);
        checkbox8 = rootView.findViewById(R.id.cb_famHist_diabetes);
        checkbox9 = rootView.findViewById(R.id.cb_famHist_hyper);
        checkbox10 = rootView.findViewById(R.id.cb_famHist_mentalHealthDisorder);
        checkbox11 = rootView.findViewById(R.id.cb_famHist_asthma);
        checkbox12 = rootView.findViewById(R.id.cb_famHist_bleedingDisorder);
        checkbox13 = rootView.findViewById(R.id.cb_famHist_none);
        checkbox14 = rootView.findViewById(R.id.cb_famHist_others);
        editSpecifyText1 = rootView.findViewById(R.id.editTextOthers);
        editSpecifyText2 = rootView.findViewById(R.id.editText_specify);
        editPrevHospitalization = rootView.findViewById(R.id.textViewPrevHos);
        editTextMenstruation = rootView.findViewById(R.id.editText_menstruation);
        editTextGravida = rootView.findViewById(R.id.editText_gravida);
        editTextAbortion = rootView.findViewById(R.id.editText_abortion);
        editTextMenopause = rootView.findViewById(R.id.editText_menopause);

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

        //Past Illness
        if (checkbox1.isChecked()) {
            pastIllnessBuilder.append(checkbox1.getText()).append(", ");
        }
        if (checkbox2.isChecked()) {
            pastIllnessBuilder.append(checkbox2.getText()).append(", ");
        }
        if (checkbox3.isChecked()) {
            pastIllnessBuilder.append(checkbox3.getText()).append(", ");
        }
        if (checkbox4.isChecked()) {
            pastIllnessBuilder.append(checkbox4.getText()).append(", ");
        }
        if (checkbox5.isChecked()) {
            pastIllnessBuilder.append(checkbox5.getText()).append(", ");
        }
        if (checkbox6.isChecked()) {
            pastIllnessBuilder.append(checkbox6.getText()).append(", ");
        }
        if (checkbox7.isChecked()) {
            pastIllnessBuilder.append(String.valueOf(editSpecifyText1.getText()).trim()).append(", ");
        }
        if (pastIllnessBuilder.length() > 0) {
            pastIllnessBuilder.setLength(pastIllnessBuilder.length() - 2);
        }
        medicalHistoryDto.setPastIllness(pastIllnessBuilder.toString());

        //Previous Hospitalization
        medicalHistoryDto.setPrevOperation(String.valueOf(editPrevHospitalization.getText()).trim());

        //Family History
        if (checkbox8.isChecked()) {
            familyHistoryBuilder.append(checkbox8.getText()).append(", ");
        }
        if (checkbox9.isChecked()) {
            familyHistoryBuilder.append(checkbox9.getText()).append(", ");
        }
        if (checkbox10.isChecked()) {
            familyHistoryBuilder.append(checkbox10.getText()).append(", ");
        }
        if (checkbox11.isChecked()) {
            familyHistoryBuilder.append(checkbox11.getText()).append(", ");
        }
        if (checkbox12.isChecked()) {
            familyHistoryBuilder.append(checkbox12.getText()).append(", ");
        }
        if (checkbox13.isChecked()) {
            familyHistoryBuilder.append(checkbox13.getText()).append(", ");
        }
        if (checkbox14.isChecked()) {
            familyHistoryBuilder.append(String.valueOf(editSpecifyText2.getText()).trim()).append(", ");
        }
        if (familyHistoryBuilder.length() > 0) {
            familyHistoryBuilder.setLength(familyHistoryBuilder.length() - 2);
        }
        medicalHistoryDto.setFamilyHist(familyHistoryBuilder.toString());

        //ObGyne History
        if (!editTextMenstruation.getText().toString().isEmpty()) {
            obgyneHistoryBuilder.append("Menstruation: ").append(editTextMenstruation.getText().toString()).append(", ");
        }
        if (!editTextGravida.getText().toString().isEmpty()) {
            obgyneHistoryBuilder.append("Gravida: ").append(editTextGravida.getText().toString()).append(", ");
        }
        if (!editTextAbortion.getText().toString().isEmpty()) {
            obgyneHistoryBuilder.append("Abortion: ").append(editTextAbortion.getText().toString()).append(", ");
        }
        if (!editTextMenopause.getText().toString().isEmpty()) {
            obgyneHistoryBuilder.append("Menopause: ").append(editTextMenopause.getText().toString()).append(", ");
        }
        if (obgyneHistoryBuilder.length() > 0) {
            obgyneHistoryBuilder.setLength(obgyneHistoryBuilder.length() - 2);
        }
        medicalHistoryDto.setObgyneHist(obgyneHistoryBuilder.toString());

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
        transaction.addToBackStack(null);
        transaction.commit();
    }
}