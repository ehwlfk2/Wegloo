package com.example.target_club_in_donga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MaterialManagementActivity_Client extends AppCompatActivity {

    ImageView material_management_image1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_client);

        material_management_image1 = (ImageView) findViewById(R.id.material_management_image1);

        material_management_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(MaterialManagementActivity_Client.this, MaterialManagementActivity_Detail.class);
                startActivity(intent);
            }
        });

        material_management_image1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"롱클릭이벤트", Toast.LENGTH_SHORT).show();
                return true;
            }

        });

    }
}
