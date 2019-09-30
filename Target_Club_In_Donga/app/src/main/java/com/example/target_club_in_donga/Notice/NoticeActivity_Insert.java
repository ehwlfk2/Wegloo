package com.example.target_club_in_donga.Notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.Package_LogIn.LoginData;
import com.example.target_club_in_donga.PushMessages.NotificationModel;
import com.example.target_club_in_donga.PushMessages.SendPushMessages;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class NoticeActivity_Insert extends AppCompatActivity{

    private FloatingActionButton activity_notice_insert_button_fontChange;
    private FloatingActionButton activity_notice_insert_button_colorChange;
    private FloatingActionButton activity_notice_insert_button_result;
    private EditText activity_notice_insert_edittext_title;
    private EditText activity_notice_insert_edittext_content;
    private Switch activity_notice_insert_switch;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private List<Notice_item_color> color_item = new ArrayList<>();
    private String type;
    private String dbKey;
    private String updateWriter;
    //private boolean updateSwitch;
    //private int nowColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_insert);


        activity_notice_insert_button_fontChange = (FloatingActionButton)findViewById(R.id.activity_notice_insert_button_fontChange);
        activity_notice_insert_button_colorChange = (FloatingActionButton)findViewById(R.id.activity_notice_insert_button_colorChange);
        activity_notice_insert_button_result = (FloatingActionButton)findViewById(R.id.activity_notice_insert_button_result);
        activity_notice_insert_edittext_title = (EditText)findViewById(R.id.activity_notice_insert_edittext_title);
        activity_notice_insert_edittext_content = (EditText)findViewById(R.id.activity_notice_insert_edittext_content);
        activity_notice_insert_switch = (Switch)findViewById(R.id.activity_notice_insert_switch);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");
        if(type.equals("update")){
            dbKey = intent.getExtras().getString("updateKey");
            database.getReference().child(clubName).child("Notice").child(dbKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Notice_Item update_item = dataSnapshot.getValue(Notice_Item.class);
                    activity_notice_insert_edittext_content.setText(update_item.getContent());
                    activity_notice_insert_switch.setChecked(update_item.isSwitchOnOff());
                    updateWriter = update_item.getWriter();
                    //updateSwitch = update_item.isSwitchOnOff();

                    SpannableStringBuilder ssb = new SpannableStringBuilder(update_item.getTitle());
                    for(int i=0;i<update_item.notice_item_colors.size();i++){
                        color_item.add(update_item.notice_item_colors.get(i));

                        int start = update_item.notice_item_colors.get(i).getStart();
                        int end = update_item.notice_item_colors.get(i).getEnd();
                        if(update_item.notice_item_colors.get(i).getStyle().equals("BOLD")){
                            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, 1);
                        }
                        else if(update_item.notice_item_colors.get(i).getStyle().equals("ITALIC")){
                            ssb.setSpan(new StyleSpan(Typeface.ITALIC), start, end, 1);
                        }
                        else if(update_item.notice_item_colors.get(i).getStyle().equals("UnderLine")){
                            ssb.setSpan(new UnderlineSpan(), start, end, 1);
                        }
                        else if(Integer.parseInt(update_item.notice_item_colors.get(i).getStyle()) == R.color.colorBlack){
                            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), start, end, 1);
                        }
                        else if(Integer.parseInt(update_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_alizarin){
                            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), start, end, 1);
                        }
                        else if(Integer.parseInt(update_item.notice_item_colors.get(i).getStyle()) == R.color.fbutton_color_belize_hole){
                            ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_belize_hole)), start, end, 1);
                        }
                        else{
                            int color = Integer.parseInt(update_item.notice_item_colors.get(i).getStyle());
                            ssb.setSpan(new ForegroundColorSpan(color), start, end, 1);
                        }
                    }
                    activity_notice_insert_edittext_title.setText(ssb);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        activity_notice_insert_edittext_title.setCustomSelectionActionModeCallback(new StyleCallback());


        activity_notice_insert_button_fontChange.setOnClickListener(new View.OnClickListener() { //글꼴 체인지 버튼 눌럿을때
            @Override
            public void onClick(View view) {
                initiatePopupWindow2();
            }
        });

        activity_notice_insert_button_colorChange.setOnClickListener(new View.OnClickListener() { //색깔 체인지 버튼
            @Override
            public void onClick(View view) {
                initiatePopupWindow();
            }
        });

        activity_notice_insert_button_result.setOnClickListener(new View.OnClickListener() { //올리기 눌럿을때
            @Override
            public void onClick(View view) {
                //final String title = activity_notice_insert_edittext_title.getText().toString();
                final String title = activity_notice_insert_edittext_title.getText().toString();
                //title.get
                //final CharacterStyle[] styleSpans2 = title.getSpans(0, title.length(), CharacterStyle.class);

                final String content = activity_notice_insert_edittext_content.getText().toString();
                if(title.length() <= 0){
                    Toast.makeText(NoticeActivity_Insert.this, "제목 입력해줘요", Toast.LENGTH_SHORT).show();
                }
                else if(content.length() <= 0){
                    Toast.makeText(NoticeActivity_Insert.this, "내용 입력해줘요", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(type.equals("insert")){ //추가
                        database.getReference().child(clubName).child("User").child(auth.getCurrentUser().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String uidName = dataSnapshot.getValue(String.class);

                                //Notice_Item notice_item = new Notice_Item(uidName,title,content,activity_notice_insert_switch.isChecked(), System.currentTimeMillis());
                                //
                                Notice_Item notice_item = new Notice_Item();
                                notice_item.setWriter(uidName);
                                notice_item.setContent(content);
                                notice_item.setSwitchOnOff(activity_notice_insert_switch.isChecked());
                                notice_item.setTitle(title.toString());
                                notice_item.notice_item_colors = color_item;
                                //notice_item.style = (Object[]) styleSpans2;
                                //notice_item.stars.put("title",title);

                                notice_item.setTimestamp(-1*System.currentTimeMillis());
                                database.getReference().child(clubName).child("Notice").push().setValue(notice_item);

                                if(activity_notice_insert_switch.isChecked()){
                                    SendPushMessages send = new SendPushMessages();
                                    send.multipleSendMessage("공지사항이 추가되었습니다",title.toString());
                                }
                                Toast.makeText(NoticeActivity_Insert.this, "공지 올렷스무디", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        if(activity_notice_insert_switch.isChecked()){
                            SendPushMessages send = new SendPushMessages();
                            send.multipleSendMessage("공지사항이 수정되었습니다",title.toString());
                        }

                        Notice_Item notice_item = new Notice_Item();
                        notice_item.setWriter(updateWriter);
                        notice_item.setContent(content);
                        notice_item.setSwitchOnOff(activity_notice_insert_switch.isChecked());
                        notice_item.setTitle(title.toString());
                        notice_item.notice_item_colors = color_item;
                        notice_item.setTimestamp(-1*System.currentTimeMillis());
                        database.getReference().child(clubName).child("Notice").child(dbKey).setValue(notice_item);
                        Toast.makeText(NoticeActivity_Insert.this, "수정완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
        });

    }



    private PopupWindow mDropdown2 = null;
    LayoutInflater mInflater2;

    private PopupWindow initiatePopupWindow2() {

        try {
            mInflater2 = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater2.inflate(R.layout.activity_notice_insert_popup2, null);

            //If you want to add any listeners to your textviews, these are two //textviews.
            //final TextView itema = (TextView) layout.findViewById(R.id.ItemA);
            //final TextView itemb = (TextView) layout.findViewById(R.id.ItemB);
            FloatingActionButton boldBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup2_bold);
            FloatingActionButton italicBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup2_italic);
            FloatingActionButton underlineBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup2_underline);
            FloatingActionButton replyBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup2_reply);

            final SpannableStringBuilder ssb2 = new SpannableStringBuilder(activity_notice_insert_edittext_title.getText());
            final int titleLen2 = activity_notice_insert_edittext_title.length();
            boldBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     ssb2.setSpan(new StyleSpan(Typeface.BOLD), 0, titleLen2, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb2);
                    Notice_item_color listcolor = new Notice_item_color();
                    listcolor.setStart(0);
                    listcolor.setEnd(titleLen2);
                    listcolor.setStyle("BOLD");
                    color_item.add(listcolor);
                }
            });
            italicBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ssb2.setSpan(new StyleSpan(Typeface.ITALIC), 0, titleLen2, 1); // Color
                    Notice_item_color listcolor = new Notice_item_color();
                    listcolor.setStart(0);
                    listcolor.setEnd(titleLen2);
                    listcolor.setStyle("ITALIC");
                    color_item.add(listcolor);
                    activity_notice_insert_edittext_title.setText(ssb2);
                }
            });
            underlineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ssb2.setSpan(new UnderlineSpan(), 0, titleLen2, 1); // Color
                    Notice_item_color listcolor = new Notice_item_color();
                    listcolor.setStart(0);
                    listcolor.setEnd(titleLen2);
                    listcolor.setStyle("UnderLine");
                    color_item.add(listcolor);
                    activity_notice_insert_edittext_title.setText(ssb2);
                }
            });
            replyBtn.setOnClickListener(new View.OnClickListener() { //전체 원래대로 되돌리기
                @Override
                public void onClick(View view) {
                    //ssb.delete(0,titleLen);
                    color_item.clear();
                    ssb2.clearSpans();
                    activity_notice_insert_edittext_title.setText(ssb2);
                    //ssb.clear();
                }
            });

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown2 = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,true);
            //Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            //mDropdown.setBackgroundDrawable(background);
            mDropdown2.showAsDropDown(activity_notice_insert_button_fontChange, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown2;

    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;

    private PopupWindow initiatePopupWindow() {

        try {
            mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.activity_notice_insert_popup, null);

            //If you want to add any listeners to your textviews, these are two //textviews.
            //final TextView itema = (TextView) layout.findViewById(R.id.ItemA);
            //final TextView itemb = (TextView) layout.findViewById(R.id.ItemB);
            FloatingActionButton blackBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup_black);
            FloatingActionButton redBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup_red);
            FloatingActionButton blueBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup_blue);
            FloatingActionButton plusBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup_plus);
            FloatingActionButton replyBtn = (FloatingActionButton)layout.findViewById(R.id.activity_notice_insert_popup_reply);

            final SpannableStringBuilder ssb = new SpannableStringBuilder(activity_notice_insert_edittext_title.getText());
            final int titleLen = activity_notice_insert_edittext_title.length();
            blackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.colorBlack));
                    //activity_notice_insert_edittext_title.setTextColor(getResources().getColor(R.color.colorBlack));
                    Notice_item_color listcolor = new Notice_item_color();
                    listcolor.setStart(0);
                    listcolor.setEnd(titleLen);
                    listcolor.setStyle(R.color.colorBlack+"");
                    color_item.add(listcolor);

                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, titleLen, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                }
            });
            redBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_alizarin));
                    Notice_item_color listcolor = new Notice_item_color();
                    listcolor.setStart(0);
                    listcolor.setEnd(titleLen);
                    listcolor.setStyle(R.color.fbutton_color_alizarin+"");
                    color_item.add(listcolor);
                    //activity_notice_insert_edittext_title.setTextColor(getResources().getColor(R.color.fbutton_color_alizarin));
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), 0, titleLen, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                }
            });
            blueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_belize_hole));
                    Notice_item_color listcolor = new Notice_item_color();
                    listcolor.setStart(0);
                    listcolor.setEnd(titleLen);
                    listcolor.setStyle(R.color.fbutton_color_belize_hole+"");
                    color_item.add(listcolor);
                    //activity_notice_insert_edittext_title.setTextColor(getResources().getColor(R.color.fbutton_color_belize_hole));
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_belize_hole)), 0, titleLen, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                }
            });
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //nowColor = activity_notice_insert_edittext_title.getCurrentTextColor();
                    openColorPicker(0,titleLen,ssb);
                    //activity_notice_insert_edittext_title.setTextColor(nowColor);

                }
            });
            replyBtn.setOnClickListener(new View.OnClickListener() { //전체 원래대로 되돌리기
                @Override
                public void onClick(View view) {
                    //ssb.delete(0,titleLen);
                    color_item.clear();
                    ssb.clearSpans();
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.colorBlack));
                    //ssb.clear();
                }
            });

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,true);
            //Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            //mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(activity_notice_insert_button_colorChange, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }


    public void openColorPicker(final int start, final int end, final SpannableStringBuilder ssb) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, activity_notice_insert_edittext_title.getCurrentTextColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                //nowColor = color;
                Notice_item_color listcolor = new Notice_item_color();
                listcolor.setStart(start);
                listcolor.setEnd(end);
                listcolor.setStyle(color+"");
                color_item.add(listcolor);

                activity_notice_insert_button_colorChange.setColorFilter(color);
                ssb.setSpan(new ForegroundColorSpan(color), start, end, 1); // Color
                activity_notice_insert_edittext_title.setText(ssb);
            }
        });
        colorPicker.show();
    }


    class StyleCallback implements ActionMode.Callback {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //Log.d(TAG, "onCreateActionMode");
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.notice_temp_menu, menu);
            menu.removeItem(android.R.id.selectAll);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //Log.d(TAG, String.format("onActionItemClicked item=%s/%d", item.toString(), item.getItemId()));
            CharacterStyle cs;
            int start = activity_notice_insert_edittext_title.getSelectionStart();
            int end = activity_notice_insert_edittext_title.getSelectionEnd();
            SpannableStringBuilder ssb = new SpannableStringBuilder(activity_notice_insert_edittext_title.getText());

            switch(item.getItemId()) {

                case R.id.bold:
                    cs = new StyleSpan(Typeface.BOLD);
                    ssb.setSpan(cs, start, end, 1);
                    activity_notice_insert_edittext_title.setText(ssb);

                    Notice_item_color listcolor = new Notice_item_color();
                    listcolor.setStart(start);
                    listcolor.setEnd(end);
                    listcolor.setStyle("BOLD");
                    color_item.add(listcolor);
                    return true;

                case R.id.italic:
                    cs = new StyleSpan(Typeface.ITALIC);
                    ssb.setSpan(cs, start, end, 1);
                    activity_notice_insert_edittext_title.setText(ssb);

                    Notice_item_color listcolor2 = new Notice_item_color();
                    listcolor2.setStart(start);
                    listcolor2.setEnd(end);
                    listcolor2.setStyle("ITALIC");
                    color_item.add(listcolor2);
                    return true;

                case R.id.underline:
                    cs = new UnderlineSpan();
                    ssb.setSpan(cs, start, end, 1);
                    activity_notice_insert_edittext_title.setText(ssb);

                    Notice_item_color listcolor3 = new Notice_item_color();
                    listcolor3.setStart(start);
                    listcolor3.setEnd(end);
                    listcolor3.setStyle("UnderLine");
                    color_item.add(listcolor3);

                    return true;

                case R.id.colorBlack:
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), start, end, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.colorBlack));

                    Notice_item_color listcolor4 = new Notice_item_color();
                    listcolor4.setStart(start);
                    listcolor4.setEnd(end);
                    listcolor4.setStyle(""+R.color.colorBlack);
                    color_item.add(listcolor4);

                    return true;
                case R.id.colorRed:
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), start, end, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_alizarin));

                    Notice_item_color listcolor5 = new Notice_item_color();
                    listcolor5.setStart(start);
                    listcolor5.setEnd(end);
                    listcolor5.setStyle(""+R.color.fbutton_color_alizarin);
                    color_item.add(listcolor5);

                    return true;
                case R.id.colorBlue:
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_belize_hole)), start, end, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_belize_hole));
                    Notice_item_color listcolor6 = new Notice_item_color();
                    listcolor6.setStart(start);
                    listcolor6.setEnd(end);
                    listcolor6.setStyle(""+R.color.fbutton_color_belize_hole);
                    color_item.add(listcolor6);
                    return true;
                case R.id.colorPlus:
                    //nowColor = activity_notice_insert_edittext_title.getCurrentTextColor();
                    openColorPicker(start,end,ssb);
                    return true;
                case R.id.reply:

                    ssb.clearSpans();
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.colorBlack));
                    color_item.clear();
                    return true;


            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    /*public void sendFcm(String toToken, String title, String text){
        Gson gson = new Gson();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to =  toToken;
        notificationModel.notification.title = title; //백그라운드
        notificationModel.notification.text = text;
        notificationModel.data.title = title; //포그라운드
        notificationModel.data.text = text;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAAN9u7iok:APA91bHiCw-fGchT3f4FDePrFXNtUQ0PpEBDZOtKuz6Az0x6gMgv2JEhVNcwKeOdJr1UWkX4JBYsShwkU2ZS00CyFNKqSet5JKJOBWxBxzy9Dh_--nbExEbPYWQCU9dwhfSaQqCeOfb3")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }*/
}
