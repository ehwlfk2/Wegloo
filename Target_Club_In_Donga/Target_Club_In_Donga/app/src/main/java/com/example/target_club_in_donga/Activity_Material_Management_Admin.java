package com.example.target_club_in_donga;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.List;

public class Activity_Material_Management_Admin extends AppCompatActivity {

    int y = 0, m = 0, d = 0, h = 0, mi = 0;
    Calendar calendar = Calendar.getInstance();

    private static final int GALLARY_CODE = 10;

    RecyclerView recyclerView;
    List<ImageDTO> imageDTOs = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

/*    ArrayList<Dictionary> mArrayList, mArrayList2;
    CustomAdapter mAdapter, mAdapter2;*/

    private TextView nameTextView, emailTextView;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    protected ImageView mImageView;

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


        this.mImageView = (ImageView) findViewById(R.id.imageview_recyclerview_image);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();

        recyclerView.setAdapter(boardRecyclerViewAdapter);
        boardRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("images").addValueEventListener(new ValueEventListener() {
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

        Button buttonInsert = (Button) findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Material_Management_Admin.this, Activity_Material_Management_Edit.class);

//                ButtonSubmit.setText("추가하기");

                startActivity(intent);

/*                String strID = "물품명";

                ImageDTO dict = new ImageDTO(strID);

                imageDTOs.add(0, dict); //첫 줄에 삽입*/
                //mArrayList.add(dict); //마지막 줄에 삽입
                boardRecyclerViewAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                // 데이터베이스에 있는 정보들을 불러와서 볼 수 있는데 Google_Board로 넘어와서 새상품 추가하기가 안됨.

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLARY_CODE) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://android-studio-firebase-872f2.appspot.com");

            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
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


    // Activity_Material_Management_Admin 어댑터

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int count = 0;

        private void delete_content(final int position) {

            storage.getReference().child("images").child(imageDTOs.get(position).imageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {
                    Toast.makeText(Activity_Material_Management_Admin.this, "삭제 완료", Toast.LENGTH_SHORT).show();

                    database.getReference().child("images").child(uidLists.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(final Void aVoid) {
                            Toast.makeText(Activity_Material_Management_Admin.this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Activity_Material_Management_Admin.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                }
            });

        }

        private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            ImageView imageView;
            TextView textView, mGoodsName;

            public CustomViewHolder(View view) {
                super(view);

                mGoodsName = (TextView) view.findViewById(R.id.textview_recyclerview_id);
                imageView = (ImageView) view.findViewById(R.id.imageview_recyclerview_image);
                textView = (TextView) view.findViewById(R.id.textview_recyclerview_id);

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

                            storage.getReference().child("images").child(imageDTOs.get(getAdapterPosition()).imageName);
                            database.getReference().child("images").child(imageDTOs.get(getAdapterPosition()).edit_name_edittext);

/*                            Intent intent = new Intent(Activity_Material_Management_Admin.this, Activity_Notice.class);
                            final EditText editTextID = (EditText) findViewById(R.id.material_management_edit_name_edittext);
                            editTextID.setText(imageDTOs.get(getAdapterPosition()).edit_name_edittext);
                            startActivity(intent);*/


//                            startActivity(new Intent(Activity_Material_Management_Admin.this, Activity_Material_Management_Edit.class));


                            // 선택한 position에 있는 것들 수정할 수 있어야 함

                            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Material_Management_Admin.this);
                            View view = LayoutInflater.from(Activity_Material_Management_Admin.this)
                                    .inflate(R.layout.activity_material_management_edit, null, false);
                            builder.setView(view);

                            final Button ButtonSubmit = (Button) view.findViewById(R.id.material_management_edit_change_button);
                            final EditText editTextID = (EditText) view.findViewById(R.id.material_management_edit_name_edittext);
                            final ImageView editImageView = (ImageView) view.findViewById(R.id.material_management_edit_image_imageview);

//                            editTextID.setText(imageDTOs.get(getAdapterPosition()).getId());

                            editTextID.setText(imageDTOs.get(getAdapterPosition()).edit_name_edittext);
//                            editImageView.setImageURI(imageDTOs.get(getAdapterPosition()).imageUri);
                            // 동작 함 굿굿

/*                            Bundle extras = ((Activity) mContext).getIntent().getExtras();
                            final Uri uri = Uri.parse(extras.getString("uri"));

                            byte[] byteArray = ((Activity) mContext).getIntent().getByteArrayExtra("image");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                            editImageView.setImageBitmap(bitmap);*/

                            final AlertDialog dialog = builder.create();

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
                                    startActivity(new Intent(Activity_Material_Management_Admin.this, Activity_Material_Management_Edit.class));
                                }
                            });

                            dialog.show();
//                            dialog.dismiss();

                            break;


//                            삭제 완료 (같은 이미즈를 넣었을 경우 이미지가 하나만 들어가서 아이템 삭제 후 똑같은 이미지가 들어있는 아이템을 삭제 하면 storage 에서
//                            찾을 수 없어서 삭제가 안되서 이것도 나중에 구현하여야함 같은 이미지여도 storage에 들어가게 하거나 다른 방법으로......)
                        case 1002:

                            delete_content(getAdapterPosition());

                            imageDTOs.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), imageDTOs.size());

                            break;

                        case 1003:

//                        count--;

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(Activity_Material_Management_Admin.this);

                            View view2 = LayoutInflater.from(Activity_Material_Management_Admin.this)
                                    .inflate(R.layout.activity_material_management_detail, null, false);
                            builder2.setView(view2);

/*                        View view3 = LayoutInflater.from(mContext)
                                .inflate(R.layout.activity_material_management_admin, null, false);*/
//                        builder2.setView(view3);

/*                        mRecyclerView2 = (RecyclerView) view3.findViewById(R.id.recyclerview_main_list2);
                        mRecyclerView2.setLayoutManager(new LinearLayoutManager(mContext));

                        mArrayList2 = new ArrayList<>();
                        mAdapter2 = new CustomAdapter(mContext, mArrayList2);

                        mRecyclerView2.setAdapter(mAdapter2);*/

                            // 여기가 문제일 확률이 높음


                            final TextView detailTextID = (TextView) view2.findViewById(R.id.material_management_detail_btn);
                            final Button detailButton = (Button) view2.findViewById(R.id.material_management_detail_btn2);
//                        final TextView detailTextID2 = (TextView) view2.findViewById(R.id.material_management_detail_btn);

                            detailTextID.setText(imageDTOs.get(getAdapterPosition()).getId());
//                        detailTextID.setText(mArrayList2.get(getAdapterPosition()).getId());

                            final AlertDialog dialog2 = builder2.create();

                            detailButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Toast.makeText(v.getContext(), "눌렀어요" + count, Toast.LENGTH_SHORT).show();

/*                                Intent intent = new Intent(v.getContext(), Activity_Material_Management_Edit.class);
                                intent.putExtra("position", getAdapterPosition());

                                mContext.startActivity(intent);*/

                                    imageDTOs.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), imageDTOs.size());

                                    String strID2 = detailTextID.getText().toString();

                                    ImageDTO dict2 = new ImageDTO(strID2);

                                    imageDTOs.add(dict2);
                                    notifyItemRangeChanged(getAdapterPosition(), imageDTOs.size());

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
                    .inflate(R.layout.activity_material_management_add, viewGroup, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            ((CustomViewHolder) viewholder).mGoodsName.setGravity(Gravity.LEFT);
            ((CustomViewHolder) viewholder).mGoodsName.setText(imageDTOs.get(position).getId());

            ((CustomViewHolder) viewholder).textView.setText(imageDTOs.get(position).edit_name_edittext);
            Glide.with(viewholder.itemView.getContext()).load(imageDTOs.get(position).imageUri).into(((CustomViewHolder) viewholder).imageView);

        }

        @Override
        public int getItemCount() {
            return imageDTOs.size();
        }

    }


}
