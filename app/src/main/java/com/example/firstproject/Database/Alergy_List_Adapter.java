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

public class Alergy_List_Adapter extends RecyclerView.Adapter<Alergy_List_Adapter.MyViewHolder> {

    Context context;
    ArrayList<AlergyHelperClass> list;

    public Alergy_List_Adapter(Context context, ArrayList<AlergyHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Alergy_List_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.text_cardview_item, parent, false);
        return new Alergy_List_Adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Alergy_List_Adapter.MyViewHolder holder, int position) {
        AlergyHelperClass alergyHelperClass = list.get(position);
        holder.text.setText(alergyHelperClass.getAlergy());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.cardView_text);
        }
    }
}
