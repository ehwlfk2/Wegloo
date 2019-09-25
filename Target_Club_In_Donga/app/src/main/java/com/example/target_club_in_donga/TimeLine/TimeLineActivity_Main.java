package com.example.target_club_in_donga.TimeLine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.target_club_in_donga.Accountbook.AccountBookActivity_Main;
import com.example.target_club_in_donga.Board.Board_Main;
import com.example.target_club_in_donga.Material_Management.MaterialManagementActivity;
import com.example.target_club_in_donga.NoticeActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeLineActivity_Main extends AppCompatActivity {

    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private TimeLineActivity_Main_RecyclerviewAdapter adapter;
    private ArrayList<TimeLine_Item> list = new ArrayList<>();//ItemFrom을 통해 받게되는 데이터를 어레이 리스트화 시킨다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_main);

        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.activity_timeline_main_recyclerview);
        recyclerView.setHasFixedSize(true);//각 아이템이 보여지는 것을 일정하게
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//앞서 선언한 리싸이클러뷰를 레이아웃메니저에 붙힌다

        adapter = new TimeLineActivity_Main_RecyclerviewAdapter(this, list);//앞서 만든 리스트를 어뎁터에 적용시켜 객체를 만든다.
        recyclerView.setAdapter(adapter);// 그리고 만든 겍체를 리싸이클러뷰에 적용시킨다.

        database.getReference().child("TimeLine").orderByChild("nowTimeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TimeLine_Item item = snapshot.getValue(TimeLine_Item.class);
                    item.setnowTimeStamp(-1*(long)item.getnowTimeStamp());
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class TimeLineActivity_Main_RecyclerviewAdapter extends RecyclerView.Adapter<TimeLineActivity_Main.TimeLineActivity_Main_RecyclerviewAdapter.MyViewholder> {

        private Activity activity;
        private ArrayList<TimeLine_Item> datalist;
        //getItemCount, onCreateViewHolder, MyViewHolder, onBindViewholder 순으로 들어오게 된다.
        // 뷰홀더에서 초기세팅해주고 바인드뷰홀더에서 셋텍스트해주는 값이 최종적으로 화면에 출력되는 값

        @Override
        public TimeLineActivity_Main.TimeLineActivity_Main_RecyclerviewAdapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_main_recyclerview_item, parent, false);//뷰 생성(아이템 레이아웃을 기반으로)
            TimeLineActivity_Main.TimeLineActivity_Main_RecyclerviewAdapter.MyViewholder viewholder1 = new TimeLineActivity_Main.TimeLineActivity_Main_RecyclerviewAdapter.MyViewholder(view);//아이템레이아웃을 기반으로 생성된 뷰를 뷰홀더에 인자로 넣어줌
            return viewholder1;
        }

        @Override
        public void onBindViewHolder(final TimeLineActivity_Main.TimeLineActivity_Main_RecyclerviewAdapter.MyViewholder holder, final int position) {

            final TimeLine_Item data = datalist.get(position);//위치에 따라서 그에 맞는 데이터를 얻어오게 한다.
            holder.timelineName.setText(data.getName());
            holder.timelineTitle.setText(data.getTitle());//앞서 뷰홀더에 세팅해준 것을 각 위치에 맞는 것들로 보여주게 하기 위해서 세팅해준다.

            //holder.profile.setImageResource(data.getImageNumber());

            //holder.voteDate.setText(data.getTime());
            long unixTime = (long) data.getnowTimeStamp();
            Date date = new Date(unixTime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            holder.timelineTimestamp.setText(time);

            holder.timelineLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getType().equals("Notice")){
                        Intent intent  = new Intent(TimeLineActivity_Main.this, NoticeActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                    else if(data.getType().equals("Vote")){
                        Intent intent = new Intent(TimeLineActivity_Main.this, VoteActivity_Main.class);
                        startActivity(intent);
                        //finish();
                    }
                    else if(data.getType().equals("Material_Management")){ //다른것들 추가해줘야해
                        Intent intent = new Intent(TimeLineActivity_Main.this, MaterialManagementActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                    else if(data.getType().equals("Board")){
                        Intent intent = new Intent(TimeLineActivity_Main.this, Board_Main.class);
                        startActivity(intent);
                    }
                    else if(data.getType().equals("AccountBook")){
                        Intent intent = new Intent(TimeLineActivity_Main.this, AccountBookActivity_Main.class);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }
        public class MyViewholder extends RecyclerView.ViewHolder {
            //ImageView profile;
            TextView timelineName;
            TextView timelineTitle;
            TextView timelineTimestamp;
            LinearLayout timelineLayout;

            public MyViewholder(final View v){
                super(v);
                timelineName = (TextView) v.findViewById(R.id.timeline_main_item_name);
                timelineTitle = (TextView) v.findViewById(R.id.timeline_main_item_title);
                timelineTimestamp = (TextView) v.findViewById(R.id.timeline_main_item_timestamp);
                timelineLayout = (LinearLayout)v.findViewById(R.id.timeline_main_item_layout);
            }
        }
        public TimeLineActivity_Main_RecyclerviewAdapter(Activity activity, ArrayList<TimeLine_Item> datalist){
            this.activity = activity;//보여지는 액티비티
            this.datalist = datalist;//내가 처리하고자 하는 아이템들의 리스트
        }
    }
}
