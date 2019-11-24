package com.example.target_club_in_donga.MemberList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.club_foundation_join.JoinData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.thisClubIsRealName;

public class MemberList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ArrayList<JoinData> loginDataArrayList = new ArrayList<>();
    private List<String> dbey = new ArrayList<>();
    private int rank, myRank;
    private EditText search_name;
    private TextView mem_txt;
    private ImageButton back, search;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        recyclerView = findViewById(R.id.memberlist_recy);
        final MemberList_Recy memberList_recy = new MemberList_Recy();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        search_name = findViewById(R.id.search_member_name);
        back = findViewById(R.id.memberlist_back);
        search = findViewById(R.id.memberlist_search);
        mem_txt =findViewById(R.id.mem_txt);

        if ( thisClubIsRealName == true ){ // 실명제 동아리
            database.getReference().child("EveryClub").child(clubName).child("User").orderByChild("admin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    loginDataArrayList.clear(); // 동아리 user
                    dbey.clear(); // user keys
                    for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final JoinData groupData = snapshot.getValue(JoinData.class); // 동아리 유저정보 컨테이너 가져오고
                        final String key = snapshot.getKey(); // 키값 가져오고
                        if(key.equals(auth.getCurrentUser().getUid())){ // 지금 접속중인 유저 키찾아서 권한가져오고
                            myRank = groupData.getAdmin();
                        }
                        dbey.add(key);
                        database.getReference().child("AppUser").child(key).addValueEventListener(new ValueEventListener() { // 가져온 키로 디비검색
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                JoinData appuserdata = dataSnapshot.getValue(JoinData.class);
                                rank = groupData.getAdmin();
                                appuserdata.setAdmin(rank);
                                loginDataArrayList.add(appuserdata); // 이름, 프사, 폰번호 가져올것
                                if (key.equals(dbey.get(dbey.size()-1))){
                                    memberList_recy.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else if( thisClubIsRealName == false){ // 별명제
            database.getReference().child("EveryClub").child(clubName).child("User").orderByChild("admin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    loginDataArrayList.clear();
                    dbey.clear();
                    for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        JoinData loginData = snapshot.getValue(JoinData.class); // 동아리 유저정보 컨테이너 가져오고
                        String key = snapshot.getKey(); // 키값 가져오고
                        loginDataArrayList.add(loginData); // 유저정보 넣고
                        if(key.equals(auth.getCurrentUser().getUid())){ // 지금 접속중인 유저 키찾아서 권한가져오고
                            myRank = loginData.getAdmin();
                        }
                        dbey.add(key);
                    }
                    memberList_recy.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mem_txt.setVisibility(View.GONE);
                search_name.setVisibility(View.VISIBLE);
            }
        });
        recyclerView.setAdapter(memberList_recy);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
    }

    class MemberList_Recy extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memberlist, parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //
            ((CustomViewHolder)holder).name.setText(loginDataArrayList.get(position).getName());
            rank = loginDataArrayList.get(position).getAdmin();
            if( rank == 3){
                ((CustomViewHolder)holder).postion.setText("회원");
            }
            else if( rank == 2 ){
                ((CustomViewHolder)holder).postion.setText("임원");
            }
            else if( rank == 1 ){
                ((CustomViewHolder)holder).postion.setText("부회장");
            }
            else if( rank == 0 ){
                ((CustomViewHolder)holder).postion.setText("회장");
            }
            else if( rank < 0 ){
                ((CustomViewHolder)holder).postion.setText("Admin");
            }
            ((CustomViewHolder)holder).phone.setText(loginDataArrayList.get(position).getPhone());
            Glide.with(holder.itemView.getContext()).load(loginDataArrayList.get(position).getRealNameProPicUrl()).override(60,60).into(((CustomViewHolder)holder).imageView);
            PopupMenu(holder,position, rank);
        }

        @Override
        public int getItemCount() {
            return loginDataArrayList.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView name, phone;
            Button postion;
            ImageView imageView;
            ImageButton posi_btn;
            LinearLayout layout;
            public CustomViewHolder(View view){
                super(view);
                name = view.findViewById(R.id.memberlist_name);
                postion = view.findViewById(R.id.memberlist_position);
                phone = view.findViewById(R.id.memberlist_phone);
                imageView = view.findViewById(R.id.memberlist_imgview);
                layout = view.findViewById(R.id.memberlist_layout);
                posi_btn = view.findViewById(R.id.posi_btn);
            }
        }
    }
    public void PopupMenu(final RecyclerView.ViewHolder holder, final int position, final int rank){
        ((MemberList_Recy.CustomViewHolder)holder).posi_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //delete_item(position);
                PopupMenu popup = new PopupMenu(view.getContext(), view);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //int x = item.getItemId();
                        switch (item.getItemId()){
                            case R.id.member_president:
                                alert("회장 위임",loginDataArrayList.get(position).getName()+" 에게 '회장'을 위임하겠습니까? \n직전 회장은 '회원'으로 바뀌게됩니다."
                                        , 0, position, "회장");
                                return true;
                            case R.id.member_vicePresident:
                                alert("부회장 변경",loginDataArrayList.get(position).getName()+" 에게 '부회장'을 부여하겠습니까?"
                                        , 1, position, "부회장");
                                return true;
                            case R.id.member_executives:
                                setAdmin(2, position);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+" 이(가) '임원'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.member_member:
                                setAdmin(3, position);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+" 이(가) '회원'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.member_kickOut:
                                alert("모임 추방", loginDataArrayList.get(position).getName()+"을 정말로 '추방'하시겠습니까?"
                                        , 4, position, "추방");
                                return true;

                            default:
                                return false;
                        }
                        //return false;
                    }
                });
                popup.inflate(R.menu.memberlist_popup);

                if(myRank >= 2){
                    allVisible(popup);
                }
                else if(myRank == 1){
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(4).setVisible(false);
                    popup.getMenu().getItem(rank).setVisible(false);
                }
                else{
                    if(rank != 0){
                        popup.getMenu().getItem(rank).setVisible(false);
                    }
                }

                if(dbey.get(position).equals(auth.getCurrentUser().getUid())){
                    allVisible(popup);
                }
                if(myRank >= rank){
                    allVisible(popup);
                }
                popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                popup.show();
            }
        });
    }
    public void allVisible(PopupMenu popup){
        popup.getMenu().getItem(0).setVisible(false);
        popup.getMenu().getItem(1).setVisible(false);
        popup.getMenu().getItem(2).setVisible(false);
        popup.getMenu().getItem(3).setVisible(false);
        popup.getMenu().getItem(4).setVisible(false);
    }
    public void setAdmin(int valueNum, int position){
        FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("User").child(dbey.get(position)).child("admin").setValue(valueNum);
    }
    public void alert(String title, String message, final int type, final int position, final String check){


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText edittext = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(100,30,30,100);
        edittext.setLayoutParams(lp);

        edittext.setHint("동의한다면 '"+check+"' 이라고 써주세요!");
        builder.setView(edittext);
        builder.setMessage(message);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(edittext.getText().toString().equals(check)){
                            if(type == 0){
                                FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("User").child(auth.getCurrentUser().getUid()).child("admin").setValue(3);
                                //FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("User").child(dbey.get(position)).child("admin").setValue(0);
                                setAdmin(0, position);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 '회장'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            else if(type == 1){
                                //FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("User").child(dbey.get(position)).child("admin").setValue(1);
                                setAdmin(1, position);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 '부회장'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("User").child(dbey.get(position)).removeValue();
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 모임에서 '추방' 되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MemberList.this, "다시 작성해주세요~", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                    }
                });
        builder.show();

    }
}