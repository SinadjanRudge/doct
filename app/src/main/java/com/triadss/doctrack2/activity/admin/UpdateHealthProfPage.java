package com.triadss.doctrack2.activity.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.triadss.doctrack2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateHealthProfPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateHealthProfPage extends Fragment {
    private static final String HEALTHPROF_ID = "healthProfUid";
    String healthProfUid;

    public UpdateHealthProfPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param healthProfUid Parameter 1.
     * @return A new instance of fragment UpdateHealthProfPage.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateHealthProfPage newInstance(String healthProfUid) {
        UpdateHealthProfPage fragment = new UpdateHealthProfPage();
        Bundle args = new Bundle();
        args.putString(HEALTHPROF_ID, healthProfUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            healthProfUid = getArguments().getString(HEALTHPROF_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_manage_user_accounts_update_health_prof, container, false);

        Button updateBtn = rootView.findViewById(R.id.buttonUpdateHealthProf);

        return rootView;
    }
}