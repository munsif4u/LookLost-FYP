package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class signup extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Safety initialization check
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        
        // Use a default if string is missing to prevent crash
        String dbUrl = getString(R.string.database_url);
        if (dbUrl != null && !dbUrl.isEmpty()) {
            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(dbUrl);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        Button register = findViewById(R.id.btn1_atsignup);
        EditText username = findViewById(R.id.et_sname);
        EditText useremail = findViewById(R.id.et_semail);
        EditText password = findViewById(R.id.et_spass);
        EditText confirmpass = findViewById(R.id.et_sconfpass);
        EditText userphonenumber = findViewById(R.id.et_sphone);
        TextView btnlogin = findViewById(R.id.btn2_atsignup);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String email = useremail.getText().toString().trim();
                String phone = userphonenumber.getText().toString().trim();
                String cPass = confirmpass.getText().toString().trim();

                if (user.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(signup.this, "All Fields Required!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (!pass.equals(cPass)) {
                    Toast.makeText(signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser firebaseUser = authResult.getUser();
                                if (firebaseUser != null) {
                                    // 1. Update Profile Name
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(user).build();
                                    firebaseUser.updateProfile(profileUpdates);

                                    // 2. Save to Firestore
                                    UserModel userModel = new UserModel();
                                    userModel.setUserName(user);
                                    userModel.setUserEmail(email);
                                    userModel.setUserNumber(phone);

                                    firebaseFirestore.collection("Users")
                                            .document(firebaseUser.getUid())
                                            .set(userModel);

                                    // 3. Save to Realtime Database (Chat)
                                    databaseReference.child("users").child(phone).child("email").setValue(email);
                                    databaseReference.child("users").child(phone).child("name").setValue(user);
                                    
                                    MemoryData.saveMobile(phone, signup.this);
                                }

                                progressDialog.dismiss();
                                Toast.makeText(signup.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                finishRegistration();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(signup.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void finishRegistration() {
        firebaseAuth.signOut(); // Sign out after registration so they can log in
        Intent intent = new Intent(signup.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
