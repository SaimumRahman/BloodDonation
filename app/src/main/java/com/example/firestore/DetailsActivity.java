package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.firestore.Model.Donation_History;
import com.example.firestore.Model.Request;
import com.example.firestore.Model.User;
import com.example.firestore.databinding.ActivityDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DetailsActivity extends AppCompatActivity {


    private String donner_id, request_id;
    private ActivityDetailsBinding detailsBinding;
    private DatePickerDialog datePickerDialog;
    private String date, userType;
    private DatabaseReference typeRef;
    private FirebaseDatabase database;
    private DatabaseReference postRef;
    private String saveCurrentDateMs;
    private static final AtomicInteger count = new AtomicInteger(0);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(detailsBinding.getRoot());

        donner_id = getIntent().getStringExtra("mainid").toString();
        request_id = getIntent().getStringExtra("request_id").toString();
        //  Toast.makeText(getApplicationContext(),donner_id,Toast.LENGTH_LONG).show();

        userChecker();
       // validation();


    }

    private void userChecker() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    User user = snapshot.child(donner_id).getValue(User.class);

                   // Toast.makeText(DetailsActivity.this, donner_id, Toast.LENGTH_LONG).show();
                    if (!user.getUser_type().equals("Admin")) {
                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Requests");

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Request request = snapshot.child(request_id).getValue(Request.class);

                                final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users");

                                dbr.child(request.getDonner_id()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user1 = snapshot.getValue(User.class);

                                        detailsBinding.emailtv.setText(user1.getEmail());
                                        detailsBinding.mobiletv.setText(user1.getContact_number());
                                        detailsBinding.nametv.setText(user1.getFirst_name());
                                        detailsBinding.presentaddresstv.setText(user1.getPresent_address());
                                        detailsBinding.religiontv.setText(user1.getReligion());
                                        detailsBinding.assignedDateTv.setText("Do You Sure Want To Donate Blood ? If Yes then Click Confirm Button");

                                        detailsBinding.confirmButtonID.setOnClickListener(v -> {

                                            validation();

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
                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Requests");

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Request request = snapshot.child(request_id).getValue(Request.class);

                                final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users");

                                dbr.child(request.getMain_recipient_id()).addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user3 = snapshot.getValue(User.class);

                                        detailsBinding.emailtv.setText(user3.getEmail());
                                        detailsBinding.mobiletv.setText(user3.getContact_number());
                                        detailsBinding.nametv.setText(user3.getFirst_name());
                                        detailsBinding.presentaddresstv.setText(user3.getPresent_address());
                                        detailsBinding.religiontv.setText(user3.getReligion());

                                        detailsBinding.assignedDateTv.setOnClickListener(v -> {
                                            datepickerini();
                                            datePickerDialog.show();


                                        });


                                        detailsBinding.confirmButtonID.setOnClickListener(v -> {
                                            historyList(request.getDonner_id(),request.getMain_recipient_id());
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

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", "assigned");

                databaseReference.child(request_id).updateChildren(hashMap).addOnCompleteListener(v -> {
                    Toast.makeText(getApplicationContext(), "Processed to ADMIN", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inputAssignedDate() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                HashMap<String, Object> assignedDatehash = new HashMap<>();
                assignedDatehash.put("assigned_date", detailsBinding.assignedDateTv.getText());
                assignedDatehash.put("status", "approved");

                databaseReference.child(request_id).updateChildren(assignedDatehash).addOnCompleteListener(task -> {
                    Toast.makeText(getApplicationContext(), "Date Assigned Successfully", Toast.LENGTH_LONG).show();
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getUserTypess() {
        typeRef = FirebaseDatabase.getInstance().getReference("Users");
        typeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.child(donner_id).getValue(User.class);
                    userType = user.getUser_type().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void historyList(String donnerId, String main_recipientid){
        final int donationHistoryid;
        donationHistoryid=count.incrementAndGet();

        final DatabaseReference db=FirebaseDatabase.getInstance().getReference("Donation_History");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("job_id",String.valueOf(donationHistoryid));
                hashMap.put("request_id",request_id);
                hashMap.put("donation_date",date);
                hashMap.put("reciepient_id",donner_id);
                hashMap.put("donner_id",main_recipientid);

                db.child(String.valueOf(donationHistoryid)).updateChildren(hashMap).addOnCompleteListener(task -> {
                    Toast.makeText(DetailsActivity.this,"History Added",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void validation(){
        final DatabaseReference dd=FirebaseDatabase.getInstance().getReference("Donation_History");
        dd.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot snapshot1:snapshot.getChildren()){

            Donation_History donation_history=snapshot1.getValue(Donation_History.class);

            if (donation_history.getDonner_id().equals(donner_id)){

                Calendar callForDate = Calendar.getInstance();

              SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
              saveCurrentDateMs = currentDate.format(callForDate.getTime());

                String dates= donation_history.getDonation_date();

                DateTimeFormatter df = DateTimeFormatter.ofPattern("d-MMM-yyyy");
                LocalDate date1= LocalDate.parse(dates,df);
                LocalDate date15= LocalDate.parse(saveCurrentDateMs,df);
              //  Log.v("Days","OKAY db"+ date1);
               // Log.v("Days","OKAY simple"+ date15);


               Long datediff = ChronoUnit.DAYS.between(date15,date1);

               Log.v("Days","Final"+ datediff);

               if(datediff>=90){

                   confirmDonatoin();

               }
               else {
                   Toast.makeText(DetailsActivity.this,"Sorry You Cannot Donatte Blodd",Toast.LENGTH_SHORT).show();
               }

            }


        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void datepickerini(){
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
            month+=1;
             date= makedatestring(day,month,year);
             detailsBinding.assignedDateTv.setText(date);

            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style= AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog =new DatePickerDialog(this,style,dateSetListener,year,month,day);

    }

    private String makedatestring(int day, int month, int year) {
        return day+"-"+getMonthformat(month)+"-"+year;
    }

    private String getMonthformat(int month) {
        if (month==1){return "Jan";}
        if (month==2){return "Feb";}
        if (month==3){return "Mar";}
        if (month==4){return "Apr";}
        if (month==5){return "May";}
        if (month==6){return "Jun";}
        if (month==7){return "Jul";}
        if (month==8){return "Aug";};
        if (month==9){return "Sep ";}
        if (month==10){return "Oct";}
        if (month==11){return "Nov";}
        if (month==12){return "Dec";}
        return "Jan";
    }




}