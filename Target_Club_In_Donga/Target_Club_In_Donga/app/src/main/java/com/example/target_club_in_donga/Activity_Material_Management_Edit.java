package com.example.target_club_in_donga;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class Activity_Material_Management_Edit extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000; // 갤러리에서 이미지를 받아오기 위한 세가지 변수
    private static final int PERMISSION_CODE = 1001; //

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private ImageView material_management_edit_image_imageview;
    private EditText material_management_edit_name_edittext;
    private Button material_management_edit_change_button;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management_edit);

        // 홈에서 공지사항을 눌었을 떄, viewPager에서 ViewPagerAdapter_Notice로 공지사항화면이 나오고
        // 오른쪽에서 왼쪽으로 슬라이드를 하면 홈 화면이 나오도록 한다.

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        material_management_edit_image_imageview = (ImageView) findViewById(R.id.material_management_edit_image_imageview);
        material_management_edit_name_edittext = (EditText) findViewById(R.id.material_management_edit_name_edittext);
        material_management_edit_change_button = (Button) findViewById(R.id.material_management_edit_change_button);

        material_management_edit_image_imageview.setOnClickListener(new View.OnClickListener() {
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

        material_management_edit_change_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                upload(imagePath);
                Intent intent = new Intent(Activity_Material_Management_Edit.this, Activity_Material_Management_Admin.class);
                startActivity(intent);
                finish();


//                Intent intent = new Intent(Activity_Material_Management_Edit.this, Activity_Material_Management_Detail.class);
/*                Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), material_management_edit_image);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);*/


//                startActivity(intent);


//                finish();
/*                Intent intent = new Intent(Activity_Material_Management_Edit.this, Activity_Material_Management_Admin.class);
                startActivity(intent);*/
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
            material_management_edit_image_imageview.setImageURI(Uri.fromFile(f));

/*            Intent intent = new Intent(Activity_Material_Management_Edit.this, Activity_Material_Management_Admin.class);

*//*            Uri uri = (Uri)data.getData();
            intent.putExtra("uri", uri.toString());*//*
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable)ImageViewChange.getDrawable()).getBitmap();

*//*            float scale = (float) (1024/(float)bitmap.getWidth());
            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createBitmap(bitmap, image_w, image_h, true);*//*

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            intent.putExtra("image", byteArray);
            startActivity(intent);*/

//                backgrounduri = result.getUri();
//                logo.setImageURI(backgrounduri);
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
        StorageReference storageRef = storage.getReferenceFromUrl("gs://android-studio-firebase-872f2.appspot.com");

        final Uri file = Uri.fromFile(new File(uri));
        StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.imageUri = downloadUri.toString();
                    imageDTO.edit_name_edittext = material_management_edit_name_edittext.getText().toString();
//                    imageDTO.edit_lender_edittext = edit_lender_edittext.getText().toString();
                    imageDTO.uId = auth.getCurrentUser().getUid();
                    imageDTO.userId = auth.getCurrentUser().getEmail();
                    imageDTO.imageName = file.getLastPathSegment();

                    database.getReference().child("images").push().setValue(imageDTO);


            }
        });

    }

}
