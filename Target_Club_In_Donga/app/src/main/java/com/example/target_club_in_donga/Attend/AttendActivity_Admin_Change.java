package com.example.target_club_in_donga.Attend;

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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_Admin_Change extends AppCompatActivity {

    private  RecyclerView activity_attend_admin_change_recyclerview_main_list;
    List<Attend_Admin_Change_Item> attendAdminItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_admin_change);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        activity_attend_admin_change_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_admin_change_recyclerview_main_list);
        activity_attend_admin_change_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final AttendAdminChangeActivity_AdminRecyclerViewAdapter attendAdminChangeActivity_adminRecyclerViewAdapter = new AttendAdminChangeActivity_AdminRecyclerViewAdapter();

        activity_attend_admin_change_recyclerview_main_list.setAdapter(attendAdminChangeActivity_adminRecyclerViewAdapter);
        attendAdminChangeActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child(clubName).child("Attend").child("Attend_Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attendAdminItems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attend_Admin_Change_Item attendAdminChangeItem = snapshot.getValue(Attend_Admin_Change_Item.class);
                    String uidKey = snapshot.getKey();
                    attendAdminItems.add(attendAdminChangeItem);
                    uidLists.add(uidKey);
                }
                attendAdminChangeActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

        Uri file = Uri.fromFile(new File(getPath(data.getData())));
        StorageReference riversRef = storageRef.child(clubName).child("Attend/" + file.getLastPathSegment());
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


    // AttendAdminChangeActivity 어댑터

    class AttendAdminChangeActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_admin_change_item_linearlayout;
            TextView activity_attend_admin_change_item_textview_name;
            TextView activity_attend_admin_change_item_textview_attend_statue;
            TextView activity_attend_admin_change_item_textview_phone_number;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_admin_change_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_admin_change_item_linearlayout);
                activity_attend_admin_change_item_textview_name = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_name);
                activity_attend_admin_change_item_textview_attend_statue = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_attend_statue);
                activity_attend_admin_change_item_textview_phone_number = (TextView) view.findViewById(R.id.activity_attend_admin_change_item_textview_phone_number);

            }

        }

/*        public void PopupMenu(final AttendActivity_Admin_Change.AttendAdminChangeActivity_AdminRecyclerViewAdapter.CustomViewHolder viewholder, final int position) {
            viewholder.activity_attend_admin_change_item_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final PopupMenu popup = new PopupMenu(view.getContext(), view);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                default:
                                    return false;
                            }
                            //return false;
                        }
                    });

                    popup.inflate(R.menu.attend_home_main_popup);

                    popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                    popup.show();
                }
            });
        }*/

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_attend_admin_change_item, viewGroup, false);

            return new AttendActivity_Admin_Change.AttendAdminChangeActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final AttendActivity_Admin_Change.AttendAdminChangeActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((AttendActivity_Admin_Change.AttendAdminChangeActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_admin_change_item_textview_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_change_item_textview_attend_statue.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_admin_change_item_textview_phone_number.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_admin_change_item_textview_name.setText(attendAdminItems.get(position).Name);
            customViewHolder.activity_attend_admin_change_item_textview_attend_statue.setText(attendAdminItems.get(position).attendStatue);
            customViewHolder.activity_attend_admin_change_item_textview_phone_number.setText(attendAdminItems.get(position).phone);

//            PopupMenu(customViewHolder, position);

        }

        @Override
        public int getItemCount() {
            return attendAdminItems.size();
        }

    }

}