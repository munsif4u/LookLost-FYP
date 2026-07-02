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

public class mypersons extends AppCompatActivity {
    private AdapterToShowPersons showItemAdapter;
    List<DocumentSnapshot> dsList ;
    RecyclerView itemrecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypersons);

        itemrecycler = findViewById(R.id.myperson_recycler);
        showItemAdapter = new AdapterToShowPersons(this);
        itemrecycler.setAdapter(showItemAdapter);
        itemrecycler.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid(); // Replace with the actual method to get the current user ID

        FirebaseFirestore.getInstance()
                .collection("Persons")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    showItemAdapter.clearItems();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        PersonModel itemModel = documentSnapshot.toObject(PersonModel.class);
                        showItemAdapter.addPerson(itemModel);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any failures
                    Toast.makeText(this, "Error Occured in MYItems", Toast.LENGTH_SHORT).show();
                });
        showItemAdapter.setOnItemClickListener(new AdapterToShowPersons.OnItemClickListener() {
            @Override
            public void onItemClick(PersonModel itemModel) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mypersons.this);
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure you want to delete this Person?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the onDeleteClick method of the listener
                        showItemAdapter.deletePerson(itemModel);
                        Toast.makeText(mypersons.this, "Person Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();


            }
        });

    }
}