package com.example.target_club_in_donga.home_viewpager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.target_club_in_donga.Board.Board_Main;
import com.example.target_club_in_donga.MyInformation;
import com.example.target_club_in_donga.Notice.NoticeActivity_Main;
import com.example.target_club_in_donga.Notice.Notice_Item;
import com.example.target_club_in_donga.Package_LogIn.LoginActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeActivityView.viewAdapter;

public class HomeFragment0 extends Fragment implements View.OnClickListener {

    private FrameLayout voteIntentBtn, attendIntentBtn, scheduleIntentBtn, boardIntentBtn;
    private ConstraintLayout noticeIntentBtn;

    private TextView home_textview_main;
    private ImageButton menu_opener;
    private FirebaseDatabase firebaseDatabase;
    private DrawerLayout drawerLayout;
    private View drawer_menu_view;
    private LinearLayout user_infomation, go_board;
    private ImageButton home_button_timeline;

    private TextView home_notice_title1, home_notice_title2, home_notice_writer1, home_notice_writer2,home_notice_date1, home_notice_date2 ;
    /**
     * 홈 화면
     */
    public HomeFragment0() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home0, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        voteIntentBtn = view.findViewById(R.id.home_frame_vote);
        attendIntentBtn = view.findViewById(R.id.home_frame_attendance);
        scheduleIntentBtn = view.findViewById(R.id.home_frame_calender);
        boardIntentBtn = view.findViewById(R.id.home_frame_board);
        noticeIntentBtn = view.findViewById(R.id.home_layout_notice);
        home_textview_main = view.findViewById(R.id.home_textview_main);
        home_button_timeline = view.findViewById(R.id.home_button_timeline);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        drawer_menu_view = view.findViewById(R.id.menu_drawer_ver);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        menu_opener = view.findViewById(R.id.home_button_menu);
        user_infomation = view.findViewById(R.id.profile_myinfomation);
        go_board = view.findViewById(R.id.go_Board);
        home_notice_title1 = view.findViewById(R.id.home_notice_title1);
        home_notice_title2 = view.findViewById(R.id.home_notice_title2);
        home_notice_writer1 = view.findViewById(R.id.home_notice_writer1);
        home_notice_writer2 = view.findViewById(R.id.home_notice_writer2);
        home_notice_date1 = view.findViewById(R.id.home_notice_date1);
        home_notice_date2 = view.findViewById(R.id.home_notice_date2);

        menu_opener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawer_menu_view);
            }
        });
        user_infomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyInformation.class);
                startActivity(intent);
            }
        });
        go_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Board_Main.class);
                startActivity(intent);
            }
        });
        drawerLayout.setDrawerListener(listner);
        drawer_menu_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("thisClubName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);
                home_textview_main.setText(title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("Notice").orderByChild("timestamp").limitToFirst(2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = true;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Notice_Item notice_item = snapshot.getValue(Notice_Item.class);
                    notice_item.setTimestamp(-1*(long)notice_item.getTimestamp());

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
                    String tt = timeStampToString(notice_item.getTimestamp(),simpleDateFormat);
                    if(flag){
                        home_notice_title1.setText(ssb);
                        home_notice_writer1.setText(notice_item.getWriter());
                        home_notice_date1.setText(tt);
                        flag = false;
                    }
                    else{
                        home_notice_title2.setText(ssb);
                        home_notice_writer2.setText(notice_item.getWriter());
                        home_notice_date2.setText(tt);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        voteIntentBtn.setOnClickListener(this);
        attendIntentBtn.setOnClickListener(this);
        scheduleIntentBtn.setOnClickListener(this);
        boardIntentBtn.setOnClickListener(this);
        noticeIntentBtn.setOnClickListener(this);
        home_button_timeline.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        /**
         * 알아서들 기능별로 intent 시키시길
         */
        switch (view.getId()){
            case R.id.home_frame_vote:
                Intent intent = new Intent(getActivity(), VoteActivity_Main.class);
                startActivity(intent);
                break;
            case R.id.home_frame_attendance:

                break;
            case R.id.home_frame_calender:

                break;
            case R.id.home_frame_board:

                break;
            case R.id.home_layout_notice:
                //Toast.makeText(getContext(), "공지공지", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getActivity(), NoticeActivity_Main.class);
                startActivity(intent1);
                break;
            case  R.id.home_button_timeline:
                viewAdapter.addItem(new TimeLineFragment0());
                viewAdapter.notifyDataSetChanged();
                viewAdapter.functionCurrent();
                break;
        }
    }
    DrawerLayout.DrawerListener listner = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
// 열릴 때
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
// 오픈이 완료됐을때
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
// 닫혔을때
        }

        @Override
        public void onDrawerStateChanged(int newState) {
// 상태 체인지됐을때
        }
    };

    private String timeStampToString(Object timestamp, SimpleDateFormat simpleDateFormat){
        long unixTime = (long) timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(date);
    }

}
