package com.example.target_club_in_donga.club_foundation_join;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.target_club_in_donga.Package_LogIn.AppLoginData;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.UnaryOperator;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.thisClubIsRealName;

public class Accept_request extends AppCompatActivity {

    private RecyclerView accept_request_recy;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageView accept_request_back;
    private Accept_request_expandAdapter adapter;
    public static List<Accept_request_expandAdapter.Item> data = new ArrayList<>();
    public static List<String> requestKey = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);
        accept_request_back = findViewById(R.id.accept_request_back);
        accept_request_recy = findViewById(R.id.accept_request_recy);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        data.clear();
        requestKey.clear();

        accept_request_recy.setLayoutManager(new LinearLayoutManager(Accept_request.this, LinearLayoutManager.VERTICAL, false));
        accept_request_recy.setHasFixedSize(true);
        adapter = new Accept_request_expandAdapter(Accept_request.this,data);
        accept_request_recy.setAdapter(adapter);


        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("WantToJoinUser").orderByChild("applicationDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                requestKey.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    final JoinData joinData = snapshot.getValue(JoinData.class);
                    final String applicationDate = timeStampToString(joinData.getApplicationDate(),simpleDateFormat);

                    if(thisClubIsRealName){
                        firebaseDatabase.getReference("AppUser").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                AppLoginData appLoginData = dataSnapshot.getValue(AppLoginData.class);
                                Log.e("appLoginData",appLoginData.getName()+"");
                                Accept_request_expandAdapter.Item places = new Accept_request_expandAdapter.Item(Accept_request_expandAdapter.HEADER,
                                        dataSnapshot.getKey(),
                                        appLoginData.getName(),applicationDate,
                                        appLoginData.getRealNameProPicUrl(),null);
                                places.invisibleChildren = new ArrayList<>();
                                places.invisibleChildren.add(new Accept_request_expandAdapter.Item(Accept_request_expandAdapter.CHILD,null,null,null,null,joinData.getResume()));
                                data.add(places);
                                requestKey.add(dataSnapshot.getKey());
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        Accept_request_expandAdapter.Item places = new Accept_request_expandAdapter.Item(Accept_request_expandAdapter.HEADER,
                                snapshot.getKey(),
                                joinData.getName(),applicationDate,
                                joinData.getRealNameProPicUrl(),null);
                        places.invisibleChildren = new ArrayList<>();
                        places.invisibleChildren.add(new Accept_request_expandAdapter.Item(Accept_request_expandAdapter.CHILD,null,null,null,null,joinData.getResume()));
                        data.add(places);
                        requestKey.add(dataSnapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        accept_request_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private String timeStampToString(Object timestamp, SimpleDateFormat simpleDateFormat){
        long unixTime = -1*(long) timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(date);
    }
}
