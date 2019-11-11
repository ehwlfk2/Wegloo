package com.example.target_club_in_donga.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;

import java.util.ArrayList;

public class Board_viewpager extends AppCompatActivity {
    private ViewPager viewPager;
    PagerAdapter pagerAdapter;
    ArrayList<String> imglist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.board_viewpager);
        viewPager = findViewById(R.id.board_viewpager);
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
                View view = getLayoutInflater().inflate(R.layout.item_board_viewpager, null);
                final ImageView imageView = view.findViewById(R.id.item_viewpager_imgview);
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
        viewPager.setCurrentItem(idx);
    }
}
