package com.example.firstproject.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstproject.R;
import com.example.firstproject.Shop.ShopHelperClass;
import com.example.firstproject.Shop.Shop_List_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Shop_List_Adapter shop_list_adapter;
    ArrayList<ShopHelperClass> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = view.findViewById(R.id.shop_Recycler);
        databaseReference = FirebaseDatabase.getInstance().getReference("Shop");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        list = new ArrayList<>();
        shop_list_adapter = new Shop_List_Adapter(getActivity().getApplicationContext(), list);
        recyclerView.setAdapter(shop_list_adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ShopHelperClass shopHelperClass = dataSnapshot.getValue(ShopHelperClass.class);
                    list.add(shopHelperClass);
                }

                shop_list_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}