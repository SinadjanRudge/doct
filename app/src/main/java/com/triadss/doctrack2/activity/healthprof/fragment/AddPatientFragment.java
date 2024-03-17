package com.triadss.doctrack2.activity.healthprof.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.triadss.doctrack2.config.model.ReportModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.repoositories.PatientRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    EditText editTextEmail, editTextAddress, editTextPhone, editTextAge, editTextCourse, editTextIdNumber, editTextFullName;

    FirebaseAuth mAuth;

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
        View rootView = inflater.inflate(R.layout.fragment_patient_record_add_patient, container, false);
        TextView toolbar = rootView.findViewById(R.id.textview_personal_information);

        // Enable the back button
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // input field
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = rootView.findViewById(R.id.input_Email);
        editTextAddress = rootView.findViewById(R.id.input_address);
        editTextPhone = rootView.findViewById(R.id.input_contactNo);
        editTextAge = rootView.findViewById(R.id.input_Age);
        editTextCourse = rootView.findViewById(R.id.input_course);
        editTextIdNumber = rootView.findViewById(R.id.input_patientID);
        editTextFullName = rootView.findViewById(R.id.input_fullName);

        Button nextButton = rootView.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMedicalHistory();
            }
        });

        return rootView;
    }

    /**
     * Creates a new patient by registering a user with FirebaseAuth and saving their information to Firestore.
     * It also displays appropriate toast messages based on the success or failure of the registration process.
     */
    private void createPatient() {
        AddPatientDto patientDto = new AddPatientDto();
        patientDto.setEmail(String.valueOf(editTextEmail.getText()).trim());
        patientDto.setFullName(String.valueOf(editTextFullName.getText()).trim());
        patientDto.setAddress(String.valueOf(editTextAddress.getText()).trim());
        patientDto.setPhone(String.valueOf(editTextPhone.getText()).trim());
        patientDto.setAge(Integer.parseInt(String.valueOf(editTextAge.getText())));
        patientDto.setCourse(String.valueOf(editTextCourse.getText()).trim());
        patientDto.setCourse(String.valueOf(editTextCourse.getText()).trim());
        patientDto.setIdNumber(String.valueOf(editTextIdNumber.getText()).trim());


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

    private void showMedicalHistory() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        // TODO: Create View Record Fragment for Patient then remove // of the nextline code to use it
        transaction.replace(R.id.frame_layout, AddMedicalHistory.newInstance("", ""));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}