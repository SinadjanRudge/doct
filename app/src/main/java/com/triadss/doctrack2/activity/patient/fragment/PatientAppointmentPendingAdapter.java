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
public class PatientAppointmentPendingAdapter extends RecyclerView.Adapter<PatientAppointmentPendingAdapter.ViewHolder> {
    ArrayList  Pending, Date, Time;
    Context context;

    // Constructor for initialization
    public PatientAppointmentPendingAdapter(Context context,  ArrayList Pending, ArrayList Date, ArrayList Time) {
        this.context = context;

        this.Pending = Pending;
        this.Date = Date;
        this.Time = Time;
    }

    @NonNull
    @Override
    public PatientAppointmentPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pending, parent, false);

        // Passing view to ViewHolder
        PatientAppointmentPendingAdapter.ViewHolder viewHolder = new PatientAppointmentPendingAdapter.ViewHolder(view);
        return viewHolder;
    }


    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull PatientAppointmentPendingAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type

        holder.purpose.setText((String) Pending.get(position));
        holder.date.setText((String) Date.get(position));
        holder.time.setText((String) Time.get(position));
    }

    @Override
    public int getItemCount() {
        return Pending.size();
    }


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView purpose,date,time;

        public ViewHolder(View view) {
            super(view);
            Button cancel, reschedule;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
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

                }
            });
        }
    }
}
