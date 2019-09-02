package com.example.target_club_in_donga.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Attend extends Fragment {

    private Gallery gallery;
    private Button select_btn;
    private Integer current_image_resource;

    private final int[] img = {R.drawable.aa, R.drawable.bb, R.drawable.cc,R.drawable.dd,R.drawable.ee};
    public Fragment_Attend() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_attend, container, false);
        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), R.layout.activity_attend_sub_layout,img);
        gallery = (Gallery)view.findViewById(R.id.activity_attend_gallery);
        select_btn = (Button)view.findViewById(R.id.activity_attend_select_btn);

        gallery.setAdapter(galleryAdapter);
        final ImageView imageView = (ImageView)view.findViewById(R.id.test);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_image_resource = img[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(current_image_resource);
            }
        });
        return view;
    }

}
class GalleryAdapter extends BaseAdapter{
    Context context;
    int layout;
    int img[];
    LayoutInflater layoutInflater;

    public GalleryAdapter(Context context, int layout, int[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = layoutInflater.inflate(layout,null);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.activity_attend_sub_layout_imageview);
        imageView.setImageResource(img[position]);
        return convertView;
    }
}
