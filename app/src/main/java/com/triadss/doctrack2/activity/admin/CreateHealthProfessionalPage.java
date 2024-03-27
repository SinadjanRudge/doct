package com.triadss.doctrack2.activity.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.HealthProfDto;
import com.triadss.doctrack2.repoositories.HealthProfRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateHealthProfessionalPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateHealthProfessionalPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HealthProfRepository healthProfRepository;
    private static final String TAG = "PatientMedicationAddFragment";
    private Button buttonSubmit;
    TextView errorTextEmail, errorTextHWN, errorTextPosition, errorTextUser, errorTextPassword, errorTextGender;
    View rootView;
    private EditText editTextPositionInput, editTextUserNameInput, editTextPasswordInput, editTextAppointmentIDInput, editTextGenderInput;
    private EditText editTextEmailInput, editTextNameInput;

    public CreateHealthProfessionalPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateHealthProfessionalPage.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateHealthProfessionalPage newInstance(String param1, String param2) {
        CreateHealthProfessionalPage fragment = new CreateHealthProfessionalPage();
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
        rootView =  inflater.inflate(R.layout.fragment_admin_manage_user_accounts_create_health_professional, container, false);

        healthProfRepository = new HealthProfRepository();
        editTextNameInput = rootView.findViewById(R.id.editTextHWN);
        editTextPositionInput = rootView.findViewById(R.id.editTextPosition);
        editTextUserNameInput = rootView.findViewById(R.id.editTextUserName);
        editTextPasswordInput = rootView.findViewById(R.id.editTextPassword);
        editTextGenderInput = rootView.findViewById(R.id.editTextGender);
        editTextEmailInput = rootView.findViewById(R.id.editTextEmail);

        errorTextEmail = rootView.findViewById(R.id.errorTextEmail);
        errorTextHWN = rootView.findViewById(R.id.errorTextHWN);
        errorTextPosition = rootView.findViewById(R.id.errorTextPosition);
        errorTextUser = rootView.findViewById(R.id.errorTextUser);
        errorTextPassword = rootView.findViewById(R.id.errorTextPassword);
        errorTextGender = rootView.findViewById(R.id.errorTextGender);

        errorTextEmail.setVisibility(rootView.GONE);
        errorTextHWN.setVisibility(rootView.GONE);
        errorTextPosition.setVisibility(rootView.GONE);
        errorTextUser.setVisibility(rootView.GONE);
        errorTextPassword.setVisibility(rootView.GONE);
        errorTextGender.setVisibility(rootView.GONE);

        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);

        setupConfirmationButton();
        return rootView;
    }
    private void setupConfirmationButton() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirmation button click
                if(editTextEmailInput.getText().toString().contains("@") && editTextEmailInput.getText().toString().contains(".com") && editTextEmailInput.getText().toString() != "" &&
                editTextNameInput.getText().toString() != "" && editTextPositionInput.getText().toString() != "" && editTextUserNameInput.getText().toString() != "" &&
                editTextPasswordInput.getText().toString() != "" && editTextGenderInput.getText().toString() != "") {
                    handleConfirmationButtonClick();
                }
                else {
                    if(editTextEmailInput.getText().toString().isEmpty()) errorTextEmail.setVisibility(rootView.VISIBLE); else errorTextEmail.setVisibility(rootView.GONE);
                    if(editTextNameInput.getText().toString().isEmpty()) errorTextHWN.setVisibility(rootView.VISIBLE); else errorTextHWN.setVisibility(rootView.GONE);
                    if(editTextPositionInput.getText().toString().isEmpty()) errorTextPosition.setVisibility(rootView.VISIBLE); else errorTextPosition.setVisibility(rootView.GONE);
                    if(editTextUserNameInput.getText().toString().isEmpty()) errorTextUser.setVisibility(rootView.VISIBLE); else errorTextUser.setVisibility(rootView.GONE);
                    if(editTextPasswordInput.getText().toString().isEmpty()) errorTextPassword.setVisibility(rootView.VISIBLE); else errorTextPassword.setVisibility(rootView.GONE);
                    if(editTextGenderInput.getText().toString().isEmpty()) errorTextGender.setVisibility(rootView.VISIBLE); else errorTextGender.setVisibility(rootView.GONE);
                }
            }
        });
    }

    private void handleConfirmationButtonClick() {
        try {
            String Position = editTextPositionInput.getText().toString();
            String UserName = editTextUserNameInput.getText().toString();
            String Password = editTextPasswordInput.getText().toString();
            String Gender = editTextGenderInput.getText().toString();
            String fullName = editTextNameInput.getText().toString();
            String email = editTextEmailInput.getText().toString();

            HealthProfDto healthProfdto = new HealthProfDto(fullName, Position,UserName, email, Password, Gender);
            healthProfRepository.addHealthProf(healthProfdto,new HealthProfRepository.HealthProAddCallback(){

                @Override
                public void onSuccess(String healthProfId) {
                    Log.e(TAG, "Successfully added medication with the id of " + healthProfId);
                    editTextNameInput.setText("");
                    editTextPositionInput.setText("");
                    editTextUserNameInput.setText("");
                    editTextPasswordInput.setText("");
                    editTextGenderInput.setText("");
                    editTextEmailInput.setText("");
                    Toast.makeText(getContext(), "Added Professional Health Account Created", Toast.LENGTH_SHORT).show();

                    getActivity().onBackPressed();
                }

                @Override
                public void onFailure(String errorMessage) {
                        Log.e(TAG, "Failure in adding medication in the document");
                }
            });
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}