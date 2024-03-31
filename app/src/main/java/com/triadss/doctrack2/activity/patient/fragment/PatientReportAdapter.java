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
import com.triadss.doctrack2.dto.ReportDto;
import com.triadss.doctrack2.repoositories.MedicationRepository;

import java.util.ArrayList;
import java.util.Iterator;

public class PatientReportAdapter extends RecyclerView.Adapter<PatientReportAdapter.ViewHolder> {
    private final String TAG = "PatientMedicationOngoingAdapter";
    private ArrayList<ReportDto> reports;
    private Context context;

    public PatientReportAdapter(Context context, ArrayList<ReportDto> reports) {
        this.context = context;
        this.reports = reports;
    }

    @NonNull
    @Override
    public PatientReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reports, parent, false);

        // Passing view to ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientReportAdapter.ViewHolder holder, int position) {
        holder.update(reports.get(position));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView description, action, date, createdByName;
        public ViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.descriptionValue);
            action = view.findViewById(R.id.actionValue);
            date = view.findViewById(R.id.dateValue);
            createdByName = view.findViewById(R.id.createdByValue);
        }

        public void update(ReportDto reportDto) {
            description.setText(reportDto.getMessage());
            action.setText(reportDto.getAction());
            createdByName.setText(reportDto.getCreatedByName());

            DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(reportDto.getCreatedDate());
            date.setText(dateTime.ToString());
        }
    }
}
