package com.example.firstproject;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class SignUp_3 extends AppCompatActivity {
    TextInputLayout phone;
    CountryCodePicker countryCodePicker;
    ConstraintLayout constraintLayout;
    RelativeLayout progressBar;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);
        titleText = findViewById(R.id.signup_text_1);
        phone = findViewById(R.id.phone_number);
        countryCodePicker = findViewById(R.id.country_picker);
        constraintLayout = findViewById(R.id.SignUp_Layout);
        progressBar = findViewById(R.id.signup_progressbar);
        Boolean addPatient = getIntent().getBooleanExtra("add_patient",false);
        if(addPatient){
            titleText.setText("Add Patient");
        }

    }

    public void back_btn(View view) {
        finish();
    }

    public void callNextSignupScreen(View view) {

        if (!validatePhoneNo()) {
            return;
        }
        //Get all values from previous Activity + this Activity
        Boolean addPatient = getIntent().getBooleanExtra("add_patient", false);
        String _role = getIntent().getStringExtra("role");
        String _fName = getIntent().getStringExtra("fName");
        String _uName = getIntent().getStringExtra("uName");
        String _email = getIntent().getStringExtra("email");
        String _password = getIntent().getStringExtra("password");
        String _date = getIntent().getStringExtra("date");
        String _gender = getIntent().getStringExtra("gender");
        String _location = getIntent().getStringExtra("location");
        String _specialization = getIntent().getStringExtra("specialization");

        String _getUserPhoneNo = phone.getEditText().getText().toString().trim();
        if (_getUserPhoneNo.charAt(0) == '0') {
            _getUserPhoneNo = _getUserPhoneNo.substring(1);
        }
        String _phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + _getUserPhoneNo;
        //Check Connection
        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Check if User Exist
        Query query = FirebaseDatabase.getInstance().getReference("All_Users").orderByChild("phoneNo").equalTo(_phoneNo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phone.setError("User already exists!");
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    phone.setError(null);
                    phone.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

                    //Pass all values to next Activity
                    intent.putExtra("add_patient", addPatient);
                    intent.putExtra("role", _role);
                    intent.putExtra("fName", _fName);
                    intent.putExtra("uName", _uName);
                    intent.putExtra("email", _email);
                    intent.putExtra("password", _password);
                    intent.putExtra("date", _date);
                    intent.putExtra("gender", _gender);
                    intent.putExtra("specialization",_specialization);
                    intent.putExtra("location",_location);
                    intent.putExtra("phoneNo", _phoneNo);

                    //Transition
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(constraintLayout, "trans_layout");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp_3.this, pairs);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUp_3.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validatePhoneNo() {
        String val = phone.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phone.setError("Please Enter Phone Number!");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

        /*
    Check Connectivity
     */

    private boolean isConnected(SignUp_3 signUp_3) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signUp_3.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp_3.this);

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

}