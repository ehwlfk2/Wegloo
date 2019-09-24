package com.example.target_club_in_donga.Material_Management;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaterialManagementActivity_Insert extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000; // 갤러리에서 이미지를 받아오기 위한 세가지 변수
    private static final int PERMISSION_CODE = 1001; //

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    private ImageView activity_material_management_insert_imageview_image;
    private EditText activity_material_management_insert_edittext_item_name;
    private Button activity_material_management_insert_button_insert;
    private String imagePath;
    private TextView activity_material_management_insert_textview_lender;

    private long now;

    String material_path, getEditName;

    int count = 0;
    //메뉴를 클릭했는지 안했는지 확인하기 위해서 사용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_insert);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        activity_material_management_insert_imageview_image = (ImageView) findViewById(R.id.activity_material_management_insert_imageview_image);
        activity_material_management_insert_edittext_item_name = (EditText) findViewById(R.id.activity_material_management_insert_edittext_item_name);
        activity_material_management_insert_button_insert = (Button) findViewById(R.id.activity_material_management_insert_button_insert);
        activity_material_management_insert_textview_lender = (TextView) findViewById(R.id.activity_material_management_insert_textview_lender);

        activity_material_management_insert_imageview_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }

                } else {
                    pickImageFromGallery();
                }

            }
        });

        activity_material_management_insert_button_insert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getEditName = activity_material_management_insert_edittext_item_name.getText().toString();
                // 물품명을 쓰지 않았을 경우를 알기위해서 String값으로 받아옴
                getEditName = getEditName.trim();
                // 띄어쓰기만 했을 떄 입력이 안먹히도록 하기 위해서

                if(count > 0) {
                    if(getEditName.getBytes().length > 0) {
                        Toast.makeText(MaterialManagementActivity_Insert.this, "상품이 추가되었습니다", Toast.LENGTH_SHORT).show();
                        upload(imagePath);
                        finish();
                    } else {
                        Toast.makeText(MaterialManagementActivity_Insert.this, "물품명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MaterialManagementActivity_Insert.this, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    } // 로고를 바꾸기 위해 필요한 함수 상수변수에 대한 값 1000 가져옴 (코드는 자세히 모름)

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    } // 코드는 자세히는 모르나 처음 갤러리로 접근 하였을 떄, 접근을 허용하면 갤러리로 들어가지고 허용 하지 않으면 Permission denied 이라고 밑에 띄어줌

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            imagePath = getPath(data.getData());
            File f = new File(imagePath);
            activity_material_management_insert_imageview_image.setImageURI(Uri.fromFile(f));
            count++;

        }
//        }
    } // 위에 있는 변수값을 가져와서 앨범을 누르면 pickImageFromGallery() 실행되어 값 1000 들어오고 if문에 걸려 로고가 바뀌고,
    // background 이미지 뷰를 누르면 pickImageFromGallery2() 실행되어 값 1002 들어와서 else if문에 걸려 배경화면이 바뀐다.

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }

    private void upload(String uri) {

        now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //"yyyy-MM-dd HH:mm:ss"
        material_path = simpleDateFormat.format(date);

        StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

        final Uri file = Uri.fromFile(new File(uri));
        StorageReference riversRef = storageRef.child("Material_Management/" + material_path + '-' + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();

                MaterialManagement_Item materialManagementItem = new MaterialManagement_Item();
                materialManagementItem.title = activity_material_management_insert_edittext_item_name.getText().toString();
                materialManagementItem.lender = activity_material_management_insert_textview_lender.getText().toString();
                materialManagementItem.timestamp = "없음";
                materialManagementItem.imageUri = downloadUri.toString();
                materialManagementItem.imageName = material_path + '-' + file.getLastPathSegment();
                materialManagementItem.state = 0;

                database.getReference().child("Material_Management").push().setValue(materialManagementItem);

            }
        });


    }

}
