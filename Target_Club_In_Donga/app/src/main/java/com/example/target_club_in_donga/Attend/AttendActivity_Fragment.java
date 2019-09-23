package com.example.target_club_in_donga.Attend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendActivity_Fragment extends Fragment {

    private Gallery gallery;
    private Button select_btn;
    private Integer current_image_resource;
    private TextView activity_attend_textview_attend_statue;

    private final int[] img = {R.drawable.aa, R.drawable.bb, R.drawable.cc, R.drawable.dd, R.drawable.ee};

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private Button activity_attend_button_attendance, activity_attend_button_cancel, activity_attend_button_admin;
    private TextView activity_attend_textview_people_count, activity_attend_textview_people_percent;
    private int peopleCount = 0, peopleAttendCount = 0;

    private int getEditCertificationNumber;
    private String getCertificationNumber, EditCertificationNumber;
    private String getAttend_Time_Limit, getTardy_Time_Limit;

    private long now;
    private String nowDate, formatDate, nowtardyTimeLimit;
    private String getAttendStatue, setAttendStatue, getTardyTimeLimit;

    private int admin;

    PieChart activity_attend_piechart;

    public AttendActivity_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_attend, container, false);
        Gallery_Adapter galleryAdapter = new Gallery_Adapter(getContext(), R.layout.activity_attend_sub_layout, img);
        gallery = (Gallery) view.findViewById(R.id.activity_attend_gallery);
        select_btn = (Button) view.findViewById(R.id.activity_attend_button_select);

        activity_attend_button_admin = (Button) view.findViewById(R.id.activity_attend_button_admin);
        activity_attend_button_attendance = (Button) view.findViewById(R.id.activity_attend_button_attendance);
        activity_attend_button_cancel = (Button) view.findViewById(R.id.activity_attend_button_cancel);
        activity_attend_textview_attend_statue = (TextView) view.findViewById(R.id.activity_attend_textview_attend_statue);
        activity_attend_textview_people_count = (TextView) view.findViewById(R.id.activity_attend_textview_people_count);
        activity_attend_textview_people_percent = (TextView) view.findViewById(R.id.activity_attend_textview_people_percent);
        activity_attend_piechart = (PieChart) view.findViewById(R.id.activity_attend_piechart);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        gallery.setAdapter(galleryAdapter);
        final ImageView imageView = (ImageView) view.findViewById(R.id.test);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_image_resource = img[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(current_image_resource);
            }
        });

        now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formatDate = simpleDateFormat.format(date);

        database.getReference().child("Attend_Admin").child(formatDate).child("Admin").child("Tardy_Time_Limit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
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
                        database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.child("attend_statue").getValue(String.class);
                                    if (snapshot.child("attend_statue").getValue(String.class).equals("미출결")) {
                                        database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").child(snapshot.getKey()).child("attend_statue").setValue("결석");
                                    }
                                }
                                database.getReference().child("Attend_Admin").child(formatDate).child("Admin").removeValue();
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

        database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        peopleCount++;
                        getAttendStatue = snapshot.child("attend_statue").getValue(String.class);
                        Log.d("오류", getAttendStatue);
                        if (getAttendStatue.equals("출석") || getAttendStatue.equals("지각")) {
                            peopleAttendCount++;
                            Log.d("값", peopleAttendCount + "");
                        }
                        activity_attend_textview_people_count.setText(peopleAttendCount + "명");
                        activity_attend_textview_people_percent.setText((peopleAttendCount * 100 / peopleCount) + "%");
                    }
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").child(auth.getCurrentUser().getUid()).child("attend_statue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class) != null) {
                    setAttendStatue = dataSnapshot.getValue().toString();
                    activity_attend_textview_attend_statue.setText(setAttendStatue);
                    if (setAttendStatue.equals("출석")) {
                        activity_attend_textview_attend_statue.setBackgroundResource(R.drawable.border_green);
                    } else if (setAttendStatue.equals("지각")) {
                        activity_attend_textview_attend_statue.setBackgroundResource(R.drawable.border_orange);
                    } else if (setAttendStatue.equals("결석")) {
                        activity_attend_textview_attend_statue.setBackgroundResource(R.drawable.border_gray);
                    } else {
                        activity_attend_textview_attend_statue.setBackgroundResource(R.drawable.border_orange);
                    }
                }
                return;
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_attend_button_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.activity_attend_check, null, false);
                builder.setView(view);

                final EditText activity_attend_check_edittext_certification_number = (EditText) view.findViewById(R.id.activity_attend_check_edittext_certification_number);
                final Button activity_attend_check_confirm = (Button) view.findViewById(R.id.activity_attend_check_button_confirm);
                final Button activity_attend_check_cancel = (Button) view.findViewById(R.id.activity_attend_check_button_cancel);

                final AlertDialog dialog = builder.create();

                database.getReference().child("Attend_Admin").child(formatDate).child("Admin").child("Attend_Certification_Number").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(getActivity(), "출석중이 아닙니다", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(final DatabaseError databaseError) {

                    }
                });

                database.getReference().child("Attend_Admin").child(formatDate).child("Admin").child("Tardy_Time_Limit").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(getActivity(), "출석중이 아닙니다", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), "출석중이 아닙니다", Toast.LENGTH_SHORT).show();
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
                            database.getReference().child("Attend_Admin").child(formatDate).child("Admin").child("Attend_Certification_Number").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    getCertificationNumber = dataSnapshot.getValue().toString();

                                    if (Integer.parseInt(getCertificationNumber) == getEditCertificationNumber) {

                                        database.getReference().child("Attend_Admin").child(formatDate).child("Admin").child("Attend_Time_Limit").addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                    Toast.makeText(getActivity(), "출석이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                                    database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").child(auth.getCurrentUser().getUid()).child("attend_statue").setValue("출석");
                                                    dialog.dismiss();
                                                } else {
                                                    database.getReference().child("Attend_Admin").child(formatDate).child("Admin").child("Tardy_Time_Limit").addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                                Toast.makeText(getActivity(), "출석시간이 지났습니다(지각)", Toast.LENGTH_SHORT).show();
                                                                activity_attend_textview_attend_statue.setText("지각");
                                                                database.getReference().child("Attend_Admin").child(formatDate).child("User_Statue").child(auth.getCurrentUser().getUid()).child("attend_statue").setValue("지각").toString();
                                                                dialog.dismiss();
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
                                        Toast.makeText(getActivity(), "인증번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(final DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Toast.makeText(getActivity(), "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                activity_attend_check_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        getActivity().finish();
                    }
                });

                dialog.show();
            }
        });

        activity_attend_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getActivity().finish();
            }
        });


        database.getReference().child("User").child(auth.getCurrentUser().getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                admin = Integer.parseInt(dataSnapshot.getValue().toString());

                if (admin > 0) {
                    activity_attend_button_admin.setVisibility(View.VISIBLE);
                } else {
                    activity_attend_button_admin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_attend_button_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), AttendActivity_Admin.class);
                startActivity(intent);
            }
        });

/*        activity_attend_piechart.setUsePercentValues(true);
        activity_attend_piechart.getDescription().setEnabled(true);
        activity_attend_piechart.setExtraOffsets(5, 10, 5, 5);

        activity_attend_piechart.setDragDecelerationFrictionCoef(0.95f);

        activity_attend_piechart.setDrawHoleEnabled(false);
        activity_attend_piechart.setHoleColor(Color.WHITE);
        activity_attend_piechart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(34f, "Japan"));
        pieEntries.add(new PieEntry(23f, "USA"));
        pieEntries.add(new PieEntry(14f, "UK"));
        pieEntries.add(new PieEntry(35f, "India"));
        pieEntries.add(new PieEntry(40f, "Russia"));
        pieEntries.add(new PieEntry(40f, "Korea"));

        Description description = new Description();
        description.setText("세계 국가");
        description.setTextSize(15);
        activity_attend_piechart.setDescription(description);

        activity_attend_piechart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "country");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData pieData = new PieData();
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);

        activity_attend_piechart.setData(pieData);*/

        return view;
    }

}

class Gallery_Adapter extends BaseAdapter {
    Context context;
    int layout;
    int img[];
    LayoutInflater layoutInflater;

    public Gallery_Adapter(Context context, int layout, int[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.activity_attend_sub_layout_imageview);
        imageView.setImageResource(img[position]);
        return convertView;
    }
}
