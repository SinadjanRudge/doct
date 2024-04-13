package com.triadss.doctrack2.activity.patient.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.MedicationTypeConstants;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.helper.ButtonManager;
import com.triadss.doctrack2.repoositories.MedicationRepository;
import com.triadss.doctrack2.repoositories.ReportsRepository;

import java.util.ArrayList;
import java.util.Iterator;

public class PatientMedicationOngoingAdapter extends RecyclerView.Adapter<PatientMedicationOngoingAdapter.ViewHolder> {
    private final String TAG = "PatientMedicationOngoingAdapter";
    private ArrayList<MedicationDto> medications;
    private Context context;
    private MedicationRepository medicationRepository;
    private ReportsRepository _reportsRepository = new ReportsRepository();

    public PatientMedicationOngoingAdapter(Context context, ArrayList<MedicationDto> medications) {
        this.context = context;
        this.medications = medications;
        this.medicationRepository = new MedicationRepository();
    }

    @NonNull
    @Override
    public PatientMedicationOngoingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_medication_ongoing, parent, false);

        // Passing view to ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientMedicationOngoingAdapter.ViewHolder holder, int position) {
        holder.update(medications.get(position));
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView medicine, note, date, time;
        private Button update, complete;
        private String mediId;

        public ViewHolder(View view) {
            super(view);
            medicine = view.findViewById(R.id.medicationMedicine);
            note = view.findViewById(R.id.medicationNote);
            date = view.findViewById(R.id.medicationDate);
            time = view.findViewById(R.id.medicationTime);
            complete = view.findViewById(R.id.medicationComplete);
            update = view.findViewById(R.id.medicationUpdate);
        }

        public void update(MedicationDto medicationDto) {
            medicine.setText(medicationDto.getMedicine());
            note.setText(medicationDto.getNote());

            DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(medicationDto.getTimestamp());
            date.setText(dateTime.getDate().ToString());
            time.setText(dateTime.getTime().ToString());
            mediId = medicationDto.getMediId();
            setupComplete(medicationDto);
            update.setOnClickListener(v -> openUpdateDialog(medicationDto));
        }

        private void openUpdateDialog(MedicationDto medicationDto) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_update_medication);

            TextInputEditText medicationEditText = dialog.findViewById(R.id.medicineValue);
            TextInputEditText noteEditText = dialog.findViewById(R.id.noteValue);
            Button updateBtn = dialog.findViewById(R.id.updateBtn);

            medicationEditText.setText(medicationDto.getMedicine());
            noteEditText.setText(medicationDto.getNote());

            updateBtn.setOnClickListener(v -> {
                String updatedMedication = medicationEditText.getText().toString();
                String updatedNote = noteEditText.getText().toString();

                medicationDto.setMedicine(updatedMedication);
                medicationDto.setNote(updatedNote);

                ButtonManager.disableButton(updateBtn);

                medicationRepository.updateMedication(mediId, medicationDto, new MedicationRepository.MedicationUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        _reportsRepository.addPatientUpdatedMedicationReport(medicationDto, new ReportsRepository.ReportCallback() {
                            @Override
                            public void onReportAddedSuccessfully() {
                                updateMedicationsList(medicationDto);
                                Toast.makeText(context, mediId + " updated", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onReportFailed(String errorMessage) {
                                ButtonManager.enableButton(updateBtn);
                            }
                        });

                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e(TAG, "Error updating medication: " + errorMessage);
                        ButtonManager.enableButton(updateBtn);
                    }
                });

            });

            dialog.show();
        }

        private void setupComplete(MedicationDto medication) {
            complete.setOnClickListener(v -> {
                ButtonManager.disableButton(complete);

                medicationRepository.updateMedicationStatus(mediId, MedicationTypeConstants.COMPLETED, new MedicationRepository.MedicationUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        _reportsRepository.addPatientCompletedMedicationReport(medication, new ReportsRepository.ReportCallback() {
                            @Override
                            public void onReportAddedSuccessfully() {
                                removeItem(mediId);
                                Log.d(TAG, "Medication status updated successfully");
                            }

                            @Override
                            public void onReportFailed(String errorMessage) {
                                ButtonManager.enableButton(complete);
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e(TAG, "Error updating medication status: " + errorMessage);
                        ButtonManager.enableButton(complete);

                    }
                });
                Toast.makeText(context, medicine.getText() + " has been completed.", Toast.LENGTH_SHORT).show();
            });
        }

        private void removeItem(String mediId) {
            Iterator<MedicationDto> iterator = medications.iterator();
            while (iterator.hasNext()) {
                MedicationDto medication = iterator.next();
                if (mediId.equals(medication.getMediId())) {
                    iterator.remove();
                    notifyDataSetChanged();
                    break;
                }
            }
        }

        private void updateMedicationsList(MedicationDto medicationDto) {
            for (int i = 0; i < medications.size(); i++) {
                MedicationDto currentMedication = medications.get(i);
                if (currentMedication.getMediId().equals(mediId)) {
                    medications.set(i, medicationDto);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }
}
