package com.triadss.doctrack2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//import com.google.firebase.auth.FirebaseAuth;

public class PatientAppointment extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;

    RecyclerView rview;
    myAdapter adapter;

    List<DataClass> dataList;

    DatabaseReference rootDatabaseref;
    FirebaseDatabase database;
    SearchView search;

    String userEmail;
    Button Home;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        database = FirebaseDatabase.getInstance();
        rootDatabaseref = database.getReference();

        dataList = new ArrayList<>();

        rview = (RecyclerView)findViewById(R.id.recyclerView);
        rview.setLayoutManager(new LinearLayoutManager(this));



        FirebaseRecyclerOptions<DataClass> options =
                new FirebaseRecyclerOptions.Builder<DataClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ravenjoven15@gmail1com"), DataClass.class)
                        .build();

        adapter = new myAdapter(options);
        rview.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.upcoming) {//replaceFragment(new HomeFragment());

            } else if (itemId == R.id.pending) {// replaceFragment(new SubscriptionFormat());
                Intent intent = new Intent(PatientAppointment.this, Pending.class);
                startActivity(intent);
            } else if (itemId == R.id.status) {//  showLogOutConfirmationDialog();

            }
            return true;
        });


    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }


//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//    }




}