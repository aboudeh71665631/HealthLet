package com.example.firstproject.DoctorNavigation.DocPatients;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstproject.R;

public class AddNewPatient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_patient);
    }

    public void back_btn(View view) { finish();
    }
}