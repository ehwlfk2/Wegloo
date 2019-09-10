package com.example.target_club_in_donga.Gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.example.target_club_in_donga.Gallery.*;

import java.util.ArrayList;

public class Gallery_Viewpager extends AppCompatActivity {
    private ViewPager viewPager;
    PagerAdapter pagerAdapter;
    ArrayList<String> imglist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery__viewpager);
        viewPager = (ViewPager) findViewById(R.id.gallery_viewpager);
        Intent intent = getIntent();
        imglist = (ArrayList<String>) intent.getSerializableExtra("imglist");
        final int idx = intent.getIntExtra("position", 0);
        viewPager.setOffscreenPageLimit(imglist.size()-1);
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return imglist.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = getLayoutInflater().inflate(R.layout.item_gallery_viewpager, null);
                final ImageView imageView = (ImageView) view.findViewById(R.id.item_viewpager_imgview);
                Glide.with(view).load(imglist.get(position)).into(imageView);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // 뷰페이저에서 삭제.
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }
}
