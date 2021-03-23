package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firestore.Model.Request;
import com.example.firestore.Model.User;
import com.example.firestore.databinding.ActivityDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private String donner_id,request_id;
    private ActivityDetailsBinding detailsBinding;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String date,userType;
    private DatabaseReference typeRef;
    private FirebaseDatabase database;
    private DatabaseReference postRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(detailsBinding.getRoot());

        donner_id = getIntent().getStringExtra("mainid").toString();
        request_id = getIntent().getStringExtra("request_id").toString();
        //  Toast.makeText(getApplicationContext(),donner_id,Toast.LENGTH_LONG).show();

        userChecker();



    }

    private void userChecker() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    User user = snapshot.child(donner_id).getValue(User.class);

                    Toast.makeText(DetailsActivity.this,donner_id,Toast.LENGTH_LONG).show();
                    if (!user.getUser_type().equals("Admin")) {
                        final DatabaseReference db=FirebaseDatabase.getInstance().getReference("Requests");

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Request request=snapshot.child(request_id).getValue(Request.class);

                                final DatabaseReference dbr=FirebaseDatabase.getInstance().getReference("Users");

                                dbr.child(request.getDonner_id()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user1=snapshot.getValue(User.class);

                                        detailsBinding.emailtv.setText(user1.getEmail());
                                        detailsBinding.mobiletv.setText(user1.getContact_number());
                                        detailsBinding.nametv.setText(user1.getFirst_name());
                                        detailsBinding.presentaddresstv.setText(user1.getPresent_address());
                                        detailsBinding.religiontv.setText(user1.getReligion());
                                        detailsBinding.assignedDateTv.setText("Do You Sure Want To Donate Blood ? If Yes then Click Confirm Button");

                                        detailsBinding.confirmButtonID.setOnClickListener(v ->{
                                            confirmDonatoin();
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else {
                        final DatabaseReference db=FirebaseDatabase.getInstance().getReference("Requests");

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Request request=snapshot.child(request_id).getValue(Request.class);

                                final DatabaseReference dbr=FirebaseDatabase.getInstance().getReference("Users");

                                dbr.child(request.getMain_recipient_id()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user3=snapshot.getValue(User.class);

                         detailsBinding.emailtv.setText(user3.getEmail());
                        detailsBinding.mobiletv.setText(user3.getContact_number());
                        detailsBinding.nametv.setText(user3.getFirst_name());
                        detailsBinding.presentaddresstv.setText(user3.getPresent_address());
                        detailsBinding.religiontv.setText(user3.getReligion());

                        detailsBinding.assignedDateTv.setOnClickListener(v ->{
                            datePicker();
                        });
                        dateListenner();

                        detailsBinding.confirmButtonID.setOnClickListener(v ->{
                            inputAssignedDate();
                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


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

    private void confirmDonatoin() {

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,Object>hashMap=new HashMap<>();
                hashMap.put("status","assigned");

                databaseReference.child(request_id).updateChildren(hashMap).addOnCompleteListener(v ->{
                    Toast.makeText(getApplicationContext(),"Processed to ADMIN",Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inputAssignedDate() {

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    HashMap<String,Object> assignedDatehash=new HashMap<>();
                    assignedDatehash.put("assigned_date",detailsBinding.assignedDateTv.getText());
                    assignedDatehash.put("status","approved");

                    databaseReference.child(request_id).updateChildren(assignedDatehash).addOnCompleteListener(task -> {
                        Toast.makeText(getApplicationContext(),"Date Assigned Successfully",Toast.LENGTH_LONG).show();
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })    ;

    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog_MinWidth,
                dateSetListener, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void dateListenner() {
        dateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            date = month + "/" + dayOfMonth + "/" + year;
            detailsBinding.assignedDateTv.setText(date);
        };
    }
    private void getUserTypess(){
        typeRef=FirebaseDatabase.getInstance().getReference("Users");
        typeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.child(donner_id).getValue(User.class);
                    userType= user.getUser_type().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}