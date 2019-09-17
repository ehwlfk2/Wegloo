package com.example.target_club_in_donga.Material_Management;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MaterialManagementActivity_Admin extends AppCompatActivity {

    int y = 0, m = 0, d = 0, h = 0, mi = 0;
    Calendar calendar = Calendar.getInstance();

    private static final int GALLARY_CODE = 10;

    RecyclerView activity_material_management_admin_recyclerview_main_list;
    List<MaterialManagement_Admin_Item> materialManagementItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    protected ImageView activity_material_management_admin_item_imageview_recyclerview_image;
    private long now;
    private String formatDate, formatHour, formatMin, startDate;
    private String dateStr,timeStr, date_Now, date_End, date_Return;

    private int flag1 = 0, flag2 = 0;
    private String findkey;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_admin);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>


        this.activity_material_management_admin_item_imageview_recyclerview_image = (ImageView) findViewById(R.id.activity_material_management_admin_item_imageview_recyclerview_image);

        activity_material_management_admin_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_material_management_admin_recyclerview_main_list);
        activity_material_management_admin_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final MaterialManagementActivity_AdminRecyclerViewAdapter materialManagementActivity_adminRecyclerViewAdapter = new MaterialManagementActivity_AdminRecyclerViewAdapter();

        activity_material_management_admin_recyclerview_main_list.setAdapter(materialManagementActivity_adminRecyclerViewAdapter);
        materialManagementActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("Material_Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                materialManagementItems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MaterialManagement_Admin_Item materialManagementItem = snapshot.getValue(MaterialManagement_Admin_Item.class);
                    String uidKey = snapshot.getKey();
                    materialManagementItems.add(materialManagementItem);
                    uidLists.add(uidKey);
                }
                materialManagementActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        Button activity_material_management_admin_button_insert = (Button) findViewById(R.id.activity_material_management_admin_button_insert);
        activity_material_management_admin_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MaterialManagementActivity_Admin.this, MaterialManagementActivity_Insert.class);
                startActivity(intent);
                materialManagementActivity_adminRecyclerViewAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLARY_CODE) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            StorageReference riversRef = storageRef.child("Material_Management/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                }
            });
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }


//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>


    // MaterialManagementActivity_Admin 어댑터

    class MaterialManagementActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            ImageView activity_material_management_admin_item_imageview_recyclerview_image;
            TextView activity_material_management_admin_item_textview_recyclerview_item_name;
            TextView activity_material_management_admin_item_textview_recyclerview_lender;
            LinearLayout activity_material_management_admin_item_linearlayout;
            TextView activity_material_management_admin_item_recyclerview_timestamp;

            public CustomViewHolder(View view) {
                super(view);

                activity_material_management_admin_item_textview_recyclerview_item_name = (TextView) view.findViewById(R.id.activity_material_management_admin_item_textview_recyclerview_item_name);
                activity_material_management_admin_item_textview_recyclerview_lender = (TextView) view.findViewById(R.id.activity_material_management_admin_item_textview_recyclerview_lender);
                activity_material_management_admin_item_imageview_recyclerview_image = (ImageView) view.findViewById(R.id.activity_material_management_admin_item_imageview_recyclerview_image);

                activity_material_management_admin_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_material_management_admin_item_linearlayout);
                activity_material_management_admin_item_recyclerview_timestamp = (TextView) view.findViewById(R.id.activity_material_management_admin_item_recyclerview_timestamp);


            }

        }

        public void PopupMenu(final MaterialManagementActivity_AdminRecyclerViewAdapter.CustomViewHolder viewholder, final int position) {
            viewholder.activity_material_management_admin_item_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete_item(position);
                    PopupMenu popup = new PopupMenu(view.getContext(), view);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //int x = item.getItemId();
                            switch (item.getItemId()){

                                case R.id.material_delete:
                                    delete_content(position);
                                    materialManagementItems.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, materialManagementItems.size());
                                    return true;

                                case R.id.material_lend:
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MaterialManagementActivity_Admin.this);

                                    View view2 = LayoutInflater.from(MaterialManagementActivity_Admin.this)
                                            .inflate(R.layout.activity_material_management_lend, null, false);
                                    builder2.setView(view2);

                                    final Button detailButton = (Button) view2.findViewById(R.id.activity_material_management_lend_button_lend);
                                    final Button detailButtonPeriodCalendar = (Button) view2.findViewById(R.id.activity_material_management_lend_period_calendar);
                                    final Button detailButtonPeriodTime = (Button) view2.findViewById(R.id.activity_material_management_lend_period_time);
                                    final TextView detailTextID = (TextView) view2.findViewById(R.id.activity_material_management_lend_item_name);
                                    final TextView detailTextName = (TextView) view2.findViewById(R.id.activity_material_management_lend_lender);
                                    final ImageView detailImageView = (ImageView) view2.findViewById(R.id.activity_material_management_lend_imageview_image);

                                    detailTextID.setText(materialManagementItems.get(position).title);
                                    detailTextName.setText(auth.getCurrentUser().getDisplayName());
                                    Glide.with(view2).load(materialManagementItems.get(position).imageUri).into(detailImageView);

                                    now = System.currentTimeMillis();
                                    // 현재시간을 date 변수에 저장한다.
                                    Date date = new Date(now);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    //"yyyy-MM-dd"
                                    formatDate = simpleDateFormat.format(date);
                                    detailButtonPeriodCalendar.setText(formatDate);

                                    SimpleDateFormat startSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    startDate = startSimpleDateFormat.format(date);

                                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                                    // "HH:mm"
                                    formatHour = simpleDateFormat.format(date);
                                    detailButtonPeriodTime.setText(formatHour);

                                    final AlertDialog dialog2 = builder2.create();

                                    detailButtonPeriodCalendar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {

                                            DatePickerDialog datePickerDialog = new DatePickerDialog(MaterialManagementActivity_Admin.this, new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                                                    y = year;
                                                    m = month + 1;
                                                    d = dayOfMonth;

                                                    if(month < 10)
                                                        dateStr = year+"-0"+(month+1)+"-";
                                                    else
                                                        dateStr = year+"-"+(month+1)+"-";

                                                    if(dayOfMonth < 10)
                                                        dateStr += ("0"+dayOfMonth);
                                                    else
                                                        dateStr += dayOfMonth;

                                                    detailButtonPeriodCalendar.setText(dateStr);
                                                    flag1++;

                                                }
                                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                                            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                                            datePickerDialog.setMessage("날짜");
                                            datePickerDialog.show();
                                        }
                                    });

                                    detailButtonPeriodTime.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(MaterialManagementActivity_Admin.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
                                                    h = hourOfDay;
                                                    mi = minute;

                                                    if(hourOfDay < 10)
                                                        timeStr = "0"+hourOfDay;
                                                    else
                                                        timeStr = hourOfDay+"";

                                                    if(minute < 10)
                                                        timeStr += ":0"+minute;
                                                    else
                                                        timeStr += ":"+minute;

                                                    detailButtonPeriodTime.setText(timeStr);
                                                    flag2++;

                                                }
                                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

                                            timePickerDialog.setMessage("시간");
                                            timePickerDialog.show();
                                        }
                                    });

                                    detailButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {

                                            if (flag1 == 0) {
                                                Toast.makeText(v.getContext(),  "대여날짜를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                            }

                                            if (flag2 == 0) {
                                                Toast.makeText(v.getContext(),  "대여시간을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                            }

                                            if(flag1 == 1 && flag2 == 1) {
                                                Toast.makeText(v.getContext(), auth.getCurrentUser().getDisplayName() + "님 대여가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                ((CustomViewHolder) viewholder).activity_material_management_admin_item_textview_recyclerview_lender.setText(auth.getCurrentUser().getDisplayName());
                                                database.getReference().child("Material_Management").child(uidLists.get(position)).child("lender").setValue(((CustomViewHolder) viewholder).activity_material_management_admin_item_textview_recyclerview_lender.getText().toString());
                                                ((CustomViewHolder) viewholder).activity_material_management_admin_item_recyclerview_timestamp.setText(dateStr + ' ' +  timeStr);
                                                database.getReference().child("Material_Management").child(uidLists.get(position)).child("timestamp").setValue(((CustomViewHolder) viewholder).activity_material_management_admin_item_recyclerview_timestamp.getText().toString());
                                                // 대여를 하였을 떄 리사이클러뷰 리스트를 변경을 함

                                                findkey = database.getReference().push().getKey(); // 대여를 했을 떄 기록을 남기기 위해 데이터베이스에 저장함
                                                database.getReference().child("Material_Management").child(uidLists.get(position)).child("lend_history").child(findkey).child("history_lend_date").setValue(startDate + " ~ "  +((CustomViewHolder) viewholder).activity_material_management_admin_item_recyclerview_timestamp.getText().toString());
                                                database.getReference().child("Material_Management").child(uidLists.get(position)).child("lend_history").child(findkey).child("history_lend_name").setValue(((CustomViewHolder) viewholder).activity_material_management_admin_item_textview_recyclerview_lender.getText().toString());
                                                database.getReference().child("Material_Management").child(uidLists.get(position)).child("state").setValue(1);

                                                flag1 = 0;
                                                flag2 = 0;
                                                dialog2.dismiss();

                                            }
                                        }
                                    });

                                    dialog2.show();
                                    return true;

                                case R.id.material_turn_in:

                                    ((CustomViewHolder) viewholder).activity_material_management_admin_item_textview_recyclerview_lender.setText("없음");
                                    database.getReference().child("Material_Management").child(uidLists.get(position)).child("lender").setValue(((CustomViewHolder) viewholder).activity_material_management_admin_item_textview_recyclerview_lender.getText().toString());
                                    ((CustomViewHolder) viewholder).activity_material_management_admin_item_recyclerview_timestamp.setText("없음");
                                    database.getReference().child("Material_Management").child(uidLists.get(position)).child("timestamp").setValue(((CustomViewHolder) viewholder).activity_material_management_admin_item_recyclerview_timestamp.getText().toString());

                                    // 반납을 했을시 물품기록사항에서 대여의 끝나는 날짜가 반납일로 바뀐다
                                    now = System.currentTimeMillis();
                                    // 현재시간을 date 변수에 저장한다.
                                    Date returnDate = new Date(now);
                                    SimpleDateFormat returnSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    //"yyyy-MM-dd HH:mm"
                                    date_Return = returnSimpleDateFormat.format(returnDate);

                                    database.getReference().child("Material_Management").child(uidLists.get(position)).child("lend_history").child(findkey).child("history_lend_date").setValue(startDate + " ~ " + date_Return);
                                    database.getReference().child("Material_Management").child(uidLists.get(position)).child("state").setValue(0);

                                    return true;

                                case R.id.material_history:

                                    Intent intent = new Intent(MaterialManagementActivity_Admin.this, MaterialManagementActivity_History.class);
                                    MaterialManagement_History_Item materialHistoryItem = new MaterialManagement_History_Item();
                                    materialHistoryItem.history_lend_date = ((CustomViewHolder) viewholder).activity_material_management_admin_item_recyclerview_timestamp.getText().toString();
                                    materialHistoryItem.history_lend_name = ((CustomViewHolder) viewholder).activity_material_management_admin_item_textview_recyclerview_lender.getText().toString();
                                    String uidAdminPath = database.getReference().child("Material_Management").child(uidLists.get(position)).getKey();
                                    intent.putExtra("uidAdminPath", uidAdminPath);
                                    startActivity(intent);

                                    return true;

                                default:
                                    return false;
                            }
                            //return false;
                        }
                    });

                    popup.inflate(R.menu.material_management_main_popup);

                    if(!auth.getCurrentUser().getDisplayName().equals(materialManagementItems.get(position).lender)) {
                        popup.getMenu().getItem(2).setVisible(false);
                    }
                    if(!materialManagementItems.get(position).lender.equals("없음")) {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(1).setVisible(false);
                        if(!auth.getCurrentUser().getDisplayName().equals(materialManagementItems.get(position).lender)) {
                            popup.getMenu().getItem(2).setVisible(false);
                        }
                    }

                    popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                    popup.show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_material_management_admin_item, viewGroup, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            CustomViewHolder customViewHolder = ((CustomViewHolder) viewholder);
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_item_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_lender.setGravity(Gravity.LEFT);

            customViewHolder.activity_material_management_admin_item_textview_recyclerview_item_name.setText(materialManagementItems.get(position).getId());
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_item_name.setText(materialManagementItems.get(position).title);
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_lender.setText(materialManagementItems.get(position).lender);
            customViewHolder.activity_material_management_admin_item_recyclerview_timestamp.setText(materialManagementItems.get(position).timestamp.toString());

            Glide.with(viewholder.itemView.getContext()).load(materialManagementItems.get(position).imageUri).into(((CustomViewHolder) viewholder).activity_material_management_admin_item_imageview_recyclerview_image);

            PopupMenu(customViewHolder, position);

            if (materialManagementItems.get(position).lender.equals("없음")) {
                customViewHolder.activity_material_management_admin_item_linearlayout.setBackgroundResource(R.drawable.border_green);
            } else {
                customViewHolder.activity_material_management_admin_item_linearlayout.setBackgroundResource(R.drawable.border_gray);
                now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                //"yyyy-MM-dd HH:mm"
                date_Now = simpleDateFormat.format(date);
                date_End = materialManagementItems.get(position).timestamp.toString();
                Date d2 = simpleDateFormat.parse(date_Now, new ParsePosition(0));
                Date d1 = simpleDateFormat.parse(date_End, new ParsePosition(0));
                long diff = d1.getTime() - d2.getTime();
                if(diff <= 0) {
                    customViewHolder.activity_material_management_admin_item_linearlayout.setBackgroundResource(R.drawable.border_orange);
                    database.getReference().child("Material_Management").child(uidLists.get(position)).child("state").setValue(2);
                }
            }

        }

        private void delete_content(final int position) {

            storage.getReference().child("Material_Management").child(materialManagementItems.get(position).imageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {
//                    Toast.makeText(MaterialManagementActivity_Admin.this, "삭제 완료", Toast.LENGTH_SHORT).show();

                    database.getReference().child("Material_Management").child(uidLists.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(final Void aVoid) {
                            Toast.makeText(MaterialManagementActivity_Admin.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull final Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {
                    Toast.makeText(MaterialManagementActivity_Admin.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return materialManagementItems.size();
        }

    }

}
