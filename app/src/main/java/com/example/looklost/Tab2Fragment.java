package com.example.looklost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Tab2Fragment extends Fragment {
    View view;
    private ShortListAdapterPerson showItemAdapter;
    RecyclerView itemrecycler;
    List<DocumentSnapshot> dsList ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2, container, false);
        itemrecycler = view.findViewById(R.id.tab2_recycler);

        showItemAdapter = new ShortListAdapterPerson(getContext());
        itemrecycler.setAdapter(showItemAdapter);
        itemrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        LoadItems();

        showItemAdapter.setOnItemClickListener(new ShortListAdapterPerson.OnItemClickListener() {
            @Override
            public void onItemClick(PersonModel itemModel) {
                GotoItemDetail(itemModel);
            }
        });
        return view;
    }
    private void LoadItems(){
        FirebaseFirestore.getInstance()
                .collection("Persons")
                .orderBy("personName")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        showItemAdapter.clearItems();
                        dsList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            PersonModel itemModel = ds.toObject(PersonModel.class);
                            showItemAdapter.addItem(itemModel);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void GotoItemDetail(PersonModel itemModel){
        FirebaseFirestore.getInstance()
                .collection("Persons")
                .document(itemModel.getPersonId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        PersonModel item = documentSnapshot.toObject(PersonModel.class);
                        Intent intent = new Intent(getContext(), PersonDetails.class);
                        intent.putExtra("personId", item.getUserId());
                        intent.putExtra("personName", item.getPersonName());
                        intent.putExtra("personCategory", item.getPersonCategory());
                        intent.putExtra("personLocation", item.getPersonLocation());
                        intent.putExtra("personDescription", item.getPersonDescription());
                        intent.putExtra("personImage", item.getPersonImage());
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to fetch item details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}