package com.technominds.lecture.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);

        mauth=FirebaseAuth.getInstance();
    }

    private void gotoprofile()
    {
        startActivity(new Intent(LoginActivity.this,MyProfile.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mauth.getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
    }

    public void loginnow(View view) {
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            String semail = email.getText().toString();
            String spassword = password.getText().toString();
            mauth.signInWithEmailAndPassword(semail, spassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            gotoprofile();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void registernow(View view)
    {
        if (email.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else
        {
            String semail = email.getText().toString();
            String spassword = password.getText().toString();
            mauth.createUserWithEmailAndPassword(semail, spassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            gotoprofile();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}