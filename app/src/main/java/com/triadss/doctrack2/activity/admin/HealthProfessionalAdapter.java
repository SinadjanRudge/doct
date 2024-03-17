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

    // Constructor for initialization
    public HealthProfessionalAdapter(Context context, ArrayList<HealthProfDto> healthProfessional) {
        this.context = context;

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
        private Button reschedule;

        public ViewHolder(View view) {
            super(view);
            Button cancel;
            name = (TextView) view.findViewById(R.id.textViewAdminName);
            email = (TextView) view.findViewById(R.id.textViewAdminEmail);
        }

        public void update(HealthProfDto appointment)
        {
            name.setText(appointment.getFullName());
        }

        private void showUpdateDialog()
        {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.fragment_patient_appointment_reschedule);

            Button dateBtn = dialog.findViewById(R.id.dateBtn);
            TextView updateDate = dialog.findViewById(R.id.updateDate);

            Button timeBtn = dialog.findViewById(R.id.timeBtn);
            TextView updateTime = dialog.findViewById(R.id.updateTime);

            DateTimeDto selectedDateTime = new DateTimeDto();

            dateBtn.setOnClickListener((View.OnClickListener) v -> {
                // Get the current date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create and show the Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Store the selected date
                            selectedDateTime.setDate(new DateDto(year1, monthOfYear, dayOfMonth));

                            // Update the text on the button
                            updateDate.setText(selectedDateTime.getDate().ToString());
                        }, year, month, day);

                // Show the Date Picker Dialog
                datePickerDialog.show();
            });

            timeBtn.setOnClickListener(v -> {
                // Get the current time
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create and show the Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        (view, hourOfDay, minute1) -> {
                            // Store the selected time

                            selectedDateTime.setTime(new TimeDto(hourOfDay, minute1));

                            // Update the text on the button
                            updateTime.setText(selectedDateTime.getTime().ToString());
                        }, hour, minute, false);

                // Show the Time Picker Dialog
                timePickerDialog.show();
            });

            dialog.show();
        }

    }
}