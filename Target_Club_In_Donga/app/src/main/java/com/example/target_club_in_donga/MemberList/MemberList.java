package com.example.target_club_in_donga.MemberList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.target_club_in_donga.Package_LogIn.LoginData;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class MemberList extends AppCompatActivity {
    ImageButton backbtn;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ArrayList<LoginData> loginDataArrayList = new ArrayList<>();
    private int admin;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        backbtn = findViewById(R.id.memberlist_back);
        recyclerView = findViewById(R.id.memberlist_recy);
        final MemberList_Recy memberList_recy = new MemberList_Recy();
        //final MemberList_Recy memberList_recy = new MemberList_Recy(memberlist_click);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child(clubName).child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loginDataArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LoginData loginData = snapshot.getValue(LoginData.class);
                    loginDataArrayList.add(loginData);
                }
                memberList_recy.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setAdapter(memberList_recy);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
    }
    /*public View.OnClickListener memberlist_click = new View.OnClickListener() { // 아이템 클릭 이벤트
        @Override
        public void onClick(View v) {
            final int position = recyclerView.getChildPosition(v);
            FirebaseUser user = auth.getCurrentUser();
            String currentuseradmin = user.getUid();
            Toast.makeText(MemberList.this, loginDataArrayList.get(position).getAdmin(), Toast.LENGTH_SHORT).show();
            if( currentuseradmin == database.getReference().child("User").getKey() ){

            }
            if( admin > 0 ){ // 현재 접속자가 임원인지 분별해줘야한다. 그럼 auth에 currentUid 를 가져와서 User DB에서 검색을 한다음 admin등급이 몇인지 확인해줘야한다.

            }
        }
    };*/

    class MemberList_Recy extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        /*View.OnClickListener memberlist_click;
        public MemberList_Recy(View.OnClickListener memberlist_click){
            this.memberlist_click = memberlist_click;
        }*/
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memberlist, parent,false);
            //view.setOnClickListener(memberlist_click);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).name.setText(loginDataArrayList.get(position).getName());
            admin = loginDataArrayList.get(position).getAdmin();
            if( admin == 0 ){
                ((CustomViewHolder)holder).postion.setText("일반 회원");
            }
            else if( admin == 1 ){
                ((CustomViewHolder)holder).postion.setText("회장");
            }
            else if( admin > 1 ){
                ((CustomViewHolder)holder).postion.setText("임원");
            }
            ((CustomViewHolder)holder).phone.setText(loginDataArrayList.get(position).getPhone());
        }

        @Override
        public int getItemCount() {
            return loginDataArrayList.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView name, postion, phone;
            ImageView imageView;
            public CustomViewHolder(View view){
                super(view);
                name = view.findViewById(R.id.memberlist_name);
                postion = view.findViewById(R.id.memberlist_position);
                phone = view.findViewById(R.id.memberlist_phone);
                imageView = view.findViewById(R.id.memberlist_imgview);
            }
        }
    }
}
