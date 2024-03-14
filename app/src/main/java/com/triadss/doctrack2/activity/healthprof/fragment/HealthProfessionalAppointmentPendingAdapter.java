package com.triadss.doctrack2.activity.healthprof.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.TimeDto;

import java.util.ArrayList;
import java.util.Calendar;


public class HealthProfessionalAppointmentPendingAdapter extends RecyclerView.Adapter<HealthProfessionalAppointmentPendingAdapter.ViewHolder> {
    ArrayList<AppointmentDto> healthProfessional;
    Context context;
    AppointmentCallback appointmentCallbacks;

    // Constructor for initialization
    public HealthProfessionalAppointmentPendingAdapter(Context context,  ArrayList<AppointmentDto> healthProfessional, AppointmentCallback appointmentCallback) {
        this.context = context;
        this.appointmentCallbacks = appointmentCallback;
        this.healthProfessional = healthProfessional;
    }

    @NonNull
    @Override
    public HealthProfessionalAppointmentPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pending, parent, false);

        // Passing view to ViewHolder
        HealthProfessionalAppointmentPendingAdapter.ViewHolder viewHolder = new HealthProfessionalAppointmentPendingAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull HealthProfessionalAppointmentPendingAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.update(healthProfessional.get(position));
    }

    @Override
    public int getItemCount() {
        return healthProfessional.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView purpose,date,time,identification,name;

        public ViewHolder(View view) {
            super(view);
            Button reschedule, cancel;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            identification = (TextView) view.findViewById(R.id.IDtext);
            name = (TextView) view.findViewById(R.id.nametext);

            cancel=(Button)itemView.findViewById(R.id.cancel_button);
            reschedule=(Button)itemView.findViewById(R.id.reschedule_button);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();

                }
            });

            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    showUpdateDialog();
                }
            });
        }

        public void update(AppointmentDto appointment)
        {
            purpose.setText(appointment.getPurpose());
            identification.setText(appointment.getPatientId());
            name.setText(appointment.getNameOfRequester());

            DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment());
            date.setText(dateTime.getDate().ToString());
            time.setText(dateTime.getTime().ToString());
        }

        private void showUpdateDialog()
        {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.fragment_patient_appointment_reschedule);

            Button dateBtn = dialog.findViewById(R.id.dateBtn);
            TextView updateDate = dialog.findViewById(R.id.updateDate);

            Button timeBtn = dialog.findViewById(R.id.timeBtn);
            TextView updateTime = dialog.findViewById(R.id.updateTime);

            Button confirm = dialog.findViewById(R.id.confirmbutton);

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

            confirm.setOnClickListener(v -> {
                appointmentCallback.onRescheduleConfirmed(selectedDateTime);
                dialog.dismiss();
            });

            dialog.show();
        }

    }


    public interface AppointmentCallback {
        void onRescheduleConfirmed(DateTimeDto dateTime, String appointmentUid);
        void onCancel(String appointmentUid);
    }
}