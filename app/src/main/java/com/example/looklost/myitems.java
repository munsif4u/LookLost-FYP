package com.example.looklost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

//myitems wala page , adaptertoshowmyitems, my items layout
public class myitems extends AppCompatActivity {
    private AdapterToShowItems showItemAdapter;
    List<DocumentSnapshot> dsList ;
    RecyclerView itemrecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myitems);

        itemrecycler = findViewById(R.id.myitems_recycler);
        showItemAdapter = new AdapterToShowItems(this);
        itemrecycler.setAdapter(showItemAdapter);
        itemrecycler.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid(); // Replace with the actual method to get the current user ID

        FirebaseFirestore.getInstance()
                .collection("Items")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    showItemAdapter.clearItems();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ItemModel itemModel = documentSnapshot.toObject(ItemModel.class);
                        showItemAdapter.addItem(itemModel);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any failures
                    Toast.makeText(this, "Error Occured in MYItems", Toast.LENGTH_SHORT).show();
                });
        showItemAdapter.setOnItemClickListener(new AdapterToShowItems.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel itemModel) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myitems.this);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure you want to delete this Person?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the onDeleteClick method of the listener
                        showItemAdapter.deleteItem(itemModel);
                        Toast.makeText(myitems.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

    }

//    private void LoadItems(){
//        FirebaseFirestore.getInstance()
//                .collection("Items")
//                .orderBy("itemName")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        showItemAdapter.clearItems();
//                        dsList = queryDocumentSnapshots.getDocuments();
//                        for(DocumentSnapshot ds:dsList){
//                            ItemModel itemModel = ds.toObject(ItemModel.class);
//                            showItemAdapter.addItem(itemModel);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//    }

    }