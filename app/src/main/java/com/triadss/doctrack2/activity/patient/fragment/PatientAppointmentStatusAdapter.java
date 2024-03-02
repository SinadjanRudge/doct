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

import java.util.ArrayList;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class PatientAppointmentStatusAdapter extends RecyclerView.Adapter<PatientAppointmentStatusAdapter.ViewHolder> {
    ArrayList  Pending, Date, Time, Status;
    Context context;

    // Constructor for initialization
    public PatientAppointmentStatusAdapter(Context context,  ArrayList Pending, ArrayList Date, ArrayList Time, ArrayList Status) {
        this.context = context;

        this.Pending = Pending;
        this.Date = Date;
        this.Time = Time;
        this.Status = Status;
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

        holder.purpose.setText((String) Pending.get(position));
        holder.date.setText((String) Date.get(position));
        holder.time.setText((String) Time.get(position));
        holder.status.setText((String) Status.get(position));
    }

    @Override
    public int getItemCount() {
        return Pending.size();
    }


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView purpose,date,time,status;

        public ViewHolder(View view) {
            super(view);
            Button cancel, reschedule;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            status = (TextView) view.findViewById(R.id.statustext);
        }
    }
}
