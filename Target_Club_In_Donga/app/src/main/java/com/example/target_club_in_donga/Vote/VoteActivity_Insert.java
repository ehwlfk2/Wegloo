package com.example.target_club_in_donga.Vote;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.PushMessages.SendPushMessages;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class    VoteActivity_Insert  extends AppCompatActivity {

    private VoteActivity_Insert_ListAdapter adapter;
    private FloatingActionButton activityvote_insert_button_listinsert;
    private FloatingActionButton activityvote_insert_button_listremove;
    private ListView activityvote_insert_listview;
    private TextView activityvote_insert_textview_calendar;
    private TextView activityvote_insert_textview_time;
    private EditText activityvote_insert_edittext_title;
    private Switch activityvote_insert_switch;

    private String dateStr;
    private String timeStr;
    private String formatDate, formatHour, formatMin;
    private long now;

    private FloatingActionButton ctivityvote_insert_button_result;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;

    public  ArrayList<Vote_Item_Count> listViewItemList = new ArrayList<Vote_Item_Count>();
    private ArrayList<Vote_Item_Count> filteredItemList = listViewItemList;

    //private ArrayList<Vote_Item> lastItems = new ArrayList<Vote_Item>();
    //private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityvote_insert);
            activityvote_insert_button_listremove = (FloatingActionButton)findViewById(R.id.activityvote_insert_button_listremove);
            activityvote_insert_button_listinsert = (FloatingActionButton)findViewById(R.id.activityvote_insert_button_listinsert);
            activityvote_insert_listview = (ListView)findViewById(R.id.activityvote_insert_listview);
            activityvote_insert_textview_calendar = (TextView)findViewById(R.id.activityvote_insert_textview_calendar);
            activityvote_insert_textview_time=(TextView)findViewById(R.id.activityvote_insert_textview_time);
            ctivityvote_insert_button_result = (FloatingActionButton)findViewById(R.id.activityvote_insert_button_result);
            activityvote_insert_edittext_title = (EditText)findViewById(R.id.activityvote_insert_edittext_title);
            activityvote_insert_switch = (Switch)findViewById(R.id.activity_vote_insert_switch);
            activityvote_insert_button_listremove.show(false);
            activityvote_insert_button_listinsert.show(false);
            ctivityvote_insert_button_result.show(false);

            firebaseDatabase = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();
            //firebaseDatabase.getReference().child("images").push().setValue(imageDTO);

        now = System.currentTimeMillis();
            // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
            // nowDate 변수에 값을 저장한다.
        formatDate = sdfNow.format(date);//"yyyy/MM/dd HH:mm:ss"
        activityvote_insert_textview_calendar.setText(formatDate);

        sdfNow = new SimpleDateFormat("HH:mm");
        formatHour = sdfNow.format(date);//"yyyy/MM/dd HH:mm:ss"*/

        /*int temp = Integer.parseInt(formatHour);
        if(temp < 12){
            formatDate = "오전 "+temp;
        }
        else if(temp == 12){
            formatDate = "오후 "+temp;
        }
        else{
            formatDate = "오후 "+(temp-12);
        }

        sdfNow = new SimpleDateFormat("mm");
        formatMin = sdfNow.format(date);
        formatDate += ":"+formatMin;
        timeTextView.setText(formatDate);*/
        activityvote_insert_textview_time.setText(formatHour);

        adapter = new VoteActivity_Insert_ListAdapter();
        activityvote_insert_listview.setAdapter(adapter);
        adapter.addItem("",0); //기본적으로 일단 두개 항목!
        adapter.addItem("",0);
        activityvote_insert_edittext_title.requestFocus();

        activityvote_insert_button_listinsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(Vote_Insert.this, "성공", Toast.LENGTH_SHORT).show();
                    adapter.addItem("",0);
                    adapter.notifyDataSetChanged();
                    //num++;
                }
            });
            activityvote_insert_button_listremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(Vote_Insert.this, "성공", Toast.LENGTH_SHORT).show();
                    adapter.delItem();
                    adapter.notifyDataSetChanged();
                    //num --;
                }
            });

            activityvote_insert_textview_calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog_DatePicker();
                }
            });

            activityvote_insert_textview_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog_TimePicker();
                }
            });

            ctivityvote_insert_button_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String title = activityvote_insert_edittext_title.getText().toString(); //제목 거르기

                    int cnt = listViewItemList.size(); //항목 없는거 거르기

                    int noneListFlag = 0;
                    for(int i=0; i<cnt;i++){  //항목 안에 내용 없는거 거르기
                        if((listViewItemList.get(i).getName()).equals("")){
                            noneListFlag = 1;
                        }
                    }

                    String str = activityvote_insert_textview_calendar.getText().toString()+" "+activityvote_insert_textview_time.getText().toString()+":00";
                    long tt = dateToMills(str); //기간 거르기

                    if(title.equals("")){
                        Toast.makeText(VoteActivity_Insert.this, "제목 입력!", Toast.LENGTH_SHORT).show();
                    }
                    else if(cnt <= 0){
                        Toast.makeText(VoteActivity_Insert.this, "항목이 하나라도 있어야댐!", Toast.LENGTH_SHORT).show();
                    }
                    else if(noneListFlag == 1){
                        Toast.makeText(VoteActivity_Insert.this, "항목 안에 내용은??", Toast.LENGTH_SHORT).show();
                    }
                    else if(Long.compare(now,tt) >= 0){  //now > tt
                        Toast.makeText(VoteActivity_Insert.this, "기간설정 다시!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(activityvote_insert_switch.isChecked()){ //push
                            SendPushMessages send = new SendPushMessages();
                            send.multipleSendMessage("투표가 추가되었습니다",title, "Vote");
                        }
                        //Toast.makeText(Vote_Insert.this, nowTime+" 현재시간", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Vote_Insert.this, dbTime+" 디비시간", Toast.LENGTH_SHORT).show();
                        Vote_Item last_item = new Vote_Item();
                        last_item.title = title;
                        last_item.timestamp = tt; //calTextView.getText().toString() +" "+timeTextView.getText().toString();

                        last_item.listItems = listViewItemList;
                        //last_item.totalCount;


                        //Toast.makeText(Vote_Insert.this, tt+"", Toast.LENGTH_SHORT).show();
                        //last_item.uid =

                        //Toast.makeText(Vote_Insert.this, last_item.listItems.get(0).getName()+"", Toast.LENGTH_SHORT).show();
                        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("Vote").push().setValue(last_item).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(Vote_Insert.this, "추가 성공", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(Vote_Insert.this, "추가 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Intent intent = new Intent(Vote_Insert.this, MainActivity.class);
                        //startActivity(intent);
                        finish();
                        //onPause();
                    }
                }
            });
    }

    public class VoteActivity_Insert_ListAdapter extends BaseAdapter {

        //public String name/*,count,price*/;

        @Override
        public int getCount() {
            return filteredItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int pos = filteredItemList.get(position).getCount();
            final Context context = parent.getContext();
            final ViewHolder holder;


            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.vote_insert_listview_item, parent, false);
                holder.editText = (EditText)convertView.findViewById(R.id.vote_insert_listview_item_edittext);
                //holder.editText2 = (EditText)convertView.findViewById(R.id.editText2);
                //holder.editText3 = (EditText)convertView.findViewById(R.id.editText3);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.ref = position;

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            final EditText editText1 = (EditText)convertView.findViewById(R.id.vote_insert_listview_item_edittext);

            // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
            final Vote_Item_Count listViewItem = filteredItemList.get(position);

            holder.editText.setText(listViewItem.getName());
            //holder.editText2.setText(listViewItem.getCount());
            // holder.editText3.setText(listViewItem.getPrice());

            //name+=holder.editText1.getText()+"#";
            //count+=holder.editText1.getText()+"#";
            //price+=holder.editText1.getText()+"#";

            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filteredItemList.get(holder.ref).setName(s.toString());
                }
            });

            return convertView;
        }

        public void addItem(String name,/* String count, String price,*/ int count) {
            Vote_Item_Count item = new Vote_Item_Count();
            item.setName(name);
            //item.setCount(count);
            //item.setPrice(price);
            item.setCount(count);

            listViewItemList.add(item);
        }

        public void delItem() {
            if (listViewItemList.size() < 1) {
            } else {
                listViewItemList.remove(listViewItemList.size() - 1);
            }
        }
    }
    public class ViewHolder {
        EditText editText;
        //EditText editText2;
        //EditText editText3;
        int ref;
    }


    private void Dialog_DatePicker(){
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if(month < 9)
                    dateStr = year+"-0"+(month+1)+"-";
                else
                    dateStr = year+"-"+(month+1)+"-";

                if(day < 10)
                    dateStr += ("0"+day);
                else
                    dateStr += day;

                activityvote_insert_textview_calendar.setText(dateStr);
                //Toast.makeText(Vote_Insert.this, dateStr, Toast.LENGTH_SHORT).show();
            }
        };
        DatePickerDialog alert = new DatePickerDialog(this,mDateSetListener,cyear,cmonth,cday);
        alert.show();

        //return dateStr;
    }

    private void Dialog_TimePicker(){
        TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {

                if(hour < 10)
                    timeStr = "0"+hour;
                else
                    timeStr = hour+"";

                if(min < 10)
                    timeStr += ":0"+min;
                else
                    timeStr += ":"+min;

                activityvote_insert_textview_time.setText(timeStr);
            }
        };
        Calendar t = Calendar.getInstance();
        int thour = t.get(Calendar.HOUR_OF_DAY);
        int tmin = t.get(Calendar.MINUTE);
        TimePickerDialog talert = new TimePickerDialog(this, mTimeSetListener,thour, tmin,false);
        talert.show();

        //return timeStr;
    }

    public long dateToMills(String date){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date trans_date = null;
        try{
            trans_date = formatter.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return trans_date.getTime();
    }
}
