package com.example.target_club_in_donga.Notice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.thisClubIsRealName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userAdmin;

public class NoticeActivity_Main extends AppCompatActivity {

    private FloatingActionButton activity_notice_main_button_intent;
    private RecyclerView activity_notice_main_recyclerview;
    public static List<ExpandableListAdapter.Item> noticeData = new ArrayList<>();
    public static List<String> noticeDbKey = new ArrayList<>();
    private ExpandableListAdapter adapter;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        activity_notice_main_button_intent = findViewById(R.id.activity_notice_main_button_intent);
        activity_notice_main_recyclerview = findViewById(R.id.activity_notice_main_recyclerview);

        activity_notice_main_button_intent.attachToRecyclerView(activity_notice_main_recyclerview);
        activity_notice_main_button_intent.show();

        if(userAdmin >= 2){
            activity_notice_main_button_intent.setVisibility(View.GONE);
        }

        activity_notice_main_button_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeActivity_Main.this, NoticeActivity_Insert.class);
                intent.putExtra("type","insert");
                startActivity(intent);
                finish();
            }
        });
        noticeData.clear();
        noticeDbKey.clear();
        activity_notice_main_recyclerview.setLayoutManager(new LinearLayoutManager(NoticeActivity_Main.this, LinearLayoutManager.VERTICAL, false));
        activity_notice_main_recyclerview.setHasFixedSize(true);

        adapter = new ExpandableListAdapter(NoticeActivity_Main.this,noticeData);
        activity_notice_main_recyclerview.setAdapter(adapter);


        database = FirebaseDatabase.getInstance();
        database.getReference().child("EveryClub").child(clubName).child("Notice").orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noticeData.clear();
                noticeDbKey.clear();
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    final Notice_Item notice_item = snapshot.getValue(Notice_Item.class);

                    if(!thisClubIsRealName){ //닉네임일경우
                        database.getReference().child("EveryClub").child(clubName).child("User").child(notice_item.getWriter()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String nicName = dataSnapshot.getValue(String.class);
                                //Log.e("nic",nicName);
                                SpannableStringBuilder ssb = new SpannableStringBuilder(notice_item.getTitle());
                                try{
                                    for(int i=0;i<notice_item.notice_item_colors.size();i++) {
                                        int start = notice_item.notice_item_colors.get(i).getStart();
                                        int end = notice_item.notice_item_colors.get(i).getEnd();
                                        if (notice_item.notice_item_colors.get(i).getStyle().equals("BOLD")) {
                                            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, 1);
                                        } else if (notice_item.notice_item_colors.get(i).getStyle().equals("ITALIC")) {
                                            ssb.setSpan(new StyleSpan(Typeface.ITALIC), start, end, 1);
                                        } else if (notice_item.notice_item_colors.get(i).getStyle().equals("UnderLine")) {
                                            ssb.setSpan(new UnderlineSpan(), start, end, 1);
                                        } else if (Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.colorBlack) {
                                            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), start, end, 1);
                                        } else if (Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_alizarin) {
                                            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), start, end, 1);
                                        } else if (Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_belize_hole) {
                                            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_belize_hole)), start, end, 1);
                                        } else {
                                            int color = Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle());
                                            ssb.setSpan(new ForegroundColorSpan(color), start, end, 1);
                                        }
                                    }
                                }
                                catch (IllegalStateException e){ //무슨예외??
                                    ssb.clear();
                                    ssb = new SpannableStringBuilder(notice_item.getTitle());
                                }
                                catch(IndexOutOfBoundsException e){
                                    ssb.clear();
                                    ssb = new SpannableStringBuilder(notice_item.getTitle());
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String tt= timeStampToString(notice_item.getTimestamp(),simpleDateFormat);
                                ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, ssb,null,
                                        nicName,tt);
                                places.invisibleChildren = new ArrayList<>();
                                places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,null,notice_item.getContent(),null,null));
                                noticeData.add(places);
                                noticeDbKey.add(snapshot.getKey());
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        SpannableStringBuilder ssb = new SpannableStringBuilder(notice_item.getTitle());
                        try{
                            for(int i=0;i<notice_item.notice_item_colors.size();i++) {
                                int start = notice_item.notice_item_colors.get(i).getStart();
                                int end = notice_item.notice_item_colors.get(i).getEnd();
                                if (notice_item.notice_item_colors.get(i).getStyle().equals("BOLD")) {
                                    ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, 1);
                                } else if (notice_item.notice_item_colors.get(i).getStyle().equals("ITALIC")) {
                                    ssb.setSpan(new StyleSpan(Typeface.ITALIC), start, end, 1);
                                } else if (notice_item.notice_item_colors.get(i).getStyle().equals("UnderLine")) {
                                    ssb.setSpan(new UnderlineSpan(), start, end, 1);
                                } else if (Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.colorBlack) {
                                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), start, end, 1);
                                } else if (Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_alizarin) {
                                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), start, end, 1);
                                } else if (Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_belize_hole) {
                                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_belize_hole)), start, end, 1);
                                } else {
                                    int color = Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle());
                                    ssb.setSpan(new ForegroundColorSpan(color), start, end, 1);
                                }
                            }
                        }
                        catch (IllegalStateException e){ //무슨예외??
                            ssb.clear();
                            ssb = new SpannableStringBuilder(notice_item.getTitle());
                        }
                        catch(IndexOutOfBoundsException e){
                            ssb.clear();
                            ssb = new SpannableStringBuilder(notice_item.getTitle());
                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String tt= timeStampToString(notice_item.getTimestamp(),simpleDateFormat);
                        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER,ssb,null,
                                notice_item.getWriter(),tt);
                        places.invisibleChildren = new ArrayList<>();
                        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,null,notice_item.getContent(),null,null));
                        noticeData.add(places);
                        noticeDbKey.add(snapshot.getKey());
                        //adapter.notifyDataSetChanged();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private String timeStampToString(Object timestamp, SimpleDateFormat simpleDateFormat){
        long unixTime = -1*(long) timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(date);
    }

    @Override
    public void onBackPressed() {
        if(isTaskRoot()){
            Intent intent = new Intent(NoticeActivity_Main.this, HomeActivityView.class);
            intent.putExtra("isRecent",true);
            startActivity(intent);
            finish();
            //// This is last activity
        }
        super.onBackPressed();
    }
}
