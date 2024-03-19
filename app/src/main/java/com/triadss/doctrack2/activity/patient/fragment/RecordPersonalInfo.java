package com.triadss.doctrack2.activity.patient.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.repoositories.PatientRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordPersonalInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordPersonalInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView fullNameValue,
             ageValue,
             genderValue,
             addressValue,
             statusValue,
             phoneValue,
             courseValue,
             yearValue;

    public RecordPersonalInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordPersonalInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordPersonalInfo newInstance(String param1, String param2) {
        RecordPersonalInfo fragment = new RecordPersonalInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View rootView = inflater.inflate(R.layout.fragment_record_personal_info, container, false);

        TextView patientId = rootView.findViewById(R.id.value_patientID);
        TextView patientName = rootView.findViewById(R.id.value_Name);
        TextView patientEmail = rootView.findViewById(R.id.value_Email);
        TextView patientAddress = rootView.findViewById(R.id.value_Address);
        TextView patientAge = rootView.findViewById(R.id.value_Age);
        TextView patientPhone = rootView.findViewById(R.id.value_ContactNo);
        TextView patientCourse = rootView.findViewById(R.id.value_Course);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String patientUid = currentUser.getUid();

        PatientRepository patientRepository = new PatientRepository();
        patientRepository.getPatient(patientUid, new PatientRepository.PatientFetchCallback() {
            @Override
            public void onSuccess(AddPatientDto patient) {
                patientId.setText(patient.getIdNumber());
                patientName.setText(patient.getFullName());
                patientEmail.setText(patient.getEmail());
                patientAddress.setText(patient.getAddress());
                patientAge.setText(patient.getAge());
                patientPhone.setText(patient.getPhone());
                patientCourse.setText(patient.getCourse());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}