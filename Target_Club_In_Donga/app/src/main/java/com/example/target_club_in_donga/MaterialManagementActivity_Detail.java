package com.example.target_club_in_donga;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MaterialManagementActivity_Detail extends AppCompatActivity {

    ImageView activity_material_management_detail_imageview_image;
    Button activity_material_management_detail_button_lend;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_detail);

        activity_material_management_detail_imageview_image = (ImageView) findViewById(R.id.activity_material_management_detail_imageview_image);
        activity_material_management_detail_button_lend = (Button) findViewById(R.id.activity_material_management_detail_button_lend);
    }

}
