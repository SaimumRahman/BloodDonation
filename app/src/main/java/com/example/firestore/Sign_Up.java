package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.firestore.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Sign_Up extends AppCompatActivity {

    private ActivitySignUpBinding signUpBinding;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String date;
    private String password = "FireStore";
    private String blood_group;
    private String religion;
    private String encryptedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        signUpBinding.SubmitBtn.setOnClickListener(v -> {
            signUp();
        });
        signUpBinding.DOBTv.setOnClickListener(v -> {
            datePicker();
        });
        dateListenner();
        signUpBinding.loginNowTv.setOnClickListener(v ->{
            startActivity(new Intent(Sign_Up.this,LoginActivity.class));
        });



    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_Up.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                dateSetListener, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void dateListenner() {
        dateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;
            date = month + "/" + dayOfMonth + "/" + year;
            signUpBinding.DOBTv.setText(date);
        };
    }

    private void signUp() {

        String first_name = signUpBinding.firstNameEt.getText().toString();
        String last_name = signUpBinding.lastNameEt.getText().toString();
        String contact_number = signUpBinding.contactNumberEt.getText().toString();
        String alternate_contact_number = signUpBinding.altContactNumberEt.getText().toString();
        String email = signUpBinding.emailEt.getText().toString();
        signUpBinding.bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blood_group= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        signUpBinding.religionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                religion= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String present_address = signUpBinding.presentAddressEt.getText().toString();
        String weight = signUpBinding.weightEt.getText().toString();

        String password = signUpBinding.passwordEt.getText().toString();

       if (TextUtils.isEmpty(first_name)){Toast.makeText(Sign_Up.this, "Please Enter First Name ", Toast.LENGTH_SHORT).show();}
       else if(TextUtils.isEmpty(last_name)){Toast.makeText(Sign_Up.this, "Please Enter Last Name ", Toast.LENGTH_SHORT).show();}
       else if (TextUtils.isEmpty(contact_number) && (contact_number.length() <= 11)){Toast.makeText(Sign_Up.this, "Please Enter Correct Mobile Number", Toast.LENGTH_SHORT).show();}
       else if (TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){Toast.makeText(Sign_Up.this, "Please Enter Proper Email Address ", Toast.LENGTH_SHORT).show();}
       else if(blood_group == null){Toast.makeText(Sign_Up.this, "Please Select Blood Group ", Toast.LENGTH_SHORT).show();}
       else if (religion == null){Toast.makeText(Sign_Up.this, "Please Select Religion ", Toast.LENGTH_SHORT).show();}
       else if (TextUtils.isEmpty(present_address)){Toast.makeText(Sign_Up.this, "Please Enter Permanent Address", Toast.LENGTH_SHORT).show();}
       else if (TextUtils.isEmpty(weight)){Toast.makeText(Sign_Up.this, "Please Enter weight ", Toast.LENGTH_SHORT).show();}

       else if (TextUtils.isEmpty(password) && !(password.length() < 10)){Toast.makeText(Sign_Up.this, "Please Enter Password AT LEAST 10 LENGTH ", Toast.LENGTH_SHORT).show();}
       else if (date == null){Toast.makeText(Sign_Up.this, "Please Select DOB ", Toast.LENGTH_SHORT).show();}

       else {
           storeData(first_name, last_name, contact_number, alternate_contact_number, email, blood_group, religion, present_address, weight, password,date);
       }

    }



    private void storeData(String first_name, String last_name, String contact_number, String alternate_contact_number, String email, String blood_group, String religion, String present_address, String weight, String password, String date) {

        try {
             encryptedPassword = AESCrypt.encrypt(password,password);
        } catch (GeneralSecurityException e) {
            Toast.makeText(Sign_Up.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        final DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(contact_number).exists()){
                    HashMap<String,Object> hashUserEntry=new HashMap<>();
                    hashUserEntry.put("first_name",first_name);
                    hashUserEntry.put("last_name",last_name);
                    hashUserEntry.put("contact_number",contact_number);
                    hashUserEntry.put("alternate_contact_number",alternate_contact_number);
                    hashUserEntry.put("email",email);
                    hashUserEntry.put("blood_group",blood_group);
                    hashUserEntry.put("religion",religion);
                    hashUserEntry.put("present_address",present_address);
                    hashUserEntry.put("weight",weight);
                    hashUserEntry.put("dob",date);

                    hashUserEntry.put("password",encryptedPassword);
                    hashUserEntry.put("user_type","Donner");

                    reference.child(contact_number).updateChildren(hashUserEntry).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(Sign_Up.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Sign_Up.this,"Please Check Your Network", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(Sign_Up.this,"This"+contact_number+"already Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}