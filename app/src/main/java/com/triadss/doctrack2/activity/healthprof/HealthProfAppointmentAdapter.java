package com.triadss.doctrack2.activity.healthprof;

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
public class HealthProfAppointmentAdapter extends RecyclerView.Adapter<HealthProfAppointmentAdapter.ViewHolder> {
    ArrayList  courseName;
    Context context;

    // Constructor for initialization
    public HealthProfAppointmentAdapter(Context context,  ArrayList courseName) {
        this.context = context;

        this.courseName = courseName;
    }

    @NonNull
    @Override
    public HealthProfAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        // Passing view to ViewHolder
        HealthProfAppointmentAdapter.ViewHolder viewHolder = new HealthProfAppointmentAdapter.ViewHolder(view);
        return viewHolder;
    }



    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull HealthProfAppointmentAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type

        holder.text.setText((String) courseName.get(position));
    }

    @Override
    public int getItemCount() {
        return courseName.size();
    }


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView text;

        public ViewHolder(View view) {
            super(view);
            Button accept;
            text = (TextView) view.findViewById(R.id.nametext);
            accept=(Button)itemView.findViewById(R.id.button2);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), text.getText(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}
