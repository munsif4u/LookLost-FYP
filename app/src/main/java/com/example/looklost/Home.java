package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    // For Bottom Navigation
    FragmentTransaction fragmentTransaction;
    FragmentContainerView containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        NavigationBarView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(this);

        containerView = findViewById(R.id.fragment_container);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerView.getId(), new HomeFragment());
        fragmentTransaction.commit();

        //For Coming from ContactUser
        int fragmentId = getIntent().getIntExtra("fragmentId", 0);
        if (fragmentId != 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the container with the desired fragment
            fragmentTransaction.replace(R.id.fragment_container, chatFragment.newInstance(fragmentId));
            fragmentTransaction.commit();
            //            navigationView.setSelectedItemId(fragmentId);
        }
        //For Coming from ContactUser
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_1) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerView.getId(), new HomeFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.item_2) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerView.getId(), new ItemsFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.item_3) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerView.getId(), new PersonFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.item_4) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerView.getId(), new chatFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.item_5) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerView.getId(), new ProfileFragment());
            fragmentTransaction.commit();
        }
        return true;
    }
}