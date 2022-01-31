package com.example.firstproject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SignUp_0 extends AppCompatActivity {
    //Vars
    AnimationDrawable animationDrawable;
    ConstraintLayout constraintLayout;
    ImageView backBtn;
    TextView titleText;
    LinearLayout roleLayout;
    String _role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up0);

        //Hooks
        constraintLayout = findViewById(R.id.SignUp_0_Layout);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        backBtn = findViewById(R.id.signup_back_button_1);
        titleText = findViewById(R.id.signup_text_1);

        roleLayout = findViewById(R.id.roleLayout);

        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

    }

    public void back_btn(View view) {
        finish();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.onClick_patient:
                _role ="patient";
                break;
            case R.id.onClick_doctor:
                _role ="doctor";
                break;
            case R.id.onClick_er:
                _role ="er";
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        callNextSignupScreen(view);
    }
    public void callNextSignupScreen(View view) {
        Intent intent = new Intent(getApplicationContext(),SignUp_1.class);
        //Pass values to next Activity
        intent.putExtra("role",_role);
        //Transition
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View,String>(backBtn,"trans_back_button");
        pairs[1] = new Pair<View,String>(titleText,"trans_signup_text");
        pairs[2] = new Pair<View,String>(constraintLayout,"trans_layout");
        pairs[3] = new Pair<View,String>(roleLayout,"trans_next_btn");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp_0.this, pairs);

        startActivity(intent,options.toBundle());

    }


}