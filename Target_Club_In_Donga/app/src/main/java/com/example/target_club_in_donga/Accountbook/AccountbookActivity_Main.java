package com.example.target_club_in_donga.Accountbook;


import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.EOFException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountbookActivity_Main extends AppCompatActivity {
    private String TAG = "AccountbookActivity_Main";
    private static final int GALLERY_CODE = 12;
    private FirebaseAuth auth;
    //    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView imageView;
    private EditText title;
    private EditText description;
    private String imagePath;
    private Button button;


    private Context mContext;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private List<Accountbook_item> mArrayList = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat;
    private String msg;
    private RecyclerView mRecyclerView;
    private String strID;
    private String strPay;
    private String strDetail;
    private int dateStr;
    private int dateStr2;
    private long now;
    int result = 0;
    int q = 0;
    int w = 0;
    String ID = null;
    TextView paytotals;
    TextView day;
    TextView incometotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityaccountbook_main);

         // databaseReference.child("board").push().setValue(day);
        day = findViewById(R.id.day);
        mRecyclerView = findViewById(R.id.recyclerView);

        paytotals = findViewById(R.id.paytotal);
        incometotal=findViewById(R.id.incometotal);
        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        day.setText(sdfNow.format(date));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final mRecyclerViewAdapter mAdapter = new mRecyclerViewAdapter(this, (ArrayList<Accountbook_item>) mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        ImageView buttonInsert = findViewById(R.id.imageView1);
        //Toast.makeText(mContext, ""+day.getText().toString(), Toast.LENGTH_SHORT).show();
        msg = day.getText().toString();
        long qq = dateToMills(msg);
        //Log.e("timestamp",""+qq);

        firebaseDatabase.getReference().child("board").child(qq+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArrayList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //Accountbook_item2 imageDTO = snapshot.getValue(Accountbook_item2.class);
                    Accountbook_Item2 item2 = snapshot.getValue(Accountbook_Item2.class);
                    //Log.e("ㅁㄴㅇㄻㄴㅇㄹ",""+item2.id);
                    Accountbook_item item =  new Accountbook_item(item2.id,item2.pay,item2.detail);
                    //Toast.makeText(mContext, ""+imageDTO.getPay(), Toast.LENGTH_SHORT).show();

                    mArrayList.add(item);
                    uidLists.add(snapshot.getKey());

                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /*
        firebaseDatabase.getReference().child("board").child(qq+"").child("-Lnk_RI5HDSPUCBHGHIt").child("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tttt = dataSnapshot.getValue(String.class);
                Toast.makeText(mContext, ""+tttt, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        day.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                Log.e(TAG, cal.get(Calendar.YEAR) + "");
                Log.e(TAG, cal.get(Calendar.MONTH) + 1 + "");
                Log.e(TAG, cal.get(Calendar.DATE) + "");
                Log.e(TAG, cal.get(Calendar.HOUR_OF_DAY) + "");
                Log.e(TAG, cal.get(Calendar.MINUTE) + "");

                DatePickerDialog dialog = new DatePickerDialog(AccountbookActivity_Main.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        if(month<10&&date<10){
                            msg=String.format(("%d-0%d-0%d"),year,month+1,date);
                        }
                        else if(month<10){
                            msg=String.format(("%d-0%d-%d"),year,month+1,date);
                        }
                        else if(date<10){
                            msg=String.format(("%d-%d-0%d"),year,month+1,date);
                        }

                        else{
                            msg=String.format("%d-%d-%d",year,month+1,date);
                        }


                        Toast.makeText(AccountbookActivity_Main.this, msg, Toast.LENGTH_SHORT).show();
                        day.setText(msg);
                       // msg += " 00:00:00";
                        long qq = dateToMills(msg);


                        firebaseDatabase.getReference().child("board").child(qq+"").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mArrayList.clear();
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    //Accountbook_item2 imageDTO = snapshot.getValue(Accountbook_item2.class);
                                    Accountbook_Item2 item2 = snapshot.getValue(Accountbook_Item2.class);
                                   // Log.e("ㅁㄴㅇㄻㄴㅇㄹ",""+item2.id);
                                    Accountbook_item item =  new Accountbook_item(item2.id,item2.pay,item2.detail);

                                    //Toast.makeText(mContext, ""+imageDTO.getPay(), Toast.LENGTH_SHORT).show();

                                    mArrayList.add(item);
                                    uidLists.add(snapshot.getKey());
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();

            }
        });

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(AccountbookActivity_Main.this);
                View view = LayoutInflater.from(AccountbookActivity_Main.this)
                        .inflate(R.layout.accountbook_editbox, null, false);
                builder.setView(view);
                final Button ButtonSubmit = view.findViewById(R.id.button_dialog_submit);
                final EditText editTextPay = view.findViewById(R.id.edittext_dialog_pay);
                final EditText editTextDetail = view.findViewById(R.id.edittext_dialog_detail);
                final CheckBox checkBox = view.findViewById(R.id.account_pay);
                final CheckBox checkBox1 = view.findViewById(R.id.account_income);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checkBox1.setChecked(false);
                    }
                });
                checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checkBox.setChecked(false);
                    }
                });

                ButtonSubmit.setText("삽입");

                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {

                        msg=day.getText().toString();
                        long tt = dateToMills(msg);
                        strPay = editTextPay.getText().toString();
                        strDetail = editTextDetail.getText().toString();
                        if (checkBox.isChecked()) {
                            strID = checkBox.getText().toString();
                            checkBox1.setChecked(false);
                        } else if (checkBox1.isChecked()) {
                            strID = checkBox1.getText().toString();
                            checkBox.setChecked(false);
                        }
                        if (strPay.equals(("")) || strDetail.equals((""))) {
                            Toast.makeText(AccountbookActivity_Main.this,
                                    "값이 없습니다.",
                                    Toast.LENGTH_LONG).show();

                        } else {

                            Accountbook_item item = new Accountbook_item(strID, strPay, strDetail);
                            mArrayList.add(item); //첫 줄에 삽입
                            //mArrayList.add(dict); //마지막 줄에 삽입


                            if(checkBox1.isChecked()){
                                dateStr+=Integer.valueOf(strPay);
                                databaseReference.child("board").child("income").setValue(dateStr);

                            }else{
                                dateStr2+=Integer.valueOf(strPay);
                                databaseReference.child("board").child("pay").setValue(dateStr2);

                            }
                             databaseReference.child("board").child(""+tt).push().setValue(item);
                            incometotal.setText(""+dateStr);
                            paytotals.setText(""+dateStr2);
                            mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
// 편집삭제시 내가선택한 어뎁터부분 ㅇㅇ 에서 pay 가져옴

                            dialog.dismiss();
                        }
                    }

                });

                dialog.show();

            }

        });
    }
    public long dateToMills(String date){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date trans_date = null;
        try{
            trans_date = formatter.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return trans_date.getTime();
    }


    class mRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accountbook_item, parent, false);
            return new CustomViewHolder(view);
        }
        public mRecyclerViewAdapter(Context context, ArrayList<Accountbook_item> list){
            mArrayList = list;
            mContext = context;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            Accountbook_item data = mArrayList.get(position);//위치에 따라서 그에 맞는 데이터를 얻어오게 한다.

            ((CustomViewHolder) holder).textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            ((CustomViewHolder) holder).textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            ((CustomViewHolder) holder).textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

            ((CustomViewHolder) holder).textView1.setGravity(Gravity.CENTER);
            ((CustomViewHolder) holder).textView2.setGravity(Gravity.CENTER);
            ((CustomViewHolder) holder).textView3.setGravity(Gravity.CENTER);

            ((CustomViewHolder) holder).textView1.setText(mArrayList.get(position).getId());
            ((CustomViewHolder) holder).textView2.setText(mArrayList.get(position).getPay());
            ((CustomViewHolder) holder).textView3.setText(mArrayList.get(position).getDetail());



            //글라인드 라이브러리로 어뎁터에 이미지 넣기
//            ((CustomViewHolder)holder).deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //delete_content(position);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView textView1;
            TextView textView2;
            TextView textView3;

            public CustomViewHolder(View view) {
                super(view);
                this.textView1=view.findViewById(R.id.textview_recyclerview_id);
                this.textView3=view.findViewById(R.id.textview_recyclerview_detail);
                this.textView2=view.findViewById(R.id.textview_recyclerview_pay);
                view.setOnCreateContextMenuListener(this);

            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
                MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
                Edit.setOnMenuItemClickListener(onEditMenu);
                Delete.setOnMenuItemClickListener(onEditMenu);
            }

            private MenuItem.OnMenuItemClickListener onEditMenu;

            {
                onEditMenu = new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case 1001:

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                // 다이얼로그를 보여주기 위해 edit_box.xml 파일을 사용합니다.
                                View view = LayoutInflater.from(mContext)
                                        .inflate(R.layout.accountbook_editbox, null, false);
                                builder.setView(view);
                             //   final Button ButtonSubmit = view.findViewById(R.id.button_dialog_submit);
                                final CheckBox checkBox = view.findViewById(R.id.account_pay);
                                final Button ButtonSubmit = view.findViewById(R.id.button_dialog_submit);
                                final CheckBox checkBox1 = view.findViewById(R.id.account_income);
                                final EditText editTextPay = view.findViewById(R.id.edittext_dialog_pay);
                                final EditText editTextDetail = view.findViewById(R.id.edittext_dialog_detail);
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        checkBox1.setChecked(false);
                                    }
                                });
                                checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        checkBox.setChecked(false);
                                    }
                                });
                                // 6. 해당 줄에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줍니다.
                                textView1.setText(mArrayList.get(getAdapterPosition()).getId());
                                editTextPay.setText(mArrayList.get(getAdapterPosition()).getPay());
                                editTextDetail.setText(mArrayList.get(getAdapterPosition()).getDetail());
                                if(textView1.getText().toString()=="지출"){
                                    checkBox.setChecked(true);
                                }
                                else if(textView1.getText().toString()=="수입"){
                                    checkBox1.setChecked(true);
                            }

                                final AlertDialog dialog = builder.create();
                                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("SetTextI18n")
                                    public void onClick(View view) {
                                        String strID = ID;

                                        if (checkBox.isChecked() == true) {
                                            strID = checkBox.getText().toString();
                                            checkBox1.setChecked(false);
                                        }
                                        if (checkBox1.isChecked() == true) {
                                            strID = checkBox1.getText().toString();
                                            checkBox.setChecked(false);
                                        }

                                        String strPay = editTextPay.getText().toString();

                                        String strDetail = editTextDetail.getText().toString();

                                        Accountbook_item item = new Accountbook_item(strID, strPay, strDetail);

                                        mArrayList.set(getAdapterPosition(),item); //첫 줄에 삽입
                                        notifyItemChanged(getAdapterPosition());
                                        dialog.dismiss();
                                    }
                                });


                                dialog.show();

                                break;

                            case 1002:
                                mArrayList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), mArrayList.size());

                                break;

                            default:
                                throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                        }
                        return true;
                    }
                };
            }
        }
    }

}