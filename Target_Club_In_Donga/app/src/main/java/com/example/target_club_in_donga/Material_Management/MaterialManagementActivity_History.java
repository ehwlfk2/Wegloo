package com.example.target_club_in_donga.Material_Management;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;

public class MaterialManagementActivity_History extends AppCompatActivity {

    ImageView activity_material_management_lend_imageview_image;
    Button activity_material_management_lend_button_lend;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_lend);

        activity_material_management_lend_imageview_image = (ImageView) findViewById(R.id.activity_material_management_lend_imageview_image);
        activity_material_management_lend_button_lend = (Button) findViewById(R.id.activity_material_management_lend_button_lend);
    }

}
