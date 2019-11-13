package com.example.target_club_in_donga.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.Attend.Attend_Information_Item;
import com.example.target_club_in_donga.HomeActivity;
import com.example.target_club_in_donga.Package_LogIn.LoginData;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//import static com.example.target_club_in_donga.MainActivity.clubName;

// 게시판 프래그먼트

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailActivity_Fragment extends Fragment {

    public UserDetailActivity_Fragment() {
        // Required empty public constructor
    }

    private RecyclerView activity_user_detail_recyclerview_main_list;
    List<Attend_Information_Item> attendAdminItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private ArrayList<String> listStartTime = new ArrayList<>();

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private TextView name, phone, school, email, studentID, circles;
    private ImageView profile;
    private Button changeButton;
    private SlidingDrawer slidingDrawer;
    private TextView slidingdrawer_title;
    private int menu_count = 0, listSize = 0;
    private String startTime;

    private String clubName = "TCID";
    // 임시로 넣어준 값

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_user_detail, container, false);
        name = view.findViewById(R.id.activity_user_detail_name);
        phone = view.findViewById(R.id.activity_user_detail_phonenumber);
        //school = view.findViewById(R.id.user_detail_school);
        email = view.findViewById(R.id.activity_user_detail_email);
        profile = view.findViewById(R.id.activity_user_detail_profile);
        circles = view.findViewById(R.id.activity_user_detail_circles);
        //studentID = view.findViewById(R.id.user_detail_studentID);
        //changeButton = view.findViewById(R.id.user_detail_change);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        slidingDrawer = (SlidingDrawer) view.findViewById(R.id.activity_user_detail_slidingdrawer);
        slidingdrawer_title = (TextView) view.findViewById(R.id.activity_user_detail_slidingdrawer_title);

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slidingdrawer_title.setVisibility(View.VISIBLE);
                everyBtnEnable(false);
                menu_count++;
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slidingdrawer_title.setVisibility(View.VISIBLE);
                everyBtnEnable(true);
                menu_count--;
            }
        });

        database.getReference().child("EveryClub").child(clubName).child("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LoginData loginData = dataSnapshot.getValue(LoginData.class);
                name.setText(loginData.getName());
                email.setText(auth.getCurrentUser().getEmail());
                phone.setText(loginData.getPhone());
                //school.setText(loginData.getSchool());
                //studentID.setText(loginData.getStudentNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

/*        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 클릭하면 내 정보를 변경 할 수 있는 layout 이동 (아직 미구현)
            }
        });*/

        activity_user_detail_recyclerview_main_list = (RecyclerView) view.findViewById(R.id.activity_user_detail_recyclerview_main_list);
        activity_user_detail_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(getContext()));

        final UserDetailActivity_Fragment.AttendAdminInformationActivity_AdminRecyclerViewAdapter attendAdminInformationActivity_adminRecyclerViewAdapter = new UserDetailActivity_Fragment.AttendAdminInformationActivity_AdminRecyclerViewAdapter();

        activity_user_detail_recyclerview_main_list.setAdapter(attendAdminInformationActivity_adminRecyclerViewAdapter);
        attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

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
                                    attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

/*                                    database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            if (*//*dataSnapshot.child("name").getValue().toString().equals(snapshot.child("name").getValue(String.class)) && *//*dataSnapshot.child("phone").getValue().toString().equals(snapshot.child("phone").getValue(String.class))) {
                                                startTime = snapshot2.child("startTime").getValue().toString();
                                                listStartTime.add(startTime);
                                                Attend_Information_Item attendAdminInformationItem = snapshot.getValue(Attend_Information_Item.class);
                                                String uidKey = snapshot.getKey();
                                                attendAdminItems.add(0, attendAdminInformationItem);
                                                uidLists.add(0, uidKey);
                                                listSize++;
                                            }

                                            for (int i = 0; i < listSize; i++) {
                                                attendAdminItems.get(i).attendTimeLimit = listStartTime.get(listSize - 1 - i);
                                            }

                                            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                                            attendAdminInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });*/
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

        // 홈에서 메뉴(슬라이딩드로우)를 열었을 경우에만 뒤로가기 버튼을 누르면 슬라이딩드로우가 닫힘
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(final View view, final int i, final KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && menu_count > 0) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("finishstatus", true);
                    slidingDrawer.animateClose();
                    everyBtnEnable(true);
//                    getActivity().finish();
//                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        return view;
    } // activity_notice에 있는 화면을 가지고 온다

    public void everyBtnEnable(boolean boo) {
        name.setEnabled(boo);
        email.setEnabled(boo);
        phone.setEnabled(boo);
        profile.setEnabled(boo);
//        changeButton.setEnabled(boo);
    }

    class AttendAdminInformationActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

            return new UserDetailActivity_Fragment.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final UserDetailActivity_Fragment.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((UserDetailActivity_Fragment.AttendAdminInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_information_item_textview_date.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_information_item_textview_attend_state.setText(attendAdminItems.get(position).attend_state);
            customViewHolder.activity_attend_information_item_textview_date.setText(attendAdminItems.get(position).attendTimeLimit);

        }

        @Override
        public int getItemCount() {
            return attendAdminItems.size();
        }

    }

/*    class WrappingSlidingDrawer extends SlidingDrawer {

        public WrappingSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

            int orientation = attrs.getAttributeIntValue("android", "orientation", ORIENTATION_VERTICAL);
            mTopOffset = attrs.getAttributeIntValue("android", "topOffset", 0);
            mVertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);
        }

        public WrappingSlidingDrawer(Context context, AttributeSet attrs) {
            super(context, attrs);

            int orientation = attrs.getAttributeIntValue("android", "orientation", ORIENTATION_VERTICAL);
            mTopOffset = attrs.getAttributeIntValue("android", "topOffset", 0);
            mVertical = (orientation == SlidingDrawer.ORIENTATION_VERTICAL);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);

            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
                throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
            }

            final View handle = getHandle();
            final View content = getContent();
            measureChild(handle, widthMeasureSpec, heightMeasureSpec);

            if (mVertical) {
                int height = heightSpecSize - handle.getMeasuredHeight() - mTopOffset;
                content.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, heightSpecMode));
                heightSpecSize = handle.getMeasuredHeight() + mTopOffset + content.getMeasuredHeight();
                widthSpecSize = content.getMeasuredWidth();
                if (handle.getMeasuredWidth() > widthSpecSize) widthSpecSize = handle.getMeasuredWidth();
            }
            else {
                int width = widthSpecSize - handle.getMeasuredWidth() - mTopOffset;
                getContent().measure(MeasureSpec.makeMeasureSpec(width, widthSpecMode), heightMeasureSpec);
                widthSpecSize = handle.getMeasuredWidth() + mTopOffset + content.getMeasuredWidth();
                heightSpecSize = content.getMeasuredHeight();
                if (handle.getMeasuredHeight() > heightSpecSize) heightSpecSize = handle.getMeasuredHeight();
            }

            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }

        private boolean mVertical;
        private int mTopOffset;
    }*/

}
