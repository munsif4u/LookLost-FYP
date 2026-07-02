package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonDetails extends AppCompatActivity {

    private ImageView personImage;
    private TextView personName, personCategory, personLocation, personDescription;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        // Initialize views
        Button contactBtn = findViewById(R.id.contactUser);
        personImage = findViewById(R.id.persondetail_Image);
        personName = findViewById(R.id.persondetail_name);
        personCategory = findViewById(R.id.persondetail_category);
        personLocation = findViewById(R.id.persondetail_location);
        personDescription = findViewById(R.id.persondetail_description);

        // Get data from Intent
        Intent intent = getIntent();
        String personId = intent.getStringExtra("personId");//this is the idea of person who uploaded the image
        String itemNameStr = intent.getStringExtra("personName");
        String itemCategoryStr = intent.getStringExtra("personCategory");
        String itemLocationStr = intent.getStringExtra("personLocation");
        String itemDescriptionStr = intent.getStringExtra("personDescription");
        String itemImageStr = intent.getStringExtra("personImage");

        // Set data to views
        personName.setText(itemNameStr);
        personCategory.setText(itemCategoryStr);
        personLocation.setText(itemLocationStr);
        personDescription.setText(itemDescriptionStr);

        if (itemImageStr != null) {
            Glide.with(this).load(itemImageStr).into(personImage);
        } else {
            personImage.setVisibility(View.GONE);
        }

        CircleImageView sharebutton = findViewById(R.id.pshare_button);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PersonDetails.this, "Not Allowed to Share Now as this App is still in testing Mode", Toast.LENGTH_LONG).show();
            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PersonDetails.this, "Clicked", Toast.LENGTH_SHORT).show();
                sendNumberToContact(personId);
            }
        });

    }

    public void sendNumberToContact(String personId) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(personId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Retrieve the phone number from the document
                            String phoneNumber = documentSnapshot.getString("userNumber");
                            // Use the phone number as needed
                            Toast.makeText(PersonDetails.this, "User phone number: " + phoneNumber, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PersonDetails.this, ContactUser.class);
                            intent.putExtra("PhoneNumberSharedToContactPage", phoneNumber);
                            startActivity(intent);
                        } else {
                            // Document does not exist
                            Toast.makeText(PersonDetails.this, "User document not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred during the retrieval
                        Toast.makeText(PersonDetails.this, "Failed to retrieve user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}