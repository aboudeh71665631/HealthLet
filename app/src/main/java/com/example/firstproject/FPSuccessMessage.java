package com.example.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FPSuccessMessage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpsuccess_message);


    }

    public void back_btn(View view) {
        finish();
    }

    public void callLogin(View view) {
        String phoneNo = getIntent().getStringExtra("phoneNo");
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("phoneNo",phoneNo);
        intent.putExtra("forget_pass",true);
        startActivity(intent);
    }
}