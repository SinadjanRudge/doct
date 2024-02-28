package com.triadss.doctrack2.activity.healthprof.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AddPatientDto;

import java.util.List;

public class PatientFragmentAdapter extends RecyclerView.Adapter<PatientFragmentAdapter.PatientFragmentViewHolder> {
    List<AddPatientDto> addPatientDtoList;

    public PatientFragmentAdapter(List<AddPatientDto> patientList) {
        this.addPatientDtoList = patientList;
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
    }

    @Override
    public int getItemCount() {
        return addPatientDtoList.size();
    }

    public static class PatientFragmentViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public PatientFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
           // name = itemView.findViewById(R.id.patientNameName);
        }
    }
}
