package com.triadss.doctrack2.activity.healthprof.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.VitalSignsDto;
import com.triadss.doctrack2.repoositories.PatientRepository;
import com.triadss.doctrack2.repoositories.VitalSignsRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateVitalSigns#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateVitalSigns extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PATIENT_UID = "patientUid";

    // TODO: Rename and change types of parameters
    private String patientUid;
    private String vitalSignsUid;

    EditText editBloodPressure, editTemperature, editPulseRate, editOxygenLevel, editWeight, editHeight, editBMI;
    VitalSignsRepository repository = new VitalSignsRepository();

    public UpdateVitalSigns() {
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
    public static UpdateVitalSigns newInstance(String patientUid) {
        UpdateVitalSigns fragment = new UpdateVitalSigns();
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
        View rootView = inflater.inflate(R.layout.fragment_update_vital_signs, container, false);
        Button submit = rootView.findViewById(R.id.updateBtn);

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
                updateVitalSigns();
            }
        });

        populatePersonalInfo();

        return rootView;
    }

    private void populatePersonalInfo()
    {
        repository.getVitalSignOfPatient(patientUid, new VitalSignsRepository.FetchCallback() {

            @Override
            public void onSuccess(VitalSignsDto vitalSigns) {
                editBloodPressure.setText(vitalSigns.getBloodPressure());
                editTemperature.setText(String.valueOf(vitalSigns.getTemperature()));
                editOxygenLevel.setText(String.valueOf(vitalSigns.getOxygenLevel()));
                editPulseRate.setText(String.valueOf(vitalSigns.getPulseRate()));
                editWeight.setText(String.valueOf(vitalSigns.getWeight()));
                editHeight.setText(String.valueOf(vitalSigns.getHeight()));
                editBMI.setText(String.valueOf(vitalSigns.getBMI()));
                vitalSignsUid = vitalSigns.getUid();
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println();
            }
        });
    }

    private void updateVitalSigns()
    {

        VitalSignsDto vitalSignsDto = new VitalSignsDto();
        vitalSignsDto.setBloodPressure(String.valueOf(editBloodPressure.getText()).trim());
        vitalSignsDto.setTemperature(Double.parseDouble(String.valueOf(editTemperature.getText()).trim()));
        vitalSignsDto.setPulseRate(Integer.parseInt(String.valueOf(editPulseRate.getText()).trim()));
        vitalSignsDto.setOxygenLevel(Integer.parseInt(String.valueOf(editOxygenLevel.getText()).trim()));
        vitalSignsDto.setWeight(Double.parseDouble(String.valueOf(editWeight.getText())));
        vitalSignsDto.setHeight(Double.parseDouble(String.valueOf(editHeight.getText())));
        vitalSignsDto.setBMI(Double.parseDouble(String.valueOf(editBMI.getText()).trim()));
        vitalSignsDto.setPatientId(patientUid);
        vitalSignsDto.setUid(vitalSignsUid);

        repository.updateVitalSigns(vitalSignsDto, new VitalSignsRepository.AddUpdateCallback() {
            @Override
            public void onSuccess(String documentId) {
                showViewPatient();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showViewPatient() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
        transaction.replace(R.id.frame_layout, ViewPatientRecordFragment.newInstance(patientUid));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}