package com.example.target_club_in_donga.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.club_foundation_join.JoinData;
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

import static com.example.target_club_in_donga.MainActivity.clubName;

public class MyInformation extends AppCompatActivity {
    private static final int GALLERY_CODE = 10;
    private String imagePath, infoDelete;
    private ImageButton myinfo_back, myinfo_confirm;
    private boolean isRealName;
    private Switch myinfo_user_pushswitch;
    private TextView myinfo_user_resume, myinfo_user_name, myinfo_user_name_title, myinfo_user_name_warning;
    private ImageView myinfo_profile_Thumbnail;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfomation);

        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        final String userUid = firebaseAuth.getCurrentUser().getUid();
        Intent intent1 = getIntent();
        isRealName = intent1.getExtras().getBoolean("thisClubIsRealName");

        myinfo_back = findViewById(R.id.myinfo_back);
        myinfo_user_pushswitch = findViewById(R.id.myinfo_user_pushswitch);
        myinfo_user_resume = findViewById(R.id.myinfo_user_resume);
        myinfo_user_name = findViewById(R.id.myinfo_user_name);
        myinfo_profile_Thumbnail = findViewById(R.id.myinfo_profile_Thumbnail);
        myinfo_confirm = findViewById(R.id.myinfo_confirm);
        myinfo_user_name_title = findViewById(R.id.myinfo_user_name_title);
        myinfo_user_name_warning = findViewById(R.id.myinfo_user_name_warning);

        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JoinData joinData = dataSnapshot.getValue(JoinData.class);
                myinfo_user_pushswitch.setChecked(joinData.isPushAlarmOnOff());
                if(joinData.getResume() != null){
                    myinfo_user_resume.setText(joinData.getResume());
                }

                if(isRealName){
                    //myinfo_user_name_layout.setVisibility(View.GONE);
                    //myinfo_user_name_layout.setBackgroundResource(R.drawable.border_gray);
                    myinfo_user_name.setEnabled(false);
                    myinfo_user_name_title.setText("이름");
                    myinfo_user_name_warning.setVisibility(View.VISIBLE);
                    //myinfo_user_name.setBackgroundResource(R.drawable.border_gray);
                }
                myinfo_user_name.setText(joinData.getName());
                if(!joinData.getRealNameProPicUrl().equals("None")){
                    Glide.with(MyInformation.this).load(joinData.getRealNameProPicUrl()).into(myinfo_profile_Thumbnail);
                }
                infoDelete = joinData.getRealNameProPicDeleteName();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myinfo_profile_Thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE);
            }
        });
        myinfo_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isRealName){ //닉네임
                    firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean nicCheck = false;
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                String tempName = ds.child("name").getValue(String.class);
                                if(tempName.equals(myinfo_user_name.getText().toString()) && !ds.getKey().equals(userUid)){
                                    nicCheck = true;
                                    break;
                                }
                            }
                            if(nicCheck){
                                Toast.makeText(MyInformation.this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(imagePath != null){ //제대로 사진하나를 수정하기위해 눌럿을경우
                                    progressDialog.setMessage("수정중입니다...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    delete_item(infoDelete, "NicName_ProfileImages");
                                }
                                else{ //사진말고 다른것들만 바꿀경우
                                    textUploadNic(userUid);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{ //실명
                    if(imagePath != null){ //제대로 사진하나를 수정하기위해 눌럿을경우
                        progressDialog.setMessage("수정중입니다...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        delete_item(infoDelete, "AppUser_ProfileImages");
                    }
                    else{
                        textUploadRealName(userUid);
                    }

                }
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
                myinfo_profile_Thumbnail.setImageURI(Uri.fromFile(f));
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

    public void delete_item(final String storageKey, final String storagePath){
        if(!storageKey.equals("None")){
            firebaseStorage.getReference().child(storagePath).child(storageKey).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(final Void aVoid) {
                    upload(imagePath, storagePath);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull final Exception e) {
                    upload(imagePath, storagePath);
                }
            });
        }
        else{
            upload(imagePath, storagePath);
        }
    }

    private void textUploadNic(String userUid){
        Map<String, Object> map = new HashMap<>();
        String resume = myinfo_user_resume.getText().toString();
        //firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).child("resume").setValue(resume);
        boolean pushOnOff = myinfo_user_pushswitch.isChecked();
        //firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).child("pushAlarmOnOff").setValue(pushOnOff);
        String nicName = myinfo_user_name.getText().toString();
        //firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).child("name").setValue(nicName);
        map.put("resume",resume);
        map.put("pushAlarmOnOff",pushOnOff);
        map.put("name",nicName);
        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).updateChildren(map);
        finish();
    }
    private void textUploadRealName(String userUid){
        Map<String, Object> map = new HashMap<>();
        String resume = myinfo_user_resume.getText().toString();
        //firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).child("resume").setValue(resume);
        boolean pushOnOff = myinfo_user_pushswitch.isChecked();
        //firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).child("pushAlarmOnOff").setValue(pushOnOff);
        map.put("resume",resume);
        map.put("pushAlarmOnOff",pushOnOff);
        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("User").child(userUid).updateChildren(map);
        finish();
    }

    private void upload(String uri, String storagePath){

        final String userUid = firebaseAuth.getCurrentUser().getUid();
        try{
            StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            final Uri file = Uri.fromFile(new File(uri));
            StorageReference riversRef = storageRef.child(storagePath+"/"+file.getLastPathSegment());
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
                    if(!isRealName){ //닉네임
                        firebaseDatabase.getReference().child("EveryClub").child(clubName)
                                .child("User").child(userUid)
                                .updateChildren(map);
                        textUploadNic(userUid);
                    }
                    else{// 실명
                        firebaseDatabase.getReference().child("AppUser").child(userUid).updateChildren(map);
                        textUploadRealName(userUid);
                    }

                    progressDialog.dismiss();
                    finish();
                }
            });
        }catch (NullPointerException e){
            Map<String, Object> map = new HashMap<>();
            map.put("realNameProPicDeleteName","None");
            map.put("realNameProPicUrl","None");
            if(!isRealName){ //닉네임
                firebaseDatabase.getReference().child("EveryClub").child(clubName)
                        .child("User").child(userUid)
                        .updateChildren(map);
                textUploadNic(userUid);
            }
            else{// 실명
                firebaseDatabase.getReference().child("AppUser").child(userUid).updateChildren(map);
                textUploadRealName(userUid);
            }
            progressDialog.dismiss();
            finish();
        }
    }


}
