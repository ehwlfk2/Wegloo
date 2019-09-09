package com.example.target_club_in_donga.Material_Management;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MaterialManagementActivity_History extends AppCompatActivity {

    RecyclerView activity_material_management_history_recyclerview_list;
    List<MaterialManagement_Item> materialHistoryItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private FirebaseDatabase database;
    Button activity_material_management_history_button_insert;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_history);

        database = FirebaseDatabase.getInstance();

        activity_material_management_history_recyclerview_list = (RecyclerView) findViewById(R.id.activity_material_management_history_recyclerview_list);
        activity_material_management_history_recyclerview_list.setLayoutManager(new LinearLayoutManager(this));

        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();

        activity_material_management_history_recyclerview_list.setAdapter(boardRecyclerViewAdapter);
        boardRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("Material_Management").child("history_all").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                materialHistoryItems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MaterialManagement_Item materialHistoryItem = snapshot.getValue(MaterialManagement_Item.class);
                    String uidKey = snapshot.getKey();
                    materialHistoryItems.add(materialHistoryItem);
                    uidLists.add(uidKey);
                }
                boardRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_material_management_history_button_insert = (Button) findViewById(R.id.activity_material_management_history_button_insert);
        activity_material_management_history_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_material_management_history_item_linearlayout;

            TextView activity_material_management_history_item_name;
            TextView activity_material_management_history_item_date;

            public CustomViewHolder(View view) {
                super(view);
                activity_material_management_history_item_name = (TextView) view.findViewById(R.id.activity_material_management_history_item_name);
                activity_material_management_history_item_date = (TextView) view.findViewById(R.id.activity_material_management_history_item_date);

                activity_material_management_history_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_material_management_history_item_linearlayout);
            }

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
            customViewHolder.activity_material_management_history_item_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_material_management_history_item_date.setGravity(Gravity.LEFT);

            customViewHolder.activity_material_management_history_item_name.setText(materialHistoryItems.get(position).getId());
            customViewHolder.activity_material_management_history_item_name.setText(materialHistoryItems.get(position).edit_name_edittext);
            customViewHolder.activity_material_management_history_item_date.setText(materialHistoryItems.get(position).timestamp.toString());

/*            if (materialManagementItems.get(position).edit_lender.equals("없음")) {
                customViewHolder.activity_material_management_history_item_linearlayout.setBackgroundResource(R.drawable.border_green);
            } else {
                customViewHolder.activity_material_management_history_item_linearlayout.setBackgroundResource(R.drawable.border_gray);
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
                    customViewHolder.activity_material_management_history_item_linearlayout.setBackgroundResource(R.drawable.border_orange);
                }
            }*/

        }

        @Override
        public int getItemCount() {
            return materialHistoryItems.size();
        }

    }

}
