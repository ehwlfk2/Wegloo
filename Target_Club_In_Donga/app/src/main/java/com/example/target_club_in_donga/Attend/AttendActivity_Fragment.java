package com.example.target_club_in_donga.Attend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

import static com.example.target_club_in_donga.Attend.AttendActivity_Home.uidPath;
import static com.example.target_club_in_donga.Attend.AttendActivity_Admin_Home.uidAdminPath;
//import static com.example.target_club_in_donga.MainActivity.clubName;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendActivity_Fragment extends Fragment {

    /*    private Gallery gallery;
        private Button select_btn;
        private Integer current_image_resource;*/
    private TextView activity_attend_detail_textview_attend_state;

//    private final int[] img = {R.drawable.aa, R.drawable.bb, R.drawable.cc, R.drawable.dd, R.drawable.ee};

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private Button activity_attend_detail_button_attendance, activity_attend_detail_button_cancel, activity_attend_detail_button_admin;
    private TextView activity_attend_detail_textview_people_count, activity_attend_detail_textview_people_percent;
    private TextView activity_attend_detail_textview_certification_number, activity_attend_detail_textview_attend_time_limit, activity_attend_detail_textview_tardy_time_limit;
    private TextView activity_attend_detail_textview_certification_number_name;

    private int peopleCount = 0, peopleAttendCount = 0;

    private int getEditCertificationNumber;
    private String getCertificationNumber, EditCertificationNumber;
    private String getAttend_Time_Limit, getTardy_Time_Limit;

    private long now;
    private String nowDate, formatDate, nowtardyTimeLimit;
    private String getAttendState, setAttendState, getTardyTimeLimit, getAttendState2;

    private int admin, attendCount = 0, tardyCount = 0, unsentCount = 0, absentCount = 0;
    private static int adminNumber = 2;

    private PieChart activity_attend_piechart;
    private String findkey, getState;

    private String clubName = "TCID";
    // 임시로 바꾼 부분

    public AttendActivity_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_attend_detail, container, false);

        findkey = uidPath;
        if (findkey == null) {
            findkey = uidAdminPath;
        }

        activity_attend_detail_textview_certification_number = (TextView) view.findViewById(R.id.activity_attend_detail_textview_certification_number);
        activity_attend_detail_textview_certification_number_name = (TextView) view.findViewById(R.id.activity_attend_detail_textview_certification_number_name);

        activity_attend_piechart = (PieChart) view.findViewById(R.id.activity_attend_piechart);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                admin = Integer.parseInt(dataSnapshot.getValue().toString());

                if (admin > adminNumber) {
                    activity_attend_detail_textview_certification_number_name.setVisibility(View.INVISIBLE);
                    activity_attend_detail_textview_certification_number.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_attend_piechart.setUsePercentValues(false);
        activity_attend_piechart.getDescription().setEnabled(true);
        activity_attend_piechart.setExtraOffsets(5, 10, 5, 5);

        activity_attend_piechart.setDragDecelerationFrictionCoef(0.95f);

        activity_attend_piechart.setDrawHoleEnabled(true);
        activity_attend_piechart.setHoleColor(Color.WHITE);
        activity_attend_piechart.setTransparentCircleRadius(61f);

        final ArrayList<PieEntry> pieEntries = new ArrayList<>();

        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    attendCount = 0;
                    tardyCount = 0;
                    unsentCount = 0;
                    absentCount = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("attend_state").getValue() != null) {

                            pieEntries.clear();
                            getAttendState2 = snapshot.child("attend_state").getValue(String.class);
                            if (getAttendState2.equals("출석")) {
                                attendCount++;
                            } else if (getAttendState2.equals("지각")) {
                                tardyCount++;
                            } else if (getAttendState2.equals("미출결")) {
                                unsentCount++;
                            } else {
                                absentCount++;
                            }
                        }
                    }

                    if (attendCount > 0) {
                        pieEntries.add(new PieEntry(attendCount , "출석"));
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

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "인원 수");
                    pieDataSet.setSliceSpace(3f);
                    pieDataSet.setSelectionShift(4f);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//                    pieDataSet.setColors(new int [] {R.drawable.border_green, R.drawable.border_orange, R.drawable.border_gray});

                    PieData pieData = new PieData((pieDataSet));
                    pieData.setValueTextSize(15f);
                    pieData.setValueTextColor(Color.WHITE);

                    activity_attend_piechart.setData(pieData);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("Attend_Certification_Number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    activity_attend_detail_textview_certification_number.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        return view;
    }

}