package com.example.target_club_in_donga.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.Package_LogIn.LoginData;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.target_club_in_donga.MainActivity.clubName;

// 게시판 프래그먼트

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailActivity_Fragment extends Fragment {


    public UserDetailActivity_Fragment() {
        // Required empty public constructor
    }
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private TextView name, phone, school, email, studentID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_user_detail, container, false);
        name = view.findViewById(R.id.user_detail_name);
        phone = view.findViewById(R.id.user_detail_phoneNumber);
        school = view.findViewById(R.id.user_detail_school);
        email = view.findViewById(R.id.user_detail_email);
        studentID = view.findViewById(R.id.user_detail_studentID);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        database.getReference().child(clubName).child("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LoginData loginData = dataSnapshot.getValue(LoginData.class);
                name.setText(loginData.getName());
                email.setText(auth.getCurrentUser().getEmail());
                phone.setText(loginData.getPhone());
                school.setText(loginData.getSchool());
                studentID.setText(loginData.getStudentNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  view;
    } // activity_notice에 있는 화면을 가지고 온다

}
