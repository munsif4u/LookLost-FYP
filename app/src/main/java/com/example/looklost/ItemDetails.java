package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemDetails extends AppCompatActivity {

    private ImageView itemImage;
    private TextView itemName, itemCategory, itemLocation, itemDescription;
    private TextView ItemQuestion, Option1, Option2, Option3;
    private String CorrectAnswer ,UserIDWhoAddItem;
    private ShareActionProvider shareActionProvider;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize views
        itemImage = findViewById(R.id.itemdetail_Image);
        itemName = findViewById(R.id.itemdetail_name);
        itemCategory = findViewById(R.id.itemdetail_category);
        itemLocation = findViewById(R.id.itemdetail_location);
        itemDescription = findViewById(R.id.itemdetail_description);
        ItemQuestion = findViewById(R.id.secureQuest_Display);
        Option1 = findViewById(R.id.item_option1);
        Option2 = findViewById(R.id.item_option2);
        Option3 = findViewById(R.id.item_option3);

        // Get data from Intent
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");
        String itemNameStr = intent.getStringExtra("itemName");
        String itemCategoryStr = intent.getStringExtra("itemCategory");
        String itemLocationStr = intent.getStringExtra("itemLocation");
        String itemDescriptionStr = intent.getStringExtra("itemDescription");
        String itemImageStr = intent.getStringExtra("itemImage");

        // Set data to views
        itemName.setText(itemNameStr);
        itemCategory.setText(itemCategoryStr);
        itemLocation.setText(itemLocationStr);
        itemDescription.setText(itemDescriptionStr);

        if (itemImageStr != null) {
            Glide.with(this).load(itemImageStr).into(itemImage);
        } else {
            itemImage.setVisibility(View.GONE);
        }

        //For Security Questions
        getQuestionById(itemId, ItemQuestion, Option1,Option2,Option3 );
        //For Matching Answer
        TextView[] optionTextViews = {Option1, Option2, Option3};
        MatchAnswer(optionTextViews);
        //For Getting UserNumber and Passing it to Chat Module
//        NewUserAdd();
//        Toast.makeText(this, UserIDWhoAddItem, Toast.LENGTH_SHORT).show();

        //Share Button Share the Item
        CircleImageView sharebutton = findViewById(R.id.share_button);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ItemDetails.this, "Not Allowed to Share Now as this App is still in testing Mode", Toast.LENGTH_LONG).show();
            //trying to share the data but failed..
            //                // Get the Bitmap from the image path
//                itemImage.setDrawingCacheEnabled(true);
//                itemImage.buildDrawingCache();
//                Bitmap itemBitmap = Bitmap.createBitmap(itemImage.getDrawingCache());
//                itemImage.setDrawingCacheEnabled(false);
//
//                // Convert the Bitmap to a JPEG file with compression
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                itemBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//                // Create a new file to save the JPEG image
//                File imageFile = new File(getCacheDir(), "item_image.jpg");
//                try {
//                    FileOutputStream fos = new FileOutputStream(imageFile);
//                    fos.write(bytes.toByteArray());
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                // Get the URI of the JPEG image file
//                Uri itemImageUri = Uri.fromFile(imageFile);
//
//                // Create the share intent
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("image/jpeg");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, itemImageUri);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this item: " + itemNameStr + "\nCategory: " + itemCategoryStr + "\nLocation: " + itemLocationStr + "\nDescription: " + itemDescriptionStr);
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Item: " + itemNameStr);
//
//                // Launch the share activity
//                startActivity(Intent.createChooser(shareIntent, "Share item"));

            }
        });

    }

    //###### User-Defined functions #######

    //For Security Question
    public void getQuestionById(String itemId, TextView questionTextView, TextView option1TextView,
                                TextView option2TextView, TextView option3TextView) {
        db = FirebaseFirestore.getInstance();
        db.collection("SecurityQuestions")
                .whereEqualTo("itemid", itemId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String questionText = document.getString("question");
                            String opt1 = document.getString("opt1");
                            String opt2 = document.getString("opt2");
                            String opt3 = document.getString("opt3");
                            String answer = document.getString("canswer" );
                            String userid = document.getString("userid" );

                            ItemQuestion.setText(questionText);
                            Option1.setText(opt1);
                            Option2.setText(opt2);
                            Option3.setText(opt3);
                            CorrectAnswer = answer;
                            UserIDWhoAddItem = userid;
                        }
                    } else {
                        Log.d("Firebase", "Error getting question: " + task.getException());
                    }
                });
    }
    //For Matching Answer
    public void MatchAnswer(TextView[] optionTextViews){
        for (TextView optionTextView : optionTextViews) {
            optionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedAnswer = ((TextView) view).getText().toString();

                    // Compare the selected answer with the correct answer
                    if (selectedAnswer.equals(CorrectAnswer)) {
                        // Display a message indicating the answer is correct
                        Toast.makeText(ItemDetails.this, "Congratulations! You selected the correct answer.", Toast.LENGTH_SHORT).show();
                        NewUserAdd(UserIDWhoAddItem);
                    } else {
                        // Display a message indicating the answer is incorrect
                        Toast.makeText(ItemDetails.this, "Sorry, the answer is incorrect.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //For Redirecting to Chat
    public void NewUserAdd(String userid){
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Retrieve the phone number from the document
                            String phoneNumber = documentSnapshot.getString("userNumber");
                            // Use the phone number as needed
                            Toast.makeText(ItemDetails.this, "User phone number: " + phoneNumber, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ItemDetails.this, ContactUser.class);
                            intent.putExtra("PhoneNumberSharedToContactPage", phoneNumber);
                            startActivity(intent);
                        } else {
                            // Document does not exist
                            Toast.makeText(ItemDetails.this, "User document not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred during the retrieval
                        Toast.makeText(ItemDetails.this, "Failed to retrieve user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}