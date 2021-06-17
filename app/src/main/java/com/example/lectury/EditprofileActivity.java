package com.example.lectury;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditprofileActivity extends AppCompatActivity {

    EditText boxemail;
    EditText boxname;
    EditText boxpassword;
    Button btnedit;
    Button btnview;
    FirebaseFirestore database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        viewUI();
        FirebaseUser user = auth.getInstance().getCurrentUser();
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = boxemail.getText().toString();
                String name = boxname.getText().toString();
                String pass = boxpassword.getText().toString();
                if (!name.isEmpty()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(EditprofileActivity.this, VideocallActivity.class));
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(EditprofileActivity.this, VideocallActivity.class));
                }
                if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(EditprofileActivity.this, VideocallActivity.class));
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(EditprofileActivity.this, VideocallActivity.class));
                }
                if (!pass.isEmpty()) {
                    user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(EditprofileActivity.this, VideocallActivity.class));
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(EditprofileActivity.this, VideocallActivity.class));
                }
            }

        });

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditprofileActivity.this, ViewprofileActivity.class));
            }
        });
    }

    public void viewUI() {
        boxemail = findViewById(R.id.boxemail);
        boxname = findViewById(R.id.boxname);
        boxpassword = findViewById(R.id.boxpassword);
        btnedit = findViewById(R.id.btnedit);
        btnview = findViewById(R.id.btnview);
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
                startActivity(new Intent(EditprofileActivity.this, LoginActivity.class));
                return true;
            case R.id.video:
                startActivity(new Intent(EditprofileActivity.this, VideocallActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}