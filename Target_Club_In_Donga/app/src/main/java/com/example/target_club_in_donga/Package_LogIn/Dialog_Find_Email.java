package com.example.target_club_in_donga.Package_LogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.target_club_in_donga.R;

public class Dialog_Find_Email {
    private Context context;

    public Dialog_Find_Email(Context context){
        this.context = context;
    }

    public void callFunction(final String username, String useremail){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog__find__email);
        dialog.show();

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
