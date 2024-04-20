package com.triadss.doctrack2.activity.healthprof.adapters;

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
import com.triadss.doctrack2.dto.TimeDto;
import com.triadss.doctrack2.helper.ButtonManager;
import com.triadss.doctrack2.utils.AppointmentFunctions;

import java.util.ArrayList;


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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointment_pending, parent, false);

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
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            identification = (TextView) view.findViewById(R.id.IDtext);
            name = (TextView) view.findViewById(R.id.nametext);
        }

        public void update(AppointmentDto appointment)
        {
            purpose.setText(appointment.getPurpose());
            identification.setText(appointment.getPatientIdNumber());
            name.setText(appointment.getNameOfRequester());

            DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment());
            date.setText(dateTime.getDate().ToString());
            time.setText(dateTime.getTime().ToString());
            Button reschedule = (Button)itemView.findViewById(R.id.reschedule_button);

            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    showUpdateDialog(appointment);
                }
            });

            Button cancel = (Button)itemView.findViewById(R.id.cancel_button);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonManager.disableButton(cancel);
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    appointmentCallbacks.onCancel(appointment.getUid());
                }
            });
        }

        private void showUpdateDialog(AppointmentDto dto)
        {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.fragment_patient_appointment_reschedule);

            Button dateBtn = dialog.findViewById(R.id.dateBtn);
            TextView updateDate = dialog.findViewById(R.id.updateDate);

            Button timeBtn = dialog.findViewById(R.id.timeBtn);
            TextView updateTime = dialog.findViewById(R.id.updateTime);
            TextView dateErrorText = dialog.findViewById(R.id.dateErrorText);
            TextView timeErrorText = dialog.findViewById(R.id.timeErrorText);

            Button confirm = dialog.findViewById(R.id.confirmbutton);

            DateTimeDto selectedDateTime = new DateTimeDto();
            DateTimeDto dateOfAppointment = DateTimeDto.ToDateTimeDto(dto.getDateOfAppointment());

            dateBtn.setOnClickListener((View.OnClickListener) v -> {
                // Get the current date
                DateDto dateDto = dateOfAppointment.getDate();

                // Create and show the Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Store the selected date
                            selectedDateTime.setDate(new DateDto(year1, monthOfYear + 1, dayOfMonth));

                            // Update the text on the button
                            updateDate.setText(selectedDateTime.getDate().ToString(false));
                        }, dateDto.getYear(), dateDto.getMonth(), dateDto.getDay());

                // Show the Date Picker Dialog
                datePickerDialog.show();
            });

            timeBtn.setOnClickListener(v -> {
                // Get the current time
                TimeDto timeDto = dateOfAppointment.getTime();

                // Create and show the Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        (view, hourOfDay, minute1) -> {
                            // Store the selected time
                            selectedDateTime.setTime(new TimeDto(hourOfDay, minute1));

                            // Update the text on the button
                            updateTime.setText(selectedDateTime.getTime().ToString());
                        }, timeDto.getHour(), timeDto.getMinute(), false);

                // Show the Time Picker Dialog
                timePickerDialog.show();
            });

            confirm.setOnClickListener(v -> {
                dateErrorText.setVisibility(View.GONE);
                timeErrorText.setVisibility(View.GONE);

                boolean inputInvalid = false;

                if (selectedDateTime.getDate() == null ||  selectedDateTime.getDate().getYear() == 0 || selectedDateTime.getDate().getMonth() == 0 || selectedDateTime.getDate().getDay() == 0) {
                    Toast.makeText(context, "Please select a valid date", Toast.LENGTH_SHORT).show();
                    dateErrorText.setVisibility(View.VISIBLE);
                    inputInvalid = true;
                }

                if (selectedDateTime.getTime() == null || AppointmentFunctions.IsValidHour(selectedDateTime.getTime())) {
                    Toast.makeText(context, "Please select a valid time", Toast.LENGTH_SHORT).show();
                    timeErrorText.setVisibility(View.VISIBLE);
                    inputInvalid = true;
                }

                if(inputInvalid)
                {
                    return;
                }

                ButtonManager.disableButton(confirm);
                appointmentCallbacks.onRescheduleConfirmed(selectedDateTime, dto.getDocumentId(), dialog);
            });

            dialog.show();
        }

    }


    public interface AppointmentCallback {
        void onRescheduleConfirmed(DateTimeDto dateTime, String appointmentUid, Dialog dialog);
        void onCancel(String appointmentUid);
    }
}