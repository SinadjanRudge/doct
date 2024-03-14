package com.triadss.doctrack2.activity.patient.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.repoositories.MedicationRepository;

import com.triadss.doctrack2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordMedication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordMedication extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MedicationRepository medicationRepository = new MedicationRepository();

    public RecordMedication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordMedication.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordMedication newInstance(String param1, String param2) {
        RecordMedication fragment = new RecordMedication();
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
        View rootView = inflater.inflate(R.layout.fragment_record_medication, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        loadMedicationCards(recyclerView);
        return rootView;
    }

    private void loadMedicationCards(RecyclerView recyclerView){
        medicationRepository.getAllMedications("", new MedicationRepository.MedicationFetchCallback() {
            @Override
            public void onSuccess(List<MedicationDto> medications) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                PatientMedicationOngoingAdapter adapter = new PatientMedicationOngoingAdapter(getContext(), (ArrayList<MedicationDto>)medications);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println();
            }
        });
    }
}