package com.example.ufanet.home;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ufanet.R;
import com.example.ufanet.start.StartActivity;
import com.example.ufanet.utils.MemoryOperation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class myKeyDialogFragment extends BottomSheetDialogFragment {

    View view;
    public final Integer REQUEST_PERMISSION_CODE = 1;

    TextView myKeyTV;
    CardView shareButtonCV;
    CardView updateButtonCV;
    CardView copyButtonCV;

    MemoryOperation memoryOperation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_my_key, container,
                false);

        memoryOperation = new MemoryOperation(getContext());

        initUI();
        setListeners();

        myKeyTV.setText(memoryOperation.getTokenUser());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void initUI(){
        myKeyTV = view.findViewById(R.id.tv_my_key);
        shareButtonCV = view.findViewById(R.id.cv_share_button);
        updateButtonCV = view.findViewById(R.id.cv_update_button);
        copyButtonCV = view.findViewById(R.id.cv_copy_button);
    }

    void setListeners(){
        shareButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText();
            }
        });

        updateButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = StartActivity.generUserKey();
                System.out.println("Random String is: " + key);
                memoryOperation.setTokenUser(key);
                myKeyTV.setText(key);
            }
        });

        copyButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", memoryOperation.getTokenUser());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copy Key", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareText() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, "Мой ключик: " + memoryOperation.getTokenUser());
        String shareKeyTitle = String.valueOf("Ключ");
        startActivity(Intent.createChooser(share, shareKeyTitle));
    }
}