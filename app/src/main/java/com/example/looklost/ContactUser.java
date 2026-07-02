package com.example.looklost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContactUser extends AppCompatActivity {

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_user);

        TextView whatsappButton = findViewById(R.id.whatsappBtn);
        TextView dialerBtn = findViewById(R.id.dialerBtn);
        TextView smsBtn = findViewById(R.id.smsBtn);
        TextView chatBtn = findViewById(R.id.chatBtn);

        Intent intent = getIntent();
        String sharedValue = intent.getStringExtra("PhoneNumberSharedToContactPage");
        if (sharedValue != null) {
        phoneNumber = sharedValue;
        }

//        phoneNumber = "03491980170"; // Replace with the desired phone number

        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = "+92"; // Replace with the desired country code

                String link = "https://wa.me/"+countryCode+phoneNumber+"?text=Hi, I saw an Item in Lost and Found App";
                // Create the intent with the appropriate action and data
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                startActivity(intent);
            }
        });
        dialerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(intent);
            }
        });
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Hello, this is a sample message."); // Replace with your desired message content
                intent.putExtra("address", phoneNumber);
                startActivity(intent);
            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactUser.this, Home.class);
                intent.putExtra("fragmentId", R.id.item_4); // Pass the ID of the fragment you want to display
                startActivity(intent);
            }
        });


    }
}