package com.example.target_club_in_donga.menu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.Attend.Attend_Information_Item;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_MyInformation extends AppCompatActivity {
    private RecyclerView activity_attend_my_information_recyclerview_main_list;

    private List<Attend_Information_Item> attendAdminItems = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();
    private ArrayList<String> listStartTime = new ArrayList<>();

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private int listSize = 0;
    private String startTime;

    private int admin, attendCount = 0, tardyCount = 0, unsentCount = 0, absentCount = 0, checkPage, menu_count = 0;

    private PieChart activity_attend_my_information_piechart;

    final int[] MY_COLORS = {Color.rgb(152, 247, 145), Color.rgb(255, 187, 0), Color.rgb(189, 189, 189), Color.rgb(255, 0, 0)};
    ArrayList<Integer> colors = new ArrayList<Integer>();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_my_information);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        activity_attend_my_information_piechart = (PieChart) findViewById(R.id.activity_attend_my_information_piechart);

        activity_attend_my_information_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_my_information_recyclerview_main_list);
        activity_attend_my_information_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(AttendActivity_MyInformation.this));

        final AttendActivity_MyInformation.MyInformationActivity_AdminRecyclerViewAdapter MyInformationActivity_adminRecyclerViewAdapter = new AttendActivity_MyInformation.MyInformationActivity_AdminRecyclerViewAdapter();

        activity_attend_my_information_recyclerview_main_list.setAdapter(MyInformationActivity_adminRecyclerViewAdapter);
        MyInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        activity_attend_my_information_piechart.setUsePercentValues(true);
        activity_attend_my_information_piechart.getDescription().setEnabled(true);
        activity_attend_my_information_piechart.setExtraOffsets(5, 10, 5, 5);

        activity_attend_my_information_piechart.setDragDecelerationFrictionCoef(0.95f);

        activity_attend_my_information_piechart.setDrawHoleEnabled(true);
        activity_attend_my_information_piechart.setHoleColor(Color.WHITE);
        activity_attend_my_information_piechart.setTransparentCircleRadius(61f);

        final ArrayList<PieEntry> pieEntries = new ArrayList<>();

        database.getReference().child("EveryClub").child(clubName).child("Attend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    attendCount = 0;
                    tardyCount = 0;
                    unsentCount = 0;
                    absentCount = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").getValue(String.class) != null) {
                            if (snapshot.child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").getValue(String.class).equals("출석")) {
                                attendCount++;
                            } else if (snapshot.child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").getValue(String.class).equals("지각")) {
                                tardyCount++;
                            } else if (snapshot.child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").getValue(String.class).equals("미출결")) {
                                unsentCount++;
                            } else {
                                absentCount++;
                            }
                        }
                    }

                    if (attendCount > 0) {
                        pieEntries.add(new PieEntry(attendCount, "출석"));
                        colors.add(MY_COLORS[0]);
                    }
                    if (tardyCount > 0) {
                        pieEntries.add(new PieEntry(tardyCount, "지각"));
                        colors.add(MY_COLORS[1]);
                    }
                    if (unsentCount > 0) {
                        pieEntries.add(new PieEntry(unsentCount, "미출결"));
                        colors.add(MY_COLORS[2]);
                    }
                    if (absentCount > 0) {
                        pieEntries.add(new PieEntry(absentCount, "결석"));
                        colors.add(MY_COLORS[3]);
                    }

                    Description description = new Description();
                    description.setText("출석률");
                    description.setTextSize(15);
                    activity_attend_my_information_piechart.setDescription(description);

                    activity_attend_my_information_piechart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "%");
                    pieDataSet.setSliceSpace(3f);
                    pieDataSet.setSelectionShift(4f);
//                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    pieDataSet.setColors(colors);

                    PieData pieData = new PieData((pieDataSet));
                    pieData.setValueTextSize(15f);
                    pieData.setValueTextColor(Color.WHITE);

                    activity_attend_my_information_piechart.setData(pieData);
                } else {
                    Toast.makeText(AttendActivity_MyInformation.this, "출결 현황이 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        database.getReference().child("EveryClub").child(clubName).child("Attend").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attendAdminItems.clear();
                uidLists.clear();
                listSize = 0;
                for (final DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.getValue() != null) {
                                        if (snapshot.getKey().equals(auth.getCurrentUser().getUid())) {
                                            startTime = snapshot2.child("startTime").getValue().toString();
                                            listStartTime.add(startTime);
                                            Attend_Information_Item attendAdminInformationItem = snapshot.getValue(Attend_Information_Item.class);
                                            String uidKey = snapshot.getKey();
                                            attendAdminItems.add(0, attendAdminInformationItem);
                                            uidLists.add(0, uidKey);
                                            listSize++;

                                            for (int i = 0; i < listSize; i++) {
                                                attendAdminItems.get(i).attendTimeLimit = listStartTime.get(listSize - 1 - i);
                                            }

                                            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                                            MyInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
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
    }

    class MyInformationActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_information_item_linearlayout;
            TextView activity_attend_information_item_textview_date;
            TextView activity_attend_information_item_textview_attend_state;
            ImageView activity_attend_information_item_imageview;
            TextView activity_attend_information_item_textview_tardy_time;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_information_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_information_item_linearlayout);
                activity_attend_information_item_textview_date = (TextView) view.findViewById(R.id.activity_attend_information_item_textview_date);
                activity_attend_information_item_textview_attend_state = (TextView) view.findViewById(R.id.activity_attend_information_item_textview_attend_state);
                activity_attend_information_item_imageview = (ImageView) view.findViewById(R.id.activity_attend_information_item_imageview);
                activity_attend_information_item_textview_tardy_time = (TextView) view.findViewById(R.id.activity_attend_information_item_textview_tardy_time);

            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_attend_information_item, viewGroup, false);

            return new AttendActivity_MyInformation.MyInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final AttendActivity_MyInformation.MyInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((AttendActivity_MyInformation.MyInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_information_item_textview_date.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_information_item_textview_attend_state.setText(attendAdminItems.get(position).attend_state);
            customViewHolder.activity_attend_information_item_textview_date.setText(attendAdminItems.get(position).attendTimeLimit);
            customViewHolder.activity_attend_information_item_textview_tardy_time.setText(attendAdminItems.get(position).late_time);
            customViewHolder.activity_attend_information_item_imageview.setVisibility(View.INVISIBLE);

        }

        @Override
        public int getItemCount() {
            return attendAdminItems.size();
        }

    }

}
