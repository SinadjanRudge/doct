package com.triadss.doctrack2.activity.patient.fragments.records;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.fragments.HealthProfHomeFragment;
import com.triadss.doctrack2.activity.patient.adapters.PatientReportAdapter;
import com.triadss.doctrack2.activity.patient.fragments.PatientHomeFragment;
import com.triadss.doctrack2.config.constants.ReportConstants;
import com.triadss.doctrack2.dto.ReportDto;
import com.triadss.doctrack2.repoositories.ReportsRepository;
import com.triadss.doctrack2.utils.FragmentFunctions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ToggleButton appointmentToggle, medicationToggle;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientReportFragment newInstance(String param1, String param2) {
        PatientReportFragment fragment = new PatientReportFragment();
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

    RecyclerView recyclerView;
    private EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient_reports_list, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewReports);
        search = (EditText) rootView.findViewById(R.id.search_bar);

        FloatingActionButton homeBtn = rootView.findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(view -> {
            FragmentFunctions.ChangeFragmentNoStack(requireActivity(), new PatientHomeFragment());
        });

        appointmentToggle = rootView.findViewById(R.id.appointmentToggle);
        medicationToggle = rootView.findViewById(R.id.medicationToggle);

        appointmentToggle.setOnClickListener(view -> {
            reloadList();
        });

        medicationToggle.setOnClickListener(view -> {
            reloadList();
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        ReportsRepository repository = new ReportsRepository();
        repository.getReportsFromUser(user.getUid(), new ReportsRepository.ReportsFetchCallback() {
            @Override
            public void onSuccess(List<ReportDto> reports) {
                PatientReportAdapter pageAdapter = new PatientReportAdapter(getContext(), (ArrayList)reports);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(pageAdapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        search.addTextChangedListener(inputTextWatcher);
        return rootView;
    }


    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            reloadList();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void reloadList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        ReportsRepository repository = new ReportsRepository();

        String[] list = new String[0];
        if(appointmentToggle.isChecked()) {
            int newCount = list.length + ReportConstants.PATIENT_APPOINTMENT_REPORTS.length;
            list = Stream.concat(Arrays.stream(list), Arrays.stream(ReportConstants.PATIENT_APPOINTMENT_REPORTS))
                    .toArray(size -> (String[]) Array.newInstance(String.class,
                            newCount));
        }

        if(medicationToggle.isChecked()) {
            int newCount = list.length + ReportConstants.PATIENT_MEDICATION_REPORTS.length;
            list = Stream.concat(Arrays.stream(list), Arrays.stream(ReportConstants.PATIENT_MEDICATION_REPORTS))
                    .toArray(size -> (String[]) Array.newInstance(String.class,
                            newCount));
        }

        repository.getReportsFromUserFilter(user.getUid(),
            search.getText().toString().toLowerCase(),
            list,
            new ReportsRepository.ReportsFilterCallback() {
                @Override
                public void onSuccess(List<ReportDto> reports) {

                    PatientReportAdapter pageAdapter = new PatientReportAdapter(getContext(), (ArrayList)reports);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(pageAdapter);
                }
                @Override
                public void onError(String errorMessage) {

                }
            });
    }

}