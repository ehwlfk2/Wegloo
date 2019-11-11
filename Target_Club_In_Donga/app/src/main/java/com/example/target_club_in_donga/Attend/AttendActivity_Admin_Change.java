package com.example.target_club_in_donga.Attend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_Admin_Change extends AppCompatActivity {

    private  RecyclerView activity_attend_admin_change_home_recyclerview_main_list;
    List<Attend_Admin_Change_Item> attendAdminItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private FirebaseDatabase database;

    private String findkey;
    private int flag;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_admin_change_home);

        Intent intent = getIntent();

        findkey = intent.getExtras().getString("findKey");

        database = FirebaseDatabase.getInstance();

        activity_attend_admin_change_home_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_admin_change_home_recyclerview_main_list);
        activity_attend_admin_change_home_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final AttendAdminChangeActivity_AdminRecyclerViewAdapter attendAdminChangeActivity_adminRecyclerViewAdapter = new AttendAdminChangeActivity_AdminRecyclerViewAdapter();

        activity_attend_admin_change_home_recyclerview_main_list.setAdapter(attendAdminChangeActivity_adminRecyclerViewAdapter);
        attendAdminChangeActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attendAdminItems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attend_Admin_Change_Item attendAdminChangeItem = snapshot.getValue(Attend_Admin_Change_Item.class);
                    String uidKey = snapshot.getKey();
                    attendAdminItems.add(attendAdminChangeItem);
                    uidLists.add(uidKey);
                }
                attendAdminChangeActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

    }

//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>


    // AttendAdminChangeActivity 어댑터

    class AttendAdminChangeActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_admin_change_item_linearlayout;
            TextView activity_attend_admin_change_item_textview_name;
            TextView activity_attend_admin_change_item_textview_attend_state;
            TextView activity_attend_admin_change_item_textview_phone_number;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_admin_change_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_admin_change_item_linearlayout);
                activity_attend_admin_change_item_textview_name = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_name);
                activity_attend_admin_change_item_textview_attend_state = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_attend_state);
                activity_attend_admin_change_item_textview_phone_number = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_phone_number);

            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_attend_admin_change_item, viewGroup, false);

            return new AttendActivity_Admin_Change.AttendAdminChangeActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final AttendActivity_Admin_Change.AttendAdminChangeActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((AttendActivity_Admin_Change.AttendAdminChangeActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_admin_change_item_textview_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_change_item_textview_attend_state.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_change_item_textview_phone_number.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_admin_change_item_textview_name.setText(attendAdminItems.get(position).name);
            customViewHolder.activity_attend_admin_change_item_textview_attend_state.setText(attendAdminItems.get(position).attend_state);
            customViewHolder.activity_attend_admin_change_item_textview_phone_number.setText(attendAdminItems.get(position).phone);

            customViewHolder.activity_attend_admin_change_item_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendActivity_Admin_Change.this);

                    View view = LayoutInflater.from(AttendActivity_Admin_Change.this)
                            .inflate(R.layout.activity_attend_admin_change, null, false);
                    builder.setView(view);

                    final RadioGroup activity_attend_admin_change_radiogroup = (RadioGroup) view.findViewById(R.id.activity_attend_admin_change_radiogroup);
                    final Button activity_attend_admin_change_button_attendance_change = (Button) view.findViewById(R.id.activity_attend_admin_change_button_attendance_change);

                    final AlertDialog dialog = builder.create();

                    activity_attend_admin_change_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(final RadioGroup group, final int checkedId) {
                            if(checkedId == R.id.activity_attend_admin_change_attend) {
                                flag = 0;
                                // 출석
                            } else if(checkedId == R.id.activity_attend_admin_change_tardy)  {
                                flag = 1;
                                // 지각
                            } else if(checkedId == R.id.activity_attend_admin_change_absent) {
                                flag = 2;
                                // 결석
                            }
                        }
                    });

                    activity_attend_admin_change_button_attendance_change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            if(flag == 0) {
                                database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("attend_state").setValue("출석");
                                // 출석으로 변경
                            } else if(flag == 1) {
                                database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("attend_state").setValue("지각");
                                // 지각으로 변경
                            } else {
                                database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(uidLists.get(position)).child("attend_state").setValue("결석");
                                // 결석으로 변경
                            }
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return attendAdminItems.size();
        }

    }

}