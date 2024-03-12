package com.triadss.doctrack2.activity.patient.fragment;

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
import com.triadss.doctrack2.repoositories.MedicationRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatientMedicationOngoingAdapter
        extends RecyclerView.Adapter<PatientMedicationOngoingAdapter.ViewHolder> {
    private final String TAG  = "PatientMedicationOngoingAdapter";
    ArrayList<MedicationDto> medications;
    Context context;

    public PatientMedicationOngoingAdapter(Context context, ArrayList<MedicationDto> medications) {
        this.context = context;
        this.medications = medications;
    }

    @NonNull
    @Override
    public PatientMedicationOngoingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_medication_ongoing, parent, false);

        // Passing view to ViewHolder
        PatientMedicationOngoingAdapter.ViewHolder viewHolder = new PatientMedicationOngoingAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientMedicationOngoingAdapter.ViewHolder holder, int position) {
        holder.update(medications.get(position));
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public void removeItem(String mediId) {
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


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        private MedicationRepository medicationRepository = new MedicationRepository();
        private TextView medicine, note, date, time;
        private Button update, complete;
        private String mediId;
        private void setupComplete(){
            complete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    medicationRepository.updateMedicationStatus(mediId, MedicationTypeConstants.COMPLETED, new MedicationRepository.MedicationUpdateCallback() {
                        @Override
                        public void onSuccess() {
                            // Update successful
                            // Remove the item from the list
                            removeItem(mediId);
                            Log.d(TAG, "Medication status updated successfully");

                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Handle error
                            Log.e(TAG, "Error updating medication status: " + errorMessage);
                        }
                    });
                    Toast.makeText(itemView.getContext(), medicine + " has been completed.", Toast.LENGTH_SHORT).show();

                }
            });
        }

        public ViewHolder(View view) {
            super(view);

            medicine = (TextView) view.findViewById(R.id.medicationMedicine);
            note = (TextView) view.findViewById(R.id.medicationNote);
            date = (TextView) view.findViewById(R.id.medicationDate);
            time = (TextView) view.findViewById(R.id.medicationTime);
            complete = (Button) itemView.findViewById(R.id.medicationComplete);
            update = (Button) itemView.findViewById(R.id.medicationUpdate);
            setupComplete();
        }

        public void update(MedicationDto medicationDto) {
            medicine.setText(medicationDto.getMedicine());
            note.setText(medicationDto.getNote());

            DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(medicationDto.getTimestamp());
            date.setText(dateTime.getDate().ToString());
            time.setText(dateTime.getTime().ToString());
            mediId = medicationDto.getMediId();

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), medicine.getText(), Toast.LENGTH_SHORT).show();

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_update_medication);

                    TextInputEditText medication = dialog.findViewById(R.id.medicineValue);
                    TextInputEditText note = dialog.findViewById(R.id.noteValue);

                    Button updateBtn = dialog.findViewById(R.id.updateBtn);

                    medication.setText(medicationDto.getMedicine());
                    note.setText(medicationDto.getNote());

                    dialog.show();
                }
            });
        }
    }
}
