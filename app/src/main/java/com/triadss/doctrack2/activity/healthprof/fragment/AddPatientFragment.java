package com.triadss.doctrack2.activity.healthprof.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.DocTrackErrorMessage;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.enums.UserRole;
import com.triadss.doctrack2.config.model.ReportModel;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.repoositories.PatientRepository;
import com.triadss.doctrack2.utils.DocTrackUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPatientFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextInputEditText editTextEmail, editTextAddress, editTextPhone, editTextAge, editTextCourse, editTextIdNumber, editTextFullName;

    TextInputLayout emailTextInputLayout, idNumberTextInputLayout, fullNameTextInputLayout;

    FirebaseAuth mAuth;

    ProgressBar progressBar;

    Button saveButton;
    PatientRepository _patientRepository;

    public AddPatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPatientFragment newInstance(String param1, String param2) {
        AddPatientFragment fragment = new AddPatientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_patient_save) {
            createPatient();
        }
    }

    /**
     * Called to create the view for this fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to. The fragment should not add the view
     *                           itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _patientRepository = new PatientRepository();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_patient, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.add_patient_toolbar);
        toolbar.setTitle("Add Patient Update");

        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        Viewpager2 viewPager = rootView.findViewById(R.id.viewPager);

        AddPatientFragmentPageAdapter pageAdapter = new RecordFragmentPageAdapter(getActivity().getSupportFragmentManager(), getLifecycle());

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
            }
        });


        // Set the Toolbar as the action bar for the activity
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set click listener for the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click, for example:
                requireActivity().onBackPressed();
            }
        });

        // input field
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = rootView.findViewById(R.id.email);
        editTextAddress = rootView.findViewById(R.id.address);
        editTextPhone = rootView.findViewById(R.id.phone);
        editTextAge = rootView.findViewById(R.id.age);
        editTextCourse = rootView.findViewById(R.id.course);
        editTextIdNumber = rootView.findViewById(R.id.idNumber);
        progressBar = rootView.findViewById(R.id.progressBar);
        saveButton = rootView.findViewById(R.id.add_patient_save);
        editTextFullName = rootView.findViewById(R.id.name);

        // layout field
        emailTextInputLayout = rootView.findViewById(R.id.emailInputLayout);
        fullNameTextInputLayout = rootView.findViewById(R.id.nameInputLayout);
        idNumberTextInputLayout = rootView.findViewById(R.id.idNumberInputLayout);

        saveButton.setOnClickListener(this);

        return rootView;
    }

    /**
     * Validates the input fields of the patient DTO.
     *
     * @param patientDto The DTO (Data Transfer Object) containing patient information.
     * @return {@code true} if the inputs are valid, {@code false} otherwise.
     */
    private boolean validateInputs(AddPatientDto patientDto) {
        boolean isValidated = true;

        if (patientDto.getEmail().isEmpty()) {
            DocTrackUtils.showValidationError("Email is Required", editTextEmail, emailTextInputLayout);
            isValidated = false;
        } else {
            DocTrackUtils.hideValidationError(emailTextInputLayout);
        }

        if (patientDto.getIdNumber().isEmpty()) {
            DocTrackUtils.showValidationError("ID number is Required", editTextIdNumber, idNumberTextInputLayout);
            isValidated = false;
        } else {
            DocTrackUtils.hideValidationError(idNumberTextInputLayout);
        }

        if (patientDto.getIdNumber().isEmpty()) {
            DocTrackUtils.showValidationError("Full is Required", editTextFullName, fullNameTextInputLayout);
            isValidated = false;
        } else {
            DocTrackUtils.hideValidationError(fullNameTextInputLayout);
        }

        return isValidated;
    }

    /**
     * Validates the email input.
     *
     * @param email The email to be validated.
     */
    private void validateEmail(String email) {
        if (email.isEmpty()) {
            DocTrackUtils.showValidationError("Email is required", editTextEmail, emailTextInputLayout);
        } else {
            DocTrackUtils.hideValidationError(emailTextInputLayout);
        }
    }

    /**
     * Creates a new patient by registering a user with FirebaseAuth and saving their information to Firestore.
     * It also displays appropriate toast messages based on the success or failure of the registration process.
     */
    private void createPatient() {
        progressBar.setVisibility(View.VISIBLE);
        AddPatientDto patientDto = new AddPatientDto();
        patientDto.setEmail(String.valueOf(editTextEmail.getText()).trim());
        patientDto.setFullName(String.valueOf(editTextFullName.getText()).trim());
        patientDto.setAddress(String.valueOf(editTextAddress.getText()).trim());
        patientDto.setPhone(String.valueOf(editTextPhone.getText()).trim());
        patientDto.setAge(String.valueOf(editTextAge.getText()));
        patientDto.setCourse(String.valueOf(editTextCourse.getText()).trim());
        patientDto.setCourse(String.valueOf(editTextCourse.getText()).trim());
        patientDto.setIdNumber(String.valueOf(editTextIdNumber.getText()).trim());

        if (validateInputs(patientDto)) {
            try {
                FirebaseAuth newAuth = FirebaseAuth.getInstance();
                newAuth.createUserWithEmailAndPassword(patientDto.getEmail(), patientDto.getIdNumber()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        try {
                            if (user != null) {
                                // create details in user table and report table
                                _patientRepository.AddPatient(user.getUid(), patientDto);
                                createReport(user.getUid(), patientDto);
                                Toast.makeText(getContext(), "Patient Created", Toast.LENGTH_SHORT).show();
                                @SuppressLint("CommitTransaction")
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                                        .beginTransaction();
                                transaction.replace(R.id.frame_layout, new PatientFragment());
                                // Add HomeFragment to the back stack with a tag
                                transaction.addToBackStack("tag_for_home_fragment");

                                transaction.commit();
                            }
                        } catch (Exception e) {
                            // DELETE newly created user when there is something wrong with saveUserToFireStore()
                            if (newAuth.getCurrentUser() != null) {
                                newAuth.getCurrentUser().delete();
                                Toast.makeText(getContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(getContext(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                // GENERIC ERROR HANDLER
                Toast.makeText(getContext(), DocTrackErrorMessage.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);

        }

    }

    /**
     * Creates a report for a patient and saves it to Firestore.
     *
     * @param userId     The ID of the user for whom the report is created.
     * @param patientDto The DTO (Data Transfer Object) containing patient information.
     */
    private void createReport(String userId, AddPatientDto patientDto) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference recordRef = db.collection(FireStoreCollection.REPORTS_TABLE).document(userId);
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
        String dateNow = currentDate.format(formatter);

        Map<String, Object> recordData = new HashMap<>();
        recordData.put(ReportModel.action, "ADD");
        recordData.put(ReportModel.message, String.format("Patient id:%s name:%s has been created", userId, patientDto.getFullName()));
        recordData.put(ReportModel.idNumber, patientDto.getIdNumber());
        recordData.put(ReportModel.createdDate, dateNow);
        recordData.put(ReportModel.updatedDate, dateNow);

        // Check if mAuth and mContext are not null
        if (mAuth != null && mAuth.getCurrentUser() != null && getContext() != null) {
            recordRef.set(recordData, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        // Show success message if needed
                        Toast.makeText(getContext(), "Report document added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Show error message
                        Toast.makeText(getContext(), "Failed to add report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Failed to add report: ", Toast.LENGTH_SHORT).show();
        }
    }

}