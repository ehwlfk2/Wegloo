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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.Material_Management.MaterialManagementActivity_Home;
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
import java.util.Date;
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class AttendActivity_Home extends AppCompatActivity {

    RecyclerView activity_attend_home_admin_recyclerview_main_list;
    List<Attend_Admin_Item> attenditems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    Button activity_attend_home_admin_button_insert;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private int admin;
    public static String uidAdminPath;

    private long now;
    private String formatDate, nowtardyTimeLimit, getTardyTimeLimit;
    private static int adminNumber = 2;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_home);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        activity_attend_home_admin_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_attend_home_admin_recyclerview_main_list);
        activity_attend_home_admin_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final AttendActivity_AdminRecyclerViewAdapter attendActivity_adminRecyclerViewAdapter = new AttendActivity_AdminRecyclerViewAdapter();

        activity_attend_home_admin_recyclerview_main_list.setAdapter(attendActivity_adminRecyclerViewAdapter);
        attendActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child(clubName).child("Attend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attenditems.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attend_Admin_Item attendItem = snapshot.getValue(Attend_Admin_Item.class);
                    String uidKey = snapshot.getKey();
                    attenditems.add(attendItem);
                    uidLists.add(uidKey);
                }
                attendActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        activity_attend_home_admin_button_insert = (Button) findViewById(R.id.activity_attend_home_admin_button_insert);
        activity_attend_home_admin_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendActivity_Home.this, AttendActivity_Admin.class);
                startActivity(intent);
            }
        });

        database.getReference().child(clubName).child("User").child(auth.getCurrentUser().getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                admin = Integer.parseInt(dataSnapshot.getValue().toString());

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


    // AttendActivity 어댑터

    class AttendActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_home_admin_item_linearlayout;
            TextView activity_attend_home_admin_item_textview_recyclerview_club_name;
            TextView activity_attend_home_admin_item_textview_recyclerview_start_time;
            TextView activity_attend_home_admin_item_recyclerview_attend_time_limit_tilte;
            TextView activity_attend_home_admin_item_recyclerview_attend_time_limit;
            TextView activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit_title;
            TextView activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit;
            TextView activity_attend_home_admin_item_textview_recyclerview_attend_statue;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_home_admin_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_home_admin_item_linearlayout);
                activity_attend_home_admin_item_textview_recyclerview_club_name = (TextView) view.findViewById(R.id.activity_attend_home_admin_item_textview_recyclerview_club_name);
                activity_attend_home_admin_item_textview_recyclerview_attend_statue = (TextView) view.findViewById(R.id.activity_attend_home_admin_item_textview_recyclerview_attend_statue);
                activity_attend_home_admin_item_textview_recyclerview_start_time = (TextView) view.findViewById(R.id.activity_attend_home_admin_item_textview_recyclerview_start_time);
                activity_attend_home_admin_item_recyclerview_attend_time_limit_tilte = (TextView) view.findViewById(R.id.activity_attend_home_admin_item_recyclerview_attend_time_limit_tilte);
                activity_attend_home_admin_item_recyclerview_attend_time_limit = (TextView) view.findViewById(R.id.activity_attend_home_admin_item_recyclerview_attend_time_limit);
                activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit = (TextView) view.findViewById(R.id.activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit);
                activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit_title = (TextView) view.findViewById(R.id.activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit_title);

            }

        }

        public void PopupMenu(final AttendActivity_Home.AttendActivity_AdminRecyclerViewAdapter.CustomViewHolder viewholder, final int position) {
            viewholder.activity_attend_home_admin_item_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final PopupMenu popup = new PopupMenu(view.getContext(), view);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.attend_detail:

                                    Intent intent = new Intent(AttendActivity_Home.this, AttendActivity.class);
                                    uidAdminPath = database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).getKey();

/*                                    Bundle bundle = new Bundle();
                                    bundle.putString("uidAdminPath", uidAdminPath);
                                    Fragment fragment = new AttendActivity_Fragment();
                                    fragment.setArguments(bundle);*/

                                    startActivity(intent);

                                    return true;

                                case R.id.attend_delete:

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendActivity_Home.this);

                                    View view = LayoutInflater.from(AttendActivity_Home.this)
                                            .inflate(R.layout.activity_attend_admin_delete_item, null, false);
                                    builder.setView(view);

                                    final Button confirmButton = (Button) view.findViewById(R.id.activity_attend_admin_delete_item_button_confirm);
                                    final Button cancelButton = (Button) view.findViewById(R.id.activity_attend_admin_delete_item_button_cancel);

                                    final AlertDialog dialog = builder.create();

                                    confirmButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            Toast.makeText(AttendActivity_Home.this, "출석이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                            delete_content(position);
                                            attenditems.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, attenditems.size());
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

                                default:
                                    return false;
                            }
                            //return false;
                        }
                    });

                    popup.inflate(R.menu.attend_home_popup);

                    database.getReference().child(clubName).child("User").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            admin = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                            if (admin <= adminNumber) {
                                popup.getMenu().getItem(1).setVisible(true);
                            }
                        }

                        @Override
                        public void onCancelled(final DatabaseError databaseError) {

                        }
                    });

                    if (admin <= adminNumber) {
                        popup.getMenu().getItem(1).setVisible(true);
                    } else {
                        popup.getMenu().getItem(1).setVisible(false);
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
            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_club_name.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_attend_statue.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_start_time.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_home_admin_item_recyclerview_attend_time_limit.setGravity(Gravity.LEFT);
            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_attend_statue.setText("미출결");
            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_club_name.setText(attenditems.get(position).clubName);
            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_start_time.setText(attenditems.get(position).startTime);
            customViewHolder.activity_attend_home_admin_item_recyclerview_attend_time_limit.setText(attenditems.get(position).attendTimeLimit);
            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit.setText(attenditems.get(position).tardyTimeLimit);

            PopupMenu(customViewHolder, position);

            database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).child("Attend_Certification_Number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        customViewHolder.activity_attend_home_admin_item_recyclerview_attend_time_limit.setVisibility(View.GONE);
                        customViewHolder.activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit.setVisibility(View.GONE);
                        customViewHolder.activity_attend_home_admin_item_recyclerview_attend_time_limit_tilte.setVisibility(View.GONE);
                        customViewHolder.activity_attend_home_admin_item_textview_recyclerview_tardy_time_limit_title.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(final DatabaseError databaseError) {

                }
            });

            database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).child("User_Statue").child(auth.getCurrentUser().getUid()).child("attend_statue").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        if (dataSnapshot.getValue().equals("출석")) {
                            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_attend_statue.setText("출석");
                        } else if (dataSnapshot.getValue().equals("지각")) {
                            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_attend_statue.setText("지각");
                        } else if (dataSnapshot.getValue().equals("결석")) {
                            customViewHolder.activity_attend_home_admin_item_textview_recyclerview_attend_statue.setText("결석");
                        }
                    }
                }

                @Override
                public void onCancelled(final DatabaseError databaseError) {

                }
            });

            database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).addValueEventListener(new ValueEventListener() {
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
                            database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).child("User_Statue").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.child("attend_statue").getValue().equals("미출결")) {
                                            database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).child("User_Statue").child(snapshot.getKey()).child("attend_statue").setValue("결석");
                                        }
                                    }
                                    database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).child("Attend_Certification_Number").removeValue();
                                    database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).child("attendTimeLimit").removeValue();
                                    database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).child("tardyTimeLimit").removeValue();
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

            database.getReference().child(clubName).child("Attend").child(uidLists.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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