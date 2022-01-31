package com.example.firstproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {
    TextInputLayout phone_number;
    String phoneNo;
    CountryCodePicker countryCodePicker;
    DatabaseReference databaseReference;
    RelativeLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        databaseReference = FirebaseDatabase.getInstance().getReference("All_Users");

        //Hooks
        phone_number = findViewById(R.id.phoneNoLayout);
        countryCodePicker = findViewById(R.id.ccp_newPass);
        progressBar = findViewById(R.id.forgetPass_progressbar);
    }

    public void next_btn(View view) {
        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateNumber()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //Cast Phone Number
        String _phoneNumber = phone_number.getEditText().getText().toString().trim();
        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }
        phone_number.getEditText().setText(_phoneNumber);
        phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNumber;

        //Check user exists?
        Query query = databaseReference.orderByChild("phoneNo").equalTo(phoneNo);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    phone_number.setError(null);
                    phone_number.setErrorEnabled(false);

                    String _role = snapshot.child(phoneNo).child("role").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo", phoneNo);
                    intent.putExtra("forget_pass", true);
                    intent.putExtra("role",_role);
                    startActivity(intent);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForgetPassword.this, "No such User exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForgetPassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



        /*
    Check Connectivity
     */

    private boolean isConnected(ForgetPassword forgetPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgetPassword.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);

        builder.setMessage("An Internet connection is required to continue this process.")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), Start_Up.class));
                finish();
            }
        }).show();
    }

    public void back_btn(View view) {
        finish();
    }


    private boolean validateNumber() {
        String val = phone_number.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phone_number.setError("Please Enter Phone Number!");
            return false;
        } else {
            phone_number.setError(null);
            phone_number.setErrorEnabled(false);
            return true;
        }

    }

}