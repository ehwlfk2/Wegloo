package com.example.target_club_in_donga.home_viewpager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.Attend.AttendActivity;
import com.example.target_club_in_donga.Attend.AttendActivity_Admin_Home;
import com.example.target_club_in_donga.Board.Board_Main;
import com.example.target_club_in_donga.History.HistoryActivity_Main;
import com.example.target_club_in_donga.Material_Rental.MaterialRentalActivity_Home;
import com.example.target_club_in_donga.MemberList.MemberList;
import com.example.target_club_in_donga.club_foundation_join.Accept_request;
import com.example.target_club_in_donga.menu.AttendActivity_MyInformation;
import com.example.target_club_in_donga.menu.MyInformation;
import com.example.target_club_in_donga.Notice.NoticeActivity_Main;
import com.example.target_club_in_donga.Notice.Notice_Item;
import com.example.target_club_in_donga.Package_LogIn.AppLoginData;
import com.example.target_club_in_donga.Package_LogIn.LoginActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.example.target_club_in_donga.club_foundation_join.ClubData;
import com.example.target_club_in_donga.club_foundation_join.JoinData;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeActivityView.viewAdapter;

public class HomeFragment0 extends Fragment implements View.OnClickListener {

    private FrameLayout voteIntentBtn, attendIntentBtn, scheduleIntentBtn, boardIntentBtn;
    private ConstraintLayout noticeIntentBtn;

    private TextView home_textview_main;
    private ImageButton menu_opener;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    public static DrawerLayout drawerLayout;
    private View drawer_menu_view;

    private ImageButton home_button_timeline;
    public static boolean menuToggle = false;
    public static boolean thisClubIsRealName;
    public static boolean thisClubIsFreeSign;
    public static String thisClubName;
    public static String userRealName;
    public static String userNicName;
    public static String userProfileUrl;
    public static int userAdmin;
    private TextView home_notice_title1, home_notice_title2, home_notice_writer1, home_notice_writer2,home_notice_date1, home_notice_date2 ;

    //메뉴 아이템
    private TextView Group_Name, profile_username, profile_admin;
    private ImageView profile_thumbnail;
    private ImageButton logout_btn;
    private LinearLayout user_infomation,go_Manage_Attend, go_AttendInfo, go_Material_Rental, go_Manage_Material_Rent;
    private LinearLayout go_Member_List,go_Gallery, go_History, go_Group_Info, go_Withdraw, go_Wegloo_Info;
    private LinearLayout go_Manage_Accept_Request, go_Manage_Group, manage_layout1, manage_layout2, go_clubDelete;
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

        passPushTokenToServer();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
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
        //go_board = view.findViewById(R.id.go_Board);
        home_notice_title1 = view.findViewById(R.id.home_notice_title1);
        home_notice_title2 = view.findViewById(R.id.home_notice_title2);
        home_notice_writer1 = view.findViewById(R.id.home_notice_writer1);
        home_notice_writer2 = view.findViewById(R.id.home_notice_writer2);
        home_notice_date1 = view.findViewById(R.id.home_notice_date1);
        home_notice_date2 = view.findViewById(R.id.home_notice_date2);


        //메뉴 아이템
        Group_Name = view.findViewById(R.id.Group_Name);
        profile_username = view.findViewById(R.id.profile_username);
        profile_admin = view.findViewById(R.id.profile_admin);
        profile_thumbnail = view.findViewById(R.id.profile_thumbnail);
        logout_btn = view.findViewById(R.id.logout_btn);

        go_AttendInfo = view.findViewById(R.id.go_AttendInfo);
        go_Material_Rental = view.findViewById(R.id.go_Material_Rental);
        go_Manage_Attend = view.findViewById(R.id.go_Manage_Attend);
        go_Manage_Material_Rent = view.findViewById(R.id.go_Manage_Material_Rent);
        go_Member_List = view.findViewById(R.id.go_Member_List);
        go_Gallery = view.findViewById(R.id.go_Gallery);
        go_History = view.findViewById(R.id.go_History);
        go_Group_Info = view.findViewById(R.id.go_Group_Info);
        go_Withdraw = view.findViewById(R.id.go_Withdraw);
        go_Wegloo_Info = view.findViewById(R.id.go_Wegloo_Info);
        go_Manage_Accept_Request = view.findViewById(R.id.go_Manage_Accept_Request);
        go_Manage_Group = view.findViewById(R.id.go_Manage_Group);
        manage_layout1 = view.findViewById(R.id.manage_layout1);
        manage_layout2 = view.findViewById(R.id.manage_layout2);
        go_clubDelete = view.findViewById(R.id.go_clubDelete);

        firebaseDatabase.getReference().child("EveryClub").child(clubName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClubData clubData = dataSnapshot.getValue(ClubData.class);
                home_textview_main.setText(clubData.getThisClubName());
                Group_Name.setText(clubData.getThisClubName());
                thisClubName = clubData.getThisClubName();
                thisClubIsRealName = clubData.isRealNameSystem();
                thisClubIsFreeSign = clubData.isFreeSign();
                clubName = dataSnapshot.getKey();
                //Log.e("thisClubIsRealName",thisClubIsRealName+"");

                String userUid = firebaseAuth.getCurrentUser().getUid();
                menuDB(userUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                intent.putExtra("thisClubIsRealName",thisClubIsRealName);
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

        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("Notice").orderByChild("timestamp").limitToFirst(2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean noticeFlag = false;
                home_notice_title1.setText("");
                home_notice_date1.setText("");
                home_notice_title2.setText("");
                home_notice_date2.setText("");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Notice_Item notice_item = snapshot.getValue(Notice_Item.class);
                    notice_item.setTimestamp(-1*(long)notice_item.getTimestamp());
                    SpannableStringBuilder ssb = new SpannableStringBuilder(notice_item.getTitle());
                    try{
                        for(int i=0;i<notice_item.notice_item_colors.size();i++){
                            int start = notice_item.notice_item_colors.get(i).getStart();
                            int end = notice_item.notice_item_colors.get(i).getEnd();

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
                    }
                    catch (IllegalStateException e){ //무슨예외??
                        ssb.clear();
                        ssb = new SpannableStringBuilder(notice_item.getTitle());
                    }
                    catch (IndexOutOfBoundsException e){
                        ssb.clear();
                        ssb = new SpannableStringBuilder(notice_item.getTitle());
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String tt = timeStampToString(notice_item.getTimestamp(),simpleDateFormat);
                    if(!noticeFlag){
                        home_notice_title1.setText(ssb);
                        home_notice_writer1.setText(notice_item.getWriter());
                        home_notice_date1.setText(tt);
                        noticeFlag = true;
                    }
                    else{
                        home_notice_title2.setText(ssb);
                        home_notice_writer2.setText(notice_item.getWriter());
                        home_notice_date2.setText(tt);
                        //noticeFlag = false;
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
        logout_btn.setOnClickListener(this);

        go_Manage_Attend.setOnClickListener(this);
        go_AttendInfo.setOnClickListener(this);
        go_Material_Rental.setOnClickListener(this);
        go_Manage_Material_Rent.setOnClickListener(this);
        go_Member_List.setOnClickListener(this);
        go_Gallery.setOnClickListener(this);
        go_History.setOnClickListener(this);
        go_Group_Info.setOnClickListener(this);
        go_Withdraw.setOnClickListener(this);
        go_Wegloo_Info.setOnClickListener(this);
        go_Manage_Accept_Request.setOnClickListener(this);
        go_Manage_Group.setOnClickListener(this);
        go_clubDelete.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_frame_vote: //홈화면 투표
                Intent intent = new Intent(getActivity(), VoteActivity_Main.class);
                startActivity(intent);
                break;
            case R.id.home_frame_attendance: //홈화면 출석
                Intent intent4 = new Intent(getActivity(), AttendActivity.class);
                startActivity(intent4);
                break;
            case R.id.home_frame_calender: //홈화면 일정




                break;
            case R.id.home_frame_board: //홈화면 자유게시판
                Intent board_intent = new Intent(getActivity(), Board_Main.class);
                startActivity(board_intent);
                break;
            case R.id.home_layout_notice: //홈화면 공지사항
                Intent intent1 = new Intent(getActivity(), NoticeActivity_Main.class);
                startActivity(intent1);
                break;
            case  R.id.home_button_timeline: //홈화면 타임라인(종모양)
                viewAdapter.addItem(new TimeLineFragment0());
                viewAdapter.notifyDataSetChanged();
                viewAdapter.functionCurrent();
                break;
            //여기부터 메뉴
            case R.id.logout_btn: //메뉴 로그아웃
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                    }
                });
                Intent intent3 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent3);
                getActivity().finish();
                break;
            case R.id.go_Manage_Attend: //메뉴 출석관리
                Intent intent2 = new Intent(getActivity(), AttendActivity_Admin_Home.class);
                startActivity(intent2);
                break;
            case R.id.go_AttendInfo: //메뉴 출석정보
                Intent intent5 = new Intent(getActivity(), AttendActivity_MyInformation.class);
                startActivity(intent5);
                break;
            case R.id.go_Material_Rental: //메뉴 물품대여
                Intent intent6 = new Intent(getActivity(), MaterialRentalActivity_Home.class);
                intent6.putExtra("differFlag", 0);
                startActivity(intent6);
                break;
            case R.id.go_Manage_Material_Rent: //메뉴 물품대여관리?
                Intent intent7 = new Intent(getActivity(), MaterialRentalActivity_Home.class);
                intent7.putExtra("differFlag", 1);
                startActivity(intent7);
                break;
            case R.id.go_Member_List: //메뉴 회원목록
                Intent intent8 = new Intent(getActivity(), MemberList.class);
                startActivity(intent8);
                break;
            case R.id.go_Gallery: //메뉴 모임앨범
                Toast.makeText(getActivity(), "구현중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.go_History: //메뉴 모임연혁
                Intent intent9 = new Intent(getActivity(), HistoryActivity_Main.class);
                startActivity(intent9);
                break;
            case R.id.go_Group_Info: //메뉴 모임정보
                Toast.makeText(getActivity(), "구현중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.go_Withdraw: //메뉴 모임탈퇴
                Toast.makeText(getActivity(), "구현중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.go_Wegloo_Info: //메뉴 Wegloo정보
                Toast.makeText(getActivity(), "구현중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.go_Manage_Accept_Request: //메뉴 가입신청
                Intent intent10 = new Intent(getActivity(), Accept_request.class);
                startActivity(intent10);
                break;
            case R.id.go_Manage_Group: //메뉴 모임관리
                Toast.makeText(getActivity(), "구현중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.go_clubDelete: //메뉴 모임삭제
                Toast.makeText(getActivity(), "구현중인 기능입니다.", Toast.LENGTH_SHORT).show();
                break;

        }
    }
    DrawerLayout.DrawerListener listner = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
// 열릴 때
            menuToggle = true;
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
// 오픈이 완료됐을때
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
// 닫혔을때
            menuToggle = false;
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

    private void menuDB(String userUid){
        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JoinData joinData = dataSnapshot.getValue(JoinData.class);
                userNicName = joinData.getName();
                userProfileUrl = joinData.getRealNameProPicUrl();
                profile_username.setText(joinData.getName());
                if(getActivity() != null){
                    if(!joinData.getRealNameProPicUrl().equals("None")){
                        Glide.with(getActivity()).load(joinData.getRealNameProPicUrl()).into(profile_thumbnail);
                    }
                }
                adminStr(joinData);
                //Log.e("myResume",myResume);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void adminStr(JoinData joinData){
        userAdmin = joinData.getAdmin();
        if(joinData.getAdmin() == 0){
            profile_admin.setText("회장");
            if(thisClubIsFreeSign){
                go_Manage_Accept_Request.setVisibility(View.GONE);
            }
        }
        else if(joinData.getAdmin() == 1){
            profile_admin.setText("부회장");
            if(thisClubIsFreeSign){
                go_Manage_Accept_Request.setVisibility(View.GONE);
            }
        }
        else if(joinData.getAdmin() == 2){
            profile_admin.setText("임원");
            manage_layout1.setVisibility(View.GONE);
            manage_layout2.setVisibility(View.GONE);
        }
        else if(joinData.getAdmin() == 3){
            profile_admin.setText("회원");
            manage_layout1.setVisibility(View.GONE);
            manage_layout2.setVisibility(View.GONE);
        }

    }
    public void passPushTokenToServer() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);
        FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("User").child(uid).updateChildren(map);
    }
}