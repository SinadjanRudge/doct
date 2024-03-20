package com.triadss.doctrack2.activity.patient.fragment;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.TimeDto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.triadss.doctrack2.repoositories.AppointmentRepository;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class PatientAppointmentPendingAdapter extends RecyclerView.Adapter<PatientAppointmentPendingAdapter.ViewHolder> {
    AppointmentRepository appointmentRepository;
    ArrayList<AppointmentDto> appointments;
    Context context;

    // Constructor for initialization
    public PatientAppointmentPendingAdapter(Context context, ArrayList<AppointmentDto> appointments) {
        this.context = context;

        this.appointments = appointments;
    }

    @NonNull
    @Override
    public PatientAppointmentPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        appointmentRepository = new AppointmentRepository();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pending, parent, false);

        // Passing view to ViewHolder
        PatientAppointmentPendingAdapter.ViewHolder viewHolder = new PatientAppointmentPendingAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull PatientAppointmentPendingAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.update(appointments.get(position));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView purpose,date,time, documentId;
        public ViewHolder(View view) {
            super(view);
            Button cancel, reschedule;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            cancel=(Button)itemView.findViewById(R.id.cancel_button);
            reschedule=(Button)itemView.findViewById(R.id.reschedule_button);
            documentId = (TextView) view.findViewById(R.id.DocumentID);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(itemView.getContext(), documentId.getText(), Toast.LENGTH_SHORT).show();
                    android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());


                    alertDialog.setTitle("Canceling");
                    alertDialog.setMessage("Are you sure?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.app.AlertDialog.Builder progressDialog = new AlertDialog.Builder(itemView.getContext());

                            getBindingAdapterPosition();
                            SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            appointmentRepository.cancelAppointment(documentId.getText().toString(), new AppointmentRepository.AppointmentCancelCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {

                                    android.app.AlertDialog.Builder progressDialog = new AlertDialog.Builder(itemView.getContext());

                                    progressDialog.setTitle("Canceled");
                                    progressDialog.setMessage("appointment was canceled");
                                    progressDialog.show();
                                }
                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                            appointmentRepository.addReport(documentId.getText().toString(),"CANCEL", new AppointmentRepository.ReportCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {
                                    Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();

                                }
                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                            myEdit.putInt("PatientPending", Integer.parseInt("10"));
                            myEdit.putInt("PatientStatus", Integer.parseInt("10"));
                            myEdit.apply();
                        }
                    });
                    alertDialog.show();

                }
            });
            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    showUpdateDialog(documentId.getText().toString());
                    System.out.println();
                }
            });
        }

        public void update(AppointmentDto appointment)
        {
            purpose.setText(appointment.getPurpose());
            DateTimeDto dateTimeDto = DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment());
            date.setText(dateTimeDto.getDate().ToString());
            time.setText(dateTimeDto.getTime().ToString());
            documentId.setText(appointment.getDocumentId());
        }

        private void showUpdateDialog(String id)
        {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.fragment_patient_appointment_reschedule);

            Button dateBtn = dialog.findViewById(R.id.dateBtn);
            TextView updateDate = dialog.findViewById(R.id.updateDate);

            Button timeBtn = dialog.findViewById(R.id.timeBtn);
            TextView updateTime = dialog.findViewById(R.id.updateTime);
            Button confirmBtn = dialog.findViewById(R.id.confirmbutton);
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

            confirmBtn.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                appointmentRepository.rescheduleAppointment(id, selectedDateTime.ToTimestamp(), new AppointmentRepository.AppointmentRescheduleCallback() {
                    @Override
                    public void onSuccess(String appointmentId) {
                        Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                appointmentRepository.addReport(id,"RESCHEDULE", new AppointmentRepository.ReportCallback() {
                    @Override
                    public void onSuccess(String appointmentId) {
                        Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                myEdit.putInt("PatientPending", Integer.parseInt("10"));
                myEdit.putInt("PatientStatus", Integer.parseInt("10"));
                myEdit.apply();
            });
            dialog.show();
        }
    }
}
