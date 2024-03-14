package com.triadss.doctrack2.activity.healthprof.fragment;

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
import com.triadss.doctrack2.dto.DateTimeDto;

import java.util.ArrayList;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class HealthProfessionalAppointmentUpcomingAdapter extends RecyclerView.Adapter<HealthProfessionalAppointmentUpcomingAdapter.ViewHolder> {
    ArrayList<AppointmentDto> appointments;
    Context context;
    AppointmentCallback callback;

    // Constructor for initialization
    public HealthProfessionalAppointmentUpcomingAdapter(Context context,  ArrayList<AppointmentDto> appointments, AppointmentCallback callback) {
        this.context = context;
        this.callback = callback;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public HealthProfessionalAppointmentUpcomingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_upcoming, parent, false);

        // Passing view to ViewHolder
        HealthProfessionalAppointmentUpcomingAdapter.ViewHolder viewHolder = new HealthProfessionalAppointmentUpcomingAdapter.ViewHolder(view);
        return viewHolder;
    }


    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull HealthProfessionalAppointmentUpcomingAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.update(appointments.get(position));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView purpose,date,time,identification,name;

        public ViewHolder(View view) {
            super(view);
            Button reject, accept;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            identification = (TextView) view.findViewById(R.id.IDtext);
            name = (TextView) view.findViewById(R.id.nametext);
            reject=(Button)itemView.findViewById(R.id.reject_button);
            accept=(Button)itemView.findViewById(R.id.accept_button);
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();

                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();

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
    }

    public interface AppointmentCallback {
        void onAccept(String appointmentUid);
        void onReject(String appointmentUid);
    }
}
