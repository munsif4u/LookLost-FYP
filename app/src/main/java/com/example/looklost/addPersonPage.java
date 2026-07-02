package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class addPersonPage extends AppCompatActivity {

    public static final int ITEM_PIC = 1000;
    private Uri itemImageUri;
    ProgressDialog progressDialog;

    AutoCompleteTextView autoCompleteTextView;
    ImageView itemImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person_page);
        progressDialog=new ProgressDialog(this);

        //Category DropDown
        autoCompleteTextView = findViewById(R.id.et_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_list_person, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(adapter);

        itemImage = (ImageView) findViewById(R.id.add_item_img);
        Button upload = (Button) findViewById(R.id.btn_upload);
//        Button add_question = (Button) findViewById(R.id.btn_secure_question);
        EditText title = (EditText) findViewById(R.id.et_title);
        EditText location = (EditText) findViewById(R.id.et_location);
        EditText description = (EditText) findViewById(R.id.et_description);

        //Image View
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }

        });

        //Add Security Question
//        add_question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View mView = getLayoutInflater().inflate(R.layout.security_dialog, null);
//
//                MaterialAlertDialogBuilder aDialog = new MaterialAlertDialogBuilder(addPersonPage.this,R.style.fullscreenDialog);
//                EditText et_question = mView.findViewById(R.id.et_question);
//                Button CloseDialog = mView.findViewById(R.id.btn_close);
//
//                aDialog.setView(mView);
//                aDialog.show();
//
//                CloseDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(addPersonPage.this, et_question.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//        });

        //Button Upload

        //Uploading Person in DataBase
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = UUID.randomUUID().toString();
                String userId = FirebaseAuth.getInstance().getUid();
                String personName = title.getText().toString();
                String personCategory = autoCompleteTextView.getText().toString();
                String personLocation = location.getText().toString();
                String personDescription = description.getText().toString();

                progressDialog.show();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Persons/"+id+"image.png");
                if(itemImageUri!=null){
                    storageReference.putFile(itemImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            PersonModel personModel = new PersonModel(id,userId,personName,personCategory,personLocation,personDescription,uri.toString());

                                            FirebaseFirestore.getInstance()
                                                    .collection("Persons")
                                                    .document(id)
                                                    .set(personModel);
                                        }
                                    });
                                    Toast.makeText(addPersonPage.this,"Person Details Uploaded", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();

                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(addPersonPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });

                }
                else{
                    Toast.makeText(addPersonPage.this, "Image Error Occured", Toast.LENGTH_SHORT).show();
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
                Glide.with(addPersonPage.this).load(itemImageUri).into(itemImage);
            }
            else{
                Toast.makeText(this, "Person Image Not Picked", Toast.LENGTH_SHORT).show();
            }

//            if(resultCode==RESULT_OK)
        }
    }

//    @Override
//    public void onClick(View view) {
//
//    }


}