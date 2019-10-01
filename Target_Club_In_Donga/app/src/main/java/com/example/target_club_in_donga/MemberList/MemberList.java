package com.example.target_club_in_donga.MemberList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
import com.example.target_club_in_donga.Package_LogIn.LoginData;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Execute;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.example.target_club_in_donga.Vote.VoteActivity_Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class MemberList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ArrayList<LoginData> loginDataArrayList = new ArrayList<>();
    private List<String> dbey = new ArrayList<>();
    private int rank, myRank;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        recyclerView = findViewById(R.id.memberlist_recy);
        final MemberList_Recy memberList_recy = new MemberList_Recy();
        //final MemberList_Recy memberList_recy = new MemberList_Recy(memberlist_click);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child(clubName).child("User").orderByChild("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loginDataArrayList.clear();
                dbey.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LoginData loginData = snapshot.getValue(LoginData.class);
                    loginDataArrayList.add(loginData);
                    if(snapshot.getKey().equals(auth.getCurrentUser().getUid())){
                        myRank = loginData.getAdmin();
                    }
                    dbey.add(snapshot.getKey());
                }
                memberList_recy.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                ((CustomViewHolder)holder).postion.setText("Master");
            }
            ((CustomViewHolder)holder).phone.setText(loginDataArrayList.get(position).getPhone());
            PopupMenu(holder,position, rank);
        }

        @Override
        public int getItemCount() {
            return loginDataArrayList.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView name, postion, phone;
            ImageView imageView;
            ConstraintLayout layout;
            public CustomViewHolder(View view){
                super(view);
                name = view.findViewById(R.id.memberlist_name);
                postion = view.findViewById(R.id.memberlist_position);
                phone = view.findViewById(R.id.memberlist_phone);
                imageView = view.findViewById(R.id.memberlist_imgview);
                layout = view.findViewById(R.id.memberlist_layout);
            }
        }
    }
    public void PopupMenu(final RecyclerView.ViewHolder holder, final int position, final int rank){
        ((MemberList_Recy.CustomViewHolder)holder).layout.setOnClickListener(new View.OnClickListener() {

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
                                alert("회장 위임",loginDataArrayList.get(position).getName()+"에게 '회장'을 위임하겠습니까? \n직전 회장은 '회원'으로 바뀌게됩니다."
                                        , 0, position, "회장");
                                return true;
                            case R.id.member_vicePresident:
                                alert("부회장 변경",loginDataArrayList.get(position).getName()+"에게 '부회장'을 부여하겠습니까?"
                                        , 1, position, "부회장");
                                return true;
                            case R.id.member_executives:
                                alert("임원 변경",loginDataArrayList.get(position).getName()+"에게 '임원'을 부여하겠습니까?"
                                        , 2, position, "임원");
                                return true;
                            case R.id.member_member:
                                alert("회원 변경",loginDataArrayList.get(position).getName()+"에게 '회원'을 부여하겠습니까?"
                                        , 3, position, "회원");
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
                                FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(dbey.get(position)).child("admin").setValue(0);
                                FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(auth.getCurrentUser().getUid()).child("admin").setValue(3);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 '회장'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            else if(type == 1){
                                FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(dbey.get(position)).child("admin").setValue(1);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 '부회장'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            else if(type == 2){
                                FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(dbey.get(position)).child("admin").setValue(2);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 '임원'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            else if(type == 3){
                                FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(dbey.get(position)).child("admin").setValue(3);
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 '회원'으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(dbey.get(position)).removeValue();
                                Toast.makeText(MemberList.this, loginDataArrayList.get(position).getName()+"이 모임에서 '추방' 되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MemberList.this, "다시 작성해주세요~", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
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
