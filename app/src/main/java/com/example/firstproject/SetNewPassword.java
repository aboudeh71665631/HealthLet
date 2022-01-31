package com.example.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetNewPassword extends AppCompatActivity {
    TextInputLayout newPass,cNewPass;
    String phoneNo,role;
    DatabaseReference databaseReference;
    HashMap hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        //Hooks
        newPass = findViewById(R.id.new_pass);
        cNewPass = findViewById(R.id.confirm_new_password);

        phoneNo = getIntent().getStringExtra("phoneNo");
        role = getIntent().getStringExtra("role");
        databaseReference = FirebaseDatabase.getInstance().getReference("All_Users");



    }

    public void back_btn(View view) {
        finish();
    }

    public void setNewPass(View view) {
        if(!validatePassword()|!confirmPassword()){
            return;
        }
        String password = newPass.getEditText().getText().toString().trim();
        hashMap.put("password",password);
        databaseReference.child(phoneNo).updateChildren(hashMap);
        FirebaseDatabase.getInstance().getReference(role).child(phoneNo).updateChildren(hashMap);

        Intent intent = new Intent(this,FPSuccessMessage.class);
        intent.putExtra("phoneNo",phoneNo);
        startActivity(intent);

    }

    private boolean validatePassword() {
        String val = newPass.getEditText().getText().toString().trim();
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
            newPass.setError("Field Cannot be Empty!");
            return false;
        } else if (!val.matches(checkPass)) {
            newPass.setError("Password Must Consist of at least 8 characters of which: 1 Uppercase, 1 Lowercase, 1 Digit, and cannot contain spaces");
            return false;
        } else {
            newPass.setError(null);
            newPass.setErrorEnabled(false);
            return true;
        }
    }

    private boolean confirmPassword() {
        String val = newPass.getEditText().getText().toString().trim();
        String cval = cNewPass.getEditText().getText().toString().trim();
        if (!val.equals(cval)){
            cNewPass.setError("Passwords do not match!");
            return false;
        }else{
            cNewPass.setError(null);
            cNewPass.setErrorEnabled(false);
            return true;
        }
    }
}