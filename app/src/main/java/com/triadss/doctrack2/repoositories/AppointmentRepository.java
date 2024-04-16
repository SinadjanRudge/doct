package com.triadss.doctrack2.repoositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.AppointmentsModel;
import com.triadss.doctrack2.config.model.ReportModel;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.AppointmentDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.ReportDto;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AppointmentRepository {
    private static final String TAG = "AppointmentRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference appointmentsCollection = firestore
            .collection(FireStoreCollection.APPOINTMENTS_TABLE);
    private final CollectionReference reportsCollection = firestore
            .collection(FireStoreCollection.REPORTS_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private PatientRepository patientRepository = new PatientRepository();

    public void addAppointment(AppointmentDto appointment, AppointmentAddCallback callback) {
        if (user != null) {


            appointment.setPatientId(user.getUid());

            FirebaseFirestore.getInstance()
                    .collection(FireStoreCollection.USERS_TABLE)
                    .document(appointment.getPatientId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // * User document found, retrieve full name
                            String fullName = documentSnapshot.getString("fullName");
                            appointment.setNameOfRequester(fullName);

                            appointment.setCreatedAt(new Timestamp(new Date()));

                            // * Proceed with adding the appointment
                            appointmentsCollection
                                    .add(appointment)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "Appointment added with ID: " + documentReference.getId());
                                        callback.onSuccess(documentReference.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error adding appointment", e);
                                        callback.onError(e.getMessage());
                                    });
                        } else {
                            // User document not found, handle this case
                            Log.e(TAG, "User document not found in Firestore");
                            callback.onError("User document not found");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Error fetching user document from Firestore
                        Log.e(TAG, "Error fetching user document from Firestore", e);
                        callback.onError(e.getMessage());
                    });
        }

    }

    public void getAllPatientPendingAppointments(String patientUid, AppointmentPatientPendingFetchCallback callback) {
        patientRepository.getPatientList(new PatientRepository.PatientListCallback() {

            @Override
            public void onSuccess(List<AddPatientDto> patients) {
                Timestamp currentTime = DateTimeDto.GetCurrentTimeStamp();

                appointmentsCollection.orderBy(AppointmentsModel.createdAt, Query.Direction.DESCENDING)
                        .whereGreaterThanOrEqualTo(AppointmentsModel.dateOfAppointment, currentTime)
                        .whereEqualTo(AppointmentsModel.status, AppointmentTypeConstants.PENDING)
                        .whereEqualTo(AppointmentsModel.patientId, patientUid)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<AppointmentDto> appointments = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    AppointmentDto appointment = document.toObject(AppointmentDto.class);
                                    appointment.setDocumentId(document.getId().toString());
                                    appointment.setUid(document.getId().toString());
                                    String idNumber = patients
                                            .stream()
                                            .filter(patient -> patient.getUid().equals(appointment.getPatientId()))
                                            .findFirst().orElse(null).getIdNumber();
                                    appointment.setPatientIdNumber(idNumber);
                                    appointments.add(appointment);
                                }
                                callback.onSuccess(appointments);
                            } else {
                                Log.e(TAG, "Error getting appointments", task.getException());
                                callback.onError(task.getException().getMessage());
                            }
                        });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });


    }
    public void getAllPatientStatusAppointments(String patientUid, AppointmentPatientStatusFetchCallback callback) {
        patientRepository.getPatientList(new PatientRepository.PatientListCallback() {
            @Override
            public void onSuccess(List<AddPatientDto> patients) {
                appointmentsCollection.orderBy(AppointmentsModel.createdAt, Query.Direction.DESCENDING)
                        .whereEqualTo(AppointmentsModel.patientId, patientUid)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<AppointmentDto> appointments = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    AppointmentDto appointment = document.toObject(AppointmentDto.class);
                                    appointment.setDocumentId(document.getId().toString());
                                    String idNumber = patients
                                            .stream()
                                            .filter(patient -> patient.getUid().equals(appointment.getPatientId()))
                                            .findFirst().orElse(null).getIdNumber();
                                    appointment.setPatientIdNumber(idNumber);
                                    if(appointment.getStatus().equals(AppointmentTypeConstants.PENDING) && appointment.getDateOfAppointment().compareTo(DateTimeDto.GetCurrentTimeStamp()) == -1)
                                    {
                                        appointment.setStatus(AppointmentTypeConstants.COMPLETED);
                                    }
                                    if(appointment.getStatus().equals(AppointmentTypeConstants.CANCELLED) || appointment.getStatus().equals(AppointmentTypeConstants.COMPLETED)) {

                                        appointments.add(appointment);
                                    }
                                }
                                callback.onSuccess(appointments);
                            } else {
                                Log.e(TAG, "Error getting appointments", task.getException());
                                callback.onError(task.getException().getMessage());
                            }
                        });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    public void getAppointmentsForHealthProf(String healthProfId, AppointmentFetchCallback callback) {
        patientRepository.getPatientList(new PatientRepository.PatientListCallback() {
            @Override
            public void onSuccess(List<AddPatientDto> patients) {
                appointmentsCollection
                        .where(Filter.or(
                                Filter.equalTo(AppointmentsModel.healthProfId, healthProfId),
                                Filter.equalTo(AppointmentsModel.status, AppointmentTypeConstants.ONGOING)
                        ))
                        .orderBy(AppointmentsModel.createdAt, Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<AppointmentDto> appointments = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    AppointmentDto appointment = document.toObject(AppointmentDto.class);
                                    String idNumber = patients
                                            .stream()
                                            .filter(patient -> patient.getUid().equals(appointment.getPatientId()))
                                            .findFirst().orElse(null).getIdNumber();
                                    appointment.setPatientIdNumber(idNumber);
                                    appointments.add(appointment);
                                }
                                callback.onSuccess(appointments);
                            } else {
                                Log.e(TAG, "Error getting appointments", task.getException());
                                callback.onError(task.getException().getMessage());
                            }
                        });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    public void updateAppointmentSchedule(String appointmentId, DateTimeDto newSchedule, AppointmentAddCallback callback) {
        if(user == null) return;

        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        Timestamp newSchedTimestamp = newSchedule.ToTimestamp();
        String newSchedTimeStamp = DateTimeDto.ToDateTimeDto(newSchedTimestamp).ToString();

        appointmentRef
                .update(AppointmentsModel.dateOfAppointment, newSchedule.ToTimestamp())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment schedule updated successfully");
                    callback.onSuccess(appointmentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating Appointment schedule", e);
                    callback.onError(e.getMessage());
                });
    }

    public void acceptAppointment(String appointmentId, String healthProfId, AppointmentAddCallback callback) {
        if(user == null) return;

        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        appointmentRef
                .update(AppointmentsModel.status, AppointmentTypeConstants.PENDING,
                        AppointmentsModel.healthProfId, healthProfId)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment status updated successfully");
                    callback.onSuccess(appointmentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating Appointment status", e);
                    callback.onError(e.getMessage());
                });
    }

    public void deleteAppointment(String appointmentId, AppointmentAddCallback callback) {
        if(user == null) return;

        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        appointmentRef
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment delete successfully");
                    callback.onSuccess(appointmentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error delete Appointment", e);
                    callback.onError(e.getMessage());
                });
    }

    public void getOngoingAppointments(AppointmentFetchCallback callback)
    {
        if (user != null) {
            patientRepository.getPatientList(new PatientRepository.PatientListCallback() {
                @Override
                public void onSuccess(List<AddPatientDto> patients) {
                    appointmentsCollection
                            .whereEqualTo(AppointmentsModel.status, AppointmentTypeConstants.ONGOING)
                            .orderBy(AppointmentsModel.dateOfAppointment, Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<AppointmentDto> appointments = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    AppointmentDto appointment = document.toObject(AppointmentDto.class);
                                    appointment.setUid(document.getId());
                                    String idNumber = patients
                                            .stream()
                                            .filter(patient -> patient.getUid().equals(appointment.getPatientId()))
                                            .findFirst().orElse(null).getIdNumber();
                                    appointment.setPatientIdNumber(idNumber);

                                    appointments.add(appointment);
                                }
                                callback.onSuccess(appointments);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error fetching medicines", e);
                                callback.onError(e.getMessage());
                            });
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });

        } else {
            Log.e(TAG, "User is null");
            callback.onError("User is null");
        }
    }

    public void getPendingAppointmentsForHealthProf(String healthProfId, AppointmentFetchCallback callback)
    {
        if (user != null) {
            patientRepository.getPatientList(new PatientRepository.PatientListCallback() {
                @Override
                public void onSuccess(List<AddPatientDto> patients) {
                    Timestamp currentTime = DateTimeDto.GetCurrentTimeStamp();
                    appointmentsCollection
                            .whereEqualTo(AppointmentsModel.healthProfId, healthProfId)
                            .whereEqualTo(AppointmentsModel.status, AppointmentTypeConstants.PENDING)
                            .whereGreaterThanOrEqualTo(AppointmentsModel.dateOfAppointment, currentTime)
                            .orderBy(AppointmentsModel.dateOfAppointment, Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<AppointmentDto> appointments = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    AppointmentDto appointment = document.toObject(AppointmentDto.class);
                                    appointment.setUid(document.getId());
                                    appointment.setDocumentId(document.getId());
                                    String idNumber = patients
                                            .stream()
                                            .filter(patient -> patient.getUid().equals(appointment.getPatientId()))
                                            .findFirst().orElse(null).getIdNumber();
                                    appointment.setPatientIdNumber(idNumber);

                                    appointments.add(appointment);
                                }
                                callback.onSuccess(appointments);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error fetching medicines", e);
                                callback.onError(e.getMessage());
                            });
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });


        } else {
            Log.e(TAG, "User is null");
            callback.onError("User is null");
        }
    }

    public void cancelAppointment(String DocumentId, AppointmentCancelCallback callback) {

        appointmentsCollection
                .document(DocumentId)
                .update(AppointmentsModel.status, AppointmentTypeConstants.CANCELLED)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Appointment added with ID: " + DocumentId);
                    callback.onSuccess(DocumentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding appointment", e);
                    callback.onError(e.getMessage());
                });
    }

    public void rescheduleAppointment(String DocumentId,Timestamp date,AppointmentRescheduleCallback callback) {
        String dateTest = DateTimeDto.ToDateTimeDto(date).ToString();
        appointmentsCollection
                .document(DocumentId)
                .update(AppointmentsModel.dateOfAppointment, date)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Appointment added with ID: " + DocumentId);
                    callback.onSuccess(DocumentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding appointment", e);
                    callback.onError(e.getMessage());
                });
    }

    public void getAppointment(String appointmentId, AppointmentDataFetchCallback callback)
    {
        appointmentsCollection
                .document(appointmentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AppointmentDto appointment = documentSnapshot.toObject(AppointmentDto.class);
                        callback.onSuccess(appointment);
                    } else {
                        callback.onError("Appointment not found");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onError(e.getMessage());
                });
    }

    public void addReport(String DocumentId,String action,ReportCallback callback) {

        DocumentReference reportRef = appointmentsCollection.document(DocumentId);

                reportRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            Map<String, Object> data = new HashMap<>();
                            data.put("action", action.toString());
                            data.put("createdBy", document.get("patientId"));
                            data.put("createdDate", document.get("createdAt"));

                            data.put("idNumber", document.getId().toString());
                            data.put("message", "Appointment ID:  " + document.getId() + "  has been  " + action.toString()+"ED");
                            data.put("updatedBy", document.get("patientId").toString());
                            data.put("updatedDate", DateTimeDto.GetCurrentTimeStamp());

                            reportsCollection
                                    .add(data)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "report added with ID: " + DocumentId);
                                        callback.onSuccess(DocumentId);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error adding appointment", e);
                                        callback.onError(e.getMessage());
                                    });
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        callback.onError(task.getException().toString());
                    }
                });
    }

    public void checkAppointmentExists(String goal,Timestamp dateAndTime,int Timeyear,int Timemonth,int Timeday,CheckAppointmentExistFetchCallback callback) {

        DateTimeDto selectedDateTime = new DateTimeDto();
        selectedDateTime.setDate(new DateDto(Timeyear, Timemonth, Timeday));
        selectedDateTime.setTime(new TimeDto(8, 0));

        Timestamp EightToNine = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(9, 0));
        Timestamp NineToTen = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(10, 0));
        Timestamp TenToEleven = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(11, 0));
        Timestamp ElevenToTwelve = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(12, 15));
        Timestamp TwelveToOne = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(13, 15));
        Timestamp OneToTwo = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(14, 0));
        Timestamp TwoToThree = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(15, 0));
        Timestamp ThreeToFour = selectedDateTime.ReschedToTimestamp();  selectedDateTime.setTime(new TimeDto(16, 0));
        Timestamp FourToFive = selectedDateTime.ReschedToTimestamp();

            appointmentsCollection.orderBy("createdAt", Query.Direction.DESCENDING)
                    .whereEqualTo("status", "Ongoing")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        boolean freespaceA = true; boolean freespaceB = true; boolean freespaceC = true; boolean freespaceD = true;
                        boolean freespaceE = true; boolean freespaceF = true; boolean freespaceG = true; boolean freespaceH = true;
                        boolean freespaceI = true;
                        ArrayList<String> TimeSlotList = new ArrayList<>();
                        ArrayList<String> FreeTimeSlotList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                            if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(EightToNine))) {

                                freespaceA = false;

                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(NineToTen))) {

                                freespaceB = false;

                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(TenToEleven))) {

                                freespaceC = false;

                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(ElevenToTwelve))) {

                                freespaceD = false;

                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(TwelveToOne))) {

                                freespaceE = false;

                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(OneToTwo))) {

                                freespaceF = false;
                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(TwoToThree))) {

                                freespaceG = false;

                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(ThreeToFour))) {

                                freespaceH = false;

                            } else if (String.valueOf(document.get("dateOfAppointment")).equals(String.valueOf(FourToFive))) {

                                freespaceI = false;

                            }

                           // TimeSlotList.add(String.valueOf(document.get("dateOfAppointment")));
                        }
                        if(goal.equals("Sunday")){
                             freespaceA = false;  freespaceB = false;  freespaceC = false;  freespaceD = false;
                             freespaceE = false;  freespaceF = false;  freespaceG = false;  freespaceH = false;
                             freespaceI = false;
                        }
                        if(goal.equals("Saturday")){
                             freespaceE = false; freespaceF = false;  freespaceG = false;  freespaceH = false;
                             freespaceI = false;
                        }

                        if(freespaceA) {TimeSlotList.add("8:00 am - 9:00 am");} else {TimeSlotList.add("Occupied");}

                        if(freespaceB) {TimeSlotList.add("9:00 am - 10:00 am");} else{TimeSlotList.add("Occupied");}

                        if(freespaceC) {TimeSlotList.add("10:00 am - 11:00 am");} else{TimeSlotList.add("Occupied");}

                        if(freespaceD) {TimeSlotList.add("11:00 am - 12:00 pm");} else{TimeSlotList.add("Occupied");}

                        if(freespaceE) {TimeSlotList.add("12:00 pm - 1:00 pm");} else{TimeSlotList.add("Occupied");}

                        if(freespaceF) {TimeSlotList.add("1:00 pm - 2:00 pm");} else{TimeSlotList.add("Occupied");}

                        if(freespaceG) {TimeSlotList.add("2:00 pm - 3:00 pm");} else{TimeSlotList.add("Occupied");}

                        if(freespaceH) {TimeSlotList.add("3:00 pm - 4:00 pm");} else{TimeSlotList.add("Occupied");}

                        if(freespaceI) {TimeSlotList.add("4:00 pm - 5:00 pm");} else{TimeSlotList.add("Occupied");}

                        callback.onSuccess(TimeSlotList);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching medicines", e);
                        callback.onError(e.getMessage());
                    });
    }

    public interface AppointmentCancelCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }
    public interface AppointmentAddCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }

    public interface AppointmentFetchCallback {
        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface AppointmentPatientPendingFetchCallback {
        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface AppointmentPatientStatusFetchCallback {
        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface AppointmentRescheduleCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }

    public interface AppointmentDataFetchCallback {
        void onSuccess(AppointmentDto appointment);

        void onError(String errorMessage);
    }

    public interface ReportCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }

    public interface CheckAppointmentExistFetchCallback {
        void onSuccess(ArrayList<String> lngList);

        void onError(String errorMessage);
    }
}
