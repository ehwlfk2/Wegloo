package com.example.target_club_in_donga.Material_Rental;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import static com.example.target_club_in_donga.MainActivity.clubName;

public class MaterialRentalActivity_Home extends AppCompatActivity {
    List<MaterialRental_Item> listItem = new ArrayList<>();

    int y = 0, m = 0, d = 0, h = 0, mi = 0;
    Calendar calendar = Calendar.getInstance();

    private static final int GALLARY_CODE = 10;

    RecyclerView activity_material_rental_home_recyclerview_main_list;
    List<MaterialRental_Item> materialRentalItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    /*    private ArrayList<String> list = new ArrayList<>();
        private ArrayAdapter<String> adapter;
        private AutoCompleteTextView material_rental_home_edittext_search;*/
    private EditText material_rental_home_edittext_search;
    private TextView activity_material_rental_home_textview;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    protected ImageView activity_material_rental_admin_item_imageview_recyclerview_image;
    private Button activity_material_rental_home_button_insert;
    private long now;
    private String formatDate, formatHour, formatMin, startDate;
    private String dateStr, timeStr, date_Now, date_End, date_Return;

    private String findkey, uidName, uidAdminPath;

    private int admin, monthInt, dayOfMonthInt, flag = 0, flag2 = 1, listNumber, lastNumber, dateFlag = 1, flag3 = 0, differFlag/*, listSize, materialListSize, listSizeFlag = 0*/, removeFlag = 1, removeFlag2 = 1, removeList, removeList2;
    private static int adminNumber = 2;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_rental_home);

        Intent intent = getIntent();
        differFlag = intent.getExtras().getInt("differFlag");

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("물품대여를 불러오는 중입니다...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        activity_material_rental_home_button_insert = (Button) findViewById(R.id.activity_material_rental_home_button_insert);
        activity_material_rental_home_textview = (TextView) findViewById(R.id.activity_material_rental_home_textview);

        if (differFlag == 0) {
            activity_material_rental_home_button_insert.setVisibility(View.GONE);
        } else {
            activity_material_rental_home_textview.setText("물품관리");
        }

        this.activity_material_rental_admin_item_imageview_recyclerview_image = (ImageView) findViewById(R.id.activity_material_rental_admin_item_imageview_recyclerview_image);

        activity_material_rental_home_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_material_rental_home_recyclerview_main_list);
        activity_material_rental_home_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final MaterialRentalActivity_AdminRecyclerViewAdapter materialRentalActivity_adminRecyclerViewAdapter = new MaterialRentalActivity_AdminRecyclerViewAdapter();

        activity_material_rental_home_recyclerview_main_list.setAdapter(materialRentalActivity_adminRecyclerViewAdapter);
        materialRentalActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        material_rental_home_edittext_search = (AutoCompleteTextView) findViewById(R.id.material_rental_home_edtitext_search);
//        material_rental_home_edittext_search = (EditText) findViewById(R.id.material_rental_home_edtitext_search);

        database.getReference().child("EveryClub").child(clubName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null && dataSnapshot.child("realNameSystem").getValue().toString().equals("true")) {
                    admin = Integer.parseInt(dataSnapshot.child("User").child(auth.getCurrentUser().getUid()).child("admin").getValue().toString());
                    if (admin > adminNumber) {
                        activity_material_rental_home_button_insert.setVisibility(View.GONE);
                    }
                    database.getReference().child("AppUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            uidName = dataSnapshot.child(auth.getCurrentUser().getUid()).child("name").getValue().toString();
                        }

                        @Override
                        public void onCancelled(final DatabaseError databaseError) {

                        }
                    });

                } else {
                    admin = Integer.parseInt(dataSnapshot.child("User").child(auth.getCurrentUser().getUid()).child("admin").getValue().toString());
                    uidName = dataSnapshot.child("User").child(auth.getCurrentUser().getUid()).child("name").getValue().toString();

                    if (admin > adminNumber) {
                        activity_material_rental_home_button_insert.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    materialRentalItems.clear();
                    uidLists.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MaterialRental_Item materialRentalItem = snapshot.getValue(MaterialRental_Item.class);
                        String uidKey = snapshot.getKey();
                        materialRentalItems.add(0, materialRentalItem);
                        uidLists.add(0, uidKey);
                    }

                    materialRentalActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();

/*                materialListSize = materialRentalItems.size();

                // 자동완성에 들어갈 물품명을 list에 저장
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.child("title").getValue(String.class) + "");
                }
                material_rental_home_edittext_search.setAdapter(adapter);*/
                }

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

/*        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        material_rental_home_edittext_search.setAdapter(adapter);*/

        activity_material_rental_home_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaterialRentalActivity_Home.this, MaterialRentalActivity_Admin_Insert.class);
                startActivity(intent);
                flag = 0;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        material_rental_home_edittext_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                String text = material_rental_home_edittext_search.getText().toString();
                search(text);
            }

            private void search(final String charText) {

                progressDialog.setMessage("검색 중입니다...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (flag == 0) {
                    listItem.clear();
                    listItem.addAll(materialRentalItems);
                    flag = 1;
                    flag3 = 0;
                }

                // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
                materialRentalItems.clear();

                // 문자 입력이 없을때는 모든 데이터를 보여준다.
                if (charText.length() == 0) {
                    materialRentalItems.addAll(listItem);
                    flag2 = 1;
                }
                // 문자 입력을 할때..
                else {
                    // 리스트의 모든 데이터를 검색한다.
                    for (int i = 0; i < listItem.size(); i++) {
                        // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                        if (listItem.get(i).title.toLowerCase().contains(charText) || listItem.get(i).lender.toLowerCase().contains(charText)) {
                            // 검색된 데이터를 리스트에 추가한다.
                            materialRentalItems.add(listItem.get(i));
                        }
                    }
                    flag2 = 0;
                }
                // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                materialRentalActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });

        AutoCompleteTextView material_rental_home_autocompletetextview = (AutoCompleteTextView) findViewById(R.id.material_rental_home_edtitext_search);
        material_rental_home_autocompletetextview.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, materialRentalItems));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLARY_CODE) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            StorageReference riversRef = storageRef.child("EveryClub").child(clubName).child("Material_Rental/" + file.getLastPathSegment());
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


    // MaterialRentalActivity_Home 어댑터

    class MaterialRentalActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            ImageView activity_material_rental_admin_item_imageview_recyclerview_image;
            TextView activity_material_rental_admin_item_textview_recyclerview_item_name;
            TextView activity_material_rental_admin_item_textview_recyclerview_lender;
            LinearLayout activity_material_rental_admin_item_linearlayout;
            TextView activity_material_rental_admin_item_recyclerview_timestamp;

            public CustomViewHolder(View view) {
                super(view);

                activity_material_rental_admin_item_textview_recyclerview_item_name = (TextView) view.findViewById(R.id.activity_material_rental_admin_item_textview_recyclerview_item_name);
                activity_material_rental_admin_item_textview_recyclerview_lender = (TextView) view.findViewById(R.id.activity_material_rental_admin_item_textview_recyclerview_lender);
                activity_material_rental_admin_item_imageview_recyclerview_image = (ImageView) view.findViewById(R.id.activity_material_rental_admin_item_imageview_recyclerview_image);

                activity_material_rental_admin_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_material_rental_admin_item_linearlayout);
                activity_material_rental_admin_item_recyclerview_timestamp = (TextView) view.findViewById(R.id.activity_material_rental_admin_item_recyclerview_timestamp);

            }

        }

        public void PopupMenu(final MaterialRentalActivity_AdminRecyclerViewAdapter.CustomViewHolder viewholder, final int position) {
            viewholder.activity_material_rental_admin_item_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Log.e("값", flag2 + "");

                    if (flag2 == 0) {
                        for (int i = 0; i < listItem.size(); i++) {
                            if (listItem.get(i).title.equals(materialRentalItems.get(position).title)) {
                                listNumber = i;
                            }
                        }
                    }

//                    Log.e("입력", listNumber + "");
//                    Log.e("길이", listItem.size() + "");
//                    Log.e("값", listItem.get(1).title + "");

                    final PopupMenu popup = new PopupMenu(view.getContext(), view);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.material_lend:

                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MaterialRentalActivity_Home.this);

                                    View view2 = LayoutInflater.from(MaterialRentalActivity_Home.this)
                                            .inflate(R.layout.activity_material_rental, null, false);
                                    builder2.setView(view2);

                                    final Button detailButton = (Button) view2.findViewById(R.id.activity_material_rental_button);
                                    final Button detailButtonPeriodCalendar = (Button) view2.findViewById(R.id.activity_material_rental_period_calendar);
                                    final Button detailButtonPeriodTime = (Button) view2.findViewById(R.id.activity_material_rental_period_time);
                                    final TextView detailTextID = (TextView) view2.findViewById(R.id.activity_material_rental_item_name);
                                    final TextView detailTextName = (TextView) view2.findViewById(R.id.activity_material_rental_renter);
                                    final ImageView detailImageView = (ImageView) view2.findViewById(R.id.activity_material_rental_imageview_image);

                                    detailTextID.setText(materialRentalItems.get(position).title);
                                    detailTextName.setText(uidName);
                                    Glide.with(view2).load(materialRentalItems.get(position).imageUri).into(detailImageView);

                                    now = System.currentTimeMillis();
                                    // 현재시간을 date 변수에 저장한다.
                                    Date date = new Date(now);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    // "yyyy-MM-dd"
                                    formatDate = simpleDateFormat.format(date);
                                    detailButtonPeriodCalendar.setText(formatDate);

                                    SimpleDateFormat startSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    // "yyyy-MM-dd HH:mm"
                                    startDate = startSimpleDateFormat.format(date);

                                    SimpleDateFormat startSimpleTimeFormat = new SimpleDateFormat("HH:mm");
                                    // "HH:mm"
                                    formatHour = startSimpleTimeFormat.format(date);
                                    detailButtonPeriodTime.setText(formatHour);

                                    dateStr = formatDate;
                                    timeStr = formatHour;

                                    final AlertDialog dialog2 = builder2.create();

                                    detailButtonPeriodCalendar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {

                                            DatePickerDialog datePickerDialog = new DatePickerDialog(MaterialRentalActivity_Home.this, new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                                                    y = year;
                                                    m = month + 1;
                                                    d = dayOfMonth;
                                                    monthInt = month;
                                                    dayOfMonthInt = dayOfMonth;

                                                    if (month < 9)
                                                        dateStr = year + "-0" + (month + 1) + "-";
                                                    else
                                                        dateStr = year + "-" + (month + 1) + "-";

                                                    if (dayOfMonth < 10)
                                                        dateStr += ("0" + dayOfMonth);
                                                    else
                                                        dateStr += dayOfMonth;

                                                    detailButtonPeriodCalendar.setText(dateStr);

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
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(MaterialRentalActivity_Home.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
                                                    h = hourOfDay;
                                                    mi = minute;
                                                    Date now = new Date();

                                                    if ((monthInt > now.getMonth() || dayOfMonthInt > now.getDay()) || (hourOfDay > now.getHours() || hourOfDay >= now.getHours() && minute > now.getMinutes())) {
                                                        if (hourOfDay < 10)
                                                            timeStr = "0" + hourOfDay;
                                                        else
                                                            timeStr = hourOfDay + "";

                                                        if (minute < 10)
                                                            timeStr += ":0" + minute;
                                                        else
                                                            timeStr += ":" + minute;

                                                        detailButtonPeriodTime.setText(timeStr);
                                                        dateFlag = 0;

                                                    } else {
                                                        Toast.makeText(v.getContext(), "이미 지난 시간은 선택할 수 없습니다", Toast.LENGTH_SHORT).show();
                                                        dateFlag = 1;
                                                    }
                                                }
                                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

                                            timePickerDialog.setMessage("시간");
                                            timePickerDialog.show();
                                        }
                                    });

                                    detailButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {

                                            if (dateFlag == 0) {
                                                Toast.makeText(v.getContext(), "대여가 완료되었습니다", Toast.LENGTH_SHORT).show();

                                                ((CustomViewHolder) viewholder).activity_material_rental_admin_item_textview_recyclerview_lender.setText(uidName);
                                                if (flag2 == 1) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("lender").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_textview_recyclerview_lender.getText().toString());
                                                } else if (flag2 == 0) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("lender").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_textview_recyclerview_lender.getText().toString());
                                                }

                                                ((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.setText(dateStr + ' ' + timeStr);
                                                if (flag2 == 1) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("timestamp").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.getText().toString());
                                                } else if (flag2 == 0) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("timestamp").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.getText().toString());
                                                }
                                                // 대여를 하였을 떄 리사이클러뷰 리스트를 변경을 함

                                                findkey = database.getReference().push().getKey(); // 대여를 했을 떄 기록을 남기기 위해 데이터베이스에 저장함
                                                if (flag2 == 1) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("lend_history").child(findkey).child("history_lend_start_date").setValue(startDate);
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("lend_history").child(findkey).child("history_lend_end_date").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.getText().toString());
                                                } else if (flag2 == 0) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("lend_history").child(findkey).child("history_lend_start_date").setValue(startDate);
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("lend_history").child(findkey).child("history_lend_end_date").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.getText().toString());
                                                }

                                                if (flag2 == 1) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("lend_history").child(findkey).child("history_lend_name").setValue(uidName);
                                                } else if (flag2 == 0) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("lend_history").child(findkey).child("history_lend_name").setValue(uidName);
                                                }

                                                if (flag2 == 1) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("state").setValue(1);
                                                } else if (flag2 == 0) {
                                                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("state").setValue(1);
                                                }

                                                dialog2.dismiss();

                                                if (flag2 == 0) {
                                                    flag3 = 1;
                                                }

                                                flag = 0;

                                            } else if (dateFlag == 1) {
                                                Toast.makeText(v.getContext(), "시간을 다시 선택해주세요", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                    dialog2.show();
                                    return true;

                                case R.id.material_turn_in:

                                    Toast.makeText(MaterialRentalActivity_Home.this, "반납이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                    ((CustomViewHolder) viewholder).activity_material_rental_admin_item_textview_recyclerview_lender.setText("없음");
                                    if (flag2 == 1 || flag3 == 1) {
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("lender").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_textview_recyclerview_lender.getText().toString());
                                    } else if (flag2 == 0) {
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("lender").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_textview_recyclerview_lender.getText().toString());
                                    }
                                    ((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.setText("없음");
                                    if (flag2 == 1 || flag3 == 1) {
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("timestamp").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.getText().toString());
                                    } else if (flag2 == 0) {
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("timestamp").setValue(((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.getText().toString());
                                    }

                                    // 반납을 했을시 물품기록사항에서 대여의 끝나는 날짜가 반납일로 바뀐다
                                    now = System.currentTimeMillis();
                                    // 현재시간을 date 변수에 저장한다.
                                    Date returnDate = new Date(now);
                                    SimpleDateFormat returnSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    //"yyyy-MM-dd HH:mm"
                                    date_Return = returnSimpleDateFormat.format(returnDate);

                                    if (flag2 == 1 || flag3 == 1) {
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("lend_history").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                lastNumber = 0;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    lastNumber++;
                                                }
                                                int i = 0;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    if (i == lastNumber - 1) {
                                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("lend_history").child(snapshot.getKey()).child("history_lend_end_date").setValue(date_Return);
                                                    }
                                                    i++;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(final DatabaseError databaseError) {

                                            }
                                        });
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("state").setValue(0);
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("timestamp").setValue("없음");
                                    } else if (flag2 == 0) {
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("lend_history").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                lastNumber = 0;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    lastNumber++;
                                                }
                                                int i = 0;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    if (i == lastNumber - 1) {
                                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("lend_history").child(snapshot.getKey()).child("history_lend_end_date").setValue(date_Return);
                                                    }
                                                    i++;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(final DatabaseError databaseError) {

                                            }
                                        });
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("state").setValue(0);
                                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).child("timestamp").setValue("없음");
                                    }

                                    flag = 0;

                                    return true;

                                case R.id.material_delete:

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialRentalActivity_Home.this);

                                    View view = LayoutInflater.from(MaterialRentalActivity_Home.this)
                                            .inflate(R.layout.activity_material_rental_admin_delete_item, null, false);
                                    builder.setView(view);

                                    final Button confirmButton = (Button) view.findViewById(R.id.activity_material_rental_admin_delete_item_button_confirm);
                                    final Button cancelButton = (Button) view.findViewById(R.id.activity_material_rental_admin_delete_item_button_cancel);

                                    final AlertDialog dialog = builder.create();

                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    confirmButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            if (flag2 == 1) {
                                                delete_content(position);
                                                materialRentalItems.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, materialRentalItems.size());
                                                flag = 0;
                                                removeFlag = 0;
                                                removeList = position;
                                                Toast.makeText(MaterialRentalActivity_Home.this, "상품이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                            } else if (flag2 == 0) {
                                                delete_content(listNumber);
                                                listItem.remove(listNumber);
                                                notifyItemRemoved(listNumber);
                                                notifyItemRangeChanged(listNumber, listItem.size());
                                                flag = 0;
                                                removeFlag2 = 0;
                                                removeList2 = listNumber;
                                                Toast.makeText(MaterialRentalActivity_Home.this, "상품이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MaterialRentalActivity_Home.this, "상품 삭제 실패", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    });

                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    return true;

                                case R.id.material_history:

                                    Intent intent = new Intent(MaterialRentalActivity_Home.this, MaterialRentalActivity_History.class);
                                    final MaterialRental_History_Item materialHistoryItem = new MaterialRental_History_Item();
//                                    materialHistoryItem.history_lend_start_date = ((CustomViewHolder) viewholder).activity_material_rental_admin_item_recyclerview_timestamp.getText().toString();

                                    materialHistoryItem.history_lend_name = uidName;

                                    if (flag2 == 1) {
                                        uidAdminPath = database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).getKey();
                                    } else if (flag2 == 0) {
                                        uidAdminPath = database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).getKey();
                                    }
                                    intent.putExtra("uidAdminPath", uidAdminPath);
                                    startActivity(intent);

                                    return true;

                                default:
                                    return false;
                            }
                            //return false;
                        }
                    });

                    popup.inflate(R.menu.material_rental_home_popup);

                    if (differFlag == 0) {
                        popup.getMenu().getItem(2).setVisible(false);
                        if (!materialRentalItems.get(position).lender.equals("없음")) {
                            popup.getMenu().getItem(0).setVisible(false);
                        }
                        if (materialRentalItems.get(position).lender.equals("없음") || !materialRentalItems.get(position).lender.equals(uidName)) {
                            popup.getMenu().getItem(3).setVisible(false);
                        }
                    } else {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                        if (admin > adminNumber || !materialRentalItems.get(position).lender.equals("없음") && admin <= adminNumber) {
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
                    .inflate(R.layout.activity_material_rental_admin_item, viewGroup, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            CustomViewHolder customViewHolder = ((CustomViewHolder) viewholder);
            customViewHolder.activity_material_rental_admin_item_textview_recyclerview_item_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_material_rental_admin_item_textview_recyclerview_lender.setGravity(Gravity.LEFT);

            customViewHolder.activity_material_rental_admin_item_textview_recyclerview_item_name.setText(materialRentalItems.get(position).getId());
            customViewHolder.activity_material_rental_admin_item_textview_recyclerview_item_name.setText(materialRentalItems.get(position).title);
            customViewHolder.activity_material_rental_admin_item_textview_recyclerview_lender.setText(materialRentalItems.get(position).lender);
            customViewHolder.activity_material_rental_admin_item_recyclerview_timestamp.setText(materialRentalItems.get(position).timestamp.toString());

            Glide.with(viewholder.itemView.getContext()).load(materialRentalItems.get(position).imageUri).into(((CustomViewHolder) viewholder).activity_material_rental_admin_item_imageview_recyclerview_image);

            PopupMenu(customViewHolder, position);

            if (materialRentalItems.get(position).lender.equals("없음")) {
                customViewHolder.activity_material_rental_admin_item_linearlayout.setBackgroundResource(R.drawable.border_green);
            } else {
                customViewHolder.activity_material_rental_admin_item_linearlayout.setBackgroundResource(R.drawable.border_gray);
                now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                //"yyyy-MM-dd HH:mm"
                date_Now = simpleDateFormat.format(date);
                date_End = materialRentalItems.get(position).timestamp.toString();
                Date d2 = simpleDateFormat.parse(date_Now, new ParsePosition(0));
                Date d1 = simpleDateFormat.parse(date_End, new ParsePosition(0));
                long diff = d1.getTime() - d2.getTime();
                if (diff <= 0) {
                    customViewHolder.activity_material_rental_admin_item_linearlayout.setBackgroundResource(R.drawable.border_orange);
                    database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).child("state").setValue(2);
                }
            }

/*            if (listSizeFlag == 0) {
                listSize = materialRentalItems.size();
                listSizeFlag = 1;
            }

            Log.e("값2", listSize + "");

            Log.e("값",  materialListSize + "");

            if (materialListSize != listSize) {
                list.add(materialRentalItems.get(materialListSize).title);
                listSize = materialListSize;
                material_rental_home_edittext_search.setAdapter(adapter);
            }*/

        }

        private void delete_content(final int position) {
            Log.e("입력", position + "");

            if (flag2 == 1) {
                storage.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(materialRentalItems.get(position).imageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(final Void aVoid) {

                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(final Void aVoid) {
                                Toast.makeText(MaterialRentalActivity_Home.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MaterialRentalActivity_Home.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (flag2 == 0) {
                storage.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(listItem.get(position).imageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(final Void aVoid) {

                        database.getReference().child("EveryClub").child(clubName).child("Material_Rental").child(uidLists.get(listNumber)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(final Void aVoid) {
                                Toast.makeText(MaterialRentalActivity_Home.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MaterialRentalActivity_Home.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return materialRentalItems.size();
        }

    }

}
