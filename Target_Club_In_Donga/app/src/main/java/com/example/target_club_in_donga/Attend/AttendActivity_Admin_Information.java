package com.example.target_club_in_donga.Attend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_Admin_Information extends AppCompatActivity {
    private RecyclerView activity_attend_admin_information_home_recyclerview_main_list;
    List<Attend_Admin_Information_Item> attendAdminItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private ArrayList<String> listStartTime = new ArrayList<>();

    private FirebaseDatabase database;

    private String startTime;
    private int listSize = 0;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_admin_information_home);

        database = FirebaseDatabase.getInstance();

        activity_attend_admin_information_home_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_admin_information_home_recyclerview_main_list);
        activity_attend_admin_information_home_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final Button attend_admin_information_home_button_search = (Button) findViewById(R.id.attend_admin_information_home_button_search);

        final AttendActivity_Admin_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter attendAdminInformationActivity_adminRecyclerViewAdapter = new AttendActivity_Admin_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter();

        activity_attend_admin_information_home_recyclerview_main_list.setAdapter(attendAdminInformationActivity_adminRecyclerViewAdapter);
        attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        final EditText attend_admin_information_home_edittext_search = (EditText) findViewById(R.id.attend_admin_information_home_edittext_search);
        attend_admin_information_home_edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                String text = attend_admin_information_home_edittext_search.getText().toString();
                search(text);
            }

            private void search(final String charText) {

                attend_admin_information_home_button_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        database.getReference().child("EveryClub").child(clubName).child("Attend").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                attendAdminItems.clear();
                                uidLists.clear();
                                listSize = 0;
                                for (final DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.child("name").getValue(String.class).toLowerCase().contains(charText) && charText.length() != 0) {
                                                    startTime = snapshot2.child("startTime").getValue().toString();
                                                    listStartTime.add(startTime);
                                                    Attend_Admin_Information_Item attendAdminInformationItem = snapshot.getValue(Attend_Admin_Information_Item.class);
                                                    String uidKey = snapshot.getKey();
                                                    attendAdminItems.add(0, attendAdminInformationItem);
//                                                    attendAdminItems.get(i).attendTimeLimit = startTime;
                                                    uidLists.add(0, uidKey);
                                                    listSize++;
                                                }
                                            }

                                            Log.e("입력", listSize + "");

                                            for (int i = 0; i < listSize; i++) {
                                                attendAdminItems.get(i).attendTimeLimit = listStartTime.get(listSize - 1 - i);
                                            }

                                            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                                            attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
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
                });

            }
        });
    }

//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>

    // AttendAdminInformationActivity 어댑터

    class AttendAdminInformationActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_admin_information_item_linearlayout;
            TextView activity_attend_admin_information_item_textview_name;
            TextView activity_attend_admin_information_item_textview_attend_state;
            TextView activity_attend_admin_information_item_textview_phone_number;
            TextView activity_attend_admin_information_item_textview_date;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_admin_information_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_admin_information_item_linearlayout);
                activity_attend_admin_information_item_textview_name = (TextView) view.findViewById(R.id.activity_attend_admin_information_item_textview_name);
                activity_attend_admin_information_item_textview_attend_state = (TextView) view.findViewById(R.id.activity_attend_admin_information_item_textview_attend_state);
                activity_attend_admin_information_item_textview_date = (TextView) view.findViewById(R.id.activity_attend_admin_information_item_textview_date);
                activity_attend_admin_information_item_textview_phone_number = (TextView) view.findViewById(R.id.activity_attend_admin_information_item_textview_phone_number);

            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_attend_admin_information_item, viewGroup, false);

            return new AttendActivity_Admin_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final AttendActivity_Admin_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((AttendActivity_Admin_Information.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_admin_information_item_textview_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_information_item_textview_attend_state.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_information_item_textview_phone_number.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_admin_information_item_textview_name.setText(attendAdminItems.get(position).name);
            customViewHolder.activity_attend_admin_information_item_textview_attend_state.setText(attendAdminItems.get(position).attend_state);
            customViewHolder.activity_attend_admin_information_item_textview_date.setText(attendAdminItems.get(position).attendTimeLimit);
            customViewHolder.activity_attend_admin_information_item_textview_phone_number.setText(attendAdminItems.get(position).phone);

        }

        @Override
        public int getItemCount() {
            return attendAdminItems.size();
        }

    }
}
