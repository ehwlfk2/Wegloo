package com.example.target_club_in_donga.club_foundation_join;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.target_club_in_donga.Package_LogIn.AppLoginData;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
import com.example.target_club_in_donga.home_viewpager.MyClubSeletedItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class Join_01 extends AppCompatActivity implements View.OnClickListener {
    private List<AutoCompleteItem> list = new ArrayList<>();
    private CustomAutoCompleteTextView joinclube_autoCompleteSearch;
    private ImageButton foundation_02_button_back;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private Button joinclube_button_next;
    private String clubUid = null;
    private EditText joinclube_edittext_content;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_01);
        joinclube_autoCompleteSearch = findViewById(R.id.joinclube_autoCompleteSearch);
        foundation_02_button_back = findViewById(R.id.joinclube_00_imagebutton_back);
        joinclube_button_next = findViewById(R.id.joinclube_button_next);
        joinclube_edittext_content = findViewById(R.id.joinclube_edittext_content);
        foundation_02_button_back.setOnClickListener(this);
        joinclube_button_next.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("EveryClubName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AutoCompleteDBItem autoCompleteDBItem = snapshot.getValue(AutoCompleteDBItem.class);

                    final AutoCompleteItem entry = new AutoCompleteItem();
                    entry.title = autoCompleteDBItem.clubTitle;
                    entry.clubUid = snapshot.getKey();
                    entry.imageUrl = autoCompleteDBItem.clubProfile;
                    if(!autoCompleteDBItem.clubProfile.equals("None")){
                        Glide.with(getApplicationContext()).asBitmap().load(autoCompleteDBItem.clubProfile)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        entry.image = resource;
                                        //할일

                                    }
                                });
                    }

                    //entry.imageUrl = autoCompleteDBItem.clubProfile;
                    list.add(entry);
                    //Toast.makeText(Join_01.this, ""+entry.image, Toast.LENGTH_SHORT).show();
                }
                SearchItemArrayAdapter adapter = new SearchItemArrayAdapter(Join_01.this, R.layout.search_listitem_icon, list);
                joinclube_autoCompleteSearch.setAdapter(adapter);
                //Toast.makeText(Join_01.this, ""+list.get(5).title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        joinclube_autoCompleteSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                if (item instanceof AutoCompleteItem) {
                    AutoCompleteItem autoCompleteItem = (AutoCompleteItem) item;
                    clubUid = autoCompleteItem.clubUid;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.joinclube_00_imagebutton_back:
                finish();
                break;
            case R.id.joinclube_button_next:
                if(clubUid == null){
                    Toast.makeText(this, "모임 선택 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{

                    String userUId = firebaseAuth.getCurrentUser().getUid();
                    firebaseDatabase.getReference().child("AppUser").child(userUId).child("signUpClub").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean alreadyJoin = false;
                            boolean checkJoin = false;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                MyClubSeletedItem myClubSeletedItem = snapshot.getValue(MyClubSeletedItem.class);
                                if(myClubSeletedItem.getSignUpclubUid().equals(clubUid)){
                                    alreadyJoin = true;
                                    checkJoin = myClubSeletedItem.isApprovalCompleted();
                                    break;
                                }
                            }
                            if(alreadyJoin && checkJoin){
                                Toast.makeText(Join_01.this, "이미 가입된 모임입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else if(alreadyJoin){
                                Toast.makeText(Join_01.this, "가입 요청중인 모임입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                joinClubDialog();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                break;
        }
    }

    public class SearchItemArrayAdapter extends ArrayAdapter<AutoCompleteItem> {
        private List<AutoCompleteItem> tempItems;
        private List<AutoCompleteItem> suggestions;
        private Context context;
        /**
         *
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public SearchItemArrayAdapter(Context context, int textViewResourceId, List<AutoCompleteItem> objects) {
            super(context, textViewResourceId, objects);
            suggestions = new ArrayList<>(objects);
            tempItems = new ArrayList<>(objects);
            this.context = context;
        }
        public class ViewHolder {
            TextView txtCustomer;
            ImageView ivCustomerImage;
            ImageView ivCustomerImageVis;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.search_listitem_icon, parent, false);
                holder.ivCustomerImage = convertView.findViewById(R.id.category_icon);
                holder.txtCustomer = convertView.findViewById(R.id.search_auto_item);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            AutoCompleteItem autoCompleteItem = getItem(position);
            holder.txtCustomer.setText(autoCompleteItem.title);


            if(!autoCompleteItem.imageUrl.equals("None")){
                //Glide.with(context).load(autoCompleteItem.imageUrl).into(holder.ivCustomerImage);
                holder.ivCustomerImage.setImageBitmap(autoCompleteItem.image);
            }
            else{
                //holder.ivCustomerImage.setVisibility(View.GONE);
                //holder.ivCustomerImageVis.setVisibility(View.VISIBLE);
                holder.ivCustomerImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
            }

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                AutoCompleteItem autoCompleteItem = (AutoCompleteItem) resultValue;
                return autoCompleteItem.title;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (AutoCompleteItem autoCompleteItem : tempItems) {
                        if (autoCompleteItem.title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(autoCompleteItem);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                }
                else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<AutoCompleteItem> filterList = (ArrayList<AutoCompleteItem>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (AutoCompleteItem autoCompleteItem : filterList) {
                        //item.clubUid = tempItems.get(i).clubUid;
                        add(autoCompleteItem);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }
    private void joinClubDialog(){

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        final View view2 = LayoutInflater.from(this).inflate(R.layout.dialog_foundation_02_createclub, null, false);
        builder2.setView(view2);
        final AlertDialog dialog2 = builder2.create();

        TextView dialog_foundation_02_notice = view2.findViewById(R.id.dialog_foundation_02_notice);
        dialog_foundation_02_notice.setVisibility(View.GONE);
        final TextView dialog_joinClub_text = view2.findViewById(R.id.dialog_foundation_02_text);
        final ImageView dialog_joinClub_button_picture = view2.findViewById(R.id.dialog_foundation_02_button_picture);
        final TextView dialog_joinClub_realName = view2.findViewById(R.id.dialog_foundation_02_realName);
        final TextView dialog_joinClub_freeSign = view2.findViewById(R.id.dialog_foundation_02_freeSign);
        final TextView dialog_joinClub_content = view2.findViewById(R.id.dialog_foundation_02_content);
        final Button dialog_joinClub_confirmBtn = view2.findViewById(R.id.dialog_foundation_02_confirmBtn);
        dialog_joinClub_confirmBtn.setText("가입하기");

        firebaseDatabase.getReference().child("EveryClub").child(clubUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ClubData clubData = dataSnapshot.getValue(ClubData.class);
                dialog_joinClub_text.setText(clubData.getThisClubName());
                dialog_joinClub_content.setText(clubData.getClubIntroduce());
                if(clubData.getClubImageUrl().equals("None")){
                    dialog_joinClub_button_picture.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                }
                else{
                    Glide.with(Join_01.this).load(clubData.getClubImageUrl()).into(dialog_joinClub_button_picture);
                }

                if(clubData.isRealNameSystem()){
                    dialog_joinClub_realName.setText("실명 모임");
                }
                else{
                    dialog_joinClub_realName.setText("닉네임 모임");
                }

                if(clubData.isFreeSign()){
                    dialog_joinClub_freeSign.setText("자유 가입");
                }
                else{
                    dialog_joinClub_freeSign.setText("승인 가입");
                }
                dialog_joinClub_confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();

                        final String userUid = firebaseAuth.getCurrentUser().getUid();
                        /**
                         * 그모임이 실명제이면!
                         */
                        if(clubData.isRealNameSystem()){
                            progressDialog.setMessage("가입중입니다...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            JoinData joinData = new JoinData();
                            joinData.setResume(joinclube_edittext_content.getText().toString());
                            joinData.setPushAlarmOnOff(true);
                            joinData.setAdmin(3);
                            joinData.setPushToken(FirebaseInstanceId.getInstance().getToken());
                            joinData.setApplicationDate(-1*System.currentTimeMillis()); //가입날짜 or 가입신청날짜

                            if(clubData.isFreeSign()){
                                //가입된거니까 바로넣어주고
                                firebaseDatabase.getReference().child("EveryClub").child(clubUid).child("User").child(userUid).setValue(joinData);
                                //로그인된 유저의 recentClub에 바로 이모임 넣어주고
                                firebaseDatabase.getReference().child("AppUser").child(userUid).child("recentClub").setValue(clubUid);

                                //가입된 클럽중에 가입완료된거니까 true
                                //firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").child(clubUid).setValue(true);
                                MyClubSeletedItem myClubSeletedItem = new MyClubSeletedItem();
                                myClubSeletedItem.setApprovalCompleted(true);
                                myClubSeletedItem.setSignUpclubUid(clubUid);
                                myClubSeletedItem.setSignUpclubName(clubData.getThisClubName());
                                myClubSeletedItem.setSignUpclubProfile(clubData.getClubImageUrl());
                                firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").child(clubUid).setValue(myClubSeletedItem);
                                clubName = clubUid;
                                progressDialog.dismiss();
                                Intent intent = new Intent(Join_01.this, HomeActivityView.class);
                                intent.putExtra("isRecent",true);
                                startActivity(intent);
                                /**
                                 * 그클럽 홈으로 intent
                                 */
                                finish();
                            }
                            else{
                                //가입 신청 바로하고
                                firebaseDatabase.getReference().child("EveryClub").child(clubUid).child("WantToJoinUser").child(userUid).setValue(joinData);

                                //가입된 클럽중에 가입완료아직 안된거니까 false
                                //firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").child(clubUid).setValue(false);
                                MyClubSeletedItem myClubSeletedItem = new MyClubSeletedItem();
                                myClubSeletedItem.setApprovalCompleted(false);
                                myClubSeletedItem.setSignUpclubUid(clubUid);
                                myClubSeletedItem.setSignUpclubName(clubData.getThisClubName());
                                myClubSeletedItem.setSignUpclubProfile(clubData.getClubImageUrl());
                                firebaseDatabase.getReference().child("AppUser").child(userUid).child("signUpClub").child(clubUid).setValue(myClubSeletedItem);
                                /**
                                 * 승인중 페이지로 intent
                                 */
                                progressDialog.dismiss();
                                Intent intent = new Intent(Join_01.this, HomeActivityView.class);
                                intent.putExtra("isRecent",false);
                                startActivity(intent);
                                finish();

                            }
                        }
                        else{
                            Intent intent = new Intent(Join_01.this, Join_02_nicName.class);
                            intent.putExtra("isFoundation",false);
                            intent.putExtra("clubUid",clubUid);
                            intent.putExtra("resume",joinclube_edittext_content.getText().toString());
                            intent.putExtra("isFreeSign",clubData.isFreeSign());
                            intent.putExtra("thisClubName",clubData.getThisClubName());
                            intent.putExtra("clubProfileUrl",clubData.getClubImageUrl());
                            startActivity(intent);
                            finish();
                            /**
                             * 그 모임이 별명제이면
                             * 별명 프로필 만드는곳으로 가서
                             * 거기서 업로드처리
                             */
                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialog2.show();
    }

}