package com.example.firstproject.DoctorNavigation.DocAppointments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstproject.Database.AppointmentHelperClass;
import com.example.firstproject.Database.Appointment_List_Adapter;
import com.example.firstproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DocReviewAppointments extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Appointment_List_Adapter appointment_list_adapter;
    ArrayList<AppointmentHelperClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_review_appointments);

        recyclerView = findViewById(R.id.app_requests_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        appointment_list_adapter = new Appointment_List_Adapter(this,list);
        recyclerView.setAdapter(appointment_list_adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AppointmentHelperClass appointmentHelperClass = dataSnapshot.getValue(AppointmentHelperClass.class);
                    list.add(appointmentHelperClass);
                }

                appointment_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}