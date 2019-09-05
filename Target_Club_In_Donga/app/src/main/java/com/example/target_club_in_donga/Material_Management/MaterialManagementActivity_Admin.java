package com.example.target_club_in_donga.Material_Management;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    List<MaterialManagement_Item> materialManagementItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

//    List<MaterialManagement_Item> mArrayList = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    protected ImageView activity_material_management_item_imageview_recyclerview_image;
    private long now;
    private String formatDate, formatHour, formatMin;
    private String dateStr,timeStr;

    protected TextView activity_material_management_item_timestamp;

    private int flag1 = 0, flag2 = 0, count = 0;

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


        this.activity_material_management_item_imageview_recyclerview_image = (ImageView) findViewById(R.id.activity_material_management_item_imageview_recyclerview_image);
        this.activity_material_management_item_timestamp = (TextView) findViewById(R.id.activity_material_management_item_timestamp);

        activity_material_management_admin_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_material_management_admin_recyclerview_main_list);
        activity_material_management_admin_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();

        activity_material_management_admin_recyclerview_main_list.setAdapter(boardRecyclerViewAdapter);
        boardRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("Material_Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                materialManagementItems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MaterialManagement_Item materialManagementItem = snapshot.getValue(MaterialManagement_Item.class);
                    String uidKey = snapshot.getKey();
                    materialManagementItems.add(materialManagementItem);
                    uidLists.add(uidKey);
                }
                boardRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        Button activity_material_management_admin_button_insert = (Button) findViewById(R.id.activity_material_management_admin_button_insert);
        activity_material_management_admin_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                Intent intent = new Intent(MaterialManagementActivity_Admin.this, MaterialManagementActivity_Insert.class);
                startActivity(intent);
                boardRecyclerViewAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
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

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

        private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            ImageView activity_material_management_item_imageview_recyclerview_image;
            TextView activity_material_management_item_textview_recyclerview_item_name;
            TextView activity_material_management_item_textview_recyclerview_lender;
            LinearLayout activity_material_management_item_linearlayout;
            TextView activity_material_management_item_timestamp;


            public CustomViewHolder(View view) {
                super(view);

                activity_material_management_item_textview_recyclerview_item_name = (TextView) view.findViewById(R.id.activity_material_management_item_textview_recyclerview_item_name);
                activity_material_management_item_textview_recyclerview_lender = (TextView) view.findViewById(R.id.activity_material_management_item_textview_recyclerview_lender);
                activity_material_management_item_imageview_recyclerview_image = (ImageView) view.findViewById(R.id.activity_material_management_item_imageview_recyclerview_image);

                activity_material_management_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_material_management_item_linearlayout);
                activity_material_management_item_timestamp = (TextView) view.findViewById(R.id.activity_material_management_item_timestamp);

                view.setOnCreateContextMenuListener(this);

            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

//                MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정하기");
                MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제하기");
                MenuItem Detail = menu.add(Menu.NONE, 1003, 3, "대여하기");
//                Edit.setOnMenuItemClickListener(onEditMenu);
                Delete.setOnMenuItemClickListener(onEditMenu);
                Detail.setOnMenuItemClickListener(onEditMenu);

            }

            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case 1001:
/*
                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialManagementActivity_Admin.this);
                            View view = LayoutInflater.from(MaterialManagementActivity_Admin.this)
                                    .inflate(R.layout.activity_material_management_insert, null, false);
                            builder.setView(view);

                            final Button editButton = (Button) view.findViewById(R.id.activity_material_management_insert_button_insert);
                            final ImageView editImageView = (ImageView) view.findViewById(R.id.activity_material_management_insert_imageview_image);
                            final EditText editTextID = (EditText) view.findViewById(R.id.activity_material_management_insert_edittext_item_name);
                            final TextView editTextName = (TextView) view.findViewById(R.id.activity_material_management_insert_textview_lender);

                            editTextID.setText(materialManagementItems.get(getAdapterPosition()).getId());
                            editTextName.setText(auth.getCurrentUser().getDisplayName());
                            Glide.with(itemView.getContext()).load(materialManagementItems.get(getAdapterPosition()).imageUri).into(editImageView);

                            final AlertDialog dialog = builder.create();

                            editButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    Toast.makeText(v.getContext(),  "수정이 완료되었습니다..", Toast.LENGTH_SHORT).show();

                                    activity_material_management_item_textview_recyclerview_lender.setText(auth.getCurrentUser().getDisplayName());
                                    database.getReference().child("Material_Management").child(uidLists.get(getAdapterPosition())).child("edit_lender").setValue(activity_material_management_item_textview_recyclerview_lender.getText().toString());*/

/*                                    materialManagementItems.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), materialManagementItems.size());

                                    String strID2 = detailTextID.getText().toString();

                                    MaterialManagement_Item dict2 = new MaterialManagement_Item(strID2);

                                    materialManagementItems.add(dict2);
                                    notifyItemRangeChanged(getAdapterPosition(), materialManagementItems.size());*/

/*                                mArrayList2.add(0, dict2); //첫 줄에 삽입
                                //mArrayList.add(dict); //마지막 줄에 삽입
                                mAdapter2.notifyDataSetChanged(); //변경된 데이터를 화면에 반영*/

//                                notifyItemRangeChanged(getAdapterPosition(), mArrayList2.size());

/*
                                    dialog.dismiss();

                                }
                            });

                            dialog.show();
*/


/*                            Bundle extras = ((Activity) mContext).getIntent().getExtras();
                            final Uri uri = Uri.parse(extras.getString("uri"));

                            byte[] byteArray = ((Activity) mContext).getIntent().getByteArrayExtra("image");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                            editImageView.setImageBitmap(bitmap);*/

/*                            final AlertDialog dialog = builder.create();

                            ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    String strID = editTextID.getText().toString();

                                    MaterialManagement_Item dict = new MaterialManagement_Item(strID);

                                    materialManagementItems.set(getAdapterPosition(), dict);
                                    notifyItemChanged(getAdapterPosition());

//                                ((BitmapDrawable)editImageView.getDrawable()).getBitmap().recycle();
                                    // 수정하기를 누르면 가지고 있던 이미지뷰의 사진 크기 메모리 반환

                                    dialog.dismiss();
                                }
                            });

                            editImageView.setOnClickListener(new View.OnClickListener() {

                                public void onClick(View v) {
                                    startActivity(new Intent(MaterialManagementActivity_Admin.this, MaterialManagementActivity_Insert.class));
                                }
                            });

                            dialog.show();*/
//                            dialog.dismiss();

                            break;

                        case 1002:

                            count--;

                            delete_content(getAdapterPosition());

                            materialManagementItems.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), materialManagementItems.size());

                            break;

                        case 1003:

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

                            detailTextID.setText(materialManagementItems.get(getAdapterPosition()).edit_name_edittext);
                            detailTextName.setText(auth.getCurrentUser().getDisplayName());
                            Glide.with(itemView.getContext()).load(materialManagementItems.get(getAdapterPosition()).imageUri).into(detailImageView);

                            now = System.currentTimeMillis();
                            // 현재시간을 date 변수에 저장한다.
                            Date date = new Date(now);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            //"yyyy-MM-dd"
                            formatDate = simpleDateFormat.format(date);
                            detailButtonPeriodCalendar.setText(formatDate);

                            simpleDateFormat = new SimpleDateFormat("HH:mm");
                            // "HH:mm"
                            formatHour = simpleDateFormat.format(date);
                            detailButtonPeriodTime.setText(formatHour);

                            final AlertDialog dialog2 = builder2.create();

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

                                    /*activity_material_management_item_timestamp.setText(formatDate);
                                    database.getReference().child("Material_Management").child(uidLists.get(getAdapterPosition())).child("timestamp").setValue(activity_material_management_item_timestamp.getText().toString());*/

//                                    materialManagementItems.remove(getAdapterPosition());
/*                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), materialManagementItems.size());

                                    String strID2 = detailTextID.getText().toString();

                                    MaterialManagement_Item dict2 = new MaterialManagement_Item(strID2);

                                    materialManagementItems.add(dict2);
                                    notifyItemRangeChanged(getAdapterPosition(), materialManagementItems.size());

                                    mArrayList.add(0, dict2); //첫 줄에 삽입
                                    //mArrayList.add(dict); //마지막 줄에 삽입
                                    mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영*/

//                                notifyItemRangeChanged(getAdapterPosition(), mArrayList2.size());

                                            if(flag1 == 1 && flag2 == 1) {
                                                Toast.makeText(v.getContext(), auth.getCurrentUser().getDisplayName() + "님 대여가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                activity_material_management_item_textview_recyclerview_lender.setText(auth.getCurrentUser().getDisplayName());
                                                database.getReference().child("Material_Management").child(uidLists.get(getAdapterPosition())).child("edit_lender").setValue(activity_material_management_item_textview_recyclerview_lender.getText().toString());
                                                activity_material_management_item_timestamp.setText(dateStr + '-' +  timeStr);
                                                database.getReference().child("Material_Management").child(uidLists.get(getAdapterPosition())).child("timestamp").setValue(activity_material_management_item_timestamp.getText().toString());
                                                flag1 = 0;
                                                flag2 = 0;
                                                dialog2.dismiss();
                                            }
                                        }
                                    });

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

                            dialog2.show();

                    }
                    return true;

                }
            };
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_material_management_item, viewGroup, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            CustomViewHolder customViewHolder = ((CustomViewHolder) viewholder);
            customViewHolder.activity_material_management_item_textview_recyclerview_item_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_material_management_item_textview_recyclerview_lender.setGravity(Gravity.LEFT);

            customViewHolder.activity_material_management_item_textview_recyclerview_item_name.setText(materialManagementItems.get(position).getId());
            customViewHolder.activity_material_management_item_textview_recyclerview_item_name.setText(materialManagementItems.get(position).edit_name_edittext);
            customViewHolder.activity_material_management_item_textview_recyclerview_lender.setText(materialManagementItems.get(position).edit_lender);
            customViewHolder.activity_material_management_item_timestamp.setText(materialManagementItems.get(position).timestamp.toString());

            Glide.with(viewholder.itemView.getContext()).load(materialManagementItems.get(position).imageUri).into(((CustomViewHolder) viewholder).activity_material_management_item_imageview_recyclerview_image);

            if (materialManagementItems.get(position).edit_lender.equals("없음")) {
            } else {
                customViewHolder.activity_material_management_item_linearlayout.setBackgroundColor(Color.LTGRAY);
            }

/*            long unixTime = (long) materialManagementItems.get(position).getTimestamp();
            Date date = new Date(unixTime);
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time = simpleDateFormat.format(date);*/

        }

        @Override
        public int getItemCount() {
            return materialManagementItems.size();
        }

    }

}
