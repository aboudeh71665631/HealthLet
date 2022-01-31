package com.example.firstproject.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstproject.R;

import java.util.ArrayList;

public class Medication_List_Adapter extends RecyclerView.Adapter<Medication_List_Adapter.MyViewHolder> {

    Context context;
    ArrayList<MedicationHelperClass> list;

    public Medication_List_Adapter(Context context, ArrayList<MedicationHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Medication_List_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.medication_card_item, parent, false);
        return new Medication_List_Adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Medication_List_Adapter.MyViewHolder holder, int position) {
        MedicationHelperClass medicationHelperClass = list.get(position);
        holder.medName.setText(medicationHelperClass.getMedName());
        holder.medDose.setText(medicationHelperClass.getMedDose());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView medName, medDose;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medName_tv);
            medDose = itemView.findViewById(R.id.medDose_tv);


        }
    }
}
