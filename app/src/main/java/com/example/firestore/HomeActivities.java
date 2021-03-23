package com.example.firestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.firestore.databinding.ActivityDonnerBinding;

public class HomeActivities extends AppCompatActivity {

    private ActivityDonnerBinding donnerBinding;
    private String donner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        donnerBinding = ActivityDonnerBinding.inflate(getLayoutInflater());
        setContentView(donnerBinding.getRoot());

        donner_id = getIntent().getStringExtra("uniquekey");

        donnerBinding.donateBloodBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivities.this, DonnerActivity.class);
            intent.putExtra("uniquekey", donner_id);
            startActivity(intent);
        });
        donnerBinding.findadonnerbtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivities.this, UserDetailsActivity.class);
            intent.putExtra("uniquekey", donner_id);
            startActivity(intent);
        });
        donnerBinding.myprofilebt.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivities.this, ProfileActivity.class);
            intent.putExtra("uniquekey", donner_id);
            startActivity(intent);
        });
         donnerBinding.notificationsbtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivities.this, NotificationActivity.class);
            intent.putExtra("uniquekey", donner_id);
            startActivity(intent);
        });

    }
}