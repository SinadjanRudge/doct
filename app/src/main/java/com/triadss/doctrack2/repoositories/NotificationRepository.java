package com.triadss.doctrack2.repoositories;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.NotificationModel;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.NotificationDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRepository {
    String TAG = "NotificationRepository";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    //String userId = currentUser.getUid();
    private FirebaseUser user = mAuth.getCurrentUser();
    NotificationDTO notification = new NotificationDTO();
    List<NotificationDTO> notificationList = new ArrayList<NotificationDTO>();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference notifyCollection = firestore
            .collection(FireStoreCollection.NOTIFICATION_TABLE);
    private AppointmentRepository appointmentRepository = new AppointmentRepository();

    public NotificationRepository()
    {

    }
    public void fetchUserNotification(String userId, FetchNotificationAddCallback callback) {
        //this.userId = userId;
        if(!userId.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("notifications")
                    .whereEqualTo("receiver", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                notification = new NotificationDTO();
                                notification.setReciver(document.getString(NotificationModel.receiver));
                                notification.setContent(document.getString(NotificationModel.content));
                                notification.setDateSent(document.getTimestamp(NotificationModel.dateSent));
                                notification.setTitle(document.getString(NotificationModel.title));
                                notificationList.add(notification);
                            }
                            callback.onSuccess(notificationList);
                        }
                    })
                    .addOnFailureListener(e->{
                        Log.d(TAG,"Fetch Notification Error on " + userId);
                        callback.onError(e.getMessage());
                    });
        }
    }

    public void NotifyRejectedAppointment(String appointmentId)
    {
        appointmentRepository.getAppointment(appointmentId, new AppointmentRepository.AppointmentDataFetchCallback() {
            @Override
            public void onSuccess(AppointmentDto appointment) {
                NotificationDTO notifyDto = new NotificationDTO();
                notifyDto.setTitle("Appointment Request Rejection");
                notifyDto.setContent("Appointment Request on " + DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment()).ToString() + " was rejected");
                notifyDto.setDateSent(DateTimeDto.GetCurrentTimeStamp());
                notifyDto.setReciver(appointment.getPatientId());
                pushUserNotification(notifyDto, new NotificationRepository.NotificationAddCallback() {
                    @Override
                    public void onSuccess(String appointmentId) {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void NotifyAcceptedAppointment(String appointmentId)
    {
        appointmentRepository.getAppointment(appointmentId, new AppointmentRepository.AppointmentDataFetchCallback() {
            @Override
            public void onSuccess(AppointmentDto appointment) {
                NotificationDTO notifyDto = new NotificationDTO();
                notifyDto.setTitle("Appointment Request Accepted");
                notifyDto.setContent("Appointment Request on " + DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment()).ToString() + " was accepted");
                notifyDto.setDateSent(DateTimeDto.GetCurrentTimeStamp());
                notifyDto.setReciver(appointment.getPatientId());
                pushUserNotification(notifyDto, new NotificationRepository.NotificationAddCallback() {
                    @Override
                    public void onSuccess(String appointmentId) {
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
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
            notifyMap.put(NotificationModel.dateSent,notifyDto.getDateSent());
            notifyCollection
                    .add(notifyMap)
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
    public  interface FetchNotificationAddCallback
    {
        void onSuccess(List<NotificationDTO> notificationList);

        void onError(String errorMessage);
    }

}