package com.triadss.doctrack2.activity.patient.fragment;
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
public class PatientAppointmentStatusAdapter extends RecyclerView.Adapter<PatientAppointmentStatusAdapter.ViewHolder> {
    ArrayList<AppointmentDto> appointments;
    Context context;

    // Constructor for initialization
    public PatientAppointmentStatusAdapter(Context context, ArrayList<AppointmentDto> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public PatientAppointmentStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_status, parent, false);

        // Passing view to ViewHolder
        PatientAppointmentStatusAdapter.ViewHolder viewHolder = new PatientAppointmentStatusAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull PatientAppointmentStatusAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.update(appointments.get(position));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView purpose,date,time,status, idText, nameText;

        public ViewHolder(View view) {
            super(view);
            Button cancel, reschedule;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            status = (TextView) view.findViewById(R.id.statustext);
            idText = view.findViewById(R.id.IDtext);
            nameText = view.findViewById(R.id.nametext);
        }

        public void update(AppointmentDto appointment)
        {
            purpose.setText(appointment.getPurpose());
            status.setText(appointment.getStatus());
            idText.setText(appointment.getDocumentId());
            nameText.setText(appointment.getNameOfRequester());

            DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment());
            date.setText(dateTime.getDate().ToString());
            time.setText(dateTime.getTime().ToString());
        }
    }
}
