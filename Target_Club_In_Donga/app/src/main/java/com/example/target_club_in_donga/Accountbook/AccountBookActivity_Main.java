package com.example.target_club_in_donga.Accountbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.BuildConfig;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Execute;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.example.target_club_in_donga.Vote.VoteActivity_Result;
import com.example.target_club_in_donga.Vote.Vote_Item_Main;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.melnykov.fab.FloatingActionButton;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AccountBookActivity_Main extends AppCompatActivity {
    private AccountBookActivity_Main_RecyclerviewAdapter adapter;
    private FloatingActionButton activity_accountbook_main_intent, activity_accountbook_main_today_intent, activity_accountbook_main_everyday_intent,
            activity_accountbook_main_export, activity_accountbook_main_delete;
    private RecyclerView activity_accountbook_recyclerview;
    private TextView activity_accountbook_main_totalPrice, activity_accountbook_main_textview;
    private ArrayList<AccountBook_Main_Item> list = new ArrayList<>();//ItemFrom을 통해 받게되는 데이터를 어레이 리스트화 시킨다.
    private ArrayList<String> dbKey = new ArrayList<>();
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private String accountInsertImagePath;
    private static final int GALLERY_CODE = 10;
    private ImageView account_dialog_imageview;
    private String dateStr;
    private CheckBox activity_accountbook_main_checkbox;
    private boolean backFlag = true;
    private Map<String,String> deleteMap = new HashMap<>();
    private PullRefreshLayout activity_accountbook_main_reLayout;
    private DecimalFormat editTextDF = new DecimalFormat("###,###.####");
    private String editTextTempResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountbook_main);
        activity_accountbook_main_totalPrice = findViewById(R.id.activity_accountbook_main_totalPrice);
        activity_accountbook_main_intent = findViewById(R.id.activity_accountbook_main_intent);
        activity_accountbook_main_today_intent = findViewById(R.id.activity_accountbook_main_today_intent);
        activity_accountbook_main_everyday_intent = findViewById(R.id.activity_accountbook_main_everyday_intent);
        activity_accountbook_recyclerview = findViewById(R.id.activity_accountbook_main_recyclerview);
        activity_accountbook_main_textview = findViewById(R.id.activity_accountbook_main_textview);
        activity_accountbook_main_checkbox = findViewById(R.id.activity_accountbook_main_allChbox);
        activity_accountbook_main_export = findViewById(R.id.activity_accountbook_main_export);
        activity_accountbook_main_delete = findViewById(R.id.activity_accountbook_main_delete);
        activity_accountbook_main_intent.attachToRecyclerView(activity_accountbook_recyclerview);
        activity_accountbook_main_intent.show();

        activity_accountbook_main_everyday_intent.show(false);
        activity_accountbook_main_today_intent.show(false);
        activity_accountbook_main_export.show(false);
        activity_accountbook_main_delete.show(false);
        activity_accountbook_main_everyday_intent.setColorFilter(getResources().getColor(R.color.colorWhite));
        activity_accountbook_main_today_intent.setColorFilter(getResources().getColor(R.color.colorWhite));
        activity_accountbook_main_export.setColorFilter(getResources().getColor(R.color.colorWhite));
        activity_accountbook_main_delete.setColorFilter(getResources().getColor(R.color.colorWhite));

        activity_accountbook_main_reLayout = findViewById(R.id.activity_accountbook_main_reLayout);
        activity_accountbook_main_reLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        activity_accountbook_recyclerview.setHasFixedSize(true);//각 아이템이 보여지는 것을 일정하게
        activity_accountbook_recyclerview.setLayoutManager(new LinearLayoutManager(this));//앞서 선언한 리싸이클러뷰를 레이아웃메니저에 붙힌다

        adapter = new AccountBookActivity_Main_RecyclerviewAdapter(this, list);//앞서 만든 리스트를 어뎁터에 적용시켜 객체를 만든다.
        activity_accountbook_recyclerview.setAdapter(adapter);// 그리고 만든 겍체를 리싸이클러뷰에 적용시킨다.

        everyDayDB();

        activity_accountbook_main_reLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dbCheck();
                activity_accountbook_main_reLayout.setRefreshing(false);
            }
        });

        activity_accountbook_main_today_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //list.clear();
                Dialog_DatePicker2();
            }
        });
        activity_accountbook_main_everyday_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                everyDayDB();
            }
        });
        activity_accountbook_main_delete.setOnClickListener(new View.OnClickListener() { //롱클릭 중일때 삭제
            @Override
            public void onClick(View view) {
                deleteMap.clear();
                for(int i = 0;i<list.size();i++){
                    if(list.get(i).isSelected()){
                        deleteMap.put(dbKey.get(i),list.get(i).getImageDeleteName());
                    }
                }
                for( String key : deleteMap.keySet() ){
                    //System.out.println( String.format("키 : %s, 값 : %s", key, map.get(key)) );
                    delete_item(key, deleteMap.get(key));
                    //Log.e(key+"",deleteMap.get(key)+"");
                }
                checkBoxClose();
                Toast.makeText(AccountBookActivity_Main.this, "삭제완료", Toast.LENGTH_SHORT).show();
            }
        });
        activity_accountbook_main_export.setOnClickListener(new View.OnClickListener() { //롱클릭 중일때 내보내기
            @Override
            public void onClick(View view) {
                //내보내고
                saveExcel();
                checkBoxClose(); //닫고
            }
        });

        activity_accountbook_main_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity_accountbook_main_checkbox.isChecked()){
                    adapter.setRealSelect(true);
                }
                else{
                    adapter.setRealSelect(false);
                }
            }
        });

        activity_accountbook_main_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountBookActivity_Main.this);
                final View v = LayoutInflater.from(AccountBookActivity_Main.this)
                        .inflate(R.layout.account_main_insert_dialog, null, false);
                builder.setView(v);

                account_dialog_imageview = v.findViewById(R.id.account_dialog_imageview);
                final EditText account_dialog_edittext_name = v.findViewById(R.id.account_dialog_edittext_name);
                final EditText account_dialog_edittext_price = v.findViewById(R.id.account_dialog_edittext_price);

                account_dialog_edittext_price.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!s.toString().equals(editTextTempResult)){     // StackOverflow를 막기위해,
                            try{
                                editTextTempResult = editTextDF.format(Long.parseLong(s.toString().replaceAll(",", "")));   // 에딧텍스트의 값을 변환하여, result에 저장.
                                account_dialog_edittext_price.setText(editTextTempResult);    // 결과 텍스트 셋팅.
                                account_dialog_edittext_price.setSelection(editTextTempResult.length());     // 커서를 제일 끝으로 보냄.
                            }
                            catch (NumberFormatException e){

                            }


                            /*editTextTempResult = editTextDF.format(Double.parseDouble(s.toString().replaceAll(",","")));
                            account_dialog_edittext_price.setText(editTextTempResult);
                            account_dialog_edittext_price.setSelection(editTextTempResult.length());*/

                        }
                    }
                });


                //final CheckBox account_dialog_checkbox_pay = v.findViewById(R.id.account_dialog_checkbox_pay);
                //final CheckBox account_dialog_checkbox_income = v.findViewById(R.id.account_dialog_checkbox_income);
                final RadioGroup radioGroup = v.findViewById(R.id.account_dialog_radioGroup);

                LinearLayout account_dialog_layout_date = v.findViewById(R.id.account_dialog_layout_date);
                final TextView account_dialog_textview_date = v.findViewById(R.id.account_dialog_textview_date);
                FloatingActionButton account_dialog_button_result = v.findViewById(R.id.account_dialog_button_result);

                final AlertDialog dialog = builder.create();

                if(activity_accountbook_main_textview.getText().toString().equals("Every day")){
                    String time = millsToString(System.currentTimeMillis());
                    account_dialog_textview_date.setText(time);
                }
                else{
                    account_dialog_textview_date.setText(activity_accountbook_main_textview.getText().toString());
                }


                account_dialog_layout_date.setOnClickListener(new View.OnClickListener() { //날짜 다이얼로그
                    @Override
                    public void onClick(View view) {
                        Dialog_DatePicker(account_dialog_textview_date);
                        //account_dialog_textview_date.setText(Dialog_DatePicker());
                    }
                });

                account_dialog_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent,GALLERY_CODE);
                    }
                });

                account_dialog_button_result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = account_dialog_edittext_name.getText().toString();
                        String price = account_dialog_edittext_price.getText().toString();
                        if(name.length()==0){
                            Toast.makeText(AccountBookActivity_Main.this, "내역 입력 부탁", Toast.LENGTH_SHORT).show();
                        }
                        else if(price.length()==0){
                            Toast.makeText(AccountBookActivity_Main.this, "금액 입력 부탁", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            int radioId = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = v.findViewById(radioId);
                            String priceId;
                            if(radioButton.getText().toString().equals("지출")){
                                priceId = "pay";
                            }
                            else{
                                priceId = "income";
                            }
                            Log.e("리졸트 확인",editTextTempResult+"");
                            upload(accountInsertImagePath,account_dialog_textview_date.getText().toString(),name, price, priceId);

                            Toast.makeText(AccountBookActivity_Main.this, "완료!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        database.getReference().child("EveryClub").child(clubName).child("AccountBook_totalPrice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    long totalPrice = dataSnapshot.getValue(long.class);
                    String total = String.format("%,d", totalPrice);
                    activity_accountbook_main_totalPrice.setText("자산 : "+total+" 원");
                }catch(NullPointerException e){
                    activity_accountbook_main_totalPrice.setText("자산 : "+0+" 원");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        if(backFlag){
            super.onBackPressed();
        }
        else{
            checkBoxClose();
        }
    }

    public class AccountBookActivity_Main_RecyclerviewAdapter extends RecyclerView.Adapter<AccountBookActivity_Main.AccountBookActivity_Main_RecyclerviewAdapter.MyViewholder> {

        private Activity activity;
        private ArrayList<AccountBook_Main_Item> datalist;
        //getItemCount, onCreateViewHolder, MyViewHolder, onBindViewholder 순으로 들어오게 된다.
        // 뷰홀더에서 초기세팅해주고 바인드뷰홀더에서 셋텍스트해주는 값이 최종적으로 화면에 출력되는 값
        private boolean selectState = false;
        private boolean realSelect = false;
        public void setCheckBoxState(boolean select){
            selectState = select;
            notifyDataSetChanged();
        }
        public void setRealSelect(boolean select){
            realSelect = select;
            notifyDataSetChanged();
        }

        @Override
        public AccountBookActivity_Main.AccountBookActivity_Main_RecyclerviewAdapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acountbook_main_item, parent, false);//뷰 생성(아이템 레이아웃을 기반으로)
            AccountBookActivity_Main.AccountBookActivity_Main_RecyclerviewAdapter.MyViewholder viewholder1 = new AccountBookActivity_Main.AccountBookActivity_Main_RecyclerviewAdapter.MyViewholder(view);//아이템레이아웃을 기반으로 생성된 뷰를 뷰홀더에 인자로 넣어줌
            return viewholder1;
        }

        @Override
        public void onBindViewHolder(final AccountBookActivity_Main.AccountBookActivity_Main_RecyclerviewAdapter.MyViewholder holder, final int position) {

            final AccountBook_Main_Item data = datalist.get(position);//위치에 따라서 그에 맞는 데이터를 얻어오게 한다.
            holder.accountTitle.setText(data.getTitle());//앞서 뷰홀더에 세팅해준 것을 각 위치에 맞는 것들로 보여주게 하기 위해서 세팅해준다.
            //holder.profile.setImageResource(data.getImageNumber());

            //holder.voteDate.setText(data.getTime());
            long unixTime = (long) data.getTimestamp();
            Date date = new Date(unixTime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            holder.accountDate.setText(time);

            if(data.getPriceId().equals("pay")){
                holder.accountPriceId.setText("지출");
                holder.accountPriceId.setTextColor(getResources().getColor(R.color.fbutton_color_alizarin));
            }
            else{
                holder.accountPriceId.setText("수입");
                holder.accountPriceId.setTextColor(getResources().getColor(R.color.fbutton_color_belize_hole));
            }

            holder.accountPrice.setText(data.getPrice()+" 원");
            if(data.getImageUrl().equals("None")){
                //holder.accountImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                //holder.accountImage.setVisibility(View.GONE);
            }
            else{
                Glide.with(activity).load(data.getImageUrl()).into(holder.accountImage);
            }
            holder.accountImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageviewDialog(position, data.getImageUrl());
                }
            });
            //PopupMenu(holder,position);
            holder.accountLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setCheckBoxState(true);
                    backFlag = false;
                    return false;

                }

            });

            if(selectState){
                activity_accountbook_main_checkbox.setVisibility(View.VISIBLE);
                //holder.accountImage.setVisibility(View.GONE);
                holder.accountCheckbox.setVisibility(View.VISIBLE);
                activity_accountbook_main_intent.setVisibility(View.GONE);
                activity_accountbook_main_today_intent.setVisibility(View.GONE);
                activity_accountbook_main_everyday_intent.setVisibility(View.GONE);
                activity_accountbook_main_export.setVisibility(View.VISIBLE);
                activity_accountbook_main_delete.setVisibility(View.VISIBLE);
            }
            else{
                activity_accountbook_main_checkbox.setVisibility(View.INVISIBLE);
                //holder.accountImage.setVisibility(View.VISIBLE);
                holder.accountCheckbox.setVisibility(View.GONE);
                activity_accountbook_main_intent.setVisibility(View.VISIBLE);
                activity_accountbook_main_today_intent.setVisibility(View.VISIBLE);
                activity_accountbook_main_everyday_intent.setVisibility(View.VISIBLE);
                activity_accountbook_main_export.setVisibility(View.GONE);
                activity_accountbook_main_delete.setVisibility(View.GONE);
            }

            holder.accountCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(list.get(position).isSelected()){
                        list.get(position).setSelected(false);
                    }
                    else{
                        list.get(position).setSelected(true);
                    }
                }
            });
            holder.accountCheckbox.setChecked(list.get(position).isSelected());

            if(realSelect){
                holder.accountCheckbox.setChecked(true);
                list.get(position).setSelected(true);
                //list.get(position).setSelected(false);
            }
            else{
                holder.accountCheckbox.setChecked(false);
                list.get(position).setSelected(false);
                //list.get(position).setSelected(true);
            }
        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }
        public class MyViewholder extends RecyclerView.ViewHolder {
            //ImageView profile;
            TextView accountPriceId;
            TextView accountPrice;
            TextView accountDate;
            TextView accountTitle;
            ImageView accountImage;
            LinearLayout accountLayout;
            CheckBox accountCheckbox;

            public MyViewholder(final View v){
                super(v);
                accountLayout = v.findViewById(R.id.accountbook_main_item_linearlayout);
                accountPriceId = v.findViewById(R.id.accountbook_main_item_priceId);
                accountPrice = v.findViewById(R.id.accountbook_main_item_price);
                accountTitle = v.findViewById(R.id.accountbook_main_item_title);
                accountDate = v.findViewById(R.id.accountbook_main_item_date);
                accountImage = v.findViewById(R.id.accountbook_main_item_imageview);
                accountCheckbox = v.findViewById(R.id.accountbook_main_item_chbox);
            }
        }
        public AccountBookActivity_Main_RecyclerviewAdapter(Activity activity, ArrayList<AccountBook_Main_Item> datalist){
            this.activity = activity;//보여지는 액티비티
            this.datalist = datalist;//내가 처리하고자 하는 아이템들의 리스트
        }
    }
    public void delete_item(final String valueKey, final String storageKey){
        if(storageKey.equals("None")){
            database.getReference().child("EveryClub").child(clubName).child("AccountBook").child(valueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {

                }
            });
        }
        else{
            storage.getReference().child("EveryClub").child(clubName).child("AccountBookImages").child(storageKey).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {
                    Toast.makeText(AccountBookActivity_Main.this, "스토리지 삭제실패", Toast.LENGTH_SHORT).show();
                }
            });
            database.getReference().child("EveryClub").child(clubName).child("AccountBook").child(valueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {
                    Toast.makeText(AccountBookActivity_Main.this, "DB 삭제실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
        dbCheck();
    }
    private void imageviewDialog(final int positon, String imageUrl){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        final View view2 = LayoutInflater.from(this)
                .inflate(R.layout.account_main_image_dialog, null, false);
        builder2.setView(view2);

        final ImageView exportBtn = view2.findViewById(R.id.account_main_image_dialog_imageview_export);
        final ImageView imageView = view2.findViewById(R.id.account_main_image_dialog_imageview);

        if(imageUrl.equals("None")){
            //imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
            exportBtn.setVisibility(View.INVISIBLE);
        }
        else{
            Glide.with(view2).load(imageUrl).into(imageView);
        }
        //Toast.makeText(this, ""+imageUrl, Toast.LENGTH_SHORT).show();
        final AlertDialog dialog2 = builder2.create();

        exportBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    //final File localFile = File.createTempFile("images", "jpg");
                    final File xlsFile = new File(getExternalFilesDir(null),list.get(positon).getImageDeleteName());
                    //FileOutputStream os = new FileOutputStream(xlsFile);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("EveryClub").child(clubName).child("AccountBookImages/"+list.get(positon).getImageDeleteName()).getFile(xlsFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(History_Main.this, ""+localFile.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),xlsFile.getAbsolutePath()+"에 저장되었습니다",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID,xlsFile ));
                            Intent chooser = Intent.createChooser(intent, "가계부 사진 내보내기");
                            getApplicationContext().startActivity(chooser);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }catch (Exception e){

                }

            }
        });
        dialog2.show();
    }
    private void Dialog_DatePicker(final TextView textView){

        int cyear;
        int cmonth;
        int cday;
        String ac = textView.getText().toString();
        if(ac.equals("Every day")){
            Calendar c = Calendar.getInstance();
            cyear = c.get(Calendar.YEAR);
            cmonth = c.get(Calendar.MONTH);
            cday = c.get(Calendar.DAY_OF_MONTH);
        }
        else{
            cyear = Integer.parseInt(ac.substring(0,4));
            cmonth = Integer.parseInt(ac.substring(5,7))-1;
            cday = Integer.parseInt(ac.substring(8,10));
        }

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

                textView.setText(dateStr);
                //Toast.makeText(Vote_Insert.this, dateStr, Toast.LENGTH_SHORT).show();
            }
        };
        DatePickerDialog alert = new DatePickerDialog(this,mDateSetListener,cyear,cmonth,cday);
        alert.getDatePicker().setMaxDate(new Date().getTime());
        alert.show();

    }

    private void Dialog_DatePicker2(){
        int cyear;
        int cmonth;
        int cday;
        String ac = activity_accountbook_main_textview.getText().toString();
        if(ac.equals("Every day")){
            Calendar c = Calendar.getInstance();
            cyear = c.get(Calendar.YEAR);
            cmonth = c.get(Calendar.MONTH);
            cday = c.get(Calendar.DAY_OF_MONTH);
        }
        else{

            cyear = Integer.parseInt(ac.substring(0,4));
            cmonth = Integer.parseInt(ac.substring(5,7))-1;
            cday = Integer.parseInt(ac.substring(8,10));
        }


        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String viewDate;
                if(month < 9)
                    viewDate = year+"-0"+(month+1)+"-";
                else
                    viewDate = year+"-"+(month+1)+"-";

                if(day < 10)
                    viewDate += ("0"+day);
                else
                    viewDate += day;
                activity_accountbook_main_textview.setText(viewDate);

                String date = year+"-"+(month+1)+"-"+day;

                //Toast.makeText(AccountBookActivity_Main.this, ""+datel, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Vote_Insert.this, dateStr, Toast.LENGTH_SHORT).show();
                dayDB(date);
            }
        };

        DatePickerDialog alert = new DatePickerDialog(this,mDateSetListener,cyear,cmonth,cday);
        alert.getDatePicker().setMaxDate(new Date().getTime());
        alert.show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            //String path = data.getData();
            try {
                accountInsertImagePath = getPath(data.getData());
                File f = new File(accountInsertImagePath);
                account_dialog_imageview.setImageURI(Uri.fromFile(f));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }

    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);

    }

    private void upload(String uri, final String time, final String title, final String price, final String priceId){

        try{
            StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            final long flagTime = System.currentTimeMillis();
            final Uri file = Uri.fromFile(new File(uri));
            accountInsertImagePath = null;
            StorageReference riversRef = storageRef.child("EveryClub").child(clubName).child("AccountBookImages/"+file.getLastPathSegment()+flagTime);
            UploadTask uploadTask = riversRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { //이미지가 먼저 일단 올라가고 그게완료대면 디비에 패스올려줄꺼
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    AccountBook_Main_Item itemDTO = new AccountBook_Main_Item();
                    itemDTO.setImageUrl(downloadUrl.toString());
                    itemDTO.setImageDeleteName(file.getLastPathSegment()+flagTime);
                    long tt = dateToMills(time);
                    itemDTO.setTimestamp(-1*tt);
                    itemDTO.setTitle(title);
                    itemDTO.setPrice(price);
                    itemDTO.setPriceId(priceId);
                    //accountInsertImagePath = null;
                    database.getReference().child("EveryClub").child(clubName).child("AccountBook").push().setValue(itemDTO);
                    activity_accountbook_main_textview.setText(time);
                    dbCheck();
                }
            });
        }catch (NullPointerException e){
            accountInsertImagePath = null;
            AccountBook_Main_Item itemDTO = new AccountBook_Main_Item();
            itemDTO.setImageUrl("None");
            itemDTO.setImageDeleteName("None");
            //itemDTO.setMonday(histroyInsertMondayEditText.getText().toString());
            long tt = dateToMills(time);
            itemDTO.setTimestamp(-1*tt);
            //itemDTO.setYear(historyInsertYearEditText.getText().toString());
            itemDTO.setTitle(title);
            itemDTO.setPrice(price);
            itemDTO.setPriceId(priceId);
            //accountInsertImagePath = null;
            //itemDTO.uid = auth.getCurrentUser().getUid();
            //itemDTO.userid = auth.getCurrentUser().getEmail();
            database.getReference().child("EveryClub").child(clubName).child("AccountBook").push().setValue(itemDTO);
            activity_accountbook_main_textview.setText(time);
            dbCheck();
        }

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
    private void everyDayDB(){
        database.getReference().child("EveryClub").child(clubName).child("AccountBook").orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)                                                                                                                               {
                list.clear();
                dbKey.clear();
                String today = millsToString(System.currentTimeMillis());
                int index = 0;
                int plusIndex = 0;
                //Log.e("today",""+today);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AccountBook_Main_Item item = snapshot.getValue(AccountBook_Main_Item.class);
                    item.setTimestamp(-1*(long)item.getTimestamp());
                    String dbTime = millsToString((long)item.getTimestamp());
                    //Log.e(item.getTitle()+"",dbTime+"");
                    if(today.equals(dbTime)){
                        list.add(index,item);
                        dbKey.add(index,snapshot.getKey());
                        plusIndex++;
                    }
                    else{
                        today = dbTime;
                        index = plusIndex;
                        plusIndex++;
                        list.add(index,item);
                        dbKey.add(index,snapshot.getKey());
                    }
                }
                activity_accountbook_main_textview.setText("Every day");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void dayDB(String date){
        long dately = dateToMills(date);
        database.getReference().child("EveryClub").child(clubName).child("AccountBook").orderByChild("timestamp").equalTo(-1*dately).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                dbKey.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AccountBook_Main_Item item = snapshot.getValue(AccountBook_Main_Item.class);
                    item.setTimestamp(-1*(long)item.getTimestamp());
                    list.add(0,item);
                    dbKey.add(0,snapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkBoxClose(){
        adapter.setCheckBoxState(false);
        /*for(int i=0;i<list.size();i++){
            list.get(i).setSelected(false);
        }*/
        adapter.setRealSelect(false);
        activity_accountbook_main_checkbox.setChecked(false);
        adapter.notifyDataSetChanged();
        backFlag = true;
    }
    private String millsToString(long unixTime){
        //long unixTime = (long) System.currentTimeMillis();
        final Date date = new Date(unixTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(date);
    }
    private void dbCheck(){
        String ac = activity_accountbook_main_textview.getText().toString();
        if(ac.equals("Every day")){
            everyDayDB();
        }
        else{
            dayDB(ac);
        }
    }
    private void saveExcel(){
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

        Row row = sheet.createRow(0); // 새로운 행 생성
        Cell cell;

        cell = row.createCell(0); // 1번 셀 생성
        cell.setCellValue("년-월-일"); // 1번 셀 값 입력

        cell = row.createCell(1); // 2번 셀 생성
        cell.setCellValue("수입/지출"); // 2번 셀 값 입력

        cell = row.createCell(2);
        cell.setCellValue("내역");

        cell = row.createCell(3);
        cell.setCellValue("금액");


        for(int i = 0; i < list.size() ; i++){ // 데이터 엑셀에 입력
            if(list.get(i).isSelected()){
                row = sheet.createRow(i+1);

                cell = row.createCell(0);
                //String year = timeStampToString(historyList.get(i).getTimestamp(), simpleDateFormat2);
                cell.setCellValue(millsToString((long)list.get(i).getTimestamp()));

                cell = row.createCell(1);
                //String date = timeStampToString(historyList.get(i).getTimestamp(), simpleDateFormat3);
                if(list.get(i).getPriceId().equals("pay"))
                    cell.setCellValue("지출");
                else
                    cell.setCellValue("수입");


                cell = row.createCell(2);
                cell.setCellValue(list.get(i).getTitle());

                cell = row.createCell(3);
                cell.setCellValue(list.get(i).getPrice());

            }
        }

        File xlsFile = new File(getExternalFilesDir(null),"AccountBook.xls");
        try{
            FileOutputStream os = new FileOutputStream(xlsFile);
            workbook.write(os); // 외부 저장소에 엑셀 파일 생성
        }catch (IOException e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),xlsFile.getAbsolutePath()+"에 저장되었습니다",Toast.LENGTH_SHORT).show();

        //나중에 API마다 따로 해줘야함 tryCatch 걸어주던지
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/excel");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, xlsFile));
        Intent chooser = Intent.createChooser(intent, "가계부 내보내기");
        this.startActivity(chooser);

        //startActivity(Intent.createChooser(shareIntent,"엑셀 내보내기"));
    }
}
