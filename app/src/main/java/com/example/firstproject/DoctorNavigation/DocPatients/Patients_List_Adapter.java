package com.example.firstproject.DoctorNavigation.DocPatients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Patients_List_Adapter extends RecyclerView.Adapter<Patients_List_Adapter.MyViewHolder> {
    public OnItemClickedListener mListener;
    Context context;
    ArrayList<PatientHelperClass> list;

    public interface OnItemClickedListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickedListener listener){
        mListener = listener;
    }

    public Patients_List_Adapter(Context context, ArrayList<PatientHelperClass> list) {
        this.context = context;
        this.list = list;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fName, email, phoneNo, dob, address;
        CircleImageView imageView;
        ImageView viewBtn;


        public MyViewHolder(@NonNull View itemView, OnItemClickedListener listener) {
            super(itemView);
            fName = itemView.findViewById(R.id.patient_name);
            email = itemView.findViewById(R.id.patient_email);
            phoneNo = itemView.findViewById(R.id.patient_phoneNo);
            dob = itemView.findViewById(R.id.patient_dob);
            address = itemView.findViewById(R.id.patient_address);
            imageView = itemView.findViewById(R.id.patient_imgview);
            viewBtn = itemView.findViewById(R.id.edit_patient_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }

    @NonNull
    @Override
    public Patients_List_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_patient_item, parent, false);
        return new Patients_List_Adapter.MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Patients_List_Adapter.MyViewHolder holder, int position) {
        PatientHelperClass patientHelperClass = list.get(position);
        holder.fName.setText(patientHelperClass.getfName());
        holder.email.setText(patientHelperClass.getEmail());
        holder.phoneNo.setText(patientHelperClass.getPhoneNo());
        holder.dob.setText(patientHelperClass.getDate());
        holder.address.setText(patientHelperClass.getAddress());
        Glide.with(context).load(patientHelperClass.getImageUri()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<PatientHelperClass> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }




}