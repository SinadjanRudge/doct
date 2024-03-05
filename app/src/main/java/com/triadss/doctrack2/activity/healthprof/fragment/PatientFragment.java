package com.triadss.doctrack2.activity.healthprof.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.adapters.PatientFragmentAdapter;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.repoositories.PatientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button addPatient;

    RecyclerView recyclerView;
    FirebaseFirestore db;
    PatientFragmentAdapter adapter;
    List<AddPatientDto> addPatientDtoList;

    EditText editTextSearch;
    List<AddPatientDto> filteredPatients;

    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientFragment newInstance(String param1, String param2) {
        PatientFragment fragment = new PatientFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_patient, container, false);

        addPatient = rootView.findViewById(R.id.patient_add_btn);

        addPatient.setOnClickListener(v -> {
            @SuppressLint("CommitTransaction")
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.frame_layout, new AddPatientFragment());
            // Add HomeFragment to the back stack with a tag
            transaction.addToBackStack("tag_for_home_fragment");

            transaction.commit();
        });

        recyclerView = rootView.findViewById(R.id.recycleview_patient_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*adapter = new PatientFragmentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);*/
        addPatientDtoList = new ArrayList<>();
        filteredPatients = new ArrayList<>();

        adapter = new PatientFragmentAdapter(addPatientDtoList);
        recyclerView.setAdapter(adapter);

        PatientRepository patientRepository = new PatientRepository();
        patientRepository.getPatientList(new PatientRepository.PatientListCallback() {
            @Override
            public void onSuccess(List<AddPatientDto> patients) {
                // Update the adapter with the latest data
                adapter.updateList(patients);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure, e.g., show an error message
                Log.e("Patients", "Error fetching patient list from Firestore. " + errorMessage);
            }
        });

        //Search patient record
        editTextSearch = rootView.findViewById(R.id.search_bar_patient);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filteredPatients.clear();
                if(s.toString().isEmpty()){
                    recyclerView.setAdapter(new PatientFragmentAdapter(addPatientDtoList));
                    adapter.notifyDataSetChanged();
                }
                else {
                    Filter(s.toString());
                }
            }
        });

        return rootView;
    }

    private void Filter(String text){
        for(AddPatientDto patient : addPatientDtoList){
            if(Objects.equals(patient.getIdNumber(), text)){
                filteredPatients.add(patient);
            }
        }
        recyclerView.setAdapter(new PatientFragmentAdapter(filteredPatients));
        adapter.notifyDataSetChanged();
    }

}