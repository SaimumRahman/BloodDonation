package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.firestore.Model.Request;
import com.example.firestore.Model.User;
import com.example.firestore.UI.RecyclerViewAdapterRequest;
import com.example.firestore.databinding.ActivityHomeBinding;
import com.example.firestore.databinding.AddrequestviewBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DonnerActivity extends AppCompatActivity {

    private ActivityHomeBinding homeBinding;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private AddrequestviewBinding addrequestviewBinding;
    private DatePickerDialog.OnDateSetListener dateSetListenerto, dateSetListenerfrom;
    private String dateto, datefrom;
    private String saveCurrentDateMs, saveCurrentTimeMs;
    private String donner_id;
    private String requested_blood;

    private RecyclerViewAdapterRequest adapter;
    private FirebaseDatabase database;
    private DatabaseReference postRef,adminref;
    private List<Request> requestArrayList = new ArrayList<>();

   // private String userType;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

//       if (donner_id==null){
//           showAllRequests();
//          // homeBinding.addRequestButton.setVisibility(View.GONE);
//       }else {
           donner_id = getIntent().getExtras().getString("uniquekey");

          adminView();

         //  showAllRequests();
           homeBinding.addRequestButton.setOnClickListener(v -> {
               createPopUp();
           });

       //}



    }
    private void adminView(){
        adminref=FirebaseDatabase.getInstance().getReference("Users");
        adminref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.child(donner_id).getValue(User.class);
                   // userType= user.getUser_type().toString();
                    database = FirebaseDatabase.getInstance();
                    adapter = new RecyclerViewAdapterRequest(DonnerActivity.this, requestArrayList,donner_id);
                    homeBinding.recyclerViewrequest.setLayoutManager(new LinearLayoutManager(DonnerActivity.this));

                    requestArrayList.clear();
                    postRef = database.getReference("Requests");
                    postRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){

                                if (!user.getUser_type().equals("Admin")){

                                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {

                                        Request request = dataSnapshot.getValue(Request.class);

                                        if (request.getStatus().equals("pending")){
                                            requestArrayList.add(request);
                                        }

                                    }
                                    //Collections.reverse(requestArrayList);
                                    homeBinding.recyclerViewrequest.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                                        Request request=dataSnapshot.getValue(Request.class);

                                        if (request.getStatus().equals("assigned")){
                                            requestArrayList.add(request);
                                        }

                                    }
                                    homeBinding.recyclerViewrequest.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
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
    private void showAllRequests(){




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPopUp() {

        builder = new AlertDialog.Builder(this);
        addrequestviewBinding = AddrequestviewBinding.inflate(getLayoutInflater());
        builder.setView(addrequestviewBinding.getRoot());

        alertDialog = builder.create();
        alertDialog.show();

        addrequestviewBinding.requestedTimeFrameFromTv.setOnClickListener(v -> {
            datePickerfrom();
        });
        dateListennerfrom();

        addrequestviewBinding.requestedTimeFrameToTv.setOnClickListener(v -> {
            datePickerTo();
        });
        dateListennerTo();
        addrequestviewBinding.RequestedBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requested_blood = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addrequestviewBinding.SubmitRequestBtn.setOnClickListener(v -> {

            String preferredLocation = addrequestviewBinding.preferredLocationEt.getText().toString();
            String relationshipBlodd = addrequestviewBinding.relationshipWithBloodRecipientEt.getText().toString();
            String altContactNumber = addrequestviewBinding.altContactNumberRequestEt.getText().toString();

            if (TextUtils.isEmpty(preferredLocation)) {
                Toast.makeText(DonnerActivity.this, "Please Enter Preferred Location ", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(relationshipBlodd)) {
                Toast.makeText(DonnerActivity.this, "Please Enter Blood Relation ", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(altContactNumber)) {
                Toast.makeText(DonnerActivity.this, "Please Enter Alternate Contact Number ", Toast.LENGTH_SHORT).show();
            } else if (datefrom == null) {
                Toast.makeText(DonnerActivity.this, "Please Enter requested time ", Toast.LENGTH_SHORT).show();
            } else if (dateto == null) {
                Toast.makeText(DonnerActivity.this, "Please Enter requested time ", Toast.LENGTH_SHORT).show();
            } else if (donner_id == null) {
                Toast.makeText(DonnerActivity.this, "Please Enter DonnerID ", Toast.LENGTH_SHORT).show();
            } else if (requested_blood == null) {
                Toast.makeText(DonnerActivity.this, "Please Enter Blood Group ", Toast.LENGTH_SHORT).show();
            } else {
                submitData(preferredLocation, relationshipBlodd, altContactNumber, datefrom, dateto, donner_id, requested_blood);
                alertDialog.dismiss();
                requestArrayList.clear();
            }
        });


    }

    private void submitData(String preferredLocation, String relationshipBlodd, String altContactNumber,
                            String datefrom, String dateto, String donner_id, String requested_blood) {

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        saveCurrentDateMs = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTimeMs = currentTime.format(callForDate.getTime());

        final DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("Requests");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child(saveCurrentTimeMs).exists()) {
                    HashMap<String, Object> hashRequestEntry = new HashMap<>();
                    hashRequestEntry.put("recipient_id", saveCurrentTimeMs);
                    hashRequestEntry.put("donner_id", donner_id);
                    hashRequestEntry.put("preferred_location", preferredLocation);
                    hashRequestEntry.put("relationship_with_blood_recipient", relationshipBlodd);
                    hashRequestEntry.put("alternate_mobile_number", altContactNumber);
                    hashRequestEntry.put("requested_time_frame_to", dateto);
                    hashRequestEntry.put("requested_time_frame_from", datefrom);
                    hashRequestEntry.put("requested_blood", requested_blood);
                    hashRequestEntry.put("status", "pending");
                    hashRequestEntry.put("main_recipient_id", donner_id);

                    reference.child(saveCurrentTimeMs).updateChildren(hashRequestEntry).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DonnerActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DonnerActivity.this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Toast.makeText(Sign_Up.this,"This"+contact_number+"already Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void datePickerTo() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DonnerActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                dateSetListenerto, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();

    }

    private void dateListennerTo() {
        dateSetListenerto = (view, year, month, dayOfMonth) -> {
            month += 1;
            dateto = month + "/" + dayOfMonth + "/" + year;

            addrequestviewBinding.requestedTimeFrameToTv.setText(dateto);
        };

    }

    private void datePickerfrom() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DonnerActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                dateSetListenerfrom, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();

    }

    private void dateListennerfrom() {
        dateSetListenerfrom = (view, year, month, dayOfMonth) -> {
            month += 1;
            datefrom = year + "-" + month + "-" + dayOfMonth;

            addrequestviewBinding.requestedTimeFrameFromTv.setText(datefrom);
        };

    }


}