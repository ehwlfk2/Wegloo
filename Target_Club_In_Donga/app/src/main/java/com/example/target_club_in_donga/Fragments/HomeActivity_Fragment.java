package com.example.target_club_in_donga.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.Accountbook.AccountbookActivity_Main;
import com.example.target_club_in_donga.AttendActivity;
import com.example.target_club_in_donga.History.HistoryActivity_Main;
import com.example.target_club_in_donga.Material_Management.MaterialManagementActivity_Admin;
import com.example.target_club_in_donga.UserDetailActivity;
import com.example.target_club_in_donga.NoticeActivity;
import com.example.target_club_in_donga.Schedule.ScheduleActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;


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

    private ImageView menu_btn,setting_btn;
    private RelativeLayout main_btn_1, main_btn_2,main_btn_3, main_btn_6, main_btn_7;
    private SlidingDrawer slidingDrawer;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btn1 = (TextView) view.findViewById(R.id.frgment_home_favorite_1);
        btn4 = (TextView) view.findViewById(R.id.frgment_home_favorite_4);

        menu_detail_btn = (TextView) view.findViewById(R.id.menu_detail_btn);

        main_btn_1 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_1);
        main_btn_2 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_2);
        main_btn_3 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_3);
        main_btn_6 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_6);
        main_btn_7 = (RelativeLayout)view.findViewById(R.id.fragment_home_main_btn_7);

        menu_btn = (ImageView)view.findViewById(R.id.frgment_home_menu_btn);

        slidingDrawer = (SlidingDrawer)view.findViewById(R.id.frgment_home_slidingdrawer);
        slidingdrawer_title = (TextView)view.findViewById(R.id.frgment_home_slidingdrawer_title);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        }); // btn1 홈에서 공지사항인데, 클릭하면 홈에서 공지사항으로 activity가 바뀜

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
                Intent intent = new Intent(getActivity(), MaterialManagementActivity_Admin.class);
                startActivity(intent);
            }
        }); // main_btb2 메뉴에서 물품관리버튼인데, 클릭하면 메뉴에서 물품관리로 activity가 바뀜
        main_btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AccountbookActivity_Main.class);
                startActivity(intent);
            }
        });
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


        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(final View v) {
                slidingDrawer.animateOpen();
            }
        }); // menu_btn 홈에서 메뉴버튼인데, 메뉴버튼을 누르면 슬라이딩드로우로 아래에서 위로 메뉴가 나타남

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slidingdrawer_title.setVisibility(View.VISIBLE);
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slidingdrawer_title.setVisibility(View.INVISIBLE);
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

}
