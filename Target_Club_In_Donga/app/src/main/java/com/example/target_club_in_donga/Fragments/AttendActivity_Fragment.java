package com.example.target_club_in_donga.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.AttendActivity_Admin;
import com.example.target_club_in_donga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendActivity_Fragment extends Fragment {

    private Gallery gallery;
    private Button select_btn;
    private Integer current_image_resource;

    private final int[] img = {R.drawable.aa, R.drawable.bb, R.drawable.cc,R.drawable.dd,R.drawable.ee};

    private FirebaseDatabase database;

    private Button activity_attend_attendance, activity_attend_cancel, activity_attend_button_admin;
    private int count, getCertificationNumber;

    private int getEditCertificationNumber;

    public AttendActivity_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_attend, container, false);
        Gallery_Adapter galleryAdapter = new Gallery_Adapter(getContext(), R.layout.activity_attend_sub_layout,img);
        gallery = (Gallery)view.findViewById(R.id.activity_attend_gallery);
        select_btn = (Button)view.findViewById(R.id.activity_attend_button_select);

        activity_attend_attendance = (Button) view.findViewById(R.id.activity_attend_attendance);
        activity_attend_cancel = (Button) view.findViewById(R.id.activity_attend_cancel);
        activity_attend_button_admin = (Button) view.findViewById(R.id.activity_attend_button_admin);

        database = FirebaseDatabase.getInstance();
        database.getReference().child("Attend_Admin_Certification_Number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("인증번호", snapshot.getValue() + "");
//                    getCertificationNumber = snapshot.getValue();
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        gallery.setAdapter(galleryAdapter);
        final ImageView imageView = (ImageView)view.findViewById(R.id.test);
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

        activity_attend_attendance.setOnClickListener(new View.OnClickListener() {
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

                activity_attend_check_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        getEditCertificationNumber = Integer.parseInt(activity_attend_check_edittext_certification_number.getText().toString());
                        // 출석확인 버튼을 누르면 인증번호를 누른 값 그대로 전달이 됨

                        if(getEditCertificationNumber == getCertificationNumber) {
                            Toast.makeText(getActivity(), "출석이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "다시 입력해주세요", Toast.LENGTH_SHORT).show();
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
//                intent.putExtra("finishstatus", true);
//                count++;
            }
        });

        activity_attend_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getActivity().finish();
            }
        });

        activity_attend_button_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), AttendActivity_Admin.class);
                startActivity(intent);
            }
        });



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
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (convertView == null){
            convertView = layoutInflater.inflate(layout,null);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.activity_attend_sub_layout_imageview);
        imageView.setImageResource(img[position]);
        return convertView;
    }
}