package com.triadss.doctrack2.activity.healthprof.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.SessionConstants;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.repoositories.PatientRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdatePersonalInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatePersonalInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PATIENT_UID = "patientUid";

    // TODO: Rename and change types of parameters
    private String patientUid;

    EditText editTextAddress, editTextPhone, editTextAge, editTextCourse,
        editTextEmail, editTextFullname, editTextIdNumber;
    PatientRepository patientRepository = new PatientRepository();
    ReportsRepository _reportsRepository = new ReportsRepository();
    String loggedInUserId;

    public UpdatePersonalInfo() {
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
    public static UpdatePersonalInfo newInstance(String patientUid) {
        UpdatePersonalInfo fragment = new UpdatePersonalInfo();
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

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_update_record, container, false);
        editTextAddress = rootView.findViewById(R.id.input_address);
        editTextPhone = rootView.findViewById(R.id.input_contactNo);
        editTextAge = rootView.findViewById(R.id.input_Age);
        editTextCourse = rootView.findViewById(R.id.input_course);
        editTextEmail = rootView.findViewById(R.id.email);
        editTextFullname = rootView.findViewById(R.id.name);
        editTextIdNumber = rootView.findViewById(R.id.idNumber);

        populatePersonalInfo();

        Button nextButton = rootView.findViewById(R.id.nxtButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePersonalInfo();
                showMedicalHistory();
            }
        });

        return rootView;
    }

    private void populatePersonalInfo()
    {
        patientRepository.getPatient(patientUid, new PatientRepository.PatientFetchCallback(){

            @Override
            public void onSuccess(AddPatientDto patient) {
                editTextEmail.setText(patient.getEmail());
                editTextFullname.setText(patient.getFullName());
                editTextAddress.setText(patient.getAddress());
                editTextPhone.setText(patient.getPhone());
                editTextAge.setText(String.valueOf(patient.getAge()));
                editTextCourse.setText(patient.getCourse());
                editTextIdNumber.setText(patient.getIdNumber());
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println();
            }
        });
    }

    private void updatePersonalInfo()
    {

        AddPatientDto patientDto = new AddPatientDto();
        patientDto.setUid(patientUid);
        patientDto.setFullName(editTextFullname.getText().toString());
        patientDto.setAddress(String.valueOf(editTextAddress.getText()).trim());
        patientDto.setPhone(String.valueOf(editTextPhone.getText()).trim());
        patientDto.setAge(Integer.parseInt(String.valueOf(editTextAge.getText())));
        patientDto.setCourse(String.valueOf(editTextCourse.getText()).trim());
        
        patientRepository.updatePatient(patientDto, new PatientRepository.PatientAddUpdateCallback() {
            @Override
            public void onSuccess(String patientId) {
                _reportsRepository.addHealthProfUpdatePatientInfoReport(loggedInUserId, patientDto, new ReportsRepository.ReportCallback() {
                    @Override
                    public void onReportAddedSuccessfully() {
                        Toast.makeText(requireContext(), "Patient information updated successfully", Toast.LENGTH_SHORT).show();
                        showMedicalHistory();
                    }

                    @Override
                    public void onReportFailed(String errorMessage) {

                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Failed to update patient information: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMedicalHistory() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
        transaction.replace(R.id.frame_layout, UpdateMedicalHistory.newInstance(patientUid));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}