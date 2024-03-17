package com.triadss.doctrack2.activity.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.fragment.AddVitalSigns;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.HealthProfDto;
import com.triadss.doctrack2.repoositories.HealthProfRepository;
import com.triadss.doctrack2.repoositories.PatientRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminManageUserAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminManageUserAccount extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Context context;
    ArrayList<HealthProfDto> healthProf;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminManageUserAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminManageUserAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminManageUserAccount newInstance(String param1, String param2) {
        AdminManageUserAccount fragment = new AdminManageUserAccount();
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

        HealthProfRepository repository = new HealthProfRepository();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_manage_user_account, container, false);

        repository.getHealthProfList(new HealthProfRepository.HealthProListCallback() {
            @Override
            public void onSuccess(List<HealthProfDto> healthProf) {
                RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewAdmin);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                HealthProfessionalAdapter adapter = new HealthProfessionalAdapter(getContext(),(ArrayList) healthProf, new HealthProfessionalAdapter.Callback() {
                    @Override
                    public void OnViewPressed(String uid) {
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
                        transaction.replace(R.id.frame_layout, ViewHealthProfPage.newInstance("", ""));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void OnUpdatePressed(String uid) {
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
                        transaction.replace(R.id.frame_layout, UpdateHealthProfPage.newInstance("", ""));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        return rootView;
    }
}