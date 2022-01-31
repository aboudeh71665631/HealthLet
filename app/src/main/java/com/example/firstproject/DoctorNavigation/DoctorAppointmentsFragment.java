package com.example.firstproject.DoctorNavigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.firstproject.DoctorNavigation.DocAppointments.DocReviewAppointments;
import com.example.firstproject.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.button.MaterialButton;


public class DoctorAppointmentsFragment extends Fragment {
    BadgeDrawable appsBadge;
    MaterialButton button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_appointments, container, false);
        //Hooks
        button = view.findViewById(R.id.app_requests);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),DocReviewAppointments.class);
                startActivity(intent);
            }
        });


        return view;
    }
}