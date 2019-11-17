package com.example.target_club_in_donga;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private long backKeyPressedTime;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGide();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            //activity.finish();
            toast.cancel();
            activity.finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }

    public void showGide() {
        toast = Toast.makeText(activity, "'뒤로' 버튼을 한번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
