package com.example.target_club_in_donga.Notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;

import com.example.target_club_in_donga.MainActivity;
import com.example.target_club_in_donga.R;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import yuku.ambilwarna.AmbilWarnaDialog;

import static java.security.AccessController.getContext;

public class NoticeActivity_Insert extends AppCompatActivity{
    private FloatingActionButton activity_notice_insert_button_fontChange;
    private FloatingActionButton activity_notice_insert_button_colorChange;
    private FloatingActionButton activity_notice_insert_button_result;
    private EditText activity_notice_insert_edittext_title;
    private EditText activity_notice_insert_edittext_content;
    private Switch activity_notice_insert_switch;
    private FirebaseDatabase database;
    private ActionMode mActionMode;
    private int editStart;
    private int editEnd;
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

        activity_notice_insert_edittext_title.setCustomSelectionActionModeCallback(new StyleCallback());


        activity_notice_insert_button_fontChange.setOnClickListener(new View.OnClickListener() { //글꼴 체인지 버튼 눌럿을때
            @Override
            public void onClick(View view) {

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
                String title = activity_notice_insert_edittext_title.getText().toString();
                String content = activity_notice_insert_edittext_content.getText().toString();
                if(title.length() <= 0){
                    Toast.makeText(NoticeActivity_Insert.this, "제목 입력해줘요", Toast.LENGTH_SHORT).show();
                }
                else if(content.length() <= 0){
                    Toast.makeText(NoticeActivity_Insert.this, "내용 입력해줘요", Toast.LENGTH_SHORT).show();
                }
                else{
                    Notice_Item notice_item = new Notice_Item(title,content,"green","san-serif",activity_notice_insert_switch.isChecked(), System.currentTimeMillis());
                    database.getReference().child("Notice").push().setValue(notice_item);
                    Toast.makeText(NoticeActivity_Insert.this, "공지 올렷스무디", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }


    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;

    private PopupWindow initiatePopupWindow() {

        try {
            mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.popop, null);

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
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), 0, titleLen, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                }
            });
            redBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_alizarin));
                    //activity_notice_insert_edittext_title.setTextColor(getResources().getColor(R.color.fbutton_color_alizarin));
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), 0, titleLen, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                }
            });
            blueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_belize_hole));
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

                    return true;

                case R.id.italic:
                    cs = new StyleSpan(Typeface.ITALIC);
                    ssb.setSpan(cs, start, end, 1);
                    activity_notice_insert_edittext_title.setText(ssb);
                    return true;

                case R.id.underline:
                    cs = new UnderlineSpan();
                    ssb.setSpan(cs, start, end, 1);
                    activity_notice_insert_edittext_title.setText(ssb);
                    return true;

                case R.id.colorBlack:
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlack)), start, end, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.colorBlack));
                    return true;
                case R.id.colorRed:
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_alizarin)), start, end, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_alizarin));
                    return true;
                case R.id.colorBlue:
                    ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbutton_color_belize_hole)), start, end, 1); // Color
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.fbutton_color_belize_hole));
                    return true;
                case R.id.colorPlus:
                    //nowColor = activity_notice_insert_edittext_title.getCurrentTextColor();
                    openColorPicker(start,end,ssb);
                    return true;
                case R.id.reply:

                    //일부 원래대로 되돌리기
                    ssb.clearSpans();
                    activity_notice_insert_edittext_title.setText(ssb);
                    activity_notice_insert_button_colorChange.setColorFilter(getResources().getColor(R.color.colorBlack));

                    return true;


            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
