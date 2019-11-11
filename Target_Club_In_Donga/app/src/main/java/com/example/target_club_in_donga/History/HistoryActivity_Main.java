package com.example.target_club_in_donga.History;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.BuildConfig;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.melnykov.fab.FloatingActionButton;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class HistoryActivity_Main extends AppCompatActivity {

    private ImageView activityhistory_main_imageview_export;
    private LinearLayout activityhistory_main_linearlayout;
    private FloatingActionButton activityhistory_main_button_intent;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private List<History_Item> historyList = new ArrayList<History_Item>();
    private List<String> dbKey = new ArrayList<String>();

    //private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
    private SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM-dd");
    private HistoryActivity_Main_ListViewAdapter adapter = new HistoryActivity_Main_ListViewAdapter();
    private ListView activityhistory_main_listview;
    private boolean backFlag = true;

    //private CheckBox historyCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhistory_main);

        //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        activityhistory_main_imageview_export = (ImageView)findViewById(R.id.activityhistory_main_imageview_export);
        activityhistory_main_linearlayout = (LinearLayout)findViewById(R.id.activityhistory_main_linearlayout);

        //historyYearCheckbox = (CheckBox) findViewById(R.id.historyYearCheckbox);
        //historyCheckbox = (CheckBox)findViewById(R.id.historyCheckbox);

        activityhistory_main_button_intent = (FloatingActionButton) findViewById(R.id.activityhistory_main_button_intent);

        // 리스트뷰 참조 및 Adapter달기
        activityhistory_main_listview = (ListView) findViewById(R.id.activityhistory_main_listview);

        activityhistory_main_button_intent.attachToListView(activityhistory_main_listview);
        activityhistory_main_button_intent.show();

        activityhistory_main_listview.setChoiceMode(activityhistory_main_listview.CHOICE_MODE_MULTIPLE);
        activityhistory_main_listview.setAdapter(adapter);
        //activityhistory_main_button_intent.attachToListView(activityhistory_main_listview);

        //adapter.addItem();
        //adapter.addItem();
        storage = FirebaseStorage.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        Query query = firebaseDatabase.getReference().child("EveryClub").child(clubName).child("History").orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy");
                // nowDate 변수에 값을 저장한다.
                String nowTime = (Integer.parseInt(sdfNow.format(date))+1)+"";

                dbKey.clear();
                historyList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    History_Item item = snapshot.getValue(History_Item.class);
                    item.setTimestamp(-1*((long)item.getTimestamp()));
                    String time = timeStampToString(item.getTimestamp(),simpleDateFormat2);
                    //Log.d("으악",""+time);
                    if(!(time.equals(nowTime))){
                        item.setVisYear(true);
                        nowTime = time;
                    }

                    adapter.addItem(item);
                    dbKey.add(snapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        activityhistory_main_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int positon, long l) {
                backFlag = false;
                activityhistory_main_button_intent.setVisibility(View.GONE);

                adapter.setCheckBoxState(true,true);
                activityhistory_main_linearlayout.setVisibility(View.VISIBLE);

                activityhistory_main_imageview_export.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveExcel();
                        checkBoxClose();
                    }
                });

                return false;
            }
        });

        activityhistory_main_button_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity_Main.this, HistoryActivity_Insert.class);
                startActivity(intent);
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


    public class HistoryActivity_Main_ListViewAdapter extends BaseAdapter {

        private boolean selectState = false;
        private boolean yearSelectState = false;

        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        //private ArrayList<History_Item> listViewItemList = new ArrayList<History_Item>() ;

        public void setCheckBoxState(boolean select, boolean yearSelect){
            selectState = select;
            yearSelectState = yearSelect;
            notifyDataSetChanged();
        }

        // ListViewAdapter의 생성자
        public HistoryActivity_Main_ListViewAdapter() {

        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return historyList.size() ;
        }


        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Context context = parent.getContext();
            final History_Item listViewItem = historyList.get(position);

            ListViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.history_main_listview_item, parent, false);

                holder= new ListViewHolder();;
                holder.imageview = (ImageView) convertView.findViewById(R.id.history_main_listview_item_imageview) ;
                holder.monday = (TextView) convertView.findViewById(R.id.history_main_listview_item_textview_date) ;
                holder.year = (TextView) convertView.findViewById(R.id.history_main_listview_item_textview_year) ;
                holder.content = (TextView) convertView.findViewById(R.id.history_main_listview_item_textview_content) ;
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.history_main_listview_item_checkbox) ;
                holder.checkBoxYear = (CheckBox) convertView.findViewById(R.id.history_main_listview_item_checkbox_year) ;

                convertView.setTag(holder);

            }
            else{
                holder = (ListViewHolder) convertView.getTag();
            }


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listViewItem.isSelected()){
                        listViewItem.setSelected(false);
                    }
                    else{
                        listViewItem.setSelected(true);
                    }
                    notifyDataSetChanged();
                }
            });
            holder.checkBox.setChecked(listViewItem.isSelected());

            holder.checkBoxYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listViewItem.isSelectedYear()){
                        listViewItem.setSelectedYear(false);
                        /*for(int i=0;i<historyList.size();i++){

                            if (historyList.get(i).getYear().equals(listViewItem.getYear())){
                                historyList.get(i).setSelected(false);
                            }
                        }*/

                        for(int i=0;i<historyList.size();i++){
                            String time1 = timeStampToString(historyList.get(i).getTimestamp(), simpleDateFormat2);
                            String time2 = timeStampToString(listViewItem.getTimestamp(), simpleDateFormat2);

                            if (time1.equals(time2)){
                                historyList.get(i).setSelected(false);
                            }
                        }


                    }
                    else{
                        listViewItem.setSelectedYear(true);
                        /*for(int i=0;i<historyList.size();i++){
                            if (historyList.get(i).getYear().equals(listViewItem.getYear())){
                                historyList.get(i).setSelected(true);
                            }
                        }*/
                        for(int i=0;i<historyList.size();i++){
                            String time1 = timeStampToString(historyList.get(i).getTimestamp(), simpleDateFormat2);
                            String time2 = timeStampToString(listViewItem.getTimestamp(), simpleDateFormat2);

                            if (time1.equals(time2)){
                                historyList.get(i).setSelected(true);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            holder.checkBoxYear.setChecked(listViewItem.isSelectedYear());
            // 아이템 내 각 위젯에 데이터 반영

            if(listViewItem.getImageUrl().equals("None")){
                holder.imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
            }
            else{
                Glide.with(convertView).load(listViewItem.getImageUrl()).into(holder.imageview);
            }

            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageviewDialog(position, listViewItem.getImageUrl());
                }
            });

            String time = timeStampToString(listViewItem.getTimestamp(), simpleDateFormat3);

            holder.monday.setText(time);


            time = timeStampToString(listViewItem.getTimestamp(), simpleDateFormat2);

            holder.year.setText(time);
            if(listViewItem.isVisYear()){
                holder.year.setVisibility(View.VISIBLE);
            }
            else{
                //holder.year.setText("       ");
                holder.year.setVisibility(View.INVISIBLE);
            }

            holder.content.setText(listViewItem.getContent());


            if( selectState ){
                holder.checkBox.setVisibility(View.VISIBLE);
            }
            else{
                holder.checkBox.setVisibility(View.GONE);
            }

            if(yearSelectState){
                if(listViewItem.isVisYear()){
                    holder.checkBoxYear.setVisibility(View.VISIBLE);
                }
                else{
                    holder.checkBoxYear.setVisibility(View.INVISIBLE);
                }
                //Log.d("으악",""+YEAR_PLUS);

            }
            else{
                holder.checkBoxYear.setVisibility(View.GONE);
            }
            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position ;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return historyList.get(position) ;
        }

        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(History_Item item) {
            historyList.add(item);
        }
        private class ListViewHolder{
            ImageView imageview;
            TextView monday;
            TextView year;
            TextView content;
            CheckBox checkBox ;
            CheckBox checkBoxYear;
        }

    }

    /*private void delete_content(final int position){

        //일단 스토리지 이미지 삭제 함수
        storage.getReference().child("images").child(historyList.get(position).getImageDeleteName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(BoardActivity.this, "삭제완료", Toast.LENGTH_SHORT).show();
                //디비 삭제제
                firebaseDatabase.getReference().child("History").child(dbKey.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(History_Main.this, "삭제성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(History_Main.this, "삭제실패", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    private String timeStampToString(Object timestamp, SimpleDateFormat simpleDateFormat){
        long unixTime = (long) timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(date);
    }

    private void saveExcel(){
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet(); // 새로운 시트 생성

        Row row = sheet.createRow(0); // 새로운 행 생성
        Cell cell;

        cell = row.createCell(0); // 1번 셀 생성
        cell.setCellValue("년"); // 1번 셀 값 입력

        cell = row.createCell(1); // 2번 셀 생성
        cell.setCellValue("월-일"); // 2번 셀 값 입력

        cell = row.createCell(2);
        cell.setCellValue("내용");


        for(int i = 0; i < historyList.size() ; i++){ // 데이터 엑셀에 입력
            if(historyList.get(i).isSelected()){
                row = sheet.createRow(i+1);

                cell = row.createCell(0);
                String year = timeStampToString(historyList.get(i).getTimestamp(), simpleDateFormat2);
                cell.setCellValue(year);

                cell = row.createCell(1);
                String date = timeStampToString(historyList.get(i).getTimestamp(), simpleDateFormat3);
                cell.setCellValue(date);

                cell = row.createCell(2);
                cell.setCellValue(historyList.get(i).getContent());

            }
        }

        File xlsFile = new File(getExternalFilesDir(null),"history.xls");
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
        Intent chooser = Intent.createChooser(intent, "연혁 내보내기");
        this.startActivity(chooser);

        //startActivity(Intent.createChooser(shareIntent,"엑셀 내보내기"));
    }
    private void checkBoxClose(){
        adapter.setCheckBoxState(false, false);
        activityhistory_main_linearlayout.setVisibility(View.GONE);
        for(int i=0;i<historyList.size();i++){
            historyList.get(i).setSelected(false);
            historyList.get(i).setSelectedYear(false);
        }
        adapter.notifyDataSetChanged();
        backFlag = true;
        activityhistory_main_button_intent.setVisibility(View.VISIBLE);
    }
    private void imageviewDialog(final int positon, String imageUrl){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        final View view2 = LayoutInflater.from(this)
                .inflate(R.layout.history_main_dialog, null, false);
        builder2.setView(view2);

        final ImageView exportBtn = (ImageView) view2.findViewById(R.id.history_main_dialog_imageview_export);
        final ImageView imageView = (ImageView) view2.findViewById(R.id.history_main_dialog_imageview);

        if(imageUrl.equals("None")){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
            exportBtn.setVisibility(View.INVISIBLE);
        }
        else{
            Glide.with(view2).load(imageUrl).into(imageView);
        }

        //Glide.with(itemView.getContext()).load(imageDTOs.get(getAdapterPosition()).imageUri).into(detailImageView);

        final AlertDialog dialog2 = builder2.create();

        exportBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    //final File localFile = File.createTempFile("images", "jpg");
                    final File xlsFile = new File(getExternalFilesDir(null),historyList.get(positon).getImageDeleteName());
                    //FileOutputStream os = new FileOutputStream(xlsFile);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("EveryClub").child(clubName).child("HistoryImages/"+historyList.get(positon).getImageDeleteName()).getFile(xlsFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(History_Main.this, ""+localFile.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),xlsFile.getAbsolutePath()+"에 저장되었습니다",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID,xlsFile ));
                            Intent chooser = Intent.createChooser(intent, "연혁 사진 내보내기");
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


}


