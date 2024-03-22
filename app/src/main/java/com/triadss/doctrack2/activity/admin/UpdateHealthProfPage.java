package com.triadss.doctrack2.activity.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.HealthProfDto;
import com.triadss.doctrack2.repoositories.HealthProfRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateHealthProfPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateHealthProfPage extends Fragment {
    private static final String TAG = "UpdateHealthProfPage";
    private static final String HEALTHPROF_ID = "healthProfUid";
    HealthProfDto healthProfDto;
    String healthProfUid;
    Button updateBtn;
    EditText editTextPosition;
    TextView textHealthWorkerName, textUserName, textGenderUpdate;

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

        HealthProfRepository repository = new HealthProfRepository();


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_manage_user_accounts_update_health_prof, container, false);

        updateBtn = rootView.findViewById(R.id.buttonUpdateHealthProf);
        editTextPosition = rootView.findViewById(R.id.editTextPosition);
        textHealthWorkerName = rootView.findViewById(R.id.health_worker_name_update);
        textUserName = rootView.findViewById(R.id.health_user_name_update);
        textGenderUpdate = rootView.findViewById(R.id.health_gender_update);

        repository.getHealthProfessional(healthProfUid, new HealthProfRepository.HealthProGetCallback() {
            @Override
            public void onSuccess(HealthProfDto dto) {
                textHealthWorkerName.setText(dto.getFullName());
                textUserName.setText(dto.getUserName());
                textGenderUpdate.setText(dto.getGender());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dto.setPosition(editTextPosition.getText().toString());
                        repository.updateHealthProfessional(dto,  new HealthProfRepository.HealthProUpdateCallback() {
                            @Override
                            public void onSuccess(HealthProfDto dto) {
                                @SuppressLint("CommitTransaction")
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                                        .beginTransaction();
                                transaction.replace(R.id.frame_layout, new AdminManageUserAccount());
                                // Add HomeFragment to the back stack with a tag
                                transaction.addToBackStack(null);

                                transaction.commit();
                            }

                            @Override
                            public void onFailure(String errorMessage) {

                            }
                        });
                    }
                });
            }
            @Override
            public void onFailure(String errorMessage) {
                System.out.println("");
            }
        });
        return rootView;
    }
/*    private void updateHealthProf() {
        // Set up Date Picker Dialog
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConfirmationButtonClick(v);
            }
        });
    }*/
    /*private void handleConfirmationButtonClick(View v) {
        try {
            String Position = editTextPosition.getText().toString();
            HealthProfessionalAdapter healthProfessionalAdapter = new HealthProfessionalAdapter(healthProfUid, healthProfDto);
            healthProfessionalAdapter.updateHealthProfile();

            textHealthWorkerName.setText("");
            *//*healthProfRepository.addHealthProf(healthProfdto,new HealthProfRepository.HealthProAddCallback(){

                @Override
                public void onSuccess(String healthProfId) {
                    Log.e(TAG, "Successfully added medication with the id of " + healthProfdto);
                    //editHWNInput.setText("");
                    editTextPositionInput.setText("");
                    editTextUserNameInput.setText("");
                    editTextPasswordInput.setText("");
                    editTextAppointmentIDInput.setText("");
                    editTextGenderInput.setText("");
                    Toast.makeText(getContext(), "Added Professional Health Account Created", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e(TAG, "Failure in adding medication in the document");
                }
            });*//*
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }*/
}