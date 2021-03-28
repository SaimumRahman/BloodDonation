package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.firestore.Model.Donation_History;
import com.example.firestore.Model.Request;
import com.example.firestore.Model.User;
import com.example.firestore.UI.RecyclerAdapterNotification;
import com.example.firestore.databinding.ActivityNotificationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

private ActivityNotificationBinding notificationBinding;
private RecyclerAdapterNotification recyclerAdapterNotification;
    private DatabaseReference notificationRef;
    private List<Donation_History> notificationArraylist = new ArrayList<>();
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(notificationBinding.getRoot());

        id=getIntent().getStringExtra("uniquekey");

        notificationRef=FirebaseDatabase.getInstance().getReference("Donation_History");
        recyclerAdapterNotification=new RecyclerAdapterNotification(NotificationActivity.this,notificationArraylist);
        notificationBinding.notificationRecycler.setLayoutManager(new LinearLayoutManager(this));

        showAllData();

    }

    private void showAllData() {

        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                        Donation_History donation_history=dataSnapshot.getValue(Donation_History.class);

                        if(donation_history.getDonner_id().equals(id)){

                            notificationArraylist.add(donation_history);

                        }

                    }
                    notificationBinding.notificationRecycler.setAdapter(recyclerAdapterNotification);
                    recyclerAdapterNotification.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}