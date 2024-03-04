package com.triadss.doctrack2.activity.patient.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PatientMedicationFragmentPageAdapter extends FragmentStateAdapter {

    public PatientMedicationFragmentPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PatientMedicationCompletedFragment();
            case 1:
                return new PatientMedicationOngoingFragment();
            default:
                return new PatientMedicationAddFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}