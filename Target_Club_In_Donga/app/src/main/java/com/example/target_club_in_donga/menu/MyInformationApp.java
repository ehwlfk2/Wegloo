package com.example.target_club_in_donga.menu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MyInformationApp extends AppCompatActivity {
    private ImageButton myinfo_back_app, myinfo_confirm_app;
    private ImageView myinfo_profile_Thumbnail_app;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private String imagePath;
    private static final int GALLERY_CODE = 10;
    //private String infoUrl;
    private String infoDelete;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinformation_app);

        /*권한*/
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        myinfo_back_app = findViewById(R.id.myinfo_back_app);
        myinfo_confirm_app = findViewById(R.id.myinfo_confirm_app);
        myinfo_profile_Thumbnail_app = findViewById(R.id.myinfo_profile_Thumbnail_app);
        firebaseDatabase.getReference().child("AppUser").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyInformationApp_Item myInformationApp_item = dataSnapshot.getValue(MyInformationApp_Item.class);
                //Log.e("imageUrl",imageUrl+"");
                if(!myInformationApp_item.getRealNameProPicUrl().equals("None")){
                    Glide.with(MyInformationApp.this).load(myInformationApp_item.getRealNameProPicUrl()).into(myinfo_profile_Thumbnail_app);
                }
                infoDelete = myInformationApp_item.getRealNameProPicDeleteName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        myinfo_back_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myinfo_profile_Thumbnail_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        myinfo_confirm_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePath == null){
                    finish();
                }
                else{
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("수정중입니다...");
                    progressDialog.show();
                    delete_item(infoDelete);
                }
                //if(imagePath.equals())
                //Log.e("imagePath",imagePath);
                //Log.e("url",infoUrl);
                //Log.e("de",infoDelete);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            //String path = data.getData();
            try {
                imagePath = getPath(data.getData());
                File f = new File(imagePath);
                myinfo_profile_Thumbnail_app.setImageURI(Uri.fromFile(f));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }

    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }

    private void upload(String uri){

        final String userUid = firebaseAuth.getCurrentUser().getUid();
        try{
            StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            final Uri file = Uri.fromFile(new File(uri));
            StorageReference riversRef = storageRef.child("AppUser_ProfileImages/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { //이미지가 먼저 일단 올라가고 그게완료대면 디비에 패스올려줄꺼
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map<String, Object> map = new HashMap<>();
                    map.put("realNameProPicDeleteName",file.getLastPathSegment());
                    map.put("realNameProPicUrl",downloadUrl.toString());
                    firebaseDatabase.getReference().child("AppUser").child(userUid).updateChildren(map);
                    //firebaseDatabase.getReference().child("AppUser").child(userUid).setValue(myInformationApp_item);
//                    firebaseDatabase.getReference().child("AppUser").child(userUid).child("realNameProPicDeleteName").setValue(file.getLastPathSegment());
//                    firebaseDatabase.getReference().child("AppUser").child(userUid).child("realNameProPicUrl").setValue(downloadUrl.toString());
                    progressDialog.dismiss();
                    finish();
                }
            });
        }catch (NullPointerException e){
            Map<String, Object> map = new HashMap<>();
            map.put("realNameProPicDeleteName","None");
            map.put("realNameProPicUrl","None");
            firebaseDatabase.getReference().child("AppUser").child(userUid).updateChildren(map);
            progressDialog.dismiss();
            //firebaseDatabase.getReference().child("AppUser").child(userUid).setValue(myInformationApp_item);
            finish();
        }
    }


    public void delete_item(final String storageKey){
        if(!storageKey.equals("None")){
            firebaseStorage.getReference().child("AppUser_ProfileImages").child(storageKey).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {
                    upload(imagePath);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {
                    //Toast.makeText(AccountBookActivity_Main.this, "스토리지 삭제실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            upload(imagePath);
        }
    }
}
