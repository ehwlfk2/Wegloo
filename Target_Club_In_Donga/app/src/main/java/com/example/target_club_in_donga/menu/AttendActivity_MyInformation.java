package com.example.target_club_in_donga.menu;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.Attend.Attend_Information_Item;
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

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_my_information);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        activity_attend_my_information_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_my_information_recyclerview_main_list);
        activity_attend_my_information_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(AttendActivity_MyInformation.this));

        final AttendActivity_MyInformation.MyInformationActivity_AdminRecyclerViewAdapter MyInformationActivity_adminRecyclerViewAdapter = new AttendActivity_MyInformation.MyInformationActivity_AdminRecyclerViewAdapter();

        activity_attend_my_information_recyclerview_main_list.setAdapter(MyInformationActivity_adminRecyclerViewAdapter);
        MyInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

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

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_information_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_information_item_linearlayout);
                activity_attend_information_item_textview_date = (TextView) view.findViewById(R.id.activity_attend_information_item_textview_date);
                activity_attend_information_item_textview_attend_state = (TextView) view.findViewById(R.id.activity_attend_information_item_textview_attend_state);

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

        }

        @Override
        public int getItemCount() {
            return attendAdminItems.size();
        }

    }

}
