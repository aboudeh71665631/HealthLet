package com.example.firstproject.DoctorNavigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.firstproject.Database.Upload_Photo;
import com.example.firstproject.DoctorNavigation.DocPatients.ViewPatients;
import com.example.firstproject.R;
import com.example.firstproject.SignUp_1;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.listener.DismissListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorHomeFragment extends Fragment {
    RelativeLayout relativeLayout,viewPatients;
    CircleImageView doctorImage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    TextView doctor_name;
    String phoneNo;
    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);
        //Hooks
        relativeLayout = view.findViewById(R.id.add_patient_layout);
        viewPatients = view.findViewById(R.id.viewPatients);
        doctorImage = view.findViewById(R.id.doctor_profile_image);
        doctor_name = view.findViewById(R.id.dName_tv);
        fab = view.findViewById(R.id.dfab);
        //Init Database
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("doctor");
        phoneNo = getActivity().getIntent().getStringExtra("phoneNo");
        //Get DoctorInfo Query
        Query query = databaseReference.child(phoneNo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String dName = snapshot.child("fName").getValue(String.class);
                    String _imageUrl = snapshot.child("imageURL").getValue(String.class);
                    doctor_name.setText("Dr. " + dName);
                    Glide.with(getActivity().getApplicationContext()).load(_imageUrl).into(doctorImage);

                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "snapshot does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(DoctorHomeFragment.this)
                        .setDismissListener(new DismissListener() {
                            @Override
                            public void onDismiss() {
                                return;
                            }
                        })
                        .galleryOnly()
                        .cropSquare()//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SignUp_1.class);
                intent.putExtra("role","patient");
                intent.putExtra("add_patient",true);
                startActivity(intent);
            }
        });


        viewPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ViewPatients.class);
                startActivity(intent);
            }
        });


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
                        hashMap.put("imageURL",upload_photo.getmImageUrl());
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
        doctorImage.setImageURI(uri);
        uploadFile(uri);
    }

}