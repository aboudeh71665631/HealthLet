package com.example.firstproject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputLayout;

public class SignUp_2b_Doctor extends AppCompatActivity {

    //Vars
    ImageView backBtn;
    TextView titleText;
    Button next;
    RelativeLayout signupLogin;

    AnimationDrawable animationDrawable;
    ConstraintLayout constraintLayout;


    String[] items = {
            "Allergy and immunology", "Anesthesiology", "Dermatology", "Diagnostic radiology", "Emergency medicine", "Family medicine"
            , "Internal medicine", "Medical genetics", "Neurology", "Nuclear medicine", "Obstetrics and gynecology", "Ophthalmology", "Pathology"
            , "Pediatrics", "Physical medicine and rehabilitation", "Preventive medicine", "Psychiatry", "Radiation oncology", "Surgery", "Urology"
    };
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String specialization;
    TextInputLayout TL_specialization, TL_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2b_doctor);

        //Bg Animation
        constraintLayout = findViewById(R.id.SignUp_Layout);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        //Hooks
        backBtn = findViewById(R.id.signup_back_button_1);
        titleText = findViewById(R.id.signup_text_1);
        next = findViewById(R.id.next_button_1);
        signupLogin = findViewById(R.id.signup_to_login_1);
        autoCompleteTextView = findViewById(R.id.autoComplete_txt);
        TL_location = findViewById(R.id.TL_location);
        TL_specialization = findViewById(R.id.TL_specialization);

        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_specialization_item, items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                specialization = parent.getItemAtPosition(position).toString();
            }
        });

    }

    public void back_btn(View view) {
        finish();
    }

    public void callNextSignupScreen(View view){
        if(!validateLocation()|!validateSpec()){
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

        String _specialization = specialization;
        String _location = TL_location.getEditText().getText().toString().trim();

        Intent intent = new Intent(this,SignUp_3.class);
        //Pass all values to next Activity
        intent.putExtra("add_patient",addPatient);
        intent.putExtra("role",_role);
        intent.putExtra("fName",_fName);
        intent.putExtra("uName",_uName);
        intent.putExtra("email",_email);
        intent.putExtra("password",_password);
        intent.putExtra("date",_date);
        intent.putExtra("gender",_gender);

        intent.putExtra("specialization",_specialization);
        intent.putExtra("location",_location);

        //Transition
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(backBtn, "trans_back_button");
        pairs[1] = new Pair<View, String>(titleText, "trans_signup_text");
        pairs[2] = new Pair<View, String>(next, "trans_next_btn");
        pairs[3] = new Pair<View, String>(signupLogin, "trans_signup_to_login");
        pairs[4] = new Pair<View, String>(constraintLayout, "trans_layout");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp_2b_Doctor.this, pairs);
        startActivity(intent, options.toBundle());

    }


    /*
     Validation
    */

    private boolean validateSpec() {
        if (specialization.trim().isEmpty()) {
            TL_specialization.setError("Please select specialization!");
            return false;
        } else {
            TL_specialization.setError(null);
            TL_specialization.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLocation() {
        String val = TL_location.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            TL_location.setError("Field Cannot be Empty!");
            return false;
        } else {
            TL_location.setError(null);
            TL_location.setErrorEnabled(false);
            return true;
        }
    }


}