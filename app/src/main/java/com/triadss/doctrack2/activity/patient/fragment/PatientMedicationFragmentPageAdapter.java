package com.triadss.doctrack2.activity.patient.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PatientMedicationFragmentPageAdapter extends FragmentStateAdapter {
    private static final String TAG = "PatientMedicationFragmentPageAdapter";

    public PatientMedicationFragmentPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                Log.e(TAG, "pos: " + position);
                return new PatientMedicationCompletedFragment();
            case 1:
                Log.e(TAG, "pos: " + position);
                return new PatientMedicationOngoingFragment();

            default:
                Log.e(TAG, "pos: " + position);
                return new PatientMedicationAddFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
