package com.triadss.doctrack2.activity.healthprof.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.triadss.doctrack2.activity.healthprof.HealthProfessionalPending;
import com.triadss.doctrack2.activity.healthprof.HealthProfessionalStatus;
import com.triadss.doctrack2.activity.healthprof.HealthProfessionalUpcoming;

public class HealthProfessionalAppointmentFragmentPageAdapter extends FragmentStateAdapter {

    public HealthProfessionalAppointmentFragmentPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HealthProfessionalUpcoming();
            case 1:
                return new HealthProfessionalPending();
            case 2:
                return new HealthProfessionalStatus();
            default:
                return new HealthProfessionalUpcoming();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}