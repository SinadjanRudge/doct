package com.triadss.doctrack2.repoositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.triadss.doctrack2.config.constants.UserRoleConstants;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.HealthProfDto;

import java.util.ArrayList;
import java.util.List;

public class HealthProfRepository {

    public void getHealthProfList(HealthProListCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .whereEqualTo(UserModel.role, UserRoleConstants.HealthProf)
            .get()
            .addOnCompleteListener(task->{
                if (task.isSuccessful()){
                    List<HealthProfDto> healthProfListDto = new ArrayList<>();

                    for (QueryDocumentSnapshot document: task.getResult()){
                        String role = document.getString(UserModel.role);
                        if (UserRoleConstants.HealthProf.equals(role)) {
                            HealthProfDto patients = document.toObject(HealthProfDto.class);
                            healthProfListDto.add(patients);
                        }
                    }
                    callback.onSuccess(healthProfListDto);
                }
                else {
                    callback.onFailure(task.getException().getMessage());
                }
            });
    }

    public interface HealthProListCallback
    {
        public void onSuccess(List<HealthProfDto> dto);
        public void onFailure(String errorMessage);

    }
}
