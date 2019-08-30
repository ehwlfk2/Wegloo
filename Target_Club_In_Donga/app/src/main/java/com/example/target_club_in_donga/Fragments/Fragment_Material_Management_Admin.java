package com.example.target_club_in_donga.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.R;

public class Fragment_Material_Management_Admin extends Fragment {

    public Fragment_Material_Management_Admin() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_material_management_admin, container, false);
        return  view;
    } // activity_material_management_admin 에 있는 화면을 가지고 온다

}
