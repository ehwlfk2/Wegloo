package com.example.target_club_in_donga;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MaterialManagementActivity_Admin extends AppCompatActivity {

    int y = 0, m = 0, d = 0, h = 0, mi = 0;
    Calendar calendar = Calendar.getInstance();

    private static final int GALLARY_CODE = 10;

    RecyclerView activity_material_management_admin_recyclerview_main_list;
    List<ImageDTO> imageDTOs = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    protected ImageView activity_material_management_item_imageview_recyclerview_image;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_admin);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>
//        <------------------------------------------------------------------------------------------------------------------------------------------>


        this.activity_material_management_item_imageview_recyclerview_image = (ImageView) findViewById(R.id.activity_material_management_item_imageview_recyclerview_image);

        activity_material_management_admin_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_material_management_admin_recyclerview_main_list);
        activity_material_management_admin_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(this));

        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();

        activity_material_management_admin_recyclerview_main_list.setAdapter(boardRecyclerViewAdapter);
        boardRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("Material_Management").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                imageDTOs.clear();
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                    String uidKey = snapshot.getKey();
                    imageDTOs.add(imageDTO);
                    uidLists.add(uidKey);
                }
                boardRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        Button activity_material_management_admin_button_insert = (Button) findViewById(R.id.activity_material_management_admin_button_insert);
        activity_material_management_admin_button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MaterialManagementActivity_Admin.this, MaterialManagementActivity_Insert.class);
                startActivity(intent);
                boardRecyclerViewAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLARY_CODE) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            StorageReference riversRef = storageRef.child("Material_Management/" + file.getLastPathSegment());
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


    // MaterialManagementActivity_Admin 어댑터

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int count = 0;

        private void delete_content(final int position) {

            storage.getReference().child("Material_Management").child(imageDTOs.get(position).imageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {
                    Toast.makeText(MaterialManagementActivity_Admin.this, "삭제 완료", Toast.LENGTH_SHORT).show();

                    database.getReference().child("Material_Management").child(uidLists.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(final Void aVoid) {
                            Toast.makeText(MaterialManagementActivity_Admin.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull final Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {
                    Toast.makeText(MaterialManagementActivity_Admin.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                }
            });

        }

        private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            ImageView activity_material_management_item_imageview_recyclerview_image;
            TextView activity_material_management_item_textview_recyclerview_item_name;
            TextView activity_material_management_item_textview_recyclerview_lender;
            LinearLayout activity_material_management_item_linearlayout;

            public CustomViewHolder(View view) {
                super(view);

                activity_material_management_item_textview_recyclerview_item_name = (TextView) view.findViewById(R.id.activity_material_management_item_textview_recyclerview_item_name);
                activity_material_management_item_textview_recyclerview_lender = (TextView) view.findViewById(R.id.activity_material_management_item_textview_recyclerview_lender);
                activity_material_management_item_imageview_recyclerview_image = (ImageView) view.findViewById(R.id.activity_material_management_item_imageview_recyclerview_image);

                activity_material_management_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_material_management_item_linearlayout);

                view.setOnCreateContextMenuListener(this);

            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

                MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정하기");
                MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제하기");
                MenuItem Detail = menu.add(Menu.NONE, 1003, 3, "상세보기");
                Edit.setOnMenuItemClickListener(onEditMenu);
                Delete.setOnMenuItemClickListener(onEditMenu);
                Detail.setOnMenuItemClickListener(onEditMenu);

            }

            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case 1001:

/*                            storage.getReference().child("images").child(imageDTOs.get(getAdapterPosition()).imageName);
                            database.getReference().child("images").child(imageDTOs.get(getAdapterPosition()).edit_name_edittext);*/

/*                            Intent intent = new Intent(MaterialManagementActivity_Admin.this, NoticeActivity.class);
                            final EditText editTextID = (EditText) findViewById(R.id.material_management_edit_name_edittext);
                            editTextID.setText(imageDTOs.get(getAdapterPosition()).edit_name_edittext);
                            startActivity(intent);*/


//                            startActivity(new Intent(MaterialManagementActivity_Admin.this, MaterialManagementActivity_Insert.class));


                            // 선택한 position에 있는 것들 수정할 수 있어야 함

                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialManagementActivity_Admin.this);
                            View view = LayoutInflater.from(MaterialManagementActivity_Admin.this)
                                    .inflate(R.layout.activity_material_management_insert, null, false);
                            builder.setView(view);

                            final Button ButtonSubmit = (Button) view.findViewById(R.id.activity_material_management_insert_button_insert);
                            final EditText editTextID = (EditText) view.findViewById(R.id.activity_material_management_insert_edittext_item_name);
                            final ImageView editImageView = (ImageView) view.findViewById(R.id.activity_material_management_insert_imageview_image);

//                            editTextID.setText(imageDTOs.get(getAdapterPosition()).getId());

                            editTextID.setText(imageDTOs.get(getAdapterPosition()).edit_name_edittext);
//                            editImageView.setImageURI(imageDTOs.get(getAdapterPosition()).imageUri);
                            // 동작 함 굿굿

/*                            Bundle extras = ((Activity) mContext).getIntent().getExtras();
                            final Uri uri = Uri.parse(extras.getString("uri"));

                            byte[] byteArray = ((Activity) mContext).getIntent().getByteArrayExtra("image");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                            editImageView.setImageBitmap(bitmap);*/

/*                            final AlertDialog dialog = builder.create();

                            ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    String strID = editTextID.getText().toString();

                                    ImageDTO dict = new ImageDTO(strID);

                                    imageDTOs.set(getAdapterPosition(), dict);
                                    notifyItemChanged(getAdapterPosition());

//                                ((BitmapDrawable)editImageView.getDrawable()).getBitmap().recycle();
                                    // 수정하기를 누르면 가지고 있던 이미지뷰의 사진 크기 메모리 반환

                                    dialog.dismiss();
                                }
                            });

                            editImageView.setOnClickListener(new View.OnClickListener() {

                                public void onClick(View v) {
                                    startActivity(new Intent(MaterialManagementActivity_Admin.this, MaterialManagementActivity_Insert.class));
                                }
                            });

                            dialog.show();*/
//                            dialog.dismiss();

                            break;

                        case 1002:

                            delete_content(getAdapterPosition());

                            imageDTOs.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), imageDTOs.size());

                            break;

                        case 1003:

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(MaterialManagementActivity_Admin.this);

                            View view2 = LayoutInflater.from(MaterialManagementActivity_Admin.this)
                                    .inflate(R.layout.activity_material_management_detail, null, false);
                            builder2.setView(view2);

                            final Button detailButton = (Button) view2.findViewById(R.id.activity_material_management_detail_button_lend);
                            final Button detailButtonPeriodCalendar = (Button) view2.findViewById(R.id.activity_material_management_detail_lend_period_calendar);
                            final Button detailButtonPeriodTime = (Button) view2.findViewById(R.id.activity_material_management_detail_lend_period_time);
                            final TextView detailTextID = (TextView) view2.findViewById(R.id.activity_material_management_detail_item_name);
                            final TextView detailTextName = (TextView) view2.findViewById(R.id.activity_material_management_detail_lender);
                            final ImageView detailImageView = (ImageView) view2.findViewById(R.id.activity_material_management_detail_imageview_image);

                            detailTextID.setText(imageDTOs.get(getAdapterPosition()).getId());
                            detailTextName.setText(auth.getCurrentUser().getDisplayName());
                            Glide.with(itemView.getContext()).load(imageDTOs.get(getAdapterPosition()).imageUri).into(detailImageView);

                            final AlertDialog dialog2 = builder2.create();

                            detailButtonPeriodCalendar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    showDate();
                                }
                            });

                            detailButtonPeriodTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    showTime();
                                }
                            });

                            detailButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    Toast.makeText(v.getContext(), auth.getCurrentUser().getDisplayName() + "님 대여가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                    activity_material_management_item_textview_recyclerview_lender.setText(auth.getCurrentUser().getDisplayName());
                                    database.getReference().child("Material_Management").child(uidLists.get(getAdapterPosition())).child("edit_lender").setValue(activity_material_management_item_textview_recyclerview_lender.getText().toString());

/*                                    imageDTOs.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), imageDTOs.size());

                                    String strID2 = detailTextID.getText().toString();

                                    ImageDTO dict2 = new ImageDTO(strID2);

                                    imageDTOs.add(dict2);
                                    notifyItemRangeChanged(getAdapterPosition(), imageDTOs.size());*/

/*                                mArrayList2.add(0, dict2); //첫 줄에 삽입
                                //mArrayList.add(dict); //마지막 줄에 삽입
                                mAdapter2.notifyDataSetChanged(); //변경된 데이터를 화면에 반영*/

//                                notifyItemRangeChanged(getAdapterPosition(), mArrayList2.size());

                                    dialog2.dismiss();

                                }
                            });

                            dialog2.show();

                    }
                    return true;
                }
            };
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_material_management_item, viewGroup, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            ((CustomViewHolder) viewholder).activity_material_management_item_textview_recyclerview_item_name.setGravity(Gravity.LEFT);
            ((CustomViewHolder) viewholder).activity_material_management_item_textview_recyclerview_lender.setGravity(Gravity.LEFT);

            ((CustomViewHolder) viewholder).activity_material_management_item_textview_recyclerview_item_name.setText(imageDTOs.get(position).getId());
            ((CustomViewHolder) viewholder).activity_material_management_item_textview_recyclerview_item_name.setText(imageDTOs.get(position).edit_name_edittext);
            ((CustomViewHolder) viewholder).activity_material_management_item_textview_recyclerview_lender.setText(imageDTOs.get(position).edit_lender);

            Glide.with(viewholder.itemView.getContext()).load(imageDTOs.get(position).imageUri).into(((CustomViewHolder) viewholder).activity_material_management_item_imageview_recyclerview_image);

            if (imageDTOs.get(position).edit_lender.equals("없음")) {
//                ((CustomViewHolder) viewholder).activity_material_management_item_linearlayout.setBackgroundColor(Color.WHITE);
            } else {
                ((CustomViewHolder) viewholder).activity_material_management_item_linearlayout.setBackgroundColor(Color.LTGRAY);
            }


        }

        @Override
        public int getItemCount() {
            return imageDTOs.size();
        }

        void showDate() {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MaterialManagementActivity_Admin.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                    y = year;
                    m = month + 1;
                    d = dayOfMonth;
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.setMessage("날짜");
            datePickerDialog.show();
        }

        void showTime() {
            TimePickerDialog timePickerDialog = new TimePickerDialog(MaterialManagementActivity_Admin.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
                    h = hourOfDay;
                    mi = minute;
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

            timePickerDialog.setMessage("시간");
            timePickerDialog.show();
        }

    }


}
