package com.example.looklost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    View view;
    ViewPager viewPager;
    TabViewPagerFragAdapter tabviewFragAdapter;
    TabLayout tabLayout;

    //Changing Fragment on Click of image
    FragmentTransaction fragmentTransaction;
    FragmentContainerView containerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout=view.findViewById(R.id.tab_layout);

        CircleImageView profilepic=view.findViewById(R.id.profile_imageview);
        TextView username=view.findViewById(R.id.home_username);
        SetUserNameOnDashBoard(username);
        
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click ON Bottom right icon for profile", Toast.LENGTH_SHORT).show();
            }
        });


        ExtendedFloatingActionButton extended_fab= view.findViewById(R.id.extended_fab);
        extended_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View mView = getLayoutInflater().inflate(R.layout.mainfab_dialog, null);

                MaterialAlertDialogBuilder aDialog = new MaterialAlertDialogBuilder(getContext(),R.style.screenDialog);
                MaterialCardView addItemCard, foundPersonCard, reportLostPersonCard;
                addItemCard = mView.findViewById(R.id.add_item_button);
                foundPersonCard = mView.findViewById(R.id.add_person_button);
                reportLostPersonCard = mView.findViewById(R.id.add_reportperson_button);
                addItemCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(),addItemPage.class));
                    }
                });
                foundPersonCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(),addPersonPage.class));
                    }
                });
                reportLostPersonCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(),addPersonPage.class));
                    }
                });

                aDialog.setView(mView);
                aDialog.show();
            }
        });

        return view;
    }

    private void SetUserNameOnDashBoard(TextView username) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String user = currentUser.getDisplayName();
            if (user == null || user.isEmpty()) {
                user = currentUser.getEmail(); // Fallback to email if name is not set
            }
            username.setText(user);
        } else {
            username.setText("Guest");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        tabviewFragAdapter = new TabViewPagerFragAdapter(getChildFragmentManager());
        tabviewFragAdapter.addFragments(new Tab1Fragment(),"Items");
        tabviewFragAdapter.addFragments(new Tab2Fragment(),"Persons");

        viewPager.setAdapter(tabviewFragAdapter);
    }
}
