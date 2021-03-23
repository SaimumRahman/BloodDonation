package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.firestore.Model.Request;
import com.example.firestore.Model.User;
import com.example.firestore.databinding.ActivityNotificationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends AppCompatActivity {
private String id;
private ActivityNotificationBinding notificationBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationBinding=ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(notificationBinding.getRoot());

        id=getIntent().getStringExtra("uniquekey");

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User i=snapshot.child(id).getValue(User.class);
                if(!i.getUser_type().equals("Admin")){

                    final DatabaseReference dbs=FirebaseDatabase.getInstance().getReference("Requests");
                    dbs.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1: snapshot.getChildren()){

                                Request request=snapshot1.getValue(Request.class);

                                if(request.getStatus().equals("approved") && request.getMain_recipient_id().equals(id)){

                                    notificationBinding.assignedDateTv.setText(request.getAssigned_date());

                                    final DatabaseReference rr=FirebaseDatabase.getInstance().getReference("Users");
                                    rr.child(request.getDonner_id()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User userss=snapshot.getValue(User.class);

                                            notificationBinding.nametv.setText(userss.getFirst_name());
                                            notificationBinding.mobiletv.setText(userss.getContact_number());
                                            notificationBinding.emailtv.setText(userss.getEmail());
                                            notificationBinding.presentaddresstv.setText(userss.getPresent_address());

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}