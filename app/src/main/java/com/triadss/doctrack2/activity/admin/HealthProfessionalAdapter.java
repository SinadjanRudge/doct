package com.triadss.doctrack2.activity.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.HealthProfDto;

import java.util.ArrayList;


public class HealthProfessionalAdapter extends RecyclerView.Adapter<HealthProfessionalAdapter.ViewHolder> {
    ArrayList<HealthProfDto> healthProfessional;
    Context context;
    Callbacks callback;

    // Constructor for initialization
    public HealthProfessionalAdapter(Context context, ArrayList<HealthProfDto> healthProfessional, Callbacks callback) {
        this.context = context;
        this.callback = callback;
        this.healthProfessional = healthProfessional;
    }

    @NonNull
    @Override
    public HealthProfessionalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin_healthprof, parent, false);

        // Passing view to ViewHolder
        HealthProfessionalAdapter.ViewHolder viewHolder = new HealthProfessionalAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull HealthProfessionalAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.update(healthProfessional.get(position));
    }

    @Override
    public int getItemCount() {
        return healthProfessional.size();
    }


    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView email, name;
        private Button viewBtn, updateBtn;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewAdminName);
            email = (TextView) view.findViewById(R.id.textViewAdminEmail);
<<<<<<< HEAD

=======
>>>>>>> be67203dbee5e8c335a38393d837c798a0ae5545
            viewBtn = view.findViewById(R.id.viewBtn);
            updateBtn = view.findViewById(R.id.updateBtn);
        }

<<<<<<< HEAD
        public void update(HealthProfDto healthProfDto)
        {
            name.setText(healthProfDto.getFullName());

            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnViewPressed(healthProfDto.getHealthProfid());
=======
        public void update(HealthProfDto healthProfDto) {
            name.setText(healthProfDto.getFullName());
            email.setText(healthProfDto.getEmail());

            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.OnView(healthProfDto.getHealthProfid());
>>>>>>> be67203dbee5e8c335a38393d837c798a0ae5545
                }
            });

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
<<<<<<< HEAD
                public void onClick(View v) {
                    callback.OnUpdatePressed(healthProfDto.getHealthProfid());
=======
                public void onClick(View view) {
                    callback.OnUpdate(healthProfDto.getHealthProfid());
>>>>>>> be67203dbee5e8c335a38393d837c798a0ae5545
                }
            });
        }
    }

    public interface Callbacks {
        public void OnView(String healthProdUid);
        public void OnUpdate(String healthProdUid);
    }

    public interface Callback {
        public void OnViewPressed(String uid);
        public void OnUpdatePressed(String uid);
    }
}