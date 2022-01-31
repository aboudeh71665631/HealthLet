package com.example.firstproject.Navigation.Appointments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstproject.Database.DoctorHelperClass;
import com.example.firstproject.Database.Doctor_List_Adapter;
import com.example.firstproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookAppointment extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Doctor_List_Adapter doctor_list_adapter;
    ArrayList<DoctorHelperClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        recyclerView = findViewById(R.id.doctor_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("doctor");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        doctor_list_adapter = new Doctor_List_Adapter(this, list);
        recyclerView.setAdapter(doctor_list_adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoctorHelperClass doctorHelperClass = dataSnapshot.getValue(DoctorHelperClass.class);
                    list.add(doctorHelperClass);
                }

                doctor_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back_btn(View view) {
        finish();
    }
}