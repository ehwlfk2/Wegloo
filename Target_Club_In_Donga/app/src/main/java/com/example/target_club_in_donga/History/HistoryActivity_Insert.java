package com.example.target_club_in_donga.History;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoryActivity_Insert extends AppCompatActivity {

    private static final int GALLERY_CODE = 10;
    private ImageView activityhistory_insert_imageview;
    //private EditText histroyInsertMondayEditText;
    private EditText activityhistory_insert_edittext_content;
    private TextView activityhistory_insert_textview_date;
    private FloatingActionButton activityhistory_insert_button_insert;
    private String dateStr;
    private String historyInsertImagePath;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhistory_insert);

        /*권한*/
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        activityhistory_insert_imageview = (ImageView)findViewById(R.id.activityhistory_insert_imageview);
        activityhistory_insert_textview_date = (TextView)findViewById(R.id.activityhistory_insert_textview_date);
        activityhistory_insert_edittext_content = (EditText)findViewById(R.id.activityhistory_insert_edittext_content);
        activityhistory_insert_button_insert = (FloatingActionButton)findViewById(R.id.activityhistory_insert_button_insert);
        activityhistory_insert_button_insert.show(false);

        now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        // nowDate 변수에 값을 저장한다.
        //formatDate = //"yyyy/MM/dd HH:mm:ss"
        //activityvote_insert_textview_calendar.setText(formatDate);

        activityhistory_insert_textview_date.setText(sdfNow.format(date));
        activityhistory_insert_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        activityhistory_insert_textview_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_DatePicker();
            }
        });

        activityhistory_insert_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activityhistory_insert_edittext_content.getText().toString().equals("")){
                    Toast.makeText(HistoryActivity_Insert.this, "내용 적어", Toast.LENGTH_SHORT).show();
                }
                else{
                    upload(historyInsertImagePath);
                    Toast.makeText(HistoryActivity_Insert.this, "추가성공", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLERY_CODE){
            //String path = data.getData();
            try{
                historyInsertImagePath = getPath(data.getData());
                File f = new File(historyInsertImagePath);
                activityhistory_insert_imageview.setImageURI(Uri.fromFile(f));
            }catch (NullPointerException e){
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

    private void upload(String uri){


        try{
            StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            final Uri file = Uri.fromFile(new File(uri));
            StorageReference riversRef = storageRef.child("HistoryImages/"+file.getLastPathSegment());
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

                    History_Item itemDTO = new History_Item();
                    itemDTO.setImageUrl(downloadUrl.toString());
                    //itemDTO.setMonday(histroyInsertMondayEditText.getText().toString());

                    itemDTO.setContent(activityhistory_insert_edittext_content.getText().toString());
                    long tt = dateToMills(activityhistory_insert_textview_date.getText().toString());
                    itemDTO.setTimestamp(-1*tt);
                    //itemDTO.setYear(historyInsertYearEditText.getText().toString());
                    itemDTO.setSelected(false);
                    itemDTO.setSelectedYear(false);
                    itemDTO.setVisYear(false);

                    //itemDTO.uid = auth.getCurrentUser().getUid();
                    //itemDTO.userid = auth.getCurrentUser().getEmail();
                    itemDTO.setImageDeleteName(file.getLastPathSegment());

                    firebaseDatabase.getReference().child("History").push().setValue(itemDTO);
                }
            });
        }catch (NullPointerException e){
            History_Item itemDTO = new History_Item();
            itemDTO.setImageUrl("None");
            //itemDTO.setMonday(histroyInsertMondayEditText.getText().toString());
            itemDTO.setContent(activityhistory_insert_edittext_content.getText().toString());
            long tt = dateToMills(activityhistory_insert_textview_date.getText().toString());
            itemDTO.setTimestamp(-1*tt);
            //itemDTO.setYear(historyInsertYearEditText.getText().toString());
            itemDTO.setSelected(false);
            itemDTO.setSelectedYear(false);
            itemDTO.setVisYear(false);

            //itemDTO.uid = auth.getCurrentUser().getUid();
            //itemDTO.userid = auth.getCurrentUser().getEmail();
            itemDTO.setImageDeleteName("None");

            firebaseDatabase.getReference().child("History").push().setValue(itemDTO);
        }

    }

    private void Dialog_DatePicker(){
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

                activityhistory_insert_textview_date.setText(dateStr);
                //Toast.makeText(Vote_Insert.this, dateStr, Toast.LENGTH_SHORT).show();
            }
        };

        DatePickerDialog alert = new DatePickerDialog(this,mDateSetListener,cyear,cmonth,cday);
        alert.getDatePicker().setMaxDate(new Date().getTime());
        alert.show();

        //return dateStr;
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

}
