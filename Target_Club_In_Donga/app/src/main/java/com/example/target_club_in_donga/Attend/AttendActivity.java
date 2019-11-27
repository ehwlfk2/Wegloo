package com.example.target_club_in_donga.Attend;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.PushMessages.SendPushMessages;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.thisClubName;

public class AttendActivity extends AppCompatActivity {

    private Button activity_attend_home_admin_button_insert;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private int admin;
    public static String uidPath;

    private long now;
    private String formatDate, nowtardyTimeLimit, getTardyTimeLimit;
    private static int adminNumber = 2;

    private ProgressDialog progressDialog;

    private Date attenddate;
    private String date_Attend, date_Trady;
    private int flag = 0, flag2 = 0, flag3 = 0, flag4 = 0;
    private int certification_number;
    private int minNumber = 1000, maxNumber = 9999;
    private String findkey, getName, getPhone;

    private int getEditCertificationNumber;
    private String getCertificationNumber, EditCertificationNumber;
    private String getAttend_Time_Limit, getTardy_Time_Limit, getStart_Time;
    private String nowDate;
    private String getState;

    private TextView activity_attend_home_textview;
    private Button activity_attend_home_button_attend, activity_attend_home_button_datail, activity_attend_home_button_number, activity_attend_home_button_cancel;
    private LinearLayout activity_attend_home_linearlayout_user, activity_attend_home_linearlayout_admin;
    private ImageButton activity_attend_home_imagebutton_back;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_home);

        final Random random_number = new Random();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        activity_attend_home_textview = (TextView) findViewById(R.id.activity_attend_home_textview);
        activity_attend_home_button_attend = (Button) findViewById(R.id.activity_attend_home_button_attend);
        activity_attend_home_button_datail = (Button) findViewById(R.id.activity_attend_home_button_datail);
        activity_attend_home_button_number = (Button) findViewById(R.id.activity_attend_home_button_number);
        activity_attend_home_button_cancel = (Button) findViewById(R.id.activity_attend_home_button_cancel);
        activity_attend_home_admin_button_insert = (Button) findViewById(R.id.activity_attend_home_button_insert);

        activity_attend_home_linearlayout_user = (LinearLayout) findViewById(R.id.activity_attend_home_linearlayout_user);
        activity_attend_home_linearlayout_admin = (LinearLayout) findViewById(R.id.activity_attend_home_linearlayout_admin);

        activity_attend_home_imagebutton_back = (ImageButton) findViewById(R.id.activity_attend_home_imagebutton_back);

        activity_attend_home_imagebutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(AttendActivity.this);

        progressDialog.setMessage("출석을 불러오는 중입니다...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                admin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                if (admin > adminNumber) {
                    activity_attend_home_admin_button_insert.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        database.getReference().child("EveryClub").child(clubName).child("Attend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (final DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                        if (snapshot2.child("Attend_Certification_Number").getValue() != null) {
                            flag3 = 1;
                            activity_attend_home_textview.setText("출석 중입니다");
                            activity_attend_home_linearlayout_user.setVisibility(View.VISIBLE);
                            activity_attend_home_admin_button_insert.setVisibility(View.GONE);

                            activity_attend_home_button_number.setText("인증번호 " + snapshot2.child("Attend_Certification_Number").getValue().toString());

                            activity_attend_home_button_attend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendActivity.this);

                                    View view = LayoutInflater.from(AttendActivity.this)
                                            .inflate(R.layout.activity_attend_check, null, false);
                                    builder.setView(view);

                                    final EditText activity_attend_check_edittext_certification_number = (EditText) view.findViewById(R.id.activity_attend_check_edittext_certification_number);
                                    final Button activity_attend_check_confirm = (Button) view.findViewById(R.id.activity_attend_check_button_confirm);
                                    final Button activity_attend_check_cancel = (Button) view.findViewById(R.id.activity_attend_check_button_cancel);

                                    final AlertDialog dialog = builder.create();

                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                if (dataSnapshot.getValue().toString().equals("출석") || dataSnapshot.getValue().toString().equals("지각")) {
                                                    dialog.dismiss();
                                                    Toast.makeText(AttendActivity.this, "이미 출석을 했습니다.", Toast.LENGTH_SHORT).show();
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
                                                database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(final DataSnapshot dataSnapshot) {
                                                        getCertificationNumber = dataSnapshot.child("Attend_Certification_Number").getValue().toString();

                                                        if (Integer.parseInt(getCertificationNumber) == getEditCertificationNumber) {
                                                            getAttend_Time_Limit = dataSnapshot.child("attendTimeLimit").getValue().toString();

                                                            now = System.currentTimeMillis();
                                                            // 현재시간을 date 변수에 저장한다.
                                                            Date date = new Date(now);
                                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                            // "yyyy-MM-dd HH:mm"
                                                            nowDate = simpleDateFormat.format(date);
                                                            Date d_nowDate = simpleDateFormat.parse(nowDate, new ParsePosition(0));
                                                            Date d_getAttend_Time_Limit = simpleDateFormat.parse(getAttend_Time_Limit, new ParsePosition(0));
                                                            long diff = d_getAttend_Time_Limit.getTime() - d_nowDate.getTime();
                                                            // 출석 끝나는 시간과 현재 시간을 비교해서 출석인지 지각인지 확인하기 위해서

                                                            if (diff > 0) {
                                                                Toast.makeText(AttendActivity.this, "출석이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                                database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").setValue("출석");
                                                                dialog.dismiss();
                                                            } else {
                                                                getTardy_Time_Limit = dataSnapshot.child("tardyTimeLimit").getValue().toString();
                                                                Date d_getTardy_Time_Limit = simpleDateFormat.parse(getTardy_Time_Limit, new ParsePosition(0));
                                                                long diff2 = d_getTardy_Time_Limit.getTime() - d_nowDate.getTime();
                                                                // 지각 끝나는 시간과 현재 시간을 비교해서 지각인지 결석인지 확인하기 위해서
                                                                Log.e("값", diff2 + "");

                                                                if (diff2 > 0) {
                                                                    Toast.makeText(AttendActivity.this, "지각을 하였습니다.", Toast.LENGTH_SHORT).show();
                                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").child(auth.getCurrentUser().getUid()).child("attend_state").setValue("지각").toString();
                                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").child(auth.getCurrentUser().getUid()).child("late_time").setValue("+" + (d_nowDate.getTime() - d_getAttend_Time_Limit.getTime()) / 60000 + "분").toString();
                                                                    dialog.dismiss();
                                                                }
                                                            }

                                                        } else {
                                                            Toast.makeText(AttendActivity.this, "인증번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(final DatabaseError databaseError) {

                                                    }
                                                });

                                            } else {
                                                Toast.makeText(AttendActivity.this, "인증번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                                }
                            });

                            getTardyTimeLimit = snapshot2.child("tardyTimeLimit").getValue(String.class);
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
                                    flag4 = 1;
                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("Attend_Certification_Number").removeValue();
                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("attendTimeLimit").removeValue();
                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("tardyTimeLimit").removeValue();

                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    if (snapshot.getValue() != null) {
                                                        getState = snapshot.child("attend_state").getValue().toString();
                                                        if (getState.equals("미출결")) {
                                                            database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").child(snapshot.getKey()).child("attend_state").setValue("결석");
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });
                                    activity_attend_home_textview.setText("출석 중이 아니에요");
                                    activity_attend_home_linearlayout_user.setVisibility(View.GONE);
                                    activity_attend_home_linearlayout_admin.setVisibility(View.GONE);
                                    database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            admin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                                            if (admin <= adminNumber) {
                                                activity_attend_home_admin_button_insert.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(final DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            activity_attend_home_button_datail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    Intent intent = new Intent(AttendActivity.this, AttendActivity_Detail_Information.class);
                                    uidPath = database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).getKey();
                                    intent.putExtra("uidPath", uidPath);
                                    intent.putExtra("checkPage", 1);

                                    startActivity(intent);
                                }
                            });

                            activity_attend_home_button_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AttendActivity.this);

                                    View view2 = LayoutInflater.from(AttendActivity.this)
                                            .inflate(R.layout.activity_attend_admin_delete, null, false);
                                    builder2.setView(view2);

                                    final Button confirmButton = (Button) view2.findViewById(R.id.activity_attend_admin_delete_item_button_confirm);
                                    final Button cancelButton = (Button) view2.findViewById(R.id.activity_attend_admin_delete_item_button_cancel);

                                    final AlertDialog dialog2 = builder2.create();

                                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    confirmButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            delete_content(snapshot2.getKey());
                                            flag4 = 1;
                                            activity_attend_home_textview.setText("출석 중이 아니에요");
                                            activity_attend_home_linearlayout_user.setVisibility(View.GONE);
                                            activity_attend_home_linearlayout_admin.setVisibility(View.GONE);
                                            database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                                    admin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                                                    if (admin <= adminNumber) {
                                                        activity_attend_home_admin_button_insert.setVisibility(View.VISIBLE);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(final DatabaseError databaseError) {

                                                }
                                            });
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

                                }

                            });

                        }

                    }

                    if (flag3 == 1) {
                        database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                admin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                                if (admin <= adminNumber && flag4 != 1) {
                                    activity_attend_home_linearlayout_admin.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(final DatabaseError databaseError) {

                            }
                        });
                    }

/*                    if (flag4 == 1) {
                        activity_attend_home_textview.setText("출석 중이 아니에요");
                        activity_attend_home_admin_button_insert.setVisibility(View.VISIBLE);
                        activity_attend_home_linearlayout_user.setVisibility(View.GONE);
                        activity_attend_home_linearlayout_admin.setVisibility(View.GONE);
                    }*/

                    flag3 = 0;
                } else {
                    activity_attend_home_textview.setText("출석 중이 아니에요");
                    activity_attend_home_linearlayout_user.setVisibility(View.GONE);
                    activity_attend_home_linearlayout_admin.setVisibility(View.GONE);
                    database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            admin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                            if (admin <= adminNumber) {
                                activity_attend_home_admin_button_insert.setVisibility(View.VISIBLE);
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

        progressDialog.dismiss();

        activity_attend_home_admin_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag4 = 0;
                progressDialog.setMessage("출석을 추가하는 중입니다...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(AttendActivity.this);

                View view = LayoutInflater.from(AttendActivity.this)
                        .inflate(R.layout.activity_attend_admin, null, false);
                builder.setView(view);

                final RadioGroup activity_attend_admin_radiogroup_attend = (RadioGroup) view.findViewById(R.id.activity_attend_admin_radiogroup_attend);
                final RadioGroup activity_attend_admin_radiogroup_tardy = (RadioGroup) view.findViewById(R.id.activity_attend_admin_radiogroup_tardy);
                final Button activity_attend_admin_button_attendance_start = (Button) view.findViewById(R.id.activity_attend_admin_button_attendance_start);
                final Button activity_attend_admin_button_attendance_cancel = (Button) view.findViewById(R.id.activity_attend_admin_button_attendance_cancel);

                final AlertDialog dialog = builder.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                activity_attend_admin_button_attendance_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        dialog.dismiss();
                    }
                });

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


                            database.getReference().child("EveryClub").child(clubName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("realNameSystem").getValue().toString().equals("true")) {
                                        for (final DataSnapshot snapshot2 : dataSnapshot.child("User").getChildren()) {
                                            database.getReference().child("AppUser").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        if (snapshot.getKey().equals(snapshot2.getKey())) {
                                                            // 파이어베이스 AppUser에 있는 키값을 하나씩 찾아서 그 키값에서 이름과 전화번호를 가지고 온다
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
                                                }

                                                @Override
                                                public void onCancelled(final DatabaseError databaseError) {

                                                }
                                            });
                                        }


                                    } else {
                                        database.getReference().child("EveryClub").child(clubName).child("User").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    // 파이어베이스 clubName -> User에 있는 키값을 하나씩 찾아서 그 키값에서 이름과 전화번호를 가지고 온다
                                                    getName = snapshot.child("name").getValue(String.class);

                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(snapshot.getKey()).child("name").setValue(getName);
//                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(snapshot.getKey()).child("phone").setValue("전화번호 없음");
                                                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(findkey).child("User_State").child(snapshot.getKey()).child("attend_state").setValue("미출결");

                                                    Attend_Admin_Change_Item attendAdminChangeItem = new Attend_Admin_Change_Item();
                                                    attendAdminChangeItem.name = getName;
                                                    attendAdminChangeItem.attend_state = "미출결";
//                                                    attendAdminChangeItem.phone = "전화번호 없음";
                                                }

                                            }

                                            @Override
                                            public void onCancelled(final DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    //Log.e("sned",thisClubName);
                                    SendPushMessages send = new SendPushMessages();
                                    send.multipleSendMessage(thisClubName, "출석체크가 시작되었습니다.", "Attend", clubName);
                                }

                                @Override
                                public void onCancelled(final DatabaseError databaseError) {

                                }
                            });


                            Toast.makeText(AttendActivity.this, "출석시간이 정해졌습니다.", Toast.LENGTH_SHORT).show();


                            dialog.dismiss();
                        } else if (flag == 0) {
                            Toast.makeText(AttendActivity.this, "출석시간을 정해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (flag2 == 0) {
                            Toast.makeText(AttendActivity.this, "지각시간을 정해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

        Uri file = Uri.fromFile(new File(getPath(data.getData())));
        StorageReference riversRef = storageRef.child("EveryClub").child(clubName).child("AttendActivity/" + file.getLastPathSegment());
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
        CursorLoader cursorLoader = new CursorLoader(AttendActivity.this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }

    private void delete_content(final String position) {

        database.getReference().child("EveryClub").child(clubName).child("Attend").child(position).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(final Void aVoid) {
                Toast.makeText(AttendActivity.this, "출석이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(AttendActivity.this, HomeActivityView.class);
            intent.putExtra("isRecent", true);
            startActivity(intent);
            finish();
            //// This is last activity
        }
        super.onBackPressed();
    }

}