package com.triadss.doctrack2.activity.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.HealthProfDto;
import com.triadss.doctrack2.dto.TimeDto;

import java.util.ArrayList;
import java.util.Calendar;


public class HealthProfessionalAdapter extends RecyclerView.Adapter<HealthProfessionalAdapter.ViewHolder> {
    ArrayList<HealthProfDto> healthProfessional;
    Context context;
    Callback callback;

    // Constructor for initialization
    public HealthProfessionalAdapter(Context context, ArrayList<HealthProfDto> healthProfessional, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.healthProfessional = healthProfessional;
    }

    @NonNull
    @Override
    public HealthProfessionalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin_healthprof, parent, false);

        // Passing view to ViewHolder
        HealthProfessionalAdapter.ViewHolder viewHolder = new HealthProfessionalAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull HealthProfessionalAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.update(healthProfessional.get(position));
    }

    @Override
    public int getItemCount() {
        return healthProfessional.size();
    }


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView email, name;
        private Button viewBtn, updateBtn;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewAdminName);
            email = (TextView) view.findViewById(R.id.textViewAdminEmail);

            viewBtn = view.findViewById(R.id.viewBtn);
            updateBtn = view.findViewById(R.id.updateBtn);
        }

        public void update(HealthProfDto healthProfDto)
        {
            name.setText(healthProfDto.getFullName());

            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Callback(healthProfDto.getHWIN());
                }
            });

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Callback(healthProfDto.getHWIN());
                }
            });
        }

    }

    public interface Callback {
        public void OnViewPressed(String uid);
        public void OnUpdatePressed(String uid);
    }
}