package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.firestore.Model.Request;
import com.example.firestore.Model.User;
import com.example.firestore.UI.RecyclerAdapterUsers;
import com.example.firestore.UI.RecyclerViewAdapterRequest;
import com.example.firestore.databinding.ActivityUserDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {

    private ActivityUserDetailsBinding userDetailsBinding;

    private RecyclerAdapterUsers adapterUsers;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private List<User> userArraylist = new ArrayList<>();

    private String donner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDetailsBinding=ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(userDetailsBinding.getRoot());

        donner_id=getIntent().getStringExtra("uniquekey");

        userDetailsBinding.recyclerViewUserss.setHasFixedSize(true);

        showAllRequests();


    }
    private void showAllRequests(){
        database = FirebaseDatabase.getInstance();
        adapterUsers = new RecyclerAdapterUsers(getApplicationContext(), userArraylist);
        userDetailsBinding.recyclerViewUserss.setLayoutManager(new LinearLayoutManager(this));

        userArraylist.clear();
        userRef = database.getReference("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userArraylist.add(user);
                }
                //Collections.reverse(requestArrayList);
                userDetailsBinding.recyclerViewUserss.setAdapter(adapterUsers);
                adapterUsers.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}