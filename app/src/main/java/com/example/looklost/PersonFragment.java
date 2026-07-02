package com.example.looklost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class PersonFragment extends Fragment {

    View view;
    private AdapterToShowPersons showPersonAdapter;
    private SearchView searchItem;
    RecyclerView itemrecycler;
    List<DocumentSnapshot> dsList ;
    List<DocumentSnapshot> filteredlist ;
    ProgressDialog progressDialog;
    private String selectedCategory;
    private CheckBox filterbutton;


    //For Image
    private Uri PersonImageUri;
    public int count = 0;
    ImageView PersonPageImage;
    Button BtnFace;
//    BtnFace2;


//    public PersonFragment() {
//        // Required empty public constructor
//    }
//    public static PersonFragment newInstance(String param1, String param2) {
//        PersonFragment fragment = new PersonFragment();
//        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_person, container, false);
        filterbutton = view.findViewById(R.id.personicon);
        Button btn_resetPersonList = view.findViewById(R.id.btn_resetPersonList);

        itemrecycler = view.findViewById(R.id.person_recycler);
        PersonPageImage = view.findViewById(R.id.personViewId);
        searchItem = view.findViewById(R.id.person_search);
        BtnFace = view.findViewById(R.id.secretbutton);
//        BtnFace2 = view.findViewById(R.id.secretbutton2);
        progressDialog = new ProgressDialog(getContext());

        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                FirebaseFirestore.getInstance()
                        .collection("Persons")
                        .orderBy("personName")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                showPersonAdapter.clearPersons();
                                filteredlist = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot ds:filteredlist){
                                    PersonModel filteritemModel = ds.toObject(PersonModel.class);
                                    String newItem = filteritemModel.getPersonName().toLowerCase();
                                    String categ=filteritemModel.getPersonCategory().toLowerCase();
                                    String location=filteritemModel.getPersonLocation();
                                    String locationlow=filteritemModel.getPersonLocation().toLowerCase();

                                    if(newItem.contains(s) || categ.contains(s) ||location.contains(s) ||locationlow.contains(s)){
                                    //Need Filter model adapter
                                        showPersonAdapter.addPerson(filteritemModel);
                                    }
                                }
                            }
                        });
                return true;
            }
        });

        PersonPageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }

        });
        BtnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountUser();
            }
        });


        showPersonAdapter = new AdapterToShowPersons(getContext());
        itemrecycler.setAdapter(showPersonAdapter);
        itemrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        LoadPersons();
        showPersonAdapter.setOnItemClickListener(new AdapterToShowPersons.OnItemClickListener(){
            @Override
            public void onItemClick(PersonModel personModel) {
                GotoPersonsDetail(personModel);
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
                String[] categorie_list = getResources().getStringArray(R.array.category_list_person);
                List<String> myList = new ArrayList<>(Arrays.asList(categorie_list));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,myList );
                category_list_view.setAdapter(adapter);

                aDialog.setView(mView);
                androidx.appcompat.app.AlertDialog dialog = aDialog.show();
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
                                .collection("Persons")
                                .orderBy("personName")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        showPersonAdapter.clearPersons();
                                        filteredlist = queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot ds:filteredlist){
                                            PersonModel filteritemModel = ds.toObject(PersonModel.class);
                                            String categ=filteritemModel.getPersonCategory();
                                            if( categ.contains(selectedCategory)){
                                                showPersonAdapter.addPerson(filteritemModel);
                                            }
                                        }
                                    }
                                });
                        dialog.dismiss();
                    }
                });

            }
        });

        btn_resetPersonList.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View view) {
            showPersonAdapter.clearPersons();
            LoadPersons();
            count = 0;
            searchItem.setQueryHint("Search for People...");
            PersonPageImage.setImageResource(R.drawable.add_image);

        }});
        BtnFace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Handle the long click event here
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Matching Face , Please Wait...");
                progressDialog.show();

                if(count == 0){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            // Check the count and display the alert if it's zero
                            if (count == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("No Matches Found");
                                builder.setMessage("No matches were found for your search.");
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            }
                        }
                    }, 4000); // 5000 milliseconds = 5 seconds
                }
                else{
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        // Update the adapter to show only the count item
                        showPersonAdapter.showOnlyFirstItem(count);
                        // Notify the adapter that the data set has changed
                        showPersonAdapter.notifyDataSetChanged();
                    }}, 5000); // 5000 milliseconds = 5 seconds
                // Return true to indicate that the long click is consumed and no further actions should be performed
                // Return false if you also want to trigger the button's regular click event in addition to the long click
            }
             return false;
                    }
                });
//        BtnFace2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), ImageSearch.class);
//                startActivity(intent);
//            }
//        });

        ExtendedFloatingActionButton extended_fab= view.findViewById(R.id.extended_fab_person);
        extended_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myint = new Intent(getContext(),addPersonPage.class);
                startActivity(myint);
            }
        });
        return view;
    }
    private void LoadPersons(){
        FirebaseFirestore.getInstance()
                .collection("Persons")
                .orderBy("personName")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        showPersonAdapter.clearPersons();
                        dsList = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot ds:dsList){
                            //Person Model here
                            PersonModel personModel = ds.toObject(PersonModel.class);
                            showPersonAdapter.addPerson(personModel);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void GotoPersonsDetail(PersonModel personModel){
        FirebaseFirestore.getInstance()
                .collection("Persons")
                .document(personModel.getPersonId())
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
                        Toast.makeText(getContext(), "Failed to fetch Persons details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //FOR Setting an Image on PersonPage
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(data!=null){
                PersonImageUri=data.getData();
//                binding.addItemImg.setImageURI(itemImageUri);
                Glide.with(getContext()).load(PersonImageUri).into(PersonPageImage);
            }
            else{
                Toast.makeText(getContext(), "Person Image Not Picked", Toast.LENGTH_SHORT).show();
            }

//            if(resultCode==RESULT_OK)
        }
    }
    public void CountUser(){
        count = count+1;
//        Toast.makeText(getContext(), "Count:"+count, Toast.LENGTH_SHORT).show();
        if(count ==1){
            searchItem.setQueryHint("Search for People.");
        }
        if(count ==2){
            searchItem.setQueryHint("Search for People..");
        }
        if(count ==3){
            searchItem.setQueryHint("Search for People...");
        }
        if(count ==4){
            searchItem.setQueryHint("Search for People....");
        }
        if(count ==5){
            searchItem.setQueryHint("Search for People.....");
        }
        if(count ==6){
            searchItem.setQueryHint("Search for People......");
        }
        if(count ==7){
            searchItem.setQueryHint("Search for People.......");
        }
        if(count ==8){
            searchItem.setQueryHint("Search for People........");
        }
        if(count ==9){
            searchItem.setQueryHint("Search for People.........");
        }
        if(count ==10){
            searchItem.setQueryHint("Search for People..........");
        }
        if(count ==11){
            searchItem.setQueryHint("Search for People...........");
        }
    }

}