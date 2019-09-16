package com.example.target_club_in_donga.Gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.target_club_in_donga.Gallery.*;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;

public class Gallery_Write extends AppCompatActivity {
    ImageButton gallery_img_upload_btn,x_btn;
    Button upload_btn;
    EditText title, contents;
    private RecyclerView recyclerView;
    private Recy_Adapter adapter;
    private static final int GALLERY_CODE = 10;
    private String imagepath;
    private ArrayList<Uri> uris = new ArrayList<>(); // 이미지 uri 리스트
    private ArrayList<Uri> uriList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    final GalleryModel galleryModel = new GalleryModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery__write);
        gallery_img_upload_btn = (ImageButton) findViewById(R.id.gallery_img_upload_btn);
        recyclerView = (RecyclerView) findViewById(R.id.gallery_img_horizontal_recy);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        x_btn = (ImageButton)findViewById(R.id.x_btn);
        upload_btn = (Button)findViewById(R.id.gallery_write_upload_btn);
        title = (EditText)findViewById(R.id.title_write_edt);
        contents = (EditText)findViewById(R.id.content_write_edt);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        adapter = new Recy_Adapter(clicklistner);
        x_btn.setOnClickListener(new View.OnClickListener() { // 뒤로가기
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() { //완료
            @Override
            public void onClick(View v) {
                if(uris.size() != 0) {
                    writeGalleryInfo();
                }
                else if ( uris.size() == 0 ){
                    Toast.makeText(Gallery_Write.this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gallery_img_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //사진등록
                try {
                    recyclerView.removeAllViewsInLayout();
                    uris.clear();
                    FishBun.with(Gallery_Write.this).setImageAdapter(new GlideAdapter()).setMaxCount(15).setMinCount(1).setActionBarTitle("사진을 선택해주세요").setPickerSpanCount(4).textOnNothingSelected("nothing selected").startAlbum();
                } catch (Exception e) {
                    Log.e("gallery pick error", " ..");
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(recyclerViewDecoration);
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Gallery_Write.this);

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }
    }
    private void writeGalleryInfo() {
        final StorageReference storageRef = storage.getReference();
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
        for (int i = 0; i < uris.size(); i++) {
            Uri individualImage = uris.get(i);
            final StorageReference ImageName = storageRef.child("Gallery/" + individualImage.getLastPathSegment());
            UploadTask uploadTask = ImageName.putFile(individualImage);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri link = taskSnapshot.getDownloadUrl();
                    String url = String.valueOf(link);
                    galleryModel.imglist.add(url);
                    galleryModel.idx += 1;
                    if( galleryModel.idx == uris.size() ){
                        StoreDatabase();
                        galleryModel.idx = 0;
                        finish();
                    }
                }
            });

        }
    }
    private void StoreDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        FirebaseUser currentuser = auth.getCurrentUser();
        galleryModel.title = title.getText().toString();
        galleryModel.contents = contents.getText().toString();
        galleryModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        galleryModel.username = currentuser.getDisplayName();
        galleryModel.timestamp = ServerValue.TIMESTAMP;
        databaseReference.push().setValue(galleryModel);
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
        Toast.makeText(this, "업로드 완료", Toast.LENGTH_SHORT).show();
    }


    public class RecyclerViewDecoration extends RecyclerView.ItemDecoration{ // 리사이클러 이미지 간격 띄우는 클래스
        private  final int divheight;

        public RecyclerViewDecoration(int divheight){
            this.divheight = divheight;
        }
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.right = divheight;
            }
        }
    }

    public View.OnClickListener clicklistner = new View.OnClickListener() { // 이미지 클릭이벤트
        @Override
        public void onClick(View v) {
            final int position = recyclerView.getChildPosition(v);
            String pos = String.valueOf(position);
            Toast.makeText(Gallery_Write.this, pos, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리 uri 리스트 받아오기
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    uris = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    break;
                }
        }
        adapter.notifyDataSetChanged(); // 리사이클러 갱신
    }
    public String getPath(Uri uri){ // 갤러리내 이미지 path 가져오기
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);
    }
    class Recy_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        View.OnClickListener clickListener;

        public Recy_Adapter(View.OnClickListener clickListener){
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_write,parent,false);
            view.setOnClickListener(clickListener);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).imageView.setImageURI(uris.get(position));

        }

        @Override
        public int getItemCount() {
            return uris.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public CustomViewHolder(View view) {
                super(view);
                imageView= (ImageView)view.findViewById(R.id.item_gallery_img_plus);
                GradientDrawable drawable = (GradientDrawable)view.getContext().getDrawable(R.drawable.imageview_round_corner);
                imageView.setBackground(drawable);
                imageView.setClipToOutline(true);
            }
        }
    }
}
