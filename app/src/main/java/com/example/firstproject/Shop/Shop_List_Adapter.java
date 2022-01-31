package com.example.firstproject.Shop;

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

public class Shop_List_Adapter extends RecyclerView.Adapter<Shop_List_Adapter.MyViewHolder> {

    Context context;
    ArrayList<ShopHelperClass> list;

    public Shop_List_Adapter(Context context, ArrayList<ShopHelperClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Shop_List_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shop_item,parent,false);
        return new Shop_List_Adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Shop_List_Adapter.MyViewHolder holder, int position) {
        ShopHelperClass shopHelperClass = list.get(position);
        holder.iName.setText(shopHelperClass.getiName());
        holder.iDescription.setText(shopHelperClass.getiDescription());
        holder.iPrice.setText(shopHelperClass.getiPrice());
        Glide.with(context).load(shopHelperClass.getiImage()).into(holder.iImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView iName,iDescription,iPrice;
        ImageView iImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iName = itemView.findViewById(R.id.iName);
            iDescription = itemView.findViewById(R.id.iDescription);
            iPrice = itemView.findViewById(R.id.iPrice);
            iImage = itemView.findViewById(R.id.iImage);

        }
    }
}