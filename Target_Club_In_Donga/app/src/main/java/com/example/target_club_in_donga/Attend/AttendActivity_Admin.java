package com.example.target_club_in_donga.Attend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_Admin extends AppCompatActivity {

    private Button activity_attend_admin_attendance_start;
    private RadioGroup activity_attend_admin_radiogroup_attend, activity_attend_admin_radiogroup_tardy;
    private int certification_number;
    private long now;
    private String date_Attend, date_Trady;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private Date attenddate;

    private int flag = 0, flag2 = 0;

    private String formatDate;
    private String getName, getPhone;

    private int minNumber = 1000, maxNumber = 9999;
    private String findkey;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_admin);

        final Random random_number = new Random();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        activity_attend_admin_radiogroup_attend = (RadioGroup) findViewById(R.id.activity_attend_admin_radiogroup_attend);
        activity_attend_admin_radiogroup_tardy = (RadioGroup) findViewById(R.id.activity_attend_admin_radiogroup_tardy);
        activity_attend_admin_attendance_start = (Button) findViewById(R.id.activity_attend_admin_button_attendance_start);

        now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        attenddate = new Date(now);

        // 출석시간을 결정
        activity_attend_admin_radiogroup_attend.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
                now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                //"yyyy-MM-dd HH:mm"

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                if (i == R.id.activity_attend_admin_radiobutton_attend_5_min) {
                    calendar.add(Calendar.MINUTE, 5);
                    date_Attend = simpleDateFormat.format(calendar.getTime());
                    attenddate = calendar.getTime();
                } else if (i == R.id.activity_attend_admin_radiobutton_attend_10_min) {
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
            public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                //"yyyy-MM-dd HH:mm"

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(attenddate);

                if (i == R.id.activity_attend_admin_radiobutton_tardy_10_min) {
                    calendar.add(Calendar.MINUTE, 10);
                    date_Trady = simpleDateFormat.format(calendar.getTime());
                } else if (i == R.id.activity_attend_admin_radiobutton_tardy_20_min) {
                    calendar.add(Calendar.MINUTE, 20);
                    date_Trady = simpleDateFormat.format(calendar.getTime());
                } else {
                    calendar.add(Calendar.MINUTE, 30);
                    date_Trady = simpleDateFormat.format(calendar.getTime());
                }

                flag2++;
            }
        });

        activity_attend_admin_attendance_start.setOnClickListener(new View.OnClickListener() {
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
                    attendItem.clubName = clubName;
                    attendItem.startTime = formatDate;
                    attendItem.attendTimeLimit = date_Attend;
                    attendItem.tardyTimeLimit = date_Trady;

                    findkey = database.getReference().push().getKey(); // 대여를 했을 떄 기록을 남기기 위해 데이터베이스에 저장함
                    database.getReference().child(clubName).child("Attend").child(findkey).setValue(attendItem);
                    database.getReference().child(clubName).child("Attend").child(findkey).child("Attend_Certification_Number").setValue(certification_number);

                    database.getReference().child(clubName).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // 파이어베이스 User에 있는 키값을 하나씩 찾아서 그 키값에서 이름과 전화번호를 가지고 온다
                                getName = snapshot.child("name").getValue(String.class);
                                getPhone = snapshot.child("phone").getValue(String.class);
                                database.getReference().child(clubName).child("Attend").child(findkey).child("User_Statue").child(snapshot.getKey()).child("name").setValue(getName);
                                database.getReference().child(clubName).child("Attend").child(findkey).child("User_Statue").child(snapshot.getKey()).child("phone").setValue(getPhone);
                                database.getReference().child(clubName).child("Attend").child(findkey).child("User_Statue").child(snapshot.getKey()).child("attend_statue").setValue("미출결");

                                Attend_Admin_Change_Item attendAdminChangeItem = new Attend_Admin_Change_Item();
                                attendAdminChangeItem.Name = getName;
                                attendAdminChangeItem.attendStatue = "미출결";
                                attendAdminChangeItem.phone = getPhone;
                            }
                        }

                        @Override
                        public void onCancelled(final DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(AttendActivity_Admin.this, "출석시간이 정해졌습니다", Toast.LENGTH_SHORT).show();
/*                    Intent intent = new Intent(AttendActivity_Admin.this, AttendActivity_Home.class);
                    intent.putExtra("findKey", findkey);
                    startActivity(intent);*/
                    finish();
                } else if (flag == 0) {
                    Toast.makeText(AttendActivity_Admin.this, "출석시간을 정해주세요", Toast.LENGTH_SHORT).show();
                } else if (flag2 == 0) {
                    Toast.makeText(AttendActivity_Admin.this, "지각시간을 정해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}