package com.triadss.doctrack2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.admin.AdminHome;
import com.triadss.doctrack2.activity.healthprof.HealthProfHome;
import com.triadss.doctrack2.activity.patient.PatientHome;
import com.triadss.doctrack2.config.enums.UserRole;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextEmail;

    TextInputEditText editTextPassword;

    Button buttonLogin;

    FirebaseAuth mAuth;

    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            fetchUserRole(currentUser.getUid());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login Successfully",
                                    Toast.LENGTH_SHORT).show();

                            if (user != null) {
                                fetchUserRole(user.getUid());
                            }

                            finish();
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(LoginActivity.this, "Failed To Login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }

    private void fetchUserRole(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a reference to the "users" collection and the specific user document
        DocumentReference userRef = db.collection("users").document(userId);

        // Fetch the user document
        userRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Retrieve the user role from the document
                            String userRoleString = document.getString("role");
                            if (userRoleString != null) {
                                UserRole userRole = UserRole.valueOf(userRoleString);
                                redirectBasedOnUserRole(userRole);
                            }
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error fetching user role.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectBasedOnUserRole(UserRole userRole) {
        switch (userRole) {
            case ADMIN:
                Intent adminIntent = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(adminIntent);
            case PROF:
                Intent profIntent = new Intent(getApplicationContext(), HealthProfHome.class);
                startActivity(profIntent);
                break;
            case PATIENT:
                Intent userIntent = new Intent(getApplicationContext(), PatientHome.class);
                startActivity(userIntent);
                break;
            default:
                Toast.makeText(LoginActivity.this, "Unknown or invalid user role.",
                        Toast.LENGTH_SHORT).show();
        }
    }

}