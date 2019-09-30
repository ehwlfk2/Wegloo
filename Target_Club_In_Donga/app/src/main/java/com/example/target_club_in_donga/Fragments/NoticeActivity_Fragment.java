package com.example.target_club_in_donga.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.Notice.ExpandableListAdapter;
import com.example.target_club_in_donga.Notice.NoticeActivity_Insert;
import com.example.target_club_in_donga.Notice.Notice_Item;
import com.example.target_club_in_donga.NoticeActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Execute;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.example.target_club_in_donga.Vote.VoteActivity_Result;
import com.example.target_club_in_donga.Vote.Vote_Item_Main;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

// 게시판 프래그먼트
/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeActivity_Fragment extends Fragment {


    public NoticeActivity_Fragment() {
        // Required empty public constructor
    }


    private FloatingActionButton activity_notice_main_button_intent;
    private RecyclerView activity_notice_main_recyclerview;
    public static List<ExpandableListAdapter.Item> data = new ArrayList<>();
    public static List<String> noticeDbKey = new ArrayList<>();
    private ExpandableListAdapter adapter;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_notice, container, false);

        activity_notice_main_button_intent = (FloatingActionButton)view.findViewById(R.id.activity_notice_main_button_intent);
        activity_notice_main_recyclerview = (RecyclerView)view.findViewById(R.id.activity_notice_main_recyclerview);

        activity_notice_main_button_intent.attachToRecyclerView(activity_notice_main_recyclerview);
        activity_notice_main_button_intent.show();

        activity_notice_main_button_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NoticeActivity_Insert.class);
                intent.putExtra("type","insert");
                startActivity(intent);
            }
        });

        activity_notice_main_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        activity_notice_main_recyclerview.setHasFixedSize(true);

        adapter = new ExpandableListAdapter(getActivity(),data);
        activity_notice_main_recyclerview.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        database.getReference().child(clubName).child("Notice").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                noticeDbKey.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Notice_Item notice_item = snapshot.getValue(Notice_Item.class);
                    notice_item.setTimestamp(-1*(long)notice_item.getTimestamp());
                    //CharacterStyle cs = (CharacterStyle) (notice_item.style.get(0));
                    //notice_item.setTitle();
                    /*CharacterStyle[] cs = new CharacterStyle[];
                    for(int i=0;i<notice_item.style.size();i++){
                        cs[i] = (CharacterStyle)notice_item.style.get(i);
                    }*/
                    SpannableStringBuilder ssb = new SpannableStringBuilder(notice_item.getTitle());
                    for(int i=0;i<notice_item.notice_item_colors.size();i++){
                        int start = notice_item.notice_item_colors.get(i).getStart();
                        int end = notice_item.notice_item_colors.get(i).getEnd();
                        try{
                            if(notice_item.notice_item_colors.get(i).getStyle().equals("BOLD")){
                                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, 1);
                            }
                            else if(notice_item.notice_item_colors.get(i).getStyle().equals("ITALIC")){
                                ssb.setSpan(new StyleSpan(Typeface.ITALIC), start, end, 1);
                            }
                            else if(notice_item.notice_item_colors.get(i).getStyle().equals("UnderLine")){
                                ssb.setSpan(new UnderlineSpan(), start, end, 1);
                            }
                            else if(Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.colorBlack){
                                ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), start, end, 1);
                            }
                            else if(Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_alizarin){
                                ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), start, end, 1);
                            }
                            else if(Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_belize_hole){
                                ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_belize_hole)), start, end, 1);
                            }
                            else{
                                int color = Integer.parseInt(notice_item.notice_item_colors.get(i).getStyle());
                                ssb.setSpan(new ForegroundColorSpan(color), start, end, 1);
                            }
                        }
                        catch (IllegalStateException e){ //무슨예외??

                        }
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String tt= timeStampToString(notice_item.getTimestamp(),simpleDateFormat);
                    ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER,ssb,null,
                            notice_item.getWriter(),tt);
                    places.invisibleChildren = new ArrayList<>();
                    places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,null,notice_item.getContent(),null,null));
                    data.add(places);
                    noticeDbKey.add(snapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "공지공지","이성현","2019-08-05");
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "이거슨 공지 공지공지\n존나공지공지공지\n아아아악 공지다 공지 상세정보\n공지닷닷","1","1"));
        //places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Tamil Nadu","1","1"));

        data.add(places);

        adapter.notifyDataSetChanged();*/



        //요기 구현

        return  view;
    } // activity_notice에 있는 화면을 가지고 온다

    private String timeStampToString(Object timestamp, SimpleDateFormat simpleDateFormat){
        long unixTime = (long) timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(date);
    }


}
