package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.looklost.databinding.ActivityAddItemPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class addItemPage extends AppCompatActivity implements View.OnClickListener {

//    Automatic binding with xml components
//    ActivityAddItemPageBinding binding;

    public static final int ITEM_PIC = 1000;
    private Uri itemImageUri;

    AutoCompleteTextView autoCompleteTextView;
    ImageView itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityAddItemPageBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_add_item_page);

        //Category DropDown
        autoCompleteTextView = findViewById(R.id.et_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_list, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(adapter);

        itemImage = (ImageView) findViewById(R.id.add_item_img);
        Button upload = (Button) findViewById(R.id.btn_upload);
        Button add_question = (Button) findViewById(R.id.btn_secure_question);
        EditText title = (EditText) findViewById(R.id.et_title);
        EditText location = (EditText) findViewById(R.id.et_location);
        EditText description = (EditText) findViewById(R.id.et_description);

        String GeneratedItemId = UUID.randomUUID().toString();

        //Image View
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }

        });

        //Add Security Question
        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.security_dialog, null);

                MaterialAlertDialogBuilder aDialog = new MaterialAlertDialogBuilder(addItemPage.this,R.style.fullscreenDialog);
                EditText et_question = mView.findViewById(R.id.et_question);
                EditText option1 = mView.findViewById(R.id.et_option1);
                EditText option2 = mView.findViewById(R.id.et_option2);
                EditText option3 = mView.findViewById(R.id.et_option3);
                EditText correctanswer = mView.findViewById(R.id.et_correctoption);
                Button addquestionDialog = mView.findViewById(R.id.btn_addquestion);
                Button CloseDialog = mView.findViewById(R.id.btn_close);
                //ADDING SECURITY QUESTIONS TO DATABASE
                addquestionDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userid = FirebaseAuth.getInstance().getUid();
                        String itemid = GeneratedItemId;
                        String questionID = UUID.randomUUID().toString();
                        String question = et_question.getText().toString();
                        String opt1 = option1.getText().toString();
                        String opt2 = option2.getText().toString();
                        String opt3 = option3.getText().toString();
                        String Canswer = correctanswer.getText().toString();

                        if (question.isEmpty() || opt1.isEmpty() || opt2.isEmpty() || opt3.isEmpty() || Canswer.isEmpty() ) {
                            Toast.makeText(addItemPage.this, "All Fields Required!!!", Toast.LENGTH_SHORT).show();
                        }
                       else{
                            QuestionModel questionModel = new QuestionModel(questionID, itemid, userid, question, opt1, opt2, opt3, Canswer);
                            // Add the QuestionModel object to Firestore
                            FirebaseFirestore.getInstance()
                                    .collection("SecurityQuestions")
                                    .document(questionID)
                                    .set(questionModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Data added successfully
                                            Toast.makeText(addItemPage.this, "Question Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error
                                            Toast.makeText(addItemPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            }
                        }
                });
                aDialog.setView(mView);
                AlertDialog dialog = aDialog.create();
                dialog.show();
                CloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //Button Upload
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String id = UUID.randomUUID().toString();
                String id = GeneratedItemId;
                String userId = FirebaseAuth.getInstance().getUid();
                String itemName = title.getText().toString();
                String itemCategory = autoCompleteTextView.getText().toString();
                String itemLocation = location.getText().toString();
                String itemDescription = description.getText().toString();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Items/"+id+"image.png");
                if(itemImageUri!=null){
                    storageReference.putFile(itemImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            ItemModel itemModel = new ItemModel(id,userId,itemName,itemCategory,itemLocation,itemDescription,uri.toString());

                                            FirebaseFirestore.getInstance()
                                                    .collection("Items")
                                                    .document(id)
                                                    .set(itemModel);
                                        }
                                    });
                                    Toast.makeText(addItemPage.this,"ITEM Uploaded", Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(addItemPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                else{
                    Toast.makeText(addItemPage.this, "Image Error Occured", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(data!=null){
                itemImageUri=data.getData();
//                binding.addItemImg.setImageURI(itemImageUri);
                Glide.with(addItemPage.this).load(itemImageUri).into(itemImage);
            }
            else{
                Toast.makeText(this, "Image Not Picked", Toast.LENGTH_SHORT).show();
            }

//            if(resultCode==RESULT_OK)
        }
    }

    @Override
    public void onClick(View view) {

    }
}