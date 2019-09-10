package com.example.target_club_in_donga.Gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.example.target_club_in_donga.Gallery.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Gallery_Board extends AppCompatActivity {// 제목, 썸네일이 존재하는 게시글 목록창

    private RecyclerView recyclerView;
    private ArrayList<GalleryModel> galleryModels = new ArrayList<>();
    private ArrayList<String> uidlist = new ArrayList<>();
    ImageButton backbtn;
    Button write;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_board);
        backbtn = (ImageButton)findViewById(R.id.gallery_back_btn);
        write = (Button)findViewById(R.id.gallery_board_write_btn);
        recyclerView = (RecyclerView)findViewById(R.id.gallery_board_recy);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        LinearLayoutManager mlayout = new LinearLayoutManager(this);
        mlayout.setReverseLayout(true);
        mlayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(mlayout);
        final GalleryBoardRecy_Adapter galleryBoardRecy_adapter = new GalleryBoardRecy_Adapter(board_clicklistner);
        recyclerView.setAdapter(galleryBoardRecy_adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),1));
        CheckTypesTask checkTypesTask = new CheckTypesTask();
        checkTypesTask.execute();
        galleryBoardRecy_adapter.notifyDataSetChanged();

        database.getReference().child("Gallery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                galleryModels.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    GalleryModel galleryModel = snapshot.getValue(GalleryModel.class);
                    String uidkey = snapshot.getKey();
                    galleryModels.add(galleryModel);
                    uidlist.add(uidkey);
                }
                galleryBoardRecy_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Gallery_Write.class);
                startActivity(intent);
            }
        });
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Gallery_Board.this);

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
        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
    public View.OnClickListener board_clicklistner = new View.OnClickListener() { // 이미지 클릭이벤트
        @Override
        public void onClick(View v) {
            final int position = recyclerView.getChildPosition(v);
            Intent intent = new Intent(getApplicationContext(), Gallery_Detail.class);
            intent.putExtra("MODEL",galleryModels.get(position));
            startActivity(intent);
        }
    };

    class GalleryBoardRecy_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
        View.OnClickListener board_clickListener;

        public GalleryBoardRecy_Adapter(View.OnClickListener board_clickListener){
            this.board_clickListener = board_clickListener;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_board, parent,false);
            view.setOnClickListener(board_clickListener);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).title.setText(galleryModels.get(position).title);
            ((CustomViewHolder)holder).contents.setText(galleryModels.get(position).contents);
            ((CustomViewHolder)holder).writer.setText(galleryModels.get(position).username);
            long unixTime = (long)galleryModels.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Korea"));
            String time = simpleDateFormat.format(date);
            ((CustomViewHolder)holder).date.setText(time);
            Glide.with(holder.itemView.getContext()).load(galleryModels.get(position).imglist.get(0)).into(((CustomViewHolder)holder).imageView);
        }

        @Override
        public int getItemCount() {
            return galleryModels.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView title, contents, date, writer;
            ImageView imageView;
            public CustomViewHolder(View view){
                super(view);
                title = (TextView)view.findViewById(R.id.gallery_board_title_txt);
                contents = (TextView)view.findViewById(R.id.gallery_board_contents_txt);
                date = (TextView)view.findViewById(R.id.gallery_board_date_txt);
                writer = (TextView)view.findViewById(R.id.gallery_board_writer_txt);
                imageView = (ImageView)view.findViewById(R.id.gallery_board_thumb);
                GradientDrawable drawable = (GradientDrawable)view.getContext().getDrawable(R.drawable.imageview_round_corner);
                imageView.setBackground(drawable);
                imageView.setClipToOutline(true);
            }
        }
    }
}
