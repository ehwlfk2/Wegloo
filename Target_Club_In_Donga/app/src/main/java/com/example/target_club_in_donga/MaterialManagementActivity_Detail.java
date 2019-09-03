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

/*        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        material_management_detail_image.setImageBitmap(bitmap);*/

// 갤러리에서 가져온 이미지로 ImageView를 바꿀 수 있다.

/*        material_management_detail_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(MaterialManagementActivity_Detail.this, MaterialManagementActivity_Admin.class);
                startActivity(intent);
            }
        });*/

    }

}
