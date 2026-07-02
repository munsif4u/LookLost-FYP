package com.example.looklost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
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

public class Tab1Fragment extends Fragment {
    View view;
    private ShortListAdapter showItemAdapter;
    RecyclerView itemrecycler;
    List<DocumentSnapshot> dsList ;
    FragmentTransaction fragmentTransaction;
    FragmentContainerView containerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        itemrecycler = view.findViewById(R.id.tab1_recycler);

        showItemAdapter = new ShortListAdapter(getContext());
        itemrecycler.setAdapter(showItemAdapter);
        itemrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        LoadItems();

        showItemAdapter.setOnItemClickListener(new ShortListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel itemModel) {
                    GotoItemDetail(itemModel);
            }
        });

        return view;
    }
    private void LoadItems(){
        FirebaseFirestore.getInstance()
                .collection("Items")
                .orderBy("itemName")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        showItemAdapter.clearItems();
                        dsList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            ItemModel itemModel = ds.toObject(ItemModel.class);
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
    private void GotoItemDetail(ItemModel itemModel){
        FirebaseFirestore.getInstance()
                .collection("Items")
                .document(itemModel.getItemId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ItemModel item = documentSnapshot.toObject(ItemModel.class);
                        Intent intent = new Intent(getContext(), ItemDetails.class);
                        intent.putExtra("itemId", item.getItemId());
                        intent.putExtra("itemName", item.getItemName());
                        intent.putExtra("itemCategory", item.getItemCategory());
                        intent.putExtra("itemLocation", item.getItemLocation());
                        intent.putExtra("itemDescription", item.getItemDescription());
                        intent.putExtra("itemImage", item.getItemImage());
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