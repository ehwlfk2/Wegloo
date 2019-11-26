package com.example.target_club_in_donga.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.thisClubIsRealName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userProfileUrl;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userRealName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userNicName;

public class Board_Write extends AppCompatActivity {
    ImageButton board_img_upload_btn,x_btn;
    Button upload_btn;
    EditText title, contents;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private Recy_Adapter adapter;
    private ArrayList<Uri> uris = new ArrayList<>(); // 이미지 uri 리스트
    private ArrayList<String> IMAGEs = new ArrayList<>(); // boardmodel 이미지랑 uri 이미지 합치기
    private LinearLayoutManager linearLayoutManager;
    RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String uid;
    BoardModel boardModel = new BoardModel();
    int edt_key = 0;
    String updatekey;
    int lastposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        board_img_upload_btn = findViewById(R.id.board_img_upload_btn);
        recyclerView = findViewById(R.id.board_img_horizontal_recy);
        progressDialog = new ProgressDialog(this);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 0);
        x_btn = findViewById(R.id.x_btn);
        upload_btn = findViewById(R.id.board_write_upload_btn);
        title = findViewById(R.id.title_write_edt);
        contents = findViewById(R.id.content_write_edt);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        adapter = new Recy_Adapter(clicklistner);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(recyclerViewDecoration);

        Intent intent = getIntent();
        edt_key = intent.getIntExtra("edt_key", 0);

        if( edt_key == 1){ // 수정 요청시
            boardModel = (BoardModel)intent.getSerializableExtra("edt_model");
            updatekey = intent.getStringExtra("updatekey");
            title.setText(boardModel.title);
            contents.setText(boardModel.contents);
            lastposition = boardModel.idx;
            IMAGEs.addAll(boardModel.imglist);
        }

        x_btn.setOnClickListener(new View.OnClickListener() { // 뒤로가기
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() { //업로드
            @Override
            public void onClick(View v) {
                String titles = title.getText().toString();
                titles = titles.trim();
                String content = contents.getText().toString();
                content = content.trim();
                if(titles.getBytes().length<=0){
                    Toast.makeText(Board_Write.this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(content.getBytes().length<=0){
                    Toast.makeText(Board_Write.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("업로드 중입니다...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    boardModel.uid = auth.getCurrentUser().getUid();
                    boardModel.name = userNicName;
                    boardModel.userPic = userProfileUrl;

                    if (edt_key == 1) { // 수정 시
                        if (IMAGEs.size() != 0) { // 사진 있음
                            boardEditInfo();
                        } else if (IMAGEs.size() == 0) { // 사진 없음
                            Edit_StoreDatabase();
                        }

                    } else { // 글쓰기
                        if (IMAGEs.size() != 0) { // 사진있음
                            writeGalleryInfo();
                        } else if (IMAGEs.size() == 0) { // 사진없음
                            StoreDatabase();
                        }
                    }

                }
            }
        });

        board_img_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //사진등록
                try {
                    FishBun.with(Board_Write.this).setImageAdapter(new GlideAdapter()).setMaxCount(10).setMinCount(0).setActionBarTitle("사진을 선택해주세요").setPickerSpanCount(4).textOnNothingSelected("nothing selected").startAlbum();
                } catch (Exception e) {
                    Log.e("gallery pick error", " ..");
                }
            }
        });
    }

    private void boardEditInfo(){
        final StorageReference storageRef = storage.getReference();
        long unix = System.currentTimeMillis();
        Date date = new Date(unix);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        final String times = simpleDateFormat.format(date);
        if( uris.size() ==0 ){ // 사진은 있는데 추가사진 없이 사진을 삭제하거나, 텍스트만 변경 -> 스토리지업로드는 따로 무필요
            Edit_StoreDatabase();
        }
        else {
            for (int i = 0; i < uris.size(); i++) { // 사진 추가
                final Uri individualImage = uris.get(i);
                final StorageReference ImageName = storageRef.child("EveryClub").child(clubName).child("Board/" + individualImage.getLastPathSegment() + '-' + times);
                UploadTask uploadTask = ImageName.putFile(individualImage);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri link = taskSnapshot.getDownloadUrl();
                        String url = String.valueOf(link);
                        boardModel.imglist.add(url);
                        boardModel.imgName.add(individualImage.getLastPathSegment() + '-' + times);
                        boardModel.idx += 1;
                        if (boardModel.idx == IMAGEs.size()) {
                            boardModel.Thumbnail = boardModel.imglist.get(0);
                            Edit_StoreDatabase();
                        }
                    }
                });
            }
        }
    }
    private void writeGalleryInfo() {
        final StorageReference storageRef = storage.getReference();
        long unix = System.currentTimeMillis();
        Date date = new Date(unix);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        final String times = simpleDateFormat.format(date);
        for (int i = 0; i < IMAGEs.size(); i++) {
            final Uri individualImage = Uri.parse(IMAGEs.get(i));
            final StorageReference ImageName = storageRef.child("EveryClub").child(clubName).child("Board/" + individualImage.getLastPathSegment() + '-' + times);
            UploadTask uploadTask = ImageName.putFile(individualImage);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri link = taskSnapshot.getDownloadUrl();
                    String url = String.valueOf(link);
                    boardModel.imglist.add(url);
                    boardModel.imgName.add(individualImage.getLastPathSegment()+'-'+times);
                    boardModel.idx += 1;
                    if( boardModel.idx == uris.size() ){
                        boardModel.Thumbnail = boardModel.imglist.get(0);
                        StoreDatabase();
                    }
                }
            });
        }
    }
    private void Edit_StoreDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("Board");
        boardModel.title = title.getText().toString();
        boardModel.contents = contents.getText().toString();
        boardModel.timestamp = ServerValue.TIMESTAMP;
        databaseReference.child(updatekey).setValue(boardModel);
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("upload_complete", 1);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void StoreDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("Board");
        boardModel.title = title.getText().toString();
        boardModel.contents = contents.getText().toString();
        boardModel.timestamp = ServerValue.TIMESTAMP;
        databaseReference.push().setValue(boardModel);
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("upload_complete", 1);
        setResult(RESULT_OK, intent);
        finish();
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
            if(position == IMAGEs.size()){ // + 이미지 클릭시 , 포지션 = 마지막
                FishBun.with(Board_Write.this).setImageAdapter(new GlideAdapter()).setMaxCount(15).setMinCount(1).setActionBarTitle("사진을 선택해주세요").setPickerSpanCount(4).textOnNothingSelected("nothing selected").startAlbum();
            }
            else{ // 일반 이미지 클릭시 삭제
                showOption(v, position);
            }

        }
    };
    void showOption(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.img_delete_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pick_delete:
                        if(edt_key==1){ // 수정 페이지에서 이미지 삭제. RTDB 내용은 업로드시 업데이트, 선택된 이미지 storage 삭제는 여기서
                            storage.getReference().child("EveryClub").child(clubName).child("Board").child(boardModel.imgName.get(position)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    boardModel.imglist.remove(position); // 이내용도 포함돼야함
                                    IMAGEs.remove(position);
                                    boardModel.imgName.remove(position);
                                    boardModel.idx -= 1;
                                    if (boardModel.idx == 0){ // 사진이 없어지면
                                        boardModel.Thumbnail = null;
                                        recyclerView.removeAllViewsInLayout();
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(getApplicationContext(), "storage 삭제 오류", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else { // 수정이 아닐때 삭제는 앨범리스트만
                            IMAGEs.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리 uri 리스트 받아오기
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    ArrayList<Uri> adduri;
                    adduri = data.getParcelableArrayListExtra(Define.INTENT_PATH); // 수정, 이미지 추가때 추가되는 경로만 한번에 담는다
                    uris.addAll(adduri);
                    for(int i =0; i<adduri.size(); i++) {
                        IMAGEs.add(String.valueOf(adduri.get(i))); //통합
                    }
                    break;
                }
        }
        adapter.notifyDataSetChanged(); // 리사이클러 갱신
    }
    class Recy_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        View.OnClickListener clickListener;

        public Recy_Adapter(View.OnClickListener clickListener){
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_write,parent,false);
            view.setOnClickListener(clickListener);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(IMAGEs.size() != 0) {
                if (position == IMAGEs.size()) { // 마지막 포지션에 add 이미지 추가
                    ((CustomViewHolder) holder).imageView.setImageResource(R.drawable.ic_img_add);
                    return;
                }
                Glide.with(holder.itemView.getContext()).load(IMAGEs.get(position)).into(((CustomViewHolder)holder).imageView);
            }
        }

        @Override
        public int getItemCount() {
            return IMAGEs.size() + 1; // 기존있던거 + 새로 추가한거 + add 이미지
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public CustomViewHolder(View view) {
                super(view);
                imageView= view.findViewById(R.id.item_board_img_plus);
                GradientDrawable drawable = (GradientDrawable)view.getContext().getDrawable(R.drawable.imageview_round_corner);
                imageView.setBackground(drawable);
                imageView.setClipToOutline(true);
            }
        }
    }
}
