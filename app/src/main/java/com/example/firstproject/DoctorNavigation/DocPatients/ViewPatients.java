package com.example.firstproject.DoctorNavigation.DocPatients;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPatients extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Patients_List_Adapter patients_list_adapter;
    ArrayList<PatientHelperClass> list;
    EditText searchPatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);

        recyclerView = findViewById(R.id.view_patients_Recycler);
        databaseReference = FirebaseDatabase.getInstance().getReference("patient");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchPatients = findViewById(R.id.search_Patients);
        searchPatients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        list = new ArrayList<>();
        patients_list_adapter = new Patients_List_Adapter(this,list);
        recyclerView.setAdapter(patients_list_adapter);

        patients_list_adapter.setOnItemClickListener(new Patients_List_Adapter.OnItemClickedListener() {
            @Override
            public void onItemClick(int position) {
//                list.get(position).changeName("Test");
//                patients_list_adapter.notifyDataSetChanged();
                Intent intent = new Intent(ViewPatients.this,Doctor_Edit_Patient.class);
                intent.putExtra("phoneNo",list.get(position).getPhoneNo());
                Toast.makeText(ViewPatients.this, list.get(position).getPhoneNo(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PatientHelperClass patientHelperClass = dataSnapshot.getValue(PatientHelperClass.class);
                    list.add(patientHelperClass);
                }
                patients_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void filter(String text) {
        ArrayList<PatientHelperClass> filteredList = new ArrayList<PatientHelperClass>();
        for (PatientHelperClass item : list){
            if(item.getfName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        patients_list_adapter.filterList(filteredList);
    }
}