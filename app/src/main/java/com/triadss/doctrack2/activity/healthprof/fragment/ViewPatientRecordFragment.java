package com.triadss.doctrack2.activity.healthprof.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AddPatientDto;

public class ViewPatientRecordFragment extends Fragment {
    public ViewPatientRecordFragment(){
        //Required empty public constructor
    }
    public static ViewPatientRecordFragment newInstance(AddPatientDto patient) {
        ViewPatientRecordFragment fragment = new ViewPatientRecordFragment();
        Bundle args = new Bundle();
        args.putParcelable("patient", patient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patientrecord_viewrecord, container, false);

        // Retrieve patient data from arguments
        Bundle args = getArguments();
        if (args != null) {
            AddPatientDto patient = args.getParcelable("patient");
            if (patient != null) {
                TextView patientId = rootView.findViewById(R.id.value_patientID);
                TextView patientName = rootView.findViewById(R.id.value_Name);
                //TextView patientEmail = rootView.findViewById(R.id.value_Email);
                TextView patientAddress = rootView.findViewById(R.id.value_Address);
                TextView patientAge = rootView.findViewById(R.id.value_Age);
                TextView patientPhone = rootView.findViewById(R.id.value_ContactNo);
                TextView patientCourse = rootView.findViewById(R.id.value_Course);

                patientId.setText(patient.getIdNumber());
                patientName.setText(patient.getFullName());
                //patientEmail.setText(patient.getEmail());
                patientAddress.setText(patient.getAddress());
                patientAge.setText(patient.getAge());
                patientPhone.setText(patient.getPhone());
                patientCourse.setText(patient.getCourse());

            }
        }

        return rootView;
    }
}
