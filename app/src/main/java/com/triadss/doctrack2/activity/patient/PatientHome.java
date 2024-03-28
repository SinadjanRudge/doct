package com.triadss.doctrack2.activity.patient;

import static com.triadss.doctrack2.utils.HealthConnectUtils.getHeartRateRecordClass;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.health.connect.client.HealthConnectClient;
import androidx.health.connect.client.PermissionController;
import androidx.health.connect.client.permission.HealthPermission;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.core.DeviceFragment;
import com.triadss.doctrack2.activity.patient.fragment.PatientAppointmentFragment;
import com.triadss.doctrack2.activity.patient.fragment.PatientMedicationFragment;
import com.triadss.doctrack2.activity.patient.fragment.PatientReportFragment;
import com.triadss.doctrack2.activity.patient.fragment.RecordFragment;
import com.triadss.doctrack2.databinding.ActivityPatientHomeBinding;

import java.util.HashSet;
import java.util.Set;

import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;

public class PatientHome extends AppCompatActivity {
    private final String TAG = "PatientHome";
    FirebaseAuth auth;

    Button button;

    FirebaseUser user;

    ActivityPatientHomeBinding binding;
    HealthConnectClient healthConnectClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        button = findViewById(R.id.logout_btn);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        if(isHealthConnectAvailable())
        {
            healthConnectClient = HealthConnectClient.getOrCreate(this);

            checkAuthorization();
        }

        binding = ActivityPatientHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.record_menu) {
                replaceFragment(new RecordFragment());
            }
            else if (item.getItemId() == R.id.temp_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else if(item.getItemId() == R.id.device_menu)
            {
                replaceFragment(new DeviceFragment(healthConnectClient));
            }
            else if (item.getItemId() == R.id.appointment_menu) {
                replaceFragment(new PatientAppointmentFragment());
            } else if (item.getItemId() == R.id.medication_menu) {
                replaceFragment(new PatientMedicationFragment());
            } else if (item.getItemId() == R.id.report_menu) {
                replaceFragment(new PatientReportFragment());
            }
            return true;
        });

    }


    private void checkAuthorization() {
        try {
            Log.d(TAG, "checking authorization");
            ActivityResultContract<Set<String>, Set<String>> requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract();

            ActivityResultLauncher permissionsLauncher = registerForActivityResult(requestPermissionActivityContract, new ActivityResultCallback<Set<String>>() {
                @Override
                public void onActivityResult(Set<String> result) {
                    Log.d(TAG, "got results from authorization request");
                    for (String res : result) {
                        Log.d(TAG, res);
                    }
                    if (result.isEmpty()) {
                        System.out.println();
                    } else {
                        System.out.println();
                    }
                }
            });

            // see https://kt.academy/article/cc-other-languages
            PermissionController pController = healthConnectClient.getPermissionController();
            Set<String> grantedPermissions = BuildersKt.runBlocking(
                    EmptyCoroutineContext.INSTANCE,
                    (s, c) -> pController.getGrantedPermissions(c)
            );

            Set<String> permissionsToRequest = new HashSet<>();
            String perm = HealthPermission.getReadPermission(getHeartRateRecordClass());
            if(!grantedPermissions.contains(perm))
            {
                permissionsToRequest.add(perm);
            }

            permissionsLauncher.launch(permissionsToRequest);


        } catch (InterruptedException ex2) {
            System.out.println();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private boolean isHealthConnectAvailable()
    {
        int availabilityStatus = HealthConnectClient.getSdkStatus(this);
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return false;
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            return false;
        }
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}