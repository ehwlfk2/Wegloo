package com.example.target_club_in_donga.Vote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class VoteActivity_Execute extends AppCompatActivity {

    private FirebaseDatabase database;
    private String dbKey;
    private ArrayList<Vote_Item_Count> items;
    private int totalCount = 0;
    private VoteActivity_Execute_ListAdapter adapter;
    private ListView activityvote_execute_listview;
    private TextView activityvote_execute_textview_title;
    private TextView activityvote_execute_textview_date;
    private FloatingActionButton activityvote_execute_button_result;
    private FloatingActionButton activityvote_execute_button_randomresult;
    private FirebaseAuth auth;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityvote_execute);

        activityvote_execute_listview = findViewById(R.id.activityvote_execute_listview);
        activityvote_execute_textview_title = findViewById(R.id.activityvote_execute_textview_title);
        activityvote_execute_textview_date = findViewById(R.id.activityvote_execute_textview_date);
        //excuteLayout = (LinearLayout)findViewById(R.id.excute_layout);
        activityvote_execute_button_result = findViewById(R.id.activityvote_execute_button_result);
        activityvote_execute_button_randomresult = findViewById(R.id.activityvote_execute_button_randomresult);

        activityvote_execute_button_result.show(false);
        activityvote_execute_button_randomresult.show(false);

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        dbKey = intent.getExtras().getString("key");

        //Toast.makeText(this, dbKey+"", Toast.LENGTH_SHORT).show();
        items = new ArrayList<>();
        //Vote_Item_Count item = new Vote_Item_Count();


        //item.setName("안녕");
        //item.setCount(3);

        //items.add(item);

        //Toast.makeText(this, items.get(0).getName()+"", Toast.LENGTH_SHORT).show();
        //adapter.notifyDataSetChanged();




        database = FirebaseDatabase.getInstance();
        database.getReference().child("EveryClub").child(clubName).child("Vote").child(dbKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vote_Item last_item = dataSnapshot.getValue(Vote_Item.class);
                //Toast.makeText(Vote_Excute.this, last_item.title+"", Toast.LENGTH_SHORT).show();
                items = last_item.listItems;
                totalCount = last_item.totalCount;

                adapter = new VoteActivity_Execute_ListAdapter(items);
                activityvote_execute_listview.setAdapter(adapter);

                activityvote_execute_textview_title.setText(last_item.title);

                long unixTime = (long) last_item.timestamp;
                Date date = new Date(unixTime);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String time = simpleDateFormat.format(date);

                activityvote_execute_textview_date.setText(time+" 까지");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        activityvote_execute_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                activityvote_execute_listview.setSelector(R.drawable.border_orange);
                //Toast.makeText(Vote_Excute.this, ""+position, Toast.LENGTH_SHORT).show();
                activityvote_execute_button_result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.onStarClicked(database.getReference().child("EveryClub").child(clubName).child("Vote").child(dbKey), position);
                        Toast.makeText(VoteActivity_Execute.this, items.get(position).getName()+" 에 투표 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        activityvote_execute_button_randomresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random = (int)(Math.random()*items.size());
                adapter.onStarClicked(database.getReference().child("EveryClub").child(clubName).child("Vote").child(dbKey), random);
                Toast.makeText(VoteActivity_Execute.this, items.get(random).getName()+" 에 투표 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public class VoteActivity_Execute_ListAdapter extends BaseAdapter {
        LayoutInflater inflater = null;
        private ArrayList<Vote_Item_Count> m_oData = null;
        //private int nListCnt = 0;
        public VoteActivity_Execute_ListAdapter(ArrayList<Vote_Item_Count> _oData) {
            m_oData = _oData;
        }
        @Override
        public int getCount() {
            //Log.i("TAG", "getCount");
            return m_oData.size();
        }
        @Override
        public Object getItem(int position) {
            return m_oData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                final Context context = parent.getContext();
                if (inflater == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                convertView = inflater.inflate(R.layout.vote_execute_listview_item, parent, false);
            }

            TextView oTextName = (TextView) convertView.findViewById(R.id.vote_execute_listview_item_textview_name);
            TextView oTextCount = (TextView) convertView.findViewById(R.id.vote_execute_listview_item_textview_count);

            oTextName.setText(m_oData.get(position).getName());
            oTextCount.setText(+m_oData.get(position).getCount()+" 명");

            return convertView;
        }

        private void onStarClicked(DatabaseReference postRef, final int lastPosition){
            postRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Vote_Item p = mutableData.getValue(Vote_Item.class);
                    if(p == null){
                        return Transaction.success(mutableData);
                    }
                    if(p.stars.containsKey(auth.getCurrentUser().getUid())){
                        //p.listItems.get(lastPosition).count = p.listItems.get(lastPosition).count -1;
                        //p.stars.remove(auth.getCurrentUser().getUid());
                        //Toast.makeText(Vote_Excute.this, p.stars.containsKey(auth.getCurrentUser().getUid())+"", Toast.LENGTH_SHORT).show();
                        int beforePosition = p.stars.get(auth.getCurrentUser().getUid());
                        p.listItems.get(beforePosition).count -= 1;
                        p.listItems.get(lastPosition).count += 1;
                        p.stars.put(auth.getCurrentUser().getUid(),lastPosition);

                    }
                    else{
                        p.listItems.get(lastPosition).count += 1;
                        p.totalCount += 1;
                        p.stars.put(auth.getCurrentUser().getUid(),lastPosition);
                        //Toast.makeText(Vote_Excute.this, p.stars.containsKey(auth.getCurrentUser().getUid())+"", Toast.LENGTH_SHORT).show();
                    }

                    mutableData.setValue(p);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                }
            });
        }
    }
}
