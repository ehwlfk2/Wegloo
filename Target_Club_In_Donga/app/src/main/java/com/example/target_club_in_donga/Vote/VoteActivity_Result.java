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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class VoteActivity_Result extends AppCompatActivity {

    private String dbKey;
    private FirebaseDatabase database;
    private ArrayList<Vote_Item_Count> items;
    private VoteActivity_ResultListAdapter adapter;
    private ListView activityvote_result_listview;
    private TextView activityvote_result_textview_title;
    private TextView activityvote_result_textview_date;
    private TextView activityvote_result_textview_totalcount;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityvote_result);
        Toast.makeText(this, "항목 클릭하면 누군지 볼수있음!!!", Toast.LENGTH_SHORT).show();

        activityvote_result_listview = (ListView)findViewById(R.id.activityvote_result_listview);
        activityvote_result_textview_title = (TextView)findViewById(R.id.activityvote_result_textview_title);
        activityvote_result_textview_date = (TextView)findViewById(R.id.activityvote_result_textview_date);
        activityvote_result_textview_totalcount = (TextView)findViewById(R.id.activityvote_result_textview_totalcount);

        Intent intent = getIntent();
        dbKey = intent.getExtras().getString("key");

        database = FirebaseDatabase.getInstance();
        items = new ArrayList<Vote_Item_Count>();

        database.getReference().child("Vote").child(dbKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vote_Item last_item = dataSnapshot.getValue(Vote_Item.class);
                //Toast.makeText(Vote_Excute.this, last_item.title+"", Toast.LENGTH_SHORT).show();
                activityvote_result_textview_totalcount.setText("투표 수 : "+last_item.totalCount);
                //items = last_item.listItems;
                // 호출 예제
                //Collections.sort(items, sortByTotalCall);
                // 역순으로 정렬시 호출

                //Collections.reverse(items);

                adapter = new VoteActivity_ResultListAdapter(last_item.listItems);
                activityvote_result_listview.setAdapter(adapter);

                activityvote_result_textview_title.setText(last_item.title);

                long unixTime = (long) last_item.timestamp;
                Date date = new Date(unixTime);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String time = simpleDateFormat.format(date);

                activityvote_result_textview_date.setText(time);

                activityvote_result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Toast.makeText(VoteActivity_Result.this, "position : "+position+"\n사실아직 구현안함", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*private final static Comparator<Vote_Item_Count> sortByTotalCall= new Comparator<Vote_Item_Count>() { //정렬
        @Override
        public int compare(Vote_Item_Count object1, Vote_Item_Count object2) {
            return Integer.compare(object1.getCount(), object2.getCount());
        }
    };*/

    public class VoteActivity_ResultListAdapter extends BaseAdapter {
        LayoutInflater inflater = null;
        private ArrayList<Vote_Item_Count> m_oData = null;
        //private int nListCnt = 0;
        public VoteActivity_ResultListAdapter(ArrayList<Vote_Item_Count> _oData) {
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
                convertView = inflater.inflate(R.layout.vote_result_listview_item, parent, false);
            }

            TextView oTextName = (TextView) convertView.findViewById(R.id.vote_result_listview_textview_name);
            TextView oTextCount = (TextView) convertView.findViewById(R.id.vote_result_listview_textview_count);

            oTextName.setText(m_oData.get(position).getName());
            oTextCount.setText(m_oData.get(position).getCount()+" 명");

            return convertView;
        }
    }
}
