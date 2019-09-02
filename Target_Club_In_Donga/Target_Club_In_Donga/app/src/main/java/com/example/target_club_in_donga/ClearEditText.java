//package com.example.target_club_in_donga;
//
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.text.TextWatcher;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import androidx.appcompat.widget.AppCompatEditText;
//import androidx.core.content.ContextCompat;
//import androidx.core.graphics.drawable.DrawableCompat;
//
//public class ClearEditText extends AppCompatEditText {
//
//    private Drawable clearDrawable;
//    private OnFocusChangeListener onFocusChangeListener;
//    private OnTouchListener onTouchListener;
//
//    public ClearEditText(final Context context) {
//        super(context);
//        init();
//    }
//
//    public ClearEditText(final Context context, final AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public ClearEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    @Override
//    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
//        this.onFocusChangeListener = onFocusChangeListener;
//    }
//
//    @Override
//    public void setOnTouchListener(OnTouchListener onTouchListener) {
//        this.onTouchListener = onTouchListener;
//    }
//
//    private void init() {
//        Drawable tempDrawable = ContextCompat.getDrawable(getContext(), R.drawable.cancel);
//        clearDrawable = DrawableCompat.wrap(tempDrawable);
//        DrawableCompat.setTintList(clearDrawable,getHintTextColors());
//        clearDrawable.setBounds(0,0,clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
//
//        setClearIconVisible(false);
//
//        super.setOnTouchListener(this);
//        super.setOnFocusChangeListener(this);
//        addTextChangedListener(this);
//    }
//
//    @Override
//    protected void onFocusChange(final View view, final boolean hasFocus){
//        if(hasFocus){
//            setClearIconVisible(getText().length() > 0);
//        }else {
//            setClearIconVisible(false);
//        }
//
//        if(onFocusChangeListener != null){
//            onFocusChangeListener.onFocusChange(view, hasFocus);
//        }
//    }
//
//    @Override
//    public boolean onTouch(final View view, final MotionEvent motionEvent) {
//        final int x = (int) motionEvent.getX();
//        if(clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()){
//            if(motionEvent.getAction() == MotionEvent.ACTION_UP){
//                setError(null);
//                setText(null);
//            }
//            return true;
//        }
//        if(onTouchListener != null) {
//            return onTouchListener.onTouch(view, motionEvent);
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    protected final void onTextChanged(final CharSequence text, final int start, final int before, final int count) {
//        if(isFocused()){
//            setClearIconVisible(s.length() > 0);
//        }
//    }
//
//    @Override
//    public
//
//    private final void setClearIconVisible(boolean visible){
//        clearDrawable.setVisible(visible, false);
//        setCompoundDrawables(null,null,visible ? clearDrawable : null, null);
//    }
//}
