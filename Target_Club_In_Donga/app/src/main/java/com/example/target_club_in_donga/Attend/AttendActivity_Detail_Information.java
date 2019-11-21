package com.example.target_club_in_donga.Attend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_Detail_Information extends AppCompatActivity {

//    private final int[] img = {R.drawable.aa, R.drawable.bb, R.drawable.cc, R.drawable.dd, R.drawable.ee};

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private Button activity_attend_detail_button_attend_state;
    private TextView activity_attend_detail_textview_attend;

    private int flag = 0, backflag = 0;

    private String getAttendState;

    private int admin, attendCount = 0, tardyCount = 0, unsentCount = 0, absentCount = 0, checkPage, menu_count = 0;
    private static int adminNumber = 2;

    private PieChart activity_attend_piechart;
    private String findkey, getState;

    private SlidingDrawer activity_attend_detail_slidingdrawer;

    private RecyclerView activity_attend_detail_recyclerview_main_list;
    private List<Attend_Admin_Change_Item> attendItems = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_detail);

        Intent intent = getIntent();
        intent.getExtras().getString("uidPath");

        findkey = intent.getExtras().getString("uidPath");
        if (findkey == null) {
            findkey = intent.getExtras().getString("uidAdminPath");
        }

        activity_attend_detail_textview_attend = (TextView) findViewById(R.id.activity_attend_detail_textview_attend);
        activity_attend_detail_button_attend_state = (Button) findViewById(R.id.activity_attend_detail_button_attend_state);
        activity_attend_detail_slidingdrawer = (SlidingDrawer) findViewById(R.id.activity_attend_detail_slidingdrawer);

        activity_attend_piechart = (PieChart) findViewById(R.id.activity_attend_piechart);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        checkPage = intent.getExtras().getInt("checkPage");

        activity_attend_detail_slidingdrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                menu_count++;
            }
        });

        activity_attend_detail_slidingdrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                menu_count--;
            }
        });

        final AttendActivity_Detail_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter attendAdminInformationActivity_adminRecyclerViewAdapter = new AttendActivity_Detail_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter();

        activity_attend_detail_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_detail_recyclerview_main_list);
        activity_attend_detail_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        activity_attend_detail_recyclerview_main_list.setAdapter(attendAdminInformationActivity_adminRecyclerViewAdapter);

//        attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attendItems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("attend_state").getValue().equals("출석")) {
                        Attend_Admin_Change_Item attendAdminChangeItem = snapshot.getValue(Attend_Admin_Change_Item.class);
                        String uidKey = snapshot.getKey();
                        attendItems.add(attendAdminChangeItem);
                        uidLists.add(uidKey);
                    }
                    attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                }

                if (attendItems.size() == 0) {
                    activity_attend_detail_slidingdrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
                        @Override
                        public void onDrawerOpened() {
                            Toast.makeText(AttendActivity_Detail_Information.this, "출석현황이 없습니다", Toast.LENGTH_SHORT).show();
                            menu_count++;
                        }
                    });
                }

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

/*        activity_attend_detail_textview_attend.setText("전체현황");
          // 전체출결을 볼 떄 썼음

        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attendItems.clear();
                uidLists.clear();
                if (dataSnapshot.child("late_time").getValue(String.class) != null) {
                    flag = 1;
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attend_Admin_Change_Item attendAdminChangeItem = snapshot.getValue(Attend_Admin_Change_Item.class);
                    String uidKey = snapshot.getKey();
                    if (flag == 1) {
                        attendAdminChangeItem.late_time = snapshot.child("late_time").getValue().toString();
                    }
                    attendItems.add(attendAdminChangeItem);
                    uidLists.add(uidKey);
                }
                flag = 0;
                attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });*/

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
                            getAttendState = snapshot.child("attend_state").getValue(String.class);
                            if (getAttendState.equals("출석")) {
                                attendCount++;
                            } else if (getAttendState.equals("지각")) {
                                tardyCount++;
                            } else if (getAttendState.equals("미출결")) {
                                unsentCount++;
                            } else {
                                absentCount++;
                            }
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
                    description.setTextSize(30f);
                    activity_attend_piechart.setDescription(description);

                    activity_attend_piechart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "인원 수");
                    pieDataSet.setSliceSpace(3f);
                    pieDataSet.setSelectionShift(4f);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//                    pieDataSet.setColors(new int [] {R.drawable.border_green, R.drawable.border_orange, R.drawable.border_gray});

                    PieData pieData = new PieData((pieDataSet));
                    pieData.setValueTextSize(20f);
                    pieData.setValueTextColor(Color.YELLOW);

                    activity_attend_piechart.setData(pieData);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        if (checkPage == 0 || checkPage == 1) {
            activity_attend_detail_button_attend_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final PopupMenu popup = new PopupMenu(AttendActivity_Detail_Information.this, v);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.attend_state_information_attend:

                                    activity_attend_detail_textview_attend.setText("출석현황");

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            attendItems.clear();
                                            uidLists.clear();
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.child("attend_state").getValue().equals("출석")) {
                                                    Attend_Admin_Change_Item attendAdminChangeItem = snapshot.getValue(Attend_Admin_Change_Item.class);
                                                    String uidKey = snapshot.getKey();
                                                    attendItems.add(attendAdminChangeItem);
                                                    uidLists.add(uidKey);
                                                }
                                                attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                                            }

                                            if (attendItems.size() == 0) {
                                                Toast.makeText(AttendActivity_Detail_Information.this, "출석현황이 없습니다", Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });

                                    popup.dismiss();

                                    return true;

                                case R.id.attend_state_information_tardy:

                                    activity_attend_detail_textview_attend.setText("지각현황");

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            attendItems.clear();
                                            uidLists.clear();
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.child("attend_state").getValue().equals("지각")) {
                                                    Attend_Admin_Change_Item attendAdminChangeItem = snapshot.getValue(Attend_Admin_Change_Item.class);
                                                    String uidKey = snapshot.getKey();
                                                    attendItems.add(attendAdminChangeItem);
                                                    uidLists.add(uidKey);
                                                }
                                                attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                                            }

                                            if (attendItems.size() == 0) {
                                                Toast.makeText(AttendActivity_Detail_Information.this, "지각현황이 없습니다", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });

                                    popup.dismiss();

                                    return true;

                                case R.id.attend_state_information_absent:

                                    activity_attend_detail_textview_attend.setText("결석현황");

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            attendItems.clear();
                                            uidLists.clear();
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.child("attend_state").getValue().equals("결석")) {
                                                    Attend_Admin_Change_Item attendAdminChangeItem = snapshot.getValue(Attend_Admin_Change_Item.class);
                                                    String uidKey = snapshot.getKey();
                                                    attendItems.add(attendAdminChangeItem);
                                                    uidLists.add(uidKey);
                                                }
                                                attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                                            }

                                            if (attendItems.size() == 0) {
                                                Toast.makeText(AttendActivity_Detail_Information.this, "결석현황이 없습니다", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });

                                    popup.dismiss();

                                    return true;

                                default:
                                    return false;
                            }
                            //return false;
                        }
                    });

                    popup.inflate(R.menu.attend_state_information_popup);

                    popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                    popup.show();
                }
            });
        }

    }

    class AttendAdminInformationActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_admin_change_item_linearlayout;
            TextView activity_attend_admin_change_item_textview_name;
            TextView activity_attend_admin_change_item_textview_phone_number;
            TextView activity_attend_admin_change_item_textview_tardy_time;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_admin_change_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_admin_change_item_linearlayout);
                activity_attend_admin_change_item_textview_name = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_name);
                activity_attend_admin_change_item_textview_phone_number = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_phone_number);
                activity_attend_admin_change_item_textview_tardy_time = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_tardy_time);

            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_attend_admin_change_item, viewGroup, false);
            return new AttendActivity_Detail_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final AttendActivity_Detail_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((AttendActivity_Detail_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_admin_change_item_textview_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_change_item_textview_phone_number.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_change_item_textview_tardy_time.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_admin_change_item_textview_name.setText(attendItems.get(position).name);
            customViewHolder.activity_attend_admin_change_item_textview_phone_number.setText(attendItems.get(position).phone);
            customViewHolder.activity_attend_admin_change_item_textview_tardy_time.setText(attendItems.get(position).late_time);

            if (checkPage == 0) {
                customViewHolder.activity_attend_admin_change_item_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PopupMenu popup = new PopupMenu(AttendActivity_Detail_Information.this, v);

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {

                                    case R.id.attend_state_information_attend:

                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("attend_state").setValue("출석");
                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("late_time").removeValue();
                                        popup.dismiss();

                                        return true;

                                    case R.id.attend_state_information_tardy:

                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("attend_state").setValue("지각");
                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("late_time").removeValue();
                                        popup.dismiss();

                                        return true;

                                    case R.id.attend_state_information_absent:

                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("attend_state").setValue("결석");
                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("late_time").removeValue();
                                        popup.dismiss();

                                        return true;

                                    default:
                                        return false;
                                }
                                //return false;
                            }
                        });

                        popup.inflate(R.menu.attend_state_information_popup);

                        popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                        popup.show();
                    }

                });
            }

        }

        @Override
        public int getItemCount() {
            return attendItems.size();
        }

    }

    @Override
    public void onBackPressed() {
        if (menu_count > 0) {
            activity_attend_detail_slidingdrawer.animateClose();
        } else {
            finish();
        }
    }

}
