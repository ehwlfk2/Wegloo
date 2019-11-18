package com.example.target_club_in_donga.Package_LogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Reset_id_pw extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private LoginData loginData = new LoginData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_id_pw);
        ImageView back = findViewById(R.id.find_back);
        EditText signed_name = findViewById(R.id.find_username);
        EditText signed_Phone = findViewById(R.id.find_user_phonenumber);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String username = signed_name.getText().toString();
        final String phonenumber = signed_Phone.getText().toString();
        final String[] userkey = new String[1];
        Button findEmail_btn = findViewById(R.id.find_userID_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( phonenumber.equals(loginData.getPhone()) ){

                    Dialog_Find_Email dialog_find_email = new Dialog_Find_Email(getApplicationContext());
                }
                else{
                    Toast.makeText(Reset_id_pw.this, "잘못된 이름이나 휴대폰 번호입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        database.getReference().child("AppUser").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loginData = null;
                loginData = dataSnapshot.getValue(LoginData.class);
                userkey[0] = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
