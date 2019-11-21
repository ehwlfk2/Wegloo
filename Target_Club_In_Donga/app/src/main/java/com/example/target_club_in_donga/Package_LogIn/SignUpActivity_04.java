package com.example.target_club_in_donga.Package_LogIn;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.target_club_in_donga.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.regex.Pattern;

public class SignUpActivity_04 extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 10;
    private ImageView activity_signup_04_realNameProfile;
    private EditText activity_signup_04_name, activity_signup_04_phoneNumber;
    private Button activity_signup_04_nextBtn;
    private ImageButton activity_signup_04_cancelBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private String realNameProfileIamgePath;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_04_ver0);

        /*권한*/
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        activity_signup_04_realNameProfile = findViewById(R.id.signup_04_button_picture);
        activity_signup_04_name = findViewById(R.id.signup_04_edittext_name);
        activity_signup_04_phoneNumber = findViewById(R.id.signup_04_edittext_phonenumber);
        activity_signup_04_nextBtn = findViewById(R.id.signup_04_button_next);
        activity_signup_04_cancelBtn = findViewById(R.id.signup_04_button_back);

        /*activity_signup_04_realNameProfile.setBackground(new ShapeDrawable(new OvalShape())); //이미지뷰 동그랗게
        if(Build.VERSION.SDK_INT >= 21) {
            activity_signup_04_realNameProfile.setClipToOutline(true);
        }*/

        activity_signup_04_nextBtn.setOnClickListener(this);
        activity_signup_04_cancelBtn.setOnClickListener(this);
        activity_signup_04_realNameProfile.setOnClickListener(this);
        /*final Button btn_tcid = findViewById(R.id.btn_tcid);
        btn_tcid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clubName = btn_tcid.getText().toString();
                dbdb();
                Intent intent = new Intent(SignUpActivity_04.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final Button btn_ka = findViewById(R.id.btn_ka);
        btn_ka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clubName = btn_ka.getText().toString();
                dbdb();
                Intent intent = new Intent(SignUpActivity_04.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

    }   // onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_04_button_next:
                String name = activity_signup_04_name.getText().toString();
                String phoneNumber = activity_signup_04_phoneNumber.getText().toString();
                if(!Pattern.matches("^[가-힣]{2,4}|[a-zA-Z]{2,20}$", name)){ //이름거르기
                    Toast.makeText(this, "이름 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!Pattern.matches("(010|011|016|017|018|019)(.+)(.{4})", phoneNumber)) { //번호거르기
                    Toast.makeText(this, "전화번호 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else{

                    insertDB(realNameProfileIamgePath, name, phoneNumber);
                }
                break;
            case R.id.signup_04_button_picture:
                /**
                 * 현재 뷰에는 원형으로 보이지만 결국 들어가는건 원본파일이 들어감
                 * Crop 활용해서 자른다음 올려줘야할듯 추후 추가예정
                 * https://github.com/igreenwood/SimpleCropView#cropmode
                 */
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE);
                break;

            case R.id.signup_04_button_back:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            try {
                realNameProfileIamgePath = getPath(data.getData());
                File f = new File(realNameProfileIamgePath);
                activity_signup_04_realNameProfile.setImageURI(Uri.fromFile(f));
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

    private void insertDB(String uri, final String name, final String phoneNumber){
        progressDialog.setMessage("회원가입 중입니다...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String uid = firebaseAuth.getCurrentUser().getUid();
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

                    AppLoginData itemDTO = new AppLoginData();

                    itemDTO.setRealNameProPicUrl(downloadUrl.toString());
                    itemDTO.setRealNameProPicDeleteName(file.getLastPathSegment());
                    itemDTO.setName(name);
                    itemDTO.setPhone(phoneNumber);
                    itemDTO.setJoinedTimestamp(System.currentTimeMillis());
                    itemDTO.setEmailLoginEmail(firebaseAuth.getCurrentUser().getEmail());

                    firebaseDatabase.getReference().child("AppUser").child(uid).setValue(itemDTO);
                    progressDialog.dismiss();
                    Intent intent = new Intent(SignUpActivity_04.this,Congratulation.class);
                    startActivity(intent);
                    finish();
                }
            });
        }catch (NullPointerException e){ //프로필 안햇을경우
            AppLoginData itemDTO = new AppLoginData();
            itemDTO.setRealNameProPicUrl("None");
            itemDTO.setRealNameProPicDeleteName("None");
            itemDTO.setName(name);
            itemDTO.setPhone(phoneNumber);
            itemDTO.setJoinedTimestamp(System.currentTimeMillis());
            itemDTO.setEmailLoginEmail(firebaseAuth.getCurrentUser().getEmail());

            firebaseDatabase.getReference().child("AppUser").child(uid).setValue(itemDTO);
            progressDialog.dismiss();
            Intent intent = new Intent(SignUpActivity_04.this,Congratulation.class);
            startActivity(intent);
            finish();
        }

    }
    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        finish();
        super.onBackPressed();
    }
    /*
    public void passPushTokenToServer() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);
        FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("User").child(uid).updateChildren(map);
    }*/
}   // SignUpActivity_04
