package com.example.target_club_in_donga.Package_LogIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Reset_id_pw extends AppCompatActivity {
    private FirebaseDatabase database;
    EditText signed_name, signed_Phone, signed_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_id_pw);
        ImageView back = findViewById(R.id.find_back);
        signed_name = findViewById(R.id.find_username);
        signed_Phone = findViewById(R.id.find_user_phonenumber);
        signed_email = findViewById(R.id.find_user_email);
        database = FirebaseDatabase.getInstance();
        Button findEmail_btn = findViewById(R.id.find_userID_btn);
        Button findpw = findViewById(R.id.find_user_pw);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordReset();
            }
        });
        findEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

            }
        });

    }
    public void sendPasswordReset() {
        // [START send_password_reset]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String emailAddress = signed_email.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Reset_id_pw.this, "가입된 메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END send_password_reset]
    }
    private void getData(){
        final String username = signed_name.getText().toString();
        final String phonenumber = signed_Phone.getText().toString();
        database.getReference().child("AppUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int flag = 0;
                for ( DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    AppLoginData appLoginData = snapshot.getValue(AppLoginData.class);
                    if (appLoginData.getName().equals(username) && appLoginData.getPhone().equals(phonenumber) ) {
                        if( username == null | phonenumber == null){
                            Toast.makeText(Reset_id_pw.this, "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Dialog_Find_Email dialog_find_email = new Dialog_Find_Email(Reset_id_pw.this);
                            dialog_find_email.callFunction(username, appLoginData.getEmailLoginEmail());
                            flag = 1;
                            break;
                        }
                    }
                }
                if( flag == 0 ) {
                    Toast.makeText(Reset_id_pw.this, "잘못된 이름이나 휴대폰 번호입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}