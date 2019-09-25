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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AccountBookActivity_Main extends AppCompatActivity {
    private AccountBookActivity_Main_RecyclerviewAdapter adapter;
    private FloatingActionButton activity_accountbook_main_intent, activity_accountbook_main_today_intent, activity_accountbook_main_everyday_intent;
    private RecyclerView activity_accountbook_recyclerview;
    private TextView activity_accountbook_main_totalPrice, activity_accountbook_main_textview;
    private ArrayList<AccountBook_Main_Item> list = new ArrayList<>();//ItemFrom을 통해 받게되는 데이터를 어레이 리스트화 시킨다.
    private ArrayList<String> dbKey = new ArrayList<String>();
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String accountInsertImagePath;
    private static final int GALLERY_CODE = 10;
    private ImageView account_dialog_imageview;
    private String dateStr;

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
        activity_accountbook_main_intent.attachToRecyclerView(activity_accountbook_recyclerview);
        activity_accountbook_main_intent.show();
        activity_accountbook_main_everyday_intent.show(false);
        activity_accountbook_main_today_intent.show(false);
        activity_accountbook_main_everyday_intent.setColorFilter(getResources().getColor(R.color.fbutton_color_peter_river));
        activity_accountbook_main_today_intent.setColorFilter(getResources().getColor(R.color.fbutton_color_peter_river));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        activity_accountbook_recyclerview.setHasFixedSize(true);//각 아이템이 보여지는 것을 일정하게
        activity_accountbook_recyclerview.setLayoutManager(new LinearLayoutManager(this));//앞서 선언한 리싸이클러뷰를 레이아웃메니저에 붙힌다

        adapter = new AccountBookActivity_Main_RecyclerviewAdapter(this, list);//앞서 만든 리스트를 어뎁터에 적용시켜 객체를 만든다.
        activity_accountbook_recyclerview.setAdapter(adapter);// 그리고 만든 겍체를 리싸이클러뷰에 적용시킨다.

        everyDayDB();

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

                //final CheckBox account_dialog_checkbox_pay = v.findViewById(R.id.account_dialog_checkbox_pay);
                //final CheckBox account_dialog_checkbox_income = v.findViewById(R.id.account_dialog_checkbox_income);
                final RadioGroup radioGroup = v.findViewById(R.id.account_dialog_radioGroup);

                LinearLayout account_dialog_layout_date = v.findViewById(R.id.account_dialog_layout_date);
                final TextView account_dialog_textview_date = v.findViewById(R.id.account_dialog_textview_date);
                FloatingActionButton account_dialog_button_result = v.findViewById(R.id.account_dialog_button_result);

                final AlertDialog dialog = builder.create();

                long unixTime = (long) System.currentTimeMillis();
                final Date date = new Date(unixTime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String time = simpleDateFormat.format(date);
                account_dialog_textview_date.setText(time);

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

                            upload(accountInsertImagePath,account_dialog_textview_date.getText().toString(),name, price, priceId);
                            Toast.makeText(AccountBookActivity_Main.this, "완료!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        database.getReference().child("AccountBook").child("totalPrice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    int totalPrice = dataSnapshot.getValue(int.class);
                    activity_accountbook_main_totalPrice.setText("자산 : "+totalPrice+"원");
                }catch(NullPointerException e){
                    activity_accountbook_main_totalPrice.setText("자산 : "+0+"원");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public class AccountBookActivity_Main_RecyclerviewAdapter extends RecyclerView.Adapter<AccountBookActivity_Main.AccountBookActivity_Main_RecyclerviewAdapter.MyViewholder> {

        private Activity activity;
        private ArrayList<AccountBook_Main_Item> datalist;
        //getItemCount, onCreateViewHolder, MyViewHolder, onBindViewholder 순으로 들어오게 된다.
        // 뷰홀더에서 초기세팅해주고 바인드뷰홀더에서 셋텍스트해주는 값이 최종적으로 화면에 출력되는 값

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

            holder.accountPrice.setText(data.getPrice()+"원");
            if(data.getImageUrl().equals("None")){
                holder.accountImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
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

        }
        public void delete_item(final int position){
            database = FirebaseDatabase.getInstance();
            database.getReference().child("AccountBook").child(dbKey.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
            TextView accountPriceId;
            TextView accountPrice;
            TextView accountDate;
            TextView accountTitle;
            ImageView accountImage;
            LinearLayout accountLayout;

            public MyViewholder(final View v){
                super(v);
                accountLayout = v.findViewById(R.id.accountbook_main_item_linearlayout);
                accountPriceId = v.findViewById(R.id.accountbook_main_item_priceId);
                accountPrice = v.findViewById(R.id.accountbook_main_item_price);
                accountTitle = v.findViewById(R.id.accountbook_main_item_title);
                accountDate = v.findViewById(R.id.accountbook_main_item_date);
                accountImage = v.findViewById(R.id.accountbook_main_item_imageview);
            }
        }
        public AccountBookActivity_Main_RecyclerviewAdapter(Activity activity, ArrayList<AccountBook_Main_Item> datalist){
            this.activity = activity;//보여지는 액티비티
            this.datalist = datalist;//내가 처리하고자 하는 아이템들의 리스트
        }

        /*public void PopupMenu(final AccountBookActivity_Main.AccountBookActivity_Main_RecyclerviewAdapter.MyViewholder holder, final int position){
            holder.accountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete_item(position);
                    PopupMenu popup = new PopupMenu(view.getContext(), view);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.accountbook_delete:
                                    //delete_item(position);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.inflate(R.menu.accountbook_menu);
//                    if(!adminCheck){
//                        popup.getMenu().getItem(2).setVisible(false);
//                        popup.getMenu().getItem(3).setVisible(false);
//                    }
                    popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                    popup.show();
                }
            });
        }*/
    }
    private void imageviewDialog(final int positon, String imageUrl){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        final View view2 = LayoutInflater.from(this)
                .inflate(R.layout.account_main_image_dialog, null, false);
        builder2.setView(view2);

        final ImageView exportBtn = (ImageView) view2.findViewById(R.id.account_main_image_dialog_imageview_export);
        final ImageView imageView = (ImageView) view2.findViewById(R.id.account_main_image_dialog_imageview);

        if(imageUrl.equals("None")){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
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
                    storageRef.child("AccountBookImages/"+list.get(positon).getImageDeleteName()).getFile(xlsFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(History_Main.this, ""+localFile.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),xlsFile.getAbsolutePath()+"에 저장되었습니다",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID,xlsFile ));
                            Intent chooser = Intent.createChooser(intent, "이미지 내보내기");
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

        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if(month < 10)
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
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String viewDate;
                if(month < 10)
                    viewDate = year+"-0"+(month+1)+"-";
                else
                    viewDate = year+"-"+(month+1)+"-";

                if(day < 10)
                    viewDate += ("0"+day);
                else
                    viewDate += day;
                activity_accountbook_main_textview.setText(viewDate);

                String date = year+"-"+(month+1)+"-"+day;
                long dately = dateToMills(date);
                //Toast.makeText(AccountBookActivity_Main.this, ""+datel, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Vote_Insert.this, dateStr, Toast.LENGTH_SHORT).show();
                database.getReference().child("AccountBook").orderByChild("timestamp").equalTo(-1*dately).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        dbKey.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            AccountBook_Main_Item item = snapshot.getValue(AccountBook_Main_Item.class);
                            item.setTimestamp(-1*(long)item.getTimestamp());
                            list.add(item);
                            dbKey.add(snapshot.getKey());
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            final Uri file = Uri.fromFile(new File(uri));
            StorageReference riversRef = storageRef.child("AccountBookImages/"+file.getLastPathSegment());
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
                    itemDTO.setImageDeleteName(file.getLastPathSegment()+System.currentTimeMillis());
                    long tt = dateToMills(time);
                    itemDTO.setTimestamp(-1*tt);
                    itemDTO.setTitle(title);
                    itemDTO.setPrice(price);
                    itemDTO.setPriceId(priceId);
                    accountInsertImagePath = null;
                    database.getReference().child("AccountBook").push().setValue(itemDTO);
                }
            });
        }catch (NullPointerException e){

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

            //itemDTO.uid = auth.getCurrentUser().getUid();
            //itemDTO.userid = auth.getCurrentUser().getEmail();

            database.getReference().child("AccountBook").push().setValue(itemDTO);
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
        activity_accountbook_main_textview.setText("Every day");
        database.getReference().child("AccountBook").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)                                                                                                                               {
                list.clear();
                dbKey.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AccountBook_Main_Item item = snapshot.getValue(AccountBook_Main_Item.class);
                    item.setTimestamp(-1*(long)item.getTimestamp());
                    list.add(item);
                    dbKey.add(snapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
