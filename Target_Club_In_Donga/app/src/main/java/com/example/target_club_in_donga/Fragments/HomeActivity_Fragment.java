package com.example.target_club_in_donga.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.Accountbook.AccountBookActivity_Main;
import com.example.target_club_in_donga.Attend.AttendActivity;
import com.example.target_club_in_donga.History.HistoryActivity_Main;
import com.example.target_club_in_donga.HomeActivity;
import com.example.target_club_in_donga.MainActivity;
import com.example.target_club_in_donga.Material_Management.MaterialManagementActivity;
import com.example.target_club_in_donga.MemberList.MemberList;
import com.example.target_club_in_donga.TimeLine.TimeLineActivity_Main;
import com.example.target_club_in_donga.UserDetailActivity;
import com.example.target_club_in_donga.NoticeActivity;
import com.example.target_club_in_donga.Schedule.ScheduleActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.example.target_club_in_donga.Board.*;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
// Home 프래그먼트

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeActivity_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeActivity_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private TextView btn1,btn2,btn3,btn4;
    private TextView menu_detail_btn;
    private TextView slidingdrawer_title;
    private ImageView menu_btn,setting_btn, timeline_btn;
    private RelativeLayout main_btn_1, main_btn_2,main_btn_3, main_btn_6, main_btn_7,main_btn_8 ,main_btn_12;
    private SlidingDrawer slidingDrawer;
    int menu_count = 0;
    private AdView mAdView;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String formatDate, nowtardyTimeLimit, getTardyTimeLimit;
    private long now;
    public HomeActivity_Fragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeActivity_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeActivity_Fragment newInstance(String param1, String param2) {
        HomeActivity_Fragment fragment = new HomeActivity_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        passPushTokenToServer();

        /*MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formatDate = simpleDateFormat.format(date);

        database.getReference().child("Attend_Admin").child(formatDate).child("Admin").child("Tardy_Time_Limit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                nowtardyTimeLimit = simpleDateFormat.format(date);

                getTardyTimeLimit = dataSnapshot.getValue().toString();
                Date d2 = simpleDateFormat.parse(nowtardyTimeLimit, new ParsePosition(0));
                Date d1 = simpleDateFormat.parse(getTardyTimeLimit, new ParsePosition(0));
                long diff = d1.getTime() - d2.getTime();
                if(diff < 0 ) {
                    database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").child(auth.getCurrentUser().getUid()).child("attend_statue").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue().toString().equals("미출결")) {
                                database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").child(auth.getCurrentUser().getUid()).child("attend_statue").setValue("결석");
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

        btn1 = (TextView) view.findViewById(R.id.frgment_home_favorite_1);
        btn2 = (TextView) view.findViewById(R.id.frgment_home_favorite_2);
        btn3 = (TextView)view.findViewById(R.id.frgment_home_favorite_3);
        btn4 = (TextView) view.findViewById(R.id.frgment_home_favorite_4);

        menu_detail_btn = (TextView) view.findViewById(R.id.menu_detail_btn);

        main_btn_1 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_1);
        main_btn_2 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_2);
        main_btn_3 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_3);
        main_btn_6 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_6);
        main_btn_7 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_7);
        main_btn_8 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_8);
        main_btn_12 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_12);

        menu_btn = (ImageView)view.findViewById(R.id.frgment_home_menu_btn);
        timeline_btn = (ImageView)view.findViewById(R.id.fragment_home_timeline_btn);

        /*memberlist = view.findViewById(R.id.fragment_home_btn_8);
        memberlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemberList.class);
                startActivity(intent);
            }
        });*/

        slidingDrawer = (SlidingDrawer)view.findViewById(R.id.frgment_home_slidingdrawer);
        slidingdrawer_title = (TextView)view.findViewById(R.id.frgment_home_slidingdrawer_title);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //sendFcm("cteKj3zZD6w:APA91bGpjC2QFFq1NCicPkvQz6ojKBbynQXAOdmKh-uX9zUlPSYf1MDucKopzNHZ_JOTpZidbqTd-nwZ3w7bxsB6a98Xns8hqRxIbXycaAOavSNy2fo0Z0QOIzgjoaJFBoWtuXcqCVFa");
                //위에 동운이꺼 밑에 내꺼
                //sendFcm("dWsU0qzEDso:APA91bEC2NqKj2ROYvRvexMVjHj0y6n0aNRQ_Zo0UqDjRRVGgTE2D7jYmasPOd0iGz_EBBUtU14tyPVxNXvT3wVxXqbTFnBSMnv2yhsikNOQQHi2TIGwN1oqLkUqgDz_BwZJvfNLQIte");
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        }); // btn1 홈에서 공지사항인데, 클릭하면 홈에서 공지사항으로 activity가 바뀜

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), Board_Main.class);
                startActivity(intent);
            }
        }); // btn2 게시판

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }); //btn3 앨범
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                startActivity(intent);
            }
        }); // btn4 홈에서 일정인데, 클릭하면 홈에서 일정으로 activity가 바뀜

        menu_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                startActivity(intent);
            }
        });

        main_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AttendActivity.class);
                startActivity(intent);
            }
        }); // main_btn1 메뉴에서 출석버튼인데, 클릭하면 메뉴에서 출석으로 activity가 바뀌

        main_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MaterialManagementActivity.class);
                startActivity(intent);
            }
        }); // main_btb2 메뉴에서 물품관리버튼인데, 클릭하면 메뉴에서 물품관리로 activity가 바뀜
        main_btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountBookActivity_Main.class);
                startActivity(intent);
            }
        }); //가계부
        main_btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity_Main.class);
                startActivity(intent);

            }
        }); //연혁

        main_btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VoteActivity_Main.class);
                startActivity(intent);
            }
        }); //투표

        main_btn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }); //로그아웃

        main_btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemberList.class);
                startActivity(intent);
            }
        }); //회원정보

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(final View v) {
                slidingDrawer.animateOpen();
                menu_count++;
                everyBtnEnable(false);
            }
        }); // menu_btn 홈에서 메뉴버튼인데, 메뉴버튼을 누르면 슬라이딩드로우로 아래에서 위로 메뉴가 나타남

        timeline_btn.setOnClickListener(new View.OnClickListener() { //홈에서 오른쪽 상단 종버튼 타임라인으로 만들꺼임
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getActivity(), TimeLineActivity_Main.class);
                startActivity(intent);
            }
        });

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slidingdrawer_title.setVisibility(View.VISIBLE);
                everyBtnEnable(false);

                view.setFocusableInTouchMode(true);
                view.requestFocus();
                view.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(final View view, final int i, final KeyEvent keyEvent) {
                        if(i == KeyEvent.KEYCODE_BACK) {
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("finishstatus", true);
                            slidingDrawer.animateClose();
                            menu_count--;
                            everyBtnEnable(true);
//                    getActivity().finish();
                            startActivity(intent);
                            return  true;
                        } else {
                            return false;
                        }
                    }
                });

            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slidingdrawer_title.setVisibility(View.INVISIBLE);
                everyBtnEnable(true);
            }
        });

        // 홈에서 메뉴(슬라이딩드로우)를 열었을 경우에만 뒤로가기 버튼을 누르면 슬라이딩드로우가 닫힘
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(final View view, final int i, final KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_BACK && menu_count > 0) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("finishstatus", true);
                    slidingDrawer.animateClose();
                    menu_count--;
                    everyBtnEnable(true);
//                    getActivity().finish();
                    startActivity(intent);
                    return  true;
                } else {
                    return false;
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken",token);
        FirebaseDatabase.getInstance().getReference().child("User").child(uid).updateChildren(map);
    }
    public void everyBtnEnable(boolean boo) {
        btn1.setEnabled(boo);
        btn2.setEnabled(boo);
        btn3.setEnabled(boo);
        btn4.setEnabled(boo);
        mAdView.setEnabled(boo);
        menu_btn.setEnabled(boo);
        timeline_btn.setEnabled(boo);
    }
}
