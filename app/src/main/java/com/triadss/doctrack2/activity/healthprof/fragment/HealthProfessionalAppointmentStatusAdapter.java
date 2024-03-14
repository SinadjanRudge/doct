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

import java.util.ArrayList;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class HealthProfessionalAppointmentStatusAdapter extends RecyclerView.Adapter<HealthProfessionalAppointmentStatusAdapter.ViewHolder> {
    ArrayList  Identification,Name,Pending, Date, Time, Status;
    Context context;

    // Constructor for initialization
    public HealthProfessionalAppointmentStatusAdapter(Context context,  ArrayList Pending, ArrayList Date, ArrayList Time, ArrayList Status, ArrayList Identification, ArrayList Name) {
        this.context = context;

        this.Pending = Pending;
        this.Date = Date;
        this.Time = Time;
        this.Status = Status;
        this.Identification = Identification;
        this.Name = Name;
    }

    @NonNull
    @Override
    public HealthProfessionalAppointmentStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_status, parent, false);

        // Passing view to ViewHolder
        HealthProfessionalAppointmentStatusAdapter.ViewHolder viewHolder = new HealthProfessionalAppointmentStatusAdapter.ViewHolder(view);
        return viewHolder;
    }


    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull HealthProfessionalAppointmentStatusAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type

        holder.purpose.setText((String) Pending.get(position));
        holder.date.setText((String) Date.get(position));
        holder.time.setText((String) Time.get(position));
        holder.status.setText((String) Status.get(position));
        holder.identification.setText((String) Identification.get(position));
        holder.name.setText((String) Name.get(position));
    }

    @Override
    public int getItemCount() {
        return Pending.size();
    }


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView purpose,date,time,status,identification,name;

        public ViewHolder(View view) {
            super(view);
            Button cancel, reschedule;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            status = (TextView) view.findViewById(R.id.statustext);
            identification = (TextView) view.findViewById(R.id.IDtext);
            name = (TextView) view.findViewById(R.id.nametext);
        }
    }
}
