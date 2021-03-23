package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.firestore.Model.User;
import com.example.firestore.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding profileBinding;
    private DatabaseReference databaseReference;
    private String donner_id;
    private String blood_group;
    private String religion;
    private String date;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());

        donner_id=getIntent().getStringExtra("uniquekey");

        Toast.makeText(getApplicationContext(),donner_id,Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(donner_id);

        getUserData();
        profileBinding.DOBTv.setOnClickListener(v -> {
            datePicker();
        });
        dateListenner();
        profileBinding.SubmitBtn.setOnClickListener(v -> {
            signUp();
            finish();

        });



    }

    private void getUserData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                   User user= snapshot.getValue(User.class);

                   profileBinding.altContactNumberEt.setText(user.getAlternate_contact_number());

                   profileBinding.DOBTv.setText(user.getDob());
                   profileBinding.emailEt.setText(user.getEmail());
                   profileBinding.bloodGroupSpinner.autofill(AutofillValue.forText(user.getBlood_group().toString()));
                   profileBinding.firstNameEt.setText(user.getFirst_name());
                   profileBinding.lastNameEt.setText(user.getLast_name());
                   profileBinding.presentAddressEt.setText(user.getPresent_address());
                   profileBinding.weightEt.setText(user.getWeight());
                   profileBinding.religionSpinner.autofill(AutofillValue.forText(user.getReligion()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            profileBinding.DOBTv.setText(date);
        };
    }
    private void signUp() {

        String first_name = profileBinding.firstNameEt.getText().toString();
        String last_name = profileBinding.lastNameEt.getText().toString();

        String alternate_contact_number = profileBinding.altContactNumberEt.getText().toString();
        String email = profileBinding.emailEt.getText().toString();
        profileBinding.bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blood_group= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        profileBinding.religionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                religion= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String present_address = profileBinding.presentAddressEt.getText().toString();
        String weight = profileBinding.weightEt.getText().toString();


        if (TextUtils.isEmpty(first_name)){Toast.makeText(ProfileActivity.this, "Please Enter First Name ", Toast.LENGTH_SHORT).show();}
        else if(TextUtils.isEmpty(last_name)){Toast.makeText(ProfileActivity.this, "Please Enter Last Name ", Toast.LENGTH_SHORT).show();}

        else if (TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){Toast.makeText(ProfileActivity.this, "Please Enter Proper Email Address ", Toast.LENGTH_SHORT).show();}
        else if(blood_group == null){Toast.makeText(this, "Please Select Blood Group ", Toast.LENGTH_SHORT).show();}
        else if (religion == null){Toast.makeText(this, "Please Select Religion ", Toast.LENGTH_SHORT).show();}
        else if (TextUtils.isEmpty(present_address)){Toast.makeText(this, "Please Enter Permanent Address", Toast.LENGTH_SHORT).show();}
        else if (TextUtils.isEmpty(weight)){Toast.makeText(this, "Please Enter weight ", Toast.LENGTH_SHORT).show();}

        else if (date == null){Toast.makeText(this, "Please Select DOB ", Toast.LENGTH_SHORT).show();}

        else {
            storeData(first_name, last_name, alternate_contact_number, email, blood_group, religion, present_address, weight, date);
        }

    }
    private void storeData(String first_name, String last_name, String alternate_contact_number, String email, String blood_group, String religion, String present_address, String weight, String date) {


        final DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(donner_id).exists()){
                    HashMap<String,Object> hashUserEntry=new HashMap<>();
                    hashUserEntry.put("first_name",first_name);
                    hashUserEntry.put("last_name",last_name);

                    hashUserEntry.put("alternate_contact_number",alternate_contact_number);
                    hashUserEntry.put("email",email);
                    hashUserEntry.put("blood_group",blood_group);
                    hashUserEntry.put("religion",religion);
                    hashUserEntry.put("present_address",present_address);
                    hashUserEntry.put("weight",weight);
                    hashUserEntry.put("dob",date);


                    hashUserEntry.put("user_type","Donner");

                    reference.child(donner_id).updateChildren(hashUserEntry).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfileActivity.this,HomeActivities.class));
                        }
                        else{
                            Toast.makeText(ProfileActivity.this,"Please Check Your Network", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                   // Toast.makeText(Sign_Up.this,"This"+contact_number+"already Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}