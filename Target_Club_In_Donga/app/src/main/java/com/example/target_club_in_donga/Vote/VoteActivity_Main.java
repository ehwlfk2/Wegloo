package com.example.target_club_in_donga.Vote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VoteActivity_Main extends AppCompatActivity {
    private RecyclerView activityvote_main_recyclerview;
    private VoteActivity_Main_RecyclerviewAdapter adapter;
    private FloatingActionButton activityvote_main_button_intent;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private SimpleDateFormat simpleDateFormat;
    public boolean adminCheck;
    public static final int insertPageNumber = 1001;

    private ArrayList<Vote_Item_Main> list = new ArrayList<>();//ItemFrom을 통해 받게되는 데이터를 어레이 리스트화 시킨다.
    public static ArrayList<String> dbKey = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityvote_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        /*

        database.getReference().child("User").child(auth.getCurrentUser().getUid()).child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminCheck = dataSnapshot.getValue(boolean.class);
                Log.e("1",""+adminCheck);
                vvvv();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //Intent intent = getIntent();
        //adminCheck = intent.getExtras().getBoolean("adminCheck");
        adminCheck = true;

        activityvote_main_button_intent = (FloatingActionButton)findViewById(R.id.activityvote_main_button_intent);

        if(!adminCheck){
            activityvote_main_button_intent.setVisibility(View.INVISIBLE);
        }


        activityvote_main_recyclerview = (RecyclerView)findViewById(R.id.activityvote_main_recyclerview);
        activityvote_main_button_intent.attachToRecyclerView(activityvote_main_recyclerview);
        activityvote_main_button_intent.show();

        activityvote_main_recyclerview.setHasFixedSize(true);//각 아이템이 보여지는 것을 일정하게
        activityvote_main_recyclerview.setLayoutManager(new LinearLayoutManager(this));//앞서 선언한 리싸이클러뷰를 레이아웃메니저에 붙힌다

        adapter = new VoteActivity_Main_RecyclerviewAdapter(this, list);//앞서 만든 리스트를 어뎁터에 적용시켜 객체를 만든다.
        activityvote_main_recyclerview.setAdapter(adapter);// 그리고 만든 겍체를 리싸이클러뷰에 적용시킨다.

        //database = FirebaseDatabase.getInstance();
        database.getReference().child("Vote").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                dbKey.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //MaterialManagement_Item imageDTO = snapshot.getValue(MaterialManagement_Item.class);
                    Vote_Item vote_last_item = snapshot.getValue(Vote_Item.class);
                    //String title =

                    long nowTime = System.currentTimeMillis();
                    if(Long.compare(nowTime,(long)vote_last_item.timestamp) >= 0){ //시간 끝났을때
                        database.getReference().child("Vote").child(snapshot.getKey()).child("deadline").setValue(true);
                    }

                    if(/*Long.compare(nowTime,(long)vote_last_item.timestamp) >= 0 ||*/ vote_last_item.deadline){
                        list.add(new Vote_Item_Main(vote_last_item.title,vote_last_item.timestamp,"gray",vote_last_item.totalCount));
                    }
                    else if(vote_last_item.stars.containsKey(auth.getCurrentUser().getUid())){
                        list.add(new Vote_Item_Main(vote_last_item.title,vote_last_item.timestamp,"orange",vote_last_item.totalCount));
                    }
                    else{
                        list.add(new Vote_Item_Main(vote_last_item.title,vote_last_item.timestamp,"green",vote_last_item.totalCount));
                    }

                    dbKey.add(snapshot.getKey());
                    //Toast.makeText(MainActivity.this, dbKey.get(0)+"", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        activityvote_main_button_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoteActivity_Main.this,VoteActivity_Insert.class);
                startActivityForResult(intent,insertPageNumber);//액티비티 띄우기
                //moveTaskToBack(true);
                //finish();
            }
        });
    }


    public class VoteActivity_Main_RecyclerviewAdapter extends RecyclerView.Adapter<VoteActivity_Main_RecyclerviewAdapter.MyViewholder> {

        private Activity activity;
        private ArrayList<Vote_Item_Main> datalist;
        private FirebaseDatabase database;
        //getItemCount, onCreateViewHolder, MyViewHolder, onBindViewholder 순으로 들어오게 된다.
        // 뷰홀더에서 초기세팅해주고 바인드뷰홀더에서 셋텍스트해주는 값이 최종적으로 화면에 출력되는 값

        @Override
        public VoteActivity_Main_RecyclerviewAdapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_main_recyclerview_item, parent, false);//뷰 생성(아이템 레이아웃을 기반으로)
            MyViewholder viewholder1 = new MyViewholder(view);//아이템레이아웃을 기반으로 생성된 뷰를 뷰홀더에 인자로 넣어줌
            return viewholder1;
        }

        @Override
        public void onBindViewHolder(final VoteActivity_Main_RecyclerviewAdapter.MyViewholder holder, final int position) {
            Vote_Item_Main data = datalist.get(position);//위치에 따라서 그에 맞는 데이터를 얻어오게 한다.
            holder.voteTitle.setText(data.getTitle());//앞서 뷰홀더에 세팅해준 것을 각 위치에 맞는 것들로 보여주게 하기 위해서 세팅해준다.
            //holder.profile.setImageResource(data.getImageNumber());

            //holder.voteDate.setText(data.getTime());
            long unixTime = (long) data.getTimestamp();
            Date date = new Date(unixTime);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            holder.voteDate.setText(time);

            String color = data.getColor();
            if(color.equals("green")){
                holder.voteLayout.setBackgroundResource(R.drawable.border_green);
                PopupMenu(holder, position, "green");
            }
            else if(color.equals("orange")){

                holder.voteLayout.setBackgroundResource(R.drawable.border_orange);
                PopupMenu(holder, position, "orange");
            }
            else{
                holder.voteLayout.setBackgroundResource(R.drawable.border_gray);
                PopupMenu(holder, position, "gray");
            }

        }
        public void delete_item(final int position){
            //ArrayList<String> dbKey = new ArrayList<String>();

            database = FirebaseDatabase.getInstance();
            database.getReference().child("Vote").child(dbKey.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(MainActivity.this, "삭제성공", Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, "삭제 성공", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(MainActivity.this, "삭제실패", Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, "삭제 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }
        public class MyViewholder extends RecyclerView.ViewHolder {
            //ImageView profile;
            TextView voteTitle;
            TextView voteDate;
            LinearLayout voteLayout;

            public MyViewholder(final View v){
                super(v);

                voteTitle = (TextView) v.findViewById(R.id.vote_main_recyclerview_item_textview_title);
                voteDate = (TextView) v.findViewById(R.id.vote_main_recyclerview_item_textview_date);
                voteLayout = (LinearLayout) v.findViewById(R.id.vote_main_recyclerview_item_linarlayout);

            }
        }
        public VoteActivity_Main_RecyclerviewAdapter(Activity activity, ArrayList<Vote_Item_Main> datalist){
            this.activity = activity;//보여지는 액티비티
            this.datalist = datalist;//내가 처리하고자 하는 아이템들의 리스트
        }

        public void PopupMenu(final VoteActivity_Main_RecyclerviewAdapter.MyViewholder holder,final int position, final String color){
            holder.voteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete_item(position);
                    PopupMenu popup = new PopupMenu(view.getContext(), view);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //int x = item.getItemId();
                            switch (item.getItemId()){
                                case R.id.vote_excute:
                                    Intent intent = new Intent(VoteActivity_Main.this, VoteActivity_Execute.class);
                                    intent.putExtra("key",dbKey.get(position));
                                    startActivity(intent);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    return true;
                                case R.id.vote_status:
                                    //Toast.makeText(activity, voteTitle.getText().toString()+"", Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(VoteActivity_Main.this, VoteActivity_Result.class);
                                    intent2.putExtra("key",dbKey.get(position));
                                    startActivity(intent2);
                                    return true;
                                case R.id.vote_deadline:
                                    //holder.voteLayout.setBackgroundResource(R.drawable.border_gray);
                                    //Map<String, Object> map = new HashMap<>();
                                    //map.put("deadline",true);
                                    FirebaseDatabase.getInstance().getReference().child("Vote").child(dbKey.get(position)).child("deadline").setValue(true);
                                    return true;
                                case R.id.vote_delete:
                                    delete_item(position);
                                    //Toast.makeText(activity, voteTitle.getText().toString()+"", Toast.LENGTH_SHORT).show();
                                    return true;

                                default:
                                    return false;
                            }
                            //return false;
                        }
                    });
                    popup.inflate(R.menu.vote_main_popup);
                    if(!adminCheck){
                        popup.getMenu().getItem(2).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                    }

                    if(color == "orange"){
                        popup.getMenu().getItem(0).setTitle("재투표 하기");
                    }
                    else if(color == "gray"){
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(1).setTitle("결과 보기");
                        popup.getMenu().getItem(2).setVisible(false);
                    }
                    popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                    popup.show();
                }
            });
        }
    }
}
