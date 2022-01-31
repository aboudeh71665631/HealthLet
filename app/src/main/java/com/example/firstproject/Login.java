package com.example.firstproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.firstproject.DoctorNavigation.DoctorMainNavigation;
import com.example.firstproject.Navigation.MainNavigation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    ConstraintLayout constraintLayout;
    CountryCodePicker countryCodePicker;
    TextInputLayout phone_number, password;
    RelativeLayout progressBar;
    Boolean forget_pass;
    CheckBox rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //Background animation
        constraintLayout = findViewById(R.id.login_layout);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();
        //Hooks
        countryCodePicker = findViewById(R.id.country_picker);
        phone_number = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progressbar);

        forget_pass = getIntent().getBooleanExtra("forget_pass",false);
        if(forget_pass){
            phone_number.getEditText().setText(getIntent().getStringExtra("phoneNo").substring(3));
        }
    }


    public void back_btn(View view) {
        finish();
    }

    public void user_login(View view) {
        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateNumber() | !validatePassword()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //Get Credentials
        String _phoneNumber = phone_number.getEditText().getText().toString().trim();
        String pass = password.getEditText().getText().toString().trim();
        if (_phoneNumber.charAt(0) == '0') {
            _phoneNumber = _phoneNumber.substring(1);
        }
        phone_number.getEditText().setText(_phoneNumber);
        String phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNumber;

        /*
        Verify Data and get Info from database
         */
        Query query = FirebaseDatabase.getInstance().getReference("All_Users").orderByChild("phoneNo").equalTo(phoneNo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phone_number.setError(null);
                    phone_number.setErrorEnabled(false);

                    String system_pass = snapshot.child(phoneNo).child("password").getValue(String.class);
                    String _role = snapshot.child(phoneNo).child("role").getValue(String.class);
                    if (system_pass.equals(pass)) {
                        password.setError(null);
                        password.setErrorEnabled(false);
                        progressBar.setVisibility(View.GONE);

//                        //Create Session
//                        SessionManager sessionManager = new SessionManager(Login.this);
//                        sessionManager.createLoginSession(phoneNo);

                        if (_role.equals("doctor")) {
                            Intent intent = new Intent(getApplicationContext(), DoctorMainNavigation.class);
                            intent.putExtra("phoneNo", phoneNo);
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainNavigation.class);
                            intent.putExtra("phoneNo", phoneNo);
                            startActivity(intent);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Password is Invalid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "No such User exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    /*
    Check Connectivity
     */

    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

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

    /*
    Validation
     */

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            password.setError("Field Cannot be Empty!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
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

    public void forgetPass(View view) {
        Intent intent = new Intent(this,ForgetPassword.class);
        startActivity(intent);
    }
}