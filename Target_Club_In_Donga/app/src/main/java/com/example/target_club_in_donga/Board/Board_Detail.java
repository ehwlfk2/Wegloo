package com.example.target_club_in_donga.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.club_foundation_join.JoinData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userAdmin;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userNicName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userProfileUrl;

public class Board_Detail extends AppCompatActivity {
    ImageButton detail_back, edt_menu;
    TextView name, timestamp, title, contents;
    EditText comment;
    ImageView userprofilepic, upload_comment;
    RecyclerView recyclerView, comment_recycler;
    ArrayList<Comment_Model> comment_models = new ArrayList<>();
    ArrayList<String> comment_keys = new ArrayList<>();
    Comment_Model comment_model = new Comment_Model();
    LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    BoardModel boardModel = new BoardModel();
    String getkey;
    InputMethodManager imm;
    ConstraintLayout detail_view;
    int delete_comment_admin = 0;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_detail);
        detail_back = findViewById(R.id.board_detail_back_btn);
        edt_menu = findViewById(R.id.board_detail_3dotmenu);
        name = findViewById(R.id.board_detail_name);
        timestamp = findViewById(R.id.board_detail_timestamp);
        title = findViewById(R.id.board_detail_title);
        contents = findViewById(R.id.board_detail_contents);
        recyclerView = findViewById(R.id.board_detail_recy);
        userprofilepic = findViewById(R.id.board_detail_user_imageView);
        comment_recycler = findViewById(R.id.board_comment_recy);
        comment = findViewById(R.id.board_comment);
        upload_comment = findViewById(R.id.board_comment_upload);
        detail_view = findViewById(R.id.detail_view);
        detail_view.setOnClickListener(viewclick);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final Detail_recyAdapter detail_recyAdapter = new Detail_recyAdapter(img_clicklistner);
        final Comment_Adapter comment_adapter = new Comment_Adapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(detail_recyAdapter);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

        comment_recycler.setLayoutManager(new LinearLayoutManager(this));
        comment_recycler.setAdapter(comment_adapter);

        Intent intent = getIntent();
        getkey = intent.getStringExtra("key");
        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upload_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String comments = comment.getText().toString();
                if (comments.getBytes().length <= 0) {
                    Toast.makeText(Board_Detail.this, "댓글 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    comment_model.comment = comments;
                    comment_model.name = userNicName;
                    comment_model.userProfilePic = userProfileUrl;
                    comment_model.time = ServerValue.TIMESTAMP;
                    comment_model.uid = auth.getCurrentUser().getUid();
                    DatabaseReference databaseReference = database.getReference().child("EveryClub").child(clubName).child("Board").child(getkey).child("Comment");
                    databaseReference.push().setValue(comment_model);
                }
                comment.setText("");
            }
        });
        database.getReference().child("EveryClub").child(clubName).child("Board").child(getkey).child("Comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                delete_comment_admin = 0;
                comment_models.clear();
                comment_keys.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment_Model comment_model = snapshot.getValue(Comment_Model.class);
                    String key = snapshot.getKey();
                    if (comment_model.uid.equals(auth.getCurrentUser().getUid()) || userAdmin < 3){ // 내댓글, 임원
                        delete_comment_admin =1;
                    }
                    comment_keys.add(key);
                    comment_models.add(comment_model);
                }
                comment_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        database.getReference().child("EveryClub").child(clubName).child("Board").child(getkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boardModel = null;
                boardModel = dataSnapshot.getValue(BoardModel.class);
                title.setText(boardModel.title);
                contents.setText(boardModel.contents);
                name.setText(boardModel.name);
                Glide.with(getApplicationContext()).load(userProfileUrl).into(userprofilepic);
                if (boardModel.uid.equals(auth.getCurrentUser().getUid()) || userAdmin < 3) { // 작성자면,
                    edt_menu.setVisibility(View.VISIBLE);
                }
                long unixTime = (long) boardModel.timestamp;
                Date date = new Date(unixTime);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Korea"));
                String time = simpleDateFormat.format(date);
                timestamp.setText(time);
                detail_recyAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        edt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardModel.uid.equals(auth.getCurrentUser().getUid())) {
                    showOption(view);
                } else if (userAdmin < 3) {
                    mastershowOption(view);
                }
            }
        });
    }

    View.OnClickListener viewclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard();
        }
    };

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
    }

    void mastershowOption(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.delete_board);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_board_master:
                        if (boardModel.idx == 0) { // 사진이 없으면
                            database.getReference().child("EveryClub").child(clubName).child("Board").child(getkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        } else if (boardModel.idx > 0) { // 사진이 있으면
                            delete_content();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("delete_board_detail", 2);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    void showOption(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.detail_edit_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editwrite:
                        if (comment_models.size() != 0) { // 댓글이 있으면 수정 불가
                            Toast.makeText(Board_Detail.this, "댓글이 달린 게시글은 수정이 불가능합니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Intent intent1 = new Intent(getApplicationContext(), Board_Write.class);
                        intent1.putExtra("edt_model", boardModel);
                        intent1.putExtra("edt_key", 1);
                        intent1.putExtra("updatekey", getkey);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.deletedetail:
                        if (boardModel.idx == 0) { // 사진이 없으면
                            database.getReference().child("EveryClub").child(clubName).child("Board").child(getkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        } else if (boardModel.idx > 0) { // 사진이 있으면
                            delete_content();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("delete_board_detail", 2);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }


    public View.OnClickListener img_clicklistner = new View.OnClickListener() { // 이미지 클릭이벤트
        @Override
        public void onClick(View v) {
            final int position = recyclerView.getChildPosition(v);
            Intent intent = new Intent(getApplicationContext(), Board_viewpager.class);
            intent.putExtra("imglist", boardModel.imglist);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    };

    public void delete_content() { // 이미지 여러개 삭제후 데이터베이스 하나를 날려야된다
        for (int i = 0; i < boardModel.imgName.size(); i++) {
            storage.getReference().child("EveryClub").child(clubName).child("Board").child(boardModel.imgName.get(i)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        database.getReference().child("EveryClub").child(clubName).child("Board").child(getkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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


    public class RecyclerViewDecoration extends RecyclerView.ItemDecoration { // 리사이클러 이미지 간격 띄우는 클래스
        private final int divheight;

        public RecyclerViewDecoration(int divheight) {
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

    class Detail_recyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        View.OnClickListener img_clicklistner;

        public Detail_recyAdapter(View.OnClickListener img_clicklistner) {
            this.img_clicklistner = img_clicklistner;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_detail, parent, false);
            view.setOnClickListener(img_clicklistner);
            return new CustomViewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (boardModel.idx == 0) {
                // do nothing
            } else if (boardModel.idx > 0) {
                Glide.with(holder.itemView.getContext()).load(boardModel.imglist.get(position)).override(100, 100).placeholder(R.drawable.default_loadimg).into(((CustomViewholder) holder).imageView);
            }
        }

        @Override
        public int getItemCount() {
            return boardModel.idx;
        }

        private class CustomViewholder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public CustomViewholder(View view) {
                super(view);
                imageView = view.findViewById(R.id.item_board__detail_img);
                GradientDrawable drawable = (GradientDrawable) view.getContext().getDrawable(R.drawable.imageview_round_corner);
                imageView.setBackground(drawable);
                imageView.setClipToOutline(true);
            }
        }
    }

    class Comment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_comment, parent, false);
            return new Commentholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((Commentholder) holder).name.setText(comment_models.get(position).name);
            ((Commentholder) holder).comment.setText(comment_models.get(position).comment);
            Glide.with(holder.itemView.getContext()).load(comment_models.get(position).userProfilePic).override(35,35).placeholder(R.drawable.face).into(((Commentholder)holder).proPic);
            long unixTime = (long) comment_models.get(position).time;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Korea"));
            String time = simpleDateFormat.format(date);
            ((Commentholder)holder).time.setText(time);
        }

        @Override
        public int getItemCount() {
            return comment_models.size();
        }

        private class Commentholder extends RecyclerView.ViewHolder {
            TextView name, comment, time, delete;
            ImageView proPic;

            public Commentholder(View view) {
                super(view);
                proPic = view.findViewById(R.id.comment_proPic);
                name = view.findViewById(R.id.comment_name);
                comment = view.findViewById(R.id.comment_content);
                time = view.findViewById(R.id.comment_time);
                delete = view.findViewById(R.id.comment_delete);
                if (delete_comment_admin == 1){
                    delete.setVisibility(View.VISIBLE);
                }
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            database.getReference().child("EveryClub").child(clubName).child("Board").child(getkey).child("Comment").child(comment_keys.get(pos)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "댓글 삭제 완료", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "댓글 삭제 오류", Toast.LENGTH_SHORT).show();
                                }
                            });
                            notifyItemChanged(pos);
                        }
                    }
                });
            }
        }
    }
}
