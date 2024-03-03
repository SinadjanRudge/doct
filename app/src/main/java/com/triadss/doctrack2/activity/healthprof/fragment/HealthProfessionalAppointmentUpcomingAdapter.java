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
public class HealthProfessionalAppointmentUpcomingAdapter extends RecyclerView.Adapter<HealthProfessionalAppointmentUpcomingAdapter.ViewHolder> {
    ArrayList  Identification,Name,Pending, Date, Time;
    Context context;

    // Constructor for initialization
    public HealthProfessionalAppointmentUpcomingAdapter(Context context,  ArrayList Pending, ArrayList Date, ArrayList Time, ArrayList Identification, ArrayList Name) {
        this.context = context;

        this.Pending = Pending;
        this.Date = Date;
        this.Time = Time;
        this.Identification = Identification;
        this.Name = Name;
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

        holder.purpose.setText((String) Pending.get(position));
        holder.date.setText((String) Date.get(position));
        holder.time.setText((String) Time.get(position));
        holder.identification.setText((String) Identification.get(position));
        holder.name.setText((String) Name.get(position));
    }

    @Override
    public int getItemCount() {
        return Pending.size();
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
    }
}
