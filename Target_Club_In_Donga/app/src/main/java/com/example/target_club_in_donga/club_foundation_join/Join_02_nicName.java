package com.example.target_club_in_donga.club_foundation_join;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
import com.example.target_club_in_donga.home_viewpager.MyClubSeletedItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class Join_02_nicName extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 10;
    private ImageButton join_02_nicName_04_button_back;
    private Button join_02_nicName_button_next;
    private ImageView join_02_nicName_button_picture;
    private EditText join_02_nicName_edittext_nicname;
    private boolean isFoundation;
    private String imagePath;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_02_nic_name);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        /*권한*/
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

        join_02_nicName_04_button_back = findViewById(R.id.join_02_nicName_04_button_back);
        join_02_nicName_button_next = findViewById(R.id.join_02_nicName_button_next);
        join_02_nicName_button_picture = findViewById(R.id.join_02_nicName_button_picture);
        join_02_nicName_edittext_nicname = findViewById(R.id.join_02_nicName_edittext_nicname);

        join_02_nicName_04_button_back.setOnClickListener(this);
        join_02_nicName_button_picture.setOnClickListener(this);
        join_02_nicName_button_next.setOnClickListener(this);
        Intent intent2 = getIntent();
        isFoundation = intent2.getExtras().getBoolean("isFoundation");
        /**
         * isFoundation false경우
         * resume ,isFreeSign 받아야함
         */
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.join_02_nicName_04_button_back:
                finish();
                break;
            case R.id.join_02_nicName_button_picture:

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE);
                break;
            case R.id.join_02_nicName_button_next:
                if(join_02_nicName_edittext_nicname.getText().length() > 0){
                    insertDB(imagePath);
                }
                else{
                    Toast.makeText(this, "닉네임을 1자 이상 입력해야합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            try {
                imagePath = getPath(data.getData());
                File f = new File(imagePath);
                join_02_nicName_button_picture.setImageURI(Uri.fromFile(f));
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
    private void insertDB(String uri){
        progressDialog.setMessage("로딩중입니다...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try{
            StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

            final Uri file = Uri.fromFile(new File(uri));
            StorageReference riversRef = storageRef.child("NicName_ProfileImages/"+file.getLastPathSegment());
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
                    insertDB2(downloadUrl.toString(),file.getLastPathSegment());
                }
            });
        }catch (NullPointerException e){ //프로필 안햇을경우
            insertDB2("None","None");
        }
    }
    private void insertDB2(final String downloadUrl,final String filePath){
        final String userUid = firebaseAuth.getCurrentUser().getUid();
        final JoinData joinData = new JoinData();
        /**
         * 회장 프로필
         */
        if(isFoundation){
            Intent intent2 = getIntent();
            final String thisClubName = intent2.getExtras().getString("thisClubName");
            String clubIntroduce = intent2.getExtras().getString("clubIntroduce");
            final String clubDownloadUrl = intent2.getExtras().getString("clubDownloadUrl");
            String clubFilePath = intent2.getExtras().getString("clubFilePath");
            boolean clubFreeSign = intent2.getExtras().getBoolean("clubFreeSign");

            final ClubData clubData = new ClubData();
            clubData.setThisClubName(thisClubName);
            clubData.setClubIntroduce(clubIntroduce);
            clubData.setClubCreateTimestamp(System.currentTimeMillis());
            clubData.setRealNameSystem(false);
            clubData.setFreeSign(clubFreeSign);
            clubData.setClubImageUrl(clubDownloadUrl);
            clubData.setClubImageDeleteName(clubFilePath);

            firebaseDatabase.getReference().child("EveryClub").push().setValue(clubData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    String clubUid = databaseReference.getKey();
                    joinData.setName(join_02_nicName_edittext_nicname.getText().toString());
                    joinData.setAdmin(0);
                    joinData.setPushAlarmOnOff(true);
                    joinData.setRealNameProPicUrl(downloadUrl);
                    joinData.setRealNameProPicDeleteName(filePath);
                    joinData.setApplicationDate(-1*System.currentTimeMillis());
                    joinData.setPushToken(FirebaseInstanceId.getInstance().getToken());

                    firebaseDatabase.getReference().child("EveryClub").child(clubUid).child("User").child(userUid).setValue(joinData);
                    clubName = clubUid;

                    firebaseDatabase.getReference().child("AppUser").child(userUid).child("recentClub").setValue(clubUid);
                    MyClubSeletedItem myClubSeletedItem = new MyClubSeletedItem();
                    myClubSeletedItem.setApprovalCompleted(true);
                    myClubSeletedItem.setSignUpclubProfile(clubDownloadUrl);
                    myClubSeletedItem.setSignUpclubUid(clubUid);
                    myClubSeletedItem.setSignUpclubName(thisClubName);
                    firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").push().setValue(myClubSeletedItem);

                    progressDialog.dismiss();
                    Intent intent = new Intent(Join_02_nicName.this, HomeActivityView.class);
                    intent.putExtra("isRecent",true);
                    startActivity(intent);
                    finish();
                }
            });





//            firebaseDatabase.getReference().child("AppUser").child(userUid).child("recentClub").setValue(foundationUid);
//
//            MyClubSeletedItem myClubSeletedItem = new MyClubSeletedItem();
//            myClubSeletedItem.setApprovalCompleted(true);
//            myClubSeletedItem.setSignUpclubProfile(foundationUrl);
//            myClubSeletedItem.setSignUpclubUid(foundationUid);
//            myClubSeletedItem.setSignUpclubName(foundationName);
//
//            firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").push().setValue(myClubSeletedItem);

//            progressDialog.dismiss();
//            Intent intent = new Intent(Join_02_nicName.this, HomeActivityView.class);
//            intent.putExtra("isRecent",true);
//            startActivity(intent);
//            finish();
        }
        /**
         * 일반 가입 프로필
         */
        else{
            Intent intent2 = getIntent();
            String clubUid = intent2.getExtras().getString("clubUid");
            String resume = intent2.getExtras().getString("resume");
            boolean isFreeSign = intent2.getExtras().getBoolean("isFreeSign");
            String thisClubName = intent2.getExtras().getString("thisClubName");
            String clubProfileUrl = intent2.getExtras().getString("clubProfileUrl");

            joinData.setName(join_02_nicName_edittext_nicname.getText().toString());
            joinData.setAdmin(3);
            joinData.setPushAlarmOnOff(true);
            joinData.setRealNameProPicUrl(downloadUrl);
            joinData.setRealNameProPicDeleteName(filePath);
            joinData.setApplicationDate(-1*System.currentTimeMillis());
            joinData.setPushToken(FirebaseInstanceId.getInstance().getToken());
            joinData.setResume(resume);
            if(isFreeSign){
                firebaseDatabase.getReference().child("EveryClub").child(clubUid).child("User").child(userUid).setValue(joinData);
                firebaseDatabase.getReference().child("AppUser").child(userUid).child("recentClub").setValue(clubUid);
                MyClubSeletedItem myClubSeletedItem = new MyClubSeletedItem();
                myClubSeletedItem.setApprovalCompleted(true);
                myClubSeletedItem.setSignUpclubProfile(clubProfileUrl);
                myClubSeletedItem.setSignUpclubUid(clubUid);
                myClubSeletedItem.setSignUpclubName(thisClubName);
                firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").push().setValue(myClubSeletedItem);

                clubName = clubUid;
                progressDialog.dismiss();
                Intent intent = new Intent(Join_02_nicName.this, HomeActivityView.class);
                intent.putExtra("isRecent",true);
                startActivity(intent);
                finish();
            }
            else{
                firebaseDatabase.getReference().child("EveryClub").child(clubUid).child("WantToJoinUser").child(userUid).setValue(joinData);
                MyClubSeletedItem myClubSeletedItem = new MyClubSeletedItem();
                myClubSeletedItem.setApprovalCompleted(false);
                myClubSeletedItem.setSignUpclubProfile(clubProfileUrl);
                myClubSeletedItem.setSignUpclubUid(clubUid);
                myClubSeletedItem.setSignUpclubName(thisClubName);
                firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").push().setValue(myClubSeletedItem);
                progressDialog.dismiss();
                Intent intent = new Intent(Join_02_nicName.this, HomeActivityView.class);
                intent.putExtra("isRecent",false);
                startActivity(intent);
                finish();
            }
        }
    }
}
