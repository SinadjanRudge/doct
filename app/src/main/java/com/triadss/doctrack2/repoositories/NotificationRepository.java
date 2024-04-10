package com.triadss.doctrack2.repoositories;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.NotificationModel;
import com.triadss.doctrack2.dto.NotificationDTO;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class NotificationRepository {
    String TAG = "NotificationRepository";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //String userId = currentUser.getUid();
    private FirebaseUser user = mAuth.getCurrentUser();
    NotificationDTO notification = new NotificationDTO();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference notifyCollection = firestore
            .collection(FireStoreCollection.NOTIFICATION_TABLE);

    public NotificationRepository()
    {

    }
    public NotificationDTO fetchUserNotification(String userId) {
        //this.userId = userId;
        if(!user.getUid().isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("notifications")
                    .whereEqualTo("receiver", "userId")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                notification.setReciver(document.getString("reciver"));
                                notification.setContent(document.getString("content"));
                                notification.setDataSent((Timestamp) document.get("dataSent"));
                                notification.setTitle(document.getString("tile"));
                            }
                        }
                    });
        }
        return notification;
    }
    public void pushUserNotification(NotificationDTO notifyDto, NotificationAddCallback callback)
    {
        notifyDto.setReciver(user.getUid());
        FirebaseAuth user = FirebaseAuth.getInstance();
        if (!user.getUid().isEmpty()) {
            String notify = user.getUid();
            Map<String, Object> notifyMap = new HashMap<>();
            notifyMap.put(NotificationModel.receiver,notifyDto.getReciver());
            notifyMap.put(NotificationModel.content,notifyDto.getContent());
            notifyMap.put(NotificationModel.title,notifyDto.getTitle());
            notifyMap.put(NotificationModel.dateSent,notifyDto.getDataSent());
            notifyCollection
                    .document(user.getUid())
                    .set(notifyMap)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG,"Notification "+ user.getUid() +" add Successfully");
                        callback.onSuccess(user.getUid());
                    }).addOnFailureListener(e->{
                            Log.d(TAG,"Notification add Failed", e);
                        callback.onError(user.getUid());
                    });
        }else {
            Log.d(TAG,"Notification add Failed");
        }
    }

    public interface NotificationAddCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }

}
