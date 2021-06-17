package com.example.lectury;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class VideocallActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    EditText boxcode;
    Button btnjoin;
    Button btnshare;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocall);
        viewUI();
        final Bundle userInfoBundle = new Bundle();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        JitsiMeetUserInfo jitsiMeetUserInfo = new JitsiMeetUserInfo(userInfoBundle);
        URL serverURL;
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                String name = value.getString("name");
                jitsiMeetUserInfo.setDisplayName(name);
            }
        });



        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setUserInfo(jitsiMeetUserInfo)
                            .setWelcomePageEnabled(false)
                            .build();

            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(boxcode.getText().toString())
                            .setUserInfo(jitsiMeetUserInfo)
                            .setWelcomePageEnabled(false)
                            .build();

                    JitsiMeetActivity.launch(VideocallActivity.this, options);
                }
            }
        });

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Data", boxcode.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(VideocallActivity.this, "Meeting code is shared", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(VideocallActivity.this, LoginActivity.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(VideocallActivity.this, ViewprofileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewUI() {
        boxcode = findViewById(R.id.boxcode);
        btnjoin = findViewById(R.id.btnjoin);
        btnshare = findViewById(R.id.btnshare);
    }

    public boolean isValid() {
        boolean valid = false;
        String code = boxcode.getText().toString();

        if (code.isEmpty()) {
            valid = false;
            boxcode.setError("Please enter valid meeting name");
        } else {
            valid = true;
            boxcode.setError(null);
        }
        return valid;
    }
}