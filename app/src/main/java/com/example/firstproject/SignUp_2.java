package com.example.firstproject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;

public class SignUp_2 extends AppCompatActivity {

    //Vars
    ImageView backBtn;
    TextView titleText;
    Button next;
    RelativeLayout signupLogin;
    RadioGroup gRadioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    AnimationDrawable animationDrawable;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

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
        gRadioGroup = findViewById(R.id.gender_radiogroup);
        datePicker = findViewById(R.id.bDate);
        Boolean addPatient = getIntent().getBooleanExtra("add_patient",false);
        if(addPatient){
            titleText.setText("Add Patient");
        }


    }

    public void back_btn(View view) {
        finish();
    }

    public void callNextSignupScreen(View view) {
        if (!validateAge() | !validateGender()) {
            return;
        }

        selectedGender = findViewById(gRadioGroup.getCheckedRadioButtonId());
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        //Get all values from previous Activity + this Activity
        Boolean addPatient = getIntent().getBooleanExtra("add_patient", false);
        String _role = getIntent().getStringExtra("role");
        String _fName = getIntent().getStringExtra("fName");
        String _uName = getIntent().getStringExtra("uName");
        String _email = getIntent().getStringExtra("email");
        String _password = getIntent().getStringExtra("password");

        String _date = day + "/" + month + "/" + year;
        String _gender = selectedGender.getText().toString();
        Intent intent;
        if (_role.equals("doctor")) {
            intent = new Intent(getApplicationContext(), SignUp_2b_Doctor.class);
        } else {
            intent = new Intent(getApplicationContext(), SignUp_3.class);
        }
        //Pass all values to next Activity
        intent.putExtra("add_patient", addPatient);
        intent.putExtra("role", _role);
        intent.putExtra("fName", _fName);
        intent.putExtra("uName", _uName);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);
        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);

        //Transition
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(backBtn, "trans_back_button");
        pairs[1] = new Pair<View, String>(titleText, "trans_signup_text");
        pairs[2] = new Pair<View, String>(next, "trans_next_btn");
        pairs[3] = new Pair<View, String>(signupLogin, "trans_signup_to_login");
        pairs[4] = new Pair<View, String>(constraintLayout, "trans_layout");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp_2.this, pairs);
        startActivity(intent, options.toBundle());

    }

    private boolean validateGender() {
        if (gRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;
        if (isAgeValid < 14) {
            Toast.makeText(this, "You are not eligible to apply!", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }
}