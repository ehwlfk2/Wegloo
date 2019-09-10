package com.example.target_club_in_donga.Material_Management;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class MaterialManagementActivity_History extends AppCompatActivity {

    RecyclerView activity_material_management_history_recyclerview_list;
    List<MaterialManagement_History_Item> materialHistoryItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private FirebaseDatabase database;
    private FirebaseStorage storage;

    Button activity_material_management_history_button_insert;

    String uidHistoryPath;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_history);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        activity_material_management_history_recyclerview_list = (RecyclerView) findViewById(R.id.activity_material_management_history_recyclerview_list);
        activity_material_management_history_recyclerview_list.setLayoutManager(new LinearLayoutManager(this));

        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();

        activity_material_management_history_recyclerview_list.setAdapter(boardRecyclerViewAdapter);
        boardRecyclerViewAdapter.notifyDataSetChanged();

        // 기록보기에서 해당 키값을 찾아와서 물품기록사항들을 보여줌
         Intent intent = getIntent();
        uidHistoryPath = intent.getExtras().getString("uidAdminPath");

        database.getReference().child("Material_Management").child(uidHistoryPath).child("lend_history").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                materialHistoryItems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MaterialManagement_History_Item materialHistoryItem = snapshot.getValue(MaterialManagement_History_Item.class);
                    String uidKey = snapshot.getKey();
                    materialHistoryItems.add(materialHistoryItem);
                    uidLists.add(uidKey);
                    Log.e("리스트", materialHistoryItem.history_lend_name);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

    }


//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>


//    MaterialManagementActivity_History 어댑터

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
                    .inflate(R.layout.activity_material_management_history_item, viewGroup, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            CustomViewHolder customViewHolder = ((CustomViewHolder) viewholder);
            customViewHolder.activity_material_management_history_item_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_material_management_history_item_date.setGravity(Gravity.LEFT);

            customViewHolder.activity_material_management_history_item_name.setText(materialHistoryItems.get(position).getId());
            customViewHolder.activity_material_management_history_item_name.setText(materialHistoryItems.get(position).history_lend_name);
            customViewHolder.activity_material_management_history_item_date.setText(materialHistoryItems.get(position).history_lend_date);

        }

        @Override
        public int getItemCount() {
            return materialHistoryItems.size();
        }

    }

}
