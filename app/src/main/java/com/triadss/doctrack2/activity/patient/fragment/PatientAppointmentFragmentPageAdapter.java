package com.triadss.doctrack2.activity.patient.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PatientAppointmentFragmentPageAdapter extends FragmentStateAdapter {

    public PatientAppointmentFragmentPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AppointmentRequest();
            case 1:
                return new AppointmentPending();
            default:
                return new AppointmentStatus();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
