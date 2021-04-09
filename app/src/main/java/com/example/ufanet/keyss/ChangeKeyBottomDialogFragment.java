package com.example.ufanet.keyss;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.ufanet.R;
import com.example.ufanet.settings.IKey;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class ChangeKeyBottomDialogFragment extends BottomSheetDialogFragment {

    IKey key;
    LinearLayout deleteKeyLL;
    LinearLayout changeKeyLL;
    View view;

    public ChangeKeyBottomDialogFragment(IKey key) {
        this.key = key;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_fragment_func_change_key, container,
                false);
        init();
        setListeners();
        return view;
    }

    void init(){
        deleteKeyLL = view.findViewById(R.id.ll_delete_key);
        changeKeyLL = view.findViewById(R.id.ll_change_key);
    }
    void setListeners(){
        deleteKeyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

