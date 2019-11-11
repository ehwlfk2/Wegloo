package com.example.target_club_in_donga.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.HomeActivity;
import com.example.target_club_in_donga.R;

public class TempClubSelectionActivity_Fragment extends Fragment implements HomeActivity.onKeyBackPressedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.temp_club_selection, container, false);

        return view;
    }

    @Override
    public void onBackKey() {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.setOnKeyBackPressedListener(null);
        homeActivity.onBackPressed();
    }

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        ((HomeActivity)context).setOnKeyBackPressedListener(this);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
