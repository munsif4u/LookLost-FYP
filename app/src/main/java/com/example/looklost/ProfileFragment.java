package com.example.looklost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

View view;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = view.findViewById(R.id.textViewname);
        TextView email = view.findViewById(R.id.textViewemail);

        TextView myitems = view.findViewById(R.id.myitems);
        TextView mypersons = view.findViewById(R.id.mypersons);
        TextView shareapplication = view.findViewById(R.id.shareapplication);
        TextView helpbtn = view.findViewById(R.id.helpbutton);
        TextView logout = view.findViewById(R.id.logout);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = currentUser.getDisplayName();
        String mail = currentUser.getEmail();
        name.setText(user);
        email.setText(mail);

        myitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myint = new Intent(getContext(),myitems.class);
                startActivity(myint);
            }
        });
        mypersons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "My Persons", Toast.LENGTH_SHORT).show();
                Intent myint = new Intent(getContext(),mypersons.class);
                startActivity(myint);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
}