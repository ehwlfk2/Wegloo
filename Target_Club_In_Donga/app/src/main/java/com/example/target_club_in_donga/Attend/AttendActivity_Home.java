package com.example.target_club_in_donga.Attend;

import android.app.ProgressDialog;
import android.content.CursorLoader;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

//import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_Home extends AppCompatActivity {

    RecyclerView activity_attend_home_admin_recyclerview_main_list;
    List<Attend_Admin_Item> attenditems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    Button activity_attend_home_admin_button_insert, activity_attend_home_button_information;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private int admin;
    public static String uidAdminPath;

    private long now;
    private String formatDate, nowtardyTimeLimit, getTardyTimeLimit;
    private static int adminNumber = 2;

    private ProgressDialog progressDialog;

    private Date attenddate;
    private String date_Attend, date_Trady;
    private int flag = 0, flag2 = 0;
    private int certification_number;
    private int minNumber = 1000, maxNumber = 9999;
    private String findkey, getName, getPhone;

    private int getEditCertificationNumber;
    private String getCertificationNumber, EditCertificationNumber;
    private String getAttend_Time_Limit, getTardy_Time_Limit;
    private String nowDate;
    private String getState;

    private String clubName = "TCID";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_home);

        final Random random_number = new Random();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("출석을 불러오는 중입니다...");
        progressDialog.show();

        database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                admin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                if (admin > adminNumber) {
                    activity_attend_home_admin_button_insert.setVisibility(View.GONE);
//                    activity_attend_home_button_information.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_attend_home_admin_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_home_recyclerview_main_list);
        activity_attend_home_admin_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final AttendActivity_AdminRecyclerViewAdapter attendActivity_adminRecyclerViewAdapter = new AttendActivity_AdminRecyclerViewAdapter();

        activity_attend_home_admin_recyclerview_main_list.setAdapter(attendActivity_adminRecyclerViewAdapter);

        database.getReference().child("EveryClub").child(clubName).child("Attend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attenditems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("Attend_Certification_Number").getValue() != null) {
                        Attend_Admin_Item attendItem = snapshot.getValue(Attend_Admin_Item.class);
                        String uidKey = snapshot.getKey();
                        attenditems.add(0, attendItem);
                        uidLists.add(0, uidKey);
                    }
                }
                attendActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_attend_home_admin_button_insert = (Button) findViewById(R.id.activity_attend_home_button_insert);
        activity_attend_home_admin_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("출석을 추가하는 중입니다...");
                progressDialog.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(AttendActivity_Home.this);

                View view = LayoutInflater.from(AttendActivity_Home.this)
                        .inflate(R.layout.activity_attend_admin, null, false);
                builder.setView(view);

                final RadioGroup activity_attend_admin_radiogroup_attend = (RadioGroup) view.findViewById(R.id.activity_attend_admin_radiogroup_attend);
                final RadioGroup activity_attend_admin_radiogroup_tardy = (RadioGroup) view.findViewById(R.id.activity_attend_admin_radiogroup_tardy);
                final Button activity_attend_admin_button_attendance_start = (Button) view.findViewById(R.id.activity_attend_admin_button_attendance_start);

                final AlertDialog dialog = builder.create();

                now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                attenddate = new Date(now);

                // 출석시간을 결정
                activity_attend_admin_radiogroup_attend.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final RadioGroup radioGroup, final int checkedId) {
                        now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        //"yyyy-MM-dd HH:mm"

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        if (checkedId == R.id.activity_attend_admin_radiobutton_attend_5_min) {
                            calendar.add(Calendar.MINUTE, 5);
                            date_Attend = simpleDateFormat.format(calendar.getTime());
                            attenddate = calendar.getTime();
                        } else if (checkedId == R.id.activity_attend_admin_radiobutton_attend_10_min) {
                            calendar.add(Calendar.MINUTE, 10);
                            date_Attend = simpleDateFormat.format(calendar.getTime());
                            attenddate = calendar.getTime();
                        } else {
                            calendar.add(Calendar.MINUTE, 15);
                            date_Attend = simpleDateFormat.format(calendar.getTime());
                            attenddate = calendar.getTime();
                        }

                        flag++;
                    }
                });

                // 지각시간을 결정
                activity_attend_admin_radiogroup_tardy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final RadioGroup radioGroup, final int checkedId) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        //"yyyy-MM-dd HH:mm"

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(attenddate);

                        if (checkedId == R.id.activity_attend_admin_radiobutton_tardy_10_min) {
                            calendar.add(Calendar.MINUTE, 10);
                            date_Trady = simpleDateFormat.format(calendar.getTime());
                        } else if (checkedId == R.id.activity_attend_admin_radiobutton_tardy_20_min) {
                            calendar.add(Calendar.MINUTE, 20);
                            date_Trady = simpleDateFormat.format(calendar.getTime());
                        } else {
                            calendar.add(Calendar.MINUTE, 30);
                            date_Trady = simpleDateFormat.format(calendar.getTime());
                        }

                        flag2++;
                    }
                });

                activity_attend_admin_button_attendance_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        if (flag > 0 && flag2 > 0) {
                            now = System.currentTimeMillis();
                            // 현재시간을 date 변수에 저장한다.
                            Date date = new Date(now);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            formatDate = simpleDateFormat.format(date);

                            certification_number = random_number.nextInt(maxNumber - minNumber + 1) + minNumber;
                            // 인즌번호가 1000~9999 4자리 수중에서 랜덤으로 결정된다.

                            Attend_Admin_Item attendItem = new Attend_Admin_Item();
                            attendItem.startTime = formatDate;
                            attendItem.attendTimeLimit = date_Attend;
                            attendItem.tardyTimeLimit = date_Trady;

                            findkey = database.getReference().push().getKey();
                            database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).setValue(attendItem);
                            database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("Attend_Certification_Number").setValue(certification_number);

                            // 회원 가입한 날짜와 현재 날짜를 비교해서 출석을 시작 하고 난 후에 회원가입을 하면 그 전에 했던 출석에 포함되지 않아야 한다.

                            database.getReference().child("EveryClub").child(clubName).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        // 파이어베이스 User에 있는 키값을 하나씩 찾아서 그 키값에서 이름과 전화번호를 가지고 온다
                                        getName = snapshot.child("name").getValue(String.class);
                                        getPhone = snapshot.child("phone").getValue(String.class);
                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(snapshot.getKey()).child("name").setValue(getName);
                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(snapshot.getKey()).child("phone").setValue(getPhone);
                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(snapshot.getKey()).child("attend_state").setValue("미출결");

                                        Attend_Admin_Change_Item attendAdminChangeItem = new Attend_Admin_Change_Item();
                                        attendAdminChangeItem.name = getName;
                                        attendAdminChangeItem.attend_state = "미출결";
                                        attendAdminChangeItem.phone = getPhone;
                                    }
                                }

                                @Override
                                public void onCancelled(final DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(AttendActivity_Home.this, "출석시간이 정해졌습니다", Toast.LENGTH_SHORT).show();
/*                            Intent intent = new Intent(AttendActivity_Admin.this, AttendActivity_Home.class);
                            intent.putExtra("findKey", findkey);
                            startActivity(intent);*/
                            dialog.dismiss();
                        } else if (flag == 0) {
                            Toast.makeText(AttendActivity_Home.this, "출석시간을 정해주세요", Toast.LENGTH_SHORT).show();
                        } else if (flag2 == 0) {
                            Toast.makeText(AttendActivity_Home.this, "지각시간을 정해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                progressDialog.dismiss();
/*                Intent intent = new Intent(AttendActivity_Home.this, AttendActivity_Admin.class);
                startActivity(intent);*/
            }
        });

/*        activity_attend_home_button_information = (Button) findViewById(R.id.activity_attend_home_button_information);
        activity_attend_home_button_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendActivity_Home.this, AttendActivity_Admin_Home.class);
                startActivity(intent);
            }
        });*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

        Uri file = Uri.fromFile(new File(getPath(data.getData())));
        StorageReference riversRef = storageRef.child("EveryClub").child(clubName).child("Attend/" + file.getLastPathSegment());
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


    // AttendActivity 어댑터

    class AttendActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_home_item_linearlayout;
            TextView activity_attend_home_item_textview_recyclerview_start_time;
            TextView activity_attend_home_item_recyclerview_attend_time_limit_tilte;
            TextView activity_attend_home_item_recyclerview_attend_time_limit;
            TextView activity_attend_home_item_textview_recyclerview_tardy_time_limit_title;
            TextView activity_attend_home_item_textview_recyclerview_tardy_time_limit;
            TextView activity_attend_home_item_textview_recyclerview_attend_state;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_home_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_home_item_linearlayout);
                activity_attend_home_item_textview_recyclerview_attend_state = (TextView) view.findViewById(R.id.activity_attend_home_item_textview_recyclerview_attend_state);
                activity_attend_home_item_textview_recyclerview_start_time = (TextView) view.findViewById(R.id.activity_attend_home_item_textview_recyclerview_start_time);
                activity_attend_home_item_recyclerview_attend_time_limit_tilte = (TextView) view.findViewById(R.id.activity_attend_home_item_recyclerview_attend_time_limit_tilte);
                activity_attend_home_item_recyclerview_attend_time_limit = (TextView) view.findViewById(R.id.activity_attend_home_item_recyclerview_attend_time_limit);
                activity_attend_home_item_textview_recyclerview_tardy_time_limit = (TextView) view.findViewById(R.id.activity_attend_home_item_textview_recyclerview_tardy_time_limit);
                activity_attend_home_item_textview_recyclerview_tardy_time_limit_title = (TextView) view.findViewById(R.id.activity_attend_home_item_textview_recyclerview_tardy_time_limit_title);

            }

        }

        public void PopupMenu(final AttendActivity_Home.AttendActivity_AdminRecyclerViewAdapter.CustomViewHolder viewholder, final int position) {
            viewholder.activity_attend_home_item_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final PopupMenu popup = new PopupMenu(view.getContext(), view);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.attend_attend:

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendActivity_Home.this);

                                    View view = LayoutInflater.from(AttendActivity_Home.this)
                                            .inflate(R.layout.activity_attend_check, null, false);
                                    builder.setView(view);

                                    final EditText activity_attend_check_edittext_certification_number = (EditText) view.findViewById(R.id.activity_attend_check_edittext_certification_number);
                                    final Button activity_attend_check_confirm = (Button) view.findViewById(R.id.activity_attend_check_button_confirm);
                                    final Button activity_attend_check_cancel = (Button) view.findViewById(R.id.activity_attend_check_button_cancel);

                                    final AlertDialog dialog = builder.create();

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                if (dataSnapshot.getValue().toString().equals("출석") || dataSnapshot.getValue().toString().equals("지각")) {
                                                    dialog.dismiss();
                                                    Toast.makeText(AttendActivity_Home.this, "이미 출석을 했습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("Attend_Certification_Number").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() == null) {
                                                Toast.makeText(AttendActivity_Home.this, "출석중이 아닙니다", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("tardyTimeLimit").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() == null) {
                                                Toast.makeText(AttendActivity_Home.this, "출석중이 아닙니다", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } else {
                                                now = System.currentTimeMillis();
                                                // 현재시간을 date 변수에 저장한다.
                                                Date date = new Date(now);
                                                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                nowtardyTimeLimit = simpleDateFormat.format(date);

                                                getTardyTimeLimit = dataSnapshot.getValue().toString();
                                                Date d2 = simpleDateFormat.parse(nowtardyTimeLimit, new ParsePosition(0));
                                                Date d1 = simpleDateFormat.parse(getTardyTimeLimit, new ParsePosition(0));
                                                long diff = d1.getTime() - d2.getTime();
                                                if (diff < 0) {
                                                    Toast.makeText(AttendActivity_Home.this, "출석중이 아닙니다", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });

                                    activity_attend_check_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View view) {
                                            EditCertificationNumber = activity_attend_check_edittext_certification_number.getText().toString();
                                            EditCertificationNumber.trim();

                                            if (EditCertificationNumber.getBytes().length > 0) {
                                                getEditCertificationNumber = Integer.parseInt(activity_attend_check_edittext_certification_number.getText().toString());
                                                database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("Attend_Certification_Number").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(final DataSnapshot dataSnapshot) {
                                                        getCertificationNumber = dataSnapshot.getValue().toString();

                                                        if (Integer.parseInt(getCertificationNumber) == getEditCertificationNumber) {

                                                            database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("attendTimeLimit").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                    getAttend_Time_Limit = dataSnapshot.getValue().toString();

                                                                    now = System.currentTimeMillis();
                                                                    // 현재시간을 date 변수에 저장한다.
                                                                    Date date = new Date(now);
                                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                                    // "yyyy-MM-dd HH:mm"
                                                                    nowDate = simpleDateFormat.format(date);
                                                                    Date d2 = simpleDateFormat.parse(nowDate, new ParsePosition(0));
                                                                    Date d1 = simpleDateFormat.parse(getAttend_Time_Limit, new ParsePosition(0));
                                                                    long diff = d1.getTime() - d2.getTime();
                                                                    // 출석 끝나는 시간과 현재 시간을 비교해서 출석인지 지각인지 확인하기 위해서

                                                                    if (diff > 0) {
                                                                        Toast.makeText(AttendActivity_Home.this, "출석이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").setValue("출석");
                                                                        dialog.dismiss();
                                                                    } else {
                                                                        database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("tardyTimeLimit").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                                getTardy_Time_Limit = dataSnapshot.getValue().toString();

                                                                                now = System.currentTimeMillis();
                                                                                // 현재시간을 date 변수에 저장한다.
                                                                                Date date = new Date(now);
                                                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                                                // "yyyy-MM-dd HH:mm"
                                                                                nowDate = simpleDateFormat.format(date);
                                                                                Date d2 = simpleDateFormat.parse(nowDate, new ParsePosition(0));
                                                                                Date d1 = simpleDateFormat.parse(getTardy_Time_Limit, new ParsePosition(0));
                                                                                long diff = d1.getTime() - d2.getTime();
                                                                                // 지각 끝나는 시간과 현재 시간을 비교해서 지각인지 결석인지 확인하기 위해서

                                                                                if (diff > 0) {
                                                                                    Toast.makeText(AttendActivity_Home.this, "출석시간이 지났습니다(지각)", Toast.LENGTH_SHORT).show();
                                                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").setValue("지각").toString();
                                                                                    dialog.dismiss();
                                                                                } else {
                                                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                                getState = snapshot.child("attend_state").getValue(String.class);
                                                                                                if (getState.equals("미출결")) {
                                                                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").child(snapshot.getKey()).setValue("결석");
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(final DatabaseError databaseError) {

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(final DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(final DatabaseError databaseError) {

                                                                }
                                                            });

                                                        } else {
                                                            Toast.makeText(AttendActivity_Home.this, "인증번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(final DatabaseError databaseError) {

                                                    }
                                                });

                                            } else {
                                                Toast.makeText(AttendActivity_Home.this, "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                    activity_attend_check_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();

                                    return true;

                                case R.id.attend_detail:

                                    Intent intent = new Intent(AttendActivity_Home.this, AttendActivity_Activity.class);
                                    uidAdminPath = database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).getKey();
                                    intent.putExtra("uidAdminPath", uidAdminPath);
                                    intent.putExtra("checkPage", 1);

/*                                    Bundle bundle = new Bundle();
                                    bundle.putString("uidAdminPath", uidAdminPath);
                                    Fragment fragment = new AttendActivity_Fragment();
                                    fragment.setArguments(bundle);*/

                                    startActivity(intent);

                                    return true;

                                case R.id.attend_delete:

                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AttendActivity_Home.this);

                                    View view2 = LayoutInflater.from(AttendActivity_Home.this)
                                            .inflate(R.layout.activity_attend_admin_delete, null, false);
                                    builder2.setView(view2);

                                    final Button confirmButton = (Button) view2.findViewById(R.id.activity_attend_admin_delete_item_button_confirm);
                                    final Button cancelButton = (Button) view2.findViewById(R.id.activity_attend_admin_delete_item_button_cancel);

                                    final AlertDialog dialog2 = builder2.create();

                                    confirmButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            Toast.makeText(AttendActivity_Home.this, "출석이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                            delete_content(position);
                                            attenditems.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, attenditems.size());
                                            dialog2.dismiss();
                                        }
                                    });

                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            dialog2.dismiss();
                                        }
                                    });

                                    dialog2.show();
                                    return true;

                                default:
                                    return false;
                            }
                            //return false;
                        }
                    });

                    popup.inflate(R.menu.attend_home_popup);

                    if (admin > adminNumber) {
                        popup.getMenu().getItem(2).setVisible(false);
                    }

                    popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                    popup.show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_attend_home_item, viewGroup, false);

            return new AttendActivity_Home.AttendActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final AttendActivity_Home.AttendActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((AttendActivity_Home.AttendActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_home_item_textview_recyclerview_attend_state.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_home_item_textview_recyclerview_start_time.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_home_item_recyclerview_attend_time_limit.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_home_item_textview_recyclerview_tardy_time_limit.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_home_item_textview_recyclerview_start_time.setText(attenditems.get(position).startTime);
            customViewHolder.activity_attend_home_item_recyclerview_attend_time_limit.setText(attenditems.get(position).attendTimeLimit);
            customViewHolder.activity_attend_home_item_textview_recyclerview_tardy_time_limit.setText(attenditems.get(position).tardyTimeLimit);

            PopupMenu(customViewHolder, position);

/*            database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("Attend_Certification_Number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        customViewHolder.activity_attend_home_item_recyclerview_attend_time_limit.setVisibility(View.GONE);
                        customViewHolder.activity_attend_home_item_textview_recyclerview_tardy_time_limit.setVisibility(View.GONE);
                        customViewHolder.activity_attend_home_item_recyclerview_attend_time_limit_tilte.setVisibility(View.GONE);
                        customViewHolder.activity_attend_home_item_textview_recyclerview_tardy_time_limit_title.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(final DatabaseError databaseError) {

                }
            });*/

            database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        if (dataSnapshot.getValue().equals("출석")) {
                            customViewHolder.activity_attend_home_item_textview_recyclerview_attend_state.setText("출석");
                        } else if (dataSnapshot.getValue().equals("지각")) {
                            customViewHolder.activity_attend_home_item_textview_recyclerview_attend_state.setText("지각");
                        } else if (dataSnapshot.getValue().equals("결석")) {
                            customViewHolder.activity_attend_home_item_textview_recyclerview_attend_state.setText("결석");
                        }
                    }
                }

                @Override
                public void onCancelled(final DatabaseError databaseError) {

                }
            });

            database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    getTardyTimeLimit = dataSnapshot.child("tardyTimeLimit").getValue(String.class);
                    if (getTardyTimeLimit != null) {
                        now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        nowtardyTimeLimit = simpleDateFormat.format(date);

                        Date d2 = simpleDateFormat.parse(nowtardyTimeLimit, new ParsePosition(0));
                        Date d1 = simpleDateFormat.parse(getTardyTimeLimit, new ParsePosition(0));
                        long diff = d1.getTime() - d2.getTime();
                        if (diff < 0) {
                            database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.child("attend_state").getValue().equals("미출결")) {
                                            database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("User_State").child(snapshot.getKey()).child("attend_state").setValue("결석");
                                        }
                                    }
                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("Attend_Certification_Number").removeValue();
                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("attendTimeLimit").removeValue();
                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).child("tardyTimeLimit").removeValue();
                                }

                                @Override
                                public void onCancelled(final DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                }

                @Override
                public void onCancelled(final DatabaseError databaseError) {

                }
            });

        }

        private void delete_content(final int position) {

            database.getReference().child("EveryClub").child(clubName).child("Attend").child(uidLists.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {
                    Toast.makeText(AttendActivity_Home.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return attenditems.size();
        }

    }

}