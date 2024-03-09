package com.triadss.doctrack2.activity.patient.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.MedicationDto;

import java.util.List;

public class OngoingMedicationAdapter extends RecyclerView.Adapter<OngoingMedicationAdapter.ViewHolder> {

    private List<MedicationDto> ongoingMedications;
    private Context context;

    public OngoingMedicationAdapter(Context context, List<MedicationDto> ongoingMedications) {
        this.context = context;
        this.ongoingMedications = ongoingMedications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ongoing_medication_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicationDto medication = ongoingMedications.get(position);

        holder.medicationNameTextView.setText("Medication Name: " + medication.getMedicine());
        holder.purposeTextView.setText("Purpose: " + medication.getNote());
        holder.patientIdTextView.setText("Patient ID: " + medication.getPatientId());
    }

    @Override
    public int getItemCount() {
        return ongoingMedications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView medicationNameTextView;
        TextView purposeTextView;
        TextView patientIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            medicationNameTextView = itemView.findViewById(R.id.ongoingMedicationName);
            purposeTextView = itemView.findViewById(R.id.ongoingMedicationPurpose);
            patientIdTextView = itemView.findViewById(R.id.ongoingMedicationPatientId);
        }
    }
}
