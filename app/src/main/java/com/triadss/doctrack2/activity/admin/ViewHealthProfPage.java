package com.triadss.doctrack2.activity.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.triadss.doctrack2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewHealthProfPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewHealthProfPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewHealthProfPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewHealthProfPage.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewHealthProfPage newInstance(String param1, String param2) {
        ViewHealthProfPage fragment = new ViewHealthProfPage();
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
        View rootView = inflater.inflate(R.layout.fragment_admin_manage_user_accounts_view_health_prof, container, false);
        Button exitBtn = rootView.findViewById(R.id.exitBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
                transaction.replace(R.id.frame_layout, AdminManageUserAccount.newInstance("", ""));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }
}