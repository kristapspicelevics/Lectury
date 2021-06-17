package com.example.lectury;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ViewprofileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    TextView textemail;
    TextView textname;
    Button btnedit;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);
        viewUI();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                textname.setText("Name: " +value.getString("name"));
                textemail.setText("Email: " +value.getString("email"));
            }
        });


        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewprofileActivity.this, EditprofileActivity.class));
            }
        });
    }

    public void viewUI() {
        textemail = findViewById(R.id.textemail);
        textname = findViewById(R.id.textname);
        btnedit = findViewById(R.id.btnedit);
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
                startActivity(new Intent(ViewprofileActivity.this, LoginActivity.class));
                return true;
            case R.id.video:
                startActivity(new Intent(ViewprofileActivity.this, VideocallActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}