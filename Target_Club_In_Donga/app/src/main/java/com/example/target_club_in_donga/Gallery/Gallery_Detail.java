package com.example.target_club_in_donga.Gallery;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Gallery.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Gallery_Detail extends AppCompatActivity {
    ImageButton detail_back;
    TextView name, timestamp, title, contents;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    GalleryModel galleryModel;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery__detail);
        detail_back = (ImageButton)findViewById(R.id.gallery_detail_back_btn);
        name = (TextView) findViewById(R.id.gallery_detail_name);
        timestamp = (TextView) findViewById(R.id.gallery_detail_timestamp);
        title = (TextView) findViewById(R.id.gallery_detail_title);
        contents = (TextView) findViewById(R.id.gallery_detail_contents);
        recyclerView = (RecyclerView)findViewById(R.id.gallery_detail_recy);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        final Detail_recyAdapter detail_recyAdapter = new Detail_recyAdapter(img_clicklistner);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(detail_recyAdapter);
        recyclerView.addItemDecoration(recyclerViewDecoration);

        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        Intent intent = getIntent();
        galleryModel = (GalleryModel)intent.getSerializableExtra("MODEL");
        name.setText(galleryModel.username);
        //timestamp.setText(galleryModel.timestamp);
        title.setText(galleryModel.title);
        contents.setText(galleryModel.contents);
        long unixTime = (long)galleryModel.timestamp;
        Date date = new Date(unixTime);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Korea"));
        String time = simpleDateFormat.format(date);
        timestamp.setText(time);
    }
    public View.OnClickListener img_clicklistner = new View.OnClickListener() { // 이미지 클릭이벤트
        @Override
        public void onClick(View v) {
            final int position = recyclerView.getChildPosition(v);
            Intent intent = new Intent(getApplicationContext(), Gallery_Viewpager.class);
            intent.putExtra("imglist",galleryModel.imglist);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    };
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Gallery_Detail.this);

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_detail, parent,false);
            view.setOnClickListener(img_clicklistner);
            return new CustomViewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext()).load(galleryModel.imglist.get(position)).into(((CustomViewholder)holder).imageView);
        }

        @Override
        public int getItemCount() {
            return galleryModel.idx;
        }
        private class CustomViewholder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public CustomViewholder(View view){
                super(view);
                imageView = (ImageView)view.findViewById(R.id.item_gallery__detail_img);
                GradientDrawable drawable = (GradientDrawable)view.getContext().getDrawable(R.drawable.imageview_round_corner);
                imageView.setBackground(drawable);
                imageView.setClipToOutline(true);
            }
        }
    }
}
