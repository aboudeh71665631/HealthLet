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

public class Doctor_List_Adapter extends RecyclerView.Adapter<Doctor_List_Adapter.MyViewHolder> {

    Context context;
    ArrayList<DoctorHelperClass> list;

    public Doctor_List_Adapter(Context context, ArrayList<DoctorHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Doctor_List_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.doctor_list_item,parent,false);
        return new Doctor_List_Adapter.MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull Doctor_List_Adapter.MyViewHolder holder, int position) {
        DoctorHelperClass doctorHelperClass = list.get(position);
        holder.fName.setText(doctorHelperClass.getfName());
        holder.specialization.setText(doctorHelperClass.getSpecialization());
        holder.locations.setText(doctorHelperClass.getLocations());
        Glide.with(context).load(doctorHelperClass.getImageUri()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fName,specialization,locations;
        CircleImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.doctor_fName);
            specialization = itemView.findViewById(R.id.specialization_tv);
            locations = itemView.findViewById(R.id.location_tv);
            imageView = itemView.findViewById(R.id.doctor_img);

        }
    }
}
