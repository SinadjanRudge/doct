package com.triadss.doctrack2.activity.healthprof.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AddPatientDto;

import java.util.List;

public class PatientFragmentAdapter extends RecyclerView.Adapter<PatientFragmentAdapter.PatientFragmentViewHolder> {
    List<AddPatientDto> addPatientDtoList;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(AddPatientDto patient);
    }

    public PatientFragmentAdapter(List<AddPatientDto> patientList, OnItemClickListener listener) {
        this.addPatientDtoList = patientList;
        this.onItemClickListener = listener;
    }

    public void updateList(List<AddPatientDto> newData) {
        addPatientDtoList.clear();
        addPatientDtoList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientFragmentAdapter.PatientFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_healthprof_patient, parent, false);
        return new PatientFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientFragmentAdapter.PatientFragmentViewHolder holder, int position) {
        holder.name.setText(addPatientDtoList.get(position).getFullName());
        holder.idNumber.setText(addPatientDtoList.get(position).getIdNumber());
    }

    @Override
    public int getItemCount() {
        return addPatientDtoList.size();
    }

    public class PatientFragmentViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView idNumber;
        Button viewRecord;
        public PatientFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            viewRecord = itemView.findViewById(R.id.viewPatientRecord);
            name = itemView.findViewById(R.id.textView_patientName);
            idNumber = itemView.findViewById(R.id.textView_patientId);

            viewRecord.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(addPatientDtoList.get(position));
                    }
                }
            });
        }
    }
}
