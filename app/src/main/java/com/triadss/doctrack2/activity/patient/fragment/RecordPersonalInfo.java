package com.triadss.doctrack2.activity.patient.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordPersonalInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordPersonalInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView fullNameValue,
             ageValue,
             genderValue,
             addressValue,
             statusValue,
             phoneValue,
             courseValue,
             yearValue;

    public RecordPersonalInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordPersonalInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordPersonalInfo newInstance(String param1, String param2) {
        RecordPersonalInfo fragment = new RecordPersonalInfo();
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
        View rootView = inflater.inflate(R.layout.fragment_record_personal_info, container, false);

        fullNameValue = rootView.findViewById(R.id.fullNameValue);
        ageValue = rootView.findViewById(R.id.ageValue);
        genderValue = rootView.findViewById(R.id.genderValue);
        addressValue = rootView.findViewById(R.id.addressValue);
        statusValue = rootView.findViewById(R.id.statusValue);
        phoneValue = rootView.findViewById(R.id.phoneValue);
        courseValue = rootView.findViewById(R.id.courseValue);
        yearValue = rootView.findViewById(R.id.yearValue);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        update(userId);
        return rootView;
    }

    private void update(String userId)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection(FireStoreCollection.USERS_TABLE)
                                        .document(userId);
        // Fetch the user document
        userRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Retrieve the user role from the document
                            fullNameValue.setText(document.getString(UserModel.fullName));
                            ageValue.setText(document.getDouble(UserModel.age).toString());
                            genderValue.setText(document.getString(UserModel.gender));
                            addressValue.setText(document.getString(UserModel.address));
                            statusValue.setText(document.getString(UserModel.status));
                            phoneValue.setText(document.getString(UserModel.phone));
                            courseValue.setText(document.getString(UserModel.course));
                            yearValue.setText(document.getDouble(UserModel.year).toString());
                        }
                    } else {
                        Toast.makeText(getContext(), "Error fetching user role.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}