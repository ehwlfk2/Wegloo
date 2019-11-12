package com.example.target_club_in_donga.home_viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.club_foundation_join.AutoCompleteDBItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClubSelectedFragment0 extends Fragment {

    /**
     * 클럽 선택
     */
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView fragment_clubSelected_recyclerview;
    private ArrayList<MyClubSeletedItem> list;//ItemFrom을 통해 받게되는 데이터를 어레이 리스트화 시킨다.
    private ClubSelected_RecyclerviewAdapter adapter;
    public ClubSelectedFragment0() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club_selected_fragment0, container, false);
        fragment_clubSelected_recyclerview = view.findViewById(R.id.fragment_clubSelected_recyclerview);
        fragment_clubSelected_recyclerview.setHasFixedSize(true);//각 아이템이 보여지는 것을 일정하게
        fragment_clubSelected_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));//앞서 선언한 리싸이클러뷰를 레이아웃메니저에 붙힌다

        adapter = new ClubSelected_RecyclerviewAdapter(getActivity(), list);//앞서 만든 리스트를 어뎁터에 적용시켜 객체를 만든다.

        fragment_clubSelected_recyclerview.setAdapter(adapter);// 그리고 만든 겍체를 리싸이클러뷰에 적용시킨다.
        fragment_clubSelected_recyclerview.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    public class ClubSelected_RecyclerviewAdapter extends RecyclerView.Adapter<ClubSelected_RecyclerviewAdapter.MyViewholder> {

        private Activity activity;
        private ArrayList<MyClubSeletedItem> datalist;
        public ClubSelected_RecyclerviewAdapter(Activity activity, ArrayList<MyClubSeletedItem> datalist){
            this.activity = activity;//보여지는 액티비티
            this.datalist = datalist;//내가 처리하고자 하는 아이템들의 리스트
        }
        //getItemCount, onCreateViewHolder, MyViewHolder, onBindViewholder 순으로 들어오게 된다.
        // 뷰홀더에서 초기세팅해주고 바인드뷰홀더에서 셋텍스트해주는 값이 최종적으로 화면에 출력되는 값

        @Override
        public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clubselected, parent, false);//뷰 생성(아이템 레이아웃을 기반으로)
            MyViewholder viewholder1 = new MyViewholder(view);//아이템레이아웃을 기반으로 생성된 뷰를 뷰홀더에 인자로 넣어줌
            return viewholder1;
        }

        @Override
        public void onBindViewHolder(final MyViewholder holder, final int position) {

            final MyClubSeletedItem data = datalist.get(position);//위치에 따라서 그에 맞는 데이터를 얻어오게 한다.

            if(data.isApprovalCompleted()){
                holder.item_clubSelected_next.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_24px));
            }
            else{
                holder.item_clubSelected_next.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_horiz_24px));
            }
            holder.item_clubSelected_title.setText(data.getSignUpclubName());

            if(!data.getSignUpclubProfile().equals("None")){
                Glide.with(getContext())
                        .asBitmap()
                        .load(data.getSignUpclubProfile())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.item_clubSelected_profile.setImageBitmap(resource);

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }

            holder.item_clubSelected_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }
        public class MyViewholder extends RecyclerView.ViewHolder {
            //ImageView profile;
            public ImageView item_clubSelected_profile;
            public TextView item_clubSelected_title;
            public ImageView item_clubSelected_next;
            public LinearLayout item_clubSelected_layout;

            public MyViewholder(final View v){
                super(v);
                item_clubSelected_profile = (ImageView) v.findViewById(R.id.item_clubSelected_profile);
                item_clubSelected_title = (TextView) v.findViewById(R.id.item_clubSelected_title);
                item_clubSelected_next = (ImageView) v.findViewById(R.id.item_clubSelected_next);
                item_clubSelected_layout = (LinearLayout) v.findViewById(R.id.item_clubSelected_layout);
            }
        }
    }

    public void initDataset(){
        list = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        /*
        MyClubSeletedItem myClubSeletedItem = new MyClubSeletedItem();
        myClubSeletedItem.setCheck(true);
        myClubSeletedItem.setSignUpClubName("아");
        myClubSeletedItem.signUpImageBitmap = null;
        list.add(myClubSeletedItem);
        list.add(myClubSeletedItem);
        list.add(myClubSeletedItem);
        list.add(myClubSeletedItem);*/

        firebaseDatabase.getReference().child("AppUser").child(firebaseAuth.getCurrentUser().getUid()).child("signUpClub").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MyClubSeletedItem myClubSeletedItem = snapshot.getValue(MyClubSeletedItem.class);
                    //Log.e("check",check+"");
                    list.add(myClubSeletedItem);
                    /*myClubSeletedItem.setCheck(check);

                    firebaseDatabase.getReference().child("EveryClubName").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            AutoCompleteDBItem autoCompleteDBItem = dataSnapshot.getValue(AutoCompleteDBItem.class);
                            myClubSeletedItem.setSignUpClubName(autoCompleteDBItem.clubTitle);
                            myClubSeletedItem.signUpImageBitmap = null;
                            list.add(myClubSeletedItem);
                            //Log.e("name",autoCompleteDBItem.clubProfile);

                            if(!autoCompleteDBItem.clubProfile.equals("None")){
                                Glide.with(getContext())
                                        .asBitmap()
                                        .load(autoCompleteDBItem.clubProfile)
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                myClubSeletedItem.signUpImageBitmap = resource;

                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                            }
                            else{
                                myClubSeletedItem.signUpImageBitmap = null;
                            }
                            //list.add(myClubSeletedItem);
//                            Log.e("name",myClubSeletedItem.getSignUpClubName());
//                            Log.e("bitmap",myClubSeletedItem.signUpImageBitmap+"");
//                            Log.e("bool",myClubSeletedItem.isCheck()+"");
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    */
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
