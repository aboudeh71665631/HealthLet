package com.example.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.firstproject.Database.DoctorHelperClass;
import com.example.firstproject.Database.UserHelperClass;
import com.example.firstproject.DoctorNavigation.DocPatients.Doctor_Edit_Patient;
import com.example.firstproject.DoctorNavigation.DocPatients.PatientHelperClass;
import com.example.firstproject.DoctorNavigation.DoctorMainNavigation;
import com.example.firstproject.Navigation.MainNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    PinView pinFromUser;
    String codeBySystem;
    String role, fName, uName, email, password, date, gender, phoneNo, location, specialization;
    TextView pNumber;
    Boolean addPatient, forgetPass;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        //Hooks
        pinFromUser = findViewById(R.id.pin_view);
        pNumber = findViewById(R.id.pNumber);
        //Get all values from previous Activity + this Activity
        addPatient = getIntent().getBooleanExtra("add_patient", false);
        forgetPass = getIntent().getBooleanExtra("forget_pass", false);
        role = getIntent().getStringExtra("role");
        fName = getIntent().getStringExtra("fName");
        uName = getIntent().getStringExtra("uName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        phoneNo = getIntent().getStringExtra("phoneNo");
        location = getIntent().getStringExtra("location");
        specialization = getIntent().getStringExtra("specialization");

        pNumber.setText(phoneNo);

        FirebaseApp.initializeApp(/*context=*/ getApplicationContext());
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        sendVerificationCodeToUser(phoneNo);
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerifyOTP.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Toast.makeText(VerifyOTP.this, "Code Sent!", Toast.LENGTH_SHORT).show();
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    Toast.makeText(VerifyOTP.this, "SMS", Toast.LENGTH_SHORT).show();
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            storeNewUsersData();
                            Toast.makeText(VerifyOTP.this, "Verification Successful!", Toast.LENGTH_SHORT).show();
                            if (addPatient) {
                                intent = new Intent(getApplicationContext(), Doctor_Edit_Patient.class);
                            } else if (forgetPass) {
                                intent = new Intent(getApplicationContext(), SetNewPassword.class);
                            } else if (role.equals("doctor")) {
                                intent = new Intent(getApplicationContext(), DoctorMainNavigation.class);
                            } else {
                                intent = new Intent(getApplicationContext(), MainNavigation.class);
                            }
                            //Set Emergency Services Flag.
                            if(role.equals("er")){
                                HashMap hashmap = new HashMap();
                                hashmap.put("er",true);
                                FirebaseDatabase.getInstance().getReference("patient").child(phoneNo).updateChildren(hashmap);
                            }

                            //ToDo Create session instead of fetching data in next Activity

                            intent.putExtra("phoneNo", phoneNo);
                            startActivity(intent);
                            //Pass Data

                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(VerifyOTP.this, "Verification Not Completed! Try Again.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerifyOTP.this, "Invalid Verification Code!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void storeNewUsersData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference(role);
        UserHelperClass addNewUser = new UserHelperClass(role, fName, uName, email, password, date, gender, phoneNo);
        PatientHelperClass addNewPatient = new PatientHelperClass(fName, email, date, phoneNo, gender);
        DoctorHelperClass addNewDoctor = new DoctorHelperClass(fName, specialization, location);
        //Add Doctor Helper Class
        if (role.equals("patient") | role.equals("er")) {
            rootNode.getReference("patient").child(phoneNo).setValue(addNewPatient);
        } else if (role.equals("doctor")) {  //Todo Add doctor specialization and location page to signup.
            reference.child(phoneNo).setValue(addNewDoctor);
        }
        rootNode.getReference("All_Users").child(phoneNo).setValue(addNewUser);
    }

    //TODO: (optional) add alert dialog on exit then redirect to startup page.
    public void close(View view) {
        finish();
    }

    public void startVerificationProcess(View view) {
        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        } else {
            Toast.makeText(VerifyOTP.this, "Please input verification code!", Toast.LENGTH_SHORT).show();
        }
    }
}