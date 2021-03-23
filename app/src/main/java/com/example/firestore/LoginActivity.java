package com.example.firestore;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.firestore.Model.User;
import com.example.firestore.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.aescrypt.AESCrypt;

import org.jetbrains.annotations.NonNls;

import java.security.GeneralSecurityException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME ="MyPrefsFile" ;
    private ActivityLoginBinding loginBinding;
    private DatabaseReference databaseReference;
    String passwordAfterDecrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        loginBinding.loginBtn.setOnClickListener(v ->{
         loginVerifications();

        });
        loginBinding.SignUpNowTv.setOnClickListener(v ->{
            startActivity(new Intent(LoginActivity.this,Sign_Up.class));
        });

    }


    private void loginVerifications() {
        String usermodileLogin=loginBinding.loginMobileEt.getText().toString();
        String passwordLogins= loginBinding.loginPasswordEt.getText().toString();

        if (TextUtils.isEmpty(usermodileLogin) || usermodileLogin.length()==10) {
            Toast.makeText(LoginActivity.this,"Please Enter Correct Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(passwordLogins)){
            Toast.makeText(LoginActivity.this,"Please Enter Correct Phone Number",Toast.LENGTH_SHORT).show();
        }else {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(usermodileLogin).exists()){

                        User user=snapshot.child(usermodileLogin).getValue(User.class);

                        try {
                            passwordAfterDecrypt = AESCrypt.decrypt(passwordLogins,user.getPassword());
                        }catch (GeneralSecurityException e){
                            Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        if (user.getUser_type().equals("Admin")){

                            if (user.getContact_number().equals(usermodileLogin) && passwordAfterDecrypt.equals(passwordLogins)){
                                Toast.makeText(LoginActivity.this,"ADMIN Successfully",Toast.LENGTH_SHORT).show();

                                //Toast.makeText(LoginActivity.this,"Logged Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this, HomeActivities.class);
                                intent.putExtra("uniquekey",user.getContact_number().toString());
                                startActivity(intent);
                            }

                        }

                       else if (user.getContact_number().equals(usermodileLogin) && passwordAfterDecrypt.equals(passwordLogins)){
                            Toast.makeText(LoginActivity.this,"Logged Successfully",Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(LoginActivity.this, HomeActivities.class);
                            intent.putExtra("uniquekey",user.getContact_number().toString());
                            startActivity(intent);
                        }
                        else {Toast.makeText(LoginActivity.this,"Logging Invalid",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }

}