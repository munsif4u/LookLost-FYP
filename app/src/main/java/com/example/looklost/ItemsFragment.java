package com.example.looklost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment {

    View view;
    private AdapterToShowItems showItemAdapter;
    private SearchView searchItem;
    private CheckBox filterbutton;
    RecyclerView itemrecycler;
    List<DocumentSnapshot> dsList ;
    List<DocumentSnapshot> filteredlist ;
//    List<ItemModel> filteredList;

    private String selectedCategory;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_items, container, false);
        Button btn_resetList = view.findViewById(R.id.btn_resetList);
        filterbutton = view.findViewById(R.id.filterbutton);
        itemrecycler = view.findViewById(R.id.item_recycler);
        searchItem = view.findViewById(R.id.item_search);
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                FirebaseFirestore.getInstance()
                        .collection("Items")
                        .orderBy("itemName")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                showItemAdapter.clearItems();
                                filteredlist = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot ds:filteredlist){
                                    ItemModel filteritemModel = ds.toObject(ItemModel.class);
                                    String newItem = filteritemModel.getItemName().toLowerCase();
                                    String categ=filteritemModel.getItemCategory().toLowerCase();
                                    String location=filteritemModel.getItemLocation();
                                    String locationlow=filteritemModel.getItemLocation().toLowerCase();

                                    if(newItem.contains(s) || categ.contains(s)|| location.contains(s) || locationlow.contains(s)){
                                        showItemAdapter.addItem(filteritemModel);
                                    }
                                }
                            }
                        });
                return true;
            }
        });

        showItemAdapter = new AdapterToShowItems(getContext());
        itemrecycler.setAdapter(showItemAdapter);
        itemrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        LoadItems();
        showItemAdapter.setOnItemClickListener(new AdapterToShowItems.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel itemModel) {
                GotoItemDetail(itemModel);
            }
        });

        //Filter Dialog
        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.search_filter_dialog, null);

                MaterialAlertDialogBuilder aDialog = new MaterialAlertDialogBuilder(getContext(),R.style.screenDialog);
                ListView category_list_view = mView.findViewById(R.id.category_list);

                // Pass the category array to this list
                String[] categorie_list = getResources().getStringArray(R.array.category_list);
                List<String> myList = new ArrayList<>(Arrays.asList(categorie_list));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,myList );
                category_list_view.setAdapter(adapter);

                aDialog.setView(mView);
                AlertDialog dialog = aDialog.show();
//                aDialog.show();
                category_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the clicked item's text
                        String selectedItem = (String) parent.getItemAtPosition(position);

                        // Do something with the selected item, like saving it to a variable
                        // For example:
                        selectedCategory = selectedItem;
                        FirebaseFirestore.getInstance()
                                .collection("Items")
                                .orderBy("itemName")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        showItemAdapter.clearItems();
                                        filteredlist = queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot ds:filteredlist){
                                            ItemModel filteritemModel = ds.toObject(ItemModel.class);
                                            String categ=filteritemModel.getItemCategory();
                                            if( categ.contains(selectedCategory)){
                                                showItemAdapter.addItem(filteritemModel);
                                            }
                                        }
                                    }
                                });
                    dialog.dismiss();
                    }
                });

            }
        });
        btn_resetList.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View view) {
        showItemAdapter.clearItems();
        LoadItems();
        }});

        ExtendedFloatingActionButton extended_fab= view.findViewById(R.id.extended_fab_item);
        extended_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myint = new Intent(getContext(),addItemPage.class);
                startActivity(myint);
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