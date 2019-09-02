package com.example.target_club_in_donga;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_Material_Management_Detail extends AppCompatActivity {

    ImageView material_management_detail_image;
    Button material_management_detail_btn2;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_detail);

        material_management_detail_image = (ImageView) findViewById(R.id.material_management_detail_image);
        material_management_detail_btn2 = (Button) findViewById(R.id.material_management_detail_btn2);

/*        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        material_management_detail_image.setImageBitmap(bitmap);*/

// 갤러리에서 가져온 이미지로 ImageView를 바꿀 수 있다.

/*        material_management_detail_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(Activity_Material_Management_Detail.this, Activity_Material_Management_Admin.class);
                startActivity(intent);
            }
        });*/

    }

}
