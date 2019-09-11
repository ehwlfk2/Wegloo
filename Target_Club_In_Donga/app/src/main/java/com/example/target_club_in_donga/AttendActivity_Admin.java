package com.example.target_club_in_donga;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.Material_Management.MaterialManagementActivity_Admin;
import com.google.firebase.database.FirebaseDatabase;

public class AttendActivity_Admin extends AppCompatActivity {

    private Button activity_attend_admin_attendance_start;
    private int certification_number;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_admin);

        database = FirebaseDatabase.getInstance();

        activity_attend_admin_attendance_start = (Button) findViewById(R.id.activity_attend_admin_attendance_start);

        activity_attend_admin_attendance_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                certification_number =
//                database.getReference().child("Attend_Admin_Certification_Number").setValue();
            }
        });


    }
}
