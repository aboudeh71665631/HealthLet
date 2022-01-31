package com.example.firstproject.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Appointment_List_Adapter extends RecyclerView.Adapter<Appointment_List_Adapter.MyViewHolder> {

    Context context;
    ArrayList<AppointmentHelperClass> list;

    public Appointment_List_Adapter(Context context, ArrayList<AppointmentHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Appointment_List_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.doctor_appointment_request,parent,false);
        return new Appointment_List_Adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Appointment_List_Adapter.MyViewHolder holder, int position) {
        AppointmentHelperClass appointmentHelperClass = list.get(position);
        holder.fName.setText(appointmentHelperClass.getfName());
        holder.time.setText(appointmentHelperClass.getTime());
        holder.locations.setText(appointmentHelperClass.getLocation());
        Glide.with(context).load(appointmentHelperClass.getImageUri()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fName,time,locations;
        CircleImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.pfName_tv);
            time = itemView.findViewById(R.id.appTime_tv);
            locations = itemView.findViewById(R.id.appLocation_tv);
            imageView = itemView.findViewById(R.id.patient_img);

        }
    }
}

