package com.example.firstproject.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.firstproject.Navigation.Appointments.BookAppointment;
import com.example.firstproject.R;

public class AppointmentsFragment extends Fragment {
    Button bookAppBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        //Hooks
        bookAppBtn = view.findViewById(R.id.book_appBtn);

        bookAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BookAppointment.class);
                startActivity(intent);
            }
        });

        return view;
    }
}