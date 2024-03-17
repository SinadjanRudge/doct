package com.triadss.doctrack2.activity.healthprof.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.VitalSignsDto;
import com.triadss.doctrack2.repoositories.VitalSignsRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddVitalSigns#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddVitalSigns extends Fragment {

    FirebaseAuth mAuth;
    EditText editBloodPressure, editTemperature, editPulseRate, editOxygenLevel, editWeight, editHeight, editBMI;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddVitalSigns() {
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
    public static AddVitalSigns newInstance(String param1, String param2, String userId) {
        AddVitalSigns fragment = new AddVitalSigns();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient_record_add_vital_signs, container, false);
        Button submit = rootView.findViewById(R.id.submitBtn);
        String userId = getArguments().getString("userId");

        mAuth = FirebaseAuth.getInstance();
        editBloodPressure = rootView.findViewById(R.id.input_bloodPressure);
        editTemperature = rootView.findViewById(R.id.input_temperature);
        editPulseRate = rootView.findViewById(R.id.input_pulseRate);
        editOxygenLevel = rootView.findViewById(R.id.input_spo2);
        editWeight = rootView.findViewById(R.id.input_weight);
        editHeight = rootView.findViewById(R.id.input_height);
        editBMI = rootView.findViewById(R.id.input_bmi);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createVitalSigns(userId);
            }
        });

        return rootView;
    }
    private void createVitalSigns(String userId) {
        VitalSignsDto vitalSignsDto = new VitalSignsDto();
        vitalSignsDto.setBloodPressure(String.valueOf(editBloodPressure.getText()).trim());
        vitalSignsDto.setTemperature(Double.parseDouble(String.valueOf(editTemperature.getText()).trim()));
        vitalSignsDto.setPulseRate(Integer.parseInt(String.valueOf(editPulseRate.getText()).trim()));
        vitalSignsDto.setOxygenLevel(Integer.parseInt(String.valueOf(editOxygenLevel.getText()).trim()));
        vitalSignsDto.setWeight(Integer.parseInt(String.valueOf(editWeight.getText())));
        vitalSignsDto.setHeight(Integer.parseInt(String.valueOf(editHeight.getText()).trim()));
        vitalSignsDto.setBMI(Double.parseDouble(String.valueOf(editBMI.getText()).trim()));

        VitalSignsRepository vitalSignsRepo = new VitalSignsRepository();
        vitalSignsRepo.AddVitalSigns(userId, vitalSignsDto);

    }
}