package com.example.firstproject.DoctorNavigation.DocPatients;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.firstproject.Database.Upload_Photo;
import com.example.firstproject.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.listener.DismissListener;
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

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctor_Edit_Patient extends AppCompatActivity {
    FloatingActionButton fab;
    CircleImageView image;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String phoneNo;
    TextView fName, gender, age, bTypeTv;
    long allergy_id,medication_id,record_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_patient);
        //Hooks
        fName = findViewById(R.id.fName_tv);
        gender = findViewById(R.id.gender_tv);
        age = findViewById(R.id.age_tv);
        fab = findViewById(R.id.add_photo_fab_2);
        bTypeTv = findViewById(R.id.bTypeTV);
        image = findViewById(R.id.user_profile_image_2);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("patient");
        phoneNo = getIntent().getStringExtra("phoneNo");
        //Database
        Query query = FirebaseDatabase.getInstance().getReference("patient").orderByChild("phoneNo").equalTo(phoneNo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child(phoneNo).child("fName").getValue(String.class);
                    String[] _DoB = snapshot.child(phoneNo).child("date").getValue(String.class).split("/");
                    String _gender = snapshot.child(phoneNo).child("gender").getValue(String.class);
                    String _imageUrl = snapshot.child(phoneNo).child("imageUri").getValue(String.class);
                    String _bType = snapshot.child(phoneNo).child("bloodType").getValue(String.class);
                    allergy_id = snapshot.child(phoneNo).child("Allergies").getChildrenCount();
                    medication_id = snapshot.child(phoneNo).child("Medications").getChildrenCount();
                    record_id = snapshot.child(phoneNo).child("Records").getChildrenCount();
                    int _age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(_DoB[2]);

                    fName.setText(name);
                    collapsingToolbarLayout.setTitle(name);
                    age.setText(String.valueOf(_age));
                    gender.setText(_gender);
                    bTypeTv.setText(_bType);
                    Glide.with(getApplicationContext()).load(_imageUrl).into(image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Doctor_Edit_Patient.this)
                        .setDismissListener(new DismissListener() {
                            @Override
                            public void onDismiss() {
                                Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .galleryOnly()
                        .cropSquare()//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    public void bTypeSelect(View view) {
        String[] bTypeArray = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Doctor_Edit_Patient.this)
                .setTitle("Blood Type:")
                .setSingleChoiceItems(bTypeArray, 0, null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pos = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        String bType = bTypeArray[pos];
                        bTypeTv.setText(bType);
                        HashMap hashMap = new HashMap();
                        hashMap.put("bloodType", bType);
                        databaseReference.child(phoneNo).updateChildren(hashMap);
                    }
                })
                .setNeutralButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void uploadFile(Uri uri) {
        StorageReference fileRef = storageReference.child(phoneNo);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Upload_Photo upload_photo = new Upload_Photo(uri.toString());
                        HashMap hashMap = new HashMap();
                        hashMap.put("imageUri", upload_photo.getmImageUrl());
                        databaseReference.child(phoneNo).updateChildren(hashMap);
                        Toast.makeText(getApplicationContext(), "Upload Successful!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Uploading Failed!", Toast.LENGTH_SHORT).show();
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

    public void add_medication(View view) {
        AlertDialog.Builder medicationDialog = new AlertDialog.Builder(Doctor_Edit_Patient.this);
        View customView = LayoutInflater.from(Doctor_Edit_Patient.this).inflate(R.layout.medication_dialog,null);
        Button btnClose = customView.findViewById(R.id.med_cancel_btn);
        Button btnSave = customView.findViewById(R.id.med_save_btn);
        EditText medName = customView.findViewById(R.id.med_name);
        EditText medDose = customView.findViewById(R.id.med_dose);
        medicationDialog.setView(customView);

        final AlertDialog customMedDialog = medicationDialog.create();
        customMedDialog.show();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMedDialog.cancel();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _medName = medName.getText().toString().trim();
                String _medDose = medDose.getText().toString().trim();
                if (!_medName.isEmpty() && !_medDose.isEmpty()){
                    HashMap hashMap = new HashMap();
                    hashMap.put("medName",_medName);
                    hashMap.put("medDose",_medDose);
                    databaseReference.child(phoneNo).child("Medications").child(String.valueOf(medication_id+1)).updateChildren(hashMap);
                    Toast.makeText(getApplicationContext(), "Medication Added Successfully!", Toast.LENGTH_SHORT).show();
                    customMedDialog.cancel();
                }else{
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty", Toast.LENGTH_SHORT).show();;
                }
            }
        });
    }
    public void add_alergy(View view) {
        AlertDialog.Builder alergyDialog = new AlertDialog.Builder(Doctor_Edit_Patient.this);
        View customView = LayoutInflater.from(Doctor_Edit_Patient.this).inflate(R.layout.add_alergy_record_dialog,null);
        Button btnClose = customView.findViewById(R.id.dialog_cancel_btn);
        Button btnSave = customView.findViewById(R.id.dialog_save_btn);
        EditText alergyText = customView.findViewById(R.id.dialog_text);
        TextView title = customView.findViewById(R.id.dialog_add_title);
        alergyDialog.setView(customView);
        title.setText("Add an Allergy");
        final AlertDialog customMedDialog = alergyDialog.create();
        customMedDialog.show();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMedDialog.cancel();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _text = alergyText.getText().toString().trim();
                if (!_text.isEmpty()){
                    HashMap hashMap = new HashMap();
                    hashMap.put("Allergy",_text);
                    databaseReference.child(phoneNo).child("Allergies").child(String.valueOf(allergy_id+1)).updateChildren(hashMap);
                    Toast.makeText(getApplicationContext(), "Allergy Added Successfully!", Toast.LENGTH_SHORT).show();
                    customMedDialog.cancel();
                }else{
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty", Toast.LENGTH_SHORT).show();;
                }
            }
        });
    }
    public void add_record(View view) {

        AlertDialog.Builder alergyDialog = new AlertDialog.Builder(Doctor_Edit_Patient.this);
        View customView = LayoutInflater.from(Doctor_Edit_Patient.this).inflate(R.layout.add_alergy_record_dialog,null);
        Button btnClose = customView.findViewById(R.id.dialog_cancel_btn);
        Button btnSave = customView.findViewById(R.id.dialog_save_btn);
        EditText recordText = customView.findViewById(R.id.dialog_text);
        recordText.setHint("Enter Record");
        TextView title = customView.findViewById(R.id.dialog_add_title);
        alergyDialog.setView(customView);
        title.setText("Add a Record");
        final AlertDialog customMedDialog = alergyDialog.create();
        customMedDialog.show();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMedDialog.cancel();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _text = recordText.getText().toString().trim();
                if (!_text.isEmpty()){
                    HashMap hashMap = new HashMap();
                    hashMap.put("Record",_text);
                    databaseReference.child(phoneNo).child("Records").child(String.valueOf(record_id+1)).updateChildren(hashMap);
                    Toast.makeText(getApplicationContext(), "Allergy Added Successfully!", Toast.LENGTH_SHORT).show();
                    customMedDialog.cancel();
                }else{
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty", Toast.LENGTH_SHORT).show();;
                }
            }
        });
    }

    public void back_btn(View view) {finish();
    }
}