package com.example.firstproject.Navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.Database.AlergyHelperClass;
import com.example.firstproject.Database.Alergy_List_Adapter;
import com.example.firstproject.Database.MedicationHelperClass;
import com.example.firstproject.Database.Medication_List_Adapter;
import com.example.firstproject.Database.Upload_Photo;
import com.example.firstproject.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {
    FloatingActionButton fab;
    CircleImageView image;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    RecyclerView medication_RecyclerView, alergy_RecyclerView, records_RecyclerView;

    Medication_List_Adapter medication_list_adapter;
    ArrayList<MedicationHelperClass> list;

    Alergy_List_Adapter alergy_list_adapter, record_list_adapter;
    ArrayList<AlergyHelperClass> alergyList, recordsList;

    CollapsingToolbarLayout collapsingToolbarLayout;
    String phoneNo, role;
    TextView fName, gender, age, bType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Hooks
        fName = view.findViewById(R.id.fName_tv);
        gender = view.findViewById(R.id.gender_tv);
        age = view.findViewById(R.id.age_tv);
        fab = view.findViewById(R.id.add_photo_fab);
        image = view.findViewById(R.id.user_profile_image);
        bType = view.findViewById(R.id.patient_bType);
        //Med Recycler
        medication_RecyclerView = view.findViewById(R.id.med_recycler);
        medication_RecyclerView.setHasFixedSize(true);
        medication_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        medication_list_adapter = new Medication_List_Adapter(getContext(), list);
        medication_RecyclerView.setAdapter(medication_list_adapter);

        //Allergy Recycler
        alergy_RecyclerView = view.findViewById(R.id.alergy_recycler);
        alergy_RecyclerView.setHasFixedSize(true);
        alergy_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        alergyList = new ArrayList<>();
        alergy_list_adapter = new Alergy_List_Adapter(getContext(), alergyList);
        alergy_RecyclerView.setAdapter(alergy_list_adapter);
        //Allergy Recycler
        records_RecyclerView = view.findViewById(R.id.records_recycler);
        records_RecyclerView.setHasFixedSize(true);
        records_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        alergyList = new ArrayList<>();
        record_list_adapter = new Alergy_List_Adapter(getContext(), recordsList);
        records_RecyclerView.setAdapter(alergy_list_adapter);

        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("patient");
        phoneNo = getActivity().getIntent().getStringExtra("phoneNo");
        role = getActivity().getIntent().getStringExtra("role");

        //Database
        Query query = FirebaseDatabase.getInstance().getReference("patient").orderByChild("phoneNo").equalTo(phoneNo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child(phoneNo).child("fName").getValue(String.class);
                    String[] _DoB = snapshot.child(phoneNo).child("date").getValue(String.class).split("/");
                    String _gender = snapshot.child(phoneNo).child("gender").getValue(String.class);
                    String _imageUrl = snapshot.child(phoneNo).child("imageURL").getValue(String.class);
                    String _bloodType = snapshot.child(phoneNo).child("bloodType").getValue(String.class);

                    int _age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(_DoB[2]);

                    fName.setText(name);
                    collapsingToolbarLayout.setTitle(name);
                    age.setText(String.valueOf(_age));
                    gender.setText(_gender);
                    bType.setText(_bloodType);
                    Glide.with(getActivity().getApplicationContext()).load(_imageUrl).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //Query for Medications
        Query medQuery = FirebaseDatabase.getInstance().getReference("patient").child(phoneNo).child("Medications");
        medQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MedicationHelperClass medicationHelperClass = dataSnapshot.getValue(MedicationHelperClass.class);
                    list.add(medicationHelperClass);
                }
                medication_list_adapter.notifyDataSetChanged();
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Query for Allergies
        Query alergyQuery = FirebaseDatabase.getInstance().getReference("patient").child(phoneNo).child("Allergies");
        alergyQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AlergyHelperClass alergyHelperClass = dataSnapshot.getValue(AlergyHelperClass.class);
                    alergyList.add(alergyHelperClass);
                }
                alergy_list_adapter.notifyDataSetChanged();
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        /*//Query for Records
        Query recordsQuery = FirebaseDatabase.getInstance().getReference("patient").child(phoneNo).child("Records");
        recordsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        AlergyHelperClass alergyHelperClass = dataSnapshot.getValue(AlergyHelperClass.class);
                        recordsList.add(alergyHelperClass);
                    }
                    alergy_list_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(HomeFragment.this)
                        .galleryOnly()
                        .cropSquare()//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void uploadFile(Uri uri) {
//        System.currentTimeMillis()
        StorageReference fileRef = storageReference.child(phoneNo);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Upload_Photo upload_photo = new Upload_Photo(uri.toString());
                        String upload_photo_id = databaseReference.push().getKey();
                        HashMap hashMap = new HashMap();
                        hashMap.put("imageURL", upload_photo.getmImageUrl());
                        databaseReference.child(phoneNo).updateChildren(hashMap);
                        Toast.makeText(getActivity().getApplicationContext(), "Upload Successful!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //Set progress bar
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Uploading Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        image.setImageURI(uri);
        uploadFile(uri);
    }
}