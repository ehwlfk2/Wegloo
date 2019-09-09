package com.example.target_club_in_donga.Material_Management;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MaterialManagementActivity_History extends AppCompatActivity {

    ImageView activity_material_management_lend_imageview_image;
    Button activity_material_management_lend_button_lend;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_history);

        activity_material_management_lend_imageview_image = (ImageView) findViewById(R.id.activity_material_management_lend_imageview_image);
        activity_material_management_lend_button_lend = (Button) findViewById(R.id.activity_material_management_lend_button_lend);
    }

/*    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_material_management_admin_item, viewGroup, false);

            return new MaterialManagementActivity_Admin.BoardRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            MaterialManagementActivity_Admin.BoardRecyclerViewAdapter.CustomViewHolder customViewHolder = ((MaterialManagementActivity_Admin.BoardRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_item_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_lender.setGravity(Gravity.LEFT);

            customViewHolder.activity_material_management_admin_item_textview_recyclerview_item_name.setText(materialManagementItems.get(position).getId());
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_item_name.setText(materialManagementItems.get(position).edit_name_edittext);
            customViewHolder.activity_material_management_admin_item_textview_recyclerview_lender.setText(materialManagementItems.get(position).edit_lender);
            customViewHolder.activity_material_management_admin_item_recyclerview_timestamp.setText(materialManagementItems.get(position).timestamp.toString());

            Glide.with(viewholder.itemView.getContext()).load(materialManagementItems.get(position).imageUri).into(((MaterialManagementActivity_Admin.BoardRecyclerViewAdapter.CustomViewHolder) viewholder).activity_material_management_admin_item_imageview_recyclerview_image);

            PopupMenu(customViewHolder, position);

            if (materialManagementItems.get(position).edit_lender.equals("없음")) {
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
                }
            }

        }

        @Override
        public int getItemCount() {
            return materialManagementItems.size();
        }

    }*/

}
