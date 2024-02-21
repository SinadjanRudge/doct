package com.triadss.doctrack2.activity.healthprof;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.fragment.AddPatientFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthProfessionalUpcoming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthProfessionalUpcoming extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private BottomNavigationView bottomNavigationView, HealthProfbottomNavigationView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthProfessionalUpcoming() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthProfessionalUpcoming.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthProfessionalUpcoming newInstance(String param1, String param2) {
        HealthProfessionalUpcoming fragment = new HealthProfessionalUpcoming();
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
        View rootView = inflater.inflate(R.layout.fragment_health_professional_upcoming, container, false);
        bottomNavigationView = rootView.findViewById(R.id.bottomNavigationView);
        HealthProfbottomNavigationView = rootView.findViewById(R.id.HealthProfbottomNavigationView);

        HealthProfbottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.upcoming) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new HealthProfessionalUpcoming());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            } else if (itemId == R.id.pending) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new HealthProfessionalPending());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            } else if (itemId == R.id.status) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.frame_layout, new HealthProfessionalStatus());
                // Add HomeFragment to the back stack with a tag
                transaction.addToBackStack("tag_for_home_fragment");

                transaction.commit();
            }
            return true;
        });
        return rootView;
    }
}