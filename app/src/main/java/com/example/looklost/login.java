package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Proper initialization check
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        EditText emailAddress = findViewById(R.id.et_name);
        EditText password = findViewById(R.id.et_pass);
        Button login = findViewById(R.id.btn1_atlogin);
        TextView signup = findViewById(R.id.btn2_atlogin);
        TextView resetPass = findViewById(R.id.forgetpass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = emailAddress.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Logging in...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(user, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

                                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("is_logged_in", true);
                                editor.apply();

                                Intent int1 = new Intent(login.this, Home.class);
                                startActivity(int1);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = emailAddress.getText().toString().trim();

                if (user.isEmpty()) {
                    Toast.makeText(login.this, "Enter Email in the field and then click forget password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setTitle("Sending Email...");
                progressDialog.setMessage("Please Check Your Email");
                progressDialog.show();

                firebaseAuth.sendPasswordResetEmail(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Check Your Email! Password reset email sent.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Failed to send reset email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logintosignup = new Intent(login.this, signup.class);
                startActivity(logintosignup);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth != null && firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(login.this, Home.class));
            finish();
        }
    }
}
