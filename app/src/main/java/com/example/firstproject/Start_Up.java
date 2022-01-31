package com.example.firstproject;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.firstproject.Navigation.MainNavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class Start_Up extends AppCompatActivity {
    boolean isScanDone = false;
    Timer timer;
    TextView taptoScan;
    AnimationDrawable animationDrawable;

    public static final String Error_Detected = "No NFC Tag Detected";
    public static final String Read_Success = "Text read succesfully";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    Tag myTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_startup);
        //Hooks
        LottieAnimationView Scan = findViewById(R.id.scan_lottie);
        taptoScan = findViewById(R.id.text_startup);

        timer = new Timer();
        Scan.setMinAndMaxProgress(0f, 0.32f);
        Scan.playAnimation();

        //*** NFC Features
        nfcAdapter = NfcAdapter.getDefaultAdapter(Start_Up.this);
        if (nfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "This device does not support NFC please Enter Id Manually!", Toast.LENGTH_SHORT).show();

        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(Start_Up.this, 0, new Intent(Start_Up.this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        //***
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;
        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        String text = "";
        //String tagId = new String(msgs [0].getRecords( [0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        try {
            //Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }
        taptoScan.setText("NFC Contents: " + text);
        //ToDo: Fetch ID(phoneNo) from bracelet and putIntentExtra to display info.
        displayInfo(text);
    }

    private void displayInfo(String text) {
        String phoneNo = text;
        LottieAnimationView Scan = findViewById(R.id.scan_lottie);
        Scan.setMinAndMaxProgress(0.7f, 1f);
        Scan.playAnimation();
        Scan.loop(false);
        Scan.setClickable(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                        /*
        Verify Data and get Info from database
         */
                Query query = FirebaseDatabase.getInstance().getReference("All_Users").orderByChild("phoneNo").equalTo(phoneNo);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Intent intent = new Intent(getApplicationContext(), MainNavigation.class);
                            intent.putExtra("phoneNo", phoneNo);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Start_Up.this, "No such User exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Start_Up.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

        /*
    Check Connectivity
     */

    private boolean isConnected(Start_Up start_up) {
        ConnectivityManager connectivityManager = (ConnectivityManager) start_up.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Start_Up.this);

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
    OnClick Methods
     */


    public void callLoginScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.startup_login), "transition_startup_login");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Start_Up.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void callSignupScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp_0.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.startup_signup), "transition_startup_signup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Start_Up.this, pairs);
        startActivity(intent);
    }

    public void Scan(View view) {
        LottieAnimationView Scan = findViewById(R.id.scan_lottie);
        if (isScanDone) {
            Scan.setMinAndMaxProgress(0.7f, 1f);
            Scan.playAnimation();
        } else {
            Scan.setMinAndMaxProgress(0.35f, 0.7f);
            Scan.playAnimation();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Scan.setMinAndMaxProgress(0f, 0.32f);
                }
            }, 9500);
        }
    }
}