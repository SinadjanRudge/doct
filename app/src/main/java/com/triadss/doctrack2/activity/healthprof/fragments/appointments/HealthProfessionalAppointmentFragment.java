package com.triadss.doctrack2.activity.healthprof.fragments.appointments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.R;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.triadss.doctrack2.activity.healthprof.adapters.HealthProfessionalAppointmentFragmentPageAdapter;
import com.triadss.doctrack2.contracts.IListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthProfessionalAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthProfessionalAppointmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    public ViewPager2 viewPager;
    private HealthProfessionalAppointmentFragmentPageAdapter pageAdapter;

    public HealthProfessionalAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthProfessionalAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthProfessionalAppointmentFragment newInstance(String param1, String param2) {
        HealthProfessionalAppointmentFragment fragment = new HealthProfessionalAppointmentFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health_professional_appointment, container, false);

        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);

        pageAdapter = new HealthProfessionalAppointmentFragmentPageAdapter(getActivity().getSupportFragmentManager(), getLifecycle());

        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Tab selected logic here
                if(tab != null)
                {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Tab unselected logic here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Tab reselected logic here
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public  void onPageSelected(int position)
            {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));

                Fragment fragment = getParentFragmentManager().findFragmentByTag("f" + position);
                boolean fragmentIsListView = fragment instanceof IListView;
                boolean fragmentIsNotNull = fragment != null;
                if (fragmentIsNotNull && fragmentIsListView) {
                    ((IListView) fragment).ReloadList();
                }
            }
        });

        return rootView;

    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link addMedicalRecord#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class addMedicalRecord extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public addMedicalRecord() {
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
        public static addMedicalRecord newInstance(String param1, String param2) {
            addMedicalRecord fragment = new addMedicalRecord();
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
            return inflater.inflate(R.layout.fragment_patient_record_add_medical_history, container, false);
        }
    }
}