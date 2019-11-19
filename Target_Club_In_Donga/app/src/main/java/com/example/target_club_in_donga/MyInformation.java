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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.Attend.Attend_Information_Item;
/*import com.example.target_club_in_donga.Material_Rental.MaterialRentalActivity_Admin_Insert;
import com.example.target_club_in_donga.Material_Rental.MaterialRental_Item;
import com.example.target_club_in_donga.Package_LogIn.LoginData;*/
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import static com.example.target_club_in_donga.MainActivity.clubName;

public class MyInformation extends AppCompatActivity {
    private RecyclerView activity_user_detail_recyclerview_main_list;
    List<Attend_Information_Item> attendAdminItems = new ArrayList<>();
    List<MyInformation_Item> myInformationItems = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();

    private ArrayList<String> listStartTime = new ArrayList<>();

    private ImageButton btn_back;
    private TextView name, phone, school, email, studentID, circles, position, slidingdrawer_title;
    private ImageView profile;
    private SlidingDrawer slidingDrawer;
    private int menu_count = 0, listSize = 0, count = 0;
    private String clubName = "TCID", startTime, imagePath, material_path;

    private Button Edit_myinfo;

    private LinearLayout myinfo_showMyAttend;

    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private static final int IMAGE_PICK_CODE = 1000; // 갤러리에서 이미지를 받아오기 위한 세가지 변수
    private static final int PERMISSION_CODE = 1001; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfomation);
        btn_back = findViewById(R.id.myinfo_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        name = findViewById(R.id.myinfo_user_name);
        phone = findViewById(R.id.myinfo_user_PhoneNumber);
        //school = view.findViewById(R.id.user_detail_school);
        email = findViewById(R.id.myinfo_user_email);
        profile = findViewById(R.id.myinfo_profile_Thumbnail);
        circles = findViewById(R.id.myinfo_user_circles);
        position = findViewById(R.id.myinfo_user_position);
        //studentID = view.findViewById(R.id.user_detail_studentID);
        //changeButton = view.findViewById(R.id.user_detail_change);
        myinfo_showMyAttend = findViewById(R.id.myinfo_showMyAttend);
        Edit_myinfo = findViewById(R.id.Edit_myinfo);

        if (count > 0) {
            Toast.makeText(MyInformation.this, "프로필이 추가되었습니다", Toast.LENGTH_SHORT).show();
            upload(imagePath);
            finish();
        }

        Glide.with(this).load(myInformationItems.get(0).imageUri).into(profile);

        profile.setOnClickListener(new View.OnClickListener() {
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

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        slidingDrawer = (SlidingDrawer) findViewById(R.id.activity_user_detail_slidingdrawer);
        slidingdrawer_title = (TextView) findViewById(R.id.activity_user_detail_slidingdrawer_title);

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slidingdrawer_title.setVisibility(View.VISIBLE);
                everyBtnEnable(false);
                menu_count++;
            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slidingdrawer_title.setVisibility(View.VISIBLE);
                everyBtnEnable(true);
                menu_count--;
            }
        });

        myinfo_showMyAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                slidingDrawer.animateOpen();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });

        circles.setText(clubName);

        database.getReference().child("EveryClub").child(clubName).child("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                int checkPosition = Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                if (checkPosition == -1) {
                    position.setText("관리자");
                } else if (checkPosition == 0) {
                    position.setText("회장");
                } else if (checkPosition == 1) {
                    position.setText("부회장");
                } else if (checkPosition == 2) {
                    position.setText("임원");
                } else if (checkPosition == 3) {
                    position.setText("회원");
                }

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

/*        database.getReference().child("EveryClub").child(clubName).child("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LoginData loginData = dataSnapshot.getValue(LoginData.class);
                name.setText(loginData.getName());
                email.setText(auth.getCurrentUser().getEmail());
                phone.setText(loginData.getPhone());
                //school.setText(loginData.getSchool());
                //studentID.setText(loginData.getStudentNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

/*        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 클릭하면 내 정보를 변경 할 수 있는 layout 이동 (아직 미구현)
            }
        });*/

        activity_user_detail_recyclerview_main_list = (RecyclerView) findViewById(R.id.activity_user_detail_recyclerview_main_list);
        activity_user_detail_recyclerview_main_list.setLayoutManager(new LinearLayoutManager(MyInformation.this));

        final MyInformation.MyInformationActivity_AdminRecyclerViewAdapter MyInformationActivity_adminRecyclerViewAdapter = new MyInformation.MyInformationActivity_AdminRecyclerViewAdapter();

        activity_user_detail_recyclerview_main_list.setAdapter(MyInformationActivity_adminRecyclerViewAdapter);
        MyInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();

        database.getReference().child("EveryClub").child(clubName).child("Attend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                attendAdminItems.clear();
                uidLists.clear();
                listSize = 0;
                for (final DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                    database.getReference().child("EveryClub").child(clubName).child("Attend").child(snapshot2.getKey()).child("User_State").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getKey().equals(auth.getCurrentUser().getUid())) {
                                    startTime = snapshot2.child("startTime").getValue().toString();
                                    listStartTime.add(startTime);
                                    Attend_Information_Item attendAdminInformationItem = snapshot.getValue(Attend_Information_Item.class);
                                    String uidKey = snapshot.getKey();
                                    attendAdminItems.add(0, attendAdminInformationItem);
                                    uidLists.add(0, uidKey);
                                    listSize++;

                                    for (int i = 0; i < listSize; i++) {
                                        attendAdminItems.get(i).attendTimeLimit = listStartTime.get(listSize - 1 - i);
                                    }

                                    // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                                    MyInformationActivity_adminRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(final DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

/*        // 홈에서 메뉴(슬라이딩드로우)를 열었을 경우에만 뒤로가기 버튼을 누르면 슬라이딩드로우가 닫힘
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(final View view, final int i, final KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && menu_count > 0) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("finishstatus", true);
                    slidingDrawer.animateClose();
                    everyBtnEnable(true);
//                    getActivity().finish();
//                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });*/

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
            profile.setImageURI(Uri.fromFile(f));
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

        StorageReference storageRef = storage.getReferenceFromUrl("gs://target-club-in-donga.appspot.com");

        final Uri file = Uri.fromFile(new File(uri));
        StorageReference riversRef = storageRef.child("EveryClub").child(clubName).child("MyInformation/").child(auth.getCurrentUser().getUid());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();

                MyInformation_Item imageProfile = new MyInformation_Item();
                imageProfile.imageUri = downloadUri.toString();
                imageProfile.imageName = file.getLastPathSegment();

                database.getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).setValue(imageProfile);
            }
        });
    }






    public void everyBtnEnable(boolean boo) {
        name.setEnabled(boo);
        email.setEnabled(boo);
        phone.setEnabled(boo);
        profile.setEnabled(boo);
//        changeButton.setEnabled(boo);
    }

    class MyInformationActivity_AdminRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            LinearLayout activity_attend_information_item_linearlayout;
            TextView activity_attend_information_item_textview_date;
            TextView activity_attend_information_item_textview_attend_state;

            public CustomViewHolder(View view) {
                super(view);

                activity_attend_information_item_linearlayout = (LinearLayout) view.findViewById(R.id.activity_attend_information_item_linearlayout);
                activity_attend_information_item_textview_date = (TextView) view.findViewById(R.id.activity_attend_information_item_textview_date);
                activity_attend_information_item_textview_attend_state = (TextView) view.findViewById(R.id.activity_attend_information_item_textview_attend_state);

            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_attend_information_item, viewGroup, false);

            return new MyInformation.MyInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
            final MyInformation.MyInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder customViewHolder = ((MyInformation.MyInformationActivity_AdminRecyclerViewAdapter.CustomViewHolder) viewholder);
            customViewHolder.activity_attend_information_item_textview_date.setGravity(Gravity.LEFT);

            customViewHolder.activity_attend_information_item_textview_attend_state.setText(attendAdminItems.get(position).attend_state);
            customViewHolder.activity_attend_information_item_textview_date.setText(attendAdminItems.get(position).attendTimeLimit);

        }

        @Override
        public int getItemCount() {
            return attendAdminItems.size();
        }

    }
}
