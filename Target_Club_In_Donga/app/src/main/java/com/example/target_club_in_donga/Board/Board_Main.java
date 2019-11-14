package com.example.target_club_in_donga.Board;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class Board_Main extends AppCompatActivity {// 제목, 썸네일이 존재하는 게시글 목록창

    private RecyclerView recyclerView;
    private ArrayList<BoardModel> boardModels = new ArrayList<>();
    private ArrayList<String> uidlist = new ArrayList<>();
    ImageButton backbtn;
    Button write;
    private FirebaseDatabase database;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_main);
        backbtn = findViewById(R.id.board_back_btn);
        write = findViewById(R.id.board_write_btn);
        recyclerView = findViewById(R.id.board_recy);
        database = FirebaseDatabase.getInstance();
        LinearLayoutManager mlayout = new LinearLayoutManager(this);
        mlayout.setReverseLayout(true);
        mlayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(mlayout);
        final BoardRecy_Adapter boardRecy_adapter = new BoardRecy_Adapter(board_clicklistner);
        recyclerView.setAdapter(boardRecy_adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),1));
        CheckTypesTask checkTypesTask = new CheckTypesTask();
        checkTypesTask.execute();
        boardRecy_adapter.notifyDataSetChanged();


        database.getReference().child("EveryClub").child(clubName).child("Board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boardModels.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    BoardModel boardModel = snapshot.getValue(BoardModel.class);
                    String uidkey = snapshot.getKey();
                    boardModels.add(boardModel);
                    uidlist.add(uidkey);
                }
                boardRecy_adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(boardModels.size()-1);
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
                Intent intent = new Intent(getApplicationContext(), Board_Write.class);
                startActivity(intent);
            }
        });
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Board_Main.this);

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
            asyncDialog.setMessage("로드 중입니다..");

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
            String pos = String.valueOf(position);
            Toast.makeText(Board_Main.this, pos, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Board_Detail.class);
            intent.putExtra("MODEL", boardModels.get(position));
            intent.putExtra("key", uidlist.get(position));
            startActivity(intent);
        }
    };

    class BoardRecy_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
        View.OnClickListener board_clickListener;

        public BoardRecy_Adapter(View.OnClickListener board_clickListener){
            this.board_clickListener = board_clickListener;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent,false);
            view.setOnClickListener(board_clickListener);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).title.setText(boardModels.get(position).title);
            ((CustomViewHolder)holder).contents.setText(boardModels.get(position).contents);
            ((CustomViewHolder)holder).writer.setText(boardModels.get(position).username);
            long unixTime = (long) boardModels.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Korea"));
            String time = simpleDateFormat.format(date);
            ((CustomViewHolder)holder).date.setText(time);
            if( boardModels.get(position).idx == 0 ){ // 사진이 없으면
                ((CustomViewHolder)holder).imageView.setVisibility(View.GONE);
            }
            else if ( boardModels.get(position).idx > 0 ) { // 사진이 있으면
                Glide.with(holder.itemView.getContext()).load(boardModels.get(position).imglist.get(0)).into(((CustomViewHolder)holder).imageView);
            }
        }

        @Override
        public int getItemCount() {
            return boardModels.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView title, contents, date, writer;
            ImageView imageView;
            public CustomViewHolder(View view){
                super(view);
                title = view.findViewById(R.id.board_title_txt);
                contents = view.findViewById(R.id.board_contents_txt);
                date = view.findViewById(R.id.board_date_txt);
                writer = view.findViewById(R.id.board_writer_txt);
                imageView = view.findViewById(R.id.board_thumb);
                GradientDrawable drawable = (GradientDrawable)view.getContext().getDrawable(R.drawable.imageview_round_corner);
                imageView.setBackground(drawable);
                imageView.setClipToOutline(true);
            }
        }
    }
}
