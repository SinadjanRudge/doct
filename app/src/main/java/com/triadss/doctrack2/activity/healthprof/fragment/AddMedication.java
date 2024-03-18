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
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.dto.VitalSignsDto;
import com.triadss.doctrack2.repoositories.MedicalHistoryRepository;
import com.triadss.doctrack2.repoositories.MedicationRepository;
import com.triadss.doctrack2.repoositories.VitalSignsRepository;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String userId;

    public AddMedication() {
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
    public static AddMedication newInstance(String param1, String param2, String userId) {
        AddMedication fragment = new AddMedication();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            userId = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient_record_add_medication, container, false);
        Button nextButton = rootView.findViewById(R.id.nxtButton);

        inputMedicine = rootView.findViewById(R.id.input_medicine);
        inputNote = rootView.findViewById(R.id.input_note);
        addMedicine = rootView.findViewById(R.id.btn_addMedicine);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVitalSigns();
            }
        });
        return rootView;
    }

    private void createMedication(){
        MedicationDto medicationDto = new MedicationDto();

        medicationDto.setMedicine(String.valueOf(inputMedicine.getText()).trim());
        medicationDto.setNote(String.valueOf(inputNote.getText()).trim());

        MedicationRepository medicationRepository = new MedicationRepository();
        medicationRepository.addMedication(medicationDto, new MedicationRepository.MedicationsAddCallback() {
            @Override
            public void onSuccess(String medicationId) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void showVitalSigns() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
        transaction.replace(R.id.frame_layout, AddVitalSigns.newInstance("", "", ""));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}