package com.example.target_club_in_donga.Package_LogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.target_club_in_donga.R;

public class Dialog_Find_Email {
    private Context context;

    public Dialog_Find_Email(Context context){
        this.context = context;
    }

    public void callFunction(final String username, final String useremail){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog__find__email);
        dialog.show();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.8f);
        int height = (int) (dm.heightPixels * 0.7f);

        Window window = dialog.getWindow();
        window.setLayout(width,height);


        final TextView userName = dialog.findViewById(R.id.found_username);
        final TextView userEmail = dialog.findViewById(R.id.found_email);
        final Button back = dialog.findViewById(R.id.found_back);

        userName.setText(username);
        userEmail.setText(useremail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
