package com.example.target_club_in_donga.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Board_Write extends AppCompatActivity {
    ImageButton board_img_upload_btn,x_btn;
    Button upload_btn;
    EditText title, contents;
    private RecyclerView recyclerView;
    private Recy_Adapter adapter;
    private ArrayList<Uri> uris = new ArrayList<>(); // 이미지 uri 리스트
    private ArrayList<String> IMAGEs = new ArrayList<>(); // boardmodel 이미지랑 uri 이미지 합치기
    private ArrayList<String> imageNames = new ArrayList<>();
    private ArrayList<Boolean> boolList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
    private FirebaseStorage storage;
    private FirebaseAuth auth;
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

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 0);

        x_btn = findViewById(R.id.x_btn);
        upload_btn = findViewById(R.id.board_write_upload_btn);
        title = findViewById(R.id.title_write_edt);
        contents = findViewById(R.id.content_write_edt);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

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
            IMAGEs = boardModel.imglist;
            // 수정때 관리해줘야할 목록
            // 1. 리사이클러뷰 내 이미지 삭제(boardmodel 내부 path, name, idx)
            // 2. 앨범에서 이미지 추가시 uri리스트를 boardmodel path,name,idx 맨 뒤에 추가
            // 3. 업로드때 uris.size말고 boardmodel.idx로 카운트
            // 4. 리사이클러뷰 이미지 마지막에 + 이미지 추가
        }

        x_btn.setOnClickListener(new View.OnClickListener() { // 뒤로가기
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() { //완료
            @Override
            public void onClick(View v) {
                if (uris.size() != 0) { // 새 글 쓰기시 사진이 있다
                    if( edt_key == 1) { // 수정을 하는데 사진을 추가했다 -> 추가한 uri를 imglist 뒤에 붙이자
                    }
                    else{ // 수정이 아니다
                        writeGalleryInfo();
                    }
                } else if (uris.size() == 0) { // 새 글 쓰기시 사진이없다 & 수정시 사진을 새로 업로드 안할시
                    if(edt_key==1){ // 수정
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(clubName).child("Board");
                        //FirebaseUser currentuser = auth.getCurrentUser();
                        boardModel.title = title.getText().toString();
                        boardModel.contents = contents.getText().toString();
                        //boardModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        //boardModel.username = currentuser.getDisplayName();
                        boardModel.timestamp = ServerValue.TIMESTAMP;
                        databaseReference.child(updatekey).setValue(boardModel);
                        finish();
                    }
                    else { // 글쓰기 업로드
                        StoreDatabase();
                        finish();
                    }
                }
            }
        });

        board_img_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //사진등록
                try {
                    FishBun.with(Board_Write.this).setImageAdapter(new GlideAdapter()).setMaxCount(15).setMinCount(1).setActionBarTitle("사진을 선택해주세요").setPickerSpanCount(4).textOnNothingSelected("nothing selected").startAlbum();
                } catch (Exception e) {
                    Log.e("gallery pick error", " ..");
                }
            }
        });
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Board_Write.this);

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < 3; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("업로드 중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }
    }
    private void writeGalleryInfo() {
        final StorageReference storageRef = storage.getReference();
        long unix = System.currentTimeMillis();
        Date date = new Date(unix);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        final String times = simpleDateFormat.format(date);
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
        for (int i = 0; i < uris.size(); i++) {
            final Uri individualImage = uris.get(i);
            final StorageReference ImageName = storageRef.child(clubName).child("Board/" + individualImage.getLastPathSegment() + '-' + times);
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
                        StoreDatabase();
                        boardModel.idx = 0;
                        finish();
                    }
                }
            });
        }
    }
    private void StoreDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(clubName).child("Board");
        //FirebaseUser currentuser = auth.getCurrentUser();
        boardModel.title = title.getText().toString();
        boardModel.contents = contents.getText().toString();
        //boardModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //boardModel.username = currentuser.getDisplayName();
        boardModel.timestamp = ServerValue.TIMESTAMP;
        databaseReference.push().setValue(boardModel);
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
            if(position == uris.size() | position==boardModel.idx | position == boardModel.idx+uris.size()){ // + 이미지 클릭시 , 포지션 = 마지막
                for(int i =0; i<boolList.size(); i++){
                    boolList.set(i,false);
                }
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
                        if(edt_key==1){ // 수정시, 이미지 삭제할땐 이미지들은 컨테이너에 있으니 컨테이너 내용을 건드려줘야한다.
                            boardModel.imglist.remove(position);
                            boardModel.imgName.remove(position);
                            boardModel.idx -= 1;
                        }
                        else { // 수정이 아닐때 삭제는 앨범리스트만
                            uris.remove(position);
                        }
                        adapter.notifyDataSetChanged();
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
                    for(int i =0; i<adduri.size(); i++) { //
                        boolList.add(false); // 이미지를 넣어주면 해당 인덱스를 true 로
                        uris.add(adduri.get(i)); // 기존에 있던 이미지들의 뒤에 추가해준다
                        IMAGEs.add(String.valueOf(adduri.get(i)));
                        // 앨범에서 고른 이미지들을 리스트에 넣어주기
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
            if(edt_key==1) { // 수정시 => 이부분은 uris, boardmodel.imglist를 한 리스트에 담아서 다시 할것
                if(uris.size() == 0) { // 이미지 추가없을때
                    if(position == boardModel.idx){ // 이미지를 다 로드시킨후, 포지션이 오버됐을때 +이미지 로드후 리턴
                        ((CustomViewHolder) holder).imageView.setImageResource(R.drawable.ic_img_add);
                        return;
                    }
                    Glide.with(holder.itemView.getContext()).load(boardModel.imglist.get(position)).into(((CustomViewHolder) holder).imageView);
                }
                else if(uris.size() != 0 ){ // 이미지를 새로 추가했을때 뒤 포지션에 붙여
                    if( position == boardModel.idx + uris.size() ){ // 마지막 add 이미지
                        ((CustomViewHolder) holder).imageView.setImageResource(R.drawable.ic_img_add);
                        lastposition = boardModel.idx + uris.size();
                        return;
                    }
                    if(position == lastposition){ // 마지막 포지션에 추가
                        lastposition += 1;
                        ((CustomViewHolder) holder).imageView.setImageURI(uris.get(position-boardModel.idx));

                    }
                }
            }
            else if(uris.size() != 0) { // 새 글쓰기 페이지
                if (position == uris.size()) { // idx가 마지막일때 add 이미지를 추가
                    ((CustomViewHolder) holder).imageView.setImageResource(R.drawable.ic_img_add);
                    return;
                }
                ((CustomViewHolder) holder).imageView.setImageURI(uris.get(position));
            }
            else if(uris.size() != 0) { // 수정아니고. 새글 쓰기 페이지
                if (position == uris.size()) { // idx가 마지막일때 add 이미지를 추가
                    ((CustomViewHolder) holder).imageView.setImageResource(R.drawable.ic_img_add);
                    return;
                }
                ((CustomViewHolder) holder).imageView.setImageURI(uris.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if( edt_key == 1){
                return boardModel.idx + uris.size() + 1; // 기존있던거 + 새로 추가한거 + add 이미지
            }
            else {
                return uris.size()+1;
            }
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
