package com.example.target_club_in_donga.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Board_Detail extends AppCompatActivity {
    ImageButton detail_back;
    TextView name, timestamp, title, contents;
    EditText edt_title, edt_contents;
    Button edt_upload;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Spinner spinner;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    BoardModel boardModel;
    String getkey;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_detail);
        detail_back = findViewById(R.id.board_detail_back_btn);
        spinner = findViewById(R.id.board_detail_spinner);
        name = findViewById(R.id.board_detail_name);
        timestamp = findViewById(R.id.board_detail_timestamp);
        title = findViewById(R.id.board_detail_title);
        contents = findViewById(R.id.board_detail_contents);
        recyclerView = findViewById(R.id.board_detail_recy);
        edt_title = findViewById(R.id.board_edit_title);
        edt_contents = findViewById(R.id.board_detail_contents_edit);
        edt_upload = findViewById(R.id.board_detail_edit_upload);

        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        final Detail_recyAdapter detail_recyAdapter = new Detail_recyAdapter(img_clicklistner);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(detail_recyAdapter);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)view).setText(null);
                if (i == 0){ //수정
                    title.setVisibility(View.INVISIBLE);
                    edt_title.setVisibility(View.VISIBLE);
                    edt_title.setText(title.getText().toString());

                    contents.setVisibility(View.INVISIBLE);
                    edt_contents.setVisibility(View.VISIBLE);
                    edt_contents.setText(contents.getText().toString());

                    spinner.setVisibility(View.INVISIBLE);
                    edt_upload.setVisibility(View.VISIBLE);
                    edt_upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // write에 있는 피쉬번, 이미지 업로드 가져오고 기존에 있던 이미지들은 다 삭제하고 다시업로드?
                            // 이미지를 리사이클러뷰에 올렸을때 클릭 리스너를 달아서 삭제가 가능하게할까
                            // 그리고 마지막에 이미지버튼으로 추가버튼 달아줄까
                        }
                    });
                }
                else if (i == 1){ // 삭제
                    if( boardModel.idx == 0 ){ // 사진이 없으면
                        database.getReference().child("Board").child(getkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "db 삭제 오류", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if( boardModel.idx > 0 ){ // 사진이 있으면
                        delete_content();
                    }
                    Intent intent = new Intent(getApplicationContext(), Board_Main.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        Intent intent = getIntent();
        boardModel = (BoardModel)intent.getSerializableExtra("MODEL");
        getkey = intent.getStringExtra("key");
        name.setText(boardModel.username);
        //timestamp.setText(boardModel.timestamp);
        title.setText(boardModel.title);
        contents.setText(boardModel.contents);
        long unixTime = (long) boardModel.timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Korea"));
        String time = simpleDateFormat.format(date);
        timestamp.setText(time);
    }
    public View.OnClickListener img_clicklistner = new View.OnClickListener() { // 이미지 클릭이벤트
        @Override
        public void onClick(View v) {
            final int position = recyclerView.getChildPosition(v);
            Intent intent = new Intent(getApplicationContext(), Board_viewpager.class);
            intent.putExtra("imglist", boardModel.imglist);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    };
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Board_Detail.this);

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < 2; i++) {
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
        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public void delete_content(){ // 이미지 여러개 삭제후 데이터베이스 하나를 날려야된다
        for(int i = 0; i < boardModel.imgName.size() ; i++){
            storage.getReference().child("Board").child(boardModel.imgName.get(i)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "store 삭제 오류", Toast.LENGTH_SHORT).show();
                }
            });
        }
        database.getReference().child("Board").child(getkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "db 삭제 오류", Toast.LENGTH_SHORT).show();
            }
        });
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

    class Detail_recyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        View.OnClickListener img_clicklistner;
        public Detail_recyAdapter(View.OnClickListener img_clicklistner){
            this.img_clicklistner = img_clicklistner;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_detail, parent,false);
            view.setOnClickListener(img_clicklistner);
            return new CustomViewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if( boardModel.idx == 0 ){
                // do nothing
            }
            else if ( boardModel.idx > 0 ){
                Glide.with(holder.itemView.getContext()).load(boardModel.imglist.get(position)).into(((CustomViewholder)holder).imageView);
            }
        }

        @Override
        public int getItemCount() {
            return boardModel.idx;
        }
        private class CustomViewholder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public CustomViewholder(View view){
                super(view);
                imageView = (ImageView)view.findViewById(R.id.item_board__detail_img);
                GradientDrawable drawable = (GradientDrawable)view.getContext().getDrawable(R.drawable.imageview_round_corner);
                imageView.setBackground(drawable);
                imageView.setClipToOutline(true);
            }
        }
    }
}
