package com.example.firstproject;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputLayout;

public class SignUp_1 extends AppCompatActivity {


    //Vars
    AnimationDrawable animationDrawable;
    ConstraintLayout constraintLayout;

    ImageView backBtn;
    TextView titleText;
    Button next;
    RelativeLayout signupLogin;

    TextInputLayout email, fName, uName, password, confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

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
        email = findViewById(R.id.email);
        fName = findViewById(R.id.fName);
        uName = findViewById(R.id.uName);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        Boolean addPatient = getIntent().getBooleanExtra("add_patient",false);
        if(addPatient){
            titleText.setText("Add Patient");
        }
    }


    public void back_btn(View view) {
        finish();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void callNextSignupScreen(View view) {
        if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword() | !confirmPassword()){
            return;
        }
        //Get all values from previous Activity + this Activity
        Boolean addPatient = getIntent().getBooleanExtra("add_patient",false);
        String _role = getIntent().getStringExtra("role");
        String _fName = fName.getEditText().getText().toString().trim();
        String _uName = uName.getEditText().getText().toString().trim();
        String _email = email.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(), SignUp_2.class);
        //Pass all values to next Activity
        intent.putExtra("add_patient",addPatient);
        intent.putExtra("role",_role);
        intent.putExtra("fName",_fName);
        intent.putExtra("uName",_uName);
        intent.putExtra("email",_email);
        intent.putExtra("password",_password);


        //Transition
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(backBtn, "trans_back_button");
        pairs[1] = new Pair<View, String>(titleText, "trans_signup_text");
        pairs[2] = new Pair<View, String>(next, "trans_next_btn");
        pairs[3] = new Pair<View, String>(signupLogin, "trans_signup_to_login");
        pairs[4] = new Pair<View, String>(constraintLayout, "trans_layout");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp_1.this, pairs);
        startActivity(intent, options.toBundle());

    }

    //Validation
    private boolean validateFullName() {
        String val = fName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fName.setError("Field Cannot be Empty!");
            return false;
        } else {
            fName.setError(null);
            fName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName() {
        String val = uName.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            uName.setError("Field Cannot be Empty!");
            return false;
        } else if (val.length() > 20) {
            uName.setError("Username should be less than 20 characters!");
            return false;
        } else if (!val.matches(checkspaces)) {
            uName.setError("Username cannot have whitespaces!");
            return false;
        } else {
            uName.setError(null);
            uName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field Cannot be Empty!");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        String checkPass= "^" +
                "(?=.*[0-9])" +   //1 digit
                "(?=.*[a-z])" +   //1 lower case
                "(?=.*[A-Z])" +   //1 upper case
                "(?=.*[a-zA-Z])" +  //any letter
                //"(?=.*[ @#$X^&+=])" + //at least 1 special char
                "(?=\\S+$)" +       //no white spaces
                ".{8,}" +           //at least 8 chars
                "$";

        if (val.isEmpty()) {
            password.setError("Field Cannot be Empty!");
            return false;
        } else if (!val.matches(checkPass)) {
            password.setError("Password Must Consist of at least 8 characters of which: 1 Uppercase, 1 Lowercase, 1 Digit, and cannot contain spaces");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean confirmPassword() {
        String val = password.getEditText().getText().toString().trim();
        String cval = confirm_password.getEditText().getText().toString().trim();
    if (!val.equals(cval)){
        confirm_password.setError("Passwords do not match!");
        return false;
    }else{
        confirm_password.setError(null);
        confirm_password.setErrorEnabled(false);
        return true;
    }
    }


}