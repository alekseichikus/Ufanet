package com.example.ufanet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class profileFragments extends BottomSheetDialogFragment {

    Context mContext;
    SharedPreferences mSettings;
    AppCompatActivity appCompatActivity;

    public profileFragments(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container,
                false);
        TextView vvv = view.findViewById(R.id.name_item);

        return view;

    }
}