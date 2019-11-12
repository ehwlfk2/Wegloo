package com.example.target_club_in_donga.Attend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendActivity_Admin_Privacy_Information extends AppCompatActivity {
    private Button activity_attend_detail_button_admin, activity_attend_detail_button_attendance, activity_attend_detail_button_cancel;
    private PieChart activity_attend_piechart;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private String userName, userPhone, userId;
    private int attendCount = 0, tardyCount = 0, unsentCount = 0, absentCount = 0, flag = 0, absent;

    private String clubName = "TCID";
    // 임시로 바꾼 부분

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_detail);

        Intent intent = getIntent();
        userName = intent.getExtras().getString("userName");
        userPhone = intent.getExtras().getString("userPhone");

        activity_attend_detail_button_admin = (Button) findViewById(R.id.activity_attend_detail_button_admin);
        activity_attend_detail_button_attendance = (Button) findViewById(R.id.activity_attend_detail_button_attendance);
        activity_attend_detail_button_cancel = (Button) findViewById(R.id.activity_attend_detail_button_cancel);

        activity_attend_piechart = (PieChart) findViewById(R.id.activity_attend_piechart);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        activity_attend_detail_button_admin.setVisibility(View.GONE);
        activity_attend_detail_button_attendance.setVisibility(View.GONE);

        database.getReference().child("EveryClub").child(clubName).child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    database.getReference().child("EveryClub").child(clubName).child("User").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("name").getValue().equals(userName) && dataSnapshot.child("phone").getValue().equals(userPhone)) {
                                userId = dataSnapshot.getKey();

                                activity_attend_piechart.setUsePercentValues(true);
                                activity_attend_piechart.getDescription().setEnabled(true);
                                activity_attend_piechart.setExtraOffsets(5, 10, 5, 5);

                                activity_attend_piechart.setDragDecelerationFrictionCoef(0.95f);

                                activity_attend_piechart.setDrawHoleEnabled(true);
                                activity_attend_piechart.setHoleColor(Color.WHITE);
                                activity_attend_piechart.setTransparentCircleRadius(61f);

                                final ArrayList<PieEntry> pieEntries = new ArrayList<>();

                                database.getReference().child("EveryClub").child(clubName).child("Attend").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {
                                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if (snapshot.child("User_State").child(userId).child("attend_state").getValue(String.class).equals("출석")) {
                                                attendCount++;
                                            } else if (snapshot.child("User_State").child(userId).child("attend_state").getValue(String.class).equals("지각")) {
                                                tardyCount++;
                                            } else if (snapshot.child("User_State").child(userId).child("attend_state").getValue(String.class).equals("미출결")) {
                                                unsentCount++;
                                            } else {
                                                absentCount++;
                                            }
                                        }

                                        if (attendCount > 0) {
                                            pieEntries.add(new PieEntry(attendCount, "출석"));
                                        }
                                        if (tardyCount > 0) {
                                            pieEntries.add(new PieEntry(tardyCount, "지각"));
                                        }
                                        if (unsentCount > 0) {
                                            pieEntries.add(new PieEntry(unsentCount, "미출결"));
                                        }
                                        if (absentCount > 0) {
                                            pieEntries.add(new PieEntry(absentCount, "결석"));
                                        }

                                        Description description = new Description();
                                        description.setText("출석률");
                                        description.setTextSize(15);
                                        activity_attend_piechart.setDescription(description);

                                        activity_attend_piechart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                                        PieDataSet pieDataSet = new PieDataSet(pieEntries, "%");
                                        pieDataSet.setSliceSpace(3f);
                                        pieDataSet.setSelectionShift(4f);
                                        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//                                        pieDataSet.setColors(new int[]{R.drawable.border_green, R.drawable.border_orange, R.drawable.border_gray});

                                        PieData pieData = new PieData((pieDataSet));
                                        pieData.setValueTextSize(15f);
                                        pieData.setValueTextColor(Color.WHITE);

                                        activity_attend_piechart.setData(pieData);

                                    }

                                    @Override
                                    public void onCancelled(final DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(final DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_attend_detail_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                finish();
            }
        });

    }
}